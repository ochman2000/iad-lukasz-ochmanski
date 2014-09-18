package pl.lodz.p.iad;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import pl.lodz.p.iad.diagram.Voronoi5;
import pl.lodz.p.iad.diagram.Voronoi6;
import pl.lodz.p.iad.structure.KsiazkaKodowa;
import pl.lodz.p.iad.structure.Mapa;
import pl.lodz.p.iad.structure.Point;

public class NeuralGas extends Diagram {
	
	private static final String EPOCH_LOG_CSV = "resources/neuralgas/epoch_log.csv";
	private static final String EPOCH_LOG_TXT = "resources/neuralgas/epoch_log.txt";
	private static final int NUMBER_OF_NEURONS = 8;
	private static final double LEARNING_RATE = 2.0;
	private static final double RADIUS = 0.6;
	private static final int LIMIT_EPOK = 100;
	private static final boolean LOG = true;
	private static final double DRAW_STEP_IN_PERCENTS = 1.0;
	private static final boolean NORMALIZATION = true;
	private static final boolean WRITE_TO_FILE = true;
	
	public NeuralGas() {
		setEpochLogCSV(EPOCH_LOG_CSV);
		setEpochLogTxt(EPOCH_LOG_TXT);
		setNumberOfNeurons(NUMBER_OF_NEURONS);
		setLearningRate(LEARNING_RATE);
		setRadius(RADIUS);
		setLimitEpok(LIMIT_EPOK);
		setLog(LOG);
		setDrawStepInPercents(DRAW_STEP_IN_PERCENTS);
		setNormalization(NORMALIZATION);
		setWriteToFile(WRITE_TO_FILE);
		setEpochLog(new StringBuilder());
		setEpochCSV(new StringBuilder());
		getEpochCSV().append("epoka;promień sąsiedztwa;współczynnik uczenia;"
				+ "błąd kwantyzacji\r\n");
	}
	
	public NeuralGas(List<Integer> kolumny) {
		this();
		setKolumny(kolumny);
		teach();
	}
	
	public void teach() {
		if (isNormalized()) setHydra(getHydra().getNormalized());
		if (getWielkoscZbioruUczacego()==0)
			setWielkoscZbioruUczacego(getHydra().size());
		setNeurons(new ArrayList<Point>(getNumberOfNeurons()));
		setVoronoiBlackWhite(new Voronoi5());
		teach(getHydra());
	}
	
	private void teach(Mapa zbiorWejsciowy){
		// LOSUJ K NEURONÓW (ZAMIAST INICJALIZOWAĆ PRZYPADKOWYMI WARTOŚCIAMI)
		Random rnd = new Random();
		while (getNeurons().size() < getNumberOfNeurons()) {
			int indeks = rnd.nextInt(zbiorWejsciowy.size());
			Point centroid = zbiorWejsciowy.get(indeks);
			centroid.setColor(Optional.of(Color.getHSBColor(
					(float) Math.random(), .7f, .7f)));
			if (!getNeurons().contains(centroid)) {
				getNeurons().add(centroid);
			}
		}
		for (int epoka=0; epoka<getLimitEpok(); epoka++) {
			String msg = epoka + "\t"
					+ "Promień sąsiedztwa: "+ promienSasiedztwa(epoka)+"\t"
					+ "Wsp. uczenia:" + calcLearningFactor(epoka) +"\t";
			System.out.print(msg);
			getEpochLog().append(msg);
			getEpochCSV().append(epoka+";");
			getEpochCSV().append(promienSasiedztwa(epoka)+";");
			getEpochCSV().append(calcLearningFactor(epoka)+";");
			zbiorWejsciowy.shuffle();
			for(int i = 0 ; i< getWielkoscZbioruUczacego(); i++){
				Point inputVector = zbiorWejsciowy.get(i);
				setNeurons(sortNeuronsByDistanceAscending(inputVector));
				modifyNeuronsWeights(epoka, inputVector);				
			}
			//ZBUDUJ KSIĄŻKĘ KODOWĄ i WYLICZ BŁĄD
			double error = new KsiazkaKodowa(zbiorWejsciowy, getNeurons())
							.getBladKwantyzacji();
			
			msg = "error: " + error +"\r\n"
					+ "------------------------------------------------"
					+ "------------------------------------------------\r\n";
			System.out.print(msg);
			getEpochLog().append(msg);
			getEpochCSV().append(error+"\r\n");
			
			double drawJump = getLimitEpok() * (getDrawStepInPercents() / 100);
			if (epoka % drawJump == 0.0) {
				wizualizujObszaryVoronoia(getNeurons(), zbiorWejsciowy);
			}
		}
		rysujDiagramVoronoia(getNeurons(), zbiorWejsciowy);
		saveAndClose();
	}
	
	private void modifyNeuronsWeights(int iterNumber, Point inputVector){
		for(int dimm = 0 ; dimm<getNeurons().get(0).getCoordinates().size();dimm++){
			for(int i=0;i<getNeurons().size();i++){
				modifySingleNeuronWeight(i, dimm, iterNumber, inputVector);
			}
		}
	}
	
	public void modifySingleNeuronWeight(int neuronIndex, int dimm, int iterNumber, 
			Point inputVector){
		Point neuron = getNeurons().get(neuronIndex);
		double newCoordinate = neuron.getCoordinate(dimm)
				+ calcLearningFactor(iterNumber) 
				* calcNeighbourhoodFunc(iterNumber, neuronIndex) 
				* (inputVector.getCoordinate(dimm) - neuron.getCoordinate(dimm) );
		neuron.setCoordinate(dimm, newCoordinate);
	}
	
	public double calcLearningFactor(int iterNumber) {
		
		double wartoscPoczatkowa = getLearningRate();
		double kmax = getLimitEpok();
		double wartoscMinimalna = 0.01;
		
		double learningFactor = wartoscPoczatkowa * 
			Math.pow( (wartoscMinimalna/wartoscPoczatkowa),
					(iterNumber/kmax) );
		return learningFactor;
	}
	
	public double calcNeighbourhoodFunc(int iterNumber, int neuronIndex) {
		return Math.exp(-1 * neuronIndex/promienSasiedztwa(iterNumber));
	}
	
	private double promienSasiedztwa(int iterNumber){
		double wartoscPoczatkowa = getRadius();
		double kmax = getLimitEpok();
		double wartoscMinimalna = 0.01;
		double result = wartoscPoczatkowa *
			Math.pow( (wartoscMinimalna/wartoscPoczatkowa),
					(iterNumber/kmax) );
		return result;
	}
	
	public List<Point> sortNeuronsByDistanceAscending(Point inputVector) {	
		return getNeurons().parallelStream()
				.sorted(inputVector::compareByEuclideanDistance)
	            .collect(Collectors.toList());
	}
	
	protected void rysujDiagramVoronoia(List<Point> centroidy, Mapa mapa) {
		setVoronoiColor(new Voronoi6());
		super.rysujDiagramVoronoia(centroidy, mapa);
	}
}

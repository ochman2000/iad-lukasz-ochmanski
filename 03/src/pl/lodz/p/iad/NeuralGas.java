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
	
	protected String EPOCH_LOG_CSV = "resources/neuralgas/epoch_csv.txt";
	protected String EPOCH_LOG_TXT = "resources/neuralgas/epoch_log.txt";
	protected int NUMBER_OF_NEURONS = 8;
	protected double LEARNING_RATE = 2.0;
	protected double RADIUS = 0.6;
	protected int LIMIT_EPOK = 100;
	protected boolean LOG = true;
	protected double DRAW_STEP_IN_PERCENTS = 1.0;
	protected boolean NORMALIZATION = false;
	protected boolean WRITE_TO_FILE = true;
	
	public NeuralGas() {
		epochLog = new StringBuilder();
		epochCSV = new StringBuilder();
		epochCSV.append("epoka;promień sąsiedztwa;współczynnik uczenia;"
				+ "błąd kwantyzacji\r\n");
	}
	
	public NeuralGas(List<Integer> kolumny) {
		this();
		setKolumny(kolumny);
		teach();
	}
	
	public void teach() {
		if (NORMALIZATION) hydra = hydra.getNormalized();
		if (wielkoscZbioruUczacego==0)
			wielkoscZbioruUczacego = hydra.size();
		neurons = new ArrayList<Point>(NUMBER_OF_NEURONS);
		voronoiBlackWhite = new Voronoi5();
		teach(hydra);
	}
	
	private void teach(Mapa map){
		// LOSUJ K NEURONÓW (ZAMIAST INICJALIZOWAĆ PRZYPADKOWYMI WARTOŚCIAMI)
		Random rnd = new Random();
		while (neurons.size() < NUMBER_OF_NEURONS) {
			int indeks = rnd.nextInt(hydra.size());
			Point centroid = hydra.get(indeks);
//			if (NORMALIZATION) { centroid = centroid.getNormalized(); }
			centroid.setColor(Optional.of(Color.getHSBColor(
					(float) Math.random(), .7f, .7f)));
			if (!neurons.contains(centroid)) {
				neurons.add(centroid);
			}
		}
		for (int epoka=0; epoka<LIMIT_EPOK; epoka++) {
			String msg = epoka + "\t"
					+ "Promień sąsiedztwa: "+ promienSasiedztwa(epoka)+"\t"
					+ "Wsp. uczenia:" + calcLearningFactor(epoka) +"\t";
			System.out.print(msg);
			epochLog.append(msg);
			epochCSV.append(epoka+";");
			epochCSV.append(promienSasiedztwa(epoka)+";");
			epochCSV.append(calcLearningFactor(epoka)+";");
			hydra.shuffle();
			for(int i = 0 ; i< wielkoscZbioruUczacego; i++){
				Point inputVector = hydra.get(i);
				neurons = sortNeuronsByDistanceAscending(inputVector);
				modifyNeuronsWeights(epoka, inputVector);				
			}
			//ZBUDUJ KSIĄŻKĘ KODOWĄ i WYLICZ BŁĄD
			double error = new KsiazkaKodowa(hydra, neurons).getBladKwantyzacji();
			
			msg = "error: " + error +"\r\n"
					+ "------------------------------------------------"
					+ "------------------------------------------------\r\n";
			System.out.print(msg);
			epochLog.append(msg);
			epochCSV.append(error+"\r\n");
			
			double drawJump = LIMIT_EPOK * (DRAW_STEP_IN_PERCENTS / 100);
			if (epoka % drawJump == 0.0) {
				wizualizujObszaryVoronoia(neurons, hydra);
			}
		}
		rysujDiagramVoronoia(neurons, hydra);
		saveAndClose();
	}
	
	private void modifyNeuronsWeights(int iterNumber, Point inputVector){
		for(int dimm = 0 ; dimm<neurons.get(0).getCoordinates().size();dimm++){
			for(int i=0;i<neurons.size();i++){
				modifySingleNeuronWeight(i, dimm, iterNumber, inputVector);
			}
		}
	}
	
	public void modifySingleNeuronWeight(int neuronIndex, int dimm, int iterNumber, 
			Point inputVector){
		Point neuron = neurons.get(neuronIndex);
		double newCoordinate = neuron.getCoordinate(dimm)
				+ calcLearningFactor(iterNumber) 
				* calcNeighbourhoodFunc(iterNumber, neuronIndex) 
				* (inputVector.getCoordinate(dimm) - neuron.getCoordinate(dimm) );
		neuron.setCoordinate(dimm, newCoordinate);
	}
	
	public double calcLearningFactor(int iterNumber) {
		
		double wartoscPoczatkowa = LEARNING_RATE;
		double kmax = LIMIT_EPOK;
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
		double wartoscPoczatkowa = RADIUS;
		double kmax = LIMIT_EPOK;
		double wartoscMinimalna = 0.01;
		double result = wartoscPoczatkowa *
			Math.pow( (wartoscMinimalna/wartoscPoczatkowa),
					(iterNumber/kmax) );
		return result;
	}
	
	public List<Point> sortNeuronsByDistanceAscending(Point inputVector) {	
		List<Point> sortedNeurons = neurons.parallelStream()
				.sorted(inputVector::compare)
	            .collect(Collectors.toList());
		return sortedNeurons;
	}
	
	protected void rysujDiagramVoronoia(List<Point> centroidy, Mapa mapa) {
		voronoiColor = new Voronoi6();
		super.rysujDiagramVoronoia(centroidy, mapa);
	}
}

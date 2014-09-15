package pl.lodz.p.iad;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
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

public class NeuralGas{
	
	private int NUMBER_OF_NEURONS = 8;
	private double LEARNING_RATE = 2.0;
	private double RADIUS = 0.6;
	private int LIMIT_EPOK = 100;
	private boolean LOG = true;
	private double DRAW_STEP_IN_PERCENTS = 1.0;
	private boolean WRITE_TO_FILE = true;
	private boolean NORMALIZATION = true;
	
	@SuppressWarnings("unused")
	private int erasLimit = 1;
	@SuppressWarnings("unused")
	private double networkAlpha = 0.01;
	@SuppressWarnings("unused")
	private double networkMomentum = 0.1;
	private int wielkoscZbioruUczacego = 0;
	private List<Point> neurons;
	private Mapa hydra;
	private Voronoi5 voronoi;
	private StringBuilder epochLog;
	private StringBuilder epochCSV;
	private List<Integer> kolumny;
	
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
		voronoi = new Voronoi5();
		teach(hydra);
	}
	
	public void teach(Mapa map){
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
		
		if (LOG) {
			Charset charset = StandardCharsets.UTF_8;
			try {
				BufferedWriter epochLogWriterTxt = Files.newBufferedWriter(
						Paths.get("resources/neuralgas/epoch_log.txt"), charset);
				BufferedWriter epochLogWriterCsv = Files.newBufferedWriter(
						Paths.get("resources/neuralgas/epoch_log.csv"), charset);
				epochLogWriterTxt.write(epochLog.toString());
				epochLogWriterCsv.write(epochCSV.toString());
				epochLogWriterTxt.close();
				epochLogWriterCsv.close();
			} catch (IOException x) {
				System.err.format("IOException: %s%n", x);
			}
		}
		System.out.println("Program terminated.");
	}
	
	private void wizualizujObszaryVoronoia(List<Point> centroidy, Mapa mapa) {
		voronoi.clear();
		for (Point point : mapa) {
			voronoi.dodajKropkę(point.getCoordinate(0), point.getCoordinate(1));
		}
		for (Point centroid : centroidy) {
			voronoi.dodajCentroid(
					centroid.getCoordinate(0),
					centroid.getCoordinate(1),
					centroid.getColor().orElseThrow(
							IllegalArgumentException::new));
		}
		voronoi.drawMe();
		if (WRITE_TO_FILE) {
			voronoi.saveVornoiToFile();
		}
	}
	
	private void rysujDiagramVoronoia(List<Point> centroidy, Mapa mapa) {
		Voronoi6 voronoi6 = new Voronoi6();
		for (Point point : mapa) {
			voronoi6.dodajKropkę(point.getCoordinate(0), point.getCoordinate(1));
		}
		for (Point centroid : centroidy) {
			voronoi6.dodajCentroid(
					centroid.getCoordinate(0),
					centroid.getCoordinate(1),
					centroid.getColor().orElseThrow(
							IllegalArgumentException::new));
		}
		voronoi6.drawMe();
		if (WRITE_TO_FILE) {
			voronoi6.saveVornoiToFile();
		}
	}
	private void modifyNeuronsWeights(int iterNumber, Point inputVector){
		//modify weights of each neuron
		//for each dimension
		for(int dimm = 0 ; dimm<neurons.get(0).getCoordinates().size();dimm++){
			//for each neuron
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
	
	
	public List<Point> sortNeuronsByDistanceAscending(Point inputVector){	
		List<Point> sortedNeurons = neurons.parallelStream().sorted((n1, n2)
			-> Double.compare(n1.getEuclideanDistanceFrom(inputVector),
							n2.getEuclideanDistanceFrom(inputVector)))
            .collect(Collectors.toList());
		return sortedNeurons;
	}
	
	public void setErasLimit(int newErasLimit){
		erasLimit = newErasLimit;
	}
	
	public void setNeuronsAmount(int neuronsAmount) {
		NUMBER_OF_NEURONS = neuronsAmount;
	}
	
	public void setNetworkAlpha(double newNetworkAlpha) {
		networkAlpha = newNetworkAlpha;		
	}
	
	public void setNetworkMomentum(double newNetworkMomentum) {
		networkMomentum = newNetworkMomentum;
	}
	
	public void writeToFile(boolean write) {
		WRITE_TO_FILE= write;
	}
	public void setDRAW_STEP_IN_PERCENT(int newDRAW_STEP_IN_PERCENT) {
		DRAW_STEP_IN_PERCENTS = newDRAW_STEP_IN_PERCENT;
	}
	
	public void setKolumny(List<Integer> kolumny) {
		this.kolumny = kolumny;
		hydra = new Mapa(this.kolumny);
	}
}

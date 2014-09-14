package pl.lodz.p.iad;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.TreeMap;

import pl.lodz.p.iad.diagram.Voronoi5;
import pl.lodz.p.iad.diagram.Voronoi6;
import pl.lodz.p.iad.structure.KsiazkaKodowa;
import pl.lodz.p.iad.structure.Mapa;
import pl.lodz.p.iad.structure.Point;

public class NeuralGas{
	
	private int NUMBER_OF_NEURONS = 10;
	private double LEARNING_RATE = 1.0;
	private double RADIUS = 0.6;
	private int LIMIT_EPOK = 100;
	private boolean LOG = true;
	private double DRAW_STEP_IN_PERCENTS = 1.0;
	private boolean WRITE_TO_FILE = true;
	private boolean NORMALIZATION = false;
	
	private int erasLimit = 1;
	private double networkAlpha = 0.01;
	private double networkMomentum = 0.1;
	private List<Point> learnPattern = new ArrayList<Point>();
	private int wielkoscZbioruUczacego = 0;
	private List<Point> neurons = new ArrayList<Point>();
	private Mapa hydra;
	private Voronoi5 voronoi;
	private StringBuilder epochLog;
	private StringBuilder epochCSV;
	private List<Integer> kolumny;
	
	public NeuralGas() {
		epochLog = new StringBuilder();
		epochCSV = new StringBuilder();
		epochCSV.append("epoka;promień sąsiedztwa;współczynnik uczenia;błąd kwantyzacji\r\n");
	}
	
	public NeuralGas(List<Integer> kolumny) {
		this();
		setKolumny(kolumny);
		teach();
	}
	
	public void teach(){
		if (NORMALIZATION) hydra = hydra.getNormalized();
		if (wielkoscZbioruUczacego==0)
			wielkoscZbioruUczacego = hydra.size();
		voronoi = new Voronoi5();
		
		// LOSUJ K NEURONÓW (ZAMIAST INICJALIZOWAĆ PRZYPADKOWYMI WARTOŚCIAMI)
		Random rnd = new Random();
		while (neurons.size() < NUMBER_OF_NEURONS) {
			int indeks = rnd.nextInt(hydra.size());
			Point centroid = hydra.get(indeks);
//					if (NORMALIZATION) { centroid = centroid.getNormalized(); }
			centroid.setColor(Optional.of(Color.getHSBColor(
					(float) Math.random(), .7f, .7f)));
			if (!neurons.contains(centroid)) {
				neurons.add(centroid);
			}
		}
		for (int epoka=0; epoka<LIMIT_EPOK; epoka++) {
			System.out.print(epoka+ "\t");
			epochLog.append(epoka+ "\t");
			epochCSV.append(epoka+";");
			hydra.shuffle();
			for(int i = 0 ; i< wielkoscZbioruUczacego; i++){
				Point inputVector = hydra.get(i);
				neurons = sortNeuronsByDistanceAscending(inputVector);
				modifyNeuronsWeights(epoka, inputVector);				
			}
			//ZBUDUJ KSIĄŻKĘ KODOWĄ i WYLICZ BŁĄD
			double error = new KsiazkaKodowa(hydra, neurons).getBladKwantyzacji();
			
			String msg = "error: " + error +"\r\n"
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
			Path fileOut02 = Paths.get("resources/neuralgas/epoch_log.txt");
			Path fileOut04 = Paths.get("resources/neuralgas/epoch_log.csv");
			try {
				BufferedWriter epochLogWriterTxt = Files.newBufferedWriter(fileOut02, charset);
				BufferedWriter epochLogWriterCsv = Files.newBufferedWriter(fileOut04, charset);
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
		double newCoordinate = neuron.getCoordinate(dimm) + calcLearningFactor(iterNumber) 
				* calcNeighbourhoodFunc(iterNumber, neuronIndex) 
				* (inputVector.getCoordinate(dimm) - neuron.getCoordinate(dimm) );
		neuron.setCoordinate(dimm, newCoordinate);
	}
	
	public double calcLearningFactor(int iterNumber) {
		
		double wartoscPoczatkowa = LEARNING_RATE;
		double kmax = wielkoscZbioruUczacego;
		double wartoscMinimalna = 0.01;
		
		double learningFactor = wartoscPoczatkowa * 
				Math.pow(
						(wartoscMinimalna / wartoscPoczatkowa), (iterNumber / kmax)
						);
		
		return learningFactor;
	}
	
	public double calcNeighbourhoodFunc(int iterNumber, int neuronIndex) {
		return Math.exp(-1 * neuronIndex / promienSasiedztwa(iterNumber));
	}
	
	private double promienSasiedztwa(int iterNumber){
		//def promienSasiedztwa(k):
		  double wartoscPoczatkowa = RADIUS;
		  double kmax = wielkoscZbioruUczacego;
		  double wartoscMinimalna = 0.01;
		  
		  double result = wartoscPoczatkowa *
				  Math.pow(
						  (wartoscMinimalna / wartoscPoczatkowa), (iterNumber / kmax)
						  );
		  return result;
	}
	
	
	public List<Point> sortNeuronsByDistanceAscending(Point inputVector){
		List<Point> sortedNeurons = new ArrayList<Point>();	
		Map<Double, Point> neuronsWithDistances = new TreeMap<Double, Point>();
		
		for(Point neuron : neurons){
			Double distance = calculateDistanceEuclides(neuron,inputVector);
			neuronsWithDistances.put(distance, neuron);
		}
		for (Double str : neuronsWithDistances.keySet()) {
		    sortedNeurons.add(neuronsWithDistances.get(str));
		}		
		return sortedNeurons;
	}
	
	private double calculateDistanceEuclides(Point neuron, Point inputVector){
		return neuron.getEuclideanDistanceFrom(inputVector);
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

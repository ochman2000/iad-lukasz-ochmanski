package pl.lodz.p.iad;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.TreeMap;

import p.lodz.p.iad.functions.IdentityFunction;
import p.lodz.p.iad.som.SOM;
import pl.lodz.p.iad.diagram.Voronoi2;
import pl.lodz.p.iad.diagram.Voronoi3;
import pl.lodz.p.iad.structure.Mapa;
import pl.lodz.p.iad.structure.Point;

public class NeuralGas{

	private List<Point> neurons = new ArrayList<Point>();
	private List<Point> learnPattern = new ArrayList<Point>();
	
	private static int PODZBIORY = 3;
	private static double LEARNING_RATE = 0.1;
	private static int LICZBA_ITERACJI = 0;
	private static double drawStepPercent = 10.0;
	private static boolean writeToFile = false;
	private List<Double> ksiazkaKodowa;
	
	private static int erasLimit = 1;
	private static double networkAlpha = 0.01;
	private static double networkMomentum = 0.1;
	
	private Mapa hydra;
	
	private Voronoi2 voronoi;
	
	
	//private Voronoi2 voronoi;
	
	public NeuralGas(List<Integer> kolumny) {
		hydra = new Mapa(kolumny);
//		hydra = hydra.getNormalized();
//		hydra = hydra.getScaled(0.02);
		if (LICZBA_ITERACJI == 0)
			LICZBA_ITERACJI = hydra.size();
		ksiazkaKodowa = new ArrayList<Double>(LICZBA_ITERACJI);
		Random rnd = new Random();
		//neurony = new ArrayList<Point>(PODZBIORY);
		voronoi = new Voronoi2(512, 512, 0);

		// LOSUJ K NEURONÓW (ZAMIAST INICJALIZOWAĆ PRZYPADKOWYMI WARTOŚCIAMI)
		while (neurons.size() < PODZBIORY) {
			int indeks = rnd.nextInt(hydra.size());
			Point centroid = hydra.get(indeks);
			centroid.setColor(Optional.of(Color.getHSBColor((float) Math.random(), .7f, .7f)));
			if (!neurons.contains(centroid)) {
				neurons.add(centroid);
			}
		}
		System.out.print("Wylosowane neurony:\t");
		for (Point point : neurons) {
			System.out.print(point + "\t");
		}
		System.out.println("\n");

		// ROZPOCZNIJ PROCES PRZESUWANIA NEURONÓW
		//List<Point> noweNeurony = przesunNeuronyZwycieskie(hydra, neurony);
		int counter = 0;

		// while (!pozycjeSaTakieSame(neurony, noweNeurony)) {
		// System.out.println("Iteracja:\t"+ ++counter);
		// neurony = noweNeurony;
		// noweNeurony = przesunNeuronyZwycieskie(hydra, neurony);
		// System.out.println("Współrzędne neuronów: \t"+noweNeurony);
		// }
		// rysujDiagramVoronoia(noweNeurony, hydra);
	}
	
	public void startTeachAlgorithm(){
		
		hydra.shuffle();
		
		for(int i = 0 ; i< LICZBA_ITERACJI; i++){
			Point inputVector = hydra.get(i);
			neurons = sortNeuronsByDistanceAscending(inputVector);
			
			modifyNeuronsWeights(i,inputVector);
			System.out.println("iter  "+ i +" end");
			
			double drawJump = LICZBA_ITERACJI * (drawStepPercent / 100);
			System.out.println("drawJump : " + drawJump);
			System.out.println("iter : " + i);
			System.out.println("mod  : " + i % drawJump);
			if (i % drawJump == 0.0) {
				wizualizujObszaryVoronoia(neurons, hydra);
				/*System.out.println("" + i + "\t learnRate: "+learnRate 
						+ "\t lambda: "+ lambda	);
						*/
			}
			
		}
		//for end
		//rysujDiagramVoronoia(neurons, hydra);
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
		if (writeToFile) {
			voronoi.saveVornoiToFile();
		}
	}
	
	private void rysujDiagramVoronoia(List<Point> centroidy, Mapa mapa) {
		Voronoi3 voronoi3= new Voronoi3();
		for (Point point : mapa) {
			voronoi3.dodajKropkę(point.getCoordinate(0), point.getCoordinate(1));
		}
		for (Point centroid : centroidy) {
			voronoi3.dodajCentroid(
					centroid.getCoordinate(0),
					centroid.getCoordinate(1),
					centroid.getColor().orElseThrow(
							IllegalArgumentException::new));
		}
		voronoi3.drawMe();
		if (writeToFile) {
			voronoi3.saveVornoiToFile();
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
	
	public void modifySingleNeuronWeight(int neuronIndex, int dimm, int iterNumber, Point inputVector){
		Point neuron = neurons.get(neuronIndex);
		//System.out.println("modifySingleNeuronWeight neuron "+ neuronIndex +" for dimm : " +dimm+  ": " + neuron.getCoordinate(dimm));
		double newCoordinate = neuron.getCoordinate(dimm) + calcLearningFactor(iterNumber) * calcNeighbourhoodFunc(iterNumber,neuronIndex) * (inputVector.getCoordinate(dimm) - neuron.getCoordinate(dimm) );
		//System.out.println("newCoordinate : " + newCoordinate);
		neuron.setCoordinate(dimm, newCoordinate);		
		//neurony[wi].coords[k] += (wspUczenia(float(i)) * funkcjaSasiedztwa(float(i), wi) * (inputPoint.coords[k] - (neurony[wi].coords[k])))
	}
	
	public double calcLearningFactor( int iterNumber){
		
		double wartoscPoczatkowa = 1;
		double kmax = LICZBA_ITERACJI;
		double wartoscMinimalna = 0.01;
		
		double learningFactor = wartoscPoczatkowa * 
				Math.pow(
						(wartoscMinimalna / wartoscPoczatkowa), (iterNumber / kmax)
						);
		
		return learningFactor;
		
	}
	
	public double calcNeighbourhoodFunc( int iterNumber, int neuronIndex){
		//Calculate the exponential of all elements in the input array.
		//Obliczyć potęgę naturalną o wykładniku wszystkich elementów tablicy wejściowej.
		//-1 * neuronIndex / promienSasiedztwa(iterNumber)
		//return numpy.exp(-1 * nrNeuronu / promienSasiedztwa(k))
		double result =  Math.exp(-1 * neuronIndex / promienSasiedztwa(iterNumber));
		
		return result;
	}
	
	private double promienSasiedztwa(int iterNumber){
		//def promienSasiedztwa(k):
		  double wartoscPoczatkowa = 0.9;
		  double kmax = LICZBA_ITERACJI;
		  double wartoscMinimalna = 0.01;
		  
		  double result = wartoscPoczatkowa *
				  Math.pow(
						  (wartoscMinimalna / wartoscPoczatkowa), (iterNumber / kmax)
						  );
		
		  return result;
	}
	
	
	public List<Point> sortNeuronsByDistanceAscending(Point inputVector){
		List<Point> sortedNeurons = new ArrayList<Point>();	
		Map<Double, Point> neuronsWithDistances = new HashMap<Double, Point>();
		
		for(Point neuron : neurons){
			Double distance = calculateDistance(neuron,inputVector);
			neuronsWithDistances.put(distance, neuron);
		}
		//sorting
		Map<Double, Point> treeMap = new TreeMap<Double, Point>(neuronsWithDistances);
		System.out.println("want to get sorted distance list : ");
		for (Double str : treeMap.keySet()) {
		    System.out.print(str + " / ");
		    sortedNeurons.add(neuronsWithDistances.get(str));
		}
		System.out.println(" ");
		return sortedNeurons;
	}
	
	private double calculateDistance(Point neuron, Point inputVector){		
		double[] wieghtsDiff = new double[inputVector.getCoordinates().size()];
		
		for(int i=0; i< inputVector.getCoordinates().size(); i++){
			wieghtsDiff[i] = neuron.getCoordinate(i) - inputVector.getCoordinate(i);
		}
		
		double distance = calculateVectorLength(wieghtsDiff);
		
		return distance;
	}
	
	
	private double calculateVectorLength(double[] vector){
		double result = 0;
		
		for(double weight : vector){
			result += (Math.pow(weight, 2));
		}
		
		return Math.sqrt(result);
	}
	
	public static void setErasLimit(int newErasLimit){
		erasLimit = newErasLimit;
	}
	
	public static void setNeuronsAmount(int neuronsAmount) {
		PODZBIORY = neuronsAmount;
	}
	
	public static void setNetworkAlpha(double newNetworkAlpha) {
		networkAlpha = newNetworkAlpha;		
	}
	
	public static void setNetworkMomentum(double newNetworkMomentum) {
		networkMomentum = newNetworkMomentum;
	}
	
	public static void setIterLimit(int limit) {
		LICZBA_ITERACJI = limit;
	}
	public static void writeToFile(boolean write) {
		writeToFile = write;
	}
	public static void setDrawStepPercent(int newDrawStepPercent) {
		drawStepPercent = newDrawStepPercent;
	}
	/*
	@Override
	public void teach(int epochs) throws IOException {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void initPoints(int size) {
		double []weights = new double[size];
        for (int i = 0; i < pointCounter; i++) {
            for(int j=0;j<size;j++) {
                weights[j] = Math.random();
            }
            neurons.add(new Point(new IdentityFunction(), weights.clone()));
        }
		
	}*/
}

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
import pl.lodz.p.iad.structure.Mapa;
import pl.lodz.p.iad.structure.Point;

public class NeuralGas extends SOM{

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
		//voronoi = new Voronoi2(512, 512, 0);

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
		}
		
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
		
	}
}

package pl.lodz.p.iad;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pl.lodz.p.iad.diagram.Voronoi;
import pl.lodz.p.iad.structure.Mapa;
import pl.lodz.p.iad.structure.Point;

public class Kohonen {
	
	/**
	 * Określa liczbę K centroidów.
	 */
	private static final int PODZBIORY = 3;
	private static final double LICZBA_ITERACJI = 1_000;
	private static final double RADIUS = 1.0;
	private static final double LEARNING_RATE* Math.exp(-(i/LICZBA_ITERACJI)); = 0;

	public Kohonen(List<Integer> kolumny) {
		Mapa mapa = new Mapa(kolumny);
		Random rnd = new Random();
		List<Point> neurony = new ArrayList<Point>(PODZBIORY);
		
		//LOSUJ K NEURONÓW
		while (neurony.size()<PODZBIORY) {
			int indeks = rnd.nextInt(mapa.size());
			Point centroid = mapa.get(indeks);
			if (!neurony.contains(centroid)) {
				centroid.setGroup(centroid);
				neurony.add(centroid);
			}
		}
		System.out.print("Wylosowane neurony:\t");
		for (Point point : mapa) {
			if (point.isCentroid())
			System.out.print(point+ "\t");
		}
		System.out.println("\n");
		
		//ROZPOCZNIJ PROCES PRZESUWANIA NEURONÓW
		List<Point> noweNeurony = przesunNeuronyZwycieskie(mapa, neurony);
		int counter=0;
		
		while (!pozycjeSaTakieSame(neurony, noweNeurony)) {
			System.out.println("Iteracja:\t"+ ++counter);
			neurony = noweNeurony;
			noweNeurony = przesunNeuronyZwycieskie(mapa, neurony);
			System.out.println("Współrzędne neuronów: \t"+noweNeurony);
		}
		rysujDiagramVoronoia(noweNeurony, mapa);
	}
	
	private void rysujDiagramVoronoia(List<Point> centroidy, Mapa mapa) {
		Voronoi voronoi = new Voronoi(512, 512, 0);
		for (Point centroid : centroidy) {
			voronoi.dodajCentroid(centroid.getCoordinate(0),
					centroid.getCoordinate(1));
		}
		
		for (Point point : mapa) {			
			voronoi.dodajKropkę(point.getCoordinate(0),
					point.getCoordinate(1));
		}
		System.out.println(mapa.get(0).getCoordinates().size());
		if (mapa.get(0).getCoordinates().size()>2) {
			Voronoi voronoi2 = new Voronoi(512, 512, 0);
			for (Point centroid : centroidy) {
				voronoi2.dodajCentroid(centroid.getCoordinate(0),
						centroid.getCoordinate(2));
			}
			
			for (Point point : mapa) {			
				voronoi2.dodajKropkę(point.getCoordinate(0),
						point.getCoordinate(2));
			}
			
			Voronoi voronoi3 = new Voronoi(512, 512, 0);
			for (Point centroid : centroidy) {
				voronoi3.dodajCentroid(centroid.getCoordinate(1),
						centroid.getCoordinate(2));
			}
			
			for (Point point : mapa) {			
				voronoi3.dodajKropkę(point.getCoordinate(1),
						point.getCoordinate(2));
			}
		}
	}
	
	private boolean pozycjeSaTakieSame(List<Point> centroidy,
			List<Point> noweCentroidy) {
		for (int i = 0; i < centroidy.size(); i++) {
			if (!centroidy.get(i).equals(noweCentroidy.get(i)))
				return false;
		}
		return true;
	}

	
	/**
	 * Klasyczny algorytm Kohonena zakłada adaptację wag jedynie neuronu
	 * zwycięskiego i neuronów znajdujących się nie dalej niż \lambda (czyli
	 * promienia sąsiedztwa). Oprócz tego należy zaimplementować adaptację z
	 * wykorzystaniem gaussowskiej funkcji sąsiedztwa, określającej współczynnik
	 * nauki neuronów przegrywających rywalizację w funkcji ich odległości od
	 * zwycięscy. Ponadto należy uwzględnić zjawisko pojawiania się martwych
	 * neuronów uwzględniając aktywność neuronów w procesie uczenia.
	 */
	private List<Point> przesunNeuronyZwycieskie(Mapa map, List<Point> neurony) {
		List<Point> noweNeurony = new ArrayList<Point>(PODZBIORY);
		Random rnd = new Random();
		for (int i = 0; i < LICZBA_ITERACJI; i++) {
			Point input = map.get(rnd.nextInt(map.size()));
			Point zwyciezca = getZwyciezca(neurony, input);
			double lambda = getPromienSasiedztwa(i);
			double learnRate = LEARNING_RATE* Math.exp(-(i/LICZBA_ITERACJI));
			for (Point neuron : neurony) {
				//punkty znajdujące się w promieniu sąsiedztwa od inputu czy zwyciezcy?
				if(neuron.getOutput(p)<lambda){
					int wymiar=0;
					for (double waga : neuron.getCoordinates()) {
						double dist = neuron.getOutput(zwyciezca);
						double gauss = Math.exp(-((dist*dist)/(2*(lambda*lambda))));
						double nowaWaga = waga + gauss*learnRate*(
								Math.abs(input.getCoordinate(wymiar))-waga);
						wymiar++;
					}
				}
			}
		}
	}
	
	private boolean isInNeighborhood(double promien) {
		
	}
	
	private Point getZwyciezca(List<Point> neurony, Point input) {
		double min = Double.MAX_VALUE;
		Point winner = null;
		for (Point point : neurony) {
			double xyz = point.getOutput(input);
			if (xyz<min) {
				min = xyz;
				winner = point;
			}
		}
		return winner;
	}
	
	private double getPromienSasiedztwa(int iteracja) {
		double wspolczynnik = LICZBA_ITERACJI/(Math.log(RADIUS));
		return RADIUS * Math.exp(-(iteracja/wspolczynnik));
	}
	

}
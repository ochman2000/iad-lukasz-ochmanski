package pl.lodz.p.iad;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pl.lodz.p.iad.diagram.Voronoi;
import pl.lodz.p.iad.structure.Mapa;
import pl.lodz.p.iad.structure.Point;

public class Kohonen {
	
	/**
	 * Określa liczbę K centroidów, czyli liczbę klastrów. Tyle właśnie
	 * neuronów będzie w warstwie ukrytej. Jeśli ma to być lattice 40x40
	 * powinieneś wpisać tutaj wartość 160;
	 */
	private static final int PODZBIORY = 3;
	private static final double LEARNING_RATE = 0.0;
	private static double LICZBA_ITERACJI = 0.0;

	public Kohonen(List<Integer> kolumny) {
		Mapa hydra = new Mapa(kolumny);
		hydra = hydra.getNormalized();
		if (LICZBA_ITERACJI==0.0) LICZBA_ITERACJI = hydra.size();
		Random rnd = new Random();
		List<Point> neurony = new ArrayList<Point>(PODZBIORY);
		
		//LOSUJ K NEURONÓW (ZAMIAST INICJALIZOWAĆ PRZYPADKOWYMI WARTOŚCIAMI)
		while (neurony.size()<PODZBIORY) {
			int indeks = rnd.nextInt(hydra.size());
			Point centroid = hydra.get(indeks);
			if (!neurony.contains(centroid)) {
				neurony.add(centroid);
			}
		}
		System.out.print("Wylosowane neurony:\t");
		for (Point point : neurony) {
			System.out.print(point+ "\t");
		}
		System.out.println("\n");
		
		//ROZPOCZNIJ PROCES PRZESUWANIA NEURONÓW
		List<Point> noweNeurony = przesunNeuronyZwycieskie(hydra, neurony);
		int counter=0;
		
		while (!pozycjeSaTakieSame(neurony, noweNeurony)) {
			System.out.println("Iteracja:\t"+ ++counter);
			neurony = noweNeurony;
			noweNeurony = przesunNeuronyZwycieskie(hydra, neurony);
			System.out.println("Współrzędne neuronów: \t"+noweNeurony);
		}
		rysujDiagramVoronoia(noweNeurony, hydra);
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
	 * zwycięzcy. Ponadto należy uwzględnić zjawisko pojawiania się martwych
	 * neuronów uwzględniając aktywność neuronów w procesie uczenia.
	 */
	private List<Point> przesunNeuronyZwycieskie(Mapa trainingSet, List<Point> neurony) {
		//ZRÓB DEEP COPY OF THE ARRAYLIST
		List<Point> noweNeurony = Kohonen.deepCopy(neurony);
		//RANDOMIZE TRAINING SET
		trainingSet.shuffle();
		for (int i = 0; i < LICZBA_ITERACJI; i++) {
			Point input = trainingSet.get(i);
			Point zwyciezca = getZwyciezca(noweNeurony, input);
			double lambda = getPromienSasiedztwa(i);
			double learnRate = LEARNING_RATE* Math.exp(-(i/LICZBA_ITERACJI));
			for (Point neuron : noweNeurony) {
				//stopień uaktywnienia neuronów z sąsiedztwa zależy od odległości
				//ich wektorów wagowych od wag neuronu wygrywającego.
				double dist = neuron.getEuclideanDistanceFrom(zwyciezca);
				if(dist<lambda){
					for (int wymiar=0; wymiar<neuron.getCoordinates().size();
							wymiar++) {
						double gauss = Math.exp(-((dist*dist)/(2*(lambda*lambda))));
						double waga = neuron.getCoordinate(wymiar);
						//czy ten wzór ma sens? to jest waga? czy output?
						double nowaWaga = waga + gauss*learnRate*(
								Math.abs(input.getCoordinate(wymiar))-waga);
						neuron.setCoordinate(wymiar, nowaWaga);
					}
				}
			}
		}
		return noweNeurony;
	}
	
	public static List<Point> deepCopy(List<Point> lista) {
		List<Point> copy = new ArrayList<Point>();
		for (Point point : lista) {
			copy.add(point.clone());
		}
		return copy;
	}
	
	/**
	 * Metoda zwraca neuron nabliższy podanemu wektorowi wejściowemu, uwzględniając
	 * przy tym liczbę zwycięstw. Neurony, które zwyciężały w przeszłości są karane
	 * poprzez zawyżanie ich odległości euklidesowej, a gdy liczba zwycięstw jest
	 * zbyt duża, muszą dodatkowo pauzować przez jeden cykl uczenia.
	 * 
	 * @param neurony
	 * @param input
	 * @return
	 */
	private Point getZwyciezca(List<Point> neurony, Point input) {
		double min = Double.MAX_VALUE;
		Point winner = null;
		for (Point point : neurony) {
			if (point.getWon()<0) {
				//PRZYWRÓĆ NEURON DO RYWALIZACJI, ALE TYM RAZEM MUSI PAUZOWAĆ
				point.odnotujZwyciestwo();
			}
			else {
				double xyz = point.getEuclideanDistanceFrom(input);
				xyz = (point.getWon()+1) * xyz;
				if (xyz<min) {
					min = xyz;
					winner = point;
				}
			}
		}
		winner.odnotujZwyciestwo();
		return winner;
	}
	
	private double getPromienSasiedztwa(int iteracja) {
		double radius = Math.sqrt(PODZBIORY)/2;
		if (radius<1) radius=1; 
		double wspolczynnik = LICZBA_ITERACJI/(Math.log(radius));
		double lambda = radius * Math.exp(-(iteracja/wspolczynnik));
		if (lambda>radius || lambda<1)
			throw new RuntimeException("Lambda out of range: "+lambda);
		return lambda;
	}
}
package pl.lodz.p.iad;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import pl.lodz.p.iad.structure.Mapa;
import pl.lodz.p.iad.structure.Point;

public class Kmeans {
	
	private static final int PODZBIORY = 3;

	public Kmeans() {
		Mapa mapa = new Mapa();
		Random rnd = new Random();
		Set<Point> centroidy = new HashSet<Point>(PODZBIORY);
		
		//LOSUJ K CENTROIDÓW
		while (centroidy.size()<PODZBIORY) {
			int indeks = rnd.nextInt(mapa.size());
			Point centroid = mapa.get(indeks);
			if (!centroidy.contains(centroid)) {
				centroid.setGroup(centroid);
				centroidy.add(centroid);
			}
		}
		System.out.println("Udało się.");
		for (Point point : mapa) {
			if (point.isCentroid())
			System.out.println(point);
		}
	}
	public static void main(String[] args) {
		new Kmeans();
	}
}

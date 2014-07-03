package pl.lodz.p.iad;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import pl.lodz.p.iad.structure.Mapa;
import pl.lodz.p.iad.structure.Point;

public class Kmeans {
	
	private static final int PODZBIORY = 3;
	private Mapa mapa;
	private Set<Point> centroidy;

	public Kmeans() {
		mapa = new Mapa();
		Random rnd = new Random();
		centroidy = new HashSet<Point>(PODZBIORY);
		
		//LOSUJ K CENTROIDÓW
		while (centroidy.size()<PODZBIORY) {
			int indeks = rnd.nextInt(mapa.size());
			Point centroid = mapa.get(indeks);
			if (!centroidy.contains(centroid)) {
				centroid.setGroup(centroid);
				centroidy.add(centroid);
			}
		}
		System.out.println("Losowanie zakończone powodzeniem.");
		for (Point point : mapa) {
			if (point.isCentroid())
			System.out.println(point);
		}
		
		grupujPunkty();
		System.out.println("Grupowanie centroidów zakończone powodzeniem.");
		
	}
	
	/**
	 * Dla wszystkich punktów, które nie są centroidami, sprawdź odległości tych punktów
	 * od każdego centroida i wybierz ten centroid, którego odległość od tego punktu jest
	 * najmniejsza. Następnie przypisz ten punkt do grupy tego centroida.
	 * Złożoność obliczeniowa: O(p*c), gdzie p to liczba punktów (10000) a c to liczba
	 * centroidów (3).
	 */
	private void grupujPunkty() {
		for (Point punkt : mapa) {
			if (punkt.isCentroid()==false) {
				double min = Double.MAX_VALUE;
				for (Point centroid : centroidy) {
					double dist = punkt.getDistanceFrom(centroid);
					if (dist<min) {
						min=dist;
						punkt.setGroup(centroid);
					}
				}
			}
		}
	}
	
	/**
	 * Dla każdej grupy centroidów, wylicz średnią dla każdego wymiaru, biorąc pod
	 * uwagę tylko te elementy, które nie są centroidami i należą do danej grupy centroida.
	 * Następnie przesuń centroida w każdą z możliwych wymiarów o tą wyliczoną wartość.
	 */
	private void przesunCentroidy() {
		for (Point centroid : centroidy) {
			for (int i=0; i<centroid.getCoordinates().size(); i++) {
				double sum = 0.0;
				int count = 0;
				for (Point punkt : mapa) {
					if (punkt.isCentroid()==false) {
						if (punkt.getGroup()==centroid) {
							sum +=punkt.getCoordinate(i);
							count++;
						}
					}
				}
				double x = sum/count;
				centroid.setCoordinate(i, x);
			}
		}
	}
	
	public static void main(String[] args) {
		new Kmeans();
	}
}

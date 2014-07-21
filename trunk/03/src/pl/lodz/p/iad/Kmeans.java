package pl.lodz.p.iad;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pl.lodz.p.iad.diagram.Voronoi;
import pl.lodz.p.iad.structure.Mapa;
import pl.lodz.p.iad.structure.Point;

public class Kmeans {
	
	/**
	 * Określa liczbę K centroidów.
	 */
	private static final int PODZBIORY = 3;

	public Kmeans(Mapa mapa) {
		Random rnd = new Random();
		List<Point> centroidy = new ArrayList<Point>(PODZBIORY);
		
		//LOSUJ K CENTROIDÓW
		while (centroidy.size()<PODZBIORY) {
			int indeks = rnd.nextInt(mapa.size());
			Point centroid = mapa.get(indeks);
			if (!centroidy.contains(centroid)) {
				centroid.setGroup(centroid);
				centroidy.add(centroid);
			}
		}
		System.out.print("Wylosowane centroidy:\t");
		for (Point point : mapa) {
			if (point.isCentroid())
			System.out.print(point+ "\t");
		}
		System.out.println("\n");
		
		//WYLICZ OPTYMALNE POZYCJE CENTROIDÓW
		mapa = grupujPunkty(mapa, centroidy);
		List<Point> noweCentroidy = przesunCentroidy(mapa, centroidy);
		int counter=0;
		
		while (!centroidySaIdentyczne(centroidy, noweCentroidy)) {
			System.out.println("Iteracja:\t"+ ++counter);
			centroidy = noweCentroidy;
			mapa = grupujPunkty(mapa, centroidy);
			noweCentroidy = przesunCentroidy(mapa, centroidy);
			System.out.println("Współrzędne centroidów: \t"+noweCentroidy);
		}
		rysujDiagramVoronoia(noweCentroidy, mapa);
//		System.out.println(mapa);
	}
	
	private void rysujDiagramVoronoia(List<Point> centroidy, Mapa mapa) {
		Voronoi voronoi = new Voronoi(512, 512, 0);
		for (Point centroid : centroidy) {
			voronoi.dodajCentroid(centroid);
		}
		
		for (Point point : mapa) {			
			voronoi.dodajKropkę(point);
		}
	}
	
	private boolean centroidySaIdentyczne(List<Point> centroidy,
			List<Point> noweCentroidy) {
		for (int i = 0; i < centroidy.size(); i++) {
			if (!centroidy.get(i).equals(noweCentroidy.get(i)))
				return false;
		}
		return true;
	}

	/**
	 * Dla wszystkich punktów, które nie są centroidami, sprawdź odległości tych punktów
	 * od każdego centroida i wybierz ten centroid, którego odległość od tego punktu jest
	 * najmniejsza. Następnie przypisz ten punkt do grupy tego centroida.
	 * Złożoność obliczeniowa: O(p*c), gdzie p to liczba punktów (10000) a c to liczba
	 * centroidów (3).
	 */
	private Mapa grupujPunkty(Mapa mapa, List<Point> centroidy) {
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
		return mapa;
	}
	
	/**
	 * Dla każdej grupy centroidów, wylicz średnią dla każdego wymiaru, biorąc pod uwagę
	 * tylko te elementy, które nie są centroidami i należą do danej grupy centroida.
	 * Następnie przesuń centroida w każdą z możliwych wymiarów o tą wyliczoną wartość.
	 */
	private List<Point> przesunCentroidy(Mapa mapa, List<Point> centroidy) {
		List<Point> noweCentroidy = new ArrayList<Point>(PODZBIORY);
		for (Point centroid : centroidy) {
			Point nowyCentroid = new Point(centroid.getCoordinates().size());
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
				nowyCentroid.addCoordinate(i, x);
				nowyCentroid.setGroup(nowyCentroid);
			}
			noweCentroidy.add(nowyCentroid);
		}
		return noweCentroidy;
	}
}

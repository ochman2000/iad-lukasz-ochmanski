package pl.lodz.p.iad.structure;

import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

public class KsiazkaKodowa extends HashMap<Point, Point> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6526246833472874969L;

	public KsiazkaKodowa(int size) {
		super(size);
	}
	
	public KsiazkaKodowa(Mapa zbiorWejsciowy, List<Point> neurony) {
		this(zbiorWejsciowy.size());
		for (Point input : zbiorWejsciowy) {
			Point teoretycznyZwyciezca = klasyfikuj(neurony, input);
			this.put(input, teoretycznyZwyciezca); 
		}
	}
	
	public Point get(Point p) {
		Point r = super.get(p);
		if (r==null) throw new NoSuchElementException("Brak wpisu w książce kodowej.");
		return r;
	}
	
	public double getBlad(Point p) {
		Point winner = this.get(p);
		if (winner==null) throw new NoSuchElementException("Brak wpisu w książce kodowej.");
		return p.getDistanceFrom(winner);
	}

	public double getBladKwantyzacji() {
		double sum = this.keySet().parallelStream()
				.mapToDouble(x -> this.getBlad(x))
				.sum();
		return sum / this.size();
	}
	
	/**
	 * Zwraca centroida (wektor Voronoia) dla danego sygnału wejściowego. Metoda
	 * używana do testowania wektora wejściowego. W przeciwieństwie do metody
	 * getZwyciezca(), nie uwzględnia zmęczenia neuronów i nie bierze pod uwagę
	 * ilości zwycięstw, kar, nie faworyzuje martwych neuronow. Jest to prosty
	 * klasyfikator.
	 */
	public static Point klasyfikuj(List<Point> neurony, Point input) {
		double min = Double.MAX_VALUE;
		Point winner = null;
		for (Point n : neurony) {
			double xyz = n.getEuclideanDistanceFrom(input);
			if (xyz < min) {
				min = xyz;
				winner = n;
			}
		}
		return winner;
	}
}

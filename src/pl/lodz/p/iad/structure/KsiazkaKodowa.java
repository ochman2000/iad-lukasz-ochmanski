package pl.lodz.p.iad.structure;

import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

public class KsiazkaKodowa extends HashMap<Point, Point> {

	private static final long serialVersionUID = 6526246833472874969L;

	public KsiazkaKodowa(int size) {
		super(size);
	}
	
	/**
	 * Wylicza średni błąd kwantyzacji sieci.
	 * @param zbiorWejsciowy jest to zbiór wektorów wejściowych (hydra).
	 * @param neurony jest to zbiór neuronów (centroidów).
	 */
	public KsiazkaKodowa(Mapa zbiorWejsciowy, List<Point> neurony) {
		this(zbiorWejsciowy.size());
		for (Point input : zbiorWejsciowy) {
			this.put(input, klasyfikuj(neurony, input));
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
		return this.keySet().parallelStream()
			.mapToDouble(this::getBlad)
			.average().getAsDouble();
	}
	
	/**
	 * Zwraca centroida (wektor Voronoia) dla danego sygnału wejściowego. Metoda
	 * używana do testowania wektora wejściowego. W przeciwieństwie do metody
	 * getZwyciezca(), nie uwzględnia zmęczenia neuronów i nie bierze pod uwagę
	 * ilości zwycięstw, kar, nie faworyzuje martwych neuronow. Jest to prosty
	 * klasyfikator.
	 */
	public static Point klasyfikuj(List<Point> neurony, Point input) {
		return neurony.parallelStream().min(input::compareByEuclideanDistance).get();
	}
}

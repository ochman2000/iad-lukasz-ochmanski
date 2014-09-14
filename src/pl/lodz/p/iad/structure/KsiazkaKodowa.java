package pl.lodz.p.iad.structure;

import java.util.HashMap;
import java.util.NoSuchElementException;

public class KsiazkaKodowa extends HashMap<Point, Point> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6526246833472874969L;

	public KsiazkaKodowa(int size) {
		super(size);
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
}

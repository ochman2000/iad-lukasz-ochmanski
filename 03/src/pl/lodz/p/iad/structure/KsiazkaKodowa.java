package pl.lodz.p.iad.structure;

import java.util.HashMap;

public class KsiazkaKodowa extends HashMap<Point, Point> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6526246833472874969L;

	public KsiazkaKodowa(int size) {
		super(size);
	}
	
	public double getBlad(Point p) {
		return p.getDistanceFrom(this.get(p));
	}

	public double getBladKwantyzacji() {
		double sum = this.values().parallelStream()
				.mapToDouble(p -> p.getDistanceFrom(this.get(p)))
				.sum();
		return sum / this.size();
	}
}

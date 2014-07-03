package pl.lodz.p.iad.structure;

import java.util.ArrayList;
import java.util.List;

public class Point {
	private List<Double> coordinates;
	private Point group;

	public Point(int size) {
		coordinates = new ArrayList<Double>(size);
	}

	public List<Double> getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(List<Double> coordinates) {
		this.coordinates = coordinates;
	}

	public void addCoordinate(int dimension, double value) {
		this.getCoordinates().add(dimension, value);
	}

	// public void setCoordinate(int dimension, double value) {
	// this.getCoordinates().set(dimension, value);
	// }

	public double getCoordinate(int dimension) {
		return this.getCoordinates().get(dimension);
	}

	public Point getGroup() {
		return group;
	}

	public void setGroup(Point group) {
		this.group = group;
	}

	public boolean isCentroid() {
		return this.getGroup() == this;
	}

	public String toString() {
		String dodatkowy = this.isCentroid() ? "\nJest centroidem"
				: "\nnależy do grupy centroida: " + this.getCoordinates();
		if (this.getGroup()==null) dodatkowy = "\nNie jest przypisany do żadnej grupy centroidów.";
		return this.getCoordinates() + dodatkowy;
	}
	
	public double getDistanceFrom(Point p) {
		double odlegloscX = p.getCoordinate(0) - this.getCoordinate(0);
		double odlegloscY = p.getCoordinate(1) - this.getCoordinate(1);
		double a = Math.pow(odlegloscX, 2);
	    double b = Math.pow(odlegloscY, 2);
	    double c = Math.sqrt(a + b);
	    return c;
	}
}

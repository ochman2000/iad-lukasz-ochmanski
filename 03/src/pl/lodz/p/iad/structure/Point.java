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

	 public void setCoordinate(int dimension, double value) {
		 this.getCoordinates().set(dimension, value);
	 }

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
		if (this.getCoordinates().size()!=p.getCoordinates().size()) {
			throw new IllegalArgumentException("Punkty mają niezgodną ilość wymiarów.");
		}
	    double sum = 0.0;
	    for (int i=0; i<this.getCoordinates().size(); i++) {
			double odleglosc = p.getCoordinate(i) - this.getCoordinate(i);
			double pq = Math.pow(odleglosc, 2);
		    sum+=pq;
	    }
	    return Math.sqrt(sum);
	}
	
	@Override
    public int hashCode() {
		double sum = 0.0;
		for (double coordinate : getCoordinates()) {
			sum+=coordinate;
			sum++;
		}
		return (int) sum;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj.hashCode()==this.hashCode()) {
			return true;
		}
		return false;
	}
}

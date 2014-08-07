package pl.lodz.p.iad.structure;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.ranges.RangeException;

public class Point {
	
	/**
	 * Used in hashCode().
	 * Determines how precise should two points be.
	 * PRECISION = 1_000_000 means that 0.000_000_1 is equal to 0.000_000_2
	 */
	private final int PRECISION = 1_000_000; 
	
	private List<Double> coordinates;
	private Point group;

	public Point(int size) {
		coordinates = new ArrayList<Double>(size);
	}

	public List<Double> getCoordinates() {
		return coordinates;
	}
	
	public List<Double> getCoordinatesTrimmed() {
		List<Double> d = new ArrayList<Double>();
		for (int i=0; i<this.getCoordinates().size(); i++) {
			BigDecimal bd = new BigDecimal(this.getCoordinate(i));
		    bd = bd.setScale(2, RoundingMode.HALF_UP);
			d.add(i, bd.doubleValue());
		}
		return d;
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
		@SuppressWarnings("unused")
		String info = this.isCentroid() ? "Jest centroidem"
				: "należy do grupy centroida: " + this.getCoordinatesTrimmed();
		if (this.getGroup()==null) info = "Nie jest przypisany do żadnej grupy centroidów.";
		return ""+this.getCoordinatesTrimmed(); // + " : " + info;
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
	
	public double getOutput(Point p) {
		double value = getNormalizationFactor()*getDistanceFrom(p);
		value = (value+1)/2;
		if (value<-1.0 || value>1.0)
			throw new IllegalStateException("Outpu of a neuron out of range.");
		return value;
	}
	
	private double getNormalizationFactor() {
		double vectorLength = 0.0;
		for (Double coord : this.getCoordinates()) {
			vectorLength += coord*coord;
		}
		return 1/(Math.sqrt(vectorLength));
	}
	
	@Override
    public int hashCode() {
		double sum = 0.0;
		for (double coordinate : getCoordinates()) {
			sum+=coordinate;
			sum++;
		}
		return (int) (sum*PRECISION);
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
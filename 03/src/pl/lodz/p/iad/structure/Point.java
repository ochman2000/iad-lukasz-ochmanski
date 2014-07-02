package pl.lodz.p.iad.structure;

import java.util.ArrayList;
import java.util.List;


public class Point {
	private List<Double> coordinates;
	
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
	
	public void getCoordinate(int dimension) {
		this.getCoordinate(dimension);
	}
}

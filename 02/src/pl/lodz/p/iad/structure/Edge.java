package pl.lodz.p.iad.structure;

import java.util.Random;

public class Edge {
	double input;
	double weight;
	
	public Edge() {
		Random random = new Random();
		double rnd = random.nextDouble();
		this.setWeight(rnd);
	}

	public double getInput() {
		return input;
	}

	public double getWeight() {
		return weight;
	}
	
	public void setWeight(double weight) {
		this.weight = weight;
	}
}
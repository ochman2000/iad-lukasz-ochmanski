package pl.lodz.p.iad.structure;

import java.util.Random;

public class Edge {
	private Neuron prev;
	private Neuron next;
	private double weight;
	private double previousDelta = 0.0;
	
	public Edge() {
		Random random = new Random();
		double rnd = random.nextDouble();
		this.setWeight(rnd);
	}

	public double getWeight() {
		return weight;
	}
	
//	public void incrementWeight(double delta) {
//		this.setPreviousDelta(delta);
//		double current = this.getWeight();
//		this.setWeight(current + delta);
//	}
	
	public void setWeight(double weight) {
		this.weight = weight;
	}

	public Neuron getPrev() {
		return prev;
	}

	public void setPrev(Neuron prev) {
		this.prev = prev;
	}

	public Neuron getNext() {
		return next;
	}

	public void setNext(Neuron next) {
		this.next = next;
	}

	public double getPreviousDelta() {
		return previousDelta;
	}

	public void setPreviousDelta(double previousDelta) {
		this.previousDelta = previousDelta;
	}
}
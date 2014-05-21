package pl.lodz.p.iad.structure;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Neuron {
	private List<Neuron> input;
	private List<Neuron> output;
	private double weightOut;
	
	public Neuron() {
		this(2,2);
	}
	public Neuron(int in, int out) {
		input = new ArrayList<Neuron>(in);
		output = new ArrayList<Neuron>(out);
		Random random = new Random();
		double rnd = random.nextDouble();
		this.setWeightOut(rnd);
	}

	public List<Neuron> getInputs() {
		return input;
	}

	public void setInputs(List<Neuron> input) {
		this.input = input;
	}

	public List<Neuron> getOutputs() {
		return output;
	}

	public void setOutputs(List<Neuron> output) {
		this.output = output;
	}
	
	public void addNeuronOut(Neuron neuron) {
		neuron.getInputs().add(this);
		this.getOutputs().add(neuron);
	}
	
	public double getLocalIn() {
		double localIn = 0.0;
		for (Neuron input : this.getInputs()) {
			double prevOut = input.getLocalOut();
			double weight = input.getWeightOut();
			double product = prevOut * weight;
			localIn+=product;
		}
		return localIn;
	}

	public double getLocalOut() {
		double x = this.getLocalIn();
		double localOut = this.sigmoidFunction(x) * x;
		return localOut;
	}
	
	private double sigmoidFunction(double x) {
		if (x < -45.0)
			return 0.0;
		else if (x > 45.0)
			return 1.0;
		else
			return 1.0 / (1.0 + Math.exp(-x));
	}
	public double getWeightOut() {
		return weightOut;
	}
	public void setWeightOut(double weightOut) {
		this.weightOut = weightOut;
	}
	
}

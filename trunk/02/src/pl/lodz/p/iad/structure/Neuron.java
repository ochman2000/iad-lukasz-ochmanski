package pl.lodz.p.iad.structure;

import java.util.ArrayList;
import java.util.List;


public class Neuron {
	private List<Edge> input;
	private List<Edge> output;
	private double localIn;
	private double localOut;
	
	public Neuron() {
		this(2,2);
	}
	public Neuron(int in, int out) {
		input = new ArrayList<Edge>(in);
		output = new ArrayList<Edge>(out);
	}

	public List<Edge> getInput() {
		return input;
	}

	public void setInput(List<Edge> input) {
		this.input = input;
	}

	public List<Edge> getOutput() {
		return output;
	}

	public void setOutput(List<Edge> output) {
		this.output = output;
	}
	
	public void addNeuronOut(Neuron neuron) {
		Edge edge = new Edge();
		neuron.getInput().add(edge);
//		int lastIndex = neuron.getInput().size()-1;
//		this.getOutput().add(neuron.getInput().get(lastIndex));
		this.getOutput().add(edge);
	}
	public double getLocalIn() {
		return localIn;
	}
	public void setLocalIn(double localIn) {
		this.localIn = localIn;
	}
	public double getLocalOut() {
		return localOut;
	}
	public void setLocalOut(double localOut) {
		this.localOut = localOut;
	}
}

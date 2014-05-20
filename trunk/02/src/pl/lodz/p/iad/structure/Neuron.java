package pl.lodz.p.iad.structure;

import java.util.List;


public class Neuron {
	private List<Edge> input;
	private Edge output;
	
	public Edge getOutput() {
		return output;
	}
	public void setOutput(Edge out) {
		this.output = out;
	}
	public List<Edge> getInputs() {
		return input;
	}
	public void setInputs(List<Edge> input2) {
		this.input = input2;
	}
}

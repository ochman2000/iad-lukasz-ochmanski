package pl.lodz.p.iad.structure;


public class Neuron {
	private Edge[] input;
	private double output;
	
	public double getOutput() {
		return output;
	}
	public void setOutput(double output) {
		this.output = output;
	}
	public Edge[] getInputs() {
		return input;
	}
	public void setInputs(Edge[] input2) {
		this.input = input2;
	}
}

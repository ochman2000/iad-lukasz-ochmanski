package pl.lodz.p.iad.structure;

import java.util.ArrayList;
import java.util.List;


public class InputLayerNeuron implements Neuron {
	private List<Edge> input;
	private List<Edge> output;
	private double localIn;
	private double localOut;
	private String ID;
	private Layer layer;
	
	@Override
	public String getID() {
		return ID;
	}

	@Override
	public void setID(String iD) {
		ID = iD;
	}

	public InputLayerNeuron() {
		this(2,2);
	}

	public InputLayerNeuron(int in, int out) {
		input = new ArrayList<Edge>(in);
		output = new ArrayList<Edge>(out);
	}

	@Override
	public List<Edge> getInputs() {
		return input;
	}

	@Override
	public void setInputs(List<Edge> input) {
		this.input = input;
	}

	@Override
	public List<Edge> getOutputs() {
		return output;
	}

	@Override
	public void setOutputs(List<Edge> output) {
		this.output = output;
	}
	
	@Override
	public void addNeuronOut(Neuron neuron) {
		Edge edge = new Edge();
		
		neuron.getInputs().add(edge);
		edge.setNext(neuron);
			
		this.getOutputs().add(edge);
		edge.setPrev(this);
	}
	
	@Override
	public double getLocalIn() {
		return localIn;
	}

	@Override
	public double getLocalOut() {
		return localOut;
	}
	
	@Override
	public void setLocalOut(double localOut) {
		this.localOut = localOut;
	}
	
	public String toString() {
		String id = this.getID()==null ? "Uknown neuron" : this.getID(); id+="\n";
		String bias = "Bias: null\n";
		String momentum = "Momentum: " + this.getLayer().getNetwork().getMomentum() + "\n";
		String in = "Wejście: " + this.getLocalIn() + "\n";
		String out = "Wyjście: " + this.getLocalOut() + "\n";
		
		return 	"=======================================\n" +
				id+bias+momentum+in+out+		
				"=======================================\n";
	}

	@Override
	public Edge getInput(int index) {
		return this.getInputs().get(index);
	}

	@Override
	public Edge getOutput(int index) {
		return this.getOutputs().get(index);
	}

	@Override
	public void setLayer(Layer layer) {
		this.layer = layer;
	}

	@Override
	public Layer getLayer() {
		return layer;
	}
	
	@Override
	public double getGradient() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setGradient(double gradient) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public double getBias() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setBias(double bias) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setPreviousDelta(double previousDelta) {
		throw new UnsupportedOperationException();
	}

	@Override
	public double getPreviousDelta() {
		throw new UnsupportedOperationException();
	}
}

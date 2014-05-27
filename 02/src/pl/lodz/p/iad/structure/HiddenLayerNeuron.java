package pl.lodz.p.iad.structure;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HiddenLayerNeuron implements Neuron {
	private List<Edge> input;
	private List<Edge> output;
	private String ID;
	private Layer layer;
	private double gradient;
	private double bias;
	private double previousDelta = 0.0;
	
	@Override
	public String getID() {
		return ID;
	}

	@Override
	public void setID(String iD) {
		ID = iD;
	}

	public HiddenLayerNeuron() {
		this(2,2);
	}

	public HiddenLayerNeuron(int in, int out) {
		input = new ArrayList<Edge>(in);
		output = new ArrayList<Edge>(out);
		Random random = new Random();
		double rnd = random.nextDouble()*4.0-2.0;
		this.setBias(rnd);
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
		double localIn = 0.0;
		for (Edge input : this.getInputs()) {
			double prevOut = input.getPrev().getLocalOut();
			double weight = input.getWeight();
			double product = prevOut * weight;
			localIn+=product;
		}
		return localIn+this.getBias();
	}

	@Override
	public double getLocalOut() {
		double x = this.getLocalIn();
		double localOut = this.sigmoidFunction(x);
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

	@Override
	public void setLocalOut(double localOut) {
		throw new UnsupportedOperationException("You should never call this function on hidden layer.");
	}
	
	public String toString() {
		String id = this.getID()==null ? "Uknown neuron" : this.getID(); id+="\t";
		String bias = "Bias: " +String.format("%.5f", this.getBias()) + "\t";
		String momentum = "Momentum: " + this.getLayer().getNetwork().getMomentum() + "\t";
		String learningRate = "Learning rate: " + this.getLayer().getNetwork().getLearningRate() + "\t";
		String in = "Wejście: " + String.format("%.10f", this.getLocalIn()) + "\t";
		String out = "Wyjście: " + String.format("%.10f", this.getLocalOut()) + "\t";
		
		return 	
//				"=======================================\n" +
				id+bias+
//				momentum+learningRate+
				out+		
//				"======================================="+
				"";
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
		return this.layer;
	}
	
	@Override
	public double getGradient() {
		return gradient;
	}

	@Override
	public void setGradient(double gradient) {
		this.gradient = gradient;
	}
	
	@Override
	public double getBias() {
		return bias;
	}

	@Override
	public void setBias(double bias) {
		this.bias = bias;
	}

	@Override
	public double getPreviousDelta() {
		return previousDelta;
	}

	@Override
	public void setPreviousDelta(double previousDelta) {
		this.previousDelta = previousDelta;
	}


}

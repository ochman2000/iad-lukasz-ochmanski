package pl.lodz.p.iad.structure;

import java.util.ArrayList;
import java.util.List;

public class HiddenLayerNeuron implements Neuron {
	private List<Edge> input;
	private List<Edge> output;
	private double bias;
	private double momentum;
	private String ID;
	
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
	
	/* (non-Javadoc)
	 * @see pl.lodz.p.iad.structure.Neuron#getLocalIn()
	 */
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
	
	@Override
	public double getMomentum() {
		return momentum;
	}

	@Override
	public void setMomentum(double momentum) {
		this.momentum = momentum;
	}
	
	@Override
	public double getBias() {
		return bias;
	}
	
	@Override
	public void setBias(double bias) {
		this.bias = bias;
	}
	
	public String toString() {
		String id = this.getID()==null ? "Uknown neuron" : this.getID(); id+="\n";
		String bias = "Bias: " +this.getBias() + "\n";
		String momentum = "Momentum: " + this.getMomentum() + "\n";
		String in = "Wejście: " + this.getLocalIn() + "\n";
		String out = "Wyjście: " + this.getLocalOut() + "\n";
		
		return 	"=======================================\n" +
				id+bias+momentum+in+out+		
				"=======================================\n";
	}

	@Override
	public double getWeightOut() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setWeightOut(double weightOut) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Edge getInput(int index) {
		return this.getInputs().get(index);
	}

	@Override
	public Edge getOutput(int index) {
		return this.getOutputs().get(index);
	}
}

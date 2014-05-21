package pl.lodz.p.iad.structure;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class HiddenLayerNeuron implements Neuron {
	private List<Neuron> input;
	private List<Neuron> output;
	private double weightOut;
	private double bias;
	private double momentum;
	

	public HiddenLayerNeuron() {
		this(2,2);
	}

	public HiddenLayerNeuron(int in, int out) {
		input = new ArrayList<Neuron>(in);
		output = new ArrayList<Neuron>(out);
		Random random = new Random();
		double rnd = random.nextDouble();
		this.setWeightOut(rnd);
	}

	@Override
	public List<Neuron> getInputs() {
		return input;
	}

	@Override
	public void setInputs(List<Neuron> input) {
		this.input = input;
	}

	@Override
	public List<Neuron> getOutputs() {
		return output;
	}

	@Override
	public void setOutputs(List<Neuron> output) {
		this.output = output;
	}
	
	@Override
	public void addNeuronOut(Neuron neuron) {
		neuron.getInputs().add(this);
		this.getOutputs().add(neuron);
	}
	
	/* (non-Javadoc)
	 * @see pl.lodz.p.iad.structure.Neuron#getLocalIn()
	 */
	@Override
	public double getLocalIn() {
		double localIn = 0.0;
		for (Neuron input : this.getInputs()) {
			double prevOut = input.getLocalOut();
			double weight = input.getWeightOut();
			double product = prevOut * weight;
			localIn+=product;
		}
		return localIn+this.getBias();
	}

	/* (non-Javadoc)
	 * @see pl.lodz.p.iad.structure.Neuron#getLocalOut()
	 */
	@Override
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
	/* (non-Javadoc)
	 * @see pl.lodz.p.iad.structure.Neuron#getWeightOut()
	 */
	@Override
	public double getWeightOut() {
		return weightOut;
	}
	/* (non-Javadoc)
	 * @see pl.lodz.p.iad.structure.Neuron#setWeightOut(double)
	 */
	@Override
	public void setWeightOut(double weightOut) {
		this.weightOut = weightOut;
	}

	@Override
	public void setLocalOut(double localOut) {
		throw new UnknownError("You should never call this function on hidden layer.");
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
}

package pl.lodz.p.iad.structure;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class InputLayerNeuron implements Neuron {
	private List<Neuron> input;
	private List<Neuron> output;
	private double weightOut;
	private double localIn;
	private double localOut;
	private double momentum;
	private double bias;
	

	public InputLayerNeuron() {
		this(2,2);
	}

	public InputLayerNeuron(int in, int out) {
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
		return localIn;
	}

	/* (non-Javadoc)
	 * @see pl.lodz.p.iad.structure.Neuron#getLocalOut()
	 */
	@Override
	public double getLocalOut() {
		return localOut;
	}
	
	@Override
	public void setLocalOut(double localOut) {
		this.localOut = localOut;
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

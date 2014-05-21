package pl.lodz.p.iad.structure;

import java.util.List;

public interface Neuron {

	public List<Neuron> getInputs();

	public void setInputs(List<Neuron> input);

	public List<Neuron> getOutputs();

	public void setOutputs(List<Neuron> output);

	public void addNeuronOut(Neuron neuron);

	public double getLocalIn();

	public double getLocalOut();

	public double getWeightOut();

	public void setWeightOut(double weightOut);

	public void setLocalOut(double localOut);
	
	public double getBias();
	
	public void setBias(double bias);
	
	public double getMomentum();

	public void setMomentum(double momentum);

}
package pl.lodz.p.iad.structure;

import java.util.List;

public interface Neuron {

	public List<Edge> getInputs();

	public void setInputs(List<Edge> input);

	public List<Edge> getOutputs();

	public void setOutputs(List<Edge> output);

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
	
	public String getID();
	
	public void setID(String id);
}
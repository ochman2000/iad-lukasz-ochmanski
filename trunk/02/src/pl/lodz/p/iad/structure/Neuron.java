package pl.lodz.p.iad.structure;

import java.util.List;

public interface Neuron {

	public List<Edge> getInputs();
	
	public Edge getInput(int index);

	public void setInputs(List<Edge> input);

	public List<Edge> getOutputs();
	
	public Edge getOutput(int index);

	public void setOutputs(List<Edge> output);

	public void addNeuronOut(Neuron neuron);

	public double getLocalIn();

	public double getLocalOut();

	public void setLocalOut(double localOut);
	
	public String getID();
	
	public void setID(String id);
	
	public void setLayer(Layer layer);
	
	public Layer getLayer();

	double getGradient();

	void setGradient(double gradient);

	void setBias(double bias);

	double getBias();
}
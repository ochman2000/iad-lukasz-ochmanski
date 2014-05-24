package pl.lodz.p.iad.structure;

import java.util.ArrayList;
import java.util.List;

public class Layer {
	private List<Neuron> neurons;
	private double bias;

	public Layer(int liczbaNeuronow) {
		neurons = new ArrayList<Neuron>(liczbaNeuronow);
	}

	public List<Neuron> getNeurons() {
		return neurons;
	}

	public void setNeurons(List<Neuron> neurons) {
		this.neurons = neurons;
	}

	public void add(Neuron neuron) {
		neuron.setLayer(this);
		this.getNeurons().add(neuron);
	}
	
	public Neuron getNeuron(int index) {
		return this.getNeurons().get(index);
	}

	public double getBias() {
		return bias;
	}

	public void setBias(double bias) {
		this.bias = bias;
	}
	
}

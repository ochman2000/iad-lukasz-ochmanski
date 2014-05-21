package pl.lodz.p.iad.structure;

import java.util.ArrayList;
import java.util.List;

public class Layer {
	private List<Neuron> neurons;

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
		this.getNeurons().add(neuron);
	}
}

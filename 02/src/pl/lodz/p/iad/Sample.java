package pl.lodz.p.iad;

import pl.lodz.p.iad.structure.Layer;
import pl.lodz.p.iad.structure.Network;
import pl.lodz.p.iad.structure.Neuron;

public class Sample {

	public Sample() {
		this.initializeStructure();
	}
	
	private Network initializeStructure() {
		
		Network network = new Network();
		
		//INPUT LAYER
		Layer layer0 = new Layer(3);
		Neuron neuron_0_0 = new Neuron();
		Neuron neuron_0_1 = new Neuron();
		Neuron neuron_0_2 = new Neuron();
		layer0.add(neuron_0_0);
		layer0.add(neuron_0_1);
		layer0.add(neuron_0_2);
		
		//HIDDEN LAYER
		Layer layer1 = new Layer(4);
		Neuron neuron_1_0 = new Neuron();
		Neuron neuron_1_1 = new Neuron();
		Neuron neuron_1_2 = new Neuron();
		Neuron neuron_1_3 = new Neuron();
		layer1.add(neuron_1_0);
		layer1.add(neuron_1_1);
		layer1.add(neuron_1_2);
		layer1.add(neuron_1_3);
		
		//OUTPUT LAYER
		Layer layer2 = new Layer(2);
		Neuron neuron_2_0 = new Neuron();
		Neuron neuron_2_1 = new Neuron();
		layer2.add(neuron_2_0);
		layer2.add(neuron_2_1);
		
		//CONNECTIONS
		neuron_0_0.addNeuronOut(neuron_1_0);
		neuron_0_0.addNeuronOut(neuron_1_1);
		neuron_0_0.addNeuronOut(neuron_1_2);
		neuron_0_0.addNeuronOut(neuron_1_3);
		
		neuron_0_1.addNeuronOut(neuron_1_0);
		neuron_0_1.addNeuronOut(neuron_1_1);
		neuron_0_1.addNeuronOut(neuron_1_2);
		neuron_0_1.addNeuronOut(neuron_1_3);
		
		neuron_0_2.addNeuronOut(neuron_1_0);
		neuron_0_2.addNeuronOut(neuron_1_1);
		neuron_0_2.addNeuronOut(neuron_1_2);
		neuron_0_2.addNeuronOut(neuron_1_3);
		
		neuron_1_0.addNeuronOut(neuron_2_0);
		neuron_1_0.addNeuronOut(neuron_2_1);
		
		neuron_1_1.addNeuronOut(neuron_2_0);
		neuron_1_1.addNeuronOut(neuron_2_1);
		
		neuron_1_2.addNeuronOut(neuron_2_0);
		neuron_1_2.addNeuronOut(neuron_2_1);
		
		neuron_1_3.addNeuronOut(neuron_2_0);
		neuron_1_3.addNeuronOut(neuron_2_1);
		
		network.addLayer(layer0);
		network.addLayer(layer1);
		network.addLayer(layer2);
		
		return network;
	}
}

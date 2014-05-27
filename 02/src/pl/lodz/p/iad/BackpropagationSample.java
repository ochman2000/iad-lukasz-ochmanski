package pl.lodz.p.iad;

import pl.lodz.p.iad.structure.HiddenLayerNeuron;
import pl.lodz.p.iad.structure.InputLayerNeuron;
import pl.lodz.p.iad.structure.Layer;
import pl.lodz.p.iad.structure.Network;
import pl.lodz.p.iad.structure.Neuron;

public class BackpropagationSample {

	public BackpropagationSample() {
		Network network = this.initializeStructure();
		while(true) {
//			System.out.println(network);
			System.out.println(network.getOutputLayer().getNeuron(0));
			System.out.println(network.getOutputLayer().getNeuron(1));
			double[] out = {-0.85, 0.75};
			network.train(out);
		}
	}
	
	private Network initializeStructure() {
		
		Network network = new Network();
		
		//INPUT LAYER
		Layer layer0 = new Layer(3);
		Neuron neuron_0_0 = new InputLayerNeuron(); neuron_0_0.setID("[0-0]");
		Neuron neuron_0_1 = new InputLayerNeuron(); neuron_0_1.setID("[0-1]");
		Neuron neuron_0_2 = new InputLayerNeuron(); neuron_0_2.setID("[0-2]");
		neuron_0_0.setLocalOut(1.0);
		neuron_0_1.setLocalOut(2.0);
		neuron_0_2.setLocalOut(3.0);
		layer0.add(neuron_0_0);
		layer0.add(neuron_0_1);
		layer0.add(neuron_0_2);
		
		//HIDDEN LAYER
		Layer layer1 = new Layer(4);
		Neuron neuron_1_0 = new HiddenLayerNeuron(); neuron_1_0.setID("[1-0]");
		Neuron neuron_1_1 = new HiddenLayerNeuron(); neuron_1_1.setID("[1-1]");
		Neuron neuron_1_2 = new HiddenLayerNeuron(); neuron_1_2.setID("[1-2]");
		Neuron neuron_1_3 = new HiddenLayerNeuron(); neuron_1_3.setID("[1-3]");
		layer1.add(neuron_1_0);
		layer1.add(neuron_1_1);
		layer1.add(neuron_1_2);
		layer1.add(neuron_1_3);
		
		//OUTPUT LAYER
		Layer layer2 = new Layer(2);
		Neuron neuron_2_0 = new HiddenLayerNeuron(); neuron_2_0.setID("[2-0]");
		Neuron neuron_2_1 = new HiddenLayerNeuron(); neuron_2_1.setID("[2-1]");
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
		
		network.setMomentum(0.4);
		network.setLearningRate(0.9);
		
		return network;
	}
	
	public static void main(String[] args) {
		new BackpropagationSample();
	}
}

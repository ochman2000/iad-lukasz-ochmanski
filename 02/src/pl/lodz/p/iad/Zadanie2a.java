package pl.lodz.p.iad;

import java.util.Random;

import pl.lodz.p.iad.structure.HiddenLayerNeuron;
import pl.lodz.p.iad.structure.InputLayerNeuron;
import pl.lodz.p.iad.structure.Layer;
import pl.lodz.p.iad.structure.Network;
import pl.lodz.p.iad.structure.Neuron;

public class Zadanie2a {

	public static void main(String[] args) {
		new Zadanie2a();

	}
	
	public Zadanie2a() {
		Network network = this.initializeStructure();
		while(true) {
			System.out.println(network.getOutputLayer().getNeuron(0));
			System.out.println(network.getOutputLayer().getNeuron(1));
			System.out.println(network.getOutputLayer().getNeuron(2));
			System.out.println(network.getOutputLayer().getNeuron(3));
			double[] in = losuj();
			double[] out = in;
			network.train(in, out);
		}
	}
	
	private double[] losuj() {
		double[][] wzorce = {{0,0,0,0}, {0,1,0,0}, {0,0,1,0}, {0,0,0,1}};
		return wzorce[new Random().nextInt(4)];
	}

	private Network initializeStructure() {
		
		Network network = new Network();
		
		//INPUT LAYER
		Layer layer0 = new Layer(4);
		Neuron neuron_0_0 = new InputLayerNeuron(); neuron_0_0.setID("[0-0]");
		Neuron neuron_0_1 = new InputLayerNeuron(); neuron_0_1.setID("[0-1]");
		Neuron neuron_0_2 = new InputLayerNeuron(); neuron_0_2.setID("[0-2]");
		Neuron neuron_0_3 = new InputLayerNeuron(); neuron_0_3.setID("[0-3]");
		layer0.add(neuron_0_0);
		layer0.add(neuron_0_1);
		layer0.add(neuron_0_2);
		layer0.add(neuron_0_3);
		
		//HIDDEN LAYER
		Layer layer1 = new Layer(4);
		Neuron neuron_1_0 = new HiddenLayerNeuron(); neuron_1_0.setID("[1-0]");
		Neuron neuron_1_1 = new HiddenLayerNeuron(); neuron_1_1.setID("[1-1]");
		layer1.add(neuron_1_0);
		layer1.add(neuron_1_1);
		
		//OUTPUT LAYER
		Layer layer2 = new Layer(2);
		Neuron neuron_2_0 = new HiddenLayerNeuron(); neuron_2_0.setID("[2-0]");
		Neuron neuron_2_1 = new HiddenLayerNeuron(); neuron_2_1.setID("[2-1]");
		Neuron neuron_2_2 = new HiddenLayerNeuron(); neuron_2_2.setID("[2-2]");
		Neuron neuron_2_3 = new HiddenLayerNeuron(); neuron_2_3.setID("[2-3]");
		layer2.add(neuron_2_0);
		layer2.add(neuron_2_1);
		layer2.add(neuron_2_2);
		layer2.add(neuron_2_3);
		
		//CONNECTIONS
		neuron_0_0.addNeuronOut(neuron_1_0);
		neuron_0_0.addNeuronOut(neuron_1_1);
		neuron_0_1.addNeuronOut(neuron_1_0);
		neuron_0_1.addNeuronOut(neuron_1_1);
		neuron_0_2.addNeuronOut(neuron_1_0);
		neuron_0_2.addNeuronOut(neuron_1_1);
		neuron_0_3.addNeuronOut(neuron_1_0);
		neuron_0_3.addNeuronOut(neuron_1_1);
		
		neuron_1_0.addNeuronOut(neuron_2_0);
		neuron_1_0.addNeuronOut(neuron_2_1);
		neuron_1_0.addNeuronOut(neuron_2_2);
		neuron_1_0.addNeuronOut(neuron_2_3);
		neuron_1_1.addNeuronOut(neuron_2_0);
		neuron_1_1.addNeuronOut(neuron_2_1);
		neuron_1_1.addNeuronOut(neuron_2_2);
		neuron_1_1.addNeuronOut(neuron_2_3);
		
		
		network.addLayer(layer0);
		network.addLayer(layer1);
		network.addLayer(layer2);
		
		network.setMomentum(0.4);
		network.setLearningRate(0.9);
		
		return network;
	}
}

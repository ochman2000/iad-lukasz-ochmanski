package pl.lodz.p.iad;

import pl.lodz.p.iad.structure.HiddenLayerNeuron;
import pl.lodz.p.iad.structure.InputLayerNeuron;
import pl.lodz.p.iad.structure.Neuron;

public class XOR {
	
	public static int NUMBER_OF_CLASSES = 2;
	public static int NUMBER_OF_LAYERS = 3;
	public static double LEARN_RATE = 0.7;
	public static double MOMENTUM = 0.9;
	
	public static double XOR_INPUT[][] = {
		{ 0.0, 0.0 }, 
		{ 1.0, 0.0 },
		{ 0.0, 1.0 }, 
		{ 1.0, 1.0 } };
	
	public static double XOR_IDEAL[] = { 0.0, 1.0, 1.0, 0.0 };
	
	public XOR() {
		this.initializeStructure();
	}
	
	private void initializeStructure() {
		//INPUT LAYER
		Neuron neuron_0_0 = new InputLayerNeuron(1,3);
		Neuron neuron_0_1 = new InputLayerNeuron(1,3);
		//HIDDEN LAYER
		Neuron neuron_1_0 = new HiddenLayerNeuron(2,1);
		Neuron neuron_1_1 = new HiddenLayerNeuron(2,1);
		Neuron neuron_1_2 = new HiddenLayerNeuron(2,1);
		//OUTPUT LAYER
		Neuron neuron_2_0 = new HiddenLayerNeuron(3,1);
		
		//CONNECTIONS
		neuron_0_0.addNeuronOut(neuron_1_0);
		neuron_0_0.addNeuronOut(neuron_1_1);
		neuron_0_0.addNeuronOut(neuron_1_2);
		
		neuron_0_1.addNeuronOut(neuron_1_0);
		neuron_0_1.addNeuronOut(neuron_1_1);
		neuron_0_1.addNeuronOut(neuron_1_2);
		
		neuron_1_0.addNeuronOut(neuron_2_0);
		neuron_1_1.addNeuronOut(neuron_2_0);
		neuron_1_2.addNeuronOut(neuron_2_0);
	}
}

package pl.lodz.p.iad;

import pl.lodz.p.iad.structure.HiddenLayerNeuron;
import pl.lodz.p.iad.structure.InputLayerNeuron;
import pl.lodz.p.iad.structure.Neuron;

public class XOR {
	
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
		Neuron neuron_0_0 = new InputLayerNeuron();
		Neuron neuron_0_1 = new InputLayerNeuron();
		//HIDDEN LAYER
		Neuron neuron_1_0 = new HiddenLayerNeuron();
		Neuron neuron_1_1 = new HiddenLayerNeuron();
		Neuron neuron_1_2 = new HiddenLayerNeuron();
		//OUTPUT LAYER
		Neuron neuron_2_0 = new HiddenLayerNeuron();
		
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

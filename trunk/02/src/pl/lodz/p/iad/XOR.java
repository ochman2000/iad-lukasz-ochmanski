package pl.lodz.p.iad;

import java.util.ArrayList;
import java.util.List;

import pl.lodz.p.iad.structure.Edge;
import pl.lodz.p.iad.structure.Network;
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

		Network network = new Network();
		network.setInputLayer(2);
		network.setOutputLayer(1);
		network.addHiddenLayer(3);
		this.initializeStructure();
	}
	
	private void initializeStructure() {
		Neuron neuron00 = makeNeuron();
		Neuron neuron01 = makeNeuron();
		
		Neuron neuron10 = makeNeuron();
		Neuron neuron11 = makeNeuron();
		Neuron neuron12 = makeNeuron();

		Neuron neuron20 = makeNeuron();
		
	}
	
	private Neuron makeNeuron() {
		
		List<Edge> edges = new ArrayList<Edge>(NUMBER_OF_CLASSES);				
		for (int i=0; i<NUMBER_OF_CLASSES; i++) {
			Edge edge = new Edge();
			edge.setWeight(0.0);
			edges.set(i, edge);
		}
		Neuron n1 = new Neuron();
		n1.setInputs(edges);
		return n1;
	}
}

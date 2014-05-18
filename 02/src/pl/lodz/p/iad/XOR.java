package pl.lodz.p.iad;

import pl.lodz.p.iad.structure.Edge;
import pl.lodz.p.iad.structure.Network;
import pl.lodz.p.iad.structure.Neuron;

public class XOR {
	
	public static int NUMBER_OF_CLASSES = 2;
	public static int NUMBER_OF_LAYERS = 3;
	
	public static double XOR_INPUT[][] = {
		{ 0.0, 0.0 }, 
		{ 1.0, 0.0 },
		{ 0.0, 1.0 }, 
		{ 1.0, 1.0 } };
	
	public static double XOR_IDEAL[] = { 0.0, 1.0, 1.0, 0.0 };
	
	public XOR() {

		Network network = new Network();
//		Neuron n1 = makeNeuron(XOR_INPUT[0], XOR_IDEAL[0]);
//		Neuron n2 = makeNeuron(XOR_INPUT[1], XOR_IDEAL[1]);
//		Neuron n3 = makeNeuron(XOR_INPUT[2], XOR_IDEAL[2]);
//		Neuron n4 = makeNeuron(XOR_INPUT[3], XOR_IDEAL[3]);
		network.addLayer(2);
		network.addLayer(3);
		network.addLayer(1);
	}
	
	private Neuron makeNeuron(double[] input, double output) {
		
		Edge[] edges = new Edge[NUMBER_OF_CLASSES];				
		for (int i=0; i<NUMBER_OF_CLASSES; i++) {
			Edge edge = new Edge();
			edge.setInput(input[i]);
			edge.setWeight(0.0);
			edges[i] = edge;
		}
		Neuron n1 = new Neuron();
		n1.setInputs(edges);
		n1.setOutput(output);
		return n1;
	}
}

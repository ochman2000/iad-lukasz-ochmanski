package pl.lodz.p.iad.strategy;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.StringTokenizer;

import pl.lodz.p.iad.structure.HiddenLayerNeuron;
import pl.lodz.p.iad.structure.Input;
import pl.lodz.p.iad.structure.InputLayerNeuron;
import pl.lodz.p.iad.structure.Layer;
import pl.lodz.p.iad.structure.Network;
import pl.lodz.p.iad.structure.Neuron;
import pl.lodz.p.iad.structure.Strategy;

/**
 * Przedmiotem badania są cechy 1,2,4
 * @author Łukasz Ochmański
 *
 */
public class Zadanie2b03 implements Strategy {
	
	private static final double LEARNING_RATE = 0.6;
	private static final double MOMENTUM = 0.1;
	private static final boolean USE_BIAS = true;
	private static final int LICZBA_CECH = 3;
	private static final int LICZBA_KLAS = 3;
	
	@Override
	public Input[] readDataFromFile(Network network, String fileName) {
		Charset charset = Charset.forName("US-ASCII");
		Path file = Paths.get(fileName);
		Input[] wzorc = null;
		try {
			List<String> lines = Files.readAllLines(file, charset);
			wzorc = new Input[lines.size()];
			for (int l=0; l<lines.size(); l++) {
				StringTokenizer st = new StringTokenizer(lines.get(l), ",");
				double[] wzorzec = new double[LICZBA_CECH];
				wzorzec[0] = Double.parseDouble(st.nextToken());
				wzorzec[1] = Double.parseDouble(st.nextToken());
				Double.parseDouble(st.nextToken());
				wzorzec[2] = Double.parseDouble(st.nextToken());
				double[] expected = new double[LICZBA_KLAS];
				for(int i=0; i<LICZBA_KLAS; i++) {
					expected[i] = Double.parseDouble(st.nextToken());
				}
				Input in = new Input();
				in.setWzorzec(wzorzec);
				in.setExpected(expected);
				wzorc[l] = in;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return wzorc;
	}
	
	@Override
	public Network initializeStructure() {
		Network network = new Network();
		network.enableBias(USE_BIAS);
		
		//INPUT LAYER
		Layer layer0 = new Layer(3);
		Neuron neuron_0_0 = new InputLayerNeuron(); neuron_0_0.setID("[0-0]");
		Neuron neuron_0_1 = new InputLayerNeuron(); neuron_0_1.setID("[0-1]");
		Neuron neuron_0_2 = new InputLayerNeuron(); neuron_0_2.setID("[0-2]");
		layer0.add(neuron_0_0);
		layer0.add(neuron_0_1);
		layer0.add(neuron_0_2);
		
		//HIDDEN LAYER
		Layer layer1 = new Layer(2);
		Neuron neuron_1_0 = new HiddenLayerNeuron(); neuron_1_0.setID("[1-0]");
		Neuron neuron_1_1 = new HiddenLayerNeuron(); neuron_1_1.setID("[1-1]");
		layer1.add(neuron_1_0);
		layer1.add(neuron_1_1);
		
		//OUTPUT LAYER
		Layer layer2 = new Layer(3);
		Neuron neuron_2_0 = new HiddenLayerNeuron(); neuron_2_0.setID("[2-0]");
		Neuron neuron_2_1 = new HiddenLayerNeuron(); neuron_2_1.setID("[2-1]");
		Neuron neuron_2_2 = new HiddenLayerNeuron(); neuron_2_2.setID("[2-2]");
		layer2.add(neuron_2_0);
		layer2.add(neuron_2_1);
		layer2.add(neuron_2_2);
		
		//CONNECTIONS
		neuron_0_0.addNeuronOut(neuron_1_0);
		neuron_0_0.addNeuronOut(neuron_1_1);
		neuron_0_1.addNeuronOut(neuron_1_0);
		neuron_0_1.addNeuronOut(neuron_1_1);
		neuron_0_2.addNeuronOut(neuron_1_0);
		neuron_0_2.addNeuronOut(neuron_1_1);
		
		neuron_1_0.addNeuronOut(neuron_2_0);
		neuron_1_0.addNeuronOut(neuron_2_1);
		neuron_1_0.addNeuronOut(neuron_2_2);
		neuron_1_1.addNeuronOut(neuron_2_0);
		neuron_1_1.addNeuronOut(neuron_2_1);
		neuron_1_1.addNeuronOut(neuron_2_2);
		
		network.addLayer(layer0);
		network.addLayer(layer1);
		network.addLayer(layer2);
		
		network.setMomentum(MOMENTUM);
		network.setLearningRate(LEARNING_RATE);
		
		return network;
	}
}

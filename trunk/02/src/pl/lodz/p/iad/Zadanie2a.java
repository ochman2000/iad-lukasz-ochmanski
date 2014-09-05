package pl.lodz.p.iad;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.spi.CharsetProvider;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import pl.lodz.p.iad.structure.HiddenLayerNeuron;
import pl.lodz.p.iad.structure.InputLayerNeuron;
import pl.lodz.p.iad.structure.Layer;
import pl.lodz.p.iad.structure.Network;
import pl.lodz.p.iad.structure.Neuron;

public class Zadanie2a {

	private static double LEARNING_RATE;
	private static double MOMENTUM ;
	private static boolean USE_BIAS;
	double[][] wzorce = {{0,0,0,0}, {0,1,0,0}, {0,0,1,0}, {0,0,0,1}};
	
	public static void main(String[] args) {
		LEARNING_RATE = 0.6;
		MOMENTUM = 0.0;
		USE_BIAS = false;
		new Zadanie2a("sprawozdanie/dane/a/test01.txt");
		
		LEARNING_RATE = 0.6;
		MOMENTUM = 0.0;
		USE_BIAS = true;
		new Zadanie2a("sprawozdanie/dane/a/test02.txt");
		
		LEARNING_RATE = 0.9;
		MOMENTUM = 0.0;
		USE_BIAS = true;
		new Zadanie2a("sprawozdanie/dane/a/test03.txt");
		
		LEARNING_RATE = 0.6;
		MOMENTUM = 0.0;
		USE_BIAS = true;
		new Zadanie2a("sprawozdanie/dane/a/test04.txt");
		
		LEARNING_RATE = 0.2;
		MOMENTUM = 0.0;
		USE_BIAS = true;
		new Zadanie2a("sprawozdanie/dane/a/test05.txt");
		
		LEARNING_RATE = 0.9;
		MOMENTUM = 0.6;
		USE_BIAS = true;
		new Zadanie2a("sprawozdanie/dane/a/test06.txt");
		
		LEARNING_RATE = 0.2;
		MOMENTUM = 0.9;
		USE_BIAS = true;
		new Zadanie2a("sprawozdanie/dane/a/test07.txt");
	}
	
	public Zadanie2a(String fileName) {
		Network network = this.initializeStructure();
		Charset charset = StandardCharsets.US_ASCII;
		Path file = Paths.get(fileName);
		try (BufferedWriter writer = Files.newBufferedWriter(file, charset)) {
			int count = 0;
			while (count < 4000) {
				double error = epoka(network);
				if (count % 100 == 0) {
					System.out.println("Epoka:\t" + count + "\tMSE: "+ String.format("%5f", error));
					writer.write(count + "\t" + error+"\n");
				}
				count++;
			}
		} catch (IOException x) {
			System.err.format("IOException: %s%n", x);
		}

		for (int i = 0; i < wzorce.length; i++) {
			System.out.print(Arrays.toString(wzorce[i])+"\t");
			System.out.println(Arrays.toString(network.test(wzorce[i])));
		}
		System.out.println(network);
	}
	
	private double epoka(Network network) {
		double error = 0.0;
		List<Integer> order = new ArrayList<Integer>(4);		
		while (order.size()<wzorce.length) {
			int a = new Random().nextInt(4);
			if (!order.contains(a)) {
				order.add(a);
				double[] output = network.train(wzorce[a], wzorce[a]);
				error += Network.MSE(wzorce[a], output);
			}
		}
		return error/order.size();
	}

	private Network initializeStructure() {
		
		Network network = new Network();
		network.enableBias(USE_BIAS);
		
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
		Layer layer1 = new Layer(2);
		Neuron neuron_1_0 = new HiddenLayerNeuron(); neuron_1_0.setID("[1-0]");
		Neuron neuron_1_1 = new HiddenLayerNeuron(); neuron_1_1.setID("[1-1]");
		layer1.add(neuron_1_0);
		layer1.add(neuron_1_1);
		
		//OUTPUT LAYER
		Layer layer2 = new Layer(4);
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
		
		network.setMomentum(MOMENTUM);
		network.setLearningRate(LEARNING_RATE);
		
		return network;
	}
}

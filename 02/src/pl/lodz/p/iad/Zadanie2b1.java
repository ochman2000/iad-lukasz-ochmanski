package pl.lodz.p.iad;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

import pl.lodz.p.iad.structure.HiddenLayerNeuron;
import pl.lodz.p.iad.structure.InputLayerNeuron;
import pl.lodz.p.iad.structure.Layer;
import pl.lodz.p.iad.structure.Network;
import pl.lodz.p.iad.structure.Neuron;

public class Zadanie2b1 {
	
	/*
	 * wydzielić 10% z jako zbiór walidacyjny i nie używać do treningu.
	 * 2 wykresy jeden dla zbióru treningowego i drugi dla zbioru walidacyjnego
	 * zestawione na jednym.
	 */

	private static final double LEARNING_RATE = 0.6;
	private static final double MOMENTUM = 0.1;
	private static final boolean USE_BIAS = true;
	private static final int LICZBA_CECH = 4;
	private static final int LICZBA_KLAS = 3;
	private static final int LIMIT_EPOK = 4000;
	private Input[] wzorce;
	private Input[]	zbiórWalidacyjny;
	
	public static void main(String[] args) {
		new Zadanie2b1();
		
	}
	
	public Zadanie2b1() {
		Network network = this.initializeStructure();
		
		Charset charset = Charset.forName("US-ASCII");
		Path file = Paths.get("iris2.dane");
		try {
			List<String> lines = Files.readAllLines(file, charset);
			wzorce = new Input[lines.size()];
			for (int l=0; l<lines.size(); l++) {
				StringTokenizer st = new StringTokenizer(lines.get(l), ",");
				double[] wzorzec = new double[LICZBA_CECH];
				for(int i=0; i<LICZBA_CECH; i++) {
					wzorzec[i] = Double.parseDouble(st.nextToken());
				}
				double[] expected = new double[LICZBA_KLAS];
				for(int i=0; i<LICZBA_KLAS; i++) {
					expected[i] = Double.parseDouble(st.nextToken());
				}
				Input in = new Input();
				in.setWzorzec(wzorzec);
				in.setExpected(expected);
				wzorce[l] = in;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Path fileOut = Paths.get("Test1.txt");
		try (BufferedWriter writer = Files.newBufferedWriter(fileOut, charset)) {
			int count = 0;
			while (count < LIMIT_EPOK) {
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
			System.out.println(Arrays.toString(wzorce[i].getWzorzec()));
			System.out.println(network.test(wzorce[i].getWzorzec(), 5));
		}
		System.out.println(network);
	}
	
	private double epoka(Network network) {
		double error = 0.0;
		Input[] shuffled = shuffleArray(wzorce);
		for (int i=0; i<shuffled.length; i++) {
			network.train(shuffled[i].getWzorzec(), shuffled[i].getExpected());
//			error += network.getMSE(shuffled[i].getWzorzec());
			error += Network.MSE(shuffled[i].getWzorzec(), shuffled[i].getExpected());
		}
		return error/shuffled.length;
	}
	
	// Implementing Fisher–Yates shuffle
	public static Input[] shuffleArray(Input[] ar) {
		Input[] out = new Input[ar.length];
		System.arraycopy(ar, 0, out, 0, ar.length);
		Random rnd = new Random();
		for (int i = out.length - 1; i > 0; i--) {
			int index = rnd.nextInt(i + 1);
			// Simple swap
			Input a = out[index];
			out[index] = out[i];
			out[i] = a;
		}
		return out;
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
		neuron_0_3.addNeuronOut(neuron_1_0);
		neuron_0_3.addNeuronOut(neuron_1_1);
		
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
class Input {
	private double[] wzorzec;
	private double[] expected;
	public double[] getWzorzec() {
		return wzorzec;
	}
	public void setWzorzec(double[] wzorzec) {
		this.wzorzec = wzorzec;
	}
	public double[] getExpected() {
		return expected;
	}
	public void setExpected(double[] expected) {
		this.expected = expected;
	}
	
}

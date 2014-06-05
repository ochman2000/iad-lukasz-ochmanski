package pl.lodz.p.iad;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

import pl.lodz.p.iad.structure.HiddenLayerNeuron;
import pl.lodz.p.iad.structure.InputLayerNeuron;
import pl.lodz.p.iad.structure.Layer;
import pl.lodz.p.iad.structure.Network;
import pl.lodz.p.iad.structure.Neuron;

public class Zadanie2b4 {
	
	/*
	 * wydzielić 10% z jako zbiór walidacyjny i nie używać do treningu.
	 * 2 wykresy jeden dla zbióru treningowego i drugi dla zbioru walidacyjnego
	 * zestawione na jednym.
	 */

	private static final String IRIS3_DANE = "iris3.dane";
	private static final String IRIS2_DANE = "iris2.dane";
	private static final String OUT_URL = "Test2b4.txt";
	private static final double LEARNING_RATE = 0.6;
	private static final double MOMENTUM = 0.1;
	private static final boolean USE_BIAS = true;
	private static final int LICZBA_CECH = 3;
	private static final int LICZBA_KLAS = 3;
	private static final int LIMIT_EPOK = 4000;
	
	public static void main(String[] args) {
		new Zadanie2b4();
	}
	
	private Zadanie2b4() {
		Network network = this.initializeStructure();
		Input[] wzorce = readDataFromFile(network, IRIS2_DANE);
		Input[] zbiórWalidacyjny = readDataFromFile(network, IRIS3_DANE);
		feedNetworkWithData(network, wzorce);
		validatePatterns(network, zbiórWalidacyjny);
//		System.out.println(network);
	}

	private void validatePatterns(Network network, Input[] zbiórWalidacyjny) {
		String s = "";
		for (int i = 0; i < zbiórWalidacyjny.length; i++) {
			System.out.println(Arrays.toString(zbiórWalidacyjny[i].getWzorzec()));
			double[] result = network.test(zbiórWalidacyjny[i].getWzorzec());
			double[] expected = zbiórWalidacyjny[i].getExpected();
			s = "[";
			for (double d : expected) { s += String.format("%.2f", d) + ", "; }
			s = s+"] [";
			for (double d : result) { s += String.format("%.2f", d) + ", ";	}
			double mse = Network.MSE(expected, result);
			s = s+"] MSE: " + String.format("%.5f", mse);
			System.out.println(s);
			
			Charset charset = Charset.forName("US-ASCII");
			Path fileOut = Paths.get(OUT_URL);
			try (BufferedWriter writer = Files.newBufferedWriter(fileOut, charset,
					new OpenOption[] {StandardOpenOption.APPEND})) {
				writer.append(i+" "+mse);
				writer.newLine();
			} catch (IOException x) {
				System.err.format("IOException: %s%n", x);
			}
		}
	}
	
	private void feedNetworkWithData(Network network, Input[] data) {
		Charset charset = Charset.forName("US-ASCII");
		Path fileOut = Paths.get(OUT_URL);
		try (BufferedWriter writer = Files.newBufferedWriter(fileOut, charset)) {
			int count = 0;
			while (count < LIMIT_EPOK) {
				double error = epoka(network, data);
				if (count % 100 == 0) {
					System.out.println("Epoka:\t" + count + "\tMSE: "+ String.format("%5f", error));
					writer.write(count + "\t" + error+"\n");
				}
				count++;
			}
		} catch (IOException x) {
			System.err.format("IOException: %s%n", x);
		}
	}

	private Input[] readDataFromFile(Network network, String fileName) {
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
				Double.parseDouble(st.nextToken());
				wzorzec[1] = Double.parseDouble(st.nextToken());
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

	private double epoka(Network network, Input[] data) {
		double error = 0.0;
		Input[] shuffled = shuffleArray(data);
		for (int i=0; i<shuffled.length; i++) {
			double[] output = network.train(shuffled[i].getWzorzec(), shuffled[i].getExpected());
			error += Network.MSE(shuffled[i].getExpected(), output);
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
		layer0.add(neuron_0_0);
		layer0.add(neuron_0_1);
		layer0.add(neuron_0_2);
		
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

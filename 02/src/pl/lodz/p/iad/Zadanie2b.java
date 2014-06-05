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
import java.util.Random;

import pl.lodz.p.iad.strategy.*;
import pl.lodz.p.iad.structure.Input;
import pl.lodz.p.iad.structure.Network;
import pl.lodz.p.iad.structure.Strategy;

public class Zadanie2b {
	
	/*
	 * wydzielić 10% z jako zbiór walidacyjny i nie używać do treningu.
	 * 2 wykresy jeden dla zbióru treningowego i drugi dla zbioru walidacyjnego
	 * zestawione na jednym.
	 */

	private Strategy strategy;
	private static final String IRIS3_DANE = "iris3.dane";
	private static final String IRIS2_DANE = "iris2.dane";
	private String outURL;
	private static final int LIMIT_EPOK = 4000;
	
	public static void main(String[] args) {
		new Zadanie2b(new Zadanie2b1(), "sprawozdanie/dane/b/test01.txt");
		new Zadanie2b(new Zadanie2b2(), "sprawozdanie/dane/b/test02.txt");
		new Zadanie2b(new Zadanie2b3(), "sprawozdanie/dane/b/test03.txt");
		new Zadanie2b(new Zadanie2b4(), "sprawozdanie/dane/b/test04.txt");
		new Zadanie2b(new Zadanie2b5(), "sprawozdanie/dane/b/test05.txt");
	}
	
	public Zadanie2b(Strategy strategy, String URL) {
		this.setStrategy(strategy);
		this.setOutURL(URL);
		this.run();
	}

	private void run() {
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
			
			Charset charset = Charset.forName("US-ASCII");
			Path fileOut = Paths.get(outURL);
			try (BufferedWriter writer = Files.newBufferedWriter(fileOut, charset,
					new OpenOption[] {StandardOpenOption.APPEND})) {
				writer.append(i+" "+mse);
				writer.newLine();
			} catch (IOException x) {
				System.err.format("IOException: %s%n", x);
			}
			System.out.println(s);
		}
	}
	
	private void feedNetworkWithData(Network network, Input[] data) {
		Charset charset = Charset.forName("US-ASCII");
		Path fileOut = Paths.get(outURL);
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
		return this.getStrategy().readDataFromFile(network, fileName);
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
		return this.getStrategy().initializeStructure();
	}

	public Strategy getStrategy() {
		return strategy;
	}

	public void setStrategy(Strategy strategy) {
		this.strategy = strategy;
	}
	
	public String getOutURL() {
		return outURL;
	}

	public void setOutURL(String outURL) {
		this.outURL = outURL;
	}

}

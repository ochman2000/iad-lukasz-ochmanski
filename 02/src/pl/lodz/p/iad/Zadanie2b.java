package pl.lodz.p.iad;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

import pl.lodz.p.iad.strategy.Zadanie2b01;
import pl.lodz.p.iad.strategy.Zadanie2b02;
import pl.lodz.p.iad.strategy.Zadanie2b03;
import pl.lodz.p.iad.strategy.Zadanie2b04;
import pl.lodz.p.iad.strategy.Zadanie2b05;
import pl.lodz.p.iad.strategy.Zadanie2b06;
import pl.lodz.p.iad.strategy.Zadanie2b07;
import pl.lodz.p.iad.strategy.Zadanie2b08;
import pl.lodz.p.iad.strategy.Zadanie2b09;
import pl.lodz.p.iad.strategy.Zadanie2b10;
import pl.lodz.p.iad.structure.Input;
import pl.lodz.p.iad.structure.Network;
import pl.lodz.p.iad.structure.Neuron;
import pl.lodz.p.iad.structure.Separator;
import pl.lodz.p.iad.structure.Strategy;

public class Zadanie2b {
	
	/*
	 * wydzielić 10% z jako zbiór walidacyjny i nie używać do treningu.
	 * 2 wykresy jeden dla zbióru treningowego i drugi dla zbioru walidacyjnego
	 * zestawione na jednym.
	 */

	private Strategy strategy;
	private static final String IRIS2_DANE = "iris2.dane";
	private static final String IRIS3_DANE = "iris3.dane";
	private String outURL, outURL2, outURL3;
	private static final int LIMIT_EPOK = 4000;
	
	public static void main(String[] args) {
		new Zadanie2b(new Zadanie2b01(), "sprawozdanie/dane/b/test01.txt");
		new Zadanie2b(new Zadanie2b02(), "sprawozdanie/dane/b/test02.txt");
		new Zadanie2b(new Zadanie2b03(), "sprawozdanie/dane/b/test03.txt");
		new Zadanie2b(new Zadanie2b04(), "sprawozdanie/dane/b/test04.txt");
		new Zadanie2b(new Zadanie2b05(), "sprawozdanie/dane/b/test05.txt");
		new Zadanie2b(new Zadanie2b06(), "sprawozdanie/dane/b/test06.txt");
		new Zadanie2b(new Zadanie2b07(), "sprawozdanie/dane/b/test07.txt");
		new Zadanie2b(new Zadanie2b08(), "sprawozdanie/dane/b/test08.txt");
		new Zadanie2b(new Zadanie2b09(), "sprawozdanie/dane/b/test09.txt");
		new Zadanie2bBagging(new Zadanie2b10(), "sprawozdanie/dane/b/test10.txt");

	}
	
	public Zadanie2b(Strategy strategy, String URL) {
		this.setStrategy(strategy);
		this.setOutURL(URL);
		this.run();
	}

	public void run() {
		Network network = this.initializeStructure();
		Input[] zbiórTreningowy = readDataFromFile(network, IRIS2_DANE);
		Input[] zbiórTestowy = readDataFromFile(network, IRIS3_DANE);
		feedNetworkWithData(network, zbiórTreningowy, zbiórTestowy);
		zapiszWarstwęUkrytą(network, zbiórTreningowy, outURL2);
		zapiszWarstwęUkrytą(network, zbiórTestowy, outURL3);
		System.out.println(network);
	}
	
	private void zapiszWarstwęUkrytą(Network network, Input[] dane, String filename) {
		Charset charset = StandardCharsets.US_ASCII;
		Path fileOut = Paths.get(filename);
		try {
			BufferedWriter writer = Files.newBufferedWriter(fileOut, charset);
			for (Input input : dane) {
				if (network.getInputLayer().getNeurons().size()!=input.getWzorzec().length) {
					throw new IllegalArgumentException("Liczba neuronów "
							+ "wejściowych nie odpowiada liczbie podanych argumentów.");
				}
				if (network.getOutputLayer().getNeurons().size()!=input.getExpected().length) {
					throw new IllegalArgumentException("Liczba neuronów "
							+ "wyjściowych nie odpowiada liczbie podanych argumentów.");
				}
				for (int i=0; i<input.getWzorzec().length; i++) {
					network.getInputLayer().getNeuron(i).setLocalOut(input.getWzorzec()[i]);
				}
				String s= "";
				Separator delim = new Separator(",");
				for (Neuron n : network.getLayer(network.getNumberOfLayers()-2).getNeurons()) {
					s += delim + String.format("%5f", n.getLocalOut());
				}
				for (double d : input.getExpected()) {
					s +=  ""+ delim + d;
				} 
				writer.write(s+"\n");
			}
			writer.close();
		} catch (IOException x) {
			System.err.format("IOException: %s%n", x);
		}
	}

	public void feedNetworkWithData(Network network, Input[] data, Input[] zbiórTestowy) {
		Charset charset = StandardCharsets.US_ASCII;
		Path fileOut = Paths.get(outURL);
		try {
			BufferedWriter writer = Files.newBufferedWriter(fileOut, charset);
			int count = 0;
			while (count < LIMIT_EPOK) {
				double error = epoka(network, data);
				if (count % 100 == 0) {
					double błądZbioruTestowego = testuj(network, zbiórTestowy);
					System.out.println("Epoka:\t" + count + "\tMSE: "+ String.format("%5f", error) + 
							"\t"+String.format("%5f", błądZbioruTestowego));
					writer.write(count + "\t" + error+"\t"+błądZbioruTestowego+"\n");
				}
				count++;
			}
			writer.close();
		} catch (IOException x) {
			System.err.format("IOException: %s%n", x);
		}
	}

	public Input[] readDataFromFile(Network network, String fileName) {
		return this.getStrategy().readDataFromFile(network, fileName);
	}

	public double epoka(Network network, Input[] data) {
		double error = 0.0;
		Input[] shuffled = shuffleArray(data);
		for (int i=0; i<shuffled.length; i++) {
			double[] output = network.train(shuffled[i].getWzorzec(), shuffled[i].getExpected());
			error += Network.MSE(shuffled[i].getExpected(), output);
		}
		return error/shuffled.length;
	}
	
	public double testuj(Network network, Input[] data) {
		double error = 0.0;
		for (Input irys : data) {
			error += Network.MSE(irys.getExpected(), network.test(irys.getWzorzec()));
		}
		return error/data.length;
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

	public Network initializeStructure() {
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
		Path full = Paths.get(outURL);
		String parent = full.getParent().toString();
		String element2 = "trening_"+full.getName(full.getNameCount()-1).toString();
		String element3 = "test_"+full.getName(full.getNameCount()-1).toString();
		this.outURL2 = (parent+"\\"+element2).replace('\\', '/');
		this.outURL3 = (parent+"\\"+element3).replace('\\', '/');
	}
}

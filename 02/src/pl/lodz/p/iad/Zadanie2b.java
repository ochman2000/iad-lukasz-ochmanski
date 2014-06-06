package pl.lodz.p.iad;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

import pl.lodz.p.iad.strategy.Zadanie2b1;
import pl.lodz.p.iad.strategy.Zadanie2b2;
import pl.lodz.p.iad.strategy.Zadanie2b3;
import pl.lodz.p.iad.strategy.Zadanie2b4;
import pl.lodz.p.iad.strategy.Zadanie2b5;
import pl.lodz.p.iad.strategy.Zadanie2b6;
import pl.lodz.p.iad.strategy.Zadanie2b7;
import pl.lodz.p.iad.strategy.Zadanie2b8;
import pl.lodz.p.iad.strategy.Zadanie2b9;
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
	private static final String IRIS2_DANE = "iris2.dane";
	private static final String IRIS3_DANE = "iris3.dane";
	private String outURL, outURL2;
	private static final int LIMIT_EPOK = 4000;
	
	public static void main(String[] args) {
		new Zadanie2b(new Zadanie2b1(), "sprawozdanie/dane/b/test01.txt");
		new Zadanie2b(new Zadanie2b2(), "sprawozdanie/dane/b/test02.txt");
		new Zadanie2b(new Zadanie2b3(), "sprawozdanie/dane/b/test03.txt");
		new Zadanie2b(new Zadanie2b4(), "sprawozdanie/dane/b/test04.txt");
		new Zadanie2b(new Zadanie2b5(), "sprawozdanie/dane/b/test05.txt");
		new Zadanie2b(new Zadanie2b6(), "sprawozdanie/dane/b/test06.txt");
		new Zadanie2b(new Zadanie2b7(), "sprawozdanie/dane/b/test07.txt");
		new Zadanie2b(new Zadanie2b8(), "sprawozdanie/dane/b/test08.txt");
		new Zadanie2b(new Zadanie2b9(), "sprawozdanie/dane/b/test09.txt");
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
		feedNetworkWithData(network, wzorce, zbiórWalidacyjny);
//		System.out.println(network);
	}
	
	private void feedNetworkWithData(Network network, Input[] data, Input[] zbiórWalidacyjny) {
		Charset charset = Charset.forName("US-ASCII");
		Path fileOut = Paths.get(outURL);
		try {
			BufferedWriter writer = Files.newBufferedWriter(fileOut, charset);
			int count = 0;
			while (count < LIMIT_EPOK) {
				double error = epoka(network, data);
				if (count % 100 == 0) {
					double błądZbioruWalidacyjnego = testuj(network, zbiórWalidacyjny);
					System.out.println("Epoka:\t" + count + "\tMSE: "+ String.format("%5f", error) + 
							"\t"+String.format("%5f", błądZbioruWalidacyjnego));
					writer.write(count + "\t" + error+"\t"+błądZbioruWalidacyjnego+"\n");
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
	
	private double testuj(Network network, Input[] data) {
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
		Path full = Paths.get(outURL);
		String parent = full.getParent().toString();
		String element = "w_"+full.getName(full.getNameCount()-1).toString();
		this.outURL = outURL;
		this.outURL2 = (parent+"\\"+element).replace('\\', '/');
	}
}

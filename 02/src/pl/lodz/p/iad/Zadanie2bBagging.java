package pl.lodz.p.iad;

import pl.lodz.p.iad.structure.Input;
import pl.lodz.p.iad.structure.Network;
import pl.lodz.p.iad.structure.Strategy;


public class Zadanie2bBagging extends Zadanie2b {
	
	public Zadanie2bBagging(Strategy strategy, String URL) {
		super(strategy, URL);
	}
	
	public void run() {
		Network network = initializeStructure();
		Input[] zbiórTreningowy = readDataFromFile(network, "sprawozdanie/dane/b/trening_test09.txt");
		Input[] zbiórTestowy = readDataFromFile(network, "sprawozdanie/dane/b/test_test09.txt");
		feedNetworkWithData(network, zbiórTreningowy, zbiórTestowy);
		System.out.println(network);
	}
}
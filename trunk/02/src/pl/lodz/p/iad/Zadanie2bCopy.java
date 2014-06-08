package pl.lodz.p.iad;

import pl.lodz.p.iad.structure.Input;
import pl.lodz.p.iad.structure.Network;
import pl.lodz.p.iad.structure.Strategy;

public class Zadanie2bCopy extends Zadanie2b{
	
	public Zadanie2bCopy(Strategy strategy, String URL) {
		super(strategy, URL);
	}


	public double epoka(Network network, Input[] data) {
		double error = 0.0;
		Input[] shuffled = Zadanie2b.shuffleArray(data);
		for (int i=0; i<shuffled.length; i++) {
			double[] output = network.train(shuffled[i].getWzorzec(), shuffled[i].getExpected());
			error += Network.MSE(shuffled[i].getExpected(), output);
		}
		return error/shuffled.length;
	}
}
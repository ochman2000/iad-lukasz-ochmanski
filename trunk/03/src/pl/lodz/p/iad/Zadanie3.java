package pl.lodz.p.iad;

import java.util.ArrayList;
import static java.util.Arrays.asList;
import java.util.List;

public class Zadanie3 {

	public static void main(String[] args) {

		List<List<Integer>> wariant = new ArrayList<>(asList(
						asList(0, 1),
						asList(2, 3, 4), 
						asList(5, 6, 7)));

//		 new Kmeans(wariant.get(0));
//		 new Kohonen(wariant.get(0));
		 new NeuralGas(wariant.get(0));
	}
}

package pl.lodz.p.iad;

import java.util.Arrays;

public class Zadanie3 {

	public static void main(String[] args) {

//		new Kmeans(Arrays.asList(0, 1));
		
//		new Kmeans(Arrays.asList(2, 3, 4));
		
//		new Kmeans(Arrays.asList(5, 6, 7));
		
		Kohonen.setNeuronsAmount(10);
		Kohonen.setIterLimit(100);
		Kohonen.setDrawStepPercent(1);
		Kohonen.writeToFile(false);
		new Kohonen(Arrays.asList(0, 1));
	}
}

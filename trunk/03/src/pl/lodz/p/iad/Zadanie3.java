package pl.lodz.p.iad;

import java.util.Arrays;

public class Zadanie3 {

	public static void main(String[] args) {

//		new Kmeans(Arrays.asList(0, 1));
		
//		new Kmeans(Arrays.asList(2, 3, 4));
		
//		new Kmeans(Arrays.asList(5, 6, 7));
		
		Kohonen.setNeuronsAmount(10);
		//Kohonen.setIterLimit(100);
		Kohonen.setEpochLimit(10);
		Kohonen.setDrawStepPercent(10);
		Kohonen.writeToFile(true);
		Kohonen.drawDiagmrams(false);
//		new Kohonen(Arrays.asList(0, 1));
//		new Kohonen(Arrays.asList(2, 3, 4));
		new Kohonen(Arrays.asList(5, 6, 7));
	}
}

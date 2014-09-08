package pl.lodz.p.iad;

import java.util.Arrays;

public class Zadanie3NeuralGas {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		
		//set eras limit
		//set SOM eras limit
		NeuralGas.setErasLimit(1);
		//set neuronsAmount		
		NeuralGas.setNeuronsAmount(5);
		//setNetworkAlpha
		NeuralGas.setNetworkAlpha(0.01);
		//set networkMomentum
		NeuralGas.setNetworkMomentum(0.1);
		
		NeuralGas.setIterLimit(2);
		
		//load data from file
		//select columns
		//set trainFile
		NeuralGas neuralGas = new NeuralGas(Arrays.asList(0, 1));
		
		neuralGas.startTeachAlgorithm();
		//set testFile
		
		//Kohonen.setNeuronsAmount(10);
		
		//Kohonen.setDrawStepPercent(1);
		//Kohonen.writeToFile(true);
		
		//start alg 
			/*
			 * new NeuralGas(neuronsAmount) 
			 * input & output Points List << loadDataFromTest/Teach file 
			 * 
			 * som.set learnPattern
			 * som.teach()
			 */
	}

}

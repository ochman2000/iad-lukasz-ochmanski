package pl.lodz.p.iad;

import java.util.ArrayList;
import static java.util.Arrays.asList;
import java.util.List;

public class Zadanie3 {

	private static List<List<Integer>> wariant 
			= new ArrayList<>(asList(
			asList(0, 1),
			asList(2, 3, 4), 
			asList(5, 6, 7)));

	public static void main(String[] args) {
//		 new Kmeans(wariant.get(0));
//		 new Kohonen(wariant.get(0));
//		 new NeuralGas(wariant.get(0));
		
		Kmeans kmeans;
		Kohonen kohonen;
		NeuralGas neuralGas;
		List<Integer> zbiorUczacy;
		
		//ZBIÓR NR 1
		zbiorUczacy = wariant.get(0);
		
		kmeans = new Kmeans();
		kmeans.setKolumny(zbiorUczacy);
		kmeans.setEpochLogCSV("resources/kmeans/wariant0_centroid8.csv");
		kmeans.run();
		kmeans.close();
		
		kohonen = new Kohonen();
		kohonen.setKolumny(zbiorUczacy);
		kohonen.setNormalization(false);
		kohonen.setEpochLogCSV("resources/kohonen/wariant0_neuron8.csv");
		kohonen.teach();
		kohonen.close();
		
		kohonen = new Kohonen();
		kohonen.setKolumny(zbiorUczacy);
		kohonen.setNormalization(true);
		kohonen.setEpochLogCSV("resources/kohonen/wariant0_neuron8_norm.csv");
		kohonen.teach();
		kohonen.close();
		
		neuralGas = new NeuralGas();
		neuralGas.setKolumny(zbiorUczacy);
		neuralGas.setNormalization(false);
		neuralGas.setEpochLogCSV("resources/neuralgas/wariant0_neuron8.csv");
		neuralGas.teach();
		neuralGas.close();
		
		neuralGas = new NeuralGas();
		neuralGas.setKolumny(zbiorUczacy);
		neuralGas.setNormalization(true);
		neuralGas.setEpochLogCSV("resources/neuralgas/wariant0_neuron8_norm.csv");
		neuralGas.teach();
		neuralGas.close();
		
		//ZBIÓR NR 2
		zbiorUczacy = wariant.get(1);
		
		kmeans = new Kmeans();
		kmeans.setKolumny(zbiorUczacy);
		kmeans.setEpochLogCSV("resources/kmeans/wariant1_centroid8.csv");
		kmeans.run();
		kmeans.close();
		
		kohonen = new Kohonen();
		kohonen.setKolumny(zbiorUczacy);
		kohonen.setNormalization(false);
		kohonen.setEpochLogCSV("resources/kohonen/wariant1_neuron8.csv");
		kohonen.teach();
		kohonen.close();
		
		kohonen = new Kohonen();
		kohonen.setKolumny(zbiorUczacy);
		kohonen.setNormalization(true);
		kohonen.setEpochLogCSV("resources/kohonen/wariant1_neuron8_norm.csv");
		kohonen.teach();
		kohonen.close();
		
		neuralGas = new NeuralGas();
		neuralGas.setKolumny(zbiorUczacy);
		neuralGas.setNormalization(false);
		neuralGas.setEpochLogCSV("resources/neuralgas/wariant1_neuron8.csv");
		neuralGas.teach();
		neuralGas.close();
		
		neuralGas = new NeuralGas();
		neuralGas.setKolumny(zbiorUczacy);
		neuralGas.setNormalization(true);
		neuralGas.setEpochLogCSV("resources/neuralgas/wariant1_neuron8_norm.csv");
		neuralGas.teach();
		neuralGas.close();
		
		//ZBIÓR NR 3
		zbiorUczacy = wariant.get(2);
		
		kmeans = new Kmeans();
		kmeans.setKolumny(zbiorUczacy);
		kmeans.setEpochLogCSV("resources/kmeans/wariant2_centroid8.csv");
		kmeans.run();
		kmeans.close();
		
		kohonen = new Kohonen();
		kohonen.setKolumny(zbiorUczacy);
		kohonen.setNormalization(false);
		kohonen.setEpochLogCSV("resources/kohonen/wariant2_neuron8.csv");
		kohonen.teach();
		kohonen.close();
		
		kohonen = new Kohonen();
		kohonen.setKolumny(zbiorUczacy);
		kohonen.setNormalization(true);
		kohonen.setEpochLogCSV("resources/kohonen/wariant2_neuron8_norm.csv");
		kohonen.teach();
		kohonen.close();
		
		neuralGas = new NeuralGas();
		neuralGas.setKolumny(zbiorUczacy);
		neuralGas.setNormalization(false);
		neuralGas.setEpochLogCSV("resources/neuralgas/wariant2_neuron8.csv");
		neuralGas.teach();
		neuralGas.close();
		
		neuralGas = new NeuralGas();
		neuralGas.setKolumny(zbiorUczacy);
		neuralGas.setNormalization(true);
		neuralGas.setEpochLogCSV("resources/neuralgas/wariant2_neuron8_norm.csv");
		neuralGas.teach();
		neuralGas.close();
		
	}
}

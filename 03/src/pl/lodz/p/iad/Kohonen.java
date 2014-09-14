package pl.lodz.p.iad;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import pl.lodz.p.iad.diagram.Voronoi2;
import pl.lodz.p.iad.diagram.Voronoi3;
import pl.lodz.p.iad.structure.KsiazkaKodowa;
import pl.lodz.p.iad.structure.Mapa;
import pl.lodz.p.iad.structure.Point;

public class Kohonen {

	/**
	 * Określa liczbę K centroidów, czyli liczbę klastrów. Tyle właśnie neuronów
	 * będzie w warstwie ukrytej. Jeśli ma to być lattice 40x40 powinieneś
	 * wpisać tutaj wartość 160;
	 */
	private int NUMBER_OF_NEURONS = 8;
	private double LEARNING_RATE = 0.1;
	private double RADIUS = 0.6;
	private int LIMIT_EPOK = 100;
	private double DRAW_STEP_IN_PERCENTS = 1.0;
	private boolean WRITE_TO_FILE = true;
	private boolean NORMALIZATION = true;
	private Method METHOD = Method.WTM;
	
	private int wielkoscZbioruUczacego = 100;
	private Voronoi2 voronoi;
	private StringBuilder epochLog;
	private StringBuilder epochCSV;

	public Kohonen(List<Integer> kolumny) {
		epochLog = new StringBuilder();
		epochCSV = new StringBuilder();
		epochCSV.append("epoka;promień sąsiedztwa;współczynnik uczenia;błąd kwantyzacji\r\n");
		
		Mapa hydra = new Mapa(kolumny);
		if (NORMALIZATION) hydra = hydra.getNormalized();
		if (wielkoscZbioruUczacego==0)
			wielkoscZbioruUczacego = hydra.size();
		List<Point> neurony = new ArrayList<Point>(NUMBER_OF_NEURONS);
		voronoi = new Voronoi2(512, 512, 0);

		teach(hydra, neurony);
		
		Charset charset = StandardCharsets.UTF_8;
		Path fileOut02 = Paths.get("resources/kohonen/epoch_log.txt");
		Path fileOut04 = Paths.get("resources/kohonen/epoch_log.csv");
		try {
			BufferedWriter epochLogWriterTxt = Files.newBufferedWriter(fileOut02, charset);
			BufferedWriter epochLogWriterCsv = Files.newBufferedWriter(fileOut04, charset);
			epochLogWriterTxt.write(epochLog.toString());
			epochLogWriterCsv.write(epochCSV.toString());
			epochLogWriterTxt.close();
			epochLogWriterCsv.close();
		} catch (IOException x) {
			System.err.format("IOException: %s%n", x);
		}
		System.out.println("Program terminated.");
	}

	private void teach(Mapa hydra, List<Point> neurony) {
		// LOSUJ K NEURONÓW (ZAMIAST INICJALIZOWAĆ PRZYPADKOWYMI WARTOŚCIAMI)
		Random rnd = new Random();
		while (neurony.size() < NUMBER_OF_NEURONS) {
			int indeks = rnd.nextInt(hydra.size());
			Point centroid = hydra.get(indeks);
			if (NORMALIZATION) centroid = centroid.getNormalized();
			centroid.setColor(Optional.of(Color.getHSBColor(
					(float) Math.random(), .7f, .7f)));
			if (!neurony.contains(centroid)) {
				neurony.add(centroid);
			}
		}

		// ROZPOCZNIJ PROCES PRZESUWANIA NEURONÓW
		System.out.println("Running Kohonen neural network, by Lukasz Ochmanski.");
		List<Point> noweNeurony = null;
		for (int epoka=0; epoka<LIMIT_EPOK; epoka++) {
			System.out.print(epoka+ "\t");
			epochLog.append(epoka+ "\t");
			epochCSV.append(epoka+";");
			noweNeurony = przesunNeuronyZwycieskie(hydra, neurony, epoka);
			double drawJump = wielkoscZbioruUczacego * (DRAW_STEP_IN_PERCENTS / 100);
			
			//ZBUDUJ KSIĄŻKĘ KODOWĄ i WYLICZ BŁĄD
			KsiazkaKodowa ksiazkaKodowa = new KsiazkaKodowa(wielkoscZbioruUczacego);;
			for (Point input : hydra) {
				Point teoretycznyZwyciezca = klasyfikuj(noweNeurony, input);
				ksiazkaKodowa.put(input, teoretycznyZwyciezca); 
			}
			double error = ksiazkaKodowa.getBladKwantyzacji();
			String msg = "error: " + error +"\r\n"
					+ "------------------------------------------------"
					+ "------------------------------------------------\r\n";
			System.out.print(msg);
			epochLog.append(msg);
			epochCSV.append(error+"\r\n");
			
			if (epoka % drawJump == 0.0) {
				wizualizujObszaryVoronoia(noweNeurony, hydra);
			}
		}
		rysujDiagramVoronoia(noweNeurony, hydra);
	}

	private void wizualizujObszaryVoronoia(List<Point> centroidy, Mapa mapa) {
		voronoi.clear();
		for (Point point : mapa) {
			voronoi.dodajKropkę(point.getCoordinate(0), point.getCoordinate(1));
		}
		for (Point centroid : centroidy) {
			voronoi.dodajCentroid(
					centroid.getCoordinate(0),
					centroid.getCoordinate(1),
					centroid.getColor().orElseThrow(
							IllegalArgumentException::new));
		}
		voronoi.drawMe();
		if (WRITE_TO_FILE) {
			voronoi.saveVornoiToFile();
		}
	}

	private void rysujDiagramVoronoia(List<Point> centroidy, Mapa mapa) {
		Voronoi3 voronoi3 = new Voronoi3();
		for (Point point : mapa) {
			voronoi3.dodajKropkę(point.getCoordinate(0), point.getCoordinate(1));
		}
		for (Point centroid : centroidy) {
			voronoi3.dodajCentroid(
					centroid.getCoordinate(0),
					centroid.getCoordinate(1),
					centroid.getColor().orElseThrow(
							IllegalArgumentException::new));
		}
		voronoi3.drawMe();
		if (WRITE_TO_FILE) {
			voronoi3.saveVornoiToFile();
		}
	}

	/**
	 * Klasyczny algorytm Kohonena zakłada adaptację wag jedynie neuronu
	 * zwycięskiego i neuronów znajdujących się nie dalej niż \lambda (czyli
	 * promienia sąsiedztwa). Oprócz tego należy zaimplementować adaptację z
	 * wykorzystaniem gaussowskiej funkcji sąsiedztwa, określającej współczynnik
	 * nauki neuronów przegrywających rywalizację w funkcji ich odległości od
	 * zwycięzcy. Ponadto należy uwzględnić zjawisko pojawiania się martwych
	 * neuronów uwzględniając aktywność neuronów w procesie uczenia.
	 */
	private List<Point> przesunNeuronyZwycieskie(Mapa trainingSet,
			List<Point> neurony, int epoka) {
		// ZRÓB DEEP COPY OF THE ARRAYLIST
		List<Point> noweNeurony = Kohonen.deepCopy(neurony);

		// RANDOMIZE TRAINING SET
		trainingSet.shuffle();
		double lambda = getPromienSasiedztwa(epoka);
		double learnRate = LEARNING_RATE
				* Math.exp(-(epoka / (double) LIMIT_EPOK));
		if (!Double.isFinite(learnRate))
			throw new ArithmeticException("Learning rate się sypnął.");
		epochLog.append("Promień sąsiedztwa: "+ lambda+"\t");
		System.out.print("Promień sąsiedztwa: "+ lambda+"\t");
		epochCSV.append(lambda+";");
		epochLog.append("Learn rate: "+learnRate+"\t");
		System.out.print("Learn rate: "+learnRate+"\t");
		epochCSV.append(learnRate+";");

		for (int i = 0; i < wielkoscZbioruUczacego; i++) {
			Point input = trainingSet.get(i);
			if (NORMALIZATION) input = input.getNormalized();
			Point uprawnionyZwyciezca = getZwyciezca(noweNeurony, input);
			for (Point neuron : noweNeurony) {
				Point tymczasowyZwyciezca = uprawnionyZwyciezca.clone();
				double dist = neuron.getEuclideanDistanceFrom(tymczasowyZwyciezca);
				double gauss = Math.exp(-((dist * dist) / (2.0 * (lambda * lambda))));
				if (!Double.isFinite(gauss))
					throw new ArithmeticException("gauss się sypnął.");
				if (0 > (gauss * learnRate) || (gauss * learnRate) > 1)
					throw new RuntimeException("Współczynnik poza zakresem (0,1): "
							+ gauss	* learnRate);
				if (METHOD==Method.WTA) { gauss = (dist<lambda) ? 1 : 0; }
				for (int wymiar = 0; wymiar < neuron.getCoordinates()
						.size(); wymiar++) {
					double waga = neuron.getCoordinate(wymiar);
					// METODA SUBRAKTYWNA
					double alpha = gauss * learnRate * (input.getCoordinate(wymiar) - waga);
					double nowaWaga = waga + alpha;
					neuron.setCoordinate(wymiar, nowaWaga);
				}
			}
		}
		return noweNeurony;
	}

	public static List<Point> deepCopy(List<Point> lista) {
		List<Point> copy = new ArrayList<Point>();
		for (Point point : lista) {
			copy.add(point.clone());
		}
		return copy;
	}

	/**
	 * Metoda zwraca neuron nabliższy podanemu wektorowi wejściowemu,
	 * uwzględniając przy tym liczbę zwycięstw. Neurony, które zwyciężały w
	 * przeszłości są karane poprzez zawyżanie ich odległości euklidesowej, a
	 * gdy liczba zwycięstw jest zbyt duża, muszą dodatkowo pauzować przez jeden
	 * cykl uczenia.
	 * 
	 * @param neurony
	 * @param input
	 * @return
	 */
	private Point getZwyciezca(List<Point> neurony, Point input) {
		double min = Double.MAX_VALUE;
		Point winner = null;
		for (Point n : neurony) {
			if (n.getWon() < 0) {
				// PRZYWRÓĆ NEURON DO RYWALIZACJI, ALE TYM RAZEM MUSI PAUZOWAĆ
				n.odnotujZwyciestwo();
			} else {
				double xyz = n.getEuclideanDistanceFrom(input);
				xyz = (double) (n.getWon() + 1) * xyz;
				if (xyz < min) {
					min = xyz;
					winner = n;
				}
			}
		}
		winner.odnotujZwyciestwo();
		return winner;
	}

	/**
	 * Zwraca centroida (wektor Voronoia) dla danego sygnału wejściowego. Metoda
	 * używana do testowania wektora wejściowego. W przeciwieństwie do metody
	 * getZwyciezca(), nie uwzględnia zmęczenia neuronów i nie bierze pod uwagę
	 * ilości zwycięstw, kar, nie faworyzuje martwych neuronow. Jest to prosty
	 * klasyfikator.
	 */
	public static Point klasyfikuj(List<Point> neurony, Point input) {
		double min = Double.MAX_VALUE;
		Point winner = null;
		for (Point n : neurony) {
			double xyz = n.getEuclideanDistanceFrom(input);
			if (xyz < min) {
				min = xyz;
				winner = n;
			}
		}
		return winner;
	}

	public double getPromienSasiedztwa(int epochNumber) {
		double wartoscPoczatkowa = RADIUS;
		double kmax = LIMIT_EPOK;
		double wartoscMinimalna = 0.01;

		double result = wartoscPoczatkowa
				* Math.pow((wartoscMinimalna / wartoscPoczatkowa),
						(epochNumber / kmax));

		return result;
	}

	public void setNeuronsAmount(int neuronsAmount) {
		NUMBER_OF_NEURONS = neuronsAmount;
	}

	public void setDrawStepPercent(int newDrawStepPercent) {
		DRAW_STEP_IN_PERCENTS = newDrawStepPercent;
	}

	public void writeToFile(boolean write) {
		WRITE_TO_FILE = write;
	}
}

enum Method { WTA, WTM; }
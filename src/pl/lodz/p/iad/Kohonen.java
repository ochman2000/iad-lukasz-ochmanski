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
	private int PODZBIORY = 8;
	private double LEARNING_RATE = 0.1;
	private double RADIUS = 0.5;
	private int LIMIT_EPOK = 100;
	private double DRAW_STEP_IN_PERCENTS = 1.0;
	private boolean WRITE_TO_FILE = true;
	private boolean DEBUG_MODE = true;
	private boolean NORMALIZATION = false;
	private Method METHOD = Method.WTM;
	
	private int liczbaIteracji;
	private KsiazkaKodowa ksiazkaKodowa;
	private BufferedWriter log;
	private Voronoi2 voronoi;
	private BufferedWriter debug;
	private StringBuilder sb01;

	public Kohonen(List<Integer> kolumny) {
		Mapa hydra = new Mapa(kolumny);
		if (NORMALIZATION) hydra = hydra.getNormalized();
		if (liczbaIteracji == 0)
			liczbaIteracji = hydra.size();
		ksiazkaKodowa = new KsiazkaKodowa(liczbaIteracji);
		Random rnd = new Random();
		List<Point> neurony = new ArrayList<Point>(PODZBIORY);
		voronoi = new Voronoi2(512, 512, 0);

		// LOSUJ K NEURONÓW (ZAMIAST INICJALIZOWAĆ PRZYPADKOWYMI WARTOŚCIAMI)
		while (neurony.size() < PODZBIORY) {
			int indeks = rnd.nextInt(hydra.size());
			Point centroid = hydra.get(indeks);
			if (NORMALIZATION) centroid = centroid.getNormalized();
			centroid.setColor(Optional.of(Color.getHSBColor(
					(float) Math.random(), .7f, .7f)));
			if (!neurony.contains(centroid)) {
				neurony.add(centroid);
			}
		}
		sb01 = new StringBuilder();
		sb01.append("Wylosowane neurony:\n");
		for (Point point : neurony) { sb01.append(point + "\n"); }
		sb01.append("\n");
		
		Charset charset = StandardCharsets.UTF_8;
		Path fileOut01 = Paths.get("resources/kohonen_log.txt");
		Path fileOut02 = Paths.get("resources/sprawozdanie.txt");
		try {
			debug = Files.newBufferedWriter(fileOut01, charset);
			log = Files.newBufferedWriter(fileOut02, charset);
		} catch (IOException x) {
			System.err.format("IOException: %s%n", x);
		}

		// ROZPOCZNIJ PROCES PRZESUWANIA NEURONÓW
		try {
			debug.write("Epoka nr:\t1\n");
			System.out.println("Epoka nr:\t1");
		}
		catch (IOException e) {	e.printStackTrace(); }
		List<Point> noweNeurony = przesunNeuronyZwycieskie(hydra, neurony);
		try { 
			debug.close();
			log.close();
			System.out.println("Program terminated.");
		} 
		catch (IOException e) {	e.printStackTrace(); }
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
			List<Point> neurony) {
		StringBuilder sb02 = new StringBuilder();
		// ZRÓB DEEP COPY OF THE ARRAYLIST
		List<Point> noweNeurony = Kohonen.deepCopy(neurony);

		// RANDOMIZE TRAINING SET
		trainingSet.shuffle();

		for (int i = 0; i < liczbaIteracji; i++) {
			Point input = trainingSet.get(i);
			if (NORMALIZATION) input = input.getNormalized();
			Point uprawnionyZwyciezca = getZwyciezca(noweNeurony, input);
			Point teoretycznyZwyciezca = klasyfikuj(noweNeurony, input);
			ksiazkaKodowa.put(input, teoretycznyZwyciezca);
			double lambda = getPromienSasiedztwa(i);
			double learnRate = LEARNING_RATE
					* Math.exp(-(i / (double) liczbaIteracji));
			if (!Double.isFinite(learnRate))
				throw new ArithmeticException("Learning rate się sypnął.");

			// STOPIEŃ UAKTYWNIENIA NEURONÓW Z SĄSIEDZTWA ZALEŻY OD ODLEGŁOŚCI
			// ICH WEKTORÓW WAGOWYCH OD WAG NEURONU WYGRYWAJĄCEGO.
			int numer=0;
			StringBuilder sb03 = new StringBuilder();
			for (Point neuron : noweNeurony) {
				numer++;
				// ZE WZGLĘDU NA TO W JAKI SPOSÓB JEST NAPISANY MÓJ KOD, NALEŻY
				// UWAŻAĆ,
				// ABY NIE LICZYĆ ODLEGŁOŚCI OD PRZESUWAJĄCEGO SIĘ ZWYCIĘZCY
				// WIĘC ZROBIĘ GŁUPIĄ KOPIĘ
				Point tymczasowyZwyciezca = uprawnionyZwyciezca.clone();
				double dist = neuron.getEuclideanDistanceFrom(tymczasowyZwyciezca);
				sb03.append("Odległość neuronu "+numer+" od neuronu zwycięzcy: "
						+ dist);
				double gauss = Math.exp(-((dist * dist) / (2.0 * (lambda * lambda))));
				if (!Double.isFinite(gauss))
					throw new ArithmeticException("gauss się sypnął.");
				if (0 > (gauss * learnRate) || (gauss * learnRate) > 1)
					throw new RuntimeException(
							"Współczynnik poza zakresem (0,1): " + gauss
									* learnRate);
				if (METHOD==Method.WTA) { gauss = (dist<lambda) ? 1 : 0; }
				for (int wymiar = 0; wymiar < neuron.getCoordinates()
						.size(); wymiar++) {
					double waga = neuron.getCoordinate(wymiar);
					// METODA SUBRAKTYWNA
					double alpha = gauss * learnRate * (input.getCoordinate(wymiar) - waga);
					double nowaWaga = waga + alpha;
					neuron.setCoordinate(wymiar, nowaWaga);

					sb03.append("\n\twymiar[" + wymiar + "] ");
					sb03.append("waga N: " + waga);
					sb03.append("\tWektor We: " + input.getCoordinate(wymiar));
					sb03.append("\tWe-N: "
							+ (input.getCoordinate(wymiar) - waga));
					sb03.append("\talpha: " + alpha);
					sb03.append("\tnowaWaga: " + (waga + alpha));
					sb03.append("\tdist: " + dist);
					sb03.append("\tlambda: "+ lambda);
					sb03.append("\tlearnRate: " + learnRate);
					sb03.append("\tgauss: " + gauss);
				}
				sb03.append("\n");
			}
			sb03.append("----------------------------------------\n");
//			System.out.print(sb03);
			try { 
				debug.write(sb03.toString()); 
			}
			catch (IOException e) {	e.printStackTrace(); }

			double drawJump = liczbaIteracji * (DRAW_STEP_IN_PERCENTS / 100);
			if (i % drawJump == 0.0) {
				wizualizujObszaryVoronoia(noweNeurony, trainingSet);
				sb02.append(i);
				sb02.append("\t learnRate: " + learnRate);
				sb02.append("\t lambda: " + lambda);
				sb02.append("\t error: " + ksiazkaKodowa.getBladKwantyzacji());
				sb02.append("\n------------------------------------------------");
				sb02.append("------------------------------------------------\n");
//				System.out.print(sb02);
			}
		}
		try { log.write(sb02.toString());	}
		catch (IOException e) { e.printStackTrace();	}
		rysujDiagramVoronoia(noweNeurony, trainingSet);
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

	public double getPromienSasiedztwa(int iterNumber) {
		// def promienSasiedztwa(k):
		double wartoscPoczatkowa = RADIUS;
		double kmax = liczbaIteracji;
		double wartoscMinimalna = 0.01;

		double result = wartoscPoczatkowa
				* Math.pow((wartoscMinimalna / wartoscPoczatkowa),
						(iterNumber / kmax));

		return result;
	}

	public void setNeuronsAmount(int neuronsAmount) {
		PODZBIORY = neuronsAmount;
	}

	public void setDrawStepPercent(int newDrawStepPercent) {
		DRAW_STEP_IN_PERCENTS = newDrawStepPercent;
	}

	public void writeToFile(boolean write) {
		WRITE_TO_FILE = write;
	}
}

enum Method { WTA, WTM; }
package pl.lodz.p.iad;

import java.awt.Color;
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
	private static int PODZBIORY = 8;
	private static double LEARNING_RATE = 0.1;
	private static int LICZBA_ITERACJI;
	private static double drawStepPercent = 1.0;
	private static boolean writeToFile = true;
	private KsiazkaKodowa ksiazkaKodowa;
	private Voronoi2 voronoi;

	public Kohonen(List<Integer> kolumny) {
		Mapa hydra = new Mapa(kolumny);
		if (LICZBA_ITERACJI == 0)
			LICZBA_ITERACJI = hydra.size();
		ksiazkaKodowa = new KsiazkaKodowa(LICZBA_ITERACJI);
		Random rnd = new Random();
		List<Point> neurony = new ArrayList<Point>(PODZBIORY);
		voronoi = new Voronoi2(512, 512, 0);

		// LOSUJ K NEURONÓW (ZAMIAST INICJALIZOWAĆ PRZYPADKOWYMI WARTOŚCIAMI)
		while (neurony.size() < PODZBIORY) {
			int indeks = rnd.nextInt(hydra.size());
			Point centroid = hydra.get(indeks).getNormalized();
			centroid.setColor(Optional.of(Color.getHSBColor(
					(float) Math.random(), .7f, .7f)));
			if (!neurony.contains(centroid)) {
				neurony.add(centroid);
			}
		}
		System.out.print("Wylosowane neurony:\t");
		for (Point point : neurony) {
			System.out.print(point + "\t");
		}
		System.out.println("\n");

		// ROZPOCZNIJ PROCES PRZESUWANIA NEURONÓW
		List<Point> noweNeurony = przesunNeuronyZwycieskie(hydra, neurony);

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
		if (writeToFile) {
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
		if (writeToFile) {
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
		// ZRÓB DEEP COPY OF THE ARRAYLIST
		List<Point> noweNeurony = Kohonen.deepCopy(neurony);

		// RANDOMIZE TRAINING SET
		trainingSet.shuffle();

		for (int i = 0; i < LICZBA_ITERACJI; i++) {
			Point input = trainingSet.get(i);
			input = input.getNormalized();
			Point uprawnionyZwyciezca = getZwyciezca(noweNeurony, input);
			Point teoretycznyZwyciezca = klasyfikuj(noweNeurony, input);
			ksiazkaKodowa.put(input, teoretycznyZwyciezca);
			double lambda = getPromienSasiedztwa(i);
			double learnRate = LEARNING_RATE
					* Math.exp(-(i / (double) LICZBA_ITERACJI));
			if (!Double.isFinite(learnRate))
				throw new ArithmeticException("Learning rate się sypnął.");

			// STOPIEŃ UAKTYWNIENIA NEURONÓW Z SĄSIEDZTWA ZALEŻY OD ODLEGŁOŚCI
			// ICH WEKTORÓW WAGOWYCH OD WAG NEURONU WYGRYWAJĄCEGO.
			int numer=0;
			StringBuilder sb = new StringBuilder();
			for (Point neuron : noweNeurony) {
				numer++;
				// ZE WZGLĘDU NA TO W JAKI SPOSÓB JEST NAPISANY MÓJ KOD, NALEŻY
				// UWAŻAĆ,
				// ABY NIE LICZYĆ ODLEGŁOŚCI OD PRZESUWAJĄCEGO SIĘ ZWYCIĘZCY
				// WIĘC ZROBIĘ GŁUPIĄ KOPIĘ
				Point tymczasowyZwyciezca = uprawnionyZwyciezca.clone();
				double dist = neuron.getEuclideanDistanceFrom(tymczasowyZwyciezca);
				sb.append("Odległość neuronu "+numer+" od neuronu zwycięzcy: "
						+ dist);
				if ((dist) < lambda) {
					double gauss = Math
							.exp(-((dist * dist) / (2.0 * (lambda * lambda))));
					if (!Double.isFinite(gauss))
						throw new ArithmeticException("gauss się sypnął.");
					if (0 > (gauss * learnRate) || (gauss * learnRate) > 1)
						throw new RuntimeException(
								"Współczynnik poza zakresem (0,1): " + gauss
										* learnRate);

					for (int wymiar = 0; wymiar < neuron.getCoordinates()
							.size(); wymiar++) {
						double waga = neuron.getCoordinate(wymiar);
						// METODA SUBRAKTYWNA
						double alpha = gauss * learnRate
								* (input.getCoordinate(wymiar) - waga);
						double nowaWaga = waga + alpha;
						neuron.setCoordinate(wymiar, nowaWaga);

//						sb.append("\nOdległość neuronu "+numer+" od neuronu zwycięzcy: "
//								+ dist);
						sb.append("\n\twymiar[" + wymiar + "] ");
						sb.append("waga N: " + waga);
						sb.append("\tWektor We: " + input.getCoordinate(wymiar));
						sb.append("\tWe-N: "
								+ (input.getCoordinate(wymiar) - waga));
						sb.append("\talpha: " + alpha);
						sb.append("\tnowaWaga: " + (waga + alpha));
						sb.append("\tdist: " + dist);
						sb.append("\tlambda: "+ lambda);
						sb.append("\tlearnRate: " + learnRate);
						sb.append("\tgauss: " + gauss);
					}
					sb.append("\n");
				}
				else {
					sb.append("\tprzekracza dopuszczalny promień sąsiedztwa: "+lambda+"\n");
				}
			}
			sb.append("----------------------------------------");
			System.out.println(sb);

			double drawJump = LICZBA_ITERACJI * (drawStepPercent / 100);
			if (i % drawJump == 0.0) {
				wizualizujObszaryVoronoia(noweNeurony, trainingSet);
				System.out.println("" + i + "\t learnRate: " + learnRate
						+ "\t lambda: " + lambda + "\t error: "
						+ ksiazkaKodowa.getBladKwantyzacji()
						+ "\n------------------------------------------------"
						+ "------------------------------------------------");
			}
		}
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

	public static double getPromienSasiedztwa(int iterNumber) {
		// def promienSasiedztwa(k):
		double wartoscPoczatkowa = 0.5;
		double kmax = LICZBA_ITERACJI;
		double wartoscMinimalna = 0.1;

		double result = wartoscPoczatkowa
				* Math.pow((wartoscMinimalna / wartoscPoczatkowa),
						(iterNumber / kmax));

		return result;
	}

	public static void setIterLimit(int limit) {
		LICZBA_ITERACJI = limit;
	}

	public static void setNeuronsAmount(int neuronsAmount) {
		PODZBIORY = neuronsAmount;
	}

	public static void setDrawStepPercent(int newDrawStepPercent) {
		drawStepPercent = newDrawStepPercent;
	}

	public static void writeToFile(boolean write) {
		writeToFile = write;
	}
}
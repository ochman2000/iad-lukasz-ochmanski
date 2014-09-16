package pl.lodz.p.iad;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import pl.lodz.p.iad.diagram.Voronoi3;
import pl.lodz.p.iad.diagram.Voronoi4;
import pl.lodz.p.iad.structure.KsiazkaKodowa;
import pl.lodz.p.iad.structure.Mapa;
import pl.lodz.p.iad.structure.Point;

public class Kohonen extends Diagram {

	/**
	 * Określa liczbę K centroidów, czyli liczbę klastrów. Tyle właśnie neuronów
	 * będzie w warstwie ukrytej. Jeśli ma to być lattice 40x40 powinieneś
	 * wpisać tutaj wartość 160;
	 */

	protected String EPOCH_LOG_CSV = "resources/kohonen/epoch_log.csv";
	protected String EPOCH_LOG_TXT = "resources/kohonen/epoch_log.txt";
	protected int NUMBER_OF_NEURONS = 8;
	protected double LEARNING_RATE = 0.1;
	protected double RADIUS = 0.6;
	protected int LIMIT_EPOK = 100;
	protected boolean LOG = true;
	protected double DRAW_STEP_IN_PERCENTS = 1.0;
	protected boolean NORMALIZATION = false;
	protected boolean WRITE_TO_FILE = true;
	protected Method METHOD = Method.WTM;

	public Kohonen() {
		epochLog = new StringBuilder();
		epochCSV = new StringBuilder();
		epochCSV.append("epoka;promień sąsiedztwa;współczynnik uczenia;"
				+ "błąd kwantyzacji\r\n");
	}
	
	public Kohonen(List<Integer> kolumny) {
		this();
		setKolumny(kolumny);
		teach();
	}
	
	public void teach() {
		if (NORMALIZATION) { hydra = hydra.getNormalized(); }
		if (wielkoscZbioruUczacego==0)
			wielkoscZbioruUczacego = hydra.size();
		neurons = new ArrayList<Point>(NUMBER_OF_NEURONS);
		voronoiBlackWhite = new Voronoi3();
		teach(hydra);
	}

	private void teach(Mapa hydra) {
		// LOSUJ K NEURONÓW (ZAMIAST INICJALIZOWAĆ PRZYPADKOWYMI WARTOŚCIAMI)
		Random rnd = new Random();
		while (neurons.size() < NUMBER_OF_NEURONS) {
			int indeks = rnd.nextInt(hydra.size());
			Point centroid = hydra.get(indeks);
//			if (NORMALIZATION) { centroid = centroid.getNormalized(); }
			centroid.setColor(Optional.of(Color.getHSBColor(
					(float) Math.random(), .7f, .7f)));
			if (!neurons.contains(centroid)) {
				neurons.add(centroid);
			}
		}

		// ROZPOCZNIJ PROCES PRZESUWANIA NEURONÓW
		System.out.println("Running Kohonen neural network, by Lukasz Ochmanski.");
		List<Point> noweNeurony = null;
		for (int epoka=0; epoka<LIMIT_EPOK; epoka++) {
			System.out.print(epoka+ "\t");
			epochLog.append(epoka+ "\t");
			epochCSV.append(epoka+";");
			noweNeurony = przesunNeuronyZwycieskie(hydra, neurons, epoka);
			
			//ZBUDUJ KSIĄŻKĘ KODOWĄ i WYLICZ BŁĄD
			double error = new KsiazkaKodowa(hydra, noweNeurony).getBladKwantyzacji();

			String msg = "error: " + error +"\r\n"
					+ "------------------------------------------------"
					+ "------------------------------------------------\r\n";
			System.out.print(msg);
			epochLog.append(msg);
			epochCSV.append(error+"\r\n");
			
			double drawJump = LIMIT_EPOK * (DRAW_STEP_IN_PERCENTS / 100);
			if (epoka % drawJump == 0.0) {
				wizualizujObszaryVoronoia(noweNeurony, hydra);
			}
		}
		rysujDiagramVoronoia(noweNeurony, hydra);
		saveAndClose();
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
		epochLog.append("Wsp. uczenia: "+learnRate+"\t");
		System.out.print("Wsp. uczenia: "+learnRate+"\t");
		epochCSV.append(learnRate+";");

		for (int i = 0; i < wielkoscZbioruUczacego; i++) {
			Point input = trainingSet.get(i);
//			if (NORMALIZATION) { input = input.getNormalized(); }
			Point uprawnionyZwyciezca = getZwyciezca(noweNeurony, input);
			for (Point neuron : noweNeurony) {
				Point tymczasowyZwyciezca = uprawnionyZwyciezca.clone();
				double dist = neuron.getEuclideanDistanceFrom(tymczasowyZwyciezca);
				double gauss = Math.exp(-((dist * dist) / (2.0 * (lambda * lambda))));
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

	public double getPromienSasiedztwa(int epochNumber) {
		double wartoscPoczatkowa = RADIUS;
		double kmax = LIMIT_EPOK;
		double wartoscMinimalna = 0.01;

		double result = wartoscPoczatkowa
				* Math.pow((wartoscMinimalna / wartoscPoczatkowa),
						(epochNumber / kmax));

		return result;
	}
	
	protected void rysujDiagramVoronoia(List<Point> centroidy, Mapa mapa) {
		voronoiColor = new Voronoi4();
		super.rysujDiagramVoronoia(centroidy, mapa);
	}
}

enum Method { WTA, WTM; }
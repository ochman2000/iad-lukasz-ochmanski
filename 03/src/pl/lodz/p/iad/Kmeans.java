package pl.lodz.p.iad;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pl.lodz.p.iad.diagram.Voronoi2;
import pl.lodz.p.iad.structure.KsiazkaKodowa;
import pl.lodz.p.iad.structure.Mapa;
import pl.lodz.p.iad.structure.Point;

public class Kmeans {
	
	private static final String EPOCH_LOG_CSV = "resources/kmeans/epoch_log.csv";
	private static final String EPOCH_LOG_TXT = "resources/kmeans/epoch_log.txt";
	private static final int NUMBER_OF_NEURONS = 8;
	/**
	 * Określa liczbę K centroidów.
	 */
	private int podzbiory;
	private boolean WRITE_TO_FILE = true;
	private boolean LOG = true;
	private String epochLogCSV;
	private String epochLogTxt;
	private List<Integer> kolumny;
	private List<Point> centroidy;
	private Mapa hydra;
	private Voronoi2 voronoi;
	private StringBuilder epochLog;
	private StringBuilder epochCSV;

	public Kmeans() {
		setEpochLogCSV(EPOCH_LOG_CSV);
		setEpochLogTxt(EPOCH_LOG_TXT);
		setPodzbiory(NUMBER_OF_NEURONS);
		epochLog = new StringBuilder();
		epochCSV = new StringBuilder();
		epochCSV.append("epoka;błąd kwantyzacji\r\n");
	}
	
	public Kmeans(List<Integer> kolumny) {
		this();
		setKolumny(kolumny);
		run();
	}
	
	public void run() {
		centroidy = new ArrayList<Point>(getPodzbiory());

		run(hydra);
	}

	public void run(Mapa hydra) {		
		//LOSUJ K CENTROIDÓW
		Random rnd = new Random();
		while (centroidy.size()<getPodzbiory()) {
			int indeks = rnd.nextInt(hydra.size());
			Point centroid = hydra.get(indeks);
			if (!centroidy.contains(centroid)) {
				centroid.setGroup(centroid);
				centroidy.add(centroid);
			}
		}
		System.out.print("Wylosowane centroidy:\t");
		for (Point point : hydra) {
			if (point.isCentroid())
			System.out.print(point+ "\t");
		}
		System.out.println("\n");
		
		//WYLICZ OPTYMALNE POZYCJE CENTROIDÓW
		hydra = grupujPunkty(hydra, centroidy);
		List<Point> noweCentroidy = przesunCentroidy(hydra, centroidy);
		int counter=0;
		
		while (!centroidySaIdentyczne(centroidy, noweCentroidy)) {
			System.out.println("Iteracja:\t"+ ++counter);
			epochLog.append(counter+"\t");
			epochCSV.append(counter+";");
			centroidy = noweCentroidy;
			hydra = grupujPunkty(hydra, centroidy);
			noweCentroidy = przesunCentroidy(hydra, centroidy);
			double error = new KsiazkaKodowa(hydra, centroidy).getBladKwantyzacji();
			System.out.println("Współrzędne centroidów: \t"+noweCentroidy);
			epochLog.append("błąd kwantyzacji: "+error+"\t"
						+ "Współrzędne centroidów: \t"+noweCentroidy+"\r\n"
						+ "------------------------------------------------"
						+ "------------------------------------------------\r\n");
			epochCSV.append(error+"\r\n");
		}
		rysujDiagramVoronoia(noweCentroidy, hydra);
		
		if (LOG) {
			Charset charset = StandardCharsets.UTF_8;
			try {
				BufferedWriter epochLogWriterTxt = Files.newBufferedWriter(
					Paths.get(epochLogTxt), charset);
				BufferedWriter epochLogWriterCsv = Files.newBufferedWriter(
					Paths.get(epochLogCSV), charset);
				epochLogWriterTxt.write(epochLog.toString());
				epochLogWriterCsv.write(epochCSV.toString());
				epochLogWriterTxt.close();
				epochLogWriterCsv.close();
			} catch (IOException x) {
				System.err.format("IOException: %s%n", x);
			}
		}
		System.out.println("Program terminated.");
	}
	
	private void rysujDiagramVoronoia(List<Point> centroidy, Mapa mapa) {
		voronoi = new Voronoi2(512, 512, 0);
		for (Point point : mapa) {			
			voronoi.dodajKropkę(point.getCoordinate(0),
					point.getCoordinate(1));
		}
		for (Point centroid : centroidy) {
			voronoi.dodajCentroid(centroid.getCoordinate(0),
					centroid.getCoordinate(1));
		}	
		if (WRITE_TO_FILE) {
			voronoi.saveVornoiToFile();
		}
	}
	
	private boolean centroidySaIdentyczne(List<Point> centroidy,
			List<Point> noweCentroidy) {
		for (int i = 0; i < centroidy.size(); i++) {
			if (!centroidy.get(i).equals(noweCentroidy.get(i)))
				return false;
		}
		return true;
	}

	/**
	 * Dla wszystkich punktów, które nie są centroidami, sprawdź odległości tych punktów
	 * od każdego centroida i wybierz ten centroid, którego odległość od tego punktu jest
	 * najmniejsza. Następnie przypisz ten punkt do grupy tego centroida.
	 * Złożoność obliczeniowa: O(p*c), gdzie p to liczba punktów (10000) a c to liczba
	 * centroidów (3).
	 */
	private Mapa grupujPunkty(Mapa mapa, List<Point> centroidy) {
		for (Point punkt : mapa) {
			double min = Double.MAX_VALUE;
			for (Point centroid : centroidy) {
				double dist = punkt.getDistanceFrom(centroid);
				if (dist<min) {
					min=dist;
					punkt.setGroup(centroid);
				}
			}
		}
		return mapa;
	}
	
	/**
	 * Dla każdej grupy centroidów, wylicz średnią dla każdego wymiaru, biorąc pod uwagę
	 * tylko te elementy, które nie są centroidami i należą do danej grupy centroida.
	 * Następnie przesuń centroida w każdą z możliwych wymiarów o tą wyliczoną wartość.
	 */
	private List<Point> przesunCentroidy(Mapa mapa, List<Point> centroidy) {
		List<Point> noweCentroidy = new ArrayList<Point>(getPodzbiory());
		for (Point centroid : centroidy) {
			Point nowyCentroid = new Point(centroid.getCoordinates().size());
			for (int i=0; i<centroid.getCoordinates().size(); i++) {
				double sum = 0.0;
				int count = 0;
				for (Point punkt : mapa) {
					if (punkt.isCentroid()==false) {
						if (punkt.getGroup().get()==centroid) {
							sum +=punkt.getCoordinate(i);
							count++;
						}
					}
				}
				double x = sum/count;
				nowyCentroid.addCoordinate(i, x);
				nowyCentroid.setGroup(nowyCentroid);
			}
			noweCentroidy.add(nowyCentroid);
		}
		return noweCentroidy;
	}
	
	public void setKolumny(List<Integer> kolumny) {
		this.kolumny = kolumny;
		hydra = new Mapa(this.kolumny);
	}

	public String getEpochLogCSV() {
		return epochLogCSV;
	}

	public void setEpochLogCSV(String epochLogCSV) {
		this.epochLogCSV = epochLogCSV;
	}
	
	public String getEpochLogTxt() {
		return epochLogTxt;
	}

	public void setEpochLogTxt(String epochLogTxt) {
		this.epochLogTxt = epochLogTxt;
	}

	public int getPodzbiory() {
		return podzbiory;
	}

	public void setPodzbiory(int podzbiory) {
		this.podzbiory = podzbiory;
	}
	
	public void close()	{
		voronoi.close();
	}

}

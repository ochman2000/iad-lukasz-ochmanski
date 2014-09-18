package pl.lodz.p.iad;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import pl.lodz.p.iad.diagram.VoronoiBlackAndWhite;
import pl.lodz.p.iad.diagram.VoronoiColor;
import pl.lodz.p.iad.structure.Mapa;
import pl.lodz.p.iad.structure.Point;

public class Diagram {

	private String epochLogCSV;
	private String epochLogTxt;
	private int numberOfNeurons;
	private double learningRate;
	private double radius;
	private int limitEpok;
	private boolean log;
	private double drawStepInPercents;
	private boolean normalized;
	private boolean writeToFile;
	private Method method;
	private VoronoiColor voronoiColor;
	private VoronoiBlackAndWhite voronoiBlackWhite;
	private StringBuilder epochLog;
	private StringBuilder epochCSV;
	private Mapa hydra;
	private List<Integer> kolumny;
	private List<Point> neurons;
	private int wielkoscZbioruUczacego;

	protected void wizualizujObszaryVoronoia(List<Point> centroidy, Mapa mapa) {
		voronoiBlackWhite.clear();
		for (Point point : mapa) {
			voronoiBlackWhite.dodajKropkę(point.getCoordinate(0),
					point.getCoordinate(1));
		}
		for (Point centroid : centroidy) {
			voronoiBlackWhite.dodajCentroid(centroid.getCoordinate(0),
					centroid.getCoordinate(1));
		}
		voronoiBlackWhite.drawMe();
		if (isWriteToFile()) {
			voronoiBlackWhite.saveVornoiToFile();
		}
	}

	protected void rysujDiagramVoronoia(List<Point> centroidy, Mapa mapa) {
		for (Point point : mapa) {
			voronoiColor.dodajKropkę(point.getCoordinate(0),
					point.getCoordinate(1));
		}
		for (Point centroid : centroidy) {
			voronoiColor.dodajCentroid(
					centroid.getCoordinate(0),
					centroid.getCoordinate(1),
					centroid.getColor().orElseThrow(
							IllegalArgumentException::new));
		}
		voronoiColor.drawMe();
		if (isWriteToFile()) {
			voronoiColor.saveVornoiToFile();
		}
	}

	protected void saveAndClose() {
		if (isLog()) {
			Charset charset = StandardCharsets.UTF_8;
			try {
				BufferedWriter epochLogWriterTxt = Files.newBufferedWriter(
						Paths.get(getEpochLogTxt()), charset);
				BufferedWriter epochLogWriterCsv = Files.newBufferedWriter(
						Paths.get(getEpochLogCSV()), charset);
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

	public int getNumberOfNeurons() {
		return numberOfNeurons;
	}

	public void setNumberOfNeurons(int numberOfNeurons) {
		this.numberOfNeurons = numberOfNeurons;
	}

	public double getLearningRate() {
		return learningRate;
	}

	public void setLearningRate(double learningRate) {
		this.learningRate = learningRate;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public int getLimitEpok() {
		return limitEpok;
	}

	public void setLimitEpok(int limitEpok) {
		this.limitEpok = limitEpok;
	}

	public boolean isLog() {
		return log;
	}

	public void setLog(boolean log) {
		this.log = log;
	}

	public double getDrawStepInPercents() {
		return drawStepInPercents;
	}

	public void setDrawStepInPercents(double drawStepInPercents) {
		this.drawStepInPercents = drawStepInPercents;
	}

	public boolean isNormalized() {
		return normalized;
	}

	public void setNormalization(boolean normalization) {
		this.normalized = normalization;
	}

	public boolean isWriteToFile() {
		return writeToFile;
	}

	public void setWriteToFile(boolean writeToFile) {
		this.writeToFile = writeToFile;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public VoronoiColor getVoronoiColor() {
		return voronoiColor;
	}

	public void setVoronoiColor(VoronoiColor voronoiColor) {
		this.voronoiColor = voronoiColor;
	}

	public VoronoiBlackAndWhite getVoronoiBlackWhite() {
		return voronoiBlackWhite;
	}

	public void setVoronoiBlackWhite(VoronoiBlackAndWhite voronoiBlackWhite) {
		this.voronoiBlackWhite = voronoiBlackWhite;
	}

	public StringBuilder getEpochLog() {
		return epochLog;
	}

	public void setEpochLog(StringBuilder epochLog) {
		this.epochLog = epochLog;
	}

	public StringBuilder getEpochCSV() {
		return epochCSV;
	}

	public void setEpochCSV(StringBuilder epochCSV) {
		this.epochCSV = epochCSV;
	}

	public List<Point> getNeurons() {
		return neurons;
	}

	public void setNeurons(List<Point> neurons) {
		this.neurons = neurons;
	}

	public int getWielkoscZbioruUczacego() {
		return wielkoscZbioruUczacego;
	}

	public void setWielkoscZbioruUczacego(int wielkoscZbioruUczacego) {
		this.wielkoscZbioruUczacego = wielkoscZbioruUczacego;
	}

	public Mapa getHydra() {
		return hydra;
	}

	public void setHydra(Mapa hydra) {
		this.hydra = hydra;
	}

	public List<Integer> getKolumny() {
		return kolumny;
	}
}

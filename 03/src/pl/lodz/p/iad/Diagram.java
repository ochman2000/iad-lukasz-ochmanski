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

	protected String EPOCH_LOG_CSV;
	protected String EPOCH_LOG_TXT;
	protected int NUMBER_OF_NEURONS = 8;
	protected double LEARNING_RATE = 0.1;
	protected double RADIUS = 0.6;
	protected int LIMIT_EPOK = 100;
	protected boolean LOG = true;
	protected double DRAW_STEP_IN_PERCENTS = 1.0;
	protected boolean NORMALIZATION = false;
	protected boolean WRITE_TO_FILE = true;
	protected Method METHOD = Method.WTM;
	protected VoronoiColor voronoiColor;
	protected VoronoiBlackAndWhite voronoiBlackWhite;
	protected StringBuilder epochLog;
	protected StringBuilder epochCSV;
	protected Mapa hydra;
	protected List<Integer> kolumny;
	protected List<Point> neurons;
	protected int wielkoscZbioruUczacego = 0;
	
	
	protected void wizualizujObszaryVoronoia(List<Point> centroidy, Mapa mapa) {
		voronoiBlackWhite.clear();
		for (Point point : mapa) {
			voronoiBlackWhite.dodajKropkę(point.getCoordinate(0), point.getCoordinate(1));
		}
		for (Point centroid : centroidy) {
			voronoiBlackWhite.dodajCentroid(
					centroid.getCoordinate(0),
					centroid.getCoordinate(1));
		}
		voronoiBlackWhite.drawMe();
		if (WRITE_TO_FILE) {
			voronoiBlackWhite.saveVornoiToFile();
		}
	}
	
	protected void rysujDiagramVoronoia(List<Point> centroidy, Mapa mapa) {
		for (Point point : mapa) {
			voronoiColor.dodajKropkę(point.getCoordinate(0), point.getCoordinate(1));
		}
		for (Point centroid : centroidy) {
			voronoiColor.dodajCentroid(
					centroid.getCoordinate(0),
					centroid.getCoordinate(1),
					centroid.getColor().orElseThrow(
							IllegalArgumentException::new));
		}
		voronoiColor.drawMe();
		if (WRITE_TO_FILE) {
			voronoiColor.saveVornoiToFile();
		}
	}
	
	protected void saveAndClose() {
		if (LOG) {
			Charset charset = StandardCharsets.UTF_8;
			try {
				BufferedWriter epochLogWriterTxt = Files.newBufferedWriter(
					Paths.get(EPOCH_LOG_TXT), charset);
				BufferedWriter epochLogWriterCsv = Files.newBufferedWriter(
					Paths.get(EPOCH_LOG_CSV), charset);
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
	
	public void setNeuronsAmount(int neuronsAmount) {
		NUMBER_OF_NEURONS = neuronsAmount;
	}
	
	public void writeToFile(boolean write) {
		WRITE_TO_FILE= write;
	}
	
	public void setDRAW_STEP_IN_PERCENT(int newDRAW_STEP_IN_PERCENT) {
		DRAW_STEP_IN_PERCENTS = newDRAW_STEP_IN_PERCENT;
	}
	
	public void setKolumny(List<Integer> kolumny) {
		this.kolumny = kolumny;
		hydra = new Mapa(this.kolumny);
	}
}

package pl.lodz.p.iad.structure;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

public class Mapa extends ArrayList<Point>{
	
	private final static int LICZBA_KOLUMN = 8;
	private static final long serialVersionUID = 1L;
	private int liczbaWymiarow = 0;

	public Mapa() {
		super();
	}
	public Mapa(List<Integer> kolumny) {
		setLiczbaWymiarow(kolumny.size());
		Path file = Paths.get("resources/hydra01.dane");
		List<String> lines = null;
		try {
			lines = Files.readAllLines(file);
		} catch (IOException x) {
			System.err.format("IOException: %s%n", x);
		}
		
		for (String line : lines) {
			StringTokenizer linia = new StringTokenizer(line, ",");
			Point punkt = new Point(kolumny.size());
			int m=0;
			for (int i=0; i<LICZBA_KOLUMN; i++) {
				if (kolumny.contains(i)) {
					String token = linia.nextToken();
					double value = Double.parseDouble(token);
					punkt.addCoordinate(m++, value);
	//				System.out.print("Punkt nr "+ i +": "+String.format("%.2f", value)+"\t");
				}
			}
			this.add(punkt);
		}
	}
	
	public String toString() {
		StringBuilder s= new StringBuilder();
		for (Point punkt : this) {
			StringBuilder info = null;
			if (punkt.isCentroid()) {
				info = new StringBuilder("Jest centroidem");
			}
			else {
				info = new StringBuilder("należy do grupy centroida: ");
				info.append(punkt.getCoordinatesTrimmed());
			}
			if (!punkt.getGroup().isPresent()) {
				info = new StringBuilder("Nie jest przypisany do żadnej grupy centroidów.");
			}
			s.append(""+punkt.getCoordinatesTrimmed());
			s.append("\t:\t");
			s.append(info);
			s.append("\n");
		}
		return s.toString();
	}
	
	public void shuffle() {
//		List<Point> l = new LinkedList<Point>();
		Random rnd = new Random();
		for (int i = 0; i < this.size(); i++) {
			int index = rnd.nextInt(i + 1);
			// Simple swap
			Point a = this.get(index);
			this.set(index, this.get(i));
			this.set(i, a);
		}	
	}
	
	public Mapa getNormalized() {
		Mapa normalized = new Mapa();
		for (Point point : this) {
			normalized.add(point.getNormalized());
		}
		if (normalized.get(0).getCoordinates().size() != getLiczbaWymiarow())
			throw new RuntimeException("Niezgodna liczba wymiarów: "
					+ normalized.get(0).getCoordinates().size());
		return normalized;
	}
	
	public Mapa getScaled(double factor) {
		Mapa scaled = new Mapa();
		for (Point p : this) {
			scaled.add(p.getScaled(factor));
		}
		if (scaled.get(0).getCoordinates().size() != getLiczbaWymiarow())
			throw new RuntimeException("Niezgodna liczba wymiarów: "
					+ scaled.get(0).getCoordinates().size());
		return scaled;
	}
	
	public int getLiczbaWymiarow() {
		return liczbaWymiarow;
	}
	public void setLiczbaWymiarow(int liczbaWymiarow) {
		this.liczbaWymiarow = liczbaWymiarow;
	}
}
package pl.lodz.p.iad.structure;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Mapa extends ArrayList<Point>{
	
	private final static int DIMENSIONS = 2;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Mapa() {
		Path file = Paths.get("resources/hydra01.dane");
		List<String> lines = null;
		try {
			lines = Files.readAllLines(file);
		} catch (IOException x) {
			System.err.format("IOException: %s%n", x);
		}
		for (String line : lines) {
			StringTokenizer linia = new StringTokenizer(line, ",");
			Point punkt = new Point(DIMENSIONS);
			for (int i=0; i<DIMENSIONS; i++) {
				String token = linia.nextToken();
				double value = Double.parseDouble(token);
				punkt.addCoordinate(i, value);
				System.out.print("Punkt nr "+ i +": "+token+"\t");
			}
			this.add(punkt);
			System.out.println();
		}
	}
}
package pl.lodz.p.iad.diagram;

public class Voronoi6 extends Voronoi4 {

	public void keyTyped(char c) {
		draw.save("resources/neuralgas/voronoi" + c + ".png");
	}

	public void saveVornoiToFile() {
		draw.save("resources/neuralgas/voronoiFinal.png");
		vornoiCounter++;
	}
}

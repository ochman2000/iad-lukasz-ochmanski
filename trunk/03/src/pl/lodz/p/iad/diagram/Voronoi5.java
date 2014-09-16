package pl.lodz.p.iad.diagram;

public class Voronoi5 extends Voronoi3 {
	
	public Voronoi5() {
		super("Algorytm gazu neuronowego");
	}
	
	public void keyTyped(char c) {
		draw.save("resources/neuralgas/voronoi" + c + ".png");
	}

	public void saveVornoiToFile() {
		draw.save("resources/neuralgas/voronoi" + vornoiCounter + ".png");
		vornoiCounter++;
	}
}

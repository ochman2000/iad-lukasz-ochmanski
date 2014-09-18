package pl.lodz.p.iad.diagram;

public interface VoronoiBlackAndWhite {
	public void dodajCentroid(double x1, double y1);
    public void dodajKropkę(double x1, double y1);
    public void clear();
    public void drawMe();
    public void saveVornoiToFile();
    public void close();
}

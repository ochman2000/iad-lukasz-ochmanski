package pl.lodz.p.iad.diagram;

import java.awt.Color;

public interface VoronoiColor {
	public void dodajCentroid(double x1, double y1, Color color);
    public void dodajKropkę(double x1, double y1);
//    public void clear();
    public void drawMe();
    public void saveVornoiToFile();
}

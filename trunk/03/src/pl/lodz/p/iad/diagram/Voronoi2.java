package pl.lodz.p.iad.diagram;

/*************************************************************************
 *  Compilation:  javac Voronoi.java
 *  Execution:    java Voronoi
 *  Dependencies: Draw.java DrawListener.java
 * 
 *  Plots the points that the user clicks, and draws the Voronoi diagram.
 *  We assume the points lie on an M-by-M grid and use a brute force
 *  discretized algorithm. Each insertion takes time proportional to M^2.
 *
 *  Limitations
 *  -----------
 *    - Running time scales (badly) with M
 *    - Fortune's algorithm can compute a Voronoi diagram on N 
 *      points in time proportional to N log N, but it is 
 *      subtantially more complicated than this program which is intended
 *      to demonstrate callbacks and GUI operations.
 *
 *************************************************************************/

import java.awt.Color;

public class Voronoi2 implements DrawListener {
    private static int SIZE = 512;
    private Point[][] nearest = new Point[SIZE][SIZE];  // which point is pixel (i, j) nearest?
    private int vornoiCounter = 0;
    private Draw draw = new Draw("Algorytm k-średnich");

    public Voronoi2() {
    	this(SIZE, SIZE, 0);
    }
    
    public Voronoi2(int w, int h, int zoom) {
    	draw.setCanvasSize(w, h);
    	draw.setXscale(0, w);
    	draw.setYscale(0, h);
    	draw.addListener(this);
    	draw.show(0);    	
    }
    
    public void dodajCentroid(double x1, double y1) {
    	double x = x1*100+256;
    	double y = y1*100+256;
    	mousePressed(x, y);
    }
    
    public void dodajKropkę(double x1, double y1) {
    	draw.setPenColor(Color.WHITE);
    	double x = x1*100+256;
    	double y = y1*100+256;
    	draw.filledCircle(x, y, 1);
     //   draw.show(0);
    }
    public void drawMe(){
    	draw.show(0);
    }

    public void mousePressed(double x, double y) {
        Point p = new Point(x, y);
//        System.out.println("Inserting:       " + p);

        // compare each pixel (i, j) and find nearest point
        draw.setPenColor(Color.getHSBColor((float) Math.random(), .7f, .7f));
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Point q = new Point(i, j);
                if ((nearest[i][j] == null) || (q.distanceTo(p) < q.distanceTo(nearest[i][j]))) {
                    nearest[i][j] = p;
                    draw.filledSquare(i+0.5, j+0.5, 0.5);
                }
            }
        }

        // draw the point afterwards
        draw.setPenColor(Color.BLACK);
        draw.filledCircle(x, y, 4);
        draw.show(0);
//        System.out.println("Done processing: " + p);
    }


    // save the screen to a file
    public void keyTyped(char c) { draw.save("resources/kmeans/voronoi" + c + ".png"); }
    public void saveVornoiToFile() { 
    	draw.save("resources/kmeans/voronoi" + vornoiCounter + ".png"); 
    	vornoiCounter++;
    	}
    // must implement these since they're part of the interface
    public void mouseDragged(double x, double y)  { }
    public void mouseReleased(double x, double y) { }

	public void close() {
		draw.kill();
	}
}

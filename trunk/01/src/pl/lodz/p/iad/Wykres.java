package pl.lodz.p.iad;

import java.io.File;
import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;


public class Wykres extends ApplicationFrame {

    public static String HISTOGRAM_PNG = "histogram.png";
    private static int bins = 25;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Wykres(int column) {
        super("Inteligentna analiza danych");
        IntervalXYDataset dataset = createDataset(column);
        JFreeChart chart = createChart(dataset);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(600, 400));
        setContentPane(chartPanel);
    }
    
    private IntervalXYDataset createDataset(int column) {
    	HistogramDataset dataset = new HistogramDataset();
    	ArrayList<Double> C1 = DataAnalyzer.getIris(DataAnalyzer.KLASA[0],column);
    	double[] values1 = new double[C1.size()];
		for (int i=0; i<C1.size(); i++) {
    		values1[i] = C1.get(i);
    	}
    	dataset.addSeries(DataAnalyzer.KLASA[0], values1, bins);
        
    	ArrayList<Double> C2 = DataAnalyzer.getIris(DataAnalyzer.KLASA[1], column);
    	double[] values2 = new double[C2.size()];
		for (int i=0; i<C2.size(); i++) {
    		values2[i] = C2.get(i);
    	}
    	dataset.addSeries(DataAnalyzer.KLASA[1], values2, bins);
    	
    	ArrayList<Double> C3 = DataAnalyzer.getIris(DataAnalyzer.KLASA[2], column);
    	double[] values3 = new double[C3.size()];
		for (int i=0; i<C3.size(); i++) {
    		values3[i] = C3.get(i);
    	}
    	dataset.addSeries(DataAnalyzer.KLASA[2], values3, bins);
    	
        return dataset;
    }

    private JFreeChart createChart(IntervalXYDataset dataset) {
        final JFreeChart chart = ChartFactory.createHistogram(
            "Histogram",
            "Przedziały klasowe", 
            "Populacja próby", 
            dataset,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );
        try {
            ChartUtilities.saveChartAsPNG(new File(HISTOGRAM_PNG), chart, 600, 400);
          } catch (Exception e) {
            e.printStackTrace();
          }
        XYPlot plot = (XYPlot) chart.getPlot();
//        final IntervalMarker target = new IntervalMarker(1.0, 4.0);
//        target.setLabel("Target Range");
//        target.setLabelFont(new Font("SansSerif", Font.ITALIC, 11));
//        target.setLabelAnchor(RectangleAnchor.LEFT);
//        target.setLabelTextAnchor(TextAnchor.HALF_ASCENT_CENTER);
//        target.setPaint(new Color(222, 222, 255, 128));
//        plot.addRangeMarker(target, Layer.BACKGROUND);
        return chart;    
    }

    public static void main(final String[] args) {

        final Wykres demo = new Wykres(0);
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);

    }

}
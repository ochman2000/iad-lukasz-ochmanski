package pl.lodz.p.iad;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

public class DataAnalyzerTest {

	@Test
	public void testSredniaArytmetyczna() {
		ArrayList<Double> oceny = new ArrayList<>(Arrays.asList(3.0, 4.0, 5.0, 6.0));
		double actual = DataAnalyzer.sredniaArytmetyczna(oceny);
		assertEquals(4.5, actual, 0.00000000);
	}

	@Test
	public void testSredniaHarmoniczna() {
		ArrayList<Double> oceny = new ArrayList<>(Arrays.asList(3.0, 4.0, 5.0, 6.0));
		double actual = DataAnalyzer.sredniaHarmoniczna(oceny);
		assertEquals(4.2105, actual, 0.0001);
	}

	@Test
	public void testSredniaKwadratowa() {
		ArrayList<Double> oceny = new ArrayList<>(Arrays.asList(3.0, 4.0, 5.0, 6.0));
		double actual = DataAnalyzer.sredniaKwadratowa(oceny);
		assertEquals(4.6368, actual, 0.0001);
	}

	@Test
	public void testSredniaGeometryczna() {
		ArrayList<Double> oceny = new ArrayList<>(Arrays.asList(3.0, 4.0, 5.0, 6.0));
		double actual = DataAnalyzer.sredniaGeometryczna(oceny);
		assertEquals(4.3559, actual, 0.0001);
	}
	
	@Test
	public void testDominanta() {
		ArrayList<Double> oceny = new ArrayList<>(Arrays.asList(3.0, 4.0, 5.0, 4.0, 6.0, 6.0, 4.0));
		double actual = DataAnalyzer.dominanta(oceny);
		assertEquals(4.0, actual, 0.0);
	}
	
	@Test
	public void testPercentyl01() {
		ArrayList<Double> oceny = new ArrayList<>(Arrays.asList(3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0));
		double actual = DataAnalyzer.percentyl(oceny, 0.50);
		assertEquals(6.0, actual, 0.0);
	}
	
	@Test
	public void testPercentyl02() {
		ArrayList<Double> oceny = new ArrayList<>(Arrays.asList(3.0, 4.0, 5.0, 6.0, 7.0, 8.0));
		double actual = DataAnalyzer.percentyl(oceny, 0.50);
		assertEquals(5.5, actual, 0.0);
	}
	
	@Test
	public void testPercentyl03() {
		ArrayList<Double> oceny = new ArrayList<>(Arrays.asList(9.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0));
		double actual = DataAnalyzer.percentyl(oceny, 0.50);
		assertEquals(6.0, actual, 0.0);
	}
	
	@Test
	public void testPercentyl04() {
		ArrayList<Double> oceny = new ArrayList<>(Arrays.asList(3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0));
		double actual = DataAnalyzer.percentyl(oceny, 0.25);
		assertEquals(4.5, actual, 0.0);
	}
	
	@Test
	public void testPercentyl05() {
		ArrayList<Double> oceny = new ArrayList<>(Arrays.asList(3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0));
		double actual = DataAnalyzer.percentyl(oceny, 0.25);
		assertEquals(4.0, actual, 0.0);
	}
	
	@Test
	public void testWspolczynnikKoncentracji01() {
		ArrayList<Double> oceny = new ArrayList<>(Arrays.asList(1.0, 2.0, 3.0, 4.0));
		double actual = DataAnalyzer.wspolczynnikKoncentracjiLorenza(oceny);
		assertEquals(0.6, actual, 0.000001);
	}
}

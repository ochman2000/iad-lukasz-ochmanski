package pl.lodz.p.iad.structure;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import pl.lodz.p.iad.Kohonen;

public class PointTest {

	@Test
	public void test01() {
		Point p1 = new Point(2);
		p1.addCoordinate(0, 2.3);
		p1.addCoordinate(1, -4.0);
		
		Point p2 = new Point(2);
		p2.addCoordinate(0, 2.3);
		p2.addCoordinate(1, -4.0);
		p2.setGroup(p1);
		
		assertFalse(p2.isCentroid());
	}

	@Test
	public void test02() {
		Point p1 = new Point(2);
		p1.addCoordinate(0, 2.3);
		p1.addCoordinate(1, -4.0);
		
		Point p2 = new Point(2);
		p2.addCoordinate(0, 2.3);
		p2.addCoordinate(1, -4.0);
		p2.setGroup(p2);
		
		assertTrue(p2.isCentroid());
	}
	
	@Test
	public void test03() {
		Point p1 = new Point(2);
		p1.addCoordinate(0, 3.0);
		p1.addCoordinate(1, 0.0);
		p1.addCoordinate(2, 0.0);
		
		Point p2 = new Point(2);
		p2.addCoordinate(0, 0);
		p2.addCoordinate(1, -4.0);
		p2.addCoordinate(2, 0.0);
		p2.setGroup(p2);
		
		assertEquals(5.0, p2.getDistanceFrom(p1), 0.00001);
	}
	
	@Test
	public void test04() {
		Point p1 = new Point(2);
		p1.addCoordinate(0, 0.0);
		p1.addCoordinate(1, 0.0);
		p1.addCoordinate(2, 0.0);
		
		Point p2 = new Point(2);
		p2.addCoordinate(0, 0.0);
		p2.addCoordinate(1, 0.0);
//		p2.addCoordinate(2, 0.0);
		p2.setGroup(p2);
		
		assertFalse(p1.equals(p2));
	}
	
	@Test
	public void test05() {
		Point p1 = new Point(2);
		p1.addCoordinate(0, 0.000_001);
		p1.addCoordinate(1, 0.0);
		p1.addCoordinate(2, 0.0);
		
		Point p2 = new Point(2);
		p2.addCoordinate(0, 0.0);
		p2.addCoordinate(1, 0.0);
		p2.addCoordinate(2, 0.0);
		p2.setGroup(p2);
		
//		System.out.println(p1.hashCode());
//		System.out.println(p2.hashCode());
		assertFalse(p1.equals(p2));
	}
	
	@Test
	public void test06() {
		Point p1 = new Point(2);
		p1.addCoordinate(0, 0.000_000_1);
		p1.addCoordinate(1, 0.0);
		p1.addCoordinate(2, 0.0);
		
		Point p2 = new Point(2);
		p2.addCoordinate(0, 0.0);
		p2.addCoordinate(1, 0.0);
		p2.addCoordinate(2, 0.0);
		p2.setGroup(p2);
		
//		System.out.println(p1.hashCode());
//		System.out.println(p2.hashCode());
		assertTrue(p1.equals(p2));
	}
	
	@Test
	public void test08() {
		Point p1 = new Point(2);
		p1.addCoordinate(0, 3.0);
		p1.addCoordinate(1, 0.0);
		p1.addCoordinate(2, 0.0);
		
		Point p2 = new Point(2);
		p2.addCoordinate(0, 3.0);
		p2.addCoordinate(1, 0.0);
		p2.addCoordinate(2, 0.0);
		p2.setGroup(p2);
		
		assertTrue(p1.equals(p2));
	}
	
	@Test
	public void test09() {
		List<Point> lista = new ArrayList<Point>(2);
		Point p1 = new Point(2);
		p1.addCoordinate(0, 3.0);
		p1.addCoordinate(1, 0.0);
		p1.addCoordinate(2, 0.0);
		
		lista.add(p1);
		
		Point p2 = new Point(2);
		p2.addCoordinate(0, 3.0);
		p2.addCoordinate(1, 0.0);
		p2.addCoordinate(2, 0.0);
		p2.setGroup(p2);
		
		assertTrue(lista.contains(p1));
	}
	
	@Test
	public void test10() {
		List<Point> lista = new ArrayList<Point>(2);
		Point p1 = new Point(2);
		p1.addCoordinate(0, 3.0);
		p1.addCoordinate(1, 0.0);
		p1.addCoordinate(2, 0.0);
		
		lista.add(p1);
		
		Point p2 = new Point(2);
		p2.addCoordinate(0, 3.0);
		p2.addCoordinate(1, 0.0);
		p2.addCoordinate(2, 0.0);
		p2.setGroup(p2);
		
		assertTrue(lista.contains(p2));
	}
	
	@Test
	public void test11() {
		List<Point> lista = new ArrayList<Point>(2);
		Point p1 = new Point(2);
		p1.addCoordinate(0, 3.0);
		p1.addCoordinate(1, 0.0);
		p1.addCoordinate(2, 0.0);
		
		lista.add(p1);
		
		Point p2 = new Point(2);
		p2.addCoordinate(0, 3.0);
		p2.addCoordinate(1, 0.0);
//		p2.addCoordinate(2, 0.0);
		p2.setGroup(p2);
		
		assertFalse(lista.contains(p2));
	}
	
	@Rule
	public ExpectedException expectedEx1 = ExpectedException.none();

	@Test
	public void test12() {
		List<Point> lista = new ArrayList<Point>(2);
		Point p1 = new Point(2);
		p1.addCoordinate(0, 3.0);
		p1.addCoordinate(1, 0.0);
		p1.addCoordinate(2, 0.0);
		
		lista.add(p1);
		
		Point p2 = new Point(2);
		p2.addCoordinate(0, 3.0);
		p2.addCoordinate(1, 0.0);
//		p2.addCoordinate(2, 0.0);
		expectedEx1.expect(IllegalArgumentException.class);
	    expectedEx1.expectMessage("Liczba wymiarów jest niezgodna");
		p1.setGroup(p2);
		
		assertFalse(lista.contains(p2));
	}
	
	@Test
	public void test13() {
		Point p1 = new Point(2);
		p1.addCoordinate(0, 3.0);
		p1.addCoordinate(1, 0.0);
		p1.addCoordinate(2, 0.0);
		
		Point p2 = p1.clone();
		
		assertTrue(p1.equals(p2));
	}
	
	@Test
	public void test14() {
		Point p1 = new Point(2);
		p1.addCoordinate(0, 3.0);
		p1.addCoordinate(1, 0.0);
		p1.addCoordinate(2, 0.0);
		
		Point p2 = p1.clone();
		p2.addCoordinate(3, 2.1);
		assertFalse(p1.equals(p2));
	}
	
	@Test
	public void test15() {
		List<Point> lista1 = new ArrayList<Point>(2);
		Point p1 = new Point(2);
		p1.addCoordinate(0, 3.0);
		p1.addCoordinate(1, 0.0);
		p1.addCoordinate(2, 0.0);
		
		List<Point> lista2 = Kohonen.deepCopy(lista1);
		
		assertTrue(lista1.equals(lista2));
	}
	
	@Test
	public void test16() {
		List<Point> lista1 = new ArrayList<Point>(2);
		Point p1 = new Point(2);
		p1.addCoordinate(0, 3.0);
		p1.addCoordinate(1, 0.0);
		p1.addCoordinate(2, 0.0);
		
		Point p2 = new Point(2);
		p2.addCoordinate(0, 3.0);
		p2.addCoordinate(1, 0.1);
		p2.addCoordinate(2, 0.0);
		p2.setGroup(p2);
		
		List<Point> lista2 = Kohonen.deepCopy(lista1);
		lista2.add(p2);
		
		assertFalse(lista1.equals(lista2));
	}
	
	@Rule
	public ExpectedException expectedEx2 = ExpectedException.none();
	
	@Test
	public void test17() {
		Point p1 = new Point(2);
		p1.addCoordinate(0, 3.0);
		p1.addCoordinate(1, 0.0);
		p1.addCoordinate(2, 0.0);
		expectedEx2.expect(IllegalArgumentException.class);
	    expectedEx2.expectMessage("Neuron nie może być przypisany do grupy null.");
	    p1.setGroup(null);
	}
	
	/**
	 * The input vector x = ( 1.2, -2.3, 3.4, -4.55, 5.6 ) is normalized as follows:
	 * c = 1.0 / sqrt( 1.22 + ( -2.3 )2 + 3.42 + ( -4.55 )2 + 5.62 ) = 0.1192
	 * x = ( 1.2* 0.1192, (-2.3)*0.1192, 3.4*0.1192, (-4.55)*0.1192, 5.6*0.1192 )
	 * x = ( 0.1430, -0.2742, 0.4053, -0.5424, 0.6675 )
	 */
	@Test
	public void test18() {
		Point p1 = new Point(5);
		p1.addCoordinate(0, 1.2);
		p1.addCoordinate(1, -2.3);
		p1.addCoordinate(2, 3.4);
		p1.addCoordinate(3, -4.55);
		p1.addCoordinate(4, 5.6);
		
		p1 = p1.getNormalized();
		
		assertEquals(-0.2742, p1.getCoordinate(1), 0.0001);
	}
	
	@Test
	public void test19() {
		Point p1 = new Point(5);
		p1.addCoordinate(0, 1.2);
		p1.addCoordinate(1, -2.3);
		p1.addCoordinate(2, 3.4);
		p1.addCoordinate(3, -4.55);
		p1.addCoordinate(4, 5.6);
	
		assertTrue(p1.getWon()==0);
	}
	
	@Test
	public void test20() {
		Point p1 = new Point(5);
		p1.addCoordinate(0, 1.2);
		p1.addCoordinate(1, -2.3);
		p1.addCoordinate(2, 3.4);
		p1.addCoordinate(3, -4.55);
		p1.addCoordinate(4, 5.6);
	
		p1.odnotujZwyciestwo();
		assertTrue(p1.getWon()==1);
	}
	
	@Test
	public void test21() {
		Point p1 = new Point(5);
		p1.addCoordinate(0, 1.2);
		p1.addCoordinate(1, -2.3);
		p1.addCoordinate(2, 3.4);
		p1.addCoordinate(3, -4.55);
		p1.addCoordinate(4, 5.6);
	
		p1.odnotujZwyciestwo();
		p1.odnotujZwyciestwo();
		assertTrue(p1.getWon()== -1);
	}
	
	@Test
	public void test22() {
		Point p1 = new Point(5);
		p1.addCoordinate(0, 1.2);
		p1.addCoordinate(1, -2.3);
		p1.addCoordinate(2, 3.4);
		p1.addCoordinate(3, -4.55);
		p1.addCoordinate(4, 5.6);
	
		p1.odnotujZwyciestwo();
		p1.odnotujZwyciestwo();
		p1.odnotujZwyciestwo();
		assertTrue(p1.getWon()==0);
	}
	
	@Test
	public void test23() {
		double wynik = Math.exp(-((0)/(2.0*(1.0*1.0))));
		
		assertEquals(1.0, wynik, 0.0001);
	}
	
	@Test
	public void test24() {

		Point p1 = new Point(2);
		p1.addCoordinate(0, 1.0);
		p1.addCoordinate(1, 1.0);

		
		Point p2 = new Point(2);
		p2.addCoordinate(0, 2.0);
		p2.addCoordinate(1, 2.0);

		
		Point p3 = new Point(2);
		p3.addCoordinate(0, 3.0);
		p3.addCoordinate(1, 3.0);

		
		List<Point> neurony = new ArrayList<Point>(3);
		neurony.add(p1);
		neurony.add(p2);
		neurony.add(p3);
		
		Point input = new Point(2);
		input.addCoordinate(0, 4.0);
		input.addCoordinate(1, 4.0);
		
		Point winner = Kohonen.klasyfikuj(neurony, input);
		assertTrue(winner==p3);
	}
	
	@Test
	public void test25() {
		Point n1 = new Point(2);
		n1.addCoordinate(0, 1.0);
		n1.addCoordinate(1, 1.0);
		
		Point n2 = new Point(2);
		n2.addCoordinate(0, 3.0);
		n2.addCoordinate(1, 3.0);

		Point n3 = new Point(2);
		n3.addCoordinate(0, 5.0);
		n3.addCoordinate(1, 5.0);
		
		List<Point> neurony = new ArrayList<Point>(3);
		neurony.add(n1);
		neurony.add(n2);
		neurony.add(n3);

		KsiazkaKodowa ksiazkaKodowa = new KsiazkaKodowa(3);
		Point p1 = new Point(2);
		p1.addCoordinate(0, 1.5);
		p1.addCoordinate(1, 1.5);
		ksiazkaKodowa.put(p1, Kohonen.klasyfikuj(neurony, p1));
		
		Point p2 = new Point(2);
		p2.addCoordinate(0, 3.5);
		p2.addCoordinate(1, 3.5);
		ksiazkaKodowa.put(p2, Kohonen.klasyfikuj(neurony, p2));

		Point p3 = new Point(2);
		p3.addCoordinate(0, 5.5);
		p3.addCoordinate(1, 5.5);
		ksiazkaKodowa.put(p3, Kohonen.klasyfikuj(neurony, p3));
		
		Point input = new Point(2);
		input.addCoordinate(0, 3.5);
		input.addCoordinate(1, 3.5);
				
		Point winner = ksiazkaKodowa.get(input);
		
		assertTrue(winner==n2);
	}
	
	@Test
	public void test26() {
		Point n1 = new Point(2);
		n1.addCoordinate(0, 3.0);
		n1.addCoordinate(1, 4.0);
		
		Point n2 = new Point(2);
		n2.addCoordinate(0, 30.0);
		n2.addCoordinate(1, 30.0);

		Point n3 = new Point(2);
		n3.addCoordinate(0, 50.0);
		n3.addCoordinate(1, 50.0);
		
		List<Point> neurony = new ArrayList<Point>(3);
		neurony.add(n1);
		neurony.add(n2);
		neurony.add(n3);

		KsiazkaKodowa ksiazkaKodowa = new KsiazkaKodowa(3);
		Point p1 = new Point(2);
		p1.addCoordinate(0, 0.0);
		p1.addCoordinate(1, 0.0);
		ksiazkaKodowa.put(p1, Kohonen.klasyfikuj(neurony, p1));
		
		Point p2 = new Point(2);
		p2.addCoordinate(0, 30.5);
		p2.addCoordinate(1, 30.5);
		ksiazkaKodowa.put(p2, Kohonen.klasyfikuj(neurony, p2));

		Point p3 = new Point(2);
		p3.addCoordinate(0, 50.5);
		p3.addCoordinate(1, 50.5);
		ksiazkaKodowa.put(p3, Kohonen.klasyfikuj(neurony, p3));
		
		Point input = new Point(2);
		input.addCoordinate(0, 0.0);
		input.addCoordinate(1, 0.0);
				
		double blad = ksiazkaKodowa.getBlad(p1);
		
		assertEquals(5.0, blad, 0.00001);
	}
	
	@Test
	public void test27() {
		Point n1 = new Point(2);
		n1.addCoordinate(0, 3.0);
		n1.addCoordinate(1, 4.0);
		
		Point n2 = new Point(2);
		n2.addCoordinate(0, 30.0);
		n2.addCoordinate(1, 30.0);

		Point n3 = new Point(2);
		n3.addCoordinate(0, 50.0);
		n3.addCoordinate(1, 50.0);
		
		List<Point> neurony = new ArrayList<Point>(3);
		neurony.add(n1);
		neurony.add(n2);
		neurony.add(n3);

		KsiazkaKodowa ksiazkaKodowa = new KsiazkaKodowa(3);
		Point p1 = new Point(2);
		p1.addCoordinate(0, 0.0);
		p1.addCoordinate(1, 0.0);
		ksiazkaKodowa.put(p1, Kohonen.klasyfikuj(neurony, p1));
		
		Point p2 = new Point(2);
		p2.addCoordinate(0, 30.5);
		p2.addCoordinate(1, 30.5);
		ksiazkaKodowa.put(p2, Kohonen.klasyfikuj(neurony, p2));

		Point p3 = new Point(2);
		p3.addCoordinate(0, 50.5);
		p3.addCoordinate(1, 50.5);
		ksiazkaKodowa.put(p3, Kohonen.klasyfikuj(neurony, p3));
		
		Point input = new Point(2);
		input.addCoordinate(0, 0.0);
		input.addCoordinate(1, 0.0);
				
		double blad = ksiazkaKodowa.getBlad(p1);
		blad += ksiazkaKodowa.getBlad(p2);
		blad += ksiazkaKodowa.getBlad(p3);
		
		assertEquals(ksiazkaKodowa.getBladKwantyzacji(), blad/3, 0.00001);
	}
}

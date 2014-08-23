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
	public ExpectedException expectedEx = ExpectedException.none();

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
		expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("Liczba wymiarów jest niezgodna");
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
		
		Point p2 = new Point(2);
		p2.addCoordinate(0, 3.0);
		p2.addCoordinate(1, 0.1);
		p2.addCoordinate(2, 0.0);
		p2.setGroup(p2);
		
		List<Point> lista2 = Kohonen.deepCopy(lista1);
		
		assertTrue(lista1.equals(lista2));
	}
}

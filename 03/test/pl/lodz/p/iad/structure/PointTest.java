package pl.lodz.p.iad.structure;

import static org.junit.Assert.*;

import org.junit.Test;

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
}
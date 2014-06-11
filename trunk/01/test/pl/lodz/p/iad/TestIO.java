package pl.lodz.p.iad;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestIO {

	@Test
	public void testSredniaArytmetyczna() {
		double actual = DataAnalyzer.getAlpha();
		assertEquals(0.001, actual, 0.0);
	}
}

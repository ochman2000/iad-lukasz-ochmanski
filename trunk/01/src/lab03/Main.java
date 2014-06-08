package lab03;

import java.util.Random;

public class Main {
	
	
	public static void main(String[] args) {
		
		double[][] kwadrat = new double[100][100];
		for (int i = 0; i < 20; i++) {
			System.out.println(losuj());
		}
	}
	
	public static double losuj() {
		Random r = new Random();
		int c = r.nextInt(20);
		c=c-10;
		Double d = r.nextDouble();
		return c+d;
	}
}

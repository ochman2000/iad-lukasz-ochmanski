package lab02;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Problem01 {
	
	private static double w1 = -1.0; //to będzie się zmieniało
	private static double w2 = -1.0; //
	//jak przestanie się zmieniać

	//wartośc oczekiwana dla pliku a wynosi 1
	
	//wartość oczekiwana dla pliku b wynosi -1
	
	
	public static void main(String[] args) {
		
		File file = new File("dane0/a");
		int minusowe=0;
		int plusowe=0;
		
		Scanner sc = null;
		try {
			sc = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while (sc.hasNext()) {
			double a = sc.nextDouble();
			double b = sc.nextDouble();
			double x = Math.signum(a*(w1)+b*(w2));
			if (x<0.0)
				minusowe++;
			if (x>0.0) {
				plusowe++;
			} else {
//				System.err.println("Coś nie śmiga...");
			}
		}
		System.out.println("Liczba -1: "+minusowe);
		System.out.println("Liczba  1: "+plusowe);
	}
}

package lab01;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Problem01 {

	public Problem01() {

	}

	/*
	 * Zadanie 1 Napisaæ funkcjê która przyjmuje dwa parametry: -iloœæ liczb
	 * wygenerowanych. -nazwê pliku do którego to siê zapisze.
	 */
	public void generateNumbers(int amount, String filename) {
		Random rn = new Random();
		FileWriter outputStream = null;
		try {
			outputStream = new FileWriter(filename);
			for (int i = 0; i < amount; i++) {
				double liczba = rn.nextDouble();
//				System.out.println(liczba);
				outputStream.write(liczba + "\r\n");
			}
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Problem01 lab = new Problem01();
//		lab.generateNumbers(20, "liczby.txt");
		lab.generateNumbers(Integer.parseInt(args[0]), args[1]);
	}
}

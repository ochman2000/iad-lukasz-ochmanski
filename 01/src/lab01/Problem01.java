package lab01;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Problem01 {

	public Problem01() {

	}

	/*
	 * Zadanie 1 Napisa� funkcj� kt�ra przyjmuje dwa parametry: -ilo�� liczb
	 * wygenerowanych. -nazw� pliku do kt�rego to si� zapisze.
	 */
	public void generateNumbers(int amount, String filename) {
		Random rn = new Random();
		FileWriter outputStream = null;
		try {
			outputStream = new FileWriter(filename);
			for (int i = 0; i < amount; i++) {
				double liczba = rn.nextDouble();
				outputStream.write(liczba + "\r\n");
			}
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Problem01 lab = new Problem01();
		lab.generateNumbers(Integer.parseInt(args[0]), args[1]);
	}
}
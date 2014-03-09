package lab01;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Problem02 {

	public Problem02() {	}

	/*
	 * Zadanie 2 Wczytaæ z pliku zbiór liczb i wczytaæ œredni¹ arytmetyczn¹.
	 */
	public Double getAvg(String filename) {
		ArrayList<Double> list = new ArrayList<Double>();
        BufferedReader inputStream = null;
        double suma=0.0;
        try {
            inputStream = new BufferedReader(new FileReader(filename));
			String l;
			while ((l = inputStream.readLine()) != null) {
				list.add(Double.parseDouble(l));
			}
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        for (Double x : list) {
			suma=+x;
		}
		return suma/(list.size());
	}

	public static void main(String[] args) {
		Problem02 lab = new Problem02();
//		Double srednia = lab.getAvg("liczby.txt");
//		System.out.println(srednia);
		Double srednia = lab.getAvg(args[0]);
		System.out.println("\r\nSrednia arytmetyczna: "+srednia);
	}
}

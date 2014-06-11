package pl.lodz.p.iad;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class DataAnalyzer {

	private static final String CHARSET = "UTF-8";
	public static String TOKEN = ",";
	public static String TEST_FILE = "183566/iris.dane";
	public static String INFO_FILE = "183566/183566.info";
	public static int COLUMN = 3;
	public static String[] NAZWA = {"sepal length", "sepal width",
		"petal length", "petal width"};
	public static String KLASA[]  = {"Iris-setosa", "Iris-versicolor", "Iris-virginica"};
		
	/**
	 * Podaj numer parametru (od 0 do 3)
	 * @param val
	 * @return
	 */
	public static ArrayList<Double> getIris(String klasa, int val) {
		ArrayList<Double> a = new ArrayList<>();
		Path file = Paths.get(TEST_FILE);
		Charset charset = Charset.forName(CHARSET);
		try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
		    String line = null;
		    while ((line = reader.readLine()) != null) {
		    	StringTokenizer st = new StringTokenizer(line, TOKEN, false);
				double x=0.0;
				for (int i=0; i<val+1; i++){
					x=Double.parseDouble(st.nextToken());
				}
				for (int i=0; i<3-val; i++){
					st.nextToken();
				}

				String y = st.nextToken();
				if (klasa.equals(y))
					a.add(x);
		    }
		} catch (IOException x) {
		    System.err.format("IOException: %s%n", x);
		}
		return a;
	}
	
	
	public static double getAlpha() {
		double alpha = 0;
		Path file = Paths.get(INFO_FILE);
		Charset charset = Charset.forName(CHARSET);
		try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
		    String line = null;
		    line = reader.readLine();
	    	StringTokenizer st = new StringTokenizer(line, TOKEN, false);
			st.nextToken("=");
			alpha = Double.parseDouble(st.nextToken());
		} catch (IOException x) {
		    System.err.format("IOException: %s%n", x);
		}
		return alpha;
	}
	
	public static boolean getOgon() {
		boolean ogon = false;
		Path file = Paths.get(INFO_FILE);
		Charset charset = Charset.forName(CHARSET);
		try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
		    String line = null;
		    line = reader.readLine();
		    line = reader.readLine();
	    	if (("Rozkład jednostronny").equals(line))
	    		ogon=true;
	    	else if (("Rozkład dwustronny").equals(line)) {
				ogon=false;
			} else {
				System.err.format("Nieznany format rozkładu.");
			} 
		} catch (IOException x) {
		    System.err.format("IOException: %s%n", x);
		}
		return ogon;
	}
	
	public static double sredniaArytmetyczna(ArrayList<Double> list) {
		double sum=0.0;
		for (Double x : list) {
			sum+=x;
		}
		return sum/(list.size());
	}
	
	public static double sredniaHarmoniczna(ArrayList<Double> list) {
        double sum = 0.0;
        for (Double x : list) {
            sum += 1.0 / x;
        }
        return list.size()/sum;
    }
	
	public static double sredniaKwadratowa(ArrayList<Double> list) {
        double ms = 0;
        for (Double x : list) {
            ms += x*x;
        }
        ms /= list.size();
        return Math.sqrt(ms);
    }
	
	public static double sredniaGeometryczna(ArrayList<Double> list) {
		double sum = list.get(0);		 
		for (int i=1; i<list.size(); i++) {
	        sum *= list.get(i);
	    }
	    return Math.pow(sum, 1.0/list.size());
	}
	
//	1.Miary średnie 
//	klasyczne
//	 średnia arytmetyczna
//	 średnia harmoniczna
//	 średnia geometryczna
//	pozycyjne
//	 dominanta
//	 kwantyle
	
	public static List<Double> dominanty(ArrayList<Double> list) {
	    final List<Double> modes = new ArrayList<Double>();
	    final Map<Double, Integer> countMap = new HashMap<Double, Integer>();
	    int max = -1;
	    for (Double n : list) {
	        int count = 0;
	        if (countMap.containsKey(n)) {
	            count = countMap.get(n) + 1;
	        } else {
	            count = 1;
	        }
	        countMap.put(n, count);
	        if (count > max) {
	            max = count;
	        }
	    }
	    for (final Map.Entry<Double, Integer> tuple : countMap.entrySet()) {
	        if (tuple.getValue() == max) {
	            modes.add(tuple.getKey());
	        }
	    }
	    return modes;
	}
	
	public static double dominanta(ArrayList<Double> list) {
		return DataAnalyzer.dominanty(list).get(0);
	}
	
	public static double percentyl(ArrayList<Double> list, double kwant) {
		int n = list.size();
		double percentlyl=0.0;
		Collections.sort(list);
		if ((n*kwant)==(int)(n*kwant)) {
			int index = (int) (n*kwant)-1;
			percentlyl = (list.get(index)+list.get(index+1))/2;
		}
		else {
			int index = (int) Math.ceil(n*kwant)-1;
			percentlyl = list.get(index);
		}
		return percentlyl;
	}
	
	public static double mediana(ArrayList<Double> list) {
		return percentyl(list, 0.5);
	}
//	
//	2. Miary zmienności/rozproszenia:
//	pozycyjne
//	 rozstęp (amplituda, empiryczny obszar zmienności)
//	 odchylenie ćwiartkowe
//	 współczynnik zmienności
//	klasyczne
//	 odchylenie standardowe
//	 wariancja
//	 odchylenie przeciętne
//	 współczynnik zmienności
	
	public static double rozstep(ArrayList<Double> list) {
		return Collections.max(list)-Collections.min(list);
	}
	
	public static double odchylenieCwiartkowe(ArrayList<Double> list) {
		return (percentyl(list, 0.75)-percentyl(list, 0.25))/2;
	}
	
	public static double wspolczynnikZmiennosci1(ArrayList<Double> list) {
		return odchylenieStandardowe(list)/sredniaArytmetyczna(list)*100;
	}
	public static double wspolczynnikZmiennosci2(ArrayList<Double> list) {
		return odchyleniePrzecietne(list)/sredniaArytmetyczna(list)*100;
	}
	public static double wspolczynnikZmiennosci3(ArrayList<Double> list) {
		return odchylenieCwiartkowe(list)/mediana(list)*100;
	}
	public static double wspolczynnikZmiennosci4(ArrayList<Double> list) {
		double licznik = percentyl(list, 0.75)-percentyl(list, 0.25);
		double mianownik = percentyl(list, 0.75)+percentyl(list, 0.25);
		return licznik/mianownik*100;
	}
	
	public static double odchylenieStandardowe(ArrayList<Double> list) {
		return Math.sqrt(wariancja(list));
	}
	
	public static double wariancja(ArrayList<Double> list) {
		double avg = sredniaArytmetyczna(list);
		double sum = 0.0;
		for (Double x : list) {
			sum += (x-avg)*(x-avg);
		}
		return sum/list.size();
	}
	
	public static double odchyleniePrzecietne(ArrayList<Double> list) {
		double avg = sredniaArytmetyczna(list);
		double sum = 0.0;
		for (Double x : list) {
			sum = sum + Math.abs(x-avg);
		}
		return sum/list.size();
	}
	
//	4. Miary asymetrii
//	 wskaźnik asymetrii (skośności):
//	 współczynnik asymetrii (skośności)
//	 momenty
	
	public static double wskaznikSkosnosci(ArrayList<Double> list) {
		double D = dominanta(list);
		double x= sredniaArytmetyczna(list);
		return x-D;
	}
	
	public static boolean isRozkladLewostronny(ArrayList<Double> list) {
		return (wskaznikSkosnosci(list)<0) ? true : false;
	}
	
	public static boolean isRozkladPrawostronny(ArrayList<Double> list) {
		return (wskaznikSkosnosci(list)>0) ? true : false;
	}
	
	public static boolean isRozkladSymetryczny(ArrayList<Double> list) {
		return (wskaznikSkosnosci(list)==0) ? true : false;
	}
	
	/**
	 * Jaką część odchylenia standardowego stanowi różnica między
	 * średnią arytmetyczną i dominantą. 
	 * @param list
	 * @return
	 */
	public static double wspolczynnikSkosnosci1(ArrayList<Double> list) {
		double x = sredniaArytmetyczna(list);
		double D = dominanta(list);
		double s = odchyleniePrzecietne(list);
		return (x-D)/s;
	}
	
	/**
	 * Jaką część odchylenia przeciętnego stanowi różnica między
	 * średnią arytmetyczną i dominantą.
	 * @param list
	 * @return
	 */
	public static double wspolczynnikSkosnosci2(ArrayList<Double> list) {
		double x = sredniaArytmetyczna(list);
		double D = dominanta(list);
		double d = odchylenieStandardowe(list);
		return (x-D)/d;
	}

	/**
	 * Pozycjny współczynnik asymetrii - jest miarą uzupełniającą ponieważ,
	 * określa kierunek i siłę asymetrii jednostek znajdujących się w drugiej
	 * i trzeciej ćwiartce obszaru zmiennośći, a więc w zawężonej "przestrzeni"
	 * @param list
	 * @return
	 */
	public static double wspolczynnikSkosnosci3(ArrayList<Double> list) {
		double Q1 = percentyl(list, 0.25);
		double Q2 = percentyl(list, 0.5);
		double Q3 = percentyl(list, 0.75);
		return ((Q3-Q2)-(Q2-Q1))/((Q3-Q2)+(Q2-Q1));
	}
	
	/**
	 * Dla szeregów dokładnie symetrycznych m3=0
	 * W rozkładach o asymetrii prawostronnej m3>0
	 * W rozkładach o asymetrii lewostronnej m3<0
	 * Moment centralny trzeciego rzędu informuje jedynie o kierunku asymetrii.
	 * @param list
	 * @param rzad
	 * @return
	 */
	public static double momentCentralny(ArrayList<Double> list, int rzad) {
		double x=sredniaArytmetyczna(list);
		double sum=0.0;
		for (Double xi : list) {
			sum+=(Math.pow((xi-x), rzad));
		}
		return sum/list.size();
	}
	
	/**
	 * W celu uzyskania miary asymetrii informującej zarówno o kierunku
	 * jak i o sile asymetrii należy obliczyć stosunek wartości momentu
	 * centralnego trzeciego rzędu do sześcianu odchylenia standardowego.
	 * Przybiera on wartości zawarte w przedziale od -1 do 1.
	 * @param list
	 * @return
	 */
	public static double momentStandaryzowanyTrzeciegoRzedu(ArrayList<Double> list) {
		double m3 = momentCentralny(list, 3);
		double s3 = Math.pow(odchylenieStandardowe(list), 3);
		return m3/s3;
	}
	
//	5. Miary koncentracji
//	 nierównomierny podział zjawiska w zbiorowości
//	 koncentracja zbiorowości wokół średniej (kurtoza)
//	 brak koncentracji
//	 koncentracja zupełna
	
	public static double wspolczynnikKoncentracjiLorenza(ArrayList<Double> list) {
		double sum = 0.0;
		double pole=0.0;
		for (Double x : list) {
			sum+=x;
			pole+=sum;
		}
		double powiekszenie = sum/100.0;
		sum = sum/powiekszenie;
		pole = pole/(powiekszenie*powiekszenie);
		return (5000-pole)/5000;
	}
	
	/**
	 * Współczynnik koncentracji zbiorowości wokół średniej (tzw. kurtoza)
	 * @param list
	 * @return
	 */
	public static double kurtoza(ArrayList<Double> list) {
		double m4 = momentCentralny(list, 4);
		double s4 = Math.pow(odchylenieStandardowe(list), 4);
		return m4/s4;
	}

	public static double getValueZ(ArrayList<Double> listaA, ArrayList<Double> listaB) {
		double m1 = sredniaArytmetyczna(listaA);
		double m2 = sredniaArytmetyczna(listaB);
		double s1 = odchylenieStandardowe(listaA);
		double s2 = odchylenieStandardowe(listaB);
		double n1 = listaA.size();
		double n2 = listaB.size();
		
		return (m1-m2)/(Math.sqrt((s1/n1)+(s2/n2)));
	}
	
	public static double wartoscP(double z, boolean jednostronny) {
		if (jednostronny)
			z = z/2;
		return Gaussian.PhiInverse(1-z);
	}
	
	public static boolean odrzucHipotezeZerowa(ArrayList<Double> listaA, ArrayList<Double> listaB,
			double alpha, boolean ogon) {
		if (Math.abs(getValueZ(listaA, listaB))>=wartoscP(alpha, ogon))
			return true;
		else
			return false;
	}
	
	public static boolean odrzucHipotezeAlternatywna(ArrayList<Double> listaA, ArrayList<Double> listaB,
			double alpha, boolean ogon) {
		if (Math.abs(getValueZ(listaA, listaB))>=wartoscP(alpha, ogon))
			return false;
		else
			return true;
	}
}

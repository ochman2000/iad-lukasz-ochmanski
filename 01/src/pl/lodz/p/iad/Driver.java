package pl.lodz.p.iad;

import java.util.ArrayList;


public class Driver {

	public static void main(String[] args) {

		if (args.length>0)
			DataAnalyzer.TEST_FILE = args[0];
		if (args.length>1)
			DataAnalyzer.INFO_FILE = args[1];
		if (args.length>2)
			Wykres.HISTOGRAM_PNG = args[2];
		if (args.length>3)
			DataAnalyzer.TOKEN = args[3];
		if (args.length>4)
			DataAnalyzer.COLUMN = Integer.parseInt(args[4]);
		
		ArrayList<Double> listA = DataAnalyzer.getIris(DataAnalyzer.KLASA[0], DataAnalyzer.COLUMN);
		ArrayList<Double> listB = DataAnalyzer.getIris(DataAnalyzer.KLASA[1], DataAnalyzer.COLUMN);
		ArrayList<Double> listC = DataAnalyzer.getIris(DataAnalyzer.KLASA[2], DataAnalyzer.COLUMN);
		@SuppressWarnings("unused")
		Wykres histogram = new Wykres(DataAnalyzer.COLUMN);
		
		//KLASA C1 "Iris-setosa"
		System.out.println("====================================================");
		System.out.println("KLASA C1 "+DataAnalyzer.KLASA[0]+"\t BADANA CECHA: "+
				DataAnalyzer.NAZWA[DataAnalyzer.COLUMN]);
		System.out.println("Liczebność próby (n) \t= "+listA.size());
		System.out.println("Średnia arytmetyczna \t= "+DataAnalyzer.sredniaArytmetyczna(listA));
		System.out.println("Odchylenie standardowe \t= "+DataAnalyzer.odchylenieStandardowe(listA));
		System.out.println("Średnia geometryczna \t= "+DataAnalyzer.sredniaGeometryczna(listA));
		System.out.println("Średnia harmoniczna \t= "+DataAnalyzer.sredniaHarmoniczna(listA));
		System.out.println("Średnia kwadratowa \t= "+DataAnalyzer.sredniaKwadratowa(listA));
		System.out.println("Moda/dominanta \t\t= "+DataAnalyzer.dominanta(listA));
		System.out.println("Pierwszy kwartyl \t= "+DataAnalyzer.percentyl(listA, 0.25));
		System.out.println("Mediana \t\t= "+DataAnalyzer.mediana(listA));
		System.out.println("Trzeci kwartyl \t\t= "+DataAnalyzer.percentyl(listA, 0.75));
		System.out.println("Odchylenie ćwiartkowe \t= "+DataAnalyzer.odchylenieCwiartkowe(listA));
		System.out.println("Odchylenie standardowe \t= "+DataAnalyzer.odchylenieStandardowe(listA));
		System.out.println("Odchylenie przeciętne \t= "+DataAnalyzer.odchyleniePrzecietne(listA));		
		System.out.println("Rozstęp ćwiartkowy \t= "+DataAnalyzer.rozstep(listA));
		System.out.println("Wariancja \t\t= "+DataAnalyzer.wariancja(listA));
		System.out.println("Moment centralny trzeciego rzędu \t= "+DataAnalyzer.momentCentralny(listA, 3));
		System.out.println("Moment standaryzowany trzeciego rzędu \t= "+DataAnalyzer.momentStandaryzowanyTrzeciegoRzedu(listA));
		System.out.println("Moment standaryzowany czwartego rzędu \t= "+DataAnalyzer.kurtoza(listA));
		System.out.println("Wskaźnik asymetrii\t\t= "+DataAnalyzer.wskaznikSkosnosci(listA));
		System.out.println("Czy rozkład jest lewostronny:\t"+DataAnalyzer.isRozkladLewostronny(listA));
		System.out.println("Czy rozkład jest prawostronny:\t"+DataAnalyzer.isRozkladPrawostronny(listA));
		System.out.println("Czy rozkład jest symetryczny:\t"+DataAnalyzer.isRozkladSymetryczny(listA));
		System.out.println("Współczynnik Skośności (s) \t= "+DataAnalyzer.wspolczynnikSkosnosci1(listA));
		System.out.println("Współczynnik Skośności (d) \t= "+DataAnalyzer.wspolczynnikSkosnosci2(listA));
		System.out.println("Pozycyjny współczynnik asymetrii \t= "+DataAnalyzer.wspolczynnikSkosnosci3(listA));
		System.out.println("Klasyczny współczynnik zmienności Vs \t= "+DataAnalyzer.wspolczynnikZmiennosci1(listA));
		System.out.println("Klasyczny współczynnik zmienności Vd \t= "+DataAnalyzer.wspolczynnikZmiennosci2(listA));
		System.out.println("Pozycyjny współczynnik zmienności VQ \t= "+DataAnalyzer.wspolczynnikZmiennosci3(listA));
		System.out.println("Pozycyjny wsp. zmienności VQ1Q3 \t= "+DataAnalyzer.wspolczynnikZmiennosci4(listA));
		System.out.println("Współczynnik koncentracji Lorenza \t= "+DataAnalyzer.wspolczynnikKoncentracjiLorenza(listA));
		
		
		//KLASA C2 "Iris-versicolor"
		System.out.println("\n\r\n\r====================================================");
		System.out.println("KLASA C2 "+DataAnalyzer.KLASA[1]+"\t BADANA CECHA: "+
				DataAnalyzer.NAZWA[DataAnalyzer.COLUMN]);
		System.out.println("Liczebność próby (n) \t= "+listB.size());
		System.out.println("Średnia arytmetyczna \t= "+DataAnalyzer.sredniaArytmetyczna(listB));
		System.out.println("Odchylenie standardowe \t= "+DataAnalyzer.odchylenieStandardowe(listB));
		System.out.println("Średnia geometryczna \t= "+DataAnalyzer.sredniaGeometryczna(listB));
		System.out.println("Średnia harmoniczna \t= "+DataAnalyzer.sredniaHarmoniczna(listB));
		System.out.println("Średnia kwadratowa \t= "+DataAnalyzer.sredniaKwadratowa(listB));
		System.out.println("Moda/dominanta \t\t= "+DataAnalyzer.dominanta(listB));
		System.out.println("Pierwszy kwartyl \t= "+DataAnalyzer.percentyl(listB, 0.25));
		System.out.println("Mediana \t\t= "+DataAnalyzer.mediana(listB));
		System.out.println("Trzeci kwartyl \t\t= "+DataAnalyzer.percentyl(listB, 0.75));
		System.out.println("Odchylenie ćwiartkowe \t= "+DataAnalyzer.odchylenieCwiartkowe(listB));
		System.out.println("Odchylenie standardowe \t= "+DataAnalyzer.odchylenieStandardowe(listB));
		System.out.println("Odchylenie przeciętne \t= "+DataAnalyzer.odchyleniePrzecietne(listB));		
		System.out.println("Rozstęp ćwiartkowy \t= "+DataAnalyzer.rozstep(listB));
		System.out.println("Wariancja \t\t= "+DataAnalyzer.wariancja(listB));
		System.out.println("Moment centralny trzeciego rzędu \t= "+DataAnalyzer.momentCentralny(listB, 3));
		System.out.println("Moment standaryzowany trzeciego rzędu \t= "+DataAnalyzer.momentStandaryzowanyTrzeciegoRzedu(listB));
		System.out.println("Moment standaryzowany czwartego rzędu \t= "+DataAnalyzer.kurtoza(listB));
		System.out.println("Wskaźnik asymetrii\t\t= "+DataAnalyzer.wskaznikSkosnosci(listB));
		System.out.println("Czy rozkład jest lewostronny:\t"+DataAnalyzer.isRozkladLewostronny(listB));
		System.out.println("Czy rozkład jest prawostronny:\t"+DataAnalyzer.isRozkladPrawostronny(listB));
		System.out.println("Czy rozkład jest symetryczny:\t"+DataAnalyzer.isRozkladSymetryczny(listB));
		System.out.println("Współczynnik Skośności (s) \t= "+DataAnalyzer.wspolczynnikSkosnosci1(listB));
		System.out.println("Współczynnik Skośności (d) \t= "+DataAnalyzer.wspolczynnikSkosnosci2(listB));
		System.out.println("Pozycyjny współczynnik asymetrii \t= "+DataAnalyzer.wspolczynnikSkosnosci3(listB));
		System.out.println("Klasyczny współczynnik zmienności Vs \t= "+DataAnalyzer.wspolczynnikZmiennosci1(listB));
		System.out.println("Klasyczny współczynnik zmienności Vd \t= "+DataAnalyzer.wspolczynnikZmiennosci2(listB));
		System.out.println("Pozycyjny współczynnik zmienności VQ \t= "+DataAnalyzer.wspolczynnikZmiennosci3(listB));
		System.out.println("Pozycyjny wsp. zmienności VQ1Q3 \t= "+DataAnalyzer.wspolczynnikZmiennosci4(listB));
		System.out.println("Współczynnik koncentracji Lorenza \t= "+DataAnalyzer.wspolczynnikKoncentracjiLorenza(listB));
		
		//KLASA C3 "Iris-virginica"
		System.out.println("\n\r\n\r====================================================");
		System.out.println("KLASA C3 "+DataAnalyzer.KLASA[2]+"\t BADANA CECHA: "+
				DataAnalyzer.NAZWA[DataAnalyzer.COLUMN]);
		System.out.println("Liczebność próby (n) \t= "+listC.size());
		System.out.println("Średnia arytmetyczna \t= "+DataAnalyzer.sredniaArytmetyczna(listC));
		System.out.println("Odchylenie standardowe \t= "+DataAnalyzer.odchylenieStandardowe(listC));
		System.out.println("Średnia geometryczna \t= "+DataAnalyzer.sredniaGeometryczna(listC));
		System.out.println("Średnia harmoniczna \t= "+DataAnalyzer.sredniaHarmoniczna(listC));
		System.out.println("Średnia kwadratowa \t= "+DataAnalyzer.sredniaKwadratowa(listC));
		System.out.println("Moda/dominanta \t\t= "+DataAnalyzer.dominanta(listC));
		System.out.println("Pierwszy kwartyl \t= "+DataAnalyzer.percentyl(listC, 0.25));
		System.out.println("Mediana \t\t= "+DataAnalyzer.mediana(listC));
		System.out.println("Trzeci kwartyl \t\t= "+DataAnalyzer.percentyl(listC, 0.75));
		System.out.println("Odchylenie ćwiartkowe \t= "+DataAnalyzer.odchylenieCwiartkowe(listC));
		System.out.println("Odchylenie standardowe \t= "+DataAnalyzer.odchylenieStandardowe(listC));
		System.out.println("Odchylenie przeciętne \t= "+DataAnalyzer.odchyleniePrzecietne(listC));		
		System.out.println("Rozstęp ćwiartkowy \t= "+DataAnalyzer.rozstep(listC));
		System.out.println("Wariancja \t\t= "+DataAnalyzer.wariancja(listC));
		System.out.println("Moment centralny trzeciego rzędu \t= "+DataAnalyzer.momentCentralny(listC, 3));
		System.out.println("Moment standaryzowany trzeciego rzędu \t= "+DataAnalyzer.momentStandaryzowanyTrzeciegoRzedu(listC));
		System.out.println("Moment standaryzowany czwartego rzędu \t= "+DataAnalyzer.kurtoza(listC));
		System.out.println("Wskaźnik asymetrii\t\t= "+DataAnalyzer.wskaznikSkosnosci(listC));
		System.out.println("Czy rozkład jest lewostronny:\t"+DataAnalyzer.isRozkladLewostronny(listC));
		System.out.println("Czy rozkład jest prawostronny:\t"+DataAnalyzer.isRozkladPrawostronny(listC));
		System.out.println("Czy rozkład jest symetryczny:\t"+DataAnalyzer.isRozkladSymetryczny(listC));
		System.out.println("Współczynnik Skośności (s) \t= "+DataAnalyzer.wspolczynnikSkosnosci1(listC));
		System.out.println("Współczynnik Skośności (d) \t= "+DataAnalyzer.wspolczynnikSkosnosci2(listC));
		System.out.println("Pozycyjny współczynnik asymetrii \t= "+DataAnalyzer.wspolczynnikSkosnosci3(listC));
		System.out.println("Klasyczny współczynnik zmienności Vs \t= "+DataAnalyzer.wspolczynnikZmiennosci1(listC));
		System.out.println("Klasyczny współczynnik zmienności Vd \t= "+DataAnalyzer.wspolczynnikZmiennosci2(listC));
		System.out.println("Pozycyjny współczynnik zmienności VQ \t= "+DataAnalyzer.wspolczynnikZmiennosci3(listC));
		System.out.println("Pozycyjny wsp. zmienności VQ1Q3 \t= "+DataAnalyzer.wspolczynnikZmiennosci4(listC));
		System.out.println("Współczynnik koncentracji Lorenza \t= "+DataAnalyzer.wspolczynnikKoncentracjiLorenza(listC));
		
		//TESTOWANIE HIPOTEZY
//		System.out.println("\n\r\n\r====================================================");
//		double alpha = DataAnalyzer.getAlpha();
//		boolean ogon = DataAnalyzer.getOgon();
//		System.out.println(DataAnalyzer.getOgon() ? "Rozkład jednostronny" : "Rozkład dwustronny");
//		System.out.println("Poziom istotności \t\t= "+DataAnalyzer.getAlpha());
//		System.out.println("Wartość z \t\t\t= "+DataAnalyzer.getValueZ(listA, listB));
//		System.out.println("Wartość Z alpha \t\t= "+DataAnalyzer.wartoscP(alpha, ogon));
//		System.out.println("Hipoteza zerowa odrzucona: \t"+DataAnalyzer.odrzucHipotezeZerowa(listA, listB, alpha, ogon));
//		System.out.println("Hipoteza alternatyw. odrzucona:\t"+DataAnalyzer.odrzucHipotezeAlternatywna(listA, listB, alpha, ogon));
		
	}
}

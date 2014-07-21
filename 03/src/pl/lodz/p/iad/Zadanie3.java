package pl.lodz.p.iad;


import java.util.Arrays;
import java.util.List;

import pl.lodz.p.iad.structure.Mapa;
import pl.lodz.p.iad.structure.Zbior;

public class Zadanie3 {
	
	
	public static void main(String[] args) {
	
		Zbior zbior1 = new Zbior() {
			@Override
			public List<Integer> getColumnIndices() {
				return Arrays.asList(0, 1);
			}
		};
		Mapa mapa1 = new Mapa(zbior1);
		new Kmeans(mapa1);
	}
}

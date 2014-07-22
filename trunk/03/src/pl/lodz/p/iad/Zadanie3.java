package pl.lodz.p.iad;


import java.util.Arrays;
import java.util.List;

import pl.lodz.p.iad.structure.Mapa;
import pl.lodz.p.iad.structure.Zbior;

public class Zadanie3 {
	
	
	public static void main(String[] args) {
	
		Mapa mapa1 = new Mapa(new Zbior() {
			@Override
			public List<Integer> getColumnIndices() {
				return Arrays.asList(0,1);
			}
		});
		new Kmeans(mapa1);
	}
}

package pl.lodz.p.iad;

import java.util.Arrays;
import java.util.List;

import pl.lodz.p.iad.structure.Zbior;

public class Zadanie3 {

	public static void main(String[] args) {

		new Kmeans(new Zbior() {
			public List<Integer> getColumnIndices() {
				return Arrays.asList(0, 1);
			}
		});
		
//		new Kmeans(new Zbior() {
//			public List<Integer> getColumnIndices() {
//				return Arrays.asList(2, 3, 4);
//			}
//		});
//		
//		new Kmeans(new Zbior() {
//			public List<Integer> getColumnIndices() {
//				return Arrays.asList(5, 6, 7);
//			}
//		});
	}
}

package pl.lodz.p.iad;

import java.util.Arrays;

public class Zadanie04 {

	public static void main(String[] args) {

		int[] test01 = new int[8];
		test01[0] = 0;
		test01[1] = 3;
		test01[2] = 3;
		test01[3] = 7;
		test01[4] = 5;
		test01[5] = 3;
		test01[6] = 11;
		test01[7] = 1;
		Zadanie04 z = new Zadanie04();
		System.out.println(z.solution(test01));
	}

	private int number;
	private int[] numbers;
	private int[] helper;

	public int solution(int[] A) {
		int[] posortowany = sort(A);
		int count = 0;
		int m = 0;
		int i = 0;
		while (i < posortowany.length) {
			if ((i + m) < posortowany.length) {
				if (posortowany[i] == posortowany[i + m]) {
					m++;
				} else {
					i++;
					m = 1;
				}
			} else {
				i++;
				m = 1;
			}
			count++;
			if (count>100_000_000) return -1;
		}
		return count;
	}

	public int[] sort(int[] values) {
		this.numbers = values;
		number = values.length;
		this.helper = new int[number];
		mergesort(0, number - 1);
		return numbers;
	}

	private void mergesort(int low, int high) {
		// check if low is smaller then high, if not then the array is sorted
		if (low < high) {
			// Get the index of the element which is in the middle
			int middle = low + (high - low) / 2;
			// Sort the left side of the array
			mergesort(low, middle);
			// Sort the right side of the array
			mergesort(middle + 1, high);
			// Combine them both
			merge(low, middle, high);
		}
	}

	private void merge(int low, int middle, int high) {

		// Copy both parts into the helper array
		for (int i = low; i <= high; i++) {
			helper[i] = numbers[i];
		}

		int i = low;
		int j = middle + 1;
		int k = low;
		// Copy the smallest values from either the left or the right side back
		// to the original array
		while (i <= middle && j <= high) {
			if (helper[i] <= helper[j]) {
				numbers[k] = helper[i];
				i++;
			} else {
				numbers[k] = helper[j];
				j++;
			}
			k++;
		}
		// Copy the rest of the left side of the array into the target array
		while (i <= middle) {
			numbers[k] = helper[i];
			k++;
			i++;
		}
	}
}

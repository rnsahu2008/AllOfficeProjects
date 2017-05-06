package SortingandMerging;

import java.util.Arrays;

public class QuickSort2 {
	private int input[];
	private int length;

	public void sort1(int[] numbers) {
		if (numbers == null || numbers.length == 0) {
			return;
		}
		this.input = numbers;
		length = numbers.length;
		quickSort(0, length - 1);
	}

	private void quickSort(int low, int high) {
		int i = low;
		int j = high;

		// pivot is middle index
		int pivot = input[low + (high - low) / 2];
		// Divide into two arrays
	while (i <= j) {

			while (input[i] < pivot) {
				i++;
			}
			while (input[j] > pivot) {
				j--;
			}
			if (i <= j) 
			{
				swap(i, j);
				i++;
				j--;
				System.out.println(Arrays.toString(input));
			}
		}
		if (low < j) {
			quickSort(low, j);
		}
		if (i < high) {
			quickSort(i, high);
		}
	}

	private void swap(int i, int j) {
		int temp = input[i];
		input[i] = input[j];
		input[j] = temp;
	}

	public static void main(String args[]) {
		// unsorted integer array
		int[] unsorted = {1,9,8,7,5,4,3,2,1 };
		// System.out.println("Unsorted array :" + Arrays.toString(unsorted));
		QuickSort2 algorithm = new QuickSort2();
		algorithm.sort1(unsorted);
		System.out.println("Sorted array :" + Arrays.toString(unsorted));
	}

}

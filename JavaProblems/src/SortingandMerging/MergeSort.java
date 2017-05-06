package SortingandMerging;

import java.io.File;
import java.util.Arrays;

public class MergeSort {
	
	public static void main(String[] args) {
			
	int[] x = { 9, 2, 4, 7, 7, 7, 10,19 };
	//System.out.println(Arrays.toString(x));
	 
	int low = 0;
	int high = x.length - 1;
	//System.out.println(high);

	mergeSort(x, low, high);
	System.out.println(Arrays.toString(x));
	}
	public static void mergeSort(int[] arr, int low, int high) 
	{
		
		//File *fl = 
		
		if (arr == null || arr.length == 0)
			return;

		if (low >= high)
			return;
 
		int middle = low + (high - low) / 2;
		int pivot = arr[middle];
		// make left < pivot and right > pivot
				int i = low, j = high;
				while (i <= j) {
					while (arr[i] < pivot) {
						i++;
					}
		 
					while (arr[j] > pivot) {
						j--;
					}
		 
					if (i <= j) {
						int temp = arr[i];
						arr[i] = arr[j];
						arr[j] = temp;
						i++;
						j--;
					}
				}
		 
				// recursively sort two sub parts
				if (low < j)
					mergeSort(arr, low, j);
		 
				if (high > i)
					mergeSort(arr, i, high);
			}
		}
package SortingandMerging;

import java.util.Arrays;

public class StringSort {

	public void bubbleSrot(String[] arr) {
		for (int i = 0; i < arr.length; i++) {
			for (int j = i + 1; j < arr.length; j++) {
				if (arr[i].compareTo(arr[j]) > 0) {
					String temp = arr[j];
					arr[j] = arr[i];
					arr[i] = temp;

				}

			}

		}

	}

	public static void main(String[] args) {
		String[] arr = { "Ram", "Sahu", "Niwas", "Om", "Prakash", "Sahu" };
		StringSort obj = new StringSort();
		obj.bubbleSrot(arr);
		System.out.println(Arrays.toString(arr));

	}

}

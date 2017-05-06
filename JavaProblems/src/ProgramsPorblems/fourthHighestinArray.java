package ProgramsPorblems;

import java.util.Arrays;

public class fourthHighestinArray {

	public void kthhighest(int[] arr) {

		for (int i = 0; i < arr.length; i++) {
			for (int j = i; j < arr.length; j++)
				if (arr[j] > arr[i]) {

					int temp = arr[j];
					arr[j] = arr[i];
					arr[i] = temp;

				}

		}

	}

	public static void main(String[] args) {

		int[] arr = { 12, 12, 14, 13, 13, 9, 5,1};
		int count = 0;
		fourthHighestinArray four = new fourthHighestinArray();
		four.kthhighest(arr);
		System.out.println(Arrays.toString(arr));
		int element = 6;
		if (element == 1) {
			System.out.println(arr[0]);
		} else {
			for (int i = 1; i < arr.length  && count <= element; i++)
				if (arr[i-1] >= arr[i]) {
					count = count + 1;
					// i++;
					System.out.println("count condition:" + count + "::i" + i);
				}
			// System.out.println(Arrays.toString(arr));

			System.out.println(arr[count]);
		}
	}

}

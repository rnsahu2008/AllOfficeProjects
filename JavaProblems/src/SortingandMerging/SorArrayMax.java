package SortingandMerging;

import java.util.Arrays;

public class SorArrayMax {

	public void findMax(int[] a) {

		if (a.length > 1) {
			int max = a[0];
			int min = a[1];

			for (int i = 1; i < a.length; i++) {
				if (a[i] > max)

				{
					max = a[i];

				}

			}
			System.out.println(max);
		} else {
			System.out.println(a[0]);
		}

	}

	public void sortarray(int[] a) {
		for (int i = 0; i < a.length; i++) {
			for (int j = i + 1; j < a.length; j++) {
				int temp = 0;
				if (a[i] > a[j]) {
					temp = a[i];
					a[i] = a[j];
					a[j] = temp;

				}
			}
		}

		System.out.println(Arrays.toString(a));
	}

	public void SecondHigh(int[] a) {

		if (a.length > 1) {
			int high = a[0];
			int sechigh = a[1];

			for (int i = 1; i < a.length; i++) {
				if (a[i] > high) {
					sechigh = high;
					high = a[i];
				}
			}
			System.out.println("high is"+high);
			System.out.println("sec high is"+sechigh);
		}
		else{System.out.println("No sec highest exists");}
	}

	public static void main(String[] args) {

		// int[] a={-2,-3,-6,-12,-10,-10,-12,-13,0};
		// int[] a={10,20,1,5,4,20,6,4,2,5,4,6,8,-5,-1};
		int[] a = { 11,12,12,12,11,10,10,0 };
		
		//int[] a = { 11 };
		SorArrayMax s1 = new SorArrayMax();
		s1.sortarray(a);
		s1.findMax(a);
		s1.SecondHigh(a);

	}
}

package ProgramsPorblems;

import java.util.Arrays;

public class KthelementinUsortedArray
{

public void ktheleme(int[] arr, int k)
{
	for (int i = 0; i < arr.length; i++) {
        for (int j = i + 1; j < arr.length; j++) {
            int tmp = 0;
            if (arr[i] > arr[j]) {
                tmp = arr[i];
                arr[i] = arr[j];
                arr[j] = tmp;
            }
        }
    }
	System.out.println(Arrays.toString(arr));
	
}
	
public static void main(String[] args) {
	
	int[] arr={1,6,3,4,9,14,11,13};
	KthelementinUsortedArray kl = new KthelementinUsortedArray();
	kl.ktheleme(arr, 5);
	System.out.println(arr[5]);
	
		
}

}

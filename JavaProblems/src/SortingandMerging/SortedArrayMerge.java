package SortingandMerging;

import java.lang.reflect.Array;
import java.util.Arrays;

public class SortedArrayMerge {
	
	public static void main(String[] args) {
		
		 int[]a = {9,12,15};
		  int[]b = {4, 14, 19};
		  int[] answer = new int[a.length + b.length];
		    int i = 0, j = 0, k = 0;

		    while (i < a.length && j < b.length)
		    {
		        if (a[i] < b[j])       
		            answer[k++] = a[i++];

		        else        
		            answer[k++] = b[j++];               
		    }

		    while (i < a.length)  
		    	
		        answer[k++] = a[i++];


		    while (j < b.length)    
		    	
		        answer[k++] = b[j++];

System.out.println(Arrays.toString(answer));
		}
	
}
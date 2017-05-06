package SortingandMerging;

import java.util.Arrays;

public class Merge2SortedArrays
{
	
protected Merge2SortedArrays()
{
	System.out.println();
	
	}

	public static int[] merge(int[] a, int[] b) {

	    int[] answer = new int[a.length + b.length];
	    int i = 0, j = 0, k = 0;
	    while (i < a.length && j < b.length)
	    {
	        if (a[i] < b[j])
	        {
	            answer[k] = a[i];
	            i++;
	        }
	        else
	        {
	            answer[k] = b[j];
	            j++;
	        }
	        k++;
	    }

	    while (i < a.length)
	    {
	        answer[k] = a[i];
	        i++;
	        k++;
	    }

	    while (j < b.length)
	    {
	        answer[k] = b[j];
	        j++;
	        k++;
	    }

	    return answer;
	}



		public static void main(String[] args) 
		{    int a [] = {3,5,7,9,12,14, 15};

		int b[]=  {4,9,17};
		Merge2SortedArrays mg = new Merge2SortedArrays();
		int[] h=mg.merge(a, b);
		System.out.println(Arrays.toString(h));
		
	   
}
}
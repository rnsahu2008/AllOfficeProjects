package SortingandMerging;

import java.util.Arrays;

public class QuickSortTest {
	
	int size;
	
	public void quickSort(int[] k,int low , int high)
	{
		if(k.length==0)
		{
			return   ;
		}

		//int middle=high/2;
		//int se=low+(high-low)/2;
		int pivot=k[low+(high-low)/2];
		size=k.length;
			int i=low;int j=high;
			while (i<=j)
			{
				System.out.println(i);
				while(k[i]<pivot)
				{System.out.println(i);
					i++;	}
		while(k[j]>pivot)
				{System.out.println(j);
			j--;}
			
			if(i<=j)
			{	
				int temp = k[i];
				k[i] = k[j];
				k[j] = temp;
				i++;
				j--;
				
				
			}
				
	}
			 if (low < j)
                 quickSort(k, low, j);
         if (high>i)
        	 quickSort(k, i, high);
	}


	public static void main(String[] args) {
		
		int[] arr1={1,6,2,12,0,9,5};
		System.out.println(Arrays.toString(arr1));

		int low=0;
		int high=arr1.length-1;
		QuickSortTest Qs= new QuickSortTest();
		Qs.quickSort(arr1, 0, high);
		System.out.println(Arrays.toString(arr1));
		//System.out.println(arr1[]);
		
	}

}

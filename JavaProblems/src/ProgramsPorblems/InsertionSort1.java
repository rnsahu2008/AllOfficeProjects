package ProgramsPorblems;

public class InsertionSort1 {

	public static void main(String a[])
	{
		int[] arr1 = {10,34,2,56,7,67,88,42};
		
		int[] arr2 = doInsertionSort(arr1);
		for(int k : arr2)
		{System.out.print(k);
		System.out.print(",");
		}
		
	}

	public static int[] doInsertionSort(int[] input)
	{
		int temp;
		
		for (int i=0;i<input.length;i++)
		{
			
			for(int j=i+1;j<input.length;j++)
			{
		if(input[i]>input[j])
				{
			temp = input[j];
			input[j] = input[i];
			input[i] = temp;

				}
				
			}
			
		}
		
		return input;

	}
}

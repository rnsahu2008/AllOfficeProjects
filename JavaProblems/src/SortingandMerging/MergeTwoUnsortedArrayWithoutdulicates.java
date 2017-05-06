package SortingandMerging;

import java.util.Arrays;

public class MergeTwoUnsortedArrayWithoutdulicates

{
	
	public void isocontain(int[] arr1 ,int[] arr2)
	{
		int[] arr = new int[arr1.length+arr2.length];
	
	   // int[] arr = new int[Math.min(arr1.length, arr2.length)]; 

		
		int index=0;
for(int i=0;i<arr1.length;i++)
{
	if(!containsa(arr, arr1[i]))
	{
		arr[index++]=arr1[i];
		
	}
}
for(int i=0;i<arr2.length;i++)
{
	if(!containsa(arr, arr2[i]))
	{
		arr[index++]=arr2[i];
		
	}

}
System.out.print(Arrays.toString(arr));
for (int j=0; j<arr.length;j++)
	if(arr[j]==0)
	{
	}
	else
	{
		System.out.print(arr[j]+ " ");

		}
	}
			

	
public boolean containsa(int[] arr,int i)
{
	for(int k:arr)
	{
		if(k==i)
			return true;
	}
	
    return false;

}
	
public static void main(String[] args) {

	int[] a1={9,5};
	int[] a2={4,5,0};	
	MergeTwoUnsortedArrayWithoutdulicates mer = new MergeTwoUnsortedArrayWithoutdulicates();
	mer.isocontain(a1, a2);
}
}


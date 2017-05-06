package SortingandMerging;
import java.util.Arrays;
public class bubbleSort 
{
	public void sortarray(int[] arr) {
		
		for(int i=0;i<arr.length;i++)
			
		{
			for(int j=i+1;j<arr.length;j++)
			{
				if(arr[i]>arr[j])
					
				{
					int temp=arr[i];
					arr[i]=arr[j];
					arr[j]=temp;
				}}		
			}
		System.out.println(Arrays.toString(arr));
					}
public static void main(String[] args) {
		int arr[] = { 71, 60, 35, 2, 45, 320, 5 };
		bubbleSort sort = new bubbleSort();

		sort.sortarray(arr);

		
	}
}

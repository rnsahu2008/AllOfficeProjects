package SortingandMerging;

public class QuickSort
{ 
  private int[] numbers;
   private int number;
 public void sort(int[] values)
	 {
         // check for empty or null array
         if (values ==null || values.length==0)
         {
                 return;
         }
         this.numbers = values;
         number = values.length;
         quicksort(0, number - 1);
 }
private void quicksort(int low, int high) 
	{
        int pivot = numbers[low + (high-low)/2];

		int i=low; int j=high;
	
		
			while(numbers[i]<pivot)
			{
				i++;
				
			}
            while(numbers[j] > pivot) {
                j--;
        }
            if (i <= j) {
                exchange(i, j);
                i++;
                j--;
        }
				
				if (low < j)
				        quicksort(low, j);
				if (i < high)
				        quicksort(i, high);
				}
	
private void exchange(int i, int j)
{
			int temp = numbers[i];
			numbers[i] = numbers[j];
			numbers[j] = temp;
			
}
	

public static void main(String[] args) 
{
	
	QuickSort sorter = new QuickSort();
	int[] input = {24,122,45,20,56,75,2,56,99,53,12};
	sorter.sort(input);
	for(int i:input){
	System.out.print(i);
System.out.print(" ");
	}
	}}
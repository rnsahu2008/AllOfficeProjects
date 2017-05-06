package SortingandMerging;

	public class MergeSort2 {
		private int[] array;
		private int[] tempMergArr;
		private int length;
	public static void main(String[] args) 
	{
		
		}
	
	public void sort(int inputArr[]) 
	{
	    this.array = inputArr;
	    this.length = inputArr.length;
	    this.tempMergArr = new int[length];
	    doMergeSort(0, length - 1);
	}
	private void doMergeSort(int lowerIndex, int higherIndex)
	{
	
		if (lowerIndex < higherIndex) {
	        int middle = lowerIndex + (higherIndex - lowerIndex) / 2;
	        // Below step sorts the left side of the array
	        doMergeSort(lowerIndex, middle);
	        // Below step sorts the right side of the array
	        doMergeSort(middle + 1, higherIndex);
	        // Now merge both sides
	    mergeParts(lowerIndex, middle, higherIndex);
	
		}
	}
	private void mergeParts(int lowerIndex, int middle, int higherIndex)
		{
			
		for (int i=lowerIndex;i<higherIndex;i++)
		{
			tempMergArr[i]=array[i];}
			//for(int )	
		}
	}
	
	
	
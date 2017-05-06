package ProgramsPorblems;

public class ArraysumPairs {

	public static void printPairs(int[] array, int sum) {

        for (int i = 0; i < array.length; i++) {
           // int first = array[i];
            for (int j = i + 1; j < array.length; j++) {
              //  int second = array[j];

                if ((array[i] + array[j]) == sum) {
                    System.out.printf("(%d, %d) %n", array[i], array[j]);
                }
            }

        }
    }
	
	public static void main(String[] args) {
		
		int[] arr1 ={1,2,3,4,5,6,7};
		ArraysumPairs obj= new ArraysumPairs();
		obj.printPairs(arr1, 5);
		
		
	}
}



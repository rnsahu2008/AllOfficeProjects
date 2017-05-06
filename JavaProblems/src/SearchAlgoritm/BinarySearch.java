package SearchAlgoritm;

public class BinarySearch {

	public int SearchBinary(int[] arr, int x)

	{
		int low = 0;
		int high = arr.length;
		while (low <= high) {
			int mid = (low + high) / 2;
			if (x == arr[mid]) {
				return mid;
			} else if (x < arr[mid]) {
				high = mid - 1;
			} else {
				low = mid + 1;
			}
		}

		return -1;

	}

	public static void main(String[] args) {

		int[] arr1 = { 3, 5, 8, 9, 10, 11, 56 ,79};
		BinarySearch bn = new BinarySearch();
		int index = bn.SearchBinary(arr1, 56);
		System.out.println(index);

	}

}

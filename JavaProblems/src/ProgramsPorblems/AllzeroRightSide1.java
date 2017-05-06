package ProgramsPorblems;

import java.util.Arrays;

public final class AllzeroRightSide1 extends AllzeroRightSide{

	public static final void main(String[] args) {
		int[] array = { 7, 0, 7, 0, 99, 9, 0, 70, 1 };
		// Maintaining count of non zero elements
		int count = -1;
		for (int i = 0; i < array.length; i++)
		{
			if (array[i] != 0) {
				array[++count] = array[i];
			}

			}
		while (count < array.length - 1)

		{
			array[++count] = 0;
		}


		System.out.println(Arrays.toString(array));
	}

}
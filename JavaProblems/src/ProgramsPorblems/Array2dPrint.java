package ProgramsPorblems;

public class Array2dPrint {

	public static void main(String[] args) {
		
		int[][] p={{2,3,4,5,6},{3,4,8,6,9},{3,4,56,8,9}};
		int [][] k=new int[3][];
		k[0]=new int[4];
		k[1]=new int[3];
		k[2]=new int[2];
		
		for(int[] l:k)
		{
			for (int y:l)
			{
				System.out.print(y);
				
			}
			System.out.println(" ");
			
		}
		
	}
}

package ProgramsPorblems;

public class Triangle {
	
	public void formTraingle(int n)
	
	{
		for (int i=1; i<n+1; i += 2)
		{
		    for (int k=0; k < ((n-1)/2 - i / 2); k++)
		    {
		        System.out.print(" ");
		    }
		    for (int j=0; j<i; j++)
		    {
		        System.out.print("*");
		    }
		    System.out.println("");
		}
		

		
	}
	
	public static void main(String[] args) {
		
		Triangle tr = new Triangle();
		tr.formTraingle(9);
	
			}

}

package ProgramsPorblems;
import java.util.Scanner;

public class Recu1 {

		static int b=1;
		public int fact(int a)
		{
			int result;
			if(a==1 || a==0)
			{
				return 1;
				
			}
			else
			{
				
				result=a*b;
				b=result;
				return result;
			}
			
			
		}
		

		public static void main(String[] args) 
		{
			//System.out.println("Test");
			Scanner scan = new Scanner(System.in);
			int a = scan.nextInt();
			Recu1 rc = new Recu1();
			
			for (int i=1;i<=a;i++)
				
			{
				int k =rc.fact(i);
				System.out.println(k);	
				
			}
			
			
			
				
		}

	}

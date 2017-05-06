package ProgramsPorblems;
import java.util.Scanner;

public class Recu {

		public int fact(int a)
		{
			int result;
			if(a==1 || a==0)
			{
				return 1;
				
			}
			else
			{
				
				result=a*fact(a-1);
				a=a-1;
				return result;
			}
			
		}
		
		

		public static void main(String[] args) 
		{
			System.out.println("Test");
			Scanner scan = new Scanner(System.in);
			int a = scan.nextInt();
		//	System.out.println(a);
		
			Recu rc = new Recu();
			int k =rc.fact(a);
			System.out.println(k);
			
				
		}

	}

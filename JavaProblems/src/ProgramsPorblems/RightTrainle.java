package ProgramsPorblems;

import java.util.Scanner;

public class RightTrainle {
	
    public static void rightTriangle(int rows)
    {
      //  Scanner input = new Scanner (System.in);
       // System.out.print("How many rows: ");
       // int rows = input.nextInt();
        for (int x = 1; x <= rows; x++)
        {
            for (int i = 1; i <= rows-x; i++)
            {
                System.out.print(" ");
            }
           
           for(int k=1; k<=x;k++)
            // while(x>=1)
            {
            System.out.print("#");
            
            }
            System.out.println("");
        }
    }
public static void main(String[] args) {
	
	RightTrainle ts = new RightTrainle();
	ts.rightTriangle(5);
	
}

}

package ProgramsPorblems;

import java.util.Stack;

public class NumbertoStars {
	
	public static void main(String[] args) {
		
		int number=341;
		Stack<Integer> stack = new Stack<Integer>();
		while (number > 0) {
		    stack.push( number % 10 );
		    number = number / 10;
		}

		while (!stack.isEmpty()) {
		   // System.out.println(stack.pop());
		    int k =stack.pop();
		    for(int i=0;i<k;i++)
		    {
		    System.out.print("*");	
		    	
		    }
		    System.out.println();

		  		}
	}


	/*
public static void main(String[] args) {
	int number=467;
	int k=0;
    while (number >0)
    {
    	k=number%10;
	//System.out.println(k);
    number = number / 10;
    for(int i=0;i<k;i++)
    {
        System.out.print("*");
    	
    }
    System.out.println();
 

    }
}

*/
}

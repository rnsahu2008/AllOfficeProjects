package ProgramsPorblems;

import java.util.Collections;
import java.util.Stack;

public class StacktoQueOperations {
	
	public static void main(String[] args) {
		
		Stack<Integer> sc = new Stack<Integer>();
		
		sc.push(1);
		sc.push(2);
		sc.push(3);
		sc.push(4);
		System.out.println(sc);
		System.out.println(sc.peek());
			
		Stack<Integer> sc1 = new Stack<Integer>();
		while(!sc.empty())
		{
			
			sc1.push(sc.pop());
		}
		System.out.println(sc1);
		System.out.println(sc1.peek());	
		sc1.pop();
		System.out.println(sc1);

		while(!sc1.empty())
		{
			
			sc.push(sc1.pop());
		}
		
		System.out.println(sc);
sc.push(5);
sc.push(6);
System.out.println(sc);
System.out.println(sc.peek());
	

	}
	
	

}

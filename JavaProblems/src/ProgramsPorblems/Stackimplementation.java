package ProgramsPorblems;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;



public class Stackimplementation 
{

	public static void main(String[] args) {
		
		Stack stack = new Stack();

		Stack stack1 = new Stack();
		Stack stack2 = new Stack();
		
		int[] arr1 = {2,3,4,56,0};
		
		stack.push(5);	stack.push(6);		stack.push(7);		stack.push(8);		stack.push(9);
		System.out.println(stack);		
		//stack.pop();
		System.out.println(stack.pop());
		stack.peek();
		System.out.println(stack);
		
		//Set to Stack
		Set<Integer> set = new HashSet<Integer>();
		for (int i=0;i<6;i++ )
		{set.add(i);}
		stack2.addAll(set);
		System.out.println("Set to Stack : "+stack2);
	
		// Stack to stack
		List< Integer> al = new ArrayList<Integer>();
		al.addAll(stack);
		System.out.println("List to Stack: " + al);
		
		//Array to stack
		for (int i=0;i<arr1.length;i++ )
		{stack1.add(arr1[i]);
		}
		System.out.println("Array to Stack:"+stack1);
				
	}
	
}
package ProgramsPorblems;

import java.util.Stack;
public class StackMethods 
{
	public String checkfunction(String str)
	{
		Stack<Character> stack = new Stack();
		for (int i=0; i<str.length();i++)
		{
			char ch =str.charAt(i);
			if(ch=='{' || ch=='(' || ch=='[')
			{		stack.push(ch);	}
			
			else if(ch==')' || ch=='}' || ch==']')
			{
				
				if(stack.empty())
				{	return "not balanced";
				}
				
			
			else if(stack.peek()=='(' && ch==')' || stack.peek()=='{' && ch=='}' || stack.peek()=='[' && ch==']')
			{	
				stack.pop();
			}
			else {return "not balanced";}
				
				}
		}
			
	
	if (stack.isEmpty())
		return "Balanced Parenthisis";
	else
		return "Not Balanced";

			
}
	
	
	public static void main(String[] args) {
		
		StackMethods stk = new StackMethods();
	System.out.println(stk.checkfunction("[{(1*2)(3*4)}]+[(5+6)(7*8)]"));
		
	}
}
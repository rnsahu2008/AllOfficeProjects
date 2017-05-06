package ProgramsPorblems;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Duplicate_digits {
	
	//Print number between to num which dont have duplicate digits
	
	public boolean  duplicatedigitInNumberRange(int  num)
	
	{

		Map<Character,Integer> map = new HashMap<Character, Integer>();
		char[] char1= String.valueOf(num).toCharArray();
	//	int count=1;
		boolean f=true;
	for(char c:char1)
	{
		if(f=true)
		{
		if(map.containsKey(c))
		{
			f=false;

			
		}
		
		else
		{
			map.put(c,1);
			
		}
		}
		
	}
	return f;
	
	}
	
	public static void main(String[] args) 
	{
		Duplicate_digits d = new Duplicate_digits();

		Scanner scn = new Scanner(System.in);
		int num1 = scn.nextInt();
		Scanner scn1 = new Scanner(System.in);
		int num2 = scn1.nextInt();
		
		while (num1<num2)
		{
			for (int i = num1; i<=num2; i++)
			{
		
		if(d.duplicatedigitInNumberRange(i)==true)
			
		{
			System.out.println(i);
			
		}
		num1++;
		
			}

		
		}
	}
	

}

package ProgramsPorblems;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

public class KeyExist 
{
	
	public static void main(String[] args) {
		
		HashMap<Integer, String> has = new HashMap<Integer, String>();
		has.put(1, "RAM");
		has.put(2, "RAM1");
		has.put(3, "RAM2");
		has.put(4, "RAM3");
		has.put(5, "RAM4");
		
		
		System.out.println(has);
		
		int i;
		Scanner scn = new Scanner(System.in);
		i=scn.nextInt();
		
		if(has.containsKey(i))
			
		{
			System.out.println("key exist");
		}
		else
		{
			
			System.out.println("Not");
			
		}
		

		
		
	}

}

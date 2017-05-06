package ProgramsPorblems;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class AnagramStrings {

	public  boolean checkAnagram(String first, String second)
	{
		char[] characters1 = first.toCharArray();
		char[] characters2 = first.toCharArray();
		boolean flag=false;
		Map<Character, Integer> firstmap = new HashMap<Character, Integer>();
		Map<Character, Integer> secondtmap = new HashMap<Character, Integer>();
		
		if(first.length()!=second.length())
		{	return false;}
		
		for (int i = 0; i < first.length(); i++)

		{
			if (firstmap.containsKey(first.charAt(i))) {

				/*Integer count = firstmap.get(first.charAt(i));
				count = count + 1;
				firstmap.put(first.charAt(i), count);
*/
				firstmap.put(first.charAt(i), firstmap.get(first.charAt(i))+1);
				
			} else {
				firstmap.put(first.charAt(i), 1);

			}
			
		//***************************************	

			if (secondtmap.containsKey(second.charAt(i))) {

			/*	Integer count = secondtmap.get(second.charAt(i));
				count = count + 1;
				secondtmap.put(second.charAt(i), count);*/

				secondtmap.put(second.charAt(i), secondtmap.get(second.charAt(i))+1);
				
			} else {
				secondtmap.put(second.charAt(i), 1);

			}

			//***************************************

		}
		
	Set<Character> hs = firstmap.keySet();	
	for (char Char:hs )
	{
		if(firstmap.get(Char).equals(secondtmap.get(Char)))
				{
				
			flag=true;
			
			
				}
		
	}
	 
	return flag;
	
	}

	public static void main(String[] args)

	{

		AnagramStrings an = new AnagramStrings();
		
		if (an.checkAnagram("TestRams", "RamsTest")==true)
		{
			System.out.println("is anagram");
		}
		else
		{
			System.out.println("not anagram");
		}
		
		
		

	}

}

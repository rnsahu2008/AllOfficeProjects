package ProgramsPorblems;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import javax.print.DocFlavor.INPUT_STREAM;

public class DuplicateChars {

	public void printDuplicateArry(String Words)

	{

		char[] characters = Words.toCharArray();
		Map<Character, Integer> charmap = new HashMap<Character, Integer>();
		for (Character ch : characters)

		{
			if (charmap.containsKey(ch)) {

				Integer count = charmap.get(ch);
				count = count + 1;
				charmap.put(ch, count);

			} else {
				charmap.put(ch, 1);

			}

			// System.out.println(charmap);

		}

		System.out.println(charmap);
		/*
		 * for(int i=0; i<Words.length();i++)
		 * 
		 * {
		 * 
		 * if(charmap.containsKey(Words.charAt(i)))
		 * 
		 * { if(charmap.get(Words.charAt(i))>1) {
		 * System.out.println(Words.charAt(i)); //flag=true; // break;
		 * 
		 * 
		 * }
		 * 
		 * 
		 * }}
		 */

		for(char c:charmap.keySet())
		{
			if(charmap.get(c)>1)
				System.out.println(c+":"+charmap.get(c));
			
		}

	}

	public static void main(String[] args)

	{
		Scanner scn = new Scanner(System.in);
		String str = scn.nextLine();
		DuplicateChars dp = new DuplicateChars();
		dp.printDuplicateArry(str);

	}

}

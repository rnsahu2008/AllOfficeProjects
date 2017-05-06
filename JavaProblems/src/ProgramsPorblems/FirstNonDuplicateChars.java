package ProgramsPorblems;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class FirstNonDuplicateChars {

	public static char getFirstNonRepeatedChar(String str) 
	{
		Map<Character, Integer> counts = new LinkedHashMap<>(str.length());

		for (char c : str.toCharArray()) 
		{
			counts.put(c, counts.containsKey(c) ? counts.get(c) + 1 : 1);
		}
		
		for (Entry<Character, Integer> entry : counts.entrySet()) 
		{
			if (entry.getValue() == 1) 
			{
				return entry.getKey();
			}
		}
		throw new RuntimeException("didn't find any non repeated Character");
	}
public static void main(String[] args) {
	
	FirstNonDuplicateChars str = new FirstNonDuplicateChars();
	char c =str.getFirstNonRepeatedChar("0AbdffdggdibA");
	System.out.println(c);
	
}
}

package ProgramsPorblems;

import java.util.HashMap;
import java.util.Map;

public class DuplicateCharsinString
{
	
	public void duplicateCharsinString(String str)
	
	{
		char[] cararr=str.toCharArray();
		int counter=1;
		
		Map<Character, Integer> map = new HashMap<Character, Integer>();
		
		for(char ch:cararr)
		{
			if(map.containsKey(ch))
			{
				map.put(ch, counter+1);
				
			}
			
			else
			{
				map.put(ch, counter);
				
			}
		}

		for(Character k:map.keySet())
			
			if(map.get(k)>1)
		System.out.println(k);
		
		
	}
	
	
public void duplicateCharsinInteger(int num)
	
	{
		char[] cararr=String.valueOf(num).toCharArray();
		int counter=1;
		
		Map<Character, Integer> map = new HashMap<Character, Integer>();
		
		for(char ch:cararr)
		{
			if(map.containsKey(ch))
			{
				map.put(ch, counter+1);
				
			}
			
			else
			{
				map.put(ch, counter);
				
			}
		}

		for(Character k:map.keySet())
			
			if(map.get(k)>1)
		System.out.println(k);
		
		
	}
	
	public static void main(String[] args) {
		
		String str="zzKkmnn";
		int num=123344;
		DuplicateCharsinString dp= new DuplicateCharsinString();
		dp.duplicateCharsinString(str);
		dp.duplicateCharsinInteger(num);


}
}
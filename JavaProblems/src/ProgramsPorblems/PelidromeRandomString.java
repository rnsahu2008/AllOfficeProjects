package ProgramsPorblems;
import java.util.HashMap;
import java.util.Map;
public class PelidromeRandomString {
	
	public boolean isPalindrome(String str)
	{
		Map<Character, Integer> map = new HashMap<Character, Integer>();
		boolean odd;
		for(char Char : str.toCharArray())
		{
			if(map.containsKey(Char))
			{map.put(Char, map.get(Char)+1);}
			
			else
			{
				map.put(Char, 1);
			}
		}
		System.out.println(map.values());
		int count=0; 
		for(int v : map.values())
			
		{
			if(v%2==1)
			{
				count+=1;
							
			}	
		}
		
		if(count>1)
		{
			return false;
		}
		else
		{
			return true;
			
		}
		}
	
	
	
	public static void main(String[] args) {
		PelidromeRandomString pl = new PelidromeRandomString();
		System.out.println(pl.isPalindrome("aaabbccc"));
	}

}

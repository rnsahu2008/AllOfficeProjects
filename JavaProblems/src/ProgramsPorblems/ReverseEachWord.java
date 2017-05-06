package ProgramsPorblems;

import java.util.Arrays;

public class ReverseEachWord {
	
	public void reverWord(String str)
	
	{
		char[] chr = str.toCharArray();
		String[] word = str.split(" ");
		
		String rev="";
		for (int i=chr.length; i<1;i--)
		{
	    rev = rev + str.charAt(i);
			
		}
		
		System.out.println(rev);	
		
	}
	
	
public void reverString1(String str)
	
	{
		String[] words = str.split(" ");
		System.out.println(Arrays.toString(words));
		System.out.println(words.length);
        String reverseString = "";

		for (int i=0; i<words.length;i++)
		{
            String word = words[i];
            String reverseWord="";
            

            
			int len1=words[i].length();
			System.out.println("Word lenght:"+words[i].length());
			
			for (int j=len1-1; j>=0;j--)
			{
				reverseWord = reverseWord + word.charAt(j);
			}	
			reverseString = reverseString +  reverseWord + "  ";
	
		}
		System.out.println(reverseString);	
		
		
		
	}
	
	public static void main(String[] args) 
	{
		ReverseEachWord rs = new ReverseEachWord();
		//rs.ReveseStringMe1("Test ram");
		rs.reverString1("Ram Niwas sahu");
		
	}

}

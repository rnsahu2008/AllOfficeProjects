package ProgramsPorblems;

import java.util.HashMap;
import java.util.Map;

public class NonRpeatedChars 
{

	Map<Character, Integer> firtmap = new HashMap<Character, Integer>();
	public void FirstNonRpeatedChars(String Word)
	
	{
		char[] charecter = Word.toCharArray();
	
		for (int i=0;i<Word.length();i++)
		{
			if(firtmap.containsKey(Word.charAt(i)))
				
			{
				//int count=firtmap.get(Word.charAt(i));
				//count=count+1;
				//firtmap.put(Word.charAt(i), count);
				firtmap.put(Word.charAt(i), firtmap.get(Word.charAt(i))+1);
							
			}
			else
			{
				firtmap.put(Word.charAt(i), 1);

				//System.out.println(firtmap);
				
			}
			
		}
		
	//	System.out.println(firtmap);
		
boolean flag =false;
for(int i=0; i<Word.length();i++)
	
{
	
if(firtmap.containsKey(Word.charAt(i)))
	
{
	if(firtmap.get(Word.charAt(i))==1)
	{
		System.out.println(Word.charAt(i));
		flag=true;
		break;
		
			
	}
	
	
	

}

	
	
	
	

	
}
		if(flag==false)
		{
			System.out.println("Word have only duplicates");
		}
		
	}
	public static void main(String[] args) 
	{
		
		NonRpeatedChars non = new NonRpeatedChars();
		non.FirstNonRpeatedChars("TTese");
		
	}
	
}

package ProgramsPorblems;

public class ReveseString {
	
	String rev="";
	
	public void ReveseStringMe(String Word)
	
	{
		char[] ch =Word.toCharArray();
			
		for(int i=ch.length-1;i>=0;i--)
		{
			 rev=rev+ch[i];
			
		}
		System.out.println(rev);
	}
	
public void ReveseStringMe1(String Word)
	
	{
		String S= "";
		for (int i=Word.length()-1;i>=0;i--)
			
		{ 
			 S= S + Word.charAt(i);
			
		}
		
		
		System.out.println(S);
		
		
	}
	
	
	public static void main(String[] args) 
	{
		ReveseString rev= new ReveseString();
		rev.ReveseStringMe("RAMNIWSahu");
		rev.ReveseStringMe1("RAMNIW Sahu");
	}

}

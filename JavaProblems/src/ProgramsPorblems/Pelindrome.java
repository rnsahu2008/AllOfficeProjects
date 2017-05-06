package ProgramsPorblems;

public class Pelindrome {
	static String str;
	
	public String Checkpelindrome(String str)
	
	{
		String rev="";
	 //System.out.println(str.length());
	// char[] st = str.toCharArray();
	for(int i=str.length()-1; i>=0;i--)
	{
		rev=rev+str.charAt(i);
		
	}

	return rev;
	
	
		
	}
	public static void main(String[] args) {
		
		Pelindrome pl = new Pelindrome();
		String str1="test";
		String strs= pl.Checkpelindrome(str1);
		if(strs.equals(str1))
		{
			
		System.out.println("pelindrome");	
		}
		
		else
		{
			System.out.println("not peli");
		}
	}

}

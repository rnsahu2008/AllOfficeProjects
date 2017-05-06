package ProgramsPorblems;

public class ReplaceAll 
{
	
	public static String replaceSpace(String s, String replacement) {
	    String ret = s.replaceAll("  *", replacement);
	    return ret;
	}

	
	
	public static void main(String[] args)
	{
	//ReplaceAll rpc = new ReplaceAll();
	//rpc.replaceSpace("", "Test Ram");
	
		
		String str= "Tes Ram Test1";
		
		int count=0;
		char[] ch = str.toCharArray();
		int l1= ch.length;
		int l2=0;
		for(char ck:ch)
		{
			if((int)ck==32) 
			{
				count=count+1;
			}
		}
		char[] chnew = new char[l1+count*2];
		

		for(char ck:ch) 
		{
			
			if((int)ck==32) 
			{
				
				chnew[l2]='%';
				chnew[l2+1]='2';
				chnew[l2+2]='0';
				l2=l2+3;
			}
			else
			{
				chnew[l2]=ck;
				l2=l2+1;
			}
			
			
			
		}
		System.out.println(chnew);
		
	}

}

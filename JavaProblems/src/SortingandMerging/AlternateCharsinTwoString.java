package SortingandMerging;

import java.io.File;
import java.nio.CharBuffer;

public class AlternateCharsinTwoString {
	
	public String join(String str1, String str2){
	  
		 String result= null;

		    char first= str1.charAt(0);
		    char second= str2.charAt(0);

		    result= Character.toString(first )+ Character.toString(second);
		    if(str1.length()>1 && str2.length()>1)
		        result= result+ join(str1.substring(1),str2.substring(1));
		    else if(str1.length()==1)
		        return result+str2.substring(1);
		    else if (str2.length()==1)
		        return result+str1.substring(1);    

		    return result;
	}

	public static void main(String[] args) throws StringIndexOutOfBoundsException {
		
		
		String s1="abc";
		String s2="df";
		
		AlternateCharsinTwoString v = new AlternateCharsinTwoString();
		System.out.println(v.join(s1, s2));
		}

}

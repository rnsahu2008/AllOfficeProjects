package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DivisibiltyBy7 {

	@SuppressWarnings("null")
	public static void main(String[] args) throws IOException {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line = br.readLine();
        int [] array = new int [line.length()];
      
       for(int i=0;i<line.length();i++)
       {
    	   String s = String.valueOf(line.charAt(i));
    	   array[i] = Integer.parseInt(String.valueOf(s));
    	  
       }
       DivisibiltyBy7 div = new DivisibiltyBy7();
       div.divisibleset(array);
        

	}

	public void divisibleset(int[] no) throws IOException
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line = br.readLine();
        int Q = Integer.parseInt(line);
        for(int i =0;i<Q;i++)
        {
        	StringBuffer str = new StringBuffer();
        	br = new BufferedReader(new InputStreamReader(System.in));
        	 line = br.readLine();
            int a = Integer.parseInt(line);
            line = br.readLine();
            int b = Integer.parseInt(line);
            while(a<b)
            {
            	 str = new StringBuffer();
            	str=str.append(Integer.toString(no[a]));
            	a=a+1;
            }
            checkDivision(Integer.parseInt(str.toString()));
        }
	}
	
	public void checkDivision(int n)
	{
		if(n%7==0)
		System.out.println("Yes");
		else
			System.out.println("No");
	}
}

package ProgramsPorblems;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileRead {
	
	
	
	public void wrtefile() throws IOException
	{
		File fl = new File("D://Test.txt");
		FileOutputStream fos = new FileOutputStream(fl);
		BufferedOutputStream bos= new BufferedOutputStream(fos);
		
	    String s="Mai Sachin Tendulkar is my favourite player";  
	     byte b[]=s.getBytes();//converting string into byte array  
	     bos.write(b);
	     bos.close();  
	     


	}
	
	
	
	public void Readfile() throws IOException
	{

		FileInputStream fstream = new FileInputStream(new File("D://Test.txt"));
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		String strLine;

		while ((strLine = br.readLine()) != null)   {
		  System.out.println (strLine);
		}

		br.close();
	
	}
	
	public void Readfile1() throws IOException
	{
		FileReader fr = new FileReader(new File("D://Test.txt"));
		BufferedReader bar = new BufferedReader(fr);
		String strLine1;
		int count=0;
		while ((strLine1 = bar.readLine()) != null)   {
			System.out.println(strLine1);

			String[] words =strLine1.split(" ") ;
			//char[] st =strLine1.toCharArray();
			for(String k :words)
			{
				if(k.equals("my"))
				{					count++;}}
		}
		System.out.println(count);
		bar.close();	
		}

	
	
	
	
	
	public static void main(String[] args) throws IOException  {
	
FileRead fr = new FileRead();
fr.wrtefile();
	
fr.Readfile1();
	
	}}

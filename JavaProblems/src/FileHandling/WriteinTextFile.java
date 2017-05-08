package FileHandling;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class WriteinTextFile {
	public static void main(String[] args) throws IOException {
		
	FileWriter fw = new FileWriter("D:\\AAAAAAA\\File1.txt");
	BufferedWriter bw = new BufferedWriter(fw);
	String content="i am going to write something";
	bw.write(content);
	bw.close();
	fw.close();
	
	

	FileReader fr = new FileReader("D:\\AAAAAAA\\File1.txt");
BufferedReader br = new BufferedReader(fr);
String line;
while((line=br.readLine())!=null)
{
System.out.println(line);	
}
		
	}
	

}

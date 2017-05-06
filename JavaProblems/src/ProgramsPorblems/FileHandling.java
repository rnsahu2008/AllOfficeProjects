package ProgramsPorblems;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import com.gargoylesoftware.htmlunit.javascript.host.file.FileReader;

public class FileHandling {

	public void wrietinfile() throws IOException {
		// FileOutputStream fout=new FileOutputStream("D:/AAA.txt");
		FileOutputStream fout = new FileOutputStream(new File("D:/AAA.txt"));

		BufferedOutputStream bout = new BufferedOutputStream(fout);

		String s = "ram";
		byte[] b = s.getBytes();
		bout.write(b);
		bout.flush();
		// bout.close();
		// fout.close();

	}

	public void Readfile() throws IOException {
		// FileOutputStream fout=new FileOutputStream("D:/AAA.txt");
		FileInputStream fin = new FileInputStream(new File("D:/AAA.txt"));

		int ch;
		StringBuffer strContent = new StringBuffer("");

		while ((ch = fin.read()) != -1) {
			// System.out.print(fin.read());
			strContent.append((char) ch);
		}

		System.out.println(strContent);
	}

	public void ReadfilebyFileReader() throws IOException {
		// FileInputStream fin=new FileInputStream(new File("D:/AAA.txt"));
		FileInputStream fin = new FileInputStream("D:/AAA.txt");
		BufferedReader reader = new BufferedReader(new InputStreamReader(fin));
		String line = reader.readLine();
		while (line != null) {
			System.out.println(line);
			line = reader.readLine();

		}
	}

	public static void main(String[] args) throws IOException {

		FileHandling obj = new FileHandling();
		obj.wrietinfile();
		obj.Readfile();
		obj.ReadfilebyFileReader();

	}
}

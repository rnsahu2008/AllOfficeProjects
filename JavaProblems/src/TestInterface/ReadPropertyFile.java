package TestInterface;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Properties;

public class ReadPropertyFile 
{
		  public static void main(String[] args) throws IOException {

			  File file = new File("config.properties");
				FileInputStream fileInput = new FileInputStream(file);
				Properties prop = new Properties();
				prop.load(fileInput);
				
				Enumeration enuKeys = prop.keys();
				while (enuKeys.hasMoreElements()) {
					String key = (String) enuKeys.nextElement();
					String value = prop.getProperty(key);
				//	System.out.println(key + ": " + value);
				}
			OutputStream output = null;
				output = new FileOutputStream(file);
				// set the properties value
				prop.setProperty("database", "localhost");
			//	prop.setProperty("dbuser", "mkyong");
				//prop.setProperty("dbpassword", "password");

				// save properties to project root folder
				prop.store(output, null);
				fileInput.close();
				output.close();
				
				
				 File file1 = new File("config.properties");
					FileInputStream fileInput1 = new FileInputStream(file1);
				
				Properties prop1 = new Properties();
				prop1.load(fileInput1);
				
				Enumeration enuKeys1 = prop1.keys();
				while (enuKeys1.hasMoreElements()) 
				{
					String key1 = (String) enuKeys1.nextElement();
					String value1 = prop1.getProperty(key1);
					System.out.println(key1 + ": " + value1);
			  
				
					  }
		}
}
package src;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;

public class ReadExcel extends Base{

	static HashMap<String,ArrayList<String>> map = new HashMap<>();
	
public ReadExcel() throws IOException
{
	testDataFile("E:\\Projects\\test\\Data\\Locator.xls");
	
	ArrayList<String> list = dataProviderByRow(0, 1, 1, 0);
	for(int i=0;i< list.size();i++)
	{
		map.put(list.get(i), data(i+1));
	}
	
	
}


public ArrayList<String> data(int j)
{
	s = w.getSheetAt(0);
	ArrayList<String> value = new ArrayList<String>();
	
	for(int i=1;i<=4;i++)
	{
		try
		{
			HSSFRow row=s.getRow(j);
			String str = row.getCell(i).getStringCellValue();	
			str = str.trim();
			
			value.add(i+"="+str);
		}
		catch(NullPointerException e)
		{
			
		}
		
	}	
	return value;
}

	public By locator(String orKey)
	{
		
		ArrayList<String> value = map.get(orKey);
//		Object[] objectArray = value.toArray();
//		String[] strArr = Arrays.copyOf(objectArray, objectArray.length, String[].class);
		String[] str = value.get(0).split("=");
		if(str[0].equals("1"))
		{
			return By.id(str[1]);
		}
		else if(str[0].equals("2"))
		{
			return By.xpath(str[1]);
		}
		else if(str[0].equals("3"))
		{
			return By.linkText(str[1]);
		}
		else if(str[0].equals("4"))
		{
			return By.cssSelector(str[1]);
		}
		else if(str[0].equals("5"))
		{
			return By.className(str[1]);
		}
		else if(str[0].equals("6"))
		{
			return By.name(str[1]);
		}
		else
		{
			throw new NoSuchElementException("No Such Element Exist: "+orKey);
		}
	
		
		
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		ReadExcel r = new ReadExcel();
//		HashMap<String,ArrayList<String>> map = new HashMap<>();
//		map=r.readdata();
//		System.out.println(map);
//		String[] str = map.get("k2").toString().split("=");
//		
//		System.out.println(map.get("k1"));
//		System.out.println(map.get("k2"));
//		System.out.println(map.get("k3"));
		
		System.out.println(r.locator("k2").toString());
	}

}

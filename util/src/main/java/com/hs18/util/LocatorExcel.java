package com.hs18.util;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;

public class LocatorExcel extends Action {

	ArrayList<String> value;


	public By locator(String orKey)
	{
		String app = data.get().get("appName");
		if(!(app.equals("Integration")))
		{
			if(app.equals("CRM"))
			{
				 value = CRM.get(orKey);
			}
			else if(app.equals("CMS"))
			{
				value = CMS.get(orKey);
			}
			else if(app.equals("OMS"))
			{
				value = OMS.get(orKey);
			}
			else if(app.equals("android"))
			{
				value = android.get(orKey);
			}
			else if(app.equals("DFC"))
			{
				value = DFC.get(orKey);
			}
			return returnElement(value, orKey);
		}
		else
		{
			if(!(CRM.get(orKey)==null))
			{
				 value = CRM.get(orKey);
			}
			else if(!(CMS.get(orKey)==null))
			{
				 value = CMS.get(orKey);
			}
			else if(!(OMS.get(orKey)==null))
			{
				 value = OMS.get(orKey);
			}
			else if(!(android.get(orKey)==null))
			{
				 value = android.get(orKey);
			}
			else if(!(DFC.get(orKey)==null))
			{
				 value = DFC.get(orKey);
			}
			return returnElement(value, orKey);
		}
		
		
		
	}
	

	
	public By returnElement(ArrayList<String> value , String orKey)
	{
		String[] str = value.get(0).split("%");
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
		else if(str[0].equals("7"))
		{
			return By.tagName(str[1]);
		}
	else
	{
		throw new NoSuchElementException("No Such Element Exist: "+orKey);
	}	
	}
	
}

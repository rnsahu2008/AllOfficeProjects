package Test;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class Locator {
	
	 By searchbox = By.name("q");
	 By searchboxbutton = By.xpath("//*[@id='container']/div/header/div[1]/div[2]/div/div/div[2]/form/div/div[2]/button/svg");

	By mobile = By.xpath("//*[@id='container']/div/div[2]/div[2]/div/div[2]/div/div[2]/div/section/ul/li[3]");
	By mobilecategory = By.xpath("//*[@id='container']/div/div[2]/div[2]/div/div[1]/div/div/div[1]/section/div[2]/div/div[2]/a[1]");

	By mobilepricerange = By.xpath("//*[@id='container']/div/div[2]/div[2]/div/div[1]/div/div/div[2]/section/div[4]/div[1]/select");
	
	 By pricedrag = By.xpath("//*[@id='container']/div/div[2]/div[2]/div/div[1]/div/div/div[2]/section/div[3]/div[1]/div[1]/div");
	 						 
	 By pricedrop = By.xpath("//*[@id='container']/div/div[2]/div[2]/div/div[1]/div/div/div[2]/section/div[3]/div[2]/div[9]");
	 						  
	 //By brandapple = By.xpath("//*[@id='container']/div/div[2]/div[2]/div/div[1]/div/div/div[4]/section/div[2]/div[1]/div[2]/div/div/label/div[1]");
	 By brandapple = By.xpath("//*[@id='container']/div/div[2]/div[2]/div/div[1]/div/div/div[5]/section/div[2]/div[1]/div[1]/div/div/label/div[1]");
	 
	// By Availbility = By.xpath("//*[@id='container']/div/div[2]/div[2]/div/div[1]/div/div/div[7]/section/div/div");
	 By Availbility = By.xpath(" //*[@id='container']/div/div[2]/div[2]/div/div[1]/div/div/div[18]/section/div[1]/div");
//	 By excludeoutofstcok = By.xpath("//*[@id='container']/div/div[2]/div[2]/div/div[1]/div/div/div[7]/section/div[2]/div/div/div/div/label/div[1]");
	
	 By excludeoutofstcok = By.xpath("//*[@id='container']/div/div[2]/div[2]/div/div[1]/div/div/div[18]/section/div[2]/div/div/div/div/label/div[1]");
		
	 By sellonflipkar = By.xpath("//*[@id='container']/div/header/div[1]/div[1]/div/ul/li[1]/a");
	 By noofProducts = By.xpath("//*[@id='container']/div/div[2]/div[2]/div/div[2]/div/div[1]/div/div[2]/h1/span/span[6]");
	 
	

}

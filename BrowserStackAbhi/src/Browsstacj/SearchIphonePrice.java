package Browsstacj;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class SearchIphonePrice {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
SearchIphonePrice result = new SearchIphonePrice();
result.sortIphone();
	}
	
	
	public void sortIphone()
	{
		int i=0;
		Flipkartiphone phone = new Flipkartiphone();
		LinkedHashMap<Integer, Flipkartiphone> map = new LinkedHashMap<Integer, Flipkartiphone>();
	
		System.setProperty("webdriver.chrome.driver","libs\\chromedriver_29.exe");
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		System.out.println("Chrome driver selected");
		driver.get("https://www.flipkart.com/");	
		
		driver.get("https://www.flipkart.com/mobiles-accessories/mobiles/pr?otracker=categorytree&p%5B%5D=facets.brand%255B%255D%3DApple&p%5B%5D=facets.price_range.from%3D30000&p%5B%5D=facets.price_range.to%3DMax&p%5B%5D=facets.availability%255B%255D%3DExclude%2BOut%2Bof%2BStock&q=iphone6&sid=tyy%2F4io");
		//WebElement element = driver.findElement(By.xpath("//*[@id=\"container\"]/div/div[2]/div[2]/div/div[2]/div/div[3]/div/div"));
		String path = "//*[@id=\"container\"]/div/div[2]/div[2]/div/div[2]/div/div[3]/div/div";
		while(driver.findElements(By.xpath(path+"/div["+i+"]")).size()>0 )
		{
			String url = driver.findElement(By.xpath(path+"/div["+i+"]/a")).getAttribute("href");
			String name = driver.findElement(By.xpath(path+"/div["+i+"]/a/div[2]/div[1]/div")).getText();
			Integer price = Integer.parseInt(driver.findElement(By.xpath(path+"/div["+i+"]/a/div[2]/div[2]/div/div/div")).getText());
			phone.setPrice(price);
			phone.setProductLink(url);
			phone.setProductName(name);
			map.put(i, phone);
		}
		Set<Entry<Integer, Flipkartiphone>> set = map.entrySet();
        List<Entry<Integer, Flipkartiphone>> list = new ArrayList<Entry<Integer, Flipkartiphone>>(set);
        Collections.sort( list, new Comparator<Map.Entry<Integer, Flipkartiphone>>()
        {
            public int compare( Map.Entry<Integer, Flipkartiphone> o1, Map.Entry<Integer, Flipkartiphone> o2 )
            {
                return (o2.getValue().price).compareTo( o1.getValue().price );
            }
        } );
        for(Map.Entry<Integer, Flipkartiphone> entry:list){
            System.out.println(entry.getValue().price+" | "+entry.getValue().productName+" | "+entry.getValue().productLink);
        }
	}

}

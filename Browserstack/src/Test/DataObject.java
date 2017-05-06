package Test;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;

public class DataObject extends ActionClass 
{
	 String productName;
	 Integer productPrice;
	 String productLink;
	 String abc;
	 String abc1;
	 String abc2;

   public DataObject(Integer productPrice,String productName,String productLink)
    {
    			
    			this.productName = productName;
            	this.productPrice = productPrice;
            	this.productLink = productLink;
     }


	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(int productPrice) {
		 this.productPrice = productPrice;
	}

	public String getProductLink() {
		return productLink;
	}

	public void setProductLink(String productLink) {
		this.productLink = productLink;
	}

	@Override
	public String toString() {
		
		return productPrice +" | "+ productName+" | "+productLink+"\n";
		
	}
	
	
}


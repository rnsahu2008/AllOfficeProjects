package com.sel.casepanelTests;

import junit.framework.Assert;

import org.openqa.selenium.WebDriver;

import com.hs18.lib.SelBase;
import com.hs18.lib.UIMapParser;
import com.hs18.lib.WaitEvent;
import com.hs18.lib.utils;

public class Components extends SelBase {
	public Components(WebDriver d) {
		try {

			// utils.setConfiguration("src/test/resources/config.properties");
			driver = d;
			we = new WaitEvent(driver);
			ui = new UIMapParser(utils.getConfig("ObjectRepository"));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Failed to instantiate Components object");
		}
	}
	
	UIMapParser ui;

}

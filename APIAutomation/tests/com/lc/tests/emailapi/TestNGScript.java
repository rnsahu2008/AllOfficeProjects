package com.lc.tests.emailapi;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import atu.testng.reports.ATUReports;
import atu.testng.reports.utils.Utils;

import atu.testng.reports.listeners.ATUReportsListener;
import atu.testng.reports.listeners.ConfigurationListener;
import atu.testng.reports.listeners.MethodListener;

@Listeners({ ATUReportsListener.class, ConfigurationListener.class,
  MethodListener.class })
public class TestNGScript {


       //Set Property for ATU Reporter Configuration
       {
         //System.setProperty("atu.reporter.config", "Path to the properties file");

       }


       WebDriver driver;

       @BeforeClass
       public void init() {
       
              setIndexPageDescription();
       }

       private void setAuthorInfoForReports() {
              ATUReports.setAuthorInfo("Automation Tester", Utils.getCurrentTime(),
                           "1.0");
       }

       private void setIndexPageDescription() {
              ATUReports.indexPageDescription = "My Project Description <br/> <b>Can include Full set of HTML Tags</b>";
       }

            //Deprecated Methods
       @Test
       public void testME() {
              setAuthorInfoForReports();
//              ATUReports.add("Step Desc", false);
//              ATUReports.add("Step Desc", "inputValue", false);
//              ATUReports.add("Step Desc", "expectedValue", "actualValue", false);
//              ATUReports.add("Step Desc", "inputValue", "expectedValue",
//                           "actualValue", false);
       }


}
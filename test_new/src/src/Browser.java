package src;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeTest;

public class Browser 
{
	  WebDriver driver=null;
	 static int i=1;
	public static HashMap<String, WebDriver> config = new HashMap<String, WebDriver>();	
	public static String str , File , screenshotPath ,imagepath , importfilepath;
	public  String chromedriver,iedriver,chromebinary;
	Properties prop;
	
	/* setDriver() reads the browser name from  test data file and activate the browser mentioned
	 *  on test data sheet . File , chromedriver.exe , iedriver.exe location are mentioned 
	 *  on config.properties file. 
	 */
	@BeforeTest
	public void setDriver() throws IOException
	{
		
		prop=new Properties();
		prop.setProperty("ws.home", "${basedir}") ;
		FileInputStream ip=new FileInputStream(System.getProperty("user.dir")+"\\Data\\config.properties");
		prop.load(ip);
		File = prop.getProperty("File");
		chromedriver = prop.getProperty("chromedriver");
		iedriver = prop.getProperty("iedriver");
		chromebinary = prop.getProperty("chromebinary");
		screenshotPath = prop.getProperty("screenshotPath");
		//imagepath = prop.getProperty("imagepath");
		//importfilepath = prop.getProperty("importfilepath");
		Base action = new Base();
      	action.testDataFile(File);
		
      	str = action.testData(0, 4, 0);
		 if (str.toLowerCase().equals("firefox"))
			{
			 	driver = new FirefoxDriver();
			 	driver.manage().window().maximize();
			 	config.put(str, driver);
			 	System.out.println("firefox driver selected");
			 	driver.get(action.testData(0, 1, 0));
			 	
			}
		if (str.toLowerCase().equals("chrome"))
			{
				System.setProperty("webdriver.chrome.driver",chromedriver );
				String userProfile= "C:\\Users\\abhishek.bisht\\AppData\\Local\\Google\\Chrome\\User Data\\Default\\";
				ChromeOptions options = new ChromeOptions();
				options.addArguments("user-data-dir="+userProfile);
				options.addArguments("--start-maximized");
				driver = new ChromeDriver();
				driver.manage().window().maximize();
				config.put(str, driver);
				System.out.println("Chrome driver selected");
				driver.get(action.testData(0, 1, 0));
				
		     
			}
		if(str.toLowerCase().equals("iexplorer"))
		{
			 System.setProperty("webdriver.ie.driver",iedriver );
			 DesiredCapabilities caps = DesiredCapabilities.internetExplorer();
			 //caps.setCapability("ignoreZoomSetting", true);			
			 //Delete Browser Cache since IE does not open a clean profile unlike Chrome & FireFox
//			 try {
//			 Runtime.getRuntime().exec("RunDll32.exe InetCpl.cpl,ClearMyTracksByProcess 255");
//			 } catch (IOException e) {
//			 e.printStackTrace();
//			 }
			 driver = new InternetExplorerDriver(caps);
			 driver.manage().deleteAllCookies();
			 driver.manage().window().maximize();
			 config.put(str, driver);
			 driver.get(action.testData(0, 1, 0));
			
			
		}
		 if (str.toLowerCase().equals("safari"))
			{
			 	driver = new SafariDriver();
			 	
			 	System.out.println("safari driver selected");
			 	driver.manage().window().maximize();
			 	driver.get(action.testData(0, 1, 0));
			 	config.put("safari", driver);
			 	System.out.println("safari");
			}
		 
		 if(str.toLowerCase().equals("grid"))
		 {
			 DesiredCapabilities cap = new DesiredCapabilities();
			 cap.setBrowserName("firefox");
			// cap.setVersion("9.0.1");
			 cap.setPlatform(org.openqa.selenium.Platform.WINDOWS);
			 driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"),cap);
		 }

//		ATUReports.setWebDriver(driver);
		 action.closeFile();
		
	}
	
	
	
	
	
	public static WebDriver getDriverInstance() 
	{
		//return driver;
		return config.get(str);
	}
	
	
	
	
	@AfterSuite
	
	public void driverClose() throws IOException
	{
		Base action = new Base();
		action.testDataFile(File);
		if(!(action.testData(0, 4, 0).toLowerCase().equals("multibrowser")))

		{
			driver.quit();
		}
		
		action.closeFile();
	}
//	@AfterSuite
//	public void sendMail()
//	{
//
//		Properties props = new Properties();
//		props.put("mail.smtp.auth", "true");
//	    props.put("mail.smtp.starttls.enable", "true");
//	    props.put("mail.smtp.host", "smtp.gmail.com");
//	    props.put("mail.smtp.port", "587");
//		  Session session = Session.getInstance(props,
//			         new javax.mail.Authenticator() {
//			            protected PasswordAuthentication getPasswordAuthentication() {
//			               return new PasswordAuthentication("abhishekhomeshop@gmail.com", "testing123@");
//			            }
//			         });
//    try {
//       
//    	// Define message
//        MimeMessage message =
//                new MimeMessage(session);
//        message.setFrom(
//                new InternetAddress("abhishekhomeshop@gmail.com"));
//        message.addRecipient(
//                Message.RecipientType.TO,
//                new InternetAddress("abhishekhomeshop@gmail.com"));
//        message.setSubject(
//                "Test Report");
//
//        // create the message part 
//        MimeBodyPart messageBodyPart =
//                new MimeBodyPart();
//
//        //fill message
//        messageBodyPart.setText("Please Check Attachment for test report");
//
//        Multipart multipart = new MimeMultipart();
//        multipart.addBodyPart(messageBodyPart);
//        String fileAttachment = "E:/Demo_Framework/test-output/emailable-report.html";
//        // Part two is attachment
//        messageBodyPart = new MimeBodyPart();
//        DataSource source =
//                new FileDataSource(fileAttachment);
//        messageBodyPart.setDataHandler(
//                new DataHandler(source));
//        messageBodyPart.setFileName(fileAttachment);
//        multipart.addBodyPart(messageBodyPart);
//        // Put parts in message
//        message.setContent(multipart);
//        // Send the message
//        Transport.send(message);        
//        System.out.println("message sent....");  
//        }catch (MessagingException ex)
//        {
//        	ex.printStackTrace();}  
//      }  
    }



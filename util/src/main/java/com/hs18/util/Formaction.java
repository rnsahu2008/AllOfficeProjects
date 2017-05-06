package com.hs18.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlInclude;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import com.hs18.DataUtils.DBQuery;
import com.hs18.DataUtils.DbConnection;
import com.hs18.DataUtils.ExcelReader;

/**
 * Servlet implementation class Formaction
 */
public class Formaction extends HttpServlet {
	ExcelReader reader = new ExcelReader();
	Browser browser = new Browser();
	Properties prop;
	public String mailto, soapDriver, home, mailto2;
	private static final long serialVersionUID = 1L;
	String fileOutput;
	public  String propfile;

	DBQuery connection;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Formaction() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			doProcess(request, response);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			doProcess(request, response);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	protected void doProcess(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException {
		Browser br = new Browser();
		String Browser = "Browser";
		String Application = "Application";
		String Environment = "Environment";
		String Group = "Group";
		String filePath = "filePath";
		connection = new DBQuery();
		prop = new Properties();
		propfile=DbConnection.propfile ;
		FileInputStream ip = new FileInputStream(propfile + "\\Data\\config.properties");
		prop.load(ip);
		mailto = request.getParameter("userid");
		mailto = mailto + "@homeshop18.com";
		if (mailto.equals( "Enter User's Homeshop18 Id@homeshop18.com")) {
			mailto = prop.getProperty("ReportToEmail");
		}
		mailto = mailto.trim().toLowerCase();
		// mailto2 = prop.getProperty("ReportToEmail2");
		soapDriver = prop.getProperty("SoapDriver");
		home = prop.getProperty("home");
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
		Date date = new Date();
		fileOutput = "Extent_" + dateFormat.format(date) + ".html";
		ExtentReporterNG listener = new ExtentReporterNG(fileOutput);
		RetryListener listner2 = new RetryListener();
		Screenshot listner3 = new Screenshot();
		HashMap<String, String> userData = new HashMap<>();

		HttpSession session = request.getSession();
		if (session != null && session.getAttribute("filePath") != null) {
			userData.put(Application, request.getParameter("ApplicationsList"));
			userData.put(Environment, request.getParameter("environmentList"));
			userData.put(Group, request.getParameter("groupList"));
			userData.put(Browser, request.getParameter("browserList"));
			userData.put(filePath, session.getAttribute("filePath").toString());
			userData.put("fileOutput", fileOutput);
			userData.put("mailto", mailto);
			userData.put("HomeDir", propfile);
		}

		if ("Android".equalsIgnoreCase(userData.get(Application))) {
			List<XmlSuite> suites = new ArrayList<XmlSuite>();
			suites.add(testngXmlCreation(userData));
			TestNG runner = new TestNG();
			runner.addListener(listener);
			runner.addListener(listner2);
			runner.setXmlSuites(suites);
			runner.run();
		}

		else if ("Soap".equalsIgnoreCase(userData.get(Application))) {
			CommandLine command = new CommandLine("cmd");
			command.addArgument("/c");
			command.addArgument(soapDriver);
			DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
			DefaultExecutor executor = new DefaultExecutor();
			executor.setExitValue(1);
			executor.execute(command, resultHandler);
			System.out.println("Started Soap Project");
		} else {
			List<XmlSuite> suites = new ArrayList<XmlSuite>();
			suites.add(testngXmlCreation(userData));
			TestNG runner = new TestNG();
			runner.addListener(listener);
			runner.addListener(listner2);
			runner.addListener(listner3);
			runner.setXmlSuites(suites);
			runner.run();

		}

		try {

			if (!("soap".equalsIgnoreCase(userData.get(Application)))) {
				response.getOutputStream()
						.print("<div style='color:red'><h1> <A onclick='javascript:window.open(\"/QAAutomation/"
								+ br.data.get().get("fileOutput") + "\")'>Test Execution Report Link</A></h1></div>");
			} else {

				Thread.sleep(70000);
				response.getOutputStream().print(
						"<div style='color:red'><h1> <A onclick=\"javascript:window.open('/QAAutomation/junit-noframes.html')\">Test Execution Report Link</A></h1></div>");
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (!("soap".equalsIgnoreCase(br.data.get().get(Application)))) {
			sendmail();
		}
	}

	@SuppressWarnings("deprecation")
	public XmlSuite testngXmlCreation(HashMap<String, String> userdata)
			throws ClassNotFoundException, IOException, SQLException {
		String Application = userdata.get("Application");
		String Group = userdata.get("Group");
		HashMap<String, ArrayList<String>> mapping = new HashMap<String, ArrayList<String>>();
		XmlSuite suite = new XmlSuite();
		suite.setName(Group);
		suite.setPreserveOrder("true");
		suite.setParameters(userdata);		
		XmlTest test = new XmlTest(suite);
		test.setName(Application);
		
		test.addParameter("filePath", userdata.get("filePath"));
		List<XmlClass> allclasses = new ArrayList<XmlClass>();
		mapping = connection.classes(Application, Group);
		List<String> addedClasses = new ArrayList<>(mapping.keySet());
		for (String eachClass : addedClasses) {
			XmlClass testClass = new XmlClass();
			testClass.setName(eachClass);
			List<XmlInclude> methodsToRun = new ArrayList<XmlInclude>();
			List<String> allMethods = mapping.get(eachClass);
			Iterator<String> iMethods = allMethods.iterator();
			while (iMethods.hasNext()) {
				XmlInclude xmlInclude = new XmlInclude(iMethods.next());
				methodsToRun.add(xmlInclude);
			}
			testClass.setIncludedMethods(methodsToRun);
			allclasses.add(testClass);
		}
		test.setPreserveOrder("true");
		test.setXmlClasses(allclasses);		
		System.out.println(suite.toXml());
		return suite;
	}

	public void sendmail() {
		// Recipient's email ID needs to be mentioned.
		Browser br = new Browser();
		String to = br.data.get().get("mailto");
		// Sender's email ID needs to be mentioned
		String from = "righttestabhi@gmail.com";
		final String username = "righttestabhi@gmail.com";
		final String password = "testing123@";
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		// Get the Session object.
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
				return new javax.mail.PasswordAuthentication(username, password);
			}
		});

		try {
			// Create a default MimeMessage object.
			Message message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			// message.setRecipients(Message.RecipientType.TO,
			// InternetAddress.parse(to2));

			// Set Subject: header field
			message.setSubject("Test Execution Report");

			// Create the message part
			BodyPart messageBodyPart = new MimeBodyPart();

			// Now set the actual message
			messageBodyPart.setText("Please find attachment for test execution report.");
			Multipart multipart = new MimeMultipart();
			// Set text message part
			multipart.addBodyPart(messageBodyPart);
			messageBodyPart = new MimeBodyPart();
			String filename = home + "\\src\\main\\webapp\\" + br.data.get().get("fileOutput");
			DataSource source = new FileDataSource(filename);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(filename);
			multipart.addBodyPart(messageBodyPart);
			// Send the complete message parts
			message.setContent(multipart);
			// Send message
			Transport.send(message);
			System.out.println("Sent message successfully....");
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

}

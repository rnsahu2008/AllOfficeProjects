package com.lc.utils;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.lc.common.Common;
import com.lc.constants.Constants_Catapult;
import com.lc.constants.Constants_Ecom;
import com.lc.constants.Constants_JobAlerts;
import com.lc.constants.Constants_JobSearch;
import com.lc.constants.Constants_JobsStore;
import com.lc.constants.Constants_TrackingService;
import com.lc.constants.Constants_UserService;

public class EmailUtil{
	static String env = "";
	static String product = "";
	static String application = "";

	public static void main(String[] args) {
		String[] mailRecipients = null;
		env = args[1].equalsIgnoreCase("REG") ? "Regression" : (args[1].equalsIgnoreCase("TEST")) ? "Test" : (args[1].equalsIgnoreCase("SB")) ? "Sandbox" : (args[1].equalsIgnoreCase("STG")) ? "AutoQA-06" : "DEV";

		if (args[0].equals("jobstore")) {
			mailRecipients = Constants_JobsStore.mailRecipients;
			product = "JobStore";
		} else if (args[0].equals("jobalerts-V1")) {
			mailRecipients = Constants_JobAlerts.mailRecipients;
			product = "JobAlerts-V1";
		} else if (args[0].equals("jobalerts-V2")) {
			mailRecipients = Constants_JobAlerts.mailRecipients;
			product = "JobAlerts-V2";
		} else if (args[0].equals("jobsearch")) {
			mailRecipients = Constants_JobSearch.mailRecipients;
			product = "JobSearch";
		} else if (args[0].equals("userservice")) {
			mailRecipients = Constants_UserService.mailRecipients;
			product = "UserService";
		} else if (args[0].equals("ecom")) {
			mailRecipients = Constants_Ecom.mailRecipients;
			product = "ECom";
		} else if (args[0].equals("trackingservice")) {
			mailRecipients = Constants_TrackingService.mailRecipients;
			product = "TrackingService";
		} else if (args[0].equals("catapult")) {
			mailRecipients = Constants_Catapult.mailRecipients;
			product = "Catapult";
		}

		EmailUtil email = new EmailUtil();
		email.sendEmail(mailRecipients, product);
	}

	public void sendEmail(String[] mailRecipients, String product) {
		try {
			HashMap<String, Integer> list = extractTestResult();
			createPieChart(list);
			Properties props = new Properties();
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.socketFactory.port", "465");
			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.socketFactory.fallback", "true");
			props.put("mail.smtp.port", "465");

			// Get the default Session object.
			final String username = "automationtesterlc";
			final String password = "automationtesterlc12";
			Session session = Session.getInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username,	password);
				}
			});

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("qa-automation@bold.com"));
			message.addHeader("Content-type", "application/zip; charset=UTF-8");
			message.addHeader("format", "flowed");
			message.addHeader("Content-Transfer-Encoding", "8bit");
			message.setSubject("API Test Report - " + product + " (" + env + " env)");
			message.setSentDate(new Date());

			BodyPart messageBodyPart = new MimeBodyPart();
			String htmlText = "";
			if (list == null || list.size() == 0) {
				htmlText = mailBodyForFailure();
				Multipart multipart = new MimeMultipart();
				messageBodyPart.setContent(htmlText, "text/html");
				multipart.addBodyPart(messageBodyPart);
				message.setContent(multipart);
			} else {
				String filePath = System.getProperty("user.dir") + File.separator + "testng-output/testng-results.xml";
				File fXmlFile = new File(filePath);
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(fXmlFile);
				doc.getDocumentElement().normalize();

				// get node with name suite and extract total execution time
				NodeList suiteNodeList = doc.getElementsByTagName("suite");
				Node suite = suiteNodeList.item(0);
				Element suiteElement = (Element) suite;
				Double totalExecutionTime = Double.parseDouble(suiteElement.getAttribute("duration-ms")) / 1000;

				htmlText = mailBodyForSuccess(list, totalExecutionTime);

				StringBuilder strBuilder = new StringBuilder(htmlText);

				StringBuilder failedBuilder = new StringBuilder("<br><br><u>Please go through failure reasons as mentioned below and check request/response details in attached log file:</u><br><br><table border=\"1\" style=\"width:90%\"><th bgcolor=\"#A9A9A9\">S.No</th><th bgcolor=\"#A9A9A9\">Class Name</th><th bgcolor=\"#A9A9A9\">Test Case Name</th><th bgcolor=\"#A9A9A9\">Failure Reason</th><th bgcolor=\"#A9A9A9\">Duration (milliseconds)</th>");

				// create separate StringBuilder for skipped test cases as these will be displayed in separate table
				StringBuilder skippedbuilder = new StringBuilder("<br><br><u>Please find below detailed report for skipped tests :</u><br><br><table border=\"1\" style=\"width:90%\"><th bgcolor=\"#A9A9A9\">S.No</th><th bgcolor=\"#A9A9A9\">Class Name</th><th bgcolor=\"#A9A9A9\">Test Case Name</th>");

				// get all nodes with tag name "test-method". If status of any test method is failed or skipped then append test details in respective string builders
				NodeList nodeList = doc.getElementsByTagName("test-method");
				int failedCount = 0;
				int skippedCount = 0;
				String[] strArr = null;
				for (int i = 0; i < nodeList.getLength(); i++) {
					Node node = nodeList.item(i);
					Element element = (Element) node;
					if (element.getAttribute("status").equalsIgnoreCase("FAIL")) {
						String[] contentArr = element.getTextContent().split(
								"java.lang.AssertionError:");
						String[] parameter = contentArr[0]
								.split("The following asserts failed:");
						strArr = ((Element) node.getParentNode()).getAttribute(
								"name").split("\\.");
						if (parameter[0].trim().equalsIgnoreCase("")) // if test method is not executed with any data provider values
							failedBuilder.append("<tr><td align=\"center\">"+ ++failedCount+ "</td><td align=\"center\">"+ strArr[4]+ "</td><td align=\"center\">"+ element.getAttribute("name")+ "</td><td align=\"center\"><font color=\"red\">"+ parameter[1]+ "</font></td><td align=\"center\">"+ element.getAttribute("duration-ms")+ "</td></tr>");
						else // if test method is executed with data provider values
							failedBuilder.append("<tr><td align=\"center\">"+ ++failedCount+ "</td><td align=\"center\">"+ strArr[4]+ "</td><td align=\"center\">"+ element.getAttribute("name")+ "</td><td align=\"center\"> Executed With Data Provider, Data pump in value :: "+ parameter[0]+ "<br><br><font color=\"red\">"+ parameter[1]+ "</font></td><td align=\"center\">"+ element.getAttribute("duration-ms")+ "</td></tr>");
					} else if (element.getAttribute("status").equalsIgnoreCase("SKIP")) {
						strArr = ((Element) node.getParentNode()).getAttribute("name").split("\\.");
						skippedbuilder.append("<tr><td align=\"center\">"+ ++skippedCount + "</td><td align=\"center\">"+ strArr[4] + "</td><td align=\"center\">"+ element.getAttribute("name") + "</td></tr>");
					}
				}

				// if failed count is zero then do not add failed Test Table in Test Report
				if (failedCount > 0)
					strBuilder.append(failedBuilder.toString() + "</table>");

				// if skipped count is zero then do not add Skipped Test Table in Test Report
				if (skippedCount > 0)
					strBuilder.append(skippedbuilder.toString() + "</table>");

				strBuilder.append("<br><br> Regards, <br>Automation Team");
				messageBodyPart.setContent(strBuilder.toString(), "text/html");

				// Add Pie Chart to report
				Multipart multipart = new MimeMultipart("related");
				messageBodyPart.setContent(strBuilder.toString(), "text/html");
				multipart.addBodyPart(messageBodyPart);
				messageBodyPart = new MimeBodyPart();
				DataSource source_pie_chart = new FileDataSource(new File(System.getProperty("user.dir") + File.separator + "testng-output" + File.separator + "PieChartSummary.jpg"));
				messageBodyPart.setDataHandler(new DataHandler(source_pie_chart));
				messageBodyPart.setDisposition(MimeBodyPart.INLINE);
				messageBodyPart.setHeader("Content-ID", "<the-img-1>");
				multipart.addBodyPart(messageBodyPart);

				//If failed test count is greater than zero then only send the log file in email attachment 
				if(failedCount > 0)
				{
					messageBodyPart = new MimeBodyPart();
					DataSource source = new FileDataSource(Common.logFileName);
					messageBodyPart.setDataHandler(new DataHandler(source));
					messageBodyPart.setFileName("log.html");
					multipart.addBodyPart(messageBodyPart);
				}
				message.setContent(multipart);
			}
			InternetAddress[] internetAddresses = new InternetAddress[mailRecipients.length];
			for (int i = 0; i < internetAddresses.length; i++) {
				internetAddresses[i] = new InternetAddress(mailRecipients[i]);
			}
			message.setRecipients(Message.RecipientType.TO, internetAddresses);
			Transport.send(message);
			System.out.println("Sent message successfully....");
		} catch (Exception mex) {
			mex.printStackTrace();
		}
	}

	public HashMap<String, Integer> extractTestResult() {
		try {
			HashMap<String, Integer> result = new HashMap<String, Integer>();
			String filePath = System.getProperty("user.dir") + File.separator + "testng-output/testng-results.xml";
			File fXmlFile = new File(filePath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			result.put("Total",	Integer.parseInt(doc.getDocumentElement().getAttribute("total")));
			result.put("Passed", Integer.parseInt(doc.getDocumentElement().getAttribute("passed")));
			result.put("Failed", Integer.parseInt(doc.getDocumentElement().getAttribute("failed")));
			result.put("Skipped", Integer.parseInt(doc.getDocumentElement().getAttribute("skipped")));
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static class PieRenderer {
		private java.awt.Color[] color;

		public PieRenderer(java.awt.Color[] color) {
			this.color = color;
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		public void setColor(PiePlot plot, DefaultPieDataset dataSet) {

			java.util.List<Comparable> keys = dataSet.getKeys();
			int aInt;
			for (int i = 0; i < keys.size(); i++) {
				aInt = i % this.color.length;
				plot.setSectionPaint(keys.get(i), this.color[aInt]);
			}
		}
	}

	public void createZipReportFile() {
		try {
			String zipFile = "TestReport.zip";
			String sourceDirectory = System.getProperty("user.dir")	+ File.separator + "testng-output";
			byte[] buffer = new byte[1024];
			FileOutputStream fout = new FileOutputStream(zipFile);
			ZipOutputStream zout = new ZipOutputStream(fout);
			File dir = new File(sourceDirectory);
			if (!dir.isDirectory()) {
				System.out.println(sourceDirectory + " is not a directory");
			} else {
				File[] files = dir.listFiles();

				for (int i = 0; i < files.length; i++) {
					if (!files[i].isDirectory()) {
						System.out.println("Adding " + files[i].getName());

						// create object of FileInputStream for source file
						FileInputStream fin = new FileInputStream(files[i]);
						zout.putNextEntry(new ZipEntry(files[i].getName()));
						int length;
						while ((length = fin.read(buffer)) > 0) {
							zout.write(buffer, 0, length);
						}
						zout.closeEntry();
						// close the InputStream
						fin.close();
					}
				}
			}
			// close the ZipOutputStream
			zout.close();
			System.out.println("Zip file has been created!");
		} catch (IOException ioe) {
			System.out.println("IOException :" + ioe);
		}
	}

	public void createPieChart(HashMap<String, Integer> list) {
		DefaultPieDataset pieDataset = new DefaultPieDataset();
		DecimalFormat dFormat = new DecimalFormat("#.00");
		double percentPass = Double.parseDouble(dFormat.format(((list.get("Passed")*100.0)/list.get("Total"))));
	 	double percentFail = Double.parseDouble(dFormat.format(((list.get("Failed")*100.0)/list.get("Total"))));
	 	double percentSkip = Double.parseDouble(dFormat.format(((list.get("Skipped")*100.0)/list.get("Total"))));
		pieDataset.setValue("PASSED: (" + percentPass + "%)", percentPass);
		pieDataset.setValue("FAILED: (" + percentFail + "%)", percentFail);
		pieDataset.setValue("SKIPPED: (" + percentSkip + "%)", percentSkip);
		JFreeChart piechart = ChartFactory.createPieChart("Execution Status :" + product + " : Environment :" + env, pieDataset, true, false,false);
		PiePlot plot = (PiePlot) piechart.getPlot();
		java.awt.Color[] colors = { new Color(162, 203, 144),new Color(233, 118, 110), new Color(255, 160, 122) };
		PieRenderer renderer = new PieRenderer(colors);
		plot.setBackgroundPaint(Color.GRAY);
		renderer.setColor(plot, pieDataset);
		try {
			ChartUtilities.saveChartAsJPEG(new File(System.getProperty("user.dir") + File.separator	+ "testng-output" + File.separator + "PieChartSummary.jpg"), piechart, 700, 400);
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public String mailBodyForSuccess(HashMap<String, Integer> list,
			Double duration) {
		try {
			String htmlText = "Hi All, <br><br>Execution of API tests for "
					+ product
					+ " web service is complete: "
					+ "<br><br><u>Below is the Summary :</u>"
					+ "<br><br><div><table border=\"1\" style=\"width:90%\"><th bgcolor=\"#A9A9A9\" style=\"width:21.6%\">Total Tests Executed</th><th bgcolor=\"#A9A9A9\">Passed</th><th bgcolor=\"#A9A9A9\">Failed</th><th bgcolor=\"#A9A9A9\">Skipped</th><th bgcolor=\"#A9A9A9\" style=\"width:21.6%\">Duration (seconds)</th><tr><td align=\"center\">"
					+ list.get("Total")
					+ "</td><td align=\"center\" bgcolor=\"#A2CB90\">"
					+ list.get("Passed")
					+ "</td><td align=\"center\" bgcolor=\"#E9766E\">"
					+ list.get("Failed")
					+ "</td><td align=\"center\" bgcolor=\"#FFA07A\">"
					+ list.get("Skipped")
					+ "</td><td align=\"center\">"
					+ duration
					+ "</td></tr></table>"
					+ "<table style=width:90%><tr><th><center><img src=\"cid:the-img-1\" align=\"middle\"/></center></th></tr></table></div>";
			return htmlText;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public String mailBodyForFailure() {
		try {
			String htmlText = "Hi All, <br><br>Execution of API tests for "
					+ product
					+ " web service is complete: "
					+ "<br><br>Ooops....something went wrong during execution. As a result no test report is generated."
					+ "<br><br>Please try again or let us know by replying back on this mail"
					+ "<br><br> Regards, <br>Automation Team";
			return htmlText;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}



}

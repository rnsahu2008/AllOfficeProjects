package com.hs18.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Servlet implementation class Formaction
 */
public class DownloadTestData extends HttpServlet {
	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DownloadTestData() {
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

	protected void doProcess(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException {
		//String filepath = "E:\\Projects\\HS18Automation\\UIAutomation\\QAAutomation";
		String filepath=System.getProperty("project.home");
		String appname = request.getParameter("appName");
		 filepath = filepath+"\\Data\\" + appname + "TestData.xls";

		File file = new File(filepath);
		FileReader fileReader = null;
		FileInputStream fis = null;
		BufferedReader bf = null;
		ServletOutputStream out = null;
		//Workbook workbook = new XSSFWorkbook();

		try {
			fis = new FileInputStream(file);

			ServletContext context = getServletConfig().getServletContext();
			String mimetype = context.getMimeType(filepath);
			System.out.println("mimetype:::" + mimetype);
			if (mimetype == null) {
				mimetype = "application/octet-stream";
			}
			response.setContentType(mimetype);

			response.setContentLength((int) file.length());
			response.setHeader("Content-Disposition",
					String.format("attachment; filename=\"%s\"", new Object[] { file.getName() }));

			out = response.getOutputStream();
			//workbook.write(out);
			byte[] bufferData = new byte[1024];
			int read = 0;
 			while ((read = fis.read(bufferData)) != -1) {
				out.write(bufferData, 0, read);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			fis.close();
			out.flush();
			out.close();
		}
	}

}

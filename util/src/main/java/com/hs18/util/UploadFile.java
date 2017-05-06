package com.hs18.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.hs18.DataUtils.DbConnection;

/**
 * Servlet implementation class Formaction
 */
@SuppressWarnings("serial")
public class UploadFile extends HttpServlet {
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	// String filePath;
	public UploadFile() {
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
	//	long serialVersionUID = 1L;
		Properties prop = new Properties();
		FileInputStream ip = new FileInputStream(DbConnection.propfile + "\\Data\\config.properties");
		prop.load(ip);
		final String UPLOAD_DIRECTORY = DbConnection.propfile+prop.getProperty("uploadDirectory")+"\\";

		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		HttpSession session = request.getSession(true);
		String filePath = "";
		
		// process only if its multipart content
		if (isMultipart) {
			// Create a factory for disk-based file items
			FileItemFactory factory = new DiskFileItemFactory();
//			((DiskFileItemFactory) factory).setRepository(new File(System.getProperty("java.io.tmpdir")));
//			 String uploadFolder = getServletContext().getRealPath("")
//		                + File.separator + DATA_DIRECTORY;

			// Create a new file upload handler
			ServletFileUpload upload = new ServletFileUpload(factory);
			try {
				// Parse the request
				List<FileItem> multiparts = upload.parseRequest(request);

				for (FileItem item : multiparts) {
					if (!item.isFormField()) {
						DateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
						Date date = new Date();
						String name = new File(item.getName()).getName();
						String [] split = name.split("\\.");
						name = split[0]+dateFormat.format(date)+"."+split[1];
						
						//item.write(new File(UPLOAD_DIRECTORY + File.separator + name));
						 filePath = UPLOAD_DIRECTORY  + name;
	                    File uploadedFile = new File(filePath);
	                    System.out.println(filePath);
	                    // saves the file to upload directory
	                    item.write(uploadedFile);
					}
				}
				
				session.setAttribute("filePath", filePath);

				// File uploaded successfully
				request.setAttribute("message", "Your file has been uploaded!");
			} catch (Exception e) {
				request.setAttribute("message", "File Upload Failed due to " + e);
			}
		} else {
			request.setAttribute("message", "This Servlet only handles file upload request");
		}
		//request.getRequestDispatcher("/index.html").forward(request, response);
	}
}

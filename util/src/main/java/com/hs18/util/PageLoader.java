package com.hs18.util;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.hs18.DataUtils.DBQuery;

/**
 * Servlet implementation class Formaction
 */
public class PageLoader extends HttpServlet {

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PageLoader() {
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
		DBQuery dbquery = new DBQuery();
		ArrayList<String> browserlist = new ArrayList<>();
		browserlist.add("Chrome");
		browserlist.add("Firefox");
		browserlist.add("IExplorer");
		ArrayList<AppGroupMapping> appGroupMapping = dbquery.getGroupNames();
		for(AppGroupMapping app:appGroupMapping)
		{
			if(!(app.getAppName().equalsIgnoreCase("Android")||app.getAppName().equalsIgnoreCase("Soap")))
			{
				app.setBrowser(browserlist);
			}
		}
		Gson gson = new Gson();
		String appplicationDetails = gson.toJson(appGroupMapping);
		 //String appplicationDetails="[{\"appname\":\"CRM\",\"appid\":\"1\",\"browser\":[\"chrome\",\"firefox\"],\"group\":[\"Smoke\",\"Sanity\"]},{\"appname\":\"cms\",\"appid\":\"2\",\"browser\":[\"chrome\",\"firefox\"],\"group\":[\"Smoke\",\"Sanity\"]}]";
		ServletOutputStream out = response.getOutputStream();
		response.setContentType("application/json");
		out.write(appplicationDetails.getBytes());
	}

}

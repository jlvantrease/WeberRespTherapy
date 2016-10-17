package edu.weber.resptherapy.charting;

import java.io.IOException;
import java.sql.Connection;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.weber.resptherapy.charting.model.Formtemplate;

/**
 * Servlet implementation class ServletTherapy
 */
@WebServlet("/ServletTherapy")
public class ServletTherapy extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    //___________________________________________________________________________________________________________________

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletTherapy() {
        super();
        // TODO Auto-generated constructor stub
    }

    //___________________________________________________________________________________________________________________

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

    //___________________________________________________________________________________________________________________

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String type = request.getParameter("type");
		
		HttpSession session = request.getSession();
		
		if (type.equals("createTemplate")) {
			System.out.println("Creating template");
			
			String templateName = request.getParameter("templateName");
			String templateHTML = request.getParameter("templateHTML");
			String templateType = request.getParameter("templateType");
			
			System.out.println(templateName);
			createTemplate(templateName, templateHTML, templateType);
			
			session.setAttribute("allTemplates", getAllTemplates());
		}
		else if (type.equals("getAllTemplates")) {
						
			session.setAttribute("allTemplates", getAllTemplates()); 
			response.sendRedirect("dashboard.jsp");
		}
		else if (type.equals("getTemplateToFillOut")) {
			
			String templateId = request.getParameter("templateId");
			response.sendRedirect("dashboard.jsp");
			
			session.setAttribute("getTemplateToFillOut", getTemplateToFillOut(templateId)); 
		}
		else if (type.equals("updateTemplate")) {
			
			int templateID = Integer.valueOf(request.getParameter("templateID"));
			String templateName = request.getParameter("templateName");
			
			updateTemplate(templateID, templateName);
			
			session.setAttribute("allTemplates", getAllTemplates());
			response.sendRedirect("dashboard.jsp");
		}
		
		
	}

    //___________________________________________________________________________________________________________________
	
	//boolean createTemplate()
	public boolean createTemplate(String templateName, String templateHTML, String templateType){
		
		DatabaseConnector connector = new DatabaseConnector();
		
		try {
			
			Connection conn = connector.connectDatabase();
			
			DatabaseCalls calls = new DatabaseCalls();
			
			return calls.createTemplate(conn, templateName, templateHTML, templateType);
		
		}catch(Exception e){
			e.printStackTrace();
			
			return false; // if we get here, creating a new template didn't work
		}
		
	}
	
    //___________________________________________________________________________________________________________________

	//boolean updateTemplate()
	public boolean updateTemplate(int templateID, String templateName){
		
		
		DatabaseConnector connector = new DatabaseConnector();
		
		try {
		
			Connection conn = connector.connectDatabase();
			
			DatabaseCalls calls = new DatabaseCalls();
			
			return calls.updateTemplate(conn, templateID, templateName);
			
		}catch(Exception e){
			e.printStackTrace();
			
			return false;
		}
		
		
		
	}
	
    //___________________________________________________________________________________________________________________

	//Map<Integer, TherapyTemplate> getAllTemplates()
	public Map<Integer, Formtemplate> getAllTemplates(){
		
//		DatabaseConnector connector = new DatabaseConnector();
		
		try {
		
//			Connection conn = connector.connectDatabase();
			
			DatabaseCalls calls = new DatabaseCalls();
			
			return calls.getAllTemplates();
			
		}catch(Exception e){
			e.printStackTrace();
			
			return null;
		}
	}
	
    //___________________________________________________________________________________________________________________

	//boolean updateTemplate()
	public Formtemplate getTemplateToFillOut(String templateID){
		
		
//		DatabaseConnector connector = new DatabaseConnector();
		
		try {
		
//			Connection conn = connector.connectDatabase();
			
			DatabaseCalls calls = new DatabaseCalls();
			
			return calls.getTemplateToFillOut( templateID);
			
		}catch(Exception e){
			e.printStackTrace();
			
			return null;
		}
		
		
		
	}
	
}

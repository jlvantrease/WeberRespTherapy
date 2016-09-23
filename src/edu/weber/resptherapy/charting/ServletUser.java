package edu.weber.resptherapy.charting;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mysql.jdbc.log.Log;

/**
 * Servlet implementation class ServletUser
 */
@WebServlet("/ServletUser")
public class ServletUser extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	   
	//___________________________________________________________________________________________________________________
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletUser() {
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
		
		if (type.equals("login")) {
			
			// session retrieved, continue with servlet operations

			//go to login function passing in user parameters (username, password)
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			
			User theUser = login(username, password);
			
			session.setAttribute("user", theUser);
			
			//if login is successful
			if(session.getAttribute("user") != null){
								
				System.out.println("Login Successful!");
				
				session.setAttribute("failed", "no");
				
				
				if (theUser.isAdmin()) {
					
					//TODO call getAllUsers()
					session.setAttribute("allUsers", getAllUsers());
				}
				else{
					
					session.setAttribute("allForms", new ServletForms().getAllForms(theUser.getUserId()));
				}
				
				session.setAttribute("allTemplates", new ServletTherapy().getAllTemplates());
				
				
				//pass all information forward to the dashboard.jsp
				response.sendRedirect("http://localhost/WeberRespiratoryTherapy/jsp/dashboard.jsp");
				return;
			}
			//if login is UNsuccessful
			else{

				System.out.println("Login Failed!");
				
				//send a "failed" message to the login page so the web code people know how to handle it
				session.setAttribute("failed", "failed");
			}
			
		
		}else if(type.equals("logout")){
			
			//invalidate the session, which logs the user out

			session.invalidate();
			
			response.sendRedirect("login.jsp");
						
		}else if(type.equals("getAllUsers")){
			
			session.setAttribute("allUsers", getAllUsers());
			
			System.out.println(session.getAttribute("allUsers").toString());
			
			response.sendRedirect("dashboard.jsp");
		
		}else if(type.equals("getUserToEdit")){
			
			session.setAttribute("userToBeEdited", userToBeEdited(request.getParameter("userId")));
			response.sendRedirect("http://localhost/WeberRespiratoryTherapy/jsp/dashboard.jsp");
			return;
	
		}else if (type.equals("resetpassword")) {
			
			String wNumber = request.getParameter("wNumber").toString();
			String firstName = request.getParameter("firstName").toString();
			String lastName = request.getParameter("lastName").toString();
			resetPassword(wNumber, firstName, lastName);		

		}else if (type.equals("changePassword")) {
			
			String wNumber = request.getParameter("wNumber").toString();
			String theNewPassword = request.getParameter("theNewPassword").toString();
			changePassword(wNumber, theNewPassword);
			response.sendRedirect("http://localhost/WeberRespiratoryTherapy/jsp/dashboard.jsp");
			return;
			
		}else if (type.equals("createUser")) {
			
			String wNumber = request.getParameter("wNumber").toString();
			String firstName = request.getParameter("firstName").toString();
			String lastName = request.getParameter("lastName").toString();
			String email = request.getParameter("email").toString();
			boolean admin = Boolean.parseBoolean((String) request.getParameter("isAdmin"));
			String yearStr = request.getParameter("userYear").toString();
			
			Date userYear = new Date();
			
			if (yearStr.equals("")) {
				
				userYear = new Date();
			}else{
				
				DateFormat format = new SimpleDateFormat("yyyy", Locale.ENGLISH);
				try {
					userYear = format.parse(yearStr);
				} catch (ParseException e) {
					e.printStackTrace();
					
					System.out.println("Failed to parse date from user year");
				}
			}
			
			//if we did NOT successfully create a new user
			if ( ! createUser(wNumber, firstName, lastName, email, userYear, admin)){
				
				session.setAttribute("failedUserCreation", true);
			}
			else{
				response.sendRedirect("dashboard.jsp");
			}
			
		}else if (type.equals("updateUser")){
			
			//String wNumber, String firstName, String lastName, String email, Date year, boolean needsResetPassword, boolean isActive, boolean isAdmin
			
			String wNumber = request.getParameter("wNumber").toString();
			String firstName = request.getParameter("firstName").toString();
			String lastName = request.getParameter("lastName").toString();
			String email = request.getParameter("email").toString();
			boolean isAdmin = Boolean.parseBoolean((String) request.getParameter("isAdmin"));
			String yearStr = request.getParameter("userYear").toString();
			boolean isActive = Boolean.parseBoolean((String) request.getParameter("isActive"));
			boolean needsResetPassword = Boolean.parseBoolean((String) request.getParameter("needsResetPassword"));
			
			Date userYear = new Date();
			
			if (yearStr.equals("")) {
				
				userYear = null;
			}else{
				
				DateFormat format = new SimpleDateFormat("yyyy", Locale.ENGLISH);
				try {
					userYear = format.parse(yearStr);
				} catch (ParseException e) {
					e.printStackTrace();
					
					System.out.println("Failed to parse date from user year");
				}
			}
			
			
			session.setAttribute("updatedUser", updateUser(wNumber, firstName, lastName, email, userYear, needsResetPassword, isActive, isAdmin));
			
			response.sendRedirect("http://localhost/WeberRespiratoryTherapy/jsp/dashboard.jsp");
			return;
		}
		
	}

    //___________________________________________________________________________________________________________________
	
	/**
	 * login function
	 */
	private User login(String username, String password){
		
//		boolean loginSuccessful = false;
		
		User theLoggedInUser = null;
		
		//query the database to compare username and password
		DatabaseConnector connector = new DatabaseConnector();
		try {
			Connection conn = connector.connectDatabase();
			
			DatabaseCalls calls = new DatabaseCalls();
			
			theLoggedInUser = calls.login(conn, username, password);
			
			//if we successfully logged in
//			if (theLoggedInUser != null) {
//				
//				loginSuccessful = true;
//				
//				//add the user to the session
//				session.setAttribute("user", theLoggedInUser);
//				
//				//set the session timeout time (in seconds)
//				session.setMaxInactiveInterval(7200);
//				
//			}
			
			conn.close();
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		return theLoggedInUser;
	}
	
    //___________________________________________________________________________________________________________________
	
	public Map<String, User> getAllUsers(){
		
		//query the database to get a list of ALL users (this includes both admin and students)
		DatabaseConnector connector = new DatabaseConnector();
		try {
			Connection conn = connector.connectDatabase();
			
			DatabaseCalls calls = new DatabaseCalls();
						
			return calls.getAllUsers(conn);
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		return null;
	}
	
	//___________________________________________________________________________________________________________________
	
	public boolean createUser(String wNumber, String firstName, String lastName, String email, Date year, boolean isAdmin){
		
		DatabaseConnector connector = new DatabaseConnector();
		try {
			Connection conn = connector.connectDatabase();
			
			DatabaseCalls calls = new DatabaseCalls();
			
			return calls.createUser(conn, wNumber, firstName, lastName, email, year, isAdmin);
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		return true;
	}
	
	//___________________________________________________________________________________________________________________
	public User userToBeEdited(String wNumber) {
		
		DatabaseConnector connector = new DatabaseConnector();
		try {
			Connection conn = connector.connectDatabase();
			
			DatabaseCalls calls = new DatabaseCalls();
			
			return calls.userToBeEdited(conn, wNumber);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	
	public User updateUser(String wNumber, String firstName, String lastName, String email, Date year, boolean needsResetPassword, boolean isActive, boolean isAdmin){
		
		//TODO
		
		DatabaseConnector connector = new DatabaseConnector();
		try {
			Connection conn = connector.connectDatabase();
			
			DatabaseCalls calls = new DatabaseCalls();
			
			return calls.updateUser(conn, wNumber, firstName, lastName, email, year, needsResetPassword, isActive, isAdmin);
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		return new User();
	}
	
	//___________________________________________________________________________________________________________________
	
	public boolean resetPassword(String wNumber, String firstName, String lastName){
		
		//if password resets successfully, return true.
		
		//Otherwise, return false.
		
		DatabaseConnector connector = new DatabaseConnector();
		try {
			Connection conn = connector.connectDatabase();
			
			DatabaseCalls calls = new DatabaseCalls();
			
			String theDefaultPassword = firstName.charAt(0) + lastName.charAt(0) + wNumber.substring(5);
			
			return calls.changePassword(conn, wNumber, theDefaultPassword);
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		
		return false;
	}
	
	//___________________________________________________________________________________________________________________
	
	public boolean changePassword(String wNumber, String theNewPassword){
				
		//if password is changed successfully, return true.
		
		//Otherwise, return false.
		
		DatabaseConnector connector = new DatabaseConnector();
		try {
			Connection conn = connector.connectDatabase();
			
			DatabaseCalls calls = new DatabaseCalls();
						
			return calls.changePassword(conn, wNumber, theNewPassword);
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		return false;
	}
	
	//___________________________________________________________________________________________________________________
	
}

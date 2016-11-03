package edu.weber.resptherapy.charting;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.weber.resptherapy.charting.model.User;

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
		String type = request.getParameter("type");
		
		HttpSession session = request.getSession();
		
		if (type.equals("login")) {
			
			// session retrieved, continue with servlet operations

			//go to login function passing in user parameters (username, password)
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			
			// statrt of example code
			//show different forms of Hibernate select queries
			DatabaseConnector.hibernateExample1();
			DatabaseConnector.hibernateExample2();
			DatabaseConnector.hibernateExample3();
			DatabaseConnector.hibernateExample4();
			User eu = DatabaseConnector.hibernateInsertExample();
			eu.setUserFirst("Hannibal");
			eu = DatabaseConnector.hibernateUpdateExample(eu);
			DatabaseConnector.hibernateDeleteExample(eu);
			
			/// end of example code
			
			
			User theUser = login(username, password);
			
			session.setAttribute("user", theUser);
			
			//if login is successful
			if(session.getAttribute("user") != null){
								
				System.out.println("Login Successful!");
				
				session.setAttribute("failed", "no");
				if (theUser.isUserAdmin()) {
					session.setAttribute("allUsers", getAllUsers());
				}
				else{
					session.setAttribute("allForms", new ServletForms().getAllForms(theUser.getUserId()));
				}
				
				// TODO session.setAttribute("allTemplates", new ServletTherapy().getAllTemplates());
				
				
				//pass all information forward to the dashboard.jsp
				//TODO can't hard code local host - make into a property
				response.sendRedirect("http://localhost:8080/WeberRespiratoryTherapy/jsp/dashboard.jsp");
				return;
			}
			//if login is UNsuccessful
			else{
				// TODO convert System.outs to logs
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
			response.sendRedirect("http://localhost:8080/WeberRespiratoryTherapy/jsp/dashboard.jsp");
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
			response.sendRedirect("http://localhost:8080/WeberRespiratoryTherapy/jsp/dashboard.jsp");
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
			
			response.sendRedirect("http://localhost:8080/WeberRespiratoryTherapy/jsp/dashboard.jsp");
			return;
		}
		
	}

    //___________________________________________________________________________________________________________________
	
	/**
	 * login function
	 */
	private edu.weber.resptherapy.charting.model.User login(String username, String password){
			edu.weber.resptherapy.charting.model.User theLoggedInUser = null;
			DatabaseCalls calls = new DatabaseCalls();
			return calls.login(username, password);
	}
	
    //___________________________________________________________________________________________________________________
	
	public Map<String, edu.weber.resptherapy.charting.model.User> getAllUsers(){
			DatabaseCalls calls = new DatabaseCalls();
			return calls.getAllUsers();
	}
	
	//___________________________________________________________________________________________________________________
	
	public boolean createUser(String wNumber, String firstName, String lastName, String email, Date year, boolean isAdmin){
		
		try {
			DatabaseCalls calls = new DatabaseCalls();
			
			//TODO: remove first parameter, no longer needed. null reference was a 'Connection' conn java class, not needed for hibernate
			return calls.createUser(wNumber, firstName, lastName, email, year, isAdmin);
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		return true;
	}
	
	//___________________________________________________________________________________________________________________
	public User userToBeEdited(String wNumber) {
		
//		DatabaseConnector connector = new DatabaseConnector();
		try {
//			Connection conn = connector.connectDatabase();
			
			DatabaseCalls calls = new DatabaseCalls();
			
			return calls.userToBeEdited(wNumber);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	
	public User updateUser(String wNumber, String firstName, String lastName, String email, Date year, boolean needsResetPassword, boolean isActive, boolean isAdmin){
		try {
			DatabaseCalls calls = new DatabaseCalls();
			
			//TODO: remove first parameter, no longer needed. null reference was a 'Connection' conn java class, not needed for hibernate
			return calls.updateUser(null, wNumber, firstName, lastName, email, year, needsResetPassword, isActive, isAdmin);
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		return new User();
	}
	
	//___________________________________________________________________________________________________________________
	
	public boolean resetPassword(String wNumber, String firstName, String lastName){
		
		//if password resets successfully, return true.
		
		//Otherwise, return false.
		try {
			DatabaseCalls calls = new DatabaseCalls();
			
			String theDefaultPassword = firstName.charAt(0) + lastName.charAt(0) + wNumber.substring(5);
			
			//TODO: remove first parameter, no longer needed. null reference was a 'Connection' conn java class, not needed for hibernate
			return calls.changePassword(wNumber, theDefaultPassword);
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		
		return false;
	}
	
	//___________________________________________________________________________________________________________________
	
	public boolean changePassword(String wNumber, String theNewPassword){
				
		//if password is changed successfully, return true.
		
		//Otherwise, return false.
		
		try {
			DatabaseCalls calls = new DatabaseCalls();
						
			//TODO: remove first parameter, no longer needed. null reference was a 'Connection' conn java class, not needed for hibernate
			return calls.changePassword(wNumber, theNewPassword);
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		return false;
	}
	
	//___________________________________________________________________________________________________________________
	
}

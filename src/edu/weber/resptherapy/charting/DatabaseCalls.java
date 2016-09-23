package edu.weber.resptherapy.charting;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DatabaseCalls {
	
	//user block
	
	//___________________________________________________________________________________________________________________
	
	public User login(Connection conn, String username, String password){
		
		PreparedStatement statement = null;
		
		User theUser = null;
		
		String query = "CALL sp_Login(?,?)";
		
		try {
			
			statement = conn.prepareStatement(query);
			
			statement.setString(1, username);
			statement.setString(2, password);
			
			ResultSet result = statement.executeQuery();

			//move the row pointer to the first row (if there's a first row, then a user exists)
			if(result.first()){

				String userId = result.getString("UserID");
				String firstName = result.getString("UserFirst");
				String lastName = result.getString("UserLast");
				String email = result.getString("UserEmail");
				int isAdmin = result.getInt("UserAdmin");
				Date year = result.getDate("UserYear");
				int isActive = result.getInt("UserActive");
				int needsResetPassword = result.getInt("UserPassReset");
				
				theUser = new User(userId, firstName, lastName, email, isAdmin, year, isActive, needsResetPassword);
				
				int needsPasswordReset = result.getInt("UserPassReset");
				
				//if user's password does NOT need to be reset
				if (needsPasswordReset == 0) {
					
					theUser.setNeedsResetPassword(false);
				}
				//if user's password needs to be reset
				else{

					theUser.setNeedsResetPassword(true);
				}
				
				
			}
			//no user exists (there's no first row)
			else{
				return null;
			}
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			theUser = null;
		}
		
		if (theUser == null) {
			return new User();
		}
		else{
			return theUser;
		}
	}
	
	//___________________________________________________________________________________________________________________
	
	public Map<String, User> getAllUsers(Connection conn){
		
		Map<String, User> mapOfAllUsers = new HashMap<String, User>();
		
		PreparedStatement statement = null;
				
		String query = "CALL sp_GetAllUsers()";
		
		try {
			
			statement = conn.prepareStatement(query);
			
			ResultSet result = statement.executeQuery();
			
			while(result.next()){
				
				String userId = result.getString("UserID");
				String firstName = result.getString("UserFirst");
				String lastName = result.getString("UserLast");
				String email = result.getString("UserEmail");
				int isAdmin = result.getInt("UserAdmin");
				Date year = result.getDate("UserYear");
				int isActive = result.getInt("UserActive");
				int needsResetPassword = result.getInt("UserPassReset");
				
				User theUser = new User(userId, firstName, lastName, email, isAdmin, year, isActive, needsResetPassword);
				
				mapOfAllUsers.put(result.getString("UserID"), theUser);
			}
			
			conn.close();
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		return mapOfAllUsers;
	}
	
	public User userToBeEdited(Connection conn, String wNumber) {
		
		//returns a user based on it's wNumber (UserID)
		PreparedStatement statement = null;
		
		String query = "SELECT UserID, UserFirst, UserLast, UserAdmin, UserPassreset, UserEmail, UserActive, UserYear FROM USER WHERE UserID = ?";
		
		User userToBeEdited = null;
		
		try {		
			
			statement = conn.prepareStatement(query);
			
			//put the user's W Number into the query
			statement.setString(1, wNumber);
			
			//execute the query which will return the updated User
			ResultSet result = statement.executeQuery();
			
			while(result.next()){
				
				String updatedUserId = result.getString("UserID");
				String updatedFirstName = result.getString("UserFirst");
				String updatedLastName = result.getString("UserLast");
				String updatedEmail = result.getString("UserEmail");
				int updatedIsAdmin = result.getInt("UserAdmin");
				Date updatedYear = result.getDate("UserYear");
				int updatedIsActive = result.getInt("UserActive");
				int updatedNeedsResetPassword = result.getInt("UserPassReset");
				
				userToBeEdited = 
						new User(updatedUserId, updatedFirstName, updatedLastName, updatedEmail, updatedIsAdmin, updatedYear, updatedIsActive, updatedNeedsResetPassword);
				
			}
			
			conn.close();
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
			System.out.println("Failed to updated user.");
			
			return userToBeEdited; //if we get to this point, the updated user should equal null
		}
		
		return userToBeEdited;

	}

	//___________________________________________________________________________________________________________________
	
	public boolean createUser(Connection conn, String wNumber, String firstName, String lastName, String email, Date year, boolean isAdmin) {

		//create default password for new user that they are required to go in and change
		String userPassword = firstName.substring(0,1).toLowerCase() + lastName.substring(0,1).toLowerCase() + wNumber.substring(wNumber.length() - 4);
		
		int admin = 0;
		
		if (isAdmin) {
			admin = 1;
		}
				
		PreparedStatement statement = null;
		
		//wnumber, first, last, pass, isAdmin, passreset, email, isactive, year
		String query = "CALL sp_AddUser(?,?,?,?,?,?,?,?,?)";
		
		try {
			
			statement = conn.prepareStatement(query);

			statement.setString(1, wNumber);
			statement.setString(2, firstName);
			statement.setString(3, lastName);
			statement.setString(4, userPassword);
			statement.setInt(5, admin);
			statement.setInt(6, 1); //default value is 1
			statement.setString(7, email);
			statement.setInt(8, 1); //default value is 1
			statement.setDate(9, new java.sql.Date(year.getTime()));
			
			ResultSet result = statement.executeQuery();
			
			conn.close();
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
			System.out.println("Failed to create new user");
			
			return false;
		}
		
		System.out.println("Successfully created new user!");
		return true;
	}
	
	//___________________________________________________________________________________________________________________
	
	public User updateUser(Connection conn, String wNumber, String firstName, String lastName, String email, Date year, boolean needsResetPassword, boolean isActive, boolean isAdmin){
		//todo add select statement to get the changes to the user.
		//if admin simply rerun getAllUsers
		User theUpdatedUser = null;
		
		PreparedStatement statement = null;
				
		String query = "CALL sp_UpdateUser(?,?,?,?,?,?,?,?)";
		
		try {
			
			statement = conn.prepareStatement(query);
			
			statement.setString(1, wNumber);
			statement.setString(2, firstName);
			statement.setString(3, lastName);
			statement.setBoolean(4, isAdmin);
			statement.setBoolean(5, needsResetPassword);
			statement.setString(6, email);
			statement.setBoolean(7, isActive);
			statement.setDate(8, new java.sql.Date(year.getTime()));
						
			//execute the query to update the user
			statement.executeQuery();
			
			//set up new SQL query to fetch the user that we just now updated
			query = "SELECT UserID, UserFirst, UserLast, UserAdmin, UserYear, UserEmail, UserActive, UserPassReset "
					+ "FROM USER "
					+ "WHERE UserID = ?";
		
			
			statement = conn.prepareStatement(query);
			
			//put the user's W Number into the query
			statement.setString(1, wNumber);
			
			//execute the query which will return the updated User
			ResultSet result = statement.executeQuery();
			
			while(result.next()){
				
				String updatedUserId = result.getString("UserID");
				String updatedFirstName = result.getString("UserFirst");
				String updatedLastName = result.getString("UserLast");
				String updatedEmail = result.getString("UserEmail");
				int updatedIsAdmin = result.getInt("UserAdmin");
				Date updatedYear = result.getDate("UserYear");
				int updatedIsActive = result.getInt("UserActive");
				int updatedNeedsResetPassword = result.getInt("UserPassReset");
				
				theUpdatedUser = 
						new User(updatedUserId, updatedFirstName, updatedLastName, updatedEmail, updatedIsAdmin, updatedYear, updatedIsActive, updatedNeedsResetPassword);
				
			}
			
			conn.close();
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
			System.out.println("Failed to updated user.");
			
			return theUpdatedUser; //if we get to this point, the updated user should equal null
		}
		
		return theUpdatedUser;
	}
	
	//___________________________________________________________________________________________________________________
	
	public boolean changePassword(Connection conn, String wNumber, String theNewPassword){
		
		//TODO need stored procedure
		
		PreparedStatement statement = null;
		
		//the W number and the new password
		String query = "CALL sp_ChangePassword(?,?)";
		
		try {
			
			statement = conn.prepareStatement(query);

			statement.setString(1, theNewPassword);
			statement.setString(2, wNumber);
			
			statement.executeQuery();
			
			conn.close();
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
			System.out.println("Failed to change password");
			
			return false;
		}
		
		System.out.println("Successfully changed password");
		return true;
	}
	
	//___________________________________________________________________________________________________________________
	
	//form block
	
	public boolean updateForm(Connection conn, String theFormName, String theFormHtml, int theFormId, String wNumber){
		//calls sp_UpdateForm in the database.
		//Call getAllForms after successful return.
		//Form theUpdatedForm = null;
		
		PreparedStatement statement = null;
				
		//form name, form html, form ID, user ID
		String query = "CALL sp_UpdateForm(?,?,?,?)";
		
		try {
			
			statement = conn.prepareStatement(query);
			
			statement.setString(1, theFormName);
			statement.setString(2, theFormHtml);
			statement.setInt(3, theFormId);
			statement.setString(4, wNumber);
			
			statement.executeQuery();
						
			//ResultSet result = statement.executeQuery();
			
//			while(result.next()){
//				
//				String updatedFormName = result.getString("UserFormName");
//				String updatedFormHtml = result.getString("UserFormHtml");
//				int updatedWnumber = result.getInt("UserFormID");
//				String updatedWNumber = result.getString("UID");
//				
//				theUpdatedForm = 
//						new Form(updatedFormName, updatedFormHtml, updatedWnumber, updatedWNumber);
//				
//			}
			
			
			conn.close();
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
			System.out.println("Failed to updated Form.");
			
			return false; //if we get to this point, the updated user should equal null
		}
		
		return true;
	}

	//___________________________________________________________________________________________________________________
	
	public Map<Integer,Form> getAllForms(Connection conn, String userID){
		
		Map<Integer, Form> mapOfAllUserForms = new HashMap<Integer, Form>();
		
		PreparedStatement statement = null;
		
		String query = "CALL sp_GetAllForms(?)";
		
		try{
			statement = conn.prepareStatement(query);
			
			statement.setString(1, userID);
			
			ResultSet result = statement.executeQuery();
			
			while(result.next()){
				int formID = result.getInt("UserFormID");
				Blob formHTMLBlob = result.getBlob("UserFormHtml");
				String formHTML = new String(formHTMLBlob.getBytes(1, (int)formHTMLBlob.length()));
				String UID = result.getString("USER_UserID");
				Date lastEdit = result.getDate("UserFormLastEdit");
				String formName = result.getString("UserFormName");
				
				Form tempForm = new Form(formID,formName,formHTML,UID,lastEdit);
				
				mapOfAllUserForms.put(formID, tempForm);
			}
			
			conn.close();
			
			return mapOfAllUserForms;
			
		}catch(Exception e){
			return null;
		}
	}
	
	//___________________________________________________________________________________________________________________
	
	public boolean createForm(Connection conn, String formHTML, String userID, Date lastEdit, String formName){
		//After successful form creation call getAllForms to refresh list.
		PreparedStatement statement = null;
		
		String query = "CALL sp_CreateForm(?,?,?,?)";
		
		try{
			statement = conn.prepareStatement(query);
			
			Blob html = conn.createBlob();
			html.setBytes(1, formHTML.getBytes());
			statement.setBlob(1, html);
			statement.setString(2, userID);
			statement.setDate(3, new java.sql.Date(lastEdit.getTime()));
			statement.setString(4, formName);
			
			statement.execute();
			
			conn.close();
			
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	//___________________________________________________________________________________________________________________
	
	//template block
	
	public boolean createTemplate(Connection conn, String templateName, String templateHTML, String templateType){
		//After successful template creation call getAllTemplates to refresh list.
		//todo: should handle the creation of a new version when stored procedure is called and auto set template version.
		PreparedStatement statement = null;
		
		String query = "CALL sp_CreateTemplate(?,?,?)";
		
		try{
			
			statement = conn.prepareStatement(query);
			
			statement.setString(1, templateName);
			
			//create html blob
			Blob html = conn.createBlob();
			//covert to bytes
			html.setBytes(1, templateHTML.getBytes());
			
			statement.setBlob(2, html);
			statement.setString(3, templateType);
			
			statement.execute();
			
			conn.close();
			
			return true;
			
		}catch(Exception e){
			return false;
		}
	}
	
	//___________________________________________________________________________________________________________________
	
	public boolean updateTemplate(Connection conn, int templateID, String templateName){
		//Only called to switch active version of template with previous version.
		PreparedStatement statement = null;
		
		String query = "CALL sp_UpdateFormTemplate(?,?)";
		
		try{
			
			statement = conn.prepareStatement(query);
			
			statement.setInt(1, templateID);
			statement.setString(2, templateName);
			
			statement.execute();
			
			conn.close();
			
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	//___________________________________________________________________________________________________________________
	
	public Map<Integer, TherapyTemplate> getAllTemplates(Connection conn){
		//Use the template id as the key as name is not distinct enough of a value for key.
		Map<Integer, TherapyTemplate> allTemplates = new HashMap<Integer, TherapyTemplate>();
		
		PreparedStatement statement = null;
		
		String query = "CALL sp_GetFormTemplates()";
		try{
			
			
			statement = conn.prepareStatement(query);
			
			ResultSet result = statement.executeQuery();
			
			while(result.next()){
				int templateID = result.getInt("FormTemplateID");
				Blob templateHTMLBlob = result.getBlob("FormTemplateHTML");
				String templateHTML = new String(templateHTMLBlob.getBytes(1, (int)templateHTMLBlob.length()));
				String templateName = result.getString("FormTemplateName");
				String templateType = result.getString("FormTemplateType");
//				boolean templateActive = result.getBoolean("FormTemplateActive");
				int activeInt = result.getInt("FormTemplateActive");
				int templateVersion = result.getInt("FormTemplateVersionNum");
				
				boolean templateActive = true;
				if (activeInt == 0) {
					templateActive = false;
				}
				
				if ("".equals(templateType)) {
					templateType = "draft";
				}
				
				TherapyTemplate tempTemplate = new TherapyTemplate(templateName, templateActive, templateHTML, templateID, templateType,templateVersion);
				
				allTemplates.put(templateID, tempTemplate);
			}
			
			conn.close();
			
			return allTemplates;
		}catch(Exception e){
			return null;
		}
		
	}
	
	//___________________________________________________________________________________________________________________
	
	public String generatePDF(Connection conn, String userID, int formID){
		
		PreparedStatement statement = null;
		
		String query = "CALL sp_GetUserForm(?,?)";
		try{
			
			
			statement = conn.prepareStatement(query);
			
			statement.setString(1, userID);
			statement.setInt(2, formID);
			
			ResultSet result = statement.executeQuery();
			
			Blob formHTMLBlob = null;
			String formHTML = null;
			
			while(result.next()){
				
				formHTMLBlob = result.getBlob("UserFormHtml");
				formHTML = new String(formHTMLBlob.getBytes(1, (int)formHTMLBlob.length()));
			}
			
			conn.close();
			
			return formHTML;
			
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
	}
	
	//___________________________________________________________________________________________________________________
	
		public TherapyTemplate getTemplateToFillOut(Connection conn, String therapyId){
			
			PreparedStatement statement = null;
			
			String query = "CALL sp_GetTemplate(?)";
			try{	
				
				statement = conn.prepareStatement(query);
				
				statement.setInt(1, Integer.parseInt(therapyId));
				
				ResultSet result = statement.executeQuery();
				
				int templateID = 0;
				Blob templateHTMLBlob = null;
				String templateHTML = null;
				String templateName = null;
				String templateType = null;
				int activeInt = -1;
				int templateVersion = -1;
				boolean templateActive = true;
				
				while(result.next()){
					
					templateID = result.getInt("FormTemplateID");
					templateHTMLBlob = result.getBlob("FormTemplateHTML");
					templateHTML = new String(templateHTMLBlob.getBytes(1, (int)templateHTMLBlob.length()));
					templateName = result.getString("FormTemplateName");
					templateType = result.getString("FormTemplateType");
					activeInt = result.getInt("FormTemplateActive");
					templateVersion = result.getInt("FormTemplateVersionNum");
					
					if (activeInt == 0) {
						templateActive = false;
					}
				}
				
				TherapyTemplate tempTemplate = new TherapyTemplate(templateName, templateActive, templateHTML, templateID, templateType, templateVersion);
				
				conn.close();
				
				return tempTemplate;
				
			}catch(Exception e){
				e.printStackTrace();
				return null;
			}
			
		}
	
		
		public Form getFormToEdit(Connection conn, String userID, int formID){
			
			PreparedStatement statement = null;
			
			String query = "CALL sp_GetUserForm(?,?)";
			try{
				
				
				statement = conn.prepareStatement(query);
				
				statement.setString(1, userID);
				statement.setInt(2, formID);
				
				ResultSet result = statement.executeQuery();
				
				int formId = 0;
				String name = null;
				String formHTML = null;
				Blob formHTMLBlob = null;
				String UID = null;
				Date lastEdit = null;
				String formName = null;
				
				while(result.next()){
					
					formId = result.getInt("UserFormID");
					formHTMLBlob = result.getBlob("UserFormHtml");
					formHTML = new String(formHTMLBlob.getBytes(1, (int)formHTMLBlob.length()));
					UID = result.getString("USER_UserID");
					lastEdit = result.getDate("UserFormLastEdit");
					formName = result.getString("UserFormName");
				}
				
				Form newForm = new Form(formId, formName, formHTML, UID, lastEdit);
				
				conn.close();
				
				return newForm;
				
			}catch(Exception e){
				e.printStackTrace();
				return null;
			}
			
		}
}

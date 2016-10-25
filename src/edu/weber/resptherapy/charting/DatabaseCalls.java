package edu.weber.resptherapy.charting;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import edu.weber.resptherapy.charting.model.Formtemplate;
import edu.weber.resptherapy.charting.model.User;
import edu.weber.resptherapy.charting.model.Userform;

public class DatabaseCalls {
	private static String NOTFOUND = "No User found!";
	//user block
	
	//___________________________________________________________________________________________________________________
	public User login(String username, String password){
		edu.weber.resptherapy.charting.model.User user = null;
		try {		
			Session session = DatabaseConnector.getCurrentSession();
			Transaction tx = session.beginTransaction();
			Criteria cr = session.createCriteria(edu.weber.resptherapy.charting.model.User.class);
			cr.add(Restrictions.eq("userId", username)); //userId
			cr.add(Restrictions.eq("password", password)); //password
			List<edu.weber.resptherapy.charting.model.User> results = cr.list();
			if (results.size() < 1) {
				throw new HibernateException("No User found!");
			}
			user = results.get(0);
		} catch (Exception e){
			//log.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage());
		} finally {
			DatabaseConnector.closeSession();
		}	
		return user;
	}

	
	//___________________________________________________________________________________________________________________
	public Map<String, User> getAllUsers(){
		Map<String, edu.weber.resptherapy.charting.model.User> mapOfAllUsers = new HashMap<String, edu.weber.resptherapy.charting.model.User>();
		try {		
			Session session = DatabaseConnector.getCurrentSession();
			Transaction tx = session.beginTransaction();
			Criteria cr = session.createCriteria(edu.weber.resptherapy.charting.model.User.class);
			List<edu.weber.resptherapy.charting.model.User> results = cr.list();
			if (results.size() < 1) {
				throw new HibernateException("No Users found!");
			}
			Iterator it = results.iterator();
			while (it.hasNext()) {
				edu.weber.resptherapy.charting.model.User u = (edu.weber.resptherapy.charting.model.User) it.next();
				mapOfAllUsers.put(u.getUserId(), u);
			}
		} catch (HibernateException e){
			//log.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage());
		} finally {
			DatabaseConnector.closeSession();
		}	
		return mapOfAllUsers;		
	}
	

	
	public User userToBeEdited( String wNumber) {
		User u = new User(); // TODO hibernate conversion
		return u;
		
		//returns a user based on it's wNumber (UserID)
//		PreparedStatement statement = null;
//		
//		String query = "SELECT UserID, UserFirst, UserLast, UserAdmin, UserPassreset, UserEmail, UserActive, UserYear FROM USER WHERE UserID = ?";
//		
//		User userToBeEdited = null;
//		
//		try {		
//			
//			statement = conn.prepareStatement(query);
//			
//			//put the user's W Number into the query
//			statement.setString(1, wNumber);
//			
//			//execute the query which will return the updated User
//			ResultSet result = statement.executeQuery();
//			
//			while(result.next()){
//				
//				String updatedUserId = result.getString("UserID");
//				String updatedFirstName = result.getString("UserFirst");
//				String updatedLastName = result.getString("UserLast");
//				String updatedEmail = result.getString("UserEmail");
//				int updatedIsAdmin = result.getInt("UserAdmin");
//				Date updatedYear = result.getDate("UserYear");
//				int updatedIsActive = result.getInt("UserActive");
//				int updatedNeedsResetPassword = result.getInt("UserPassReset");
//				
////TODO - replace with Hibernate				userToBeEdited = 
////						new User(updatedUserId, updatedFirstName, updatedLastName, updatedEmail, updatedIsAdmin, updatedYear, updatedIsActive, updatedNeedsResetPassword);
//				
//			}
//			
//			conn.close();
//			
//		} catch (Exception e) {
//			
//			e.printStackTrace();
//			
//			System.out.println("Failed to updated user.");
//			
//			return userToBeEdited; //if we get to this point, the updated user should equal null
//		}
//		
//		return userToBeEdited;

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
				
//TODO: hibernate				theUpdatedUser = 
//						new User(updatedUserId, updatedFirstName, updatedLastName, updatedEmail, updatedIsAdmin, updatedYear, updatedIsActive, updatedNeedsResetPassword);
				
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
	
	//Formtemplate block
	
	public boolean updateForm( String theFormtemplateName, String theFormtemplateHtml, int theFormtemplateId, String wNumber){
			return true;  // TODO Hibernate
		
//		//calls sp_UpdateFormtemplate in the database.
//		//Call getAllFormtemplates after successful return.
//		//Formtemplate theUpdatedFormtemplate = null;
//		
//		PreparedStatement statement = null;
//				
//		//Formtemplate name, Formtemplate html, Formtemplate ID, user ID
//		String query = "CALL sp_UpdateFormtemplate(?,?,?,?)";
//		
//		try {
//			
//			statement = conn.prepareStatement(query);
//			
//			statement.setString(1, theFormtemplateName);
//			statement.setString(2, theFormtemplateHtml);
//			statement.setInt(3, theFormtemplateId);
//			statement.setString(4, wNumber);
//			
//			statement.executeQuery();
//						
			//ResultSet result = statement.executeQuery();
			
//			while(result.next()){
//				
//				String updatedFormtemplateName = result.getString("UserFormtemplateName");
//				String updatedFormtemplateHtml = result.getString("UserFormtemplateHtml");
//				int updatedWnumber = result.getInt("UserFormtemplateID");
//				String updatedWNumber = result.getString("UID");
//				
//TODO HIBERNATE				theUpdatedFormtemplate = 
//						new Formtemplate(updatedFormtemplateName, updatedFormtemplateHtml, updatedWnumber, updatedWNumber);
//				
//			}
			
//			
//			conn.close();
//			
//		} catch (Exception e) {
//			
//			e.printStackTrace();
//			
//			System.out.println("Failed to updated Formtemplate.");
//			
//			return false; //if we get to this point, the updated user should equal null
//		}
//		
//		return true;
	}

	//___________________________________________________________________________________________________________________
	public Map<Integer,Userform> getAllForms( String userID){
		Map<Integer, Userform> mapTemplates = new HashMap<Integer, Userform>();
		try {		
			Session session = DatabaseConnector.getCurrentSession();
			Transaction tx = session.beginTransaction();
			User u = new User(userID);
			
			Criteria cr_userFormtemplate = session.createCriteria(Userform.class);
			cr_userFormtemplate.add(Restrictions.eq("user", u)); //userId
			List<Userform> results = cr_userFormtemplate.list();
			if (results.size() < 1) {
				throw new HibernateException("No User found!");
			}
			Iterator it = results.iterator();
			while (it.hasNext()) {
				Userform f = (Userform) it.next();
				mapTemplates.put(f.getUserFormId(), f);
			}

		} catch (HibernateException e){
			//log.error(e.getMessage(), e);
			if (!NOTFOUND.equals(e.getMessage())) {
				throw new RuntimeException(e.getMessage());
			}
		} finally {
			DatabaseConnector.closeSession();
		}	
		return mapTemplates;
	}
	

	
	//___________________________________________________________________________________________________________________
	
	public boolean createForm( String FormtemplateHTML, String userID, Date lastEdit, String FormtemplateName){
		Formtemplate formtemplate = null; // complete with hibernate
		return true;  // false if there are errors
		
		//After successful Formtemplate creation call getAllFormtemplates to refresh list.
//		PreparedStatement statement = null;
//		
//		String query = "CALL sp_CreateFormtemplate(?,?,?,?)";
		
//TODO HIBERNATE		try{
//			statement = conn.prepareStatement(query);
//			
//			Blob html = conn.createBlob();
//			html.setBytes(1, FormtemplateHTML.getBytes());
//			statement.setBlob(1, html);
//			statement.setString(2, userID);
//			statement.setDate(3, new java.sql.Date(lastEdit.getTime()));
//			statement.setString(4, FormtemplateName);
//			
// TODO HIBERNATE			statement.execute();
//			
//			conn.close();
//			
//			return true;
//		}catch(Exception e){
//			return false;
//		}
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
			
// TODO HIBERNATE			statement.execute();
			
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
		
		String query = "CALL sp_UpdateFormtemplateTemplate(?,?)";
		
		try{
			
			statement = conn.prepareStatement(query);
			
			statement.setInt(1, templateID);
			statement.setString(2, templateName);
			
// TODO HIBERNATE			statement.execute();
			
			conn.close();
			
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	//___________________________________________________________________________________________________________________
	public Map<Integer,Formtemplate> getAllTemplates(){
		Map<Integer, Formtemplate> mapTemplates = new HashMap<Integer, Formtemplate>();
		try {		
			Session session = DatabaseConnector.getCurrentSession();
			Transaction tx = session.beginTransaction();
			Criteria cr_userFormtemplate = session.createCriteria(Formtemplate.class);
			List<Formtemplate> results = cr_userFormtemplate.list();
			if (results.size() < 1) {
				throw new HibernateException("No User found!");
			}
			Iterator it = results.iterator();
			while (it.hasNext()) {
				Formtemplate f = (Formtemplate) it.next();
				mapTemplates.put(f.getFormTemplateId(), f);
			}

		} catch (HibernateException e){
			//log.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage());
		} finally {
			DatabaseConnector.closeSession();
		}	
		return mapTemplates;
	}

	
	//___________________________________________________________________________________________________________________
	
	public String generatePDF(Connection conn, String userID, int FormtemplateID){
		
		PreparedStatement statement = null;
		
		String query = "CALL sp_GetUserFormtemplate(?,?)";
		try{
			
			
			statement = conn.prepareStatement(query);
			
			statement.setString(1, userID);
			statement.setInt(2, FormtemplateID);
			
//TODO HIBERNATE			ResultSet result = statement.executeQuery();
			
			Blob FormtemplateHTMLBlob = null;
			String FormtemplateHTML = null;
			
//			while(result.next()){
//				
//				FormtemplateHTMLBlob = result.getBlob("UserFormtemplateHtml");
//				FormtemplateHTML = new String(FormtemplateHTMLBlob.getBytes(1, (int)FormtemplateHTMLBlob.length()));
//			}
			
			conn.close();
			
			return FormtemplateHTML;
			
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
	}
	
	//___________________________________________________________________________________________________________________
	
		public Formtemplate getTemplateToFillOut( String therapyId){
			Formtemplate formtemplate = null; // TODO complete with hibernate
			return formtemplate;
			
			
//			PreparedStatement statement = null;
//			
//			String query = "CALL sp_GetTemplate(?)";
//			try{	
//				
//				statement = conn.prepareStatement(query);
//				
//				statement.setInt(1, Integer.parseInt(therapyId));
				
//TODO HIBERNATE				ResultSet result = statement.executeQuery();
				
//				int templateID = 0;
//				Blob templateHTMLBlob = null;
//				String templateHTML = null;
//				String templateName = null;
//				String templateType = null;
//				int activeInt = -1;
//				int templateVersion = -1;
//				boolean templateActive = true;
				
//				while(result.next()){
//					
//					templateID = result.getInt("FormtemplateTemplateID");
//					templateHTMLBlob = result.getBlob("FormtemplateTemplateHTML");
//					templateHTML = new String(templateHTMLBlob.getBytes(1, (int)templateHTMLBlob.length()));
//					templateName = result.getString("FormtemplateTemplateName");
//					templateType = result.getString("FormtemplateTemplateType");
//					activeInt = result.getInt("FormtemplateTemplateActive");
//					templateVersion = result.getInt("FormtemplateTemplateVersionNum");
//					
//					if (activeInt == 0) {
//						templateActive = false;
//					}
//				}
//				
//				TherapyTemplate tempTemplate = new TherapyTemplate(templateName, templateActive, templateHTML, templateID, templateType, templateVersion);
//				
//				conn.close();
//				
//				return tempTemplate;
				
//			}catch(Exception e){
//				e.printStackTrace();
//				return null;
//			}
//			return formtemplate;
		}
	
		
		public Userform getFormToEdit(String userID, int userFormId){
			Userform formtemplate = null; // complete with hibernate

			try {		
				Session session = DatabaseConnector.getCurrentSession();
				Transaction tx = session.beginTransaction();
				User u =  new User(userID);
				Criteria cr = session.createCriteria(Userform.class);
				cr.add(Restrictions.eq("user", u)); //userId
				cr.add(Restrictions.eq("userFormId", userFormId)); 
				formtemplate = (Userform) cr.uniqueResult();
				if (formtemplate == null) {
					throw new HibernateException("Template not found!");
				}

			} catch (HibernateException e){
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			} finally {
				DatabaseConnector.closeSession();
			}	
			
			return formtemplate;
			
			
//TODO HIBERNATE			PreparedStatement statement = null;
//			
//			String query = "CALL sp_GetUserFormtemplate(?,?)";
//			try{
//				
//				
//				statement = conn.prepareStatement(query);
//				
//				statement.setString(1, userID);
//				statement.setInt(2, FormtemplateID);
//				
//				ResultSet result = statement.executeQuery();
//				
//				int FormtemplateId = 0;
//				String name = null;
//				String FormtemplateHTML = null;
//				Blob FormtemplateHTMLBlob = null;
//				String UID = null;
//				Date lastEdit = null;
//				String FormtemplateName = null;
//				
//				while(result.next()){
//					
//					FormtemplateId = result.getInt("UserFormtemplateID");
//					FormtemplateHTMLBlob = result.getBlob("UserFormtemplateHtml");
//					FormtemplateHTML = new String(FormtemplateHTMLBlob.getBytes(1, (int)FormtemplateHTMLBlob.length()));
//					UID = result.getString("USER_UserID");
//					lastEdit = result.getDate("UserFormtemplateLastEdit");
//					FormtemplateName = result.getString("UserFormtemplateName");
//				}
//				
//				Formtemplate newFormtemplate = new Formtemplate(FormtemplateId, FormtemplateName, FormtemplateHTML, UID, lastEdit);
//				
//				conn.close();
//				
//				return newFormtemplate;
//				
//			}catch(Exception e){
//				e.printStackTrace();
//				return null;
//			}
			
		}
}

package edu.weber.resptherapy.charting;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
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

	//___________________________________________________________________________________________________________________

	public User userToBeEdited(String wNumber) {
		edu.weber.resptherapy.charting.model.User user = null; // TODO hibernate conversion
		try {
			Session session = DatabaseConnector.getCurrentSession();
			Transaction tx = session.beginTransaction();
			Criteria cr = session.createCriteria(edu.weber.resptherapy.charting.model.User.class);
			cr.add(Restrictions.eq("userId", wNumber)); //wNumber
			User result = (User) cr.uniqueResult();
			if (result == null) {
				throw new HibernateException("No User found!");
			}
			user = result;
		} catch (HibernateException e){
			//log.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage());
		} finally {
			DatabaseConnector.closeSession();
		}
		return user;
	}

	//___________________________________________________________________________________________________________________

	public boolean createUser(String wNumber, String firstName, String lastName, String email, String year, boolean isAdmin) {
		User user = new User();
		user.setUserId(wNumber);
		user.setUserFirst(firstName);
		user.setUserLast(lastName);
		user.setPassword(firstName.substring(0,1).toLowerCase() +
				lastName.substring(0,1).toLowerCase() +
				wNumber.substring(wNumber.length() - 4));
		user.setUserAdmin(isAdmin);
		user.setUserPassReset(true);
		user.setUserActive(true);
		user.setUserEmail(email);
		user.setUserYear(year);

		try {
			Session session = DatabaseConnector.getCurrentSession();
			Transaction tx = session.beginTransaction();
			session.save(user);
			tx.commit();
		} catch (HibernateException e){
			//log.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage());
		} finally {
			DatabaseConnector.closeSession();
		}
		return true;
	}
	
	//___________________________________________________________________________________________________________________

	public User updateUser(Connection conn, String wNumber, String firstName, String lastName, String email, String year, boolean needsResetPassword, boolean isActive, boolean isAdmin){
		//todo add select statement to get the changes to the user.
		//if admin simply rerun getAllUsers
		User theUpdatedUser = null;
		
		PreparedStatement statement = null;
				
		String query = "CALL sp_UpdateUser(?,?,?,?,?,?,?,?)";
		
		try {
			SimpleDateFormat formatter = new SimpleDateFormat();
			statement = conn.prepareStatement(query);
			
			statement.setString(1, wNumber);
			statement.setString(2, firstName);
			statement.setString(3, lastName);
			statement.setBoolean(4, isAdmin);
			statement.setBoolean(5, needsResetPassword);
			statement.setString(6, email);
			statement.setBoolean(7, isActive);
			statement.setDate(8, new java.sql.Date(formatter.parse(year).getTime()));
						
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
	
	public boolean changePassword(String wNumber, String theNewPassword){

		edu.weber.resptherapy.charting.model.User user = null;
		try {
			Session session = DatabaseConnector.getCurrentSession();
			Transaction tx = session.beginTransaction();
			Criteria cr = session.createCriteria(edu.weber.resptherapy.charting.model.User.class);
			cr.add(Restrictions.eq("userId", wNumber));
			List<edu.weber.resptherapy.charting.model.User> results = cr.list();
			if (results.size() < 1) {
				throw new HibernateException("No User found!");
			}
			user = results.get(0);
			user.setPassword(theNewPassword);
			session.saveOrUpdate(user);
			tx.commit();
		} catch (Exception e){
			//log.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage());
		} finally {
			DatabaseConnector.closeSession();
		}
		return true;
	}
	
	//___________________________________________________________________________________________________________________
	
	//UserForm block
	
	public boolean updateForm(String userFormName, String theFormtemplateHtml, String formDataValues, int userFormId, String wNumber){

		edu.weber.resptherapy.charting.model.Userform userform = null;
		try {
			Session session = DatabaseConnector.getCurrentSession();
			Transaction tx = session.beginTransaction();

			Criteria cr = session.createCriteria(edu.weber.resptherapy.charting.model.Userform.class);
			Criteria userCriteria = cr.createCriteria("user");
			userCriteria.add(Restrictions.eq("userId", wNumber));
			cr.add(Restrictions.eq("userFormId", userFormId));
			List<edu.weber.resptherapy.charting.model.Userform> results = cr.list();

			if (results.size() < 1) {
				throw new HibernateException("No UserForm found!");
			}

			userform = results.get(0);
			if(userFormName != null && userFormName != "") {
				userform.setUserFormName(userFormName);
			}
			userform.setFormTemplateHtml(theFormtemplateHtml);
			userform.setFormDataValues(formDataValues);
			session.saveOrUpdate(userform);
			tx.commit();
			
		} catch (Exception e){
			//log.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage());
		} finally {
			DatabaseConnector.closeSession();
		}
		return true;
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
	
	public boolean createForm(String formTemplateHtml, String formDataValues, String userId, Date lastEdit, String formName){

		Userform userform = new Userform();
		userform.setUserFormName(formName);
		userform.setFormTemplateHtml(formTemplateHtml);
		userform.setFormDataValues(formDataValues);
		userform.setUserFormLastEdit(lastEdit);
		userform.setUser(userToBeEdited(userId));

		try {
			Session session = DatabaseConnector.getCurrentSession();
			Transaction tx = session.beginTransaction();
			session.save(userform);
			tx.commit();
		} catch (HibernateException e){
			throw new RuntimeException(e.getMessage());
		} finally {
			DatabaseConnector.closeSession();
		}
		return true;
	}
	
	//___________________________________________________________________________________________________________________
	
	//template block
	
	public boolean createTemplate( String templateName, String templateHTML, String templateType){
		edu.weber.resptherapy.charting.model.Formtemplate formtemplate = null;
		try {
			Session session = DatabaseConnector.getCurrentSession();
			Transaction tx = session.beginTransaction();

			Criteria cr = session.createCriteria(edu.weber.resptherapy.charting.model.Formtemplate.class);
			cr.add(Restrictions.eq("formTemplateName", templateName));

			formtemplate = (Formtemplate) cr.uniqueResult();
			if (formtemplate == null) {
				formtemplate = new Formtemplate();
				formtemplate.setFormTemplateVersionNum(0);
			}
			formtemplate.setFormTemplateType(templateType);
			formtemplate.setFormTemplateName(templateName);
			formtemplate.setFormTemplateHtml(templateHTML);
			formtemplate.setFormTemplateActive(true);
			formtemplate.setFormTemplateVersionNum(formtemplate.getFormTemplateVersionNum()+1);
			session.saveOrUpdate(formtemplate);
			tx.commit();
		} catch (Exception e){
			throw new RuntimeException(e.getMessage());
		} finally {
			DatabaseConnector.closeSession();
		}
		return true;		

	}
	
	//___________________________________________________________________________________________________________________
	
	public boolean updateTemplate(int templateID, String formTemplateName, String formTemplateHtml){

		edu.weber.resptherapy.charting.model.Formtemplate formtemplate = null;
		try {
			Session session = DatabaseConnector.getCurrentSession();
			Transaction tx = session.beginTransaction();

			Criteria cr = session.createCriteria(edu.weber.resptherapy.charting.model.Formtemplate.class);
			cr.add(Restrictions.eq("formTemplateName", formTemplateName));
//			List<edu.weber.resptherapy.charting.model.Formtemplate> results = cr.list();
//
//			if (results.size() < 1) {
//				throw new HibernateException("No UserForm found!");
//			}

			formtemplate = (Formtemplate) cr.uniqueResult();
			formtemplate.setFormTemplateName(formTemplateName);
			formtemplate.setFormTemplateHtml(formTemplateHtml);
			session.saveOrUpdate(formtemplate);
			tx.commit();
		} catch (Exception e){
			throw new RuntimeException(e.getMessage());
		} finally {
			DatabaseConnector.closeSession();
		}
		return true;
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
				throw new HibernateException("No templates found!");
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
	
	public String generatePDF( String userID, int FormtemplateID){


				//User u = new User(userID);
				String FormtemplateHTML = null;

				try {
					Session session = DatabaseConnector.getCurrentSession();
					Transaction tx = session.beginTransaction();
					Criteria cr = session.createCriteria(Userform.class);  // We are telling Hibernate to use the Userform class to load data from the DB Userform table

					//cr.add(Restrictions.eq("user", u)); // parameters for where clause
					cr.add(Restrictions.eq("userFormId", FormtemplateID));

					Userform userForm = (Userform) cr.uniqueResult(); // load data and cast the results to a Userform class
					if (userForm == null) {
						throw new HibernateException("User form not found!");
					}
					
					FormtemplateHTML = userForm.getFormTemplateHtml(); // FormtemplateHTML is in the object returned from DB (in the DB row)
					return FormtemplateHTML;

				} catch (HibernateException e){
					//log.error(e.getMessage(), e);
					throw new RuntimeException(e.getMessage());
					//return null;
				} finally {
					DatabaseConnector.closeSession();
				}

		}//end generatePDF
	
	//___________________________________________________________________________________________________________________
	
		public Formtemplate getTemplateToFillOut( String therapyId){
			Formtemplate formtemplate = null; 
			try {		
				Session session = DatabaseConnector.getCurrentSession();
				Transaction tx = session.beginTransaction();
				Criteria cr_userFormtemplate = session.createCriteria(Formtemplate.class);
				cr_userFormtemplate.add(Restrictions.eq("formTemplateId", Integer.valueOf(therapyId) )); //template Id
				
				formtemplate = (Formtemplate) cr_userFormtemplate.uniqueResult();
				if (formtemplate == null) {
					throw new HibernateException("No template found with id: "+therapyId);
				}

			} catch (HibernateException e){
				//log.error(e.getMessage(), e);
				throw new RuntimeException(e.getMessage());
			} finally {
				DatabaseConnector.closeSession();
			}	
			return formtemplate;

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

package edu.weber.resptherapy.charting;

//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;
//import org.apache.tomcat.jdbc.pool.DataSource;
//import org.apache.tomcat.dbcp.dbcp.BasicDataSource;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;

import edu.weber.resptherapy.charting.model.User;

/**
 * Class for handling connection to the database
 */
public class DatabaseConnector {

	private static Logger log = Logger.getLogger(DatabaseConnector.class);
	private static SessionFactory sessionFactory; 
	
	// new hibernate DATABASE methods
	static {
	      try {
				sessionFactory = new Configuration().configure("/hibernate.cfg.therapy.xml").buildSessionFactory();
			} catch (Throwable ex) {
				// Log the exception.  Name [jdbc/weberRespDB] Name is not bound in this Context. Unable to find [jdbc].
				log.error("Initial SessionFactory creation failed." + ex);
				throw new ExceptionInInitializerError(ex);
			}
	
	}
	
	protected static void closeSession() {
		Session s = getCurrentSession();
		try {
			if (s != null && s.isOpen()) {
				s.close();
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			log.error("CLOSE_EXCEPTION");
		}
	}
	protected static void rollbackTransaction( Transaction transaction ) {
		try {
			transaction.rollback();
		} catch ( Exception e ) {
			log.error(e.getMessage());
			log.error("ROLLBACK_EXCEPTION");
		}
	}	
	public static Session getCurrentSession() {
		Session s = sessionFactory.getCurrentSession();
		try {
			if (!s.isOpen()) {
				s = sessionFactory.openSession();
			}
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	
	//______________________________________________________________________________________
	/**
	 * Get (select) from database using session.get
	 */
	public static void hibernateExample1() {
		String useridparameter = "admin";
		User user = null;
		try {		
			Session session = DatabaseConnector.getCurrentSession();
			Transaction tx = session.beginTransaction();
			user = (edu.weber.resptherapy.charting.model.User) session.get(edu.weber.resptherapy.charting.model.User.class, useridparameter);
			System.out.println("{Example1}[GET] Successfully loaded user: "+user.getUserId()+" First name: "+user.getUserFirst()+" Last Name: "+user.getUserLast()+"!");
		} catch (HibernateException e){
			//log.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage());
		} finally {
			DatabaseConnector.closeSession();
		}
	}
	/**
	 * Get using Criteria (select) from database using session.get
	 */
	public static void hibernateExample2() {
		String useridparameter = "admin";
		String passwordparameter = "admin";
		User user = null;
		try {		
			Session session = DatabaseConnector.getCurrentSession();
			Transaction tx = session.beginTransaction();
			Criteria cr = session.createCriteria(edu.weber.resptherapy.charting.model.User.class);
			cr.add(Restrictions.eq("userId", useridparameter)); //userId
			cr.add(Restrictions.eq("password", passwordparameter)); //password
			List<User> results = cr.list(); // load from database
			if (results.size() < 1) {
				throw new HibernateException("No User found!");
			}
			user = results.get(0);
			System.out.println("{Example2}[CRITERIA] Successfully loaded user: "+user.getUserId()+" First name: "+user.getUserFirst()+" Last Name: "+user.getUserLast()+"!");
		} catch (HibernateException e){
			//log.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage());
		} finally {
			DatabaseConnector.closeSession();
		}
	}

	/**
	 * SELECT using 'Hibernate query' from database using session.get
	 */
	public static User hibernateExample3() {
		String hql = "from User u where u.userId = :id and u.password = :pw"; //  this is shorthand for 'select * from User as u ...'
		String useridparameter = "admin";
		String passwordparameter = "admin";
		User user = null;
		try {		
			Session session = DatabaseConnector.getCurrentSession();
			Transaction tx = session.beginTransaction();
			Query query =session.createQuery(hql);
			List<User> results = query 
					.setParameter("id",useridparameter)	// another way to set parameters
					.setParameter("pw",passwordparameter)
					.list(); // load from database
			if (results.size() < 1) {
				throw new HibernateException("No User found!");
			}
			user = results.get(0);
			System.out.println("{Example3}[HQL] Successfully loaded user: "+user.getUserId()+" First name: "+user.getUserFirst()+" Last Name: "+user.getUserLast()+"!");
		} catch (HibernateException e){
			//log.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage());
		} finally {
			DatabaseConnector.closeSession();
		}
		return user;
	}
	/**
	 * SELECT using 'SQL query' from database using session.get
	 */
	public static void hibernateExample4() {
		String sql = "select UserID,UserFirst,UserLast,"+
				"password,UserAdmin,UserPassReset,UserActive, UserEmail, UserYear "+
				"from therapy.User where UserID = :id and password = :pw"; //  SQL query. may need to define schema in hbm model
		String useridparameter = "admin";
		String passwordparameter = "admin";
		
		User user = null;
		try {		
			Session session = DatabaseConnector.getCurrentSession();
			Transaction tx = session.beginTransaction();
			Query query = session.createSQLQuery(sql).addEntity(User.class);
			query.setParameter("id",useridparameter);
			query.setParameter("pw",passwordparameter);
			user = (User) query.uniqueResult(); // we know we will get 1 result
			System.out.println("{Example4}[SQL] Successfully loaded user: "+user.getUserId()+" First name: "+user.getUserFirst()+" Last Name: "+user.getUserLast()+"!");
		} catch (HibernateException e){
			//log.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage());
		} finally {
			DatabaseConnector.closeSession();
		}
	}

	/**
	 * Insert new user using hibernate
	 * @return
	 */
	public static User hibernateInsertExample() {
		User u = new User(); // saving new user
		u.setUserId("00000000"); // w #
	    u.setUserFirst("John");
	    u.setUserLast("McLastName");
	    u.setPassword("password");
	    u.setUserAdmin(false);
	    u.setUserPassReset(false);
	    u.setUserActive(false);
	    u.setUserEmail("student@weber.edu");
	    u.setUserYear(new Date());
	    
		try {		
			Session session = DatabaseConnector.getCurrentSession();
			Transaction tx = session.beginTransaction();
			session.saveOrUpdate(u);
			tx.commit();  // remember to commit or rollback
			System.out.println("{InsertExample}[saveOrUpdate] Successfully inserted user: "+u.getUserId()+" First name: "+u.getUserFirst()+" Last Name: "+u.getUserLast()+"!");
		} catch (HibernateException e){
			//log.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage());
		} finally {
			DatabaseConnector.closeSession();
		}
		return u;
	}

	/**
	 * Update hibenate object and save to database.  Object should have been previously loaded by hibernate, not a new object
	 * @param u
	 * @return
	 */
	public static User hibernateUpdateExample(User u) {
		u.setUserLast("Hunter");
		try {		
			Session session = DatabaseConnector.getCurrentSession();
			Transaction tx = session.beginTransaction();
			session.saveOrUpdate(u);
			tx.commit();  // remember to commit or rollback
			System.out.println("{UpdateExample}[saveOrUpdate] Successfully updated user: "+u.getUserId()+" First name: "+u.getUserFirst()+" Last Name: "+u.getUserLast()+"!");
		} catch (HibernateException e){
			//log.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage());
		} finally {
			DatabaseConnector.closeSession();
		}
		return u;
	}
	/**
	 * Delete hibenate object and save to database.  Object should have been previously loaded by hibernate, not a new object
	 * @param u
	 * @return
	 */	public static User hibernateDeleteExample(User u) {
		try {		
			Session session = DatabaseConnector.getCurrentSession();
			Transaction tx = session.beginTransaction();
			session.delete(u);
			tx.commit();  // remember to commit or rollback
			System.out.println("{DeleteExample}[delete] Successfully deleted user: "+u.getUserId()+" First name: "+u.getUserFirst()+" Last Name: "+u.getUserLast()+"!");
		} catch (HibernateException e){
			//log.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage());
		} finally {
			DatabaseConnector.closeSession();
		}
		return u;
	}
	
	
	//open the database and connect  REMOVED, NOT NEEDED FOR HIBERNATE
//	public void connectDatabase() {
//        int count = 1;
// 		//ip address
////		String host = "127.2.85.130"; //PRODUCTION
////		String host = "127.0.0.1"; //TEST
//		//port
////		String port = "3306";
//		
//		String appName = "weberstate";
//		
//		//the database JDBC URL
////		String url = "jdbc:mysql://" + host + ":" + port + "/" + appName;
//		
//		//the database username
//		String username = "adminFxASVVU"; //adminFxASVVU
//		
//		//the database password
//		String password = "Q2lcMxlMgS_e"; //Q2lcMxlMgS_e
//		
//		return null; 
//	}
	

}

package edu.weber.resptherapy.charting;

import java.io.Serializable;
import java.util.Date;



/**
 * 
 * The base User class.</br>
 * There is a boolean data member flagging whether the User is an admin or not.
 *
 */
public class User implements Serializable{
	
	//****************************************************************//
	// DATA MEMEBERS
	//****************************************************************//
	
	private String 	_userId; // the User's W number
	
	private String 	_firstName;
	
	private String 	_lastName;
	
	private String _email;
	
	private boolean _isAdmin;
	
	private Date 	_year;
	
	private boolean _isActive;
	
	private boolean _needsResetPassword = true;
		
	//****************************************************************//
	// CONSTRUCTORS
	//****************************************************************//
	
	public User(){
		
	}
	
	public User(String userId, String firstName, String lastName, String email, int isAdmin, Date year, int isActive, int needsResetPassword){
		
		this._userId 	= userId;
		this._firstName = firstName;
		this._lastName 	= lastName;
		this._email = email;
		
		if (isAdmin == 0) {
			this._isAdmin = false;
		}
		else{
			this._isAdmin = true;
		}
		
		this._year = year;
		
		if (isActive == 0) {
			this._isActive = false;
		}
		else{
			this._isActive = true;
		}
		
		if (needsResetPassword == 0) {
			this._needsResetPassword = false;
		}
		else{
			this._needsResetPassword = true;
		}
	}

	//****************************************************************//
	// GETTERS AND SETTERS
	//****************************************************************//

	//___________________________________________________________ GET user ID
	
	public String getUserId() {
		return _userId;
	}

	//---------------------------------------------- SET user ID
	
	public void setUserId(String wNumber) {
		this._userId = wNumber;
	}

	//___________________________________________________________ GET first name
	
	public String getFirstName() {
		return _firstName;
	}

	//---------------------------------------------- SET first name
	
	public void setFirstName(String firstName) {
		this._firstName = firstName;
	}
	
	//___________________________________________________________ GET last name
	
	public String getLastName() {
		return _lastName;
	}

	//---------------------------------------------- SET last name
	
	public void setLastName(String lastName) {
		this._lastName = lastName;
	}
	
	//---------------------------------------------- GET full name

	public String getFullName(){
		
		return getFirstName() + " " + getLastName();
	}
	
	//___________________________________________________________ GET email
	
	public String getEmail(){
		return _email;
	}
	
	//---------------------------------------------- SET email
	
	public void setEmail(String emailAddress){
		this._email = emailAddress;
	}

	//___________________________________________________________ is Admin?
	
	public boolean isAdmin() {
		return _isAdmin;
	}
	
	//___________________________________________________________ GET year
	
	public Date getYear(){
		
		return _year;
	}
	
	//___________________________________________________________ is Active?
	
	public boolean isActive(){
		
		return _isActive;
	}
	
	//___________________________________________________________ needs reset password?
	
	public boolean needsResetPassword(){
		return _needsResetPassword;
	}
	
	//---------------------------------------------- SET needs password reset
	
	public void setNeedsResetPassword(boolean needsPassReset){
		
		this._needsResetPassword = needsPassReset;
	}

	//****************************************************************//
	// METHODS
	//****************************************************************//


}
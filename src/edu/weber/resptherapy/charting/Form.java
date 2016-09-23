package edu.weber.resptherapy.charting;

import java.util.Date;

public class Form {

	//****************************************************************//
	// DATA MEMEBERS
	//****************************************************************//
	
	private int id;
	
	private String name;
	
	private String theFormHtml;
	
	private String userId;
	
	private Date lastEdited;

	//****************************************************************//
	// CONSTRUCTORS
	//****************************************************************//
	
	public Form(){ /*empty default constructor*/ }
	
	//----------------------------------------------
	
	public Form(int id, String name, String theFormHtml, String userId, Date lastEdited){
		
		this.id = id;
		this.name = name;
		this.theFormHtml = theFormHtml;
		this.userId = userId;
		this.lastEdited = lastEdited;
	}
	
	//****************************************************************//
	// GETTERS AND SETTERS
	//****************************************************************//

	public int getId() {
		return id;
	}

	//----------------------------------------------
	
	public void setId(int id) {
		this.id = id;
	}

	//___________________________________________________________
	
	public String getName() {
		return name;
	}
	
	//----------------------------------------------
	
	public void setName(String name) {
		this.name = name;
	}

	//___________________________________________________________
	
	public String getTheFormHtml() {
		return theFormHtml;
	}

	//----------------------------------------------
	
	public void setTheFormHtml(String theFormHtml) {
		this.theFormHtml = theFormHtml;
	}
	
	//___________________________________________________________
	
	public String getUserId() {
		return userId;
	}

	//----------------------------------------------
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	//___________________________________________________________
	
	public Date getLastEdited() {
		return lastEdited;
	}

	//----------------------------------------------
	
	public void setLastEdited(Date lastEdited) {
		this.lastEdited = lastEdited;
	}
		
	//****************************************************************//
	// METHODS
	//****************************************************************//

}

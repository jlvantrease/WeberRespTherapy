package edu.weber.resptherapy.charting;

import java.io.Serializable;

/**
 * 
 * An administrator can create new Therapy Templates, but a student
 *
 */
public class TherapyTemplate implements Serializable {

	//****************************************************************//
	// DATA MEMEBERS
	//****************************************************************//
	
	private String name;
	
	private boolean isActive;
	
	private String theHtml;
	
	private int id;
	
	private String type;
	
	private int versionNum;

	//****************************************************************//
	// CONSTRUCTORS
	//****************************************************************//
	
	public TherapyTemplate(){ /*empty default constructor*/ }
	
	//----------------------------------------------
	
	public TherapyTemplate(String name, boolean isActive, String theHtml,
			int id, String type, int versionNum) {
		
		this.name = name;
		this.isActive = isActive;
		this.theHtml = theHtml;
		this.id = id;
		this.type = type;
		this.versionNum = versionNum;
	}

	//****************************************************************//
	// GETTERS AND SETTERS
	//****************************************************************//

	public String getName(){
		
		return name;
	}
	
	//----------------------------------------------
	
	public void setName(String name){
		
		this.name = name;
	}
	
	//___________________________________________________________
	
	public boolean isActive() {
		return isActive;
	}

	//----------------------------------------------
	
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	//___________________________________________________________
	
	public String getTheHtml() {
		return theHtml;
	}

	//----------------------------------------------
	
	public void setTheHtml(String theHtml) {
		this.theHtml = theHtml;
	}

	//___________________________________________________________
	
	public int getId() {
		return id;
	}

	//----------------------------------------------
	
	public void setId(int id) {
		this.id = id;
	}

	//___________________________________________________________
	
	public String getType() {
		return type;
	}

	//----------------------------------------------
	
	public void setType(String type) {
		this.type = type;
	}

	//___________________________________________________________
	
	public int getVersionNum() {
		return versionNum;
	}

	//----------------------------------------------
	
	public void setVersionNum(int versionNum) {
		this.versionNum = versionNum;
	}

	//****************************************************************//
	// METHODS
	//****************************************************************//

}

package edu.weber.resptherapy.charting;

import java.util.ArrayList;

public class Assignment {

	//****************************************************************//
	// DATA MEMEBERS
	//****************************************************************//

	private ArrayList<TherapyTemplate> _listOfTherapies = new ArrayList<TherapyTemplate>();

	//****************************************************************//
	// CONSTRUCTORS
	//****************************************************************//
	

	//****************************************************************//
	// GETTERS AND SETTERS
	//****************************************************************//
	
	public ArrayList<TherapyTemplate> getListOfTherapies(){
		
		return _listOfTherapies;
	}

	//****************************************************************//
	// METHODS
	//****************************************************************//

	public void addNewTherapy(TherapyTemplate therapy){
		
		this._listOfTherapies.add(therapy);
	}

}

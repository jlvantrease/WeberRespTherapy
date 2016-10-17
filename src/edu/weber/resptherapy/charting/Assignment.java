package edu.weber.resptherapy.charting;

import java.util.ArrayList;

import edu.weber.resptherapy.charting.model.Formtemplate;

public class Assignment {

	//****************************************************************//
	// DATA MEMEBERS
	//****************************************************************//

	private ArrayList<Formtemplate> _listOfTherapies = new ArrayList<Formtemplate>();

	//****************************************************************//
	// CONSTRUCTORS
	//****************************************************************//
	

	//****************************************************************//
	// GETTERS AND SETTERS
	//****************************************************************//
	
	public ArrayList<Formtemplate> getListOfTherapies(){
		
		return _listOfTherapies;
	}

	//****************************************************************//
	// METHODS
	//****************************************************************//

	public void addNewTherapy(Formtemplate therapy){
		
		this._listOfTherapies.add(therapy);
	}

}

package edu.weber.resptherapy.charting;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import edu.weber.resptherapy.charting.User;
 
public class Dashboard {

	public Map<String, String> leftPaneList = new HashMap<String, String>();
	
	public String updateLeftLinks(Map<String, String> mapOfItems, String typeOfItems, boolean isAdmin) { 
		
		leftPaneList = mapOfItems;
		String newHtml = "";
		Iterator listIterator = leftPaneList.entrySet().iterator(); 
		ArrayList<String> listOfLeftItems = new ArrayList<String>();
			int numberOfListItems = 0;
			while (listIterator.hasNext()) {
				numberOfListItems++;
				Map.Entry pair = (Map.Entry)listIterator.next();
				if("users".equals(typeOfItems)) {
					listOfLeftItems.add("<li class='leftLinkItem' id='link_" + (Integer.toString(numberOfListItems)) + "'> " + pair.getValue() + "</li>"); 
					listOfLeftItems.add("<li class='editBelowLink' id='link_editLine_" + (Integer.toString(numberOfListItems)) + "'>&nbsp;&nbsp;&nbsp;<a class='editLink'>Edit</a></li>"); 
					listOfLeftItems.add("<li class='hiddenWNumber' id='link_wNumberLine_" + (Integer.toString(numberOfListItems)) + "' >" + pair.getKey() + "</li>");
				}
				else if("therapies".equals(typeOfItems)) {
					if (isAdmin) {
						listOfLeftItems.add("<li class='leftLinkItem' id='link_" + (Integer.toString(numberOfListItems)) + "'> " + pair.getValue() + "</li>"); 
						listOfLeftItems.add("<li class='editBelowTherapyLink' id='link_editTherapyLine_" + (Integer.toString(numberOfListItems)) + "'>&nbsp;&nbsp;&nbsp;<a class='editTherapyLink'>Edit</a></li>"); 
						listOfLeftItems.add("<li class='hiddenTherapyId' id='link_therapyIdLine_" + (Integer.toString(numberOfListItems)) + "' >" + pair.getKey() + "</li>");
						}
					else {
						listOfLeftItems.add("<li class='leftLinkItem' id='link_" + (Integer.toString(numberOfListItems)) + "'> " + pair.getValue() + "</li>"); 
						listOfLeftItems.add("<li class='addToTherapyLinkBelow' id='link_addToTherapyLine_" + (Integer.toString(numberOfListItems)) + "'>&nbsp;&nbsp;&nbsp;<a class='addToTherapyLink'>Add To Form</a></li>"); 
						listOfLeftItems.add("<li class='hiddenTherapyId' id='link_therapyIdLine_" + (Integer.toString(numberOfListItems)) + "' >" + pair.getKey() + "</li>");
					}
				}
				else if("forms".equals(typeOfItems)) {
					listOfLeftItems.add("<li class='leftLinkItem' id='link_" + (Integer.toString(numberOfListItems)) + "'> " + pair.getValue() + "</li>"); 
					listOfLeftItems.add("<li class='addToFormLinkBelow' id='link_addToTherapyLine_" + (Integer.toString(numberOfListItems)) + "'>&nbsp;&nbsp;&nbsp;<a class='editFormLink'>Edit Form</a></li>"); 
					listOfLeftItems.add("<li class='hiddenFormId' id='link_formIdLine_" + (Integer.toString(numberOfListItems)) + "' >" + pair.getKey() + "</li>");
				}
			}
			
			newHtml = "\"<ul id='leftListItems'>";
			
			Iterator<String> liElementIterator = listOfLeftItems.iterator(); 
			while (liElementIterator.hasNext()) {
				String liHTML = liElementIterator.next();
				newHtml = newHtml + liHTML;
			}
			newHtml = newHtml + "</ul>\"";
			
			return newHtml;
    }
	
	public Map<String, User> getCurrentFirstYears(Map<String, User> allUsers) {
	    Map<String, User> currentFirstYears = new HashMap<String, User>();
	    
		for(String key : allUsers.keySet()) {
			
			//Skip inactive users
			if(!allUsers.get(key).isActive()) {
				continue;
			}

			//Skip admins
			if(allUsers.get(key).isAdmin()) {
				continue;
			}
			
			//If the user doesn't have a year value, skip them
			if(allUsers.get(key).getYear() == null || allUsers.get(key).getYear().toString().length() < 1) {
				continue;
			}
			
			String userYear = allUsers.get(key).getYear().toString();
			userYear = userYear.substring(0, 4);
			
			//If the user's year is the current year and it is after June then they are a current first year
			if(getCurrentYear().equals(userYear)) {
				if(Integer.parseInt(getCurrentMonth()) >= 5 ) {
					currentFirstYears.put(key, allUsers.get(key));
				}
			}
			
			//If the user's year is last year and it is before June then they are a current first year
			else if(getLastYear().equals(userYear)) {
				if(Integer.parseInt(getCurrentMonth()) < 5 ) {
					currentFirstYears.put(key, allUsers.get(key));
				}
			}
		}
		
		return currentFirstYears;
	}
	
	
	public Map<String, User> getCurrentSecondYears(Map<String, User> allUsers) {
	    Map<String, User> currentSecondYears = new HashMap<String, User>();
	    
		for(String key : allUsers.keySet()) {
			
			//Skip inactive users
			if(!allUsers.get(key).isActive()) {
				continue;
			}
			
			//Skip admins
			if(allUsers.get(key).isAdmin()) {
				continue;
			}
			
			//If the user doesn't have a year value, skip them
			if(allUsers.get(key).getYear() == null || allUsers.get(key).getYear().toString().length() < 1) {
				continue;
			}
			
			String userYear = allUsers.get(key).getYear().toString();
			userYear = userYear.substring(0, 4);
			
			//If the user's year is last year and it is after June then they are a current second year
			if(getLastYear().equals(userYear)) {
				if(Integer.parseInt(getCurrentMonth()) >= 5 ) {
					currentSecondYears.put(key, allUsers.get(key));
				}
			}
			
			//If the user's year is two years ago and it is before June then they are a current second year
			else if(getTwoYearsAgo().equals(userYear)) {
				if(Integer.parseInt(getCurrentMonth()) < 5 ) {
					currentSecondYears.put(key, allUsers.get(key));
				}
			}
		}
		
		return currentSecondYears;
	}
	
	public Map<String, User> getAdmins(Map<String, User> allUsers) {
	    Map<String, User> admins = new HashMap<String, User>();
	    
		for(String key : allUsers.keySet()) {
			
			//Skip inactive users
			if(!allUsers.get(key).isActive()) {
				continue;
			}
			
			//Only get admin users
			if(allUsers.get(key).isAdmin()) {
				admins.put(key, allUsers.get(key));
			}
		}
		
		return admins;
	}
	
	public Map<String, User> getInactiveUsers(Map<String, User> allUsers) {
	    Map<String, User> inactiveUsers = new HashMap<String, User>();
	    
		for(String key : allUsers.keySet()) {
			
			//Addinactive users
			if(!allUsers.get(key).isActive()) {
				inactiveUsers.put(key, allUsers.get(key));
			}
		}
		
		return inactiveUsers;
	}
	
	public static String getCurrentYear() {
		Calendar calendar = Calendar.getInstance();
		int currentYear = calendar.get(Calendar.YEAR);
		return currentYear + "";
	}
	
	public static String getLastYear() {
		Calendar calendar = Calendar.getInstance();
		int currentYear = calendar.get(Calendar.YEAR);
		return (currentYear - 1) + "";
	}
	
	public static String getTwoYearsAgo() {
		Calendar calendar = Calendar.getInstance();
		int currentYear = calendar.get(Calendar.YEAR);
		return (currentYear - 2) + "";
	}
	
	public static String getCurrentMonth() {
		Calendar calendar = Calendar.getInstance();
		int currentMonth = calendar.get(Calendar.MONTH);
		return currentMonth + "";
	}
	
	public Map<Integer, TherapyTemplate> getBeginningTherapies(Map<Integer, TherapyTemplate> allTemplates) {
		Map<Integer, TherapyTemplate> beginningTherapies = new HashMap<Integer, TherapyTemplate>();
		
		for(Integer key : allTemplates.keySet())
		{
			if("beginner".equals(allTemplates.get(key).getType()))
			{
				if(allTemplates.get(key).isActive() == true)
				beginningTherapies.put(key, allTemplates.get(key));
			}
		}
		return beginningTherapies;
	}
	
	public Map<Integer, TherapyTemplate> getAdvancedTherapies(Map<Integer, TherapyTemplate> allTemplates) {
		Map<Integer, TherapyTemplate> advancedTherapies = new HashMap<Integer, TherapyTemplate>();
		
		for(Integer key : allTemplates.keySet())
		{
			if("advanced".equals(allTemplates.get(key).getType()))
			{
				if(allTemplates.get(key).isActive() == true)
				advancedTherapies.put(key, allTemplates.get(key));
			}
		}
		return advancedTherapies;
	}
	
	public Map<Integer, TherapyTemplate> getTherapyDrafts(Map<Integer, TherapyTemplate> allTemplates) {
		Map<Integer, TherapyTemplate> therapyDrafts = new HashMap<Integer, TherapyTemplate>();
		
		for(Integer key : allTemplates.keySet())
		{
			if("draft".equals(allTemplates.get(key).getType()))
			{
				therapyDrafts.put(key, allTemplates.get(key));
			}
		}
		return therapyDrafts;
	}

}

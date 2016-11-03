package edu.weber.resptherapy.charting.model;
// Generated Oct 15, 2016 12:14:38 AM by Hibernate Tools 4.3.1


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * User generated by hbm2java
 */
public class User implements java.io.Serializable {


     private String userId;
     private String userFirst;
     private String userLast;
     private String password;
     private boolean userAdmin;
     private boolean userPassReset;
     private boolean userActive;
     private String userEmail;
     private Date userYear;
     private Set userforms = new HashSet(0);

    public User() {
    }

    public User(String userId) {
        this.userId = userId;
   }

	
    public User(String userId, String userFirst, String userLast, String password, boolean userAdmin, boolean userPassReset, boolean userActive, Date userYear) {
        this.userId = userId;
        this.userFirst = userFirst;
        this.userLast = userLast;
        this.password = password;
        this.userAdmin = userAdmin;
        this.userPassReset = userPassReset;
        this.userActive = userActive;
        this.userYear = userYear;
    }
    public User(String userId, String userFirst, String userLast, String password, boolean userAdmin, boolean userPassReset, boolean userActive, String userEmail, Date userYear, Set userforms) {
       this.userId = userId;
       this.userFirst = userFirst;
       this.userLast = userLast;
       this.password = password;
       this.userAdmin = userAdmin;
       this.userPassReset = userPassReset;
       this.userActive = userActive;
       this.userEmail = userEmail;
       this.userYear = userYear;
       this.userforms = userforms;
    }
   
    public String getUserId() {
        return this.userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUserFirst() {
        return this.userFirst;
    }
    
    public void setUserFirst(String userFirst) {
        this.userFirst = userFirst;
    }
    public String getUserLast() {
        return this.userLast;
    }
    
    public void setUserLast(String userLast) {
        this.userLast = userLast;
    }
    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    public boolean isUserAdmin() {
        return this.userAdmin;
    }
    
    public void setUserAdmin(boolean userAdmin) {
        this.userAdmin = userAdmin;
    }
    public boolean isUserPassReset() {
        return this.userPassReset;
    }
    
    public void setUserPassReset(boolean userPassReset) {
        this.userPassReset = userPassReset;
    }
    public boolean isUserActive() {
        return this.userActive;
    }
    
    public void setUserActive(boolean userActive) {
        this.userActive = userActive;
    }
    public String getUserEmail() {
        return this.userEmail;
    }
    
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    public Date getUserYear() {
        return this.userYear;
    }
    
    public void setUserYear(Date userYear) {
        this.userYear = userYear;
    }
    public Set getUserforms() {
        return this.userforms;
    }
    
    public void setUserforms(Set userforms) {
        this.userforms = userforms;
    }




}



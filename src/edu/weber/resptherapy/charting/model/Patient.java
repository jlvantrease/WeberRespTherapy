package edu.weber.resptherapy.charting.model;

import java.util.Date;

public class Patient implements java.io.Serializable {
  
  private Integer patientId;
  private String firstName;
  private String lastName;
  private String middleName;
  private Date dateOfBirth;
  private String gender;
  private String phoneNumber;

  public Patient(String firstName, String lastName, String middleName, Date dateOfBirth, String gender, String phoneNumber) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.middleName = middleName;
    this.dateOfBirth = dateOfBirth;
    this.gender = gender;
    this.phoneNumber = phoneNumber;
  }

  public Integer getPatientId() {
    return patientId;
  }

  public void setPatientId(Integer patientId) {
    this.patientId = patientId;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getMiddleName() {
    return middleName;
  }

  public void setMiddleName(String middleName) {
    this.middleName = middleName;
  }

  public Date getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(Date dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }
}

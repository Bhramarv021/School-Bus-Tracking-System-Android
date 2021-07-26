package com.example.schoolbustrackingsystem.Model;

public class UsersModel {

    String userType;
    String schoolId;
    String emailId;

    public UsersModel() {
    }

    public UsersModel(String emailId, String userType, String schoolId){
        this.emailId = emailId;
        this.userType = userType;
        this.schoolId = schoolId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
}

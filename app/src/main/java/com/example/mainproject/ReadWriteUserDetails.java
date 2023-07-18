package com.example.mainproject;

public class ReadWriteUserDetails {
    public String fullName,email,doB,gender,mobile;
    public ReadWriteUserDetails(){};
    public ReadWriteUserDetails(String fullName,String email, String doB ,String gender, String mobile){
        this.fullName = fullName;
        this.email=email;
        this.doB = doB;
        this.gender = gender;
        this.mobile = mobile;


    }

    public String email() {
        return email;
    }


  /*  public String getFullName() {return Fullname;}

    public void setFullName(String fullName) {
        this.Fullname = fullName;
    }

    public String getDoB() {
        return DoB;
    }

    public void setDoB(String doB) {
        this.DoB = doB;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        this.Gender = gender;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        this.Mobile = mobile;
    }*/
}


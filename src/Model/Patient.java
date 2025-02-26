package Model;

import Model.GeneralObject;

public class Patient extends GeneralObject {
    // Patient Variables
    private String patUID;
    private String firstName;
    private String lastName;
    private String middleName;
    private String birthday;
    private String gender;
    private String address;
    private String phoneNo;
    private String nationalIdNo;

    public Patient(
        String patUID,
        String firstName,
        String lastName,
        String middleName,
        String birthday,
        String gender,
        String address,
        String phoneNo,
        String nationalIdNo,
        char delIndicator,
        String delReason
    ) {
        super(delIndicator, delReason);
        this.patUID = patUID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.birthday = birthday;
        this.gender = gender;
        this.address = address;
        this.phoneNo = phoneNo;
        this.nationalIdNo = nationalIdNo;
    }

    public String getPatUID() {
        return patUID;
    }

    public void setPatUID(String patUID) {
        this.patUID = patUID;
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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getNationalIdNo() {
        return nationalIdNo;
    }

    public void setNationalIdNo(String nationalIdNo) {
        this.nationalIdNo = nationalIdNo;
    }

}

public class Patient extends GeneralObject {
    // Patient Variables
    private String patUID;
    private String firstName;
    private String lastName;
    private String middleName;
    private long birthday;
    private char gender;
    private String address;
    private long phoneNo;
    private long nationalIdNo;

    public Patient(
        String patUID,
        String firstName,
        String lastName,
        String middleName,
        long birthday,
        char gender,
        String address,
        long phoneNo,
        long nationalIdNo,
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

    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(long phoneNo) {
        this.phoneNo = phoneNo;
    }

    public long getNationalIdNo() {
        return nationalIdNo;
    }

    public void setNationalIdNo(long nationalIdNo) {
        this.nationalIdNo = nationalIdNo;
    }

}

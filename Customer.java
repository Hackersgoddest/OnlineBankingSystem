package classes.project;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Customer extends ValidationMethods {
    // Customer data declaration
    private String fullName;
    private String contact;
    private String email;
    private String address;
    private String dateOfBirth;
    private String password;
    private int age;
    private String encryptedPassword;

    /*
    securityQueForPassReset is an array containing questions which a new customer opening an account will be allowed to
    choose any one of them to answer, which will later be used to grant a customer the opportunity to reset his or her
    password if he or she has forgotten.
     */
    // constant array of string data type
    private final String[] securityQueForPassReset = {"What is your favorite pet name", "What is your favorite food"};
    private int securityOption;

    //initializing customer data using constructor
    public Customer() {
        this.fullName = "";
        this.contact = "";
        this.email = "";
        this.address = "";
        this.password = "";
        this.encryptedPassword = "";
        this.securityOption = 0;
        this.age = 0;
    }


    // Customer methods declaration and definitions
    // Mutators
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPassword(String password) {
        this.password = password;
        encryptedPassword = new String(encryptPassword(this.password.getBytes())); // encrypts the password set
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    // method for checking whether a customer's age is eligible for opening account
    public boolean isEligibleAge(String dob) {
        String[] array = dob.split("-");
        LocalDate myDate = LocalDate.now();
        DateTimeFormatter myFormatDate = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String date = myDate.format(myFormatDate);
        String[] dateArray = date.split("-");
        age = (int)(((double) (Integer.parseInt(dateArray[0]) - Integer.parseInt(array[0])) / 365) +
                ((double) (Integer.parseInt(dateArray[1]) - Integer.parseInt(array[1])) / 12) + ((Integer.parseInt(dateArray[2]) - Integer.parseInt(array[2]))));
        return (age >= 18);
    }

    public void setSecurityQueForPassReset(String value) {
        this.securityQueForPassReset[getSecurityOption()] = value;
    }

    public void setSecurityOption(int securityOption) {
        this.securityOption = securityOption;
    }

    //Accessors
    public String getFullName() {
        return fullName.toUpperCase();
    }

    public String getContact() {
        return contact;
    }

    public String getEmail() {
        return email.toLowerCase();
    }

    public String getAddress() {
        return address.toUpperCase();
    }

    public String getPassword() {
        return password;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public int getAge() {
        return age;
    }

    // method for encrypting customer's password
    public byte[] encryptPassword(byte[] data) {
        byte[] encryptedPassword = new byte[data.length];
        for(int index = 0; index < data.length; index++) {
            encryptedPassword[index] = (byte)((index % 2 == 0)? data[index] + 1 : data[index] - 1);
        }
        return encryptedPassword;
    }

    // method for decrypting customer's password
    public byte[] decryptPassword(byte[] data) {
        byte[] decryptedPassword = new byte[data.length];
        for(int index = 0; index < data.length; index++) {
            decryptedPassword[index] = (byte)((index % 2 == 0)? data[index] - 1 : data[index] + 1);
        }
        return decryptedPassword;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public int getSecurityOption() {
        return securityOption;
    }

    public String getSecurityQueForPassReset() {
        return securityQueForPassReset[getSecurityOption()];
    }

    // method for encrypting and returning security question and answer
    public String getEncryptedSecurityQueForPassReset() {
        return new String(encryptPassword(securityQueForPassReset[getSecurityOption()].getBytes()));
    }

    public String[] getAllSecurityQuestions() {
        return  securityQueForPassReset;
    }
}

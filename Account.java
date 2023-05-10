package classes.project;

import java.util.Scanner;

public class Account extends Customer {

    //Account data declaration
    private String pin;
    private String accountType;
    private double accountBalance;
    private String accountNumber;
    private String nextOfKin;
    private String nextOfKinContact;
    private String encryptedPin;


    // Using Constructor to initialize the fields
    public Account() {
        this.accountBalance = 0;
        this.accountType = "";
        this.accountNumber = "";
        this.nextOfKin = "";
        this.nextOfKinContact = "";
        this.pin = "";
        this.encryptedPin = "";
    }


    //Account methods declaration and definition
    //Mutators | Setters


    public void setPin(String pin) {
        this.pin = pin;
        encryptedPin = new String(encryptPin(this.pin.getBytes())); // encrypts the pin set
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public void setNextOfKin(String nextOfKin) {
        this.nextOfKin = nextOfKin;
    }

    public void setNextOfKinContact(String nextOfKinContact) {
        this.nextOfKinContact = nextOfKinContact;
    }


    //Accessor | Getters

    public String getPin() {
        return pin;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public String getAccountType() {
        return accountType.toUpperCase();
    }

    public String getNextOfKin() {
        return nextOfKin.toUpperCase();
    }

    public String getNextOfKinContact() {
        return nextOfKinContact;
    }
    public String getEncryptedPin() {
        return encryptedPin;
    }


    // method for encrypting the account's pin
    public byte[] encryptPin(byte[] data) {
        byte[] encryptedPassword = new byte[data.length];
        for (int index = 0; index < data.length; index++) {
            encryptedPassword[index] = (byte) ((index % 2 == 0) ? data[index] + 2 :
                    (index % 3 == 0) ? data[index] - 3 : data[index] + 1);
        }
        return encryptedPassword;
    }

    // method for decrypting the account's pin
    public byte[] decryptPin(byte[] data) {
        byte[] decryptedPassword = new byte[data.length];
        for (int index = 0; index < data.length; index++) {
            decryptedPassword[index] = (byte) ((index % 2 == 0) ? data[index] - 2 :
                    (index % 3 == 0) ? data[index] + 3 : data[index] - 1);
        }
        return decryptedPassword;
    }

    public void displaySuccessMessage(){
        Scanner keyboard = new Scanner(System.in);
        System.out.print("""
                Congratulations
                You have successfully created your account
                Press any key to continue...""");
        keyboard.nextLine();
    }

}

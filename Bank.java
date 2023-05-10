package classes.project;

import javax.swing.*;
import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Bank extends Account {


    // method for generating random numbers of 10 digits
    public String accountNumGen() {
        Random random = new Random();
        long n = (long) (1000000000L + random.nextFloat() * 9000000000L);
        return Long.toString(n);
    }

    // method for displaying customer information after he or she is done opening his or her account
    public void displayAccountInfo() {
        JOptionPane.showMessageDialog(null, String.format("""
                        Congratulation, your account has successfully been created
                        Account Number: %s
                        Full Name : %s
                        Age: %s
                        Date of Birth: %s
                        Contact : %s
                        Email : %s
                        City : %s
                        Password: %s
                        Pin : %s
                        Account Type: %s
                        Next of Kin: %s
                        Next of Kin contact: %s
                        Security Question
                        %s
                                        
                        NB: Please do not share your account number, password and pin with anyone. Thank you.
                        Press Enter to take you back to the main menu...
                        """, getAccountNumber(), getFullName(), getAge(), getDateOfBirth(), getContact(), getEmail(), getAddress(), getPassword(),
                getPin(), getAccountType(), getNextOfKin(), getNextOfKinContact(), getSecurityQueForPassReset()));
    }

    @Override
    public void displaySuccessMessage() {
        JOptionPane.showMessageDialog(null, "Congratulation\nYou have successfully created your account\nPress enter to continue...");
    }

    // method for saving customer information into a file after he or she is done opening the account
    public boolean saveAccountInfo() {
        boolean accountNumAlreadyExist;
        boolean contactExist = false;
        int contactIndex = 0;

        // using LocalDate and DateTimeFormater to format how the date should be display
        LocalDate myDate = LocalDate.now();
        DateTimeFormatter myFormatDate = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String dateAccountCreated = myDate.format(myFormatDate);
        DateTimeFormatter myDateFormat = DateTimeFormatter.ofPattern("E, MMM-dd-yy 'at' h:mm a");
        LocalDateTime myDateTime = LocalDateTime.now();
        String date = myDateTime.format(myDateFormat);

        /*
            this below do while loop assigns 10 random digits to a new customer as his or her acccount number and
            then tries to open the customer account file to check if that random digits has been already assigns to someone
            as his or her account number, if yes then a new random 10 digits is generated for the new customer, so this code
            ensures that every customer has a unique account number.
             */
        do {
            setAccountNumber(accountNumGen()); // assigns the random digits generated to the random number
            try {
                Scanner readFromFile = new Scanner(new File(getCustomerFilePath(getAccountNumber())));
                readFromFile.close();
                accountNumAlreadyExist = true;
            } catch (FileNotFoundException e) {
                accountNumAlreadyExist = false;
            }
        } while (accountNumAlreadyExist);

        // this outer try and catch block saves customer information into a text file and use the account number as the file name
        try {
            // this two lines of codes ensures that a text file is created for the new customer
            PrintWriter writeToFile = new PrintWriter(new FileWriter(getCustomerFilePath(getAccountNumber())));
            writeToFile.printf("""
                            Account Number: %s
                            Account Type: %s
                            Full Name: %s
                            Age: %s
                            Contact: %s
                            Email: %s
                            Date Of Birth: %s
                            Address: %s
                            Next of Kin: %s
                            Next of Kin contact: %s
                            Account Balance: $%.2f
                            Password: %s
                            Pin: %s
                            %s
                            Date Created: %s""", getAccountNumber(), getAccountType(), getFullName(), getAge(), getContact(),
                    getEmail(), getDateOfBirth(), getAddress(), getNextOfKin(), getNextOfKinContact(),
                    getAccountBalance(), getEncryptedPassword(), getEncryptedPin(), getEncryptedSecurityQueForPassReset(), dateAccountCreated);
            if (getAccountType().equalsIgnoreCase("savings")) writeToFile.printf("\nInterest Count: %d", 0);
            writeToFile.close();

            /*
            create another text file using the account number and the account type as the file name to
            keep records of all the transactions that will be associated with that account
             */
            PrintWriter writeToTranFile = new PrintWriter(getCustomerTransactionFilePath(getAccountNumber(), getAccountType().toLowerCase()));
            writeToTranFile.printf("""
                    TRANSACTION SUMMARY
                    You deposited an amount of $%.2f into your account on %s""", getAccountBalance(), date);
            writeToTranFile.close();
            /*
            there is a text file manually created in this program called auth which saves contacts that have been used
            to open account and their respective account type, and this text file is used to ensure that a contact have only two
            accounts of different type, so this try and catch block just keep updating the auth text file with contacts and their
            respective account type.
             */
            try {
                ArrayList<String> authDetails = new ArrayList<>();
                Scanner readFromFile = new Scanner(new File(getAuthorizationFilePath()));
                int count = 0;
                while (readFromFile.hasNext()) {
                    String info = readFromFile.nextLine();
                    String[] userInfo = info.split(" ");
                    if (userInfo[0].equals(getContact())) {
                        contactExist = true;
                        contactIndex = count;
                    }
                    authDetails.add(info);
                    count++;
                }
                readFromFile.close();
                if (contactExist) {
                    PrintWriter writeToAuthFile = new PrintWriter(getAuthorizationFilePath());
                    String userUpdate = authDetails.get(contactIndex) + " " + getAccountType();
                    authDetails.set(contactIndex, userUpdate);
                    for (String authDetail : authDetails) {
                        writeToAuthFile.println(authDetail);
                    }
                    writeToAuthFile.close();
                } else {
                    PrintWriter appendToAuthFile = new PrintWriter(new FileWriter(getAuthorizationFilePath(), true));
                    String userInfo = getContact() + " " + getAccountType();
                    appendToAuthFile.println(userInfo);
                    appendToAuthFile.close();
                }
            } catch (FileNotFoundException e) {
                System.out.println("File not found");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error, Cannot open file to save customer information!!!");
            return false;
        }
        return true;
    }
}

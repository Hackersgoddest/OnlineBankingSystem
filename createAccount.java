package classes.project;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class createAccount extends Bank{
    // method for handling opening of new account
    public createAccount() {
        Scanner keyboard = new Scanner(System.in);
        String username = nameValidation(keyboard, "name");
        if (!(username.equalsIgnoreCase("b"))) {
            setFullName(username);  // assigning username to Customer instance field called full name.
            String contact = contactValidation(keyboard, "contact");
            // checking whether the contact the customer is using already exist in the system
            boolean contactExist = false;
            String[] userInfo = new String[3];
            try {
                File authFile = new File(getAuthorizationFilePath());
                Scanner readFromFile = new Scanner(authFile);
                while (readFromFile.hasNext() && !contactExist) {
                    userInfo = readFromFile.nextLine().split(" ");
                    if (userInfo[0].equals(contact)) {
                        contactExist = true;
                    }
                }
                readFromFile.close();
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(null, "Sorry authentication was unsuccessful try next time");
            }

            /*
            this if block allows the customer to continue with the opening of the account, if the contact the user is using
            does not exist in the system at all, or if it exists in the system, it only has one account type created with that contact
             */
            if (!contactExist || userInfo.length == 2) {
                setContact(contact);  // assigning contact to Customer instance field call contact
                setEmail(emailValidation(keyboard)); // Assigning email to Customer instance field call email

                // Asking and validating customer date of birth
                StringBuilder dob;
                boolean isDobValid;
                boolean eligibleAge;
                do {
                    eligibleAge = true;
                    System.out.print("Enter your date of birth in this format(dd-mm-yyyy): ");
                    dob = new StringBuilder(keyboard.nextLine().trim());
                    isDobValid = isValidDOB(dob.toString());
                    if(isDobValid) {
                        if (!(isEligibleAge(dob.toString()))) {
                            JOptionPane.showMessageDialog(null, "Sorry, you are under 18 years so you are not eligible to open account");
                            eligibleAge = false;
                        }
                    }
                } while (!isDobValid);
                if (eligibleAge) {
                    String[] dobArray = dob.toString().split("-");
                    dob = new StringBuilder();
                    for (int index = 0; index < (dobArray.length); index++) {
                        if (dobArray[index].length() == 1) dobArray[index] = "0" + dobArray[index];
                        dob.append(dobArray[index]);
                        if (index < 2) dob.append('-');
                    }
                    setDateOfBirth(dob.toString()); // Assigning date of birth to Customer instance field call dateOfBirth
                    setAddress(nameValidation(keyboard, "city"));  // Assigning city to Customer instance field called address.

                    // Asking and validating customer next of kin name
                    String nextOfKinName = nameValidation(keyboard, "next of kin name");
                    setNextOfKin(nextOfKinName);  // assigning name to Account instance field called nextOfKin.

                    // Asking and validating customer next of kin contact
                    setNextOfKinContact(contactValidation(keyboard, "next of kin contact"));  // assigning contact to Account instance field called nextOfKinContact


                    // Asking and validating Account type customer wants to open
                    String accountType;
                    do {
                        System.out.print("""
                                                    
                                Choose Account Type
                                    Savings Account
                                    Current Account
                                Enter 'S' for Savings Account or 'C' for Current Account:\s""");
                        accountType = keyboard.nextLine();
                    } while (!accountType.equalsIgnoreCase("S") && !accountType.equalsIgnoreCase("C"));
                    accountType = (accountType.equalsIgnoreCase("C")) ? "Current" : "Savings";

                    /*
                     this if blocks allow the customer continue with the opening of the account if the contact he or she is using does not
                     exist in the system at all, or if it exists, the account type the customer is trying to open is different from the
                     account type that is already created with that contact
                     */
                    if (!contactExist || !(userInfo[1].equalsIgnoreCase(accountType))) {
                        setAccountType(accountType);// Assigning account type to Account instance field called accountType
                        // Calling a method to ask customer if he or she wishes to continue with the registration
                        if (wantToContinue(accountType)) {
                            // Asking for user's initial deposit into his or her account
                            setAccountBalance(initialDepositValidation(keyboard, accountType));

                            // setting customer password
                            setPassword(passwordValidation(keyboard));

                            // setting customer pin
                            setPin(pinValidation(keyboard));

                            // Asking to choose security Question
                            int choice;
                            String option;
                            boolean isValidChoice;
                            String questionAns;
                            do {
                                System.out.printf("""
                                                                        
                                        Please choose which security question will you like us to ask you to reset your password if you forget your password
                                        1. %s
                                        2. %s
                                                                        
                                        Enter Option:\s""", getAllSecurityQuestions()[0], getAllSecurityQuestions()[1]);
                                option = keyboard.nextLine();
                                isValidChoice = isValidInteger(option);
                                if (!isValidChoice) System.out.print("\nInvalid option, Try again\n");
                                else {
                                    choice = Integer.parseInt(option);
                                    if (choice != 1 && choice != 2) {
                                        isValidChoice = false;
                                        System.out.print("\nInvalid option, Try again\n");
                                    } else {
                                        choice -= 1;
                                        setSecurityOption(choice);
                                        do {
                                            System.out.print("Enter your answer: ");
                                            questionAns = keyboard.nextLine();
                                            questionAns = questionAns.trim();
                                            if (questionAns.isEmpty())
                                                System.out.println("\nAnswer cannot be empty, Try again");
                                        } while (questionAns.isEmpty());
                                        getAllSecurityQuestions()[choice] = getAllSecurityQuestions()[choice] + ": " + questionAns;
                                        setSecurityQueForPassReset(getAllSecurityQuestions()[choice]);
                                    }
                                }
                            } while (!isValidChoice);
                            if (saveAccountInfo()) {
                                // Polymorphism
                                Account ac = new Account();
                                ac.displaySuccessMessage();
                                ac = new Bank();
                                ac.displaySuccessMessage();
                                displayAccountInfo();
                            }
                            else
                                JOptionPane.showMessageDialog(null, "Sorry!!, Account registration is unsuccessful, Try again");

                        } else
                            JOptionPane.showMessageDialog(null, "Registration of Account has been cancelled successfully");
                    } else {
                        JOptionPane.showMessageDialog(null, "Sorry this contact has already been used for this account type, Thank you");
                    }
                }
            } else JOptionPane.showMessageDialog(null, "Sorry, this contact has already been used");
        }

    }


}

package classes.project;

import javax.swing.*;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class LoginMethods extends ValidationMethods {
    // methods used only in the login method

    // method for generating the current date and time
    public String currentTimeGen() {
        DateTimeFormatter myDateFormat = DateTimeFormatter.ofPattern("E, MMM-dd-yy 'at' h:mm a");
        LocalDateTime myDateTime = LocalDateTime.now();
        return myDateTime.format(myDateFormat);
    }

    // method which takes in a filename as an argument and return data stored in that file.
    public ArrayList<String> getFileDetails(String acNum) {
        ArrayList<String> userDetail = new ArrayList<>();
        try {
            Scanner readFromFile = new Scanner(new File(getCustomerFilePath(acNum)));
            while (readFromFile.hasNext()) {
                String userInfo = readFromFile.nextLine();
                userDetail.add(userInfo);
            }
            readFromFile.close();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Account Number does not exist");
        }
        return userDetail;
    }

    // method which takes in filename and arraylist of string data type as argument and update the file with the content of the arraylist.
    public void updateFileContent(String acNum, ArrayList<String> value) {
        try {
            PrintWriter writeToFile = new PrintWriter(new FileWriter(getCustomerFilePath(acNum), false));
            for (String userInfo : value) writeToFile.println(userInfo);
            writeToFile.close();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Sorry, an error so user information's could not be updated successfully");
        }

    }

    // method which takes in filename as an argument, decrypts the pin stored in that file and return it.
    public String getDecryptedPin(String acNum) {
        Bank userAc = new Bank();
        String decryptedPin = "";
        for (String userInfo : getFileDetails(acNum)) {
            String[] userDetail = userInfo.split(" ");
            if (userDetail[0].equalsIgnoreCase("pin:")) {
                decryptedPin = new String(userAc.decryptPin(userDetail[1].getBytes()));
                break;
            }
        }
        return decryptedPin;
    }

    // method which takes in filename as an argument, decrypts the password stored in that file and return it.
    public String getDecryptedPassword(String acNum) {
        Bank userAc = new Bank();
        String decryptedPassword = "";
        for (String userInfo : getFileDetails(acNum)) {
            String[] userDetail = userInfo.split(" ");
            if (userDetail[0].equalsIgnoreCase("password:")) {
                decryptedPassword = new String(userAc.decryptPassword(userDetail[1].getBytes()));
                break;
            }
        }
        return decryptedPassword;
    }

    // method that takes in a filename as an argument and return the balance information stored in that file
    public double getAccountBalance(String acNum) {
        double balance = 0;
        for (String userInfo : getFileDetails(acNum)) {
            String[] userDetail = userInfo.split(" ");
            if (userDetail[0].equalsIgnoreCase("account") && userDetail[1].equalsIgnoreCase("balance:")) {
                String bal = userDetail[2].substring(1);
                balance = Double.parseDouble(bal);
                break;
            }
        }
        return balance;
    }

    // method that takes in a filename as an argument and return the full name information stored in that file
    public String getCurrentUserName(String acNum) {
        String name = "";
        for (String userInfo : getFileDetails(acNum)) {
            String[] userDetail = userInfo.split(":");
            if (userDetail[0].equalsIgnoreCase("full name")) {
                name = userDetail[1].trim();
                break;
            }
        }
        return name;
    }

    // method that takes in a filename as an argument and return the account type information stored in that file
    public String getAccountType(String acNum) {
        String accountType = "";
        for (String userInfo : getFileDetails(acNum)) {
            String[] userDetail = userInfo.split(" ");
            if (userDetail[0].equalsIgnoreCase("account") && userDetail[1].equalsIgnoreCase("type:")) {
                accountType = userDetail[2];
                break;
            }
        }
        return accountType.toLowerCase();
    }

    // method that takes in filename as an argument and return the decrypted version of question and answer that is used to allow the user to reset his or her password.
    public String[] getPassResetQueAns(String acNum) {
        Bank currentUser = new Bank();
        StringBuilder passResetAns = new StringBuilder();
        String[] passResetQueAns = new String[2];
        for (String userInfo : getFileDetails(acNum)) {
            userInfo = new String(currentUser.decryptPassword(userInfo.getBytes()));
            String[] userDetail = userInfo.split(":");
            if (userDetail[0].equalsIgnoreCase("What is your favorite pet name") || userDetail[0].equalsIgnoreCase("What is your favorite food")) {
                for (String value : userDetail) {
                    if (!(value.equalsIgnoreCase("What is your favorite pet name") || value.equalsIgnoreCase("What is your favorite food")))
                        passResetAns.append(value);
                }
                passResetQueAns[0] = userDetail[0];
                break;
            }
        }
        passResetAns = new StringBuilder(passResetAns.toString().trim());
        passResetQueAns[1] = passResetAns.toString();
        return passResetQueAns;
    }

    // method which takes in filename as an argument and return the number of days the file has elapsed since the day it was created
    public int getAgeOfAccountInDays(String acNum) {
        LocalDate myDate = LocalDate.now();
        DateTimeFormatter myFormatDate = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String todayDate = myDate.format(myFormatDate);
        String[] todayDateArray = todayDate.split("-");
        int dateCreatedDay;
        int dateCreatedMonth;
        int dateCreatedYear;
        int dateCreatedInDays = 0;
        int todayDay = Integer.parseInt(todayDateArray[0]);
        int todayMonth = Integer.parseInt(todayDateArray[1]);
        int todayYear = Integer.parseInt(todayDateArray[2]);
        int todayDateInDays = todayDay + (todayMonth * 30) + (todayYear * 365);
        for (String userInfo : getFileDetails(acNum)) {
            String[] userDetail = userInfo.split(" ");
            if (userDetail[0].equalsIgnoreCase("date") && userDetail[1].equalsIgnoreCase("created:")) {
                String[] dateCreated = userDetail[2].split("-");
                dateCreatedDay = Integer.parseInt(dateCreated[0]);
                dateCreatedMonth = Integer.parseInt(dateCreated[1]);
                dateCreatedYear = Integer.parseInt(dateCreated[2]);
                dateCreatedInDays = dateCreatedDay + (dateCreatedMonth * 30) + (dateCreatedYear * 365);
                break;
            }
        }
        return (todayDateInDays - dateCreatedInDays);
    }

    // method for display logout message
    public void logout() {
        JOptionPane.showMessageDialog(null, "You have log out successfully");
    }

    // method for authenticating customer's pin
    public boolean verifyPin(String acNum) {
        int count = 2;
        String userPin;
        String decryptedPin = getDecryptedPin(acNum);
        do {
            userPin = JOptionPane.showInputDialog(String.format("""
                    You have %d attempt(s)
                    Enter your pin""", count));
            if (!(userPin.equals(decryptedPin))) JOptionPane.showMessageDialog(null, "Invalid pin");
            count--;
        } while (count > 0 && !(userPin.equals(decryptedPin)));
        return userPin.equals(decryptedPin);
    }

    /*
    method that takes in a filename as an argument and update both the current balance and interest count information
    saved in the file after every 28 days, and it is automatically called when a customer with saving account type log in
     */
    public void updateBalWithInterest(String acNum) {
        ArrayList<String> userUpdate = new ArrayList<>();
        int currentInterestCount;
        int oldInterestCount = 0;
        int newNumOfInterestCount = getAgeOfAccountInDays(acNum) / 28;
        double updatedBalance = 0;
        for (String userInfo : getFileDetails(acNum)) {
            String[] interest = userInfo.split(" ");
            if (interest[0].equalsIgnoreCase("interest") && interest[1].equalsIgnoreCase("count:")) {
                oldInterestCount = Integer.parseInt(interest[2]);
                break;
            }
        }
        currentInterestCount = newNumOfInterestCount - oldInterestCount;
        for (String userInfo : getFileDetails(acNum)) {
            String[] balance = userInfo.split(" ");
            if (balance[0].equalsIgnoreCase("account") && balance[1].equalsIgnoreCase("balance:")) {
                balance[2] = balance[2].substring(1);
                updatedBalance = Float.parseFloat(balance[2]);
                while (currentInterestCount > 0) {
                    updatedBalance += (updatedBalance * 0.02);
                    --currentInterestCount;
                }
                break;
            }
        }
        if (newNumOfInterestCount > oldInterestCount) {
            for (String userInfo : getFileDetails(acNum)) {
                String[] userDetail = userInfo.split(" ");
                if (userDetail[0].equalsIgnoreCase("account") && userDetail[1].equalsIgnoreCase("balance:")) {
                    userInfo = String.format("Account Balance: $%.2f", updatedBalance);
                }
                if (userDetail[0].equalsIgnoreCase("interest") && userDetail[1].equalsIgnoreCase("count:")) {
                    userInfo = String.format("Interest Count: %d", newNumOfInterestCount);
                }
                userUpdate.add(userInfo);
            }
            updateFileContent(acNum, userUpdate);
        }
    }

    // method for displaying customer's account balance
    public void checkBalance(String acNum) {
        if (verifyPin(acNum))
            JOptionPane.showMessageDialog(null, String.format("Your balance is $%.2f", getAccountBalance(acNum)));
    }


    public void depositMoney(String acNum) {
        double amountDeposited;
        boolean isValidAmount = true;
        String amt;
        do {
            amt = JOptionPane.showInputDialog("""
                    Enter amount
                    NB: Enter 'B' to take you back to previous menu""");
            if (!(amt.equalsIgnoreCase("b"))) {
                isValidAmount = isValidDouble(amt);
                if (!isValidAmount) {
                    JOptionPane.showMessageDialog(null, "Invalid amount, Try again");
                } else if (Double.parseDouble(amt) < 1) {
                    isValidAmount = false;
                    JOptionPane.showMessageDialog(null, "Amount cannot be less than $1.00, Try again");
                } else {
                    amountDeposited = Double.parseDouble(amt);
                    double currentBalance = amountDeposited + getAccountBalance(acNum);
                    JOptionPane.showMessageDialog(null, String.format("""
                            Congratulation %s
                            An amount of $%.2f has been deposited into your account successfully
                            Your current balance is $%.2f""", getCurrentUserName(acNum), amountDeposited, currentBalance));
                    ArrayList<String> userUpdate = new ArrayList<>();
                    for (String userInfo : getFileDetails(acNum)) {
                        String[] bal = userInfo.split(" ");
                        if (bal[0].equalsIgnoreCase("account") && bal[1].equalsIgnoreCase("balance:")) {
                            userInfo = String.format("Account Balance: $%.2f", currentBalance);
                        }
                        userUpdate.add(userInfo);
                    }
                    updateFileContent(acNum, userUpdate);
                    String depositMessage = String.format("You deposited an amount of $%.2f into your account on %s", amountDeposited, currentTimeGen());
                    updateTransactionFile(acNum, getAccountType(acNum), depositMessage);
                }
            }
        } while (!isValidAmount && !(amt.equalsIgnoreCase("b")));
    }

    public void withdrawMoney(String acNum, String acType) {
        int days = getAgeOfAccountInDays(acNum);
        if (acType.equalsIgnoreCase("savings") && days < 28) {
            JOptionPane.showMessageDialog(null, String.format("""
                    Sorry %s
                    You are not qualify to perform this operation
                    You have %d day(s) remaining before you can perform this operation
                    Thank You.""", getCurrentUserName(acNum), (28 - days)));
        } else {
            double amountWithdrawn;
            boolean isValidAmount = true;
            String amt;
            double balance = getAccountBalance(acNum);
            do {
                amt = JOptionPane.showInputDialog("""
                        Enter amount
                        NB: Enter 'B' to take you back to previous menu""");
                if (!(amt.equalsIgnoreCase("b"))) {
                    isValidAmount = isValidDouble(amt);
                    if (!isValidAmount) {
                        JOptionPane.showMessageDialog(null, "Invalid amount, Try again");
                    } else if (Double.parseDouble(amt) < 1) {
                        isValidAmount = false;
                        JOptionPane.showMessageDialog(null, "Amount cannot be less than $1.00, Try again");
                    } else if (Double.parseDouble(amt) > balance) {
                        isValidAmount = false;
                        JOptionPane.showMessageDialog(null, "Your balance is insufficient,\nEnter amount less or equal to your current balance");
                    } else {
                        if (verifyPin(acNum)) {
                            amountWithdrawn = Double.parseDouble(amt);
                            double currentBalance = balance - amountWithdrawn;
                            JOptionPane.showMessageDialog(null, String.format("""
                                    Congratulation %s
                                    An amount of $%.2f has been withdrawn from your account successfully
                                    Your current balance is $%.2f""", getCurrentUserName(acNum), amountWithdrawn, currentBalance));
                            ArrayList<String> userUpdate = new ArrayList<>();
                            for (String userInfo : getFileDetails(acNum)) {
                                String[] bal = userInfo.split(" ");
                                if (bal[0].equalsIgnoreCase("account") && bal[1].equalsIgnoreCase("balance:")) {
                                    userInfo = String.format("Account Balance: $%.2f", currentBalance);
                                }
                                userUpdate.add(userInfo);
                            }
                            updateFileContent(acNum, userUpdate);
                            String withdrawalMessage = String.format("You withdrawn an amount of $%.2f from your account on %s", amountWithdrawn, currentTimeGen());
                            updateTransactionFile(acNum, getAccountType(acNum), withdrawalMessage);
                        }
                    }
                }
            } while (!isValidAmount && !(amt.equalsIgnoreCase("b")));
        }

    }

    public void transferMoney(String senderAcNum) {
        String receiverAcNum;
        boolean isValidAcNum;
        do {
            isValidAcNum = false;
            receiverAcNum = JOptionPane.showInputDialog("""
                    Enter receiver account number
                    Enter 'B' to go to previous menu
                                        
                    Account Number""");
            if (!(receiverAcNum.equalsIgnoreCase("b"))) {
                isValidAcNum = isValidInteger(receiverAcNum);
                if (!isValidAcNum) JOptionPane.showMessageDialog(null, """
                        Invalid Account Number Format
                        Account Number must contain only numbers""");
                else if (receiverAcNum.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Recipient Account Number cannot be empty");
                    isValidAcNum = false;
                } else if (receiverAcNum.length() != 10) {
                    JOptionPane.showMessageDialog(null, "Invalid Account Number Format\nAccount Number must contain 10 digits of numbers");
                    isValidAcNum = false;
                } else if (getFileDetails(receiverAcNum).size() == 0) isValidAcNum = false;
            }
        } while (!isValidAcNum && !(receiverAcNum.equalsIgnoreCase("b")));
        if (receiverAcNum.equals(senderAcNum)) {
            isValidAcNum = false;
            JOptionPane.showMessageDialog(null, "Sorry, you can't transfer money to yourself");
        }
        if (isValidAcNum) {
            double amountTransferred;
            boolean isValidAmount = true;
            String amt;
            double senderBalance = getAccountBalance(senderAcNum);
            double receiverBalance = getAccountBalance(receiverAcNum);
            String senderName = getCurrentUserName(senderAcNum);
            String receiverName = getCurrentUserName(receiverAcNum);
            do {
                amt = JOptionPane.showInputDialog("""
                        Enter amount
                        NB: Enter 'C' to cancel this operation""");
                if (!(amt.equalsIgnoreCase("c"))) {
                    isValidAmount = isValidDouble(amt);
                    if (!isValidAmount) {
                        JOptionPane.showMessageDialog(null, "Invalid amount, Try again");
                    } else if (Double.parseDouble(amt) < 1) {
                        isValidAmount = false;
                        JOptionPane.showMessageDialog(null, "Amount cannot be less than $1.00, Try again");
                    } else if (Double.parseDouble(amt) > senderBalance) {
                        isValidAmount = false;
                        JOptionPane.showMessageDialog(null, "Your balance is insufficient,\nEnter amount less or equal to your current balance");
                    } else {
                        boolean wishToContinue;
                        String option;
                        do {
                            amountTransferred = Double.parseDouble(amt);
                            wishToContinue = true;
                            option = JOptionPane.showInputDialog(String.format("""
                                    %s are you want to transfer an amount of $%.2f to %s?
                                    Enter YES(Y) to continue
                                    Enter NO(N) to MENU""", senderName, amountTransferred, receiverName));
                            if (!option.equalsIgnoreCase("y") && !option.equalsIgnoreCase("n") &&
                                    !option.equalsIgnoreCase("yes") && !option.equalsIgnoreCase("no"))
                                JOptionPane.showMessageDialog(null, "Invalid choice, Try again");
                            else if (option.equalsIgnoreCase("n") || option.equalsIgnoreCase("no"))
                                wishToContinue = false;
                        } while (!option.equalsIgnoreCase("y") && !option.equalsIgnoreCase("n") &&
                                !option.equalsIgnoreCase("yes") && !option.equalsIgnoreCase("no"));
                        if (wishToContinue && verifyPin(senderAcNum)) {
                            double senderCurrentBalance = senderBalance - amountTransferred;
                            double receiverCurrentBalance = receiverBalance + amountTransferred;
                            JOptionPane.showMessageDialog(null, String.format("""
                                    Congratulation %s
                                    You have transfer an amount of $%.2f to %s successfully
                                    Your current balance is $%.2f""", senderName, amountTransferred, receiverName, senderCurrentBalance));
                            ArrayList<String> updateFile = new ArrayList<>();
                            for (String userInfo : getFileDetails(senderAcNum)) {
                                String[] senderDetail = userInfo.split(" ");
                                if (senderDetail[0].equalsIgnoreCase("account") && senderDetail[1].equalsIgnoreCase("balance:")) {
                                    userInfo = String.format("Account Balance: $%.2f", senderCurrentBalance);
                                }
                                updateFile.add(userInfo);
                            }
                            updateFileContent(senderAcNum, updateFile);
                            String timeNow = currentTimeGen();
                            String transferMessage = String.format("You transferred an amount of $%.2f to %s on %s", amountTransferred, receiverName, timeNow);
                            updateTransactionFile(senderAcNum, getAccountType(senderAcNum), transferMessage);
                            updateFile.clear();
                            for (String userInfo : getFileDetails(receiverAcNum)) {
                                String[] receiverDetail = userInfo.split(" ");
                                if (receiverDetail[0].equalsIgnoreCase("account") && receiverDetail[1].equalsIgnoreCase("balance:")) {
                                    userInfo = String.format("Account Balance: $%.2f", receiverCurrentBalance);
                                }
                                updateFile.add(userInfo);
                            }
                            updateFileContent(receiverAcNum, updateFile);
                            String receiverMessage = String.format("You received an amount of $%.2f from  %s on %s", amountTransferred, senderName, timeNow);
                            updateTransactionFile(receiverAcNum, getAccountType(receiverAcNum), receiverMessage);
                        }
                    }
                }
            } while (!isValidAmount && !(amt.equalsIgnoreCase("c")));
        }
    }


    public void updateTransactionFile(String acNum, String acType, String value) {
        ArrayList<String> transactionUpdate = new ArrayList<>();
        String fileName = acNum + acType;
        for (String userTransaction : getFileDetails(fileName)) {
            if (!userTransaction.equalsIgnoreCase("transaction summary")) transactionUpdate.add(userTransaction);
        }
        transactionUpdate.add(0, value);
        transactionUpdate.add(0, "TRANSACTION SUMMARY");
        updateFileContent(fileName, transactionUpdate);
    }

    public void transactions(String transactionFileName, String acNum) {
        if (verifyPin(acNum)) {
            StringBuilder transactionMessage = new StringBuilder();
            int count = 0;
            for (String userInfo : getFileDetails(transactionFileName)) {
                if (userInfo.equalsIgnoreCase("transaction summary"))
                    transactionMessage.append('\t').append(userInfo).append("\n\n");
                else transactionMessage.append(count).append(". ").append(userInfo).append('\n');
                count++;
            }
            JOptionPane.showMessageDialog(null, transactionMessage.toString());
        }
    }

    public void accountDetails(String acNum) {
        Bank currentUser = new Bank();
        String[] passResetQueAns = getPassResetQueAns(acNum);
        if (verifyPin(acNum)) {
            StringBuilder userDetail = new StringBuilder("PERSONAL INFORMAL\n\n");
            for (String userInfo : getFileDetails(acNum)) {
                String userDecryptInfo = new String(currentUser.decryptPassword(userInfo.getBytes()));
                String[] detail = userInfo.split(" ");
                String[] userDetailInfo = userDecryptInfo.split(":");
                if (userDetailInfo[0].equalsIgnoreCase(passResetQueAns[0]))
                    userInfo = passResetQueAns[0] + ": " + passResetQueAns[1];
                else if (detail[0].equalsIgnoreCase("password:"))
                    userInfo = detail[0] + " " + getDecryptedPassword(acNum);
                else if (detail[0].equalsIgnoreCase("pin:")) userInfo = detail[0] + " " + getDecryptedPin(acNum);
                userDetail.append(userInfo).append('\n');
                if (detail[0].equalsIgnoreCase("date") && detail[1].equalsIgnoreCase("created:")) break;
            }
            JOptionPane.showMessageDialog(null, userDetail.toString());
        }
    }

    public void changePin(String acNum) {
        String currentPin = JOptionPane.showInputDialog("Enter your current pin");
        if (currentPin.equals(getDecryptedPin(acNum))) {
            String pin1, pin2 = "";
            boolean isPinValid;
            do {
                do {
                    isPinValid = false;
                    pin1 = JOptionPane.showInputDialog("""
                            Enter 'C' to cancel this operation
                            Enter your new 4 digit PIN
                            NEW PIN""");
                    if (!(pin1.equalsIgnoreCase("c"))) {
                        if (!(pin1.length() == 4))
                            JOptionPane.showMessageDialog(null, "Pin must contain only 4 digits, Try again");
                        else {
                            isPinValid = isValidInteger(pin1);
                            if (!isPinValid)
                                JOptionPane.showMessageDialog(null, "Pin must contain only numbers, Try again");
                            else if (pin1.equals(currentPin)) {
                                isPinValid = false;
                                JOptionPane.showMessageDialog(null, "New pin cannot be the same as old pin, Try again");
                            }
                        }
                    }
                } while (!isPinValid && !(pin1.equalsIgnoreCase("c")));
                if (isPinValid) {
                    pin2 = JOptionPane.showInputDialog("Re-enter your 4 digits pin to confirmed");
                    if (!pin1.equals(pin2)) JOptionPane.showMessageDialog(null, "pin does not match, Try again");
                    else {
                        Bank currentUser = new Bank();
                        ArrayList<String> updateUserFile = new ArrayList<>();
                        String newPin = new String(currentUser.encryptPin(pin1.getBytes()));
                        for (String userInfo : getFileDetails(acNum)) {
                            String[] detail = userInfo.split(" ");
                            if (detail[0].equalsIgnoreCase("pin:")) userInfo = detail[0] + " " + newPin;
                            updateUserFile.add(userInfo);
                        }
                        updateFileContent(acNum, updateUserFile);
                        JOptionPane.showMessageDialog(null, "Pin has been changed successfully");
                    }
                }

            } while (!pin1.equals(pin2) && isPinValid);

        } else JOptionPane.showMessageDialog(null, "Sorry your pin is invalid");
    }

    public void passwordReset(String acNum) {
        String userPassResponse;
        int count = 2;
        String[] passResetQueAns = getPassResetQueAns(acNum);
        do {
            userPassResponse = JOptionPane.showInputDialog(String.format("""
                    You have %d attempt(s)
                    Enter 'C' to cancel the operation
                                        
                    SECURITY QUESTION
                    %s
                                        
                    Enter your answer""", count, passResetQueAns[0]));
            if (!(userPassResponse.equalsIgnoreCase("c"))) {
                if (!(userPassResponse.equals(passResetQueAns[1])))
                    JOptionPane.showMessageDialog(null, "Sorry your answer is incorrect");
                count--;
            }
        } while (count > 0 && !(userPassResponse.equals(passResetQueAns[1])) && !userPassResponse.equalsIgnoreCase("c"));
        if (userPassResponse.equals(passResetQueAns[1])) {
            String password1, password2 = "";
            boolean isPasswordValid;
            do {
                do {
                    isPasswordValid = false;
                    password1 = JOptionPane.showInputDialog("""
                            Enter 'C' to cancel the operation
                            Password must include at least a special character, a number and a letter and length cannot be less than 8
                                                                    
                            NEW PASSWORD""");
                    if (!password1.equalsIgnoreCase("c")) {
                        if (password1.length() < 8)
                            JOptionPane.showMessageDialog(null, "Password length cannot be less than 8, Try again");
                        else {
                            isPasswordValid = isValidPassword(password1);
                            if (!isPasswordValid)
                                JOptionPane.showMessageDialog(null, "Password does not meet requirement, Try again");
                            else if (password1.equals(getDecryptedPassword(acNum))) {
                                isPasswordValid = false;
                                JOptionPane.showMessageDialog(null, "New password cannot be the same as old password");
                            }

                        }
                    }
                } while (!isPasswordValid && !password1.equalsIgnoreCase("c"));
                if (isPasswordValid) {
                    password2 = JOptionPane.showInputDialog("Re-enter your password to confirmed");
                    if (!password1.equals(password2))
                        JOptionPane.showMessageDialog(null, "Password does not match, Try again");
                    else {
                        Bank currentUser = new Bank();
                        ArrayList<String> updateUserFile = new ArrayList<>();
                        String newPassword = new String(currentUser.encryptPassword(password1.getBytes()));
                        for (String userInfo : getFileDetails(acNum)) {
                            String[] detail = userInfo.split(" ");
                            if (detail[0].equalsIgnoreCase("password:")) userInfo = detail[0] + " " + newPassword;
                            updateUserFile.add(userInfo);
                        }
                        updateFileContent(acNum, updateUserFile);
                        JOptionPane.showMessageDialog(null, "Password has been reset successfully");
                    }
                }
            } while (!password1.equals(password2) && isPasswordValid);
        }
    }

}

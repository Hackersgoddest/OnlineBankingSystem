package classes.project;

import javax.swing.*;

public class Login extends LoginMethods{
    public Login() {
        short count = 3;
        boolean isValidAccountNumber;
        String accountNum;
        do {
            isValidAccountNumber = false;
            accountNum = JOptionPane.showInputDialog(String.format("""
                    Your have %d attempt(s)
                    Enter 'B' to go back to previous menu
                                        
                    Enter your account number""", count));
            if (!(accountNum.equalsIgnoreCase("b"))) {
                if (!isValidInteger(accountNum))
                    JOptionPane.showMessageDialog(null, "Invalid Account Number Format\nAccount Number must contain only numbers");
                else if (accountNum.isEmpty()) JOptionPane.showMessageDialog(null, "Account Number cannot be empty");
                else if (accountNum.length() != 10)
                    JOptionPane.showMessageDialog(null, "Invalid Account Number Format\nAccount Number must contain 10 digits of numbers");
                else if (getFileDetails(accountNum).size() > 0) isValidAccountNumber = true;
            }
            count--;
        } while (count > 0 && !isValidAccountNumber && !(accountNum.equalsIgnoreCase("b")));
        if (isValidAccountNumber) {
            String decryptedPassword = getDecryptedPassword(accountNum);
            String accountType = getAccountType(accountNum);
            count = 3;
            boolean isValidPassword;
            String password;
            do {
                isValidPassword = false;
                password = JOptionPane.showInputDialog(String.format("""
                        You have %d attempt(s)
                        Enter 'B' to MENU
                        Enter 'R' to reset password
                                                
                        Enter your password""", count));
                if (!password.equalsIgnoreCase("B") && !password.equalsIgnoreCase("R")) {
                    if (password.equals(decryptedPassword)) isValidPassword = true;
                    else JOptionPane.showMessageDialog(null, "Invalid Password");
                    count--;
                }

            } while (count > 0 && !isValidPassword && !password.equalsIgnoreCase("B") && !password.equalsIgnoreCase("R"));
            if (isValidPassword) {
                int userChoice = 0;
                String transactionFileName = accountNum + (accountType.toLowerCase());
                if (accountType.equalsIgnoreCase("current")) {
                    do {
                        try {
                            userChoice = Integer.parseInt(JOptionPane.showInputDialog("""
                                    G14 BANKING SYSTEM
                                                                        
                                    1. Check Balance
                                    2. Deposit Money
                                    3. Withdraw Money
                                    4. Transfer Money
                                    5. View Transactions
                                    6. View Account Details
                                    7. Change PIN
                                    0. Logout
                                                                        
                                    Enter Option"""));
                            switch (userChoice) {
                                case 0 -> logout();
                                case 1 -> checkBalance(accountNum);
                                case 2 -> depositMoney(accountNum);
                                case 3 -> withdrawMoney(accountNum, accountType);
                                case 4 -> transferMoney(accountNum);
                                case 5 -> transactions(transactionFileName, accountNum);
                                case 6 -> accountDetails(accountNum);
                                case 7 -> changePin(accountNum);
                                default -> JOptionPane.showMessageDialog(null, "Invalid Option, Try again");
                            }
                        } catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(null, "Invalid option, Try again");
                        }

                    } while (userChoice != 0);
                } else {
                    updateBalWithInterest(accountNum);
                    do {
                        try {
                            userChoice = Integer.parseInt(JOptionPane.showInputDialog("""
                                    G14 BANKING SYSTEM
                                                                        
                                    1. Check Balance
                                    2. Deposit Money
                                    3. Withdraw Money
                                    4. View Transactions
                                    5. View Account Details
                                    6. Change PIN
                                    0. Logout
                                                                        
                                    Enter Option"""));
                            switch (userChoice) {
                                case 0 -> logout();
                                case 1 -> checkBalance(accountNum);
                                case 2 -> depositMoney(accountNum);
                                case 3 -> withdrawMoney(accountNum, accountType);
                                case 4 -> transactions(transactionFileName, accountNum);
                                case 5 -> accountDetails(accountNum);
                                case 6 -> changePin(accountNum);
                                default -> JOptionPane.showMessageDialog(null, "Invalid Option, Try again");
                            }
                        } catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(null, "Invalid option, Try again");
                        }
                    } while (userChoice != 0);

                }
            } else if (password.equalsIgnoreCase("R")) passwordReset(accountNum);
        }

    }
}

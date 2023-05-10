package classes.project;

import javax.swing.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static java.lang.Character.*;

public class ValidationMethods extends Path {
    // method for validating variables values of String data type
    public boolean isValidString(String value) {
        for (int index = 0; index < value.length(); index++) {
            if (!isLetter(value.charAt(index)) && !isWhitespace(value.charAt(index))) return false;
        }
        return true;
    }

    // method for validating variables values of integer, short and long data type
    public boolean isValidInteger(String value) {
        for (int index = 0; index < value.length(); index++) {
            if (!isDigit(value.charAt(index))) return false;
        }
        return true;
    }

    // method for validating variables values of double data type
    public boolean isValidDouble(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // method for validating password
    public boolean isValidPassword(String password) {

        if (password.length() >= 8) {
            Pattern letter = Pattern.compile("[a-zA-Z]");
            Pattern digit = Pattern.compile("[0-9]");
            Pattern special = Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~-]");


            Matcher hasLetter = letter.matcher(password);
            Matcher hasDigit = digit.matcher(password);
            Matcher hasSpecial = special.matcher(password);

            return hasLetter.find() && hasDigit.find() && hasSpecial.find();

        } else
            return false;

    }

    // method for validating email
    public boolean isValidEmail(String value) {
        Pattern pattern = Pattern.compile("^[a-z0-9+_.-]+@(.+)[a-z]$");
        Matcher hasAtInMid = pattern.matcher(value);
        return hasAtInMid.find();
    }

    // method for validating date
    public boolean isValidDOB(String dob) {
        String[] dobArray = dob.split("-");
        if (dobArray.length != 3) {
            System.out.println("Invalid date format, Try again");
            return false;
        }                    dob = dob.toString().trim();

        LocalDate myDate = LocalDate.now();
        DateTimeFormatter myFormatDate = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String date = myDate.format(myFormatDate);
        String[] dateArray = date.split("-");
        for (short index = 0; index < 3; index++) {
            if (!isValidInteger(dobArray[index])) {
                System.out.println("Invalid date of birth, Try again");
                return false;
            }
        }
        for(short index = 0; index < 3; index++) {
            short value = Short.parseShort(dobArray[index]);
            if (index == 0 && (value < 1 || value > 31)) {
                System.out.println("Day cannot be less than 1 or greater than 31, Try again");
                return false;
            } else if (index == 1 && (value < 1 || value > 12)) {
                System.out.println("Month cannot be less than 1 or greater than 12, Try again");
                return false;
            } else if (index == 2 && (value < 1800 || value > Integer.parseInt(dateArray[2]))) {
                System.out.println("year cannot be less than 1800 or greater than 2022, Try again");
                return false;
            } else if ((((Short.parseShort(dobArray[2]) % 4 == 0) &&
                    (Short.parseShort(dobArray[2]) % 100 != 0)) ||
                    (Short.parseShort(dobArray[2]) % 400 == 0)) &&
                    Short.parseShort(dobArray[1]) == 2 && Short.parseShort(dobArray[0]) > 29) {
                System.out.println("Is a leap year, so days in February cannot be greater than 29, Try again");
                return false;
            } else if (!(((Short.parseShort(dobArray[2]) % 4 == 0) &&
                    (Short.parseShort(dobArray[2]) % 100 != 0)) ||
                    (Short.parseShort(dobArray[2]) % 400 == 0)) &&
                    Short.parseShort(dobArray[1]) == 2 && Short.parseShort(dobArray[0]) > 28) {
                System.out.println("Is not a leap year, so days in February cannot be greater than 28, Try again");
                return false;
            } else if ((Short.parseShort(dobArray[1]) == 4 ||
                    Short.parseShort(dobArray[1]) == 6 ||
                    Short.parseShort(dobArray[1]) == 9 ||
                    Short.parseShort(dobArray[1]) == 11) && Short.parseShort(dobArray[0]) > 30) {
                System.out.println("Days in this month cannot be greater than 30");
                return false;
            }
        }
        return true;
    }

    // method which will be called during opening of account to assess the customer whether he or she wants to continue with the registration or not
    public boolean wantToContinue(String acType) {
        String option;
        do {
            // The if block will be executed if the account type of the account the user is opening is savings else the else block will be executed.
            if (acType.equalsIgnoreCase("savings")) option = JOptionPane.showInputDialog("""
                    Are you satisfied so far?
                    By continuing, you agree that you can't withdrawn money from this account until 28 days later,
                    Also you will receive 2% interest on your current balance every 28 days
                    Enter YES(Y) to continue
                    Enter NO(N) to MENU""");
            else option = JOptionPane.showInputDialog("""
                    Are you satisfied so far?
                    Enter YES(Y) to continue
                    Enter NO(N) to MENU""");
            if (!option.equalsIgnoreCase("y") && !option.equalsIgnoreCase("n") &&
                    !option.equalsIgnoreCase("yes") && !option.equalsIgnoreCase("no"))
                JOptionPane.showMessageDialog(null, "Invalid choice, Try again");
        } while (!option.equalsIgnoreCase("y") && !option.equalsIgnoreCase("n") &&
                !option.equalsIgnoreCase("yes") && !option.equalsIgnoreCase("no"));
        return option.equalsIgnoreCase("y") || option.equalsIgnoreCase("yes");
    }

    // name validation method
    public String nameValidation(Scanner keyboard, String value) {
        String name;
        boolean isValidName;
        do {
            if(value.equalsIgnoreCase("name")) System.out.print("Enter your name(type 'b' to go to previous menu): ");
            else System.out.print("Enter your "+value+": ");
            name = keyboard.nextLine();
            name = name.replaceAll("\\s+", " ").trim();   // removing unnecessary spaces from username
            isValidName = isValidString(name);
            if (!isValidName) System.out.println("Invalid name, Try again");
            if (name.isEmpty()) {
                System.out.println("Name cannot be empty, Try again");
                isValidName = false;
            }
        } while (!isValidName && !(name.equalsIgnoreCase("b")));
        return name;
    }

    public String contactValidation(Scanner keyboard, String value){
        // Asking and validating customer contact
        String contact;
        boolean isValidContact;
        do {
            System.out.print("Enter your "+value+"(Contact should start with 0): ");
            contact = keyboard.nextLine();
            contact = contact.trim();   // removing unnecessary spaces before and after contact
            isValidContact = isValidInteger(contact);
            if (!isValidContact) System.out.println("Invalid contact, Try again");
            if (contact.charAt(0) != '0') {
                System.out.println("Invalid contact, Contact should start with 0, Try again");
                isValidContact = false;
            } else if (contact.length() < 10) {
                System.out.println("Contact digits cannot be less than 10 , Try again");
                isValidContact = false;
            } else if (contact.length() > 10) {
                System.out.println("Contact digits cannot be greater than 10 , Try again");
                isValidContact = false;
            }
        } while (!isValidContact);
        return contact;
    }

    public String emailValidation(Scanner keyboard) {
        // Asking and validating customer email
        String email;
        boolean isEmailValid;
        do {
            System.out.print("""
                    NB: (make sure to include '@' but other special symbols are not accepted except '_', '.', and '-' before the @ sign
                    and it should be end with a letter)
                    Enter your email:\s""");
            email = keyboard.nextLine();
            email = email.trim();
            isEmailValid = isValidEmail(email);
            if (!isEmailValid) System.out.print("Invalid email, Try again");
            else {
                int countAt = 0;
                for (int index = 0; index < email.length(); index++) {
                    if (email.charAt(index) == '@') countAt++;
                }
                if (countAt > 1) {
                    isEmailValid = false;
                    System.out.println("Invalid email, Try again");
                }
            }
        } while (!isEmailValid);
        return email;
    }
    public double initialDepositValidation(Scanner keyboard, String accountType) {
        double amount = 0;
        boolean isValidAmount;
        do {
            if (accountType.equalsIgnoreCase("savings")) {
                System.out.print("""
                                                                
                                            NB: (initial deposit cannot be less than $10.00)
                                            Enter initial amount you want to deposit:\s""");
            } else {
                System.out.print("""
                                                                
                                            NB: (initial deposit cannot be less than $100.00)
                                            Enter initial amount you want to deposit:\s""");
            }
            String amt = keyboard.nextLine();
            isValidAmount = isValidDouble(amt);
            if (!isValidAmount) System.out.print("\nInvalid amount, Try again");
            else {
                amount = Double.parseDouble(amt);
                if (accountType.equalsIgnoreCase("savings") && amount < 10.00)
                    isValidAmount = false;
                else if (accountType.equalsIgnoreCase("current") && amount < 100.00)
                    isValidAmount = false;
            }
        } while (!isValidAmount);
        return amount;
    }

    public String passwordValidation(Scanner keyboard) {
        // Asking and validating customer password he or she will use to log in to the account
        String password1, password2;
        do {
            boolean isPasswordValid;
            do {
                System.out.print("""
                                                                    
                                            NB:(Password must include at least a special character, a number and a letter and length cannot be less than 8)
                                            Enter your password:\s""");
                password1 = keyboard.nextLine();
                if (password1.length() < 8) {
                    System.out.println("Password length cannot be less than 8, Try again");
                    isPasswordValid = false;
                } else {
                    isPasswordValid = isValidPassword(password1);
                    if (!isPasswordValid)
                        System.out.print("\nPassword does not meet requirement, Try again");
                }
            } while (!isPasswordValid);
            System.out.print("""
                                        Re-enter your password to confirmed:\s""");
            password2 = keyboard.nextLine();

            if (!password1.equals(password2))
                System.out.print("\nPassword does not match, Try again");
        } while (!password1.equals(password2));
        return password1;
    }

    public String pinValidation(Scanner keyboard) {
        // Asking and validating customer pin he or she will use to authorize activities such as transferring money, withdrawing money, etc.
        String pin1, pin2;
        do {
            boolean isPinValid;
            do {
                System.out.print("""
                                            Enter your 4 digit PIN
                                            NB: (You will be using this pin as key to authorize activities like transferring or withdrawing money and many more)
                                            PIN:\s""");
                pin1 = keyboard.nextLine();
                if (!(pin1.length() == 4)) {
                    System.out.println("Pin must contain only 4 digits, Try again");
                    isPinValid = false;
                } else {
                    isPinValid = isValidInteger(pin1);
                    if (!isPinValid) System.out.print("\nPin must contain only numbers, Try again");
                }
            } while (!isPinValid);
            System.out.print("""
                                        Re-enter your 4 digits pin to confirmed:\s""");
            pin2 = keyboard.nextLine();
            if (!pin1.equals(pin2)) System.out.print("\npin does not match, Try again");
        } while (!pin1.equals(pin2));
        return pin1;
    }

}

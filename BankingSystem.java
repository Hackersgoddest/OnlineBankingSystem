package classes.project;

import javax.swing.*;


public class BankingSystem {
    public static void main(String[] args) {
        // calling the mainMenu method
        mainMenu();
    }


    // method for displaying the main menu of the banking system
    public static void mainMenu() {
        int choice = 0;
        boolean isValidChoice;
        do {
            isValidChoice = true;
            try {
                choice = Integer.parseInt(JOptionPane.showInputDialog("""
                            G14 BANK
                                                
                        1. Create Account
                        2. Login
                        0. Exit
                                        
                        Enter option"""));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid choice, Try again");
                isValidChoice = false;
            }
            if (isValidChoice) {
                switch (choice) {
                    case 1 -> new createAccount();
                    case 2 -> new Login();
                    case 0 -> JOptionPane.showMessageDialog(null, "Thank you for using G14 Bank");
                    default -> JOptionPane.showMessageDialog(null, "Your choice is out of range, Try again");
                }
            }
        } while (choice != 0 || !isValidChoice);

    }

//    public static void callingPythonMethod() {
//        // calling and executing a python file in this java file
//        try {
//            String pythonScriptPath = "welcome/welcomeMessage.py";
//            String[] cmd = new String[2];
//            cmd[0] = "python"; // command to execute Python interpreter
//            cmd[1] = pythonScriptPath; // path to the Python script file
//
//            ProcessBuilder processBuilder = new ProcessBuilder(cmd);
//            Process process = processBuilder.start();
//
//            // Read output from the Python script
//            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            String line;
//            while ((line = reader.readLine()) != null) {
//                JOptionPane.showMessageDialog(null, line);
//            }
//
//            // Wait for the Python script to complete
//            int exitCode = process.waitFor();
//            System.out.println("Python script exited with code " + exitCode);
//        } catch (IOException e) {
//            System.out.println("Can't call python file");
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//    }
}





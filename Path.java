package classes.project;

public class Path {
    // this method returns path to the customer file containing his or her account details
    public String getCustomerFilePath(String acNum) {
        return "src/classes/project/" + acNum + ".txt";
    }

    // this method returns path to the customer file which contains his or her transactions details
    public String getCustomerTransactionFilePath(String acNum, String acTyp) {
        return "src/classes/project/" + acNum + acTyp + ".txt";
    }

    /* this method returns the path to the file which allows the customer to use the same contact create only two different
    different account type, that is, one contact can be use to create only two account types, that is savings and current
    account
     */
    public String getAuthorizationFilePath() {
        return "src/classes/project/auth.txt";
    }
}

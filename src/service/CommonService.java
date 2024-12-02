package service;

import model.User;
import utility.Utility;

import java.util.ArrayList;

public class CommonService {


    public void accessLandingPage(){

        System.out.println("Welcome to the Landing Page\n");

        System.out.println("1. Sign In");
        System.out.println("2. Sign Up\n");

        switch (Utility.inputOutput("Please Select One Of The Options")){
            case "1":{
                System.out.println("directing to Sign In Page");
                viewSignInPage();
                break;
            }
            case "2":{
                System.out.println("directing to Sign Up Page");
                viewSignUpPage();
                break;
            }
            default: {
                System.out.println("You entered invalid option");
                accessLandingPage();
                break;
            }
        }

    }

    public void viewSignInPage(){
        System.out.println("Welcome to Signin page\n");
        System.out.println("1. Continue to Signin\n");
        System.out.println("2. Go back to Landing page\n");

        switch(Utility.inputOutput("Please Select One Of The Options")){
            case "1": 
                System.out.println("Welcome to Signin page\n");
                String userName = Utility.inputOutput("Enter your User name");
                String password = Utility.inputOutput("Enter your password");
                signIn(userName, password);
                break;
            case "2": 
                System.out.println("\nRediredting to Landing Page\n");
                accessLandingPage();
                break;
            default:
                System.out.println("You entered invalid option");
                viewSignInPage();
                break;
        }
    }

    public void signIn(String userName, String password){
       
        System.out.println("\nUser name" + userName + "\nPassword" + password);
    }

    public void signIn(ArrayList<User> users, String userName, String password) {
        System.out.println("Welcome to Sign-in page\n");
    }
    
    public void viewSignUpPage(){
        String id;
        String firstName;
        String lastName;
        String userName;
        String password;
        String recruiterCode = "ABCD";
        System.out.println("Welcome to Sign Up page\n");
        System.out.println("1. Signup as Applicant\n");
        System.out.println("2. Signup as Recruiter\n");
        System.out.println("3. Go back to Landing page\n");

        switch(Utility.inputOutput("Please Select One Of The Options")){
            case "1": 
                System.out.println("Welcome to Applicant Signup page \n");
                id = "111";
                firstName = Utility.inputOutput("Enter your first name");
                lastName =  Utility.inputOutput("Enter your last name");
                userName =  Utility.inputOutput("Create a user name");
                password =  Utility.inputOutput("Create a strong password");
                signUp("Applicant", id, firstName, lastName, userName, password);
                break;
            case "2":
                System.out.println("Welcome to Recruiter Signup page \n");
                if (Utility.inputOutput("Enter the Recruiter Code").equals(recruiterCode)) {
                    id = "111";
                    firstName = Utility.inputOutput("Enter your first name");
                    lastName =  Utility.inputOutput("Enter your last name");
                    userName =  Utility.inputOutput("Create a user name");
                    password =  Utility.inputOutput("Create a strong password");
                    signUp("Recruiter", id, firstName, lastName, userName, password);                
                }else {
                    System.out.println("\nIncorrect Recruiter Code\n");
                    viewSignUpPage();
                }
                break;
            case "3": 
                System.out.println("\nRediredting to Landing Page\n");
                accessLandingPage();
                break;
            default:
                System.out.println("You entered invalid option");
                viewSignUpPage();
                break;
        }

    }
    
    public void signUp(String role, String id, String firstName, String lastName, String userName, String password){
        System.out.println("Welcome to " + role + " Signup page \n");

        if (role.equals("Applicant")) {
            System.out.println("\nAPPLICANT DETAILS\n");
            System.out.println("\nId " + id + "\nFirst name " + firstName + "\nLast name " + lastName + "\nUser name " + userName + "\nPassword " + password + "\nRole " + role);
        } else {
            System.out.println("\nRECRUITER DETAILS\n");
            System.out.println("\nId " + id + "\nFirst name " + firstName + "\nLast name " + lastName + "\nUser name " + userName + "\nPassword " + password + "\nRole " + role);
        }
    }

    public void logOut() {

    }

    public void viewResetPasswordPage() {

    }

    public void resetPassword() {

    }


}

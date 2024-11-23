package service;

import java.util.ArrayList;

import model.Applicant;
import model.Recruiter;
import model.User;
import utility.Utility;

public class CommonService {

    private final Utility utility;

    public CommonService() {
        utility = new Utility();}

    public void accessLandingPage(ArrayList<User> users){

        System.out.println("Welcome to the Landing Page\n");

        System.out.println("1. Sign In");
        System.out.println("2. Sign Up\n");

        switch (utility.inputOutput("Please Select One Of The Options")){
            case "1":{
                viewSignInPage(users);
                break;
            }
            case "2":{
                viewSignUpPage(users);
                break;
            }
            default:{
                System.out.println("You entered invalid option");
                accessLandingPage(users);
                break;
            }
        }

    }

    public void viewSignInPage(ArrayList<User> users){
        System.out.println("Welcome to Signin page\n");
        System.out.println("1. Continue to Signin\n");
        System.out.println("2. Go back to Landing page\n");

        switch(utility.inputOutput("Please Select One Of The Options")){
            case "1": 
                signIn(users);
                break;
            case "2": 
                accessLandingPage(users);
                break;
            default: 
                System.out.println("You entered invalid option");
                viewSignInPage(users);
                break;
        }

    }
    public void signIn(ArrayList<User> users){
        System.out.println("Welcome to Signin page \n");

        String userName = utility.inputOutput("Enter your User name");
        String password = utility.inputOutput("Enter your password");

        System.out.println("\nUser name" + userName + "\nPassword" + password);

    }
    public void viewSignUpPage(ArrayList<User> users){
        System.out.println("Welcome to Sign Up page\n");
        System.out.println("1. Signup as Applicant\n");
        System.out.println("2. Signup as Recruiter\n");
        System.out.println("3. Go back to Landing page\n");

        switch(utility.inputOutput("Please Select One Of The Options")){
            case "1": 
                signUp("Applicant", users);
                break;
            case "2": 
                signUp("Recruiter", users);
                break;
            case "3": 
                accessLandingPage(users);
                break;
            default: 
                System.out.println("You entered invalid option");
                viewSignUpPage(users);
                break;
        }

    }
    public void signUp(String role, ArrayList<User> users){
        System.out.println("Welcome to " + role + " Signup page \n");

        String id = "Applicant Unique Id";
        String firstName =  utility.inputOutput("Enter your first name");
        String lastName =  utility.inputOutput("Enter your last name");
        String userName =  utility.inputOutput("Create a user name");
        String password =  utility.inputOutput("Create a strong password");

        if (role.equals("Applicant")) {
            System.out.println("\nAPPLICANT DETAILS\n");
            System.out.println("\nId " + id + "\nFirst name " + firstName + "\nLast name " + lastName + "\nUser name " + userName + "\nPassword " + password + "\nRole " + role);
        } else {
            System.out.println("\nRECRUITER DETAILS\n");
            System.out.println("\nId " + id + "\nFirst name " + firstName + "\nLast name " + lastName + "\nUser name " + userName + "\nPassword " + password + "\nRole " + role);
        }
           }
    public void logOut(){

    }
    public void viewResetPasswordPage(){

    }
    public void resetPassword(){

    }




}

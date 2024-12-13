package service;

import java.util.ArrayList;
import java.util.UUID;
import model.Recruiter;
import model.User;
import utility.Utility;


public class CommonService {

    ApplicantService applicantService = new ApplicantService();
    RecruiterService recruiterService = new RecruiterService();


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

    public void signIn(String userName, String password) {
        ArrayList<User> users = Utility.getUsers();
        Boolean isUserFound = false;

        for (User user : users) {
            if (user.getUserName().equals(userName) && user.getPassword().equals(password)) {
                isUserFound = true;

                if (user.getRole() == null) {
                    System.out.println("\nUser role is not defined. Please contact the administrator.");
                    Utility.inputOutput("Press enter to go back...");
                    accessLandingPage();
                }

                Utility.setCurrentUser(user);

                if (user.getRole().toLowerCase().equals("applicant")) {
                    ApplicantService applicantService = new ApplicantService();
                    applicantService.viewApplicantDashboard();
                }
            }
        }

        if (!isUserFound) {
            System.out.println("\n");
            String tryAgain = Utility.inputOutput("Invalid username or password. Do you want to try again? (y/n)");
            
            if (tryAgain.equals("y")) {
                viewSignInPage();
            } else {
                accessLandingPage();
            }
        }
    }

    public void viewSignUpPage(){
        String firstName;
        String lastName;
        String userName;
        String password;
        String recruiterCode;
        System.out.println("Welcome to Sign Up page\n");
        System.out.println("1. Signup as Applicant\n");
        System.out.println("2. Signup as Recruiter\n");
        System.out.println("3. Go back to Landing page\n");

        switch(Utility.inputOutput("Please Select One Of The Options")){
            case "1": 
                System.out.println("Welcome to Applicant Signup page \n");
                firstName = Utility.inputOutput("Enter your first name");
                lastName =  Utility.inputOutput("Enter your last name");
                userName =  Utility.inputOutput("Create a user name");
                password =  Utility.inputOutput("Create a strong password");
                signUp("Applicant",null, firstName, lastName, userName, password);
                break;
            case "2":
                System.out.println("Welcome to Recruiter Signup page \n");
                    firstName = Utility.inputOutput("Enter your first name");
                    lastName =  Utility.inputOutput("Enter your last name");
                    userName =  Utility.inputOutput("Create a user name");
                    password =  Utility.inputOutput("Create a strong password");
                    recruiterCode = Utility.inputOutput("Enter the Recruiter Code");
                    signUp("Recruiter", recruiterCode, firstName, lastName, userName, password);                
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

    public void signUp(String role, String recruiterCode, String firstName, String lastName, String userName, String password){
        System.out.println("Welcome to " + role + " Signup page \n");
        if(role.equals("Recruiter")){
        RecruiterService recruiterService = new RecruiterService();
        String id = UUID.randomUUID().toString();
        if(recruiterCode!=null && recruiterCode.equals("XVQTY")){
            Recruiter newRecruiter = new Recruiter(id,firstName, lastName, userName, password);
            Utility.getUsers().add(newRecruiter);
            System.out.println("Sign Up Successful for Recruiter");
            Utility.setCurrentUser(newRecruiter);
            System.out.println("directing to Recruiter Dashboard");
            recruiterService.viewRecruiterDashboard();
        }}

        // Applicant part
        else if(role.equals("Applicant")){
            ApplicantService applicantService = new ApplicantService();
            String id = UUID.randomUUID().toString();
            User newUser = new User(id, firstName, lastName, userName, password, role);
            Utility.getUsers().add(newUser);
            System.out.println("Sign Up Successful for Applicant");
            Utility.setCurrentUser(newUser);
            System.out.println("directing to Applicant Dashboard");
            applicantService.viewApplicantDashboard();
        }
        else
        {
            System.out.println("Invalid Role");
            viewSignUpPage();
        }
    }

    public void logOut() {
        Utility.setCurrentUser(null);
        System.out.println("Logged Out Successfully");
        accessLandingPage();

    }

    public void viewResetPasswordPage() {

        System.out.println("Welcome to reset password page\n");
        System.out.println("1. Continue to reset paswsword\n");
        System.out.println("2. Go back to dashword\n");

        switch(Utility.inputOutput("Please Select One Of The Options")){
            case "1": 
                System.out.println("Welcome to reset password page\n");
                String userName = Utility.inputOutput("Enter your User name");
                resetPassword(userName);
                break;
            case "2": 
                System.out.println("\nRediredting to Dashboard\n");
                if(Utility.getCurrentUser().getRole().equals("Applicant")) {
                    System.out.println("\nRedirecting to Applicant dashboard\n");
                    applicantService.viewApplicantDashboard();
                } else if(Utility.getCurrentUser().getRole().equals("Recruiter")) {
                    System.out.println("\nRedirecting to Recruiter dashboard\n");
                    recruiterService.viewRecruiterDashboard();
                }
                break;
            default:
                System.out.println("You entered invalid option");
                viewResetPasswordPage();
                break;
        }

    }

    public void resetPassword(String userName) {
        System.out.println("\nYour entered username: " + userName + "\n");
        if(Utility.getCurrentUser().getUserName().equals(userName) && Utility.getCurrentUser().getRole().equals("Applicant")) {
            Utility.inputOutput("Enter your New Password");
            Utility.getCurrentUser().setPassword(userName);
            applicantService.viewApplicantDashboard();
            
        } else {
            System.out.println("\nYou have entered wrong Crediantials\n");
            viewResetPasswordPage();
        }
    }

}

package service;

import java.util.ArrayList;
import model.User;
import utility.Utility;

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
            default:{
                System.out.println("You entered invalid option");
                accessLandingPage();
                break;
            }
        }

    }

    public void viewSignInPage(){

    }

    public void signIn(){
        ArrayList<User> users = Utility.getUsers();
        Boolean isUserFound = false;

        System.out.println("\nSign In\n");
        String userName = Utility.inputOutput("Enter Username: ");
        String password = Utility.inputOutput("Enter Password: ");

        for (User user : users) {
            if (user.getUserName().equals(userName) && user.getPassword().equals(password)) {
                isUserFound = true;

                if (user.getRole() == null) {
                    System.out.println("\nUser role is not defined. Please contact the administrator.");
                    Utility.inputOutput("Press enter to go back...");
                    accessLandingPage();
                }

                if (user.getRole().toLowerCase().equals("applicant")) {
                    ApplicantService applicantService = new ApplicantService();
                    applicantService.viewApplicantDashboard(user);
                }
            }
        }

        if (!isUserFound) {
            System.out.println("\n");
            String tryAgain = Utility.inputOutput("Invalid username or password. Do you want to try again? (y/n)");
            
            if (tryAgain.equals("y")) {
                signIn();
            } else {
                accessLandingPage();
            }
        }
    }

    public void viewSignUpPage(){

    }
    public void signUp(){

    }
    public void logOut(){

    }
    public void viewResetPasswordPage(){

    }
    public void resetPassword(){

    }




}

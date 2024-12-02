package service;

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

    }
    public void viewSignUpPage(){

    }
    public void signUp(){

    }
    public void logOut(){

        Utility.setCurrentUser(null);
        System.out.println("Logged Out Successfully");
        accessLandingPage();

    }
    public void viewResetPasswordPage(){

    }
    public void resetPassword(){

    }




}

package service;

import model.Recruiter;
import model.User;
import utility.Utility;

import java.util.UUID;

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
    public void signUp(User user,String code){

        RecruiterService recruiterService = new RecruiterService();
        String id = UUID.randomUUID().toString();
        if(code!=null && code.equals("XVQTY")){
            Recruiter newRecruiter = new Recruiter(id,user.getName(), user.getLastName(), user.getUserName(),user.getPassword());
            Utility.getUsers().add(newRecruiter);
            System.out.println("Sign Up Successful for Recruiter");
            Utility.setCurrentUser(newRecruiter);
            System.out.println("directing to Recruiter Dashboard");
            recruiterService.viewRecruiterDashboard();
        }

        // Applicant part

    }
    public void logOut(){

    }
    public void viewResetPasswordPage(){

    }
    public void resetPassword(){

    }




}

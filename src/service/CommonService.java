package service;

import model.Applicant;
import model.Recruiter;
import model.User;
import model.UserRole;
import utility.Utility;

import java.util.ArrayList;
import java.util.UUID;


public class CommonService {


    private static CommonService instance = null;

    public static CommonService getInstance() {
        if (instance == null) {
            instance = new CommonService();
        }
        return instance;
    }


    public void accessLandingPage() {

        System.out.println("Welcome to the Landing Page\n");

        System.out.println("1. Sign In");
        System.out.println("2. Sign Up\n");

        switch (Utility.inputOutput("Please Select One Of The Options")) {
            case "1": {
                System.out.println("directing to Sign In Page");
                viewSignInPage();
                break;
            }
            case "2": {
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

    public void viewSignInPage() {
        System.out.println("Welcome to Signin page\n");
        System.out.println("1. Continue to Signin\n");
        System.out.println("2. Go back to Landing page\n");

        switch (Utility.inputOutput("Please Select One Of The Options")) {
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


    /**
     * @param users    List of users from the database to look for the user
     * @param userName username
     * @param password password
     * @return User object if authenticated, null otherwise
     */
    public User authenticateUser(ArrayList<User> users, String userName, String password) {
        for (User user : users) {
            if (user.getUserName().equals(userName) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    public void signIn(String userName, String password) {
        ArrayList<User> users = Utility.getUsers();

        User user = authenticateUser(users, userName, password);
        if (user == null) {
            System.out.println("\n");
            String tryAgain = Utility.inputOutput("Invalid username or password. Do you want to try again? (y/n)");
            
            if (tryAgain.equals("y")) {
                viewSignInPage();
            } else {
                accessLandingPage();
            }
        } else {
            Utility.setCurrentUser(user);
            if (user.getRole() == UserRole.APPLICANT) {
                ApplicantService applicantService = new ApplicantService();
                applicantService.viewApplicantDashboard();
            } else if (user.getRole() == UserRole.RECRUITER) {
                RecruiterService recruiterService = new RecruiterService();
                recruiterService.viewRecruiterDashboard();
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

        switch (Utility.inputOutput("Please Select One Of The Options")) {
            case "1":
                System.out.println("Welcome to Applicant Signup page \n");
                firstName = Utility.inputOutput("Enter your first name");
                lastName = Utility.inputOutput("Enter your last name");
                userName = Utility.inputOutput("Create a user name");
                password = Utility.inputOutput("Create a strong password");
                signUp("Applicant", null, firstName, lastName, userName, password);
                break;
            case "2":
                System.out.println("Welcome to Recruiter Signup page \n");
                firstName = Utility.inputOutput("Enter your first name");
                lastName = Utility.inputOutput("Enter your last name");
                userName = Utility.inputOutput("Create a user name");
                password = Utility.inputOutput("Create a strong password");
                recruiterCode = Utility.inputOutput("Enter the Recruiter Code");
                signUp("Recruiter", recruiterCode, firstName, lastName, userName, password);
                break;
            case "3":
                System.out.println("\nRedirecting to Landing Page\n");
                accessLandingPage();
                break;
            default:
                System.out.println("You entered invalid option");
                viewSignUpPage();
                break;
        }
    }

    public void signUp(String role, String recruiterCode, String firstName, String lastName, String userName, String password) {
        System.out.println("Welcome to " + role + " Signup page \n");
        
        RecruiterService recruiterService = new RecruiterService();
        String id = UUID.randomUUID().toString();
        if (recruiterCode != null && recruiterCode.equals("XVQTY")) {
            Recruiter newRecruiter = new Recruiter(id, firstName, lastName, userName, password);
            Utility.getUsers().add(newRecruiter);
            System.out.println("Sign Up Successful for Recruiter");
            Utility.setCurrentUser(newRecruiter);
            System.out.println("directing to Recruiter Dashboard");
            recruiterService.viewRecruiterDashboard();
        }
        // Applicant part
        else {
            ApplicantService applicantService = new ApplicantService();
            User newUser = new Applicant(id, firstName, lastName, userName, password, new ArrayList<>());
            Utility.getUsers().add(newUser);
            System.out.println("Sign Up Successful for Applicant");
            Utility.setCurrentUser(newUser);
            System.out.println("directing to Applicant Dashboard");
            applicantService.viewApplicantDashboard();
        }

    }

    public void logOut() {
        Utility.setCurrentUser(null);
        System.out.println("Logged Out Successfully");
        accessLandingPage();
    }

    public void viewResetPasswordPage() {

        System.out.println("Welcome to reset password page\n");
        System.out.println("1. Continue to reset password\n");
        System.out.println("2. Go back to dashboard\n");

        switch (Utility.inputOutput("Please Select One Of The Options")) {
            case "1":
                System.out.println("Welcome to reset password page\n");
                String userName = Utility.inputOutput("Enter your User name");
                resetPassword(userName);
                break;
            case "2": 
                System.out.println("\nRediredting to Dashboard\n");
                if (Utility.getCurrentUser().getRole().equals(UserRole.APPLICANT)) {
                    System.out.println("\nRedirecting to Applicant dashboard\n");
                    ApplicantService.getInstance().viewApplicantDashboard();
                } else {
                    System.out.println("\nRedirecting to Recruiter dashboard\n");
                    RecruiterService.getInstance().viewRecruiterDashboard();
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
        if (Utility.getCurrentUser().getUserName().equals(userName)) {
            String password = Utility.inputOutput("Enter your New Password");
            Utility.getCurrentUser().setPassword(password);
            for(User user: Utility.getUsers()) {
                if(user.getUserName().equals(userName)) {
                    user.setPassword(password);
                }
            }
            if (Utility.getCurrentUser().getRole() == UserRole.APPLICANT) {
                System.out.println("\nRedirecting to Applicant dashboard");
                ApplicantService.getInstance().viewApplicantDashboard();
            } else {
                System.out.println("\nRedirecting to Recruiter dashboard");
                RecruiterService.getInstance().viewRecruiterDashboard();
            }
        } else {
            System.out.println("\nYou have entered wrong Credentials\n");
            viewResetPasswordPage();
        }
    }

}

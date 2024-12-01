package service;

import model.AppState;
import model.Role;
import model.User;
import utility.Utility;

import java.util.ArrayList;

public class CommonService {

    private final Utility utility;

    public CommonService() {
        utility = new Utility();
    }

    public void accessLandingPage(ArrayList<User> users) {

        System.out.println("Welcome to the Landing Page\n");

        System.out.println("1. Sign In");
        System.out.println("2. Sign Up\n");

        switch (utility.inputOutput("Please Select One Of The Options")) {
            case "1": {
                viewSignInPage(users);
                break;
            }
            case "2": {
                viewSignUpPage(users);
                break;
            }
            default: {
                System.out.println("You entered invalid option");
                accessLandingPage(users);
                break;
            }
        }

    }

    public void viewSignInPage(ArrayList<User> users) {
        System.out.println("Welcome to Sign-in page\n");
        System.out.println("1. Continue to Sign-in\n");
        System.out.println("2. Go back to Landing page\n");

        switch (utility.inputOutput("Please Select One Of The Options")) {
            case "1":
                System.out.println("Welcome to Sign-in page\n");
                String userName = utility.inputOutput("Enter your User name");
                String password = utility.inputOutput("Enter your password");
                signIn(users, userName, password);
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

    public User authenticateUser(ArrayList<User> users, String userName, String password) {
        for (User user : users) {
            if (user.getUserName().equals(userName) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }


    public void signIn(ArrayList<User> users, String userName, String password) {
        System.out.println("Welcome to Sign-in page\n");
        User user = authenticateUser(users, userName, password);
        if (user == null) {
            System.out.println("Invalid User name or password\n");
            viewSignInPage(users);
            return;
        }
        AppState.getInstance().setCurrentUser(user);
        user.greetUser();
        if (user.getRole() == Role.RECRUITER) {
            RecruiterService.getInstance().viewDashboard();
        } else {
//            TODO: Go to Applicant Dashboard
        }
    }

    public void viewSignUpPage(ArrayList<User> users) {
        String id;
        String firstName;
        String lastName;
        String userName;
        String password;
        String recruiterCode;
        System.out.println("Welcome to Sign Up page\n");
        System.out.println("1. Signup as Applicant\n");
        System.out.println("2. Signup as Recruiter\n");
        System.out.println("3. Go back to Landing page\n");

        switch (utility.inputOutput("Please Select One Of The Options")) {
            case "1":
                System.out.println("Welcome to Applicant Signup page \n");
                id = "111";
                firstName = utility.inputOutput("Enter your first name");
                lastName = utility.inputOutput("Enter your last name");
                userName = utility.inputOutput("Create a user name");
                password = utility.inputOutput("Create a strong password");
                signUp("Applicant", users, id, firstName, lastName, userName, password);
                break;
            case "2":
                System.out.println("Welcome to Recruiter Signup page \n");
                if (utility.inputOutput("Enter the Recruiter Code").equals("ABCD")) {
                    id = "111";
                    firstName = utility.inputOutput("Enter your first name");
                    lastName = utility.inputOutput("Enter your last name");
                    userName = utility.inputOutput("Create a user name");
                    password = utility.inputOutput("Create a strong password");
                    signUp("Recruiter", users, id, firstName, lastName, userName, password);
                } else {
                    System.out.println("\nIncorrect Recruiter Code\n");
                    viewSignUpPage(users);
                }
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

    public void signUp(String role, ArrayList<User> users, String id, String firstName, String lastName, String userName, String password) {
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

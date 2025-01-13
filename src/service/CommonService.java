package service;

import model.User;
import utility.Utility;

import java.util.ArrayList;

public class CommonService {

    private static CommonService instance = null;

    public static CommonService getInstance() {
        if (instance == null) {
            instance = new CommonService();
        }
        return instance;
    }

    // ETY1 - STORY 1
    public void accessLandingPage() {

        System.out.println("Welcome to the Landing Page\n");
        System.out.println("Are you Recruiter or Applicant?");

        System.out.println("1. Recruiter");
        System.out.println("2. Applicant");
        System.out.println("3. Exit");

        switch (Utility.inputOutput("Please Select One Of The Options")) {
            case "1": {
                System.out.println("Directing to Recruiter Landing Page");
                RecruiterService.getInstance().visitSignInSignUpPageRecruiter();
                break;
            }
            case "2": {
                System.out.println("Directing to Applicant Landing Page");
                ApplicantService.getInstance().applicantViewSignInSignUpPage();
                break;
            }
            case "3": {
                System.out.println("Exiting the application");
                break;
            }
            default: {
                System.out.println("You entered invalid option");
                accessLandingPage();
                break;
            }
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

}

package service;

import model.*;
import utility.Utility;

import java.util.ArrayList;

public class ApplicantService {

    private static ApplicantService instance = null;

    public static ApplicantService getInstance() {
        if (instance == null) {
            instance = new ApplicantService();
        }
        return instance;
    }


    public void submitAssessmentForm() {
    }

    public void viewAssessment() {

    }

    public void submitInterviewForm() {

    }

    public void viewInterview() {

    }

    public void viewFeedback() {

    }

    public void viewJobPost() {

    }

    public void viewApplicationForm() {

    }

    public void viewAllAvailableJobs() {

    }

    public void submitApplicationForm() {

    }

    public void viewApplicantDashboard() {

        System.out.println("Welcome to the Applicant Dashboard\n");

        System.out.println("1. View Profile");
        System.out.println("2. View Applications");
        System.out.println("3. View Available Jobs");

        System.out.println("\n4. Log Out");
        switch (Utility.inputOutput("Please Select One Of The Options")) {
            case "1": {
                System.out.println("Directing to Profile Page...");
                viewApplicantProfilePage();
                break;
            }
            case "2": {
                System.out.println("Directing to Applications Page...");
                viewApplicantApplications();
                break;
            }
            case "3": {
                System.out.println("Directing to Available Jobs Page...");
                viewAllAvailableJobs();
                break;
            }
            case "4": {
                System.out.println("Logging Out...");
                CommonService.getInstance().logOut();
                break;
            }
            default: {
                System.out.println("You entered invalid option");
                viewApplicantDashboard();
                break;
            }
        }

    }

    public void viewApplicantProfilePage() {
        User user = Utility.getCurrentUser();
        System.out.println("\nApplicant Profile\n");

        if (user.getId() == null || user.getId().isEmpty()) {
            System.out.println("ID is missing.");
        } else {
            System.out.println("ID: " + user.getId());
        }

        if (user.getName() == null || user.getName().isEmpty()) {
            System.out.println("Name is missing.");
        } else {
            System.out.println("Name: " + user.getName());
        }

        if (user.getLastName() == null || user.getLastName().isEmpty()) {
            System.out.println("Last Name is missing.");
        } else {
            System.out.println("Last Name: " + user.getLastName());
        }

        if (user.getUserName() == null || user.getUserName().isEmpty()) {
            System.out.println("Username is missing.");
        } else {
            System.out.println("Username: " + user.getUserName());
        }

        if (user.getRole() == null) {
            System.out.println("Role is missing.");
        } else {
            System.out.println("Role: " + user.getRole().name());
        }
        String answer = Utility.inputOutput("Type anything to go back to the dashboard?");

        if (!answer.isEmpty()) {
            viewApplicantDashboard();
        }
    }

    public void deleteProfileHelper() {
        User user = Utility.getCurrentUser();
        ArrayList<User> users = Utility.getUsers();
        users.removeIf(u -> u.getId().equals(user.getId()));
        Utility.setUsers(users);
        Utility.setCurrentUser(null);
    }

    public void deleteApplicantProfile() {
        String input = Utility.inputOutput("Are you sure you want to delete your profile? (Y/N)");
        if (input.equalsIgnoreCase("Y")) {
            System.out.println("Deleting Applicant Profile...");
            deleteProfileHelper();
            CommonService.getInstance().accessLandingPage();
        } else if (input.equalsIgnoreCase("N")) {
            System.out.println("Profile deletion cancelled.");
            viewApplicantProfilePage();
        } else {
            System.out.println("Invalid input. Profile deletion cancelled.");
        }
    }

    public void updateProfile(User user) {
//        Filter the user from the list and update the user
        ArrayList<User> users = Utility.getUsers();
//        Remove the user from the list
        users.removeIf(u -> u.getId().equals(user.getId()));
//        Add the updated user to the list
        users.add(user);
//        Update the current user
        Utility.setCurrentUser(user);
    }

    public void showUpdateProfilePage() {
        System.out.println("Welcome to Update profile page\n");
        User currentUser = Utility.getCurrentUser();
        String name = Utility.inputOutput("Enter your name: ");
        if (name != null && !name.isEmpty()) {
            currentUser.setName(name);
        }
        String lastName = Utility.inputOutput("Enter your last name: ");
        if (lastName != null && !lastName.isEmpty()) {
            currentUser.setLastName(lastName);
        }
        System.out.println("Profile updated successfully\n");
        updateProfile(currentUser);
    }

    public void viewApplicantApplications() {
        User user = Utility.getCurrentUser();
        if (user instanceof Recruiter) {
            System.out.println("You are not an applicant.");
            return;
        }
        ArrayList<Application> applications = ((Applicant) user).getApplications();
        if (applications == null || applications.isEmpty()) {
            System.out.println("No applications found.");
            return;
        }
        ArrayList<Job> jobs = Utility.getJobs();
        System.out.println("Applications:");
        System.out.println();
        for (Application application : applications) {
            Job job = jobs.stream().filter(j -> j.getId().equals(application.getJobId())).findFirst().orElse(null);
            if (job == null) {
                System.out.println("Job not found for application: " + application.getId());
                continue;
            }

            System.out.println("Job Title: " + job.getJobName());
            System.out.println("Job Description: " + job.getJobDescription());
            System.out.println("Status: " + application.getStatus());
            System.out.println("Application ID: " + application.getId());
            System.out.println();
        }
    }

    public void viewSpecificApplication() {

    }

    public void withdrawApplication() {

    }

    public void viewJobDescFromApplication() {

    }

    public void viewApplicationProcessDashboard() {
    }

}
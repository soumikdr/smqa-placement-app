package service;

import model.Job;
import model.User;
import utility.Utility;

import java.util.List;

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
        User user = Utility.getCurrentUser();
        if (user == null || !"Applicant".equalsIgnoreCase(user.getRole())) {
            System.out.println("You are not authorized to view this page");
            return;
        }

        String jobId = Utility.inputOutput("Enter the Job ID to view the details:");
        List<Job> jobs = Utility.getJobs();
        Job job = jobs.stream()
                .filter(j -> j.getId().equals(jobId))
                .findFirst()
                .orElse(null);

        if (job == null) {
            System.out.println("Job with ID " + jobId + " not found.");
            return;
        }
        System.out.println("Welcome to the Job Post\n");
        System.out.println("Job ID: " + job.getId());
        System.out.println("Job Title: " + job.getJobName());
        System.out.println("Job Description: " + job.getJobDescription());
        System.out.println("Job Status: " + job.getJobStatus());
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

        if (user.getRole() == null || user.getRole().isEmpty()) {
            System.out.println("Role is missing.");
        } else {
            System.out.println("Role: " + user.getRole());
        }

        String answer = Utility.inputOutput("Type anything to go back to the dashboard?");

        if (!answer.isEmpty()) {
            viewApplicantDashboard();
        }
    }

    public void deleteApplicantProfile() {

    }

    public void updateApplicantProfile() {

    }

    public void viewApplicantApplications() {

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
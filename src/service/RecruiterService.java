package service;

import model.Job;
import model.JobStatus;
import utility.Utility;

import java.util.UUID;

public class RecruiterService {

    private static RecruiterService instance = null;

    public static RecruiterService getInstance() {
        if (instance == null) {
            instance = new RecruiterService();
        }
        return instance;
    }

    public void sendAssessment() {
    }

    public void viewAssessmentResult() {

    }

    public void sendInterview() {

    }

    public void viewInterviewResult() {

    }

    public void viewRecruiterDashboard() {
        System.out.println("Welcome to Recruiter Dashboard\n");
    }

    public void viewRecruiterProfilePage() {
        System.out.println("\nWelcome to your profile page\n");
        System.out.println("\nFirst Name: " + Utility.getCurrentUser().getName() + "\n");
        System.out.println("\nLast Name: " + Utility.getCurrentUser().getLastName() + "\n");
        System.out.println("\nUser Name: " + Utility.getCurrentUser().getUserName() + "\n");
        System.out.println("\nRole: " + Utility.getCurrentUser().getRole());

        System.out.println("\n1: Update your profile\n");
        System.out.println("\n2: Delete your profile\n");
        System.out.println("\n3: Go back to dashboard\n");

        switch (Utility.inputOutput("Please Select One Of The Options")) {
            case "1":
                System.out.println("Welcome to Update profile page\n");
                updateRecruiterProfile();
                break;
            case "2":
                System.out.println("Welcome to Delete profile page\n");
                deleteRecruiterProfile();
                break;
            default:
                System.out.println("You entered invalid option");
                viewRecruiterProfilePage();
                break;
        }

    }

    public void deleteRecruiterProfile() {

    }

    public void updateRecruiterProfile() {

    }

    public void viewAvailableJobs() {

    }

    public void viewSpecificJobPost() {

    }

    public void updateDescriptionOfJobPost() {

    }

    public void updateStatusOfJobPost() {

    }

    public void viewTotalNumberOfApplications() {

    }

    public void viewJobPostingForm() {

    }

    public void submitNewJobPost(String jobName, String jobDescription) {
        String id = UUID.randomUUID().toString();
        Job job = new Job(
                id,
                jobName,
                jobDescription,
                JobStatus.PUBLIC
        );
        Utility.addJob(job);
        System.out.println("Job posted successfully");
    }

    public void viewAllApplications() {

    }

    public void viewSpecificApplication() {

    }

    public void sendFeedback() {

    }

    public void viewFeedbackForm() {

    }


}
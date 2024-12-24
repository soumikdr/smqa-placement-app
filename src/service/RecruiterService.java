package service;

import model.Job;
import utility.Utility;

import java.util.ArrayList;
import java.util.Objects;

public class RecruiterService {

    private static RecruiterService instance = null;

    public static RecruiterService getInstance() {
        if (instance == null) {
            instance = new RecruiterService();
        }
        return instance;
    }

    public void sendAssessment(){
    }

    public void viewAssessmentResult() {

    }

    public void sendInterview() {

    }

    public void viewInterviewResult() {

    }

    public void viewRecruiterDashboard(){
        System.out.println("Welcome to Recruiter Dashboard\n");

        System.out.println("1. View profile");
        System.out.println("2. View available jobs");
        System.out.println("3. View available applications");
        System.out.println("4. Post a new job");
        System.out.println("5. Logout");

        switch(Utility.inputOutput("\nPlease select one option to proceed..")){
            case "1":
                viewRecruiterProfilePage();
                break;
            case "2":
                viewAvailableJobs();
                break;
            case "3":
                viewAllApplications();
                break;
            case "4":
                viewJobPostingForm();
                break;
            case "5":
                logout();
                break;
            default:
                System.out.println("\nYou have entered an invalid option. Please try again.\n");
                viewRecruiterDashboard();
                break;
        }
    }

    public void viewRecruiterProfilePage(){
        System.out.println("\nWelcome to your profile page\n");
        System.out.println("\nFirst Name: " + Utility.getCurrentUser().getName() + "\n");
        System.out.println("\nLast Name: " + Utility.getCurrentUser().getLastName() + "\n");
        System.out.println("\nUser Name: " + Utility.getCurrentUser().getUserName() + "\n");
        System.out.println("\nRole: " + Utility.getCurrentUser().getRole());

        System.out.println("\n1: Update your profile\n");
        System.out.println("\n2: Delete your profile\n");
        System.out.println("\n3: Go back to dashboard\n");

        switch(Utility.inputOutput("Please Select One Of The Options")){
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

    public void deleteRecruiterProfile(){

    }
    public void updateRecruiterProfile(){

    }

    public void viewAvailableJobs() {

    }

    public void viewSpecificJobPost() {

    }

    public void updateDescriptionOfJobPost() {

    }

    public void updateStatusOfJobPost(String jobId) {
        ArrayList<Job> jobs = Utility.getJobs();
        if (jobs == null || jobs.isEmpty()) {
            System.out.println("No jobs available");
            return;
        }
        for (Job job : jobs) {
            if (job.getId().equals(jobId)) {
                System.out.println("Current status of job: " + job.getJobStatus());
                String newStatus = Objects.equals(job.getJobStatus(), "Private") ? "Public" : "Private";
                job.setJobStatus(newStatus);
                System.out.println("Status of job updated successfully");
                return;
            }
        }
        System.out.println("No job post available with given id");
    }

    public void viewTotalNumberOfApplications() {

    }

    public void viewJobPostingForm() {

    }

    public void submitNewJobPost() {

    }

    public void viewAllApplications() {

    }

    public void viewSpecificApplication() {

    }

    public void sendFeedback() {

    }

    public void viewFeedbackForm() {

    }

    public void logout() {

    }
}
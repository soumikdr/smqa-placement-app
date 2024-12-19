package service;

import model.Job;
import model.User;
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
        System.out.println("\nUpdate profile information (leave empty for no change)\n");

        String firstName = Utility.inputOutput("Enter new first name: ");
        String lastName = Utility.inputOutput("Enter new last name: ");

        if (!firstName.isEmpty()) {
            Utility.getCurrentUser().setName(firstName);
        }
        
        if (!lastName.isEmpty()) {
            Utility.getCurrentUser().setLastName(lastName);
        }

        boolean uniqueUsername = false;

        while (!uniqueUsername) {
            String userName = Utility.inputOutput("Enter new username: ");

            if (Utility.getUsers().stream().filter(u -> u.getUserName().equals(userName)).findFirst().orElse(null) == null) {
                uniqueUsername = true;
                Utility.getCurrentUser().setUserName(userName);
            } else {
                System.out.println("Username already exists. Please try again.");
            }
        }

        System.out.println("\nProfile updated successfully!\n");
        viewRecruiterProfilePage();
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

}
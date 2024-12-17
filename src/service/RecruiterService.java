package service;

import model.Job;
import utility.Utility;

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
                System.out.println("Redirecting to update profile page...\n");
                updateRecruiterProfile();
                break;
            case "2":
                System.out.println("Redirecting to delete profile page...\n");
                deleteRecruiterProfile();
                break;
            case "3":
            System.out.println("Redirecting to dashboard...\n");
                viewRecruiterDashboard();
                 break;
            default:
                System.out.println("You entered invalid option\n");
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
        System.out.println("Welocme to Specific Job Post Ddtails\n");
        String jobId = Utility.inputOutput("\nEnter the Job Id\n");
        Boolean invalidJobId = true;

        for (Job job: Utility.getJobs()) {
            if (job.getId().equals(jobId)) {
                System.out.println("\nJob ID: " + job.getId());
                System.out.println("\nJob Name: " + job.getJobName());
                System.out.println("\nJob Description: " + job.getJobDescription());
                System.out.println("\nJob Status: " + job.getJobStatus());
                invalidJobId = false;
                break;
            }
        }

        if (invalidJobId) {
            System.out.println("\nYou have entered a invalid Job id\n");
        }

        System.out.println("\n1: View another job details\n");
        System.out.println("\n2: Go back to dashboard\n");

        switch(Utility.inputOutput("Please Select One Of The Options")){
            case "1":
                System.out.println("Redirecting to view specific job details \n");
                viewSpecificJobPost();
                break;
            case "2":
                System.out.println("Redirecting to dashboard\n");
                viewRecruiterDashboard();
                break;
            default:
                System.out.println("You entered invalid option");
                viewRecruiterDashboard();
                break;
        }

    }

    public void updateDescriptionOfJobPost() {

    }

    public void updateStatusOfJobPost() {

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
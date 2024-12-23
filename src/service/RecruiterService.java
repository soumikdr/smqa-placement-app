package service;

import model.Application;
import model.Assignment;
import model.Job;
import model.JobStatus;
import utility.Utility;

import java.util.ArrayList;
import java.util.Objects;
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

    public void viewAssessmentResult(String applicationId, String assignmentId) {
    	
    	Assignment result=new Assignment();
    	
    	for(Application a: Utility.getApplications()) {
    		if(a.getId().equals(applicationId)) {
    			for(Assignment assign: a.getAssignments()) {
    				if(assign.getId().equals(assignmentId)) {
    					result=assign;
    					System.out.println("Assessment Found..");
    				}
    			}
    		}
    	}
    	if(result.getId()==null || result.getId().isEmpty()) {
    		System.out.println("There is no Coding Assessment Result for this application");
    	}else {
        	int answerCount=0;
        	System.out.println("Assessment Questions and Answers : ");
        	for(String question: result.getQuestions()) {
        		System.out.println(question+"\n");
        		
        		System.out.println(result.getAnswers().get(answerCount)+"\n");
        		answerCount++;
     
        	}
    	}
    	
    	System.out.println("Directing to Application Page..");
    	viewSpecificApplication();
    	

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

    public void deleteRecruiterProfile() {

    }

    public void updateRecruiterProfile() {

    }

    public void viewAvailableJobs() {

    }

    public void viewSpecificJobPost() {
        System.out.println("Welocme to Specific Job Post Details\n");
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
        } else {
            System.out.println("\n1: Update job description\n");
            System.out.println("\n2: Update job status\n");
            System.out.println("\n3: View total applications for this job\n");
            System.out.println("\n4: Continue to main menu\n");

            switch(Utility.inputOutput("Please Select One Of The Options")){
                case "1":
                    System.out.println("Redirecting to update job description page \n");
                    updateDescriptionOfJobPost(jobId);
                    break;
                case "2":
                    System.out.println("Redirecting to total applications for the job\n");
                    updateStatusOfJobPost(jobId);
                    break;
                case "3":
                    System.out.println("Redirecting to dashboard\n");
                    viewTotalNumberOfApplications(jobId);
                    break;
                case "4":
                    System.out.println("Redirecting to main menu\n");
                    break;
                default:
                    System.out.println("You entered invalid option");
                    viewRecruiterDashboard();
                    break;
            }
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

    public void updateDescriptionOfJobPost(String jobId) {

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
                JobStatus newStatus = Objects.equals(job.getJobStatus(), JobStatus.PRIVATE) ? JobStatus.PUBLIC : JobStatus.PRIVATE;
                job.setJobStatus(newStatus);
                System.out.println("Status of job updated successfully");
                return;
            }
        }
        System.out.println("No job post available with given id");
    }

    public void viewTotalNumberOfApplications(String jobId) {

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
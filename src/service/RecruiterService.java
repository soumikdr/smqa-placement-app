package service;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

import model.Application;
import model.Assignment;
import model.Job;
import model.JobStatus;
import model.User;
import utility.Utility;

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
    	viewSpecificApplication(applicationId);
    	

    }

    public void sendInterview(Application application) {

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

    public void approveRejectApplication(Application application) {

    }

    public void deleteRecruiterProfile(){

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

    public void viewSpecificApplication(String applicationId) {
        User userApplicant = null;
        Application application = null;
        ArrayList<Application> allApplications = Utility.getApplications();
        ArrayList<User> allUsers = Utility.getUsers();

        for (Application app : allApplications) {
            if (app.getId().equals(applicationId)) {
                application = app;
            }
        }

        if (application == null) {
            System.out.println("No application found with given id");
            viewAllApplications();
            return;
        }

        for (User user : allUsers) {
            if (user.getId().equals(application.getApplicantId())) {
                userApplicant = user;
            }
        }

        if (userApplicant == null) {
            System.out.println("No applicant found with for this application");
            viewAllApplications();
            return;
        }

        System.out.println("Application ID: " + application.getId());
        System.out.println("Job ID: " + application.getJobId());
        System.out.println("Applicant ID: " + application.getApplicantId());
        System.out.println("Applicant Name: " + userApplicant.getName() + " " + userApplicant.getLastName());
        System.out.println("Status: " + application.getStatus());
        System.out.println("Assignments found: " + application.getAssignments().size());

        for (int i = 0; i < application.getAssignments().size(); i++) {
            System.out.println("\nAssignment " + i + ": " + application.getAssignments().get(i).getAssignmentName());
            System.out.println("Questions: ");

            for (int j = 0; j < application.getAssignments().get(i).getQuestions().size(); j++) {
                System.out.println(application.getAssignments().get(i).getQuestions().get(j));
            }

            System.out.println("Answers: ");
            
            for (int j = 0; j < application.getAssignments().get(i).getAnswers().size(); j++) {
                System.out.println(application.getAssignments().get(i).getAnswers().get(j));
            }
        }

        System.out.println("\n1: Update status of application");
        System.out.println("2: Send an assignment");
        System.out.println("3: Send interview questions");
        System.out.println("4: Send feedback");
        System.out.println("5: Go back to All Applications");

        switch (Utility.inputOutput("\nPlease select one of the options..")) {
            case "1":
                approveRejectApplication(application);
                break;
            case "2":
                sendAssignment(application);
                break;
            case "3":
                sendInterview(application);
                break;
            case "4":
                sendFeedback();
                break;
            case "5":
                viewAllApplications();
                break;
            default:
                System.out.println("You entered invalid option");
                viewSpecificApplication(applicationId);
                break;
        }        
    }

    public void sendAssignment(Application application) {
        
    }

    public void sendFeedback() {

    }

    public void viewFeedbackForm() {

    }


}
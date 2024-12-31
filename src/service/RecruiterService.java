package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import model.*;
import utility.Utility;
import java.util.concurrent.atomic.AtomicInteger;

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
                logoutRecruiter();
                break;
            default:
                System.out.println("\nYou have entered an invalid option. Please try again.\n");
                viewRecruiterDashboard();
                break;
        }
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

    	System.out.println("Do you want to Approve/Reject Application?");

    	System.out.println("1. Approve The Application");
    	System.out.print("2. Reject The Application\n");

    	System.out.println("0. Go Back to Application Page");

    	String selection=Utility.inputOutput("Please Select One Of The Options");

    	switch (selection) {
		case "1": {
              application.setStatus("Approved");
              Utility.getApplications().stream().map(a -> {
            	  if(a.getId().equals(application.getId())) {
            		  a.setStatus(application.getStatus());
            	  }
            	  return a;
              });
              System.out.println("Application Approved");
              System.out.println("Directing to Application's Page");

              //has to redirect to application page
              viewSpecificApplication(application.getId());
               break;
		}
		case "2":{
            application.setStatus("Rejected");
            Utility.getApplications().stream().map(a -> {
          	  if(a.getId().equals(application.getId())) {
          		  a.setStatus(application.getStatus());
          	  }
          	  return a;
            });
            System.out.println("Application Rejected");
            System.out.println("Directing to Application's Page");
            //has to redirect to application page
            viewSpecificApplication(application.getId());
            break;
		}
		case "3":{
			viewSpecificApplication(application.getId());
            //has to redirect to application page
            System.out.println("Directing to Application's Page");
            break;
		}
		default:
            System.out.println("You entered invalid option");
            approveRejectApplication(application);
            break;
		}

    }

    public void deleteRecruiterProfile(){
        System.out.println("Deleting your profile...");
        String userName = Utility.getCurrentUser().getUserName();
        Utility.getUsers().removeIf(user -> user.getUserName().equals(userName));
        Utility.setCurrentUser(null);
        System.out.println("Profile deleted successfully");
        commonService.accessLandingPage();
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

            if (Utility.getUsers().stream().filter(u -> u.getUserName().equals(userName)).findFirst().orElse(null) == null && !userName.isEmpty()) {
                uniqueUsername = true;
                Utility.getCurrentUser().setUserName(userName);
            } else {
                System.out.println("Provided username is empty or already exists. Please try again.");
            }
        }

        System.out.println("\nProfile updated successfully!\n");
        viewRecruiterProfilePage();
    }

    public void viewAvailableJobs() {
        ArrayList<Job> jobs = Utility.getJobs();
        if (jobs == null || jobs.isEmpty()) {
            System.out.println("No jobs available");
            return;
        }
        System.out.println("Available Jobs");
        int index = 1;
        for (Job job : jobs) {
            System.out.println(index + ". Job ID: " + job.getId() + " | Job Title: " + job.getJobName());
            index++;
        }
        viewSpecificJobPost();
    }

    public void viewSpecificJobPost() {
        System.out.println("Welocme to Specific Job Post Details\n");
        String jobId = Utility.inputOutput("\nEnter the Job Id\n");
        Boolean invalidJobId = true;

        for (Job job : Utility.getJobs()) {
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

        switch (Utility.inputOutput("Please Select One Of The Options")) {
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
        System.out.println("\n-------- Update job description --------\n");
        ArrayList<Job> jobs = Utility.getJobs();
        Job job = null;

        if (jobs == null || jobs.isEmpty()) {
            System.out.println("No jobs available");
            return;
        }

        for (Job j : jobs) {
            if (j.getId().equals(jobId)) {
                job = j;
            }
        }

        if (job == null) {
            System.out.println("No job post available with given id");
            viewAvailableJobs();
            return;
        }

        System.out.println("Current title of job: " + job.getJobName());
        System.out.println("Current description of job: " + job.getJobDescription());

        System.out.println("\n");
        String newName = Utility.inputOutput("Enter new job title..");
        String newDescription = Utility.inputOutput("Enter new job description..");
        System.out.println("\n");

        if (newName.isEmpty()) {
            System.out.println("Job title not updated as it is empty");
        } else {
            job.setJobName(newName);
            System.out.println("Job title updated successfully");
        }

        if (newDescription.isEmpty()) {
            System.out.println("Job description not updated as it is empty");
        } else {
            job.setJobDescription(newDescription);
            System.out.println("Job description updated successfully");
        }

        System.out.println("\n");
        viewAvailableJobs();
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

        AtomicInteger total= new AtomicInteger();
        Utility.getApplications().stream().forEach(application -> {
            if(application.getJobId().equals(jobId)){
                total.getAndIncrement();
            }
        });

        System.out.println("Total Applications of : "+jobId+" is " + total);
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
        User user = Utility.getCurrentUser();
        if(user instanceof Applicant)
        {
            System.out.println("You are not authorized to view this page");
            return;
        }
        ArrayList<Application> applications = Utility.getApplications();
        if(applications == null || applications.isEmpty())
        {
            System.out.println("No applications available.");
            return;
        }

        applications.stream().filter(application -> ApplicationStatus.INPROGRESS.equals(application.getStatus())).
                forEach(application -> {
            System.out.println("Application ID: " + application.getId() + "|"+"Status: " + application.getStatus()+"|"+"Applicant ID: " + application.getApplicantId());
        });

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
        System.out.println("Feedback: " + application.getFeedback());
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
        String answer = Utility.inputOutput("Please select one of the options..");

        switch (answer) {
            case "1" -> approveRejectApplication(application);
            case "2" -> sendAssignment(application);
            case "3" -> sendInterview(application);
            case "4" -> sendFeedback(application);
            case "5" -> viewAllApplications();
            default -> {
                System.out.println("You entered invalid option");
                viewSpecificApplication(applicationId);
                return;
            }
        }
    }

    public void sendAssignment(Application application) {
        System.out.println("\n-------- Send assignment to applicant --------\n");
        System.out.println("Please type one from the roles below to send the assignment questions (e.g. frontend)");

        Map<String, List<String>> questionMap = Utility.getQuestionMap();
        System.out.println(questionMap.keySet().toString());
        String roleInput = Utility.inputOutput("\nType here..");
        List<String> questions = questionMap.get(roleInput);

        if (questions == null) {
            System.out.println("\nNo questions found for the given role\n");
            viewSpecificApplication(application.getId());
            return;
        }

        Assignment newAssignment = new Assignment(UUID.randomUUID().toString(), application.getApplicantId(), "Assignment " + roleInput, new ArrayList<String>(questions), new ArrayList<>());
        ArrayList <Assignment> assignments = application.getAssignments();
        assignments.add(newAssignment);
        application.setAssignments(assignments);

        System.out.println("\nAssignment sent successfully.\n");
        viewSpecificApplication(application.getId());
    }

    public void sendFeedback(Application application) {
        System.out.println("\n-------- Send feedback to applicant --------\n");
        String feedback = Utility.inputOutput("Please type your feedback here..");

        if (feedback.isEmpty()) {
            System.out.println("Feedback not updated as it is empty");
            return;
        }

        application.setFeedback(feedback);
        System.out.println("\nFeedback sent successfully\n");

        viewSpecificApplication(application.getId());
    }

    public void viewSubmittedAnswers(String applicationId) {
        System.out.println("\nWelcome to view submitted answers for the application " + applicationId);
        ArrayList<String> questions = new ArrayList<String>();
        ArrayList<String> answers = new ArrayList<String>();

        for (Application application: Utility.getApplications()) {
            if (application.getId().equals(applicationId)) {
                for (Assignment assignment : application.getAssignments()) {
                    questions=assignment.getQuestions();
                    answers=assignment.getAnswers();
                    for(int i=0; i<questions.size(); i++) {
                        System.out.println("\nQuestion: \n");
                        System.out.println(questions.get(i));
                        System.out.println("Answer: \n");
                        System.out.println(answers.get(i));
                    }
                }
            }
        }
    }

    public void logoutRecruiter() {
        System.out.println("Initiating logout process for Recruiter");
        Utility.setCurrentUser(null);
        System.out.println("You have been logged out successfully.");
        CommonService.getInstance().accessLandingPage();
    }

    public void visitSignInSignUpPageRecruiter() {
        System.out.println("Welcome to the Sign In/Sign Up page for Recruiter\n");
        System.out.println("1. Sign In for Recruiter");
        System.out.println("2. Sign Up for Recruiter");
        System.out.println("3. Go back to the previous menu");

        switch (Utility.inputOutput("Please select one of the options")) {
            case "1":
                System.out.println("Redirecting to Sign In page for Recruiter\n");
                recruiterSignInPage();
                break;
            case "2":
                System.out.println("Redirecting to Sign Up page for Recruiter\n");
                recruiterSignUpPage();
                break;
            case "3":
                System.out.println("Redirecting to the previous menu\n");
                CommonService.getInstance().accessLandingPage();
                break;
            default:
                System.out.println("You entered an invalid option. Please try again.\n");
                visitSignInSignUpPageRecruiter();
                break;
        }
    }

    
    public void recruiterSignUpPage() {
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

    public void recruiterSignInPage() {
        ArrayList<User> users = Utility.getUsers();

        String userName = Utility.inputOutput("Enter your User name:");
        String password = Utility.inputOutput("Enter your password:");
        User recruiter = authenticateUser(users, userName, password);

        if (recruiter == null || recruiter.getRole() != UserRole.RECRUITER) {
            System.out.println("\n");
            String tryAgain = Utility.inputOutput("\nInvalid username or password. Do you want to try again? (y/n)");
            
            if (tryAgain.equals("y")) {
                recruiterSignInPage();
            } else {
                CommonService commonService = CommonService.getInstance();
                commonService.accessLandingPage();
            }
        } else {
            System.out.println("\nRecruiter Signin successful. proceeding to applicant dashboard.. \n");
            Utility.setCurrentUser(recruiter);
            ApplicantService applicantService = new ApplicantService();
            applicantService.viewApplicantDashboard();
        }
    }
}
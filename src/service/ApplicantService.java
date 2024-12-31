package service;

import model.*;
import utility.Utility;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

public class ApplicantService {

    private static ApplicantService instance = null;

    public static ApplicantService getInstance() {
        if (instance == null) {
            instance = new ApplicantService();
        }
        return instance;
    }

     public void applicantViewSignInSignUpPage() {
        System.out.println("Welcome to Applicant Landing Pagen");
        System.out.println("1. Sign In\n");
        System.out.println("2. Sign Up\n");
        System.out.println("3: Go back to the previous menu");

        switch (Utility.inputOutput("Please Select One Of The Options")) {
            case "1":
                System.out.println("Redirecting to Applicant Sign In Page\n");
                signIn();
                break;
            case "2":
                System.out.println("\nRedirecting to Applicant Sign Up Page\n");
                signUp();
                break;
            case "3":
                System.out.println("\nRedirecting to previous menu\n");
                CommonService.getInstance().accessLandingPage();
                break;
            default:
                System.out.println("You entered invalid option");
                applicantViewSignInSignUpPage();
                break;
        }
    }

    public void signUp() {

    }

    public void signIn() {
        ArrayList<User> users = Utility.getUsers();
        User applicant = null;

        String userName = Utility.inputOutput("Enter your User name:");
        String password = Utility.inputOutput("Enter your password:");

        for (User user : users) {
            if (user.getUserName().equals(userName) && user.getPassword().equals(password) && user.getRole() == UserRole.APPLICANT) {
                applicant = user;
            }
        }

        if (applicant == null) {
            System.out.println("\n");
            String tryAgain = Utility.inputOutput("\nInvalid username or password. Do you want to try again? (y/n)");
            
            if (tryAgain.equals("y")) {
                signIn();
            } else {
                CommonService commonService = CommonService.getInstance();
                commonService.accessLandingPage();
            }
        } else {
            System.out.println("\nApplicant Signin successful. proceeding to applicant dashboard.. \n");
            Utility.setCurrentUser(applicant);
            ApplicantService applicantService = new ApplicantService();
            applicantService.viewApplicantDashboard();
        }
    }

    public void submitAssessmentForm(String applicationId) {
        System.out.println("\n------ Submit your assignments ------\n");
        Application application = null;

        for(Application app: Utility.getApplications()) {
            if (app.getId().equals(applicationId)) {
                application = app;
                break;
            }
        }

        if (application == null) {
            System.out.println("Application with given ID not found");
            return;
        }

        ArrayList<Assignment> assignments = application.getAssignments();

        // Check if there are any assignments for this application other than 'interview'
        if (assignments.size() < 2) {
            System.out.println("No assignments found for this application");
            viewApplicationProcessDashboard(applicationId);
            return;
        }

        for (Assignment assignment: assignments) {
            if (assignment.getAssignmentName().equals("interview")) {
                continue;
            }

            System.out.println("\nAssignment Name: " + assignment.getAssignmentName() + "\n");

            if (assignment.getStatus() == AssignmentStatus.SUBMITTED) {
                System.out.println("This assignment is already submitted");
                continue;
            }

            ArrayList<String> answers = new ArrayList<>();

            for (String question : assignment.getQuestions()) {
                System.out.println("Question: " + question);
                String answer = Utility.inputOutput("Type your answer here (include question number): ");
                answers.add(answer);
            }

            assignment.setAnswers(answers);
            assignment.setStatus(AssignmentStatus.SUBMITTED);
        }

        System.out.println("\nAll assignments submitted successfully\n");
        viewApplicationProcessDashboard(applicationId);
    }

    public void viewAssessment(String applicationId) {
        System.out.println("Welcome to the Assessment Page\n");

        for(Application application: Utility.getApplications()) {
            if (application.getId().equals(applicationId)) {
                if(application.getAssignments().isEmpty()) {
                    System.out.println("No assignments found");
                } else {
                    System.out.println("Assignments for " + applicationId);
                    for(Assignment assignment: application.getAssignments()) {
                        System.out.println("Assignment ID: " + assignment.getId()+"|" +"Assignment Name: " + assignment.getAssignmentName() +"|"+"Assignment Status: " + assignment.getStatus());
                    }
                    break;
                }
            }
        }
    }

    public void submitInterviewForm(String applicationId) {
        Application application = null;

        for(Application app: Utility.getApplications()) {
            if (app.getId().equals(applicationId)) {
                application = app;
                break;
            }
        }

        Assignment interview = null;

        for(Assignment assignment: application.getAssignments()) {
            if (assignment.getAssignmentName().equals("interview")) {
                interview = assignment;
                break;
            }
        }

        if (interview == null) {
            System.out.println("Interview not found for this application");
            return;
        }

    	System.out.println("Please answer questions to complete interview :");

    	for(String question:interview.getQuestions()) {


    		interview.getAnswers().add(Utility.inputOutput(question));

    	}
    	System.out.println("Interview completed.");

    	for(Assignment a: Utility.getAssignments())
    	{
    		if(a.getId().equals(interview.getId())) {
    			a.setAnswers(interview.getAnswers());
    	    	a.setStatus(AssignmentStatus.SUBMITTED);
    		}
    	}

    	System.out.println("Answers Submitted.");
    	System.out.println("Directing to application dashboard..");

        viewApplicationProcessDashboard(application.getId());
    }

    public void viewInterview(String applicationId) {
        System.out.println("\n------ View Interview Questions ------\n");
        Application application = null;

        for(Application app: Utility.getApplications()) {
            if (app.getId().equals(applicationId)) {
                application = app;
                break;
            }
        }

        if (application == null) {
            System.out.println("Application with given ID not found");
            return;
        }

        ArrayList<Assignment> assignments = application.getAssignments();

        for (Assignment assignment: assignments) {
            if (assignment.getAssignmentName().equals("interview")) {
                System.out.println("Questions are: ");

                for (String question: assignment.getQuestions()) {
                    System.out.println(question);
                }

                break;
            }
        }

        System.out.println("\nRedirecting back to Application Dashboard..\n");
        viewApplicationProcessDashboard(applicationId);
    }

    public void viewFeedback(String applicationId) {
        System.out.println("\nWelcoem to view feedback page");

        for(Application application: Utility.getApplications()) {
            if (application.getId().equals(applicationId)) {
                if(application.getFeedback().isEmpty()) {
                    System.out.println("\n Feedback not received");
                } else {
                    System.out.println("\nFeedback for " + applicationId);
                    System.out.println("\n" + application.getFeedback());
                }
            }
        }
        viewApplicationProcessDashboard(applicationId);
    }

    public void viewJobPost() {
        User user = Utility.getCurrentUser();
        if (user == null || user.getRole() != UserRole.APPLICANT) {
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

    public void viewApplicationForm(Job job) {
        System.out.println("Welcome to the Job Application Form\n");
        System.out.print("Enter your education: ");
        String education = Utility.inputOutput("Enter your education: ");
        System.out.print("Enter your experience: ");
        Integer experience = Integer.valueOf(Utility.inputOutput("Enter your experience: "));
        System.out.print("Enter your skills: ");
        String skills = Utility.inputOutput("Enter your skills: ");
        submitApplicationForm(job, education, experience, skills);
    }

    public void viewAllAvailableJobs() {
        User user = Utility.getCurrentUser();
        if (user == null || user.getRole() == null || user.getRole() != UserRole.APPLICANT) {
            System.out.println("You are not authorized to view this page");
            return;
        }
        System.out.println("Welcome to the Available Jobs Page\n");
        List<Job> jobs = Utility.getJobs();
        List<Job> openJobs = jobs.stream().filter(j -> JobStatus.PUBLIC.equals(j.getJobStatus())).toList();
        if (openJobs.isEmpty()) {
            System.out.println("No jobs available at the moment");
            return;
        }
        System.out.println("Available Jobs");
        openJobs.forEach(job -> {
            System.out.println("Job ID: " + job.getId()+ "|"+"Job Name: " + job.getJobName());
            System.out.println();
            });
        viewJobPost();
        }

    public void submitApplicationForm(String jobId, String applicantId) {

    	String applicationId=UUID.randomUUID().toString();
    	Application newApplication=new Application(applicationId,jobId, applicantId,"Submitted", new ArrayList<Assignment>());
    	Utility.getApplications().add(newApplication);

    	System.out.println("Application submitted : "+newApplication.getId());
    	System.out.println("Directing to Applicant Dashboard");

    	viewApplicantDashboard();

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

        Utility.inputOutput("Type anything to go back to the dashboard?");
        viewApplicantDashboard();
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
        System.out.println("Applications:");
        System.out.println();
        int index = 1;
        for (Application application : applications) {
            System.out.println(index + ". Application ID: " + application.getId() + " | " + "Status: " + application.getStatus());
            System.out.println();
            index++;
        }
        viewSpecificApplication();
    }

    public void viewSpecificApplication() {
        User user = Utility.getCurrentUser();

        System.out.println("\nWelcome to specific application details\n");

        String applicationId = Utility.inputOutput("\nEnter the Application Id\n");
        Boolean invalidApplicationId = true;

        for (Application application: Utility.getApplications()) {
            if (application.getId().equals(applicationId) && user.getId().equals(application.getApplicantId())) {
                System.out.println("\nApplication ID: " + application.getId());
                System.out.println("\nJob Id: " + application.getJobId());
                System.out.println("\nApplicant Id: " + application.getApplicantId());
                System.out.println("\nApplication Status: " + application.getStatus());
                System.out.println("\nAssignments: ");
                if (application.getAssignments().isEmpty()) {
                    System.out.println("\nNo assignemts for this applicaton");
                } else {
                    for(Assignment assignemt: application.getAssignments() ) {
                        System.out.println("\nAssignment Id: " + assignemt.getId());
                        System.out.println("\nApplicant Id: " + assignemt.getApplicantId());
                        System.out.println("\nAssignment Name: " + assignemt.getAssignmentName());
                        System.out.println("\nQuestions: ");
                        if (assignemt.getQuestions().isEmpty()) {
                            System.out.println("\nNo questions for this assignemt");
                        } else {
                            for(String question: assignemt.getQuestions()) {
                                System.out.println("\n" + question);
                            }
                        }
                        System.out.println("\nAnswers: ");
                        if (assignemt.getAnswers().isEmpty()) {
                            System.out.println("\nNo answers submitted for this assignemt");
                        } else {
                            for(String answer: assignemt.getAnswers()) {
                                System.out.println("\n" + answer);
                            }
                        }
                    }
                }
                if (application.getStatus().equals(ApplicationStatus.INPROGRESS)) {
                    System.out.println("\n1: Complete your application\n");
                    System.out.println("\n2: View Job Description");
                    System.out.println("\n3: Withdraw application");
                    System.out.println("\n4: Complete your application later\n");

                    switch(Utility.inputOutput("Please Select One Of The Options")) {
                        case "1":
                            System.out.println("\nRedirecting to application process dashboard");
                            viewApplicationProcessDashboard(applicationId);
                            break;
                        case "2":
                        System.out.println("\nComplete your application soon");
                        break;
                        case "3":
                            System.out.println("\nRedirecting to view job description page");
                            viewJobDescFromApplication(applicationId);
                            break;
                        case "4":
                            System.out.println("\nRedirecting to withdraw application page");
                            withdrawApplication(applicationId);
                            break;
                        default:
                            System.out.println("You entered invalid option");
                            break;
                    }
                }
                invalidApplicationId = false;
                break;
        }
        }

        if (invalidApplicationId) {
            System.out.println("\nYou have entered a invalid application id\n");
        }

        System.out.println("\n1: View another Application details\n");
        System.out.println("\n2: Go back to applications page\n");

        switch(Utility.inputOutput("Please Select One Of The Options")){
            case "1":
                System.out.println("Redirecting to view specific application details \n");
                viewSpecificApplication();
                break;
            case "2":
                System.out.println("Redirecting to applications page\n");
                viewApplicantApplications();
                break;
            default:
                System.out.println("You entered invalid option");
                viewApplicantApplications();
                break;
        }
    }

    public void withdrawApplication(String applicationId) {
        System.out.println("\nWelcome to withdraw application page");

        System.out.println("1: Confirm to withdraw application");
        System.out.println("2: Go back to applications page");

        switch(Utility.inputOutput("Please Select One Of The Options")){
            case "1":
                System.out.println("Withdrawing application \n");
                ArrayList<Application> applications = Utility.getApplications();
                applications.removeIf(application -> applicationId.equals(application.getId()));
                break;
            case "2":
                System.out.println("Redirecting to applications page\n");
                viewApplicantApplications();
                break;
            default:
                System.out.println("You entered invalid option");
                viewApplicantApplications();
                break;
        }
    }

    public void viewJobDescFromApplication(String applicationId) {
        Application application = Utility.getApplications().stream()
                .filter(a -> a.getId().equals(applicationId))
                .findFirst()
                .orElse(null);

        if (application == null) {
            System.out.println("Application with ID " + applicationId + " not found.");
            return;
        }
        Job job = Utility.getJobs().stream()
                .filter(j -> j.getId().equals(application.getJobId()))
                .findFirst()
                .orElse(null);

        if (job == null) {
            System.out.println("Job with ID " + application.getJobId() + " not found.");
            return;
        }
        System.out.println("Job ID: " + job.getId());
        System.out.println("Job Title: " + job.getJobName());
        System.out.println("Job Description: " + job.getJobDescription());
    }

    public void viewApplicationProcessDashboard(String applicationId) {

        System.out.println("\nWelcome to application process dashboard\n");


        System.out.println("1. View assignments");
        System.out.println("2. Submit assignment\n");
        System.out.println("3. View interview questions\n");
        System.out.println("4. Submit interview answers\n");
        System.out.println("5. View feedback\n");

        switch (Utility.inputOutput("Please Select One Of The Options")) {
            case "1": {
                System.out.println("Redirecting to View assignments page");
                viewAssessment(applicationId);
                break;
            }
            case "2": {
                System.out.println("Redirecting to submit assignments page");
                submitAssessmentForm(applicationId);
                break;
            }
            case "3": {
                System.out.println("Redirecting to view interview questions page");
                viewInterview(applicationId);
                break;
            }
            case "4": {
                System.out.println("Redirecting to submit interview answers page");
                submitInterviewForm(applicationId);
                break;
            }
            case "5": {
                System.out.println("Redirecting to view feedback page");
                viewFeedback(applicationId);
                break;
            }
            case "6": {
                System.out.println("Go back to Applications page");
                viewApplicantApplications();
                break;
            }
            default: {
                System.out.println("You entered invalid option");
                viewApplicationProcessDashboard(applicationId);
                break;
            }
        }

    }

}
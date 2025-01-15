package service;

import model.*;
import utility.Utility;

import java.util.ArrayList;
import java.util.List;
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
        System.out.println("\nWelcome to Applicant Landing Page");
        System.out.println("1. Sign In");
        System.out.println("2. Sign Up");
        System.out.println("3: Go back to the previous menu");

        switch (Utility.inputOutput("Please Select One Of The Options")) {
            case "1":
                System.out.println("Redirecting to Applicant Sign In Page");
                signIn();
                break;
            case "2":
                System.out.println("Redirecting to Applicant Sign Up Page");
                signUp();
                break;
            case "3":
                System.out.println("Redirecting to previous menu");
                CommonService.getInstance().accessLandingPage();
                break;
            default:
                System.out.println("You entered invalid option");
                applicantViewSignInSignUpPage();
                break;
        }
    }

    // UserStory:4; ar668
    public void signUp() {
        System.out.println("Welcome to Applicant Sign Up Page\n");
        System.out.println("Please enter the following details\n");
        String firstName = Utility.inputOutput("First Name: ");
        String lastName = Utility.inputOutput("Last Name: ");
        String userName = Utility.inputOutput("User Name: ");
        String password = Utility.inputOutput("Password: ");
        if (firstName == null || firstName.isEmpty() ||
                lastName == null || lastName.isEmpty() ||
                userName == null || userName.isEmpty() ||
                password == null || password.isEmpty()) {
            System.out.println("All fields are required. Please try again.");
            signUp();
            return;
        }
        String id = UUID.randomUUID().toString();
        User newUser = new Applicant(id, firstName, lastName, userName, password);
        Utility.getUsers().add(newUser);

        System.out.println("Sign Up Successful for Applicant");
        Utility.setCurrentUser(newUser);
        System.out.println("Directing to Applicant Dashboard");
        viewApplicantDashboard();
    }

    /*
     * Author: Soumik Datta (sd631)
     * User Story: 7
     */
    public void signIn() {
        ArrayList<User> users = Utility.getUsers();
        User applicant = null;

        String userName = Utility.inputOutput("Enter your User name:");
        String password = Utility.inputOutput("Enter your password:");

        for (User user : users) {
            if (user.getUserName().equals(userName) && user.getPassword().equals(password)
                    && user.getRole() == UserRole.APPLICANT) {
                applicant = user;
            }
        }

        if (applicant == null) {
            System.out.println("\nInvalid credentials or Not an Applicant.");
            String tryAgain = Utility.inputOutput("\nDo you want to try again? (y/n)");
            if (tryAgain.equals("y")) {
                signIn();
            } else {
                applicantViewSignInSignUpPage();
            }
        } else {
            System.out.println("\nApplicant Signin successful. proceeding to applicant dashboard.. \n");
            Utility.setCurrentUser(applicant);
            viewApplicantDashboard();
        }
    }

    /*
     * Author: Soumik Datta (sd631)
     * User Story: 41
     */
    public void submitAssessmentForm(String applicationId) {
        System.out.println("------ Submit your assignments ------\n");
        Application application = null;
        for (Application app : Utility.getApplications()) {
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

        for (Assignment assignment : assignments) {
            System.out.println("\nAssignment Name: " + assignment.getAssignmentName());
            if (assignment.getStatus() == AssignmentStatus.SUBMITTED) {
                System.out.println("This assignment is already submitted");
                continue;
            }
            ArrayList<String> answers = new ArrayList<>();
            for (String question : assignment.getQuestions()) {
                System.out.println("Question: " + question);
                String answer = Utility.inputOutput("Type your answer here: ");
                answers.add(answer);
            }

            assignment.setAnswers(answers);
            assignment.setStatus(AssignmentStatus.SUBMITTED);
        }

        System.out.println("\nAll assignments submitted successfully\n");
        viewApplicationProcessDashboard(applicationId);
    }

    // UserStory:40; ar668
    public void viewAssessment(String applicationId) {
        System.out.println("Welcome to the Assessment Page\n");

        for (Application application : Utility.getApplications()) {
            if (application.getId().equals(applicationId)) {
                if (application.getAssignments().isEmpty()) {
                    System.out.println("No assignments found");
                } else {
                    System.out.println("Assignments for " + applicationId);
                    for (Assignment assignment : application.getAssignments()) {
                        System.out.println("Assignment ID: " + assignment.getId() + " | " + "Assignment Name: "
                                + assignment.getAssignmentName() + " | " + "Assignment Status: "
                                + assignment.getStatus());
                    }
                    break;
                }
            }
        }

        System.out.println("\n1. Submit Assignment");
        System.out.println("2. Go back to the dashboard");
        switch (Utility.inputOutput("Please Select One Of The Options")) {
            case "1":
                submitAssessmentForm(applicationId);
                break;
            case "2":
                viewApplicationProcessDashboard(applicationId);
                break;
            default:
                System.out.println("Invalid input. Redirecting to the dashboard.");
                viewApplicationProcessDashboard(applicationId);
                break;
        }
    }

    // ETY1 - STORY 45
    public void submitInterviewForm(String applicationId) {
        Application application = null;

        for (Application app : Utility.getApplications()) {
            if (app.getId().equals(applicationId)) {
                application = app;
                break;
            }
        }
        if (application == null) {
            System.out.println("Application with given ID not found");
            viewApplicantApplications();
            return;
        }

        ArrayList<Assignment> interviews = application.getInterviewAssignments();
        if (interviews.isEmpty()) {
            System.out.println("No interview questions found");
            viewApplicantApplications();
            return;
        }

        for (Assignment assignment : application.getInterviewAssignments()) {
            System.out.println("Interview ID: " + assignment.getId());
            if (assignment.getStatus() == AssignmentStatus.SUBMITTED) {
                System.out.println("Interview already submitted");
                continue;
            }
            ArrayList<String> answers = new ArrayList<>();
            for (String question : assignment.getQuestions()) {
                System.out.println("Question: " + question);
                String answer = Utility.inputOutput("Type your answer here: ");
                answers.add(answer);
            }
            assignment.setAnswers(answers);
            assignment.setStatus(AssignmentStatus.SUBMITTED);
        }

        System.out.println("Answers Submitted.");
        System.out.println("Directing to application dashboard..");

        viewApplicationProcessDashboard(application.getId());
    }

    /*
     * Author: Soumik Datta (sd631)
     * User Story: 44
     */
    public void viewInterview(String applicationId) {
        System.out.println("\n------ View Interview Questions ------\n");
        Application application = null;

        for (Application app : Utility.getApplications()) {
            if (app.getId().equals(applicationId)) {
                application = app;
                break;
            }
        }

        if (application == null) {
            System.out.println("Application with given ID not found");
            viewApplicantApplications();
            return;
        }

        ArrayList<Assignment> assignments = application.getInterviewAssignments();

        if (assignments.isEmpty()) {
            System.out.println("No interview questions found");
        } else {
            for (Assignment assignment : assignments) {
                System.out.println("\nInterview Name: " + assignment.getAssignmentName());
                System.out.println("Questions are: ");
                for (String question : assignment.getQuestions()) {
                    System.out.println(question);
                }
            }
        }

        Utility.inputOutput("Press any key to go back to the dashboard");
        System.out.println("\nRedirecting back to Application Dashboard..");
        viewApplicationProcessDashboard(applicationId);
    }

    public void viewFeedback(String applicationId) {
        System.out.println("\nWelcome to view feedback page");
        for (Application application : Utility.getApplications()) {
            if (application.getId().equals(applicationId)) {
                if (application.getFeedback().isEmpty()) {
                    System.out.println("\nFeedback not received");
                } else {
                    System.out.println("\nFeedback for " + applicationId);
                    System.out.println("\n" + application.getFeedback());
                }
                break;
            }
        }
        viewApplicationProcessDashboard(applicationId);
    }

    // UserStory:26; ar668
    public void viewJobPost() {
        User user = Utility.getCurrentUser();
        if (user == null || user.getRole() != UserRole.APPLICANT) {
            System.out.println("You are not authorized to view this page");
            viewAllAvailableJobs();
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
            viewAllAvailableJobs();
            return;
        }
        System.out.println("Welcome to the Job Post\n");
        System.out.println("Job ID: " + job.getId());
        System.out.println("Job Title: " + job.getJobName());
        System.out.println("Job Description: " + job.getJobDescription());
        System.out.println("Job Status: " + job.getJobStatus());

        String input = Utility.inputOutput("Do you want to apply for this job? (or type anything to go back) (Y/N)");

        if (input.equalsIgnoreCase("Y")) {
            viewApplicationForm(job);
        }
        viewAllAvailableJobs();
    }

    // UserStory:27; ar668
    public void viewApplicationForm(Job job) {
        System.out.println("\nWelcome to the Job Application Form\n");
        String education = Utility.inputOutput("Enter your education: ");
        String experiencInput = "";
        Integer experience = 0;
        while (true) {
            experiencInput = Utility.inputOutput("Enter your number of experience experience: ");
            try {
                experience = Integer.parseInt(experiencInput);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number for experience");
            }
        }
        String skills = Utility.inputOutput("Enter your skills: ");
        submitApplicationForm(job, education, experience, skills);
    }

    // UserStory:25; ar668
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
            System.out.println("Job ID: " + job.getId() + "|" + "Job Name: " + job.getJobName());
            System.out.println();
        });
        String input = Utility.inputOutput("Do you want to view a job post? (Y/N)");
        if (input.equalsIgnoreCase("Y")) {
            viewJobPost();
        } else {
            viewApplicantDashboard();
        }
    }

    // ETY1 - STORY 28
    public void submitApplicationForm(Job job, String education, Integer experience, String skills) {
        String applicationId = UUID.randomUUID().toString();
        Application newApplication = new Application(
                applicationId,
                job.getId(),
                Utility.getCurrentUser().getId(),
                ApplicationStatus.INPROGRESS,
                new ArrayList<Assignment>(),
                experience,
                education,
                skills,
                "");
        Utility.getApplications().add(newApplication);

        System.out.println("Application submitted : " + newApplication.getId());
        System.out.println("Directing to Applicant Dashboard");

        viewApplicantDashboard();

    }

    // ETY1 - STORY 14
    public void viewApplicantDashboard() {

        System.out.println("Welcome to the Applicant Dashboard");
        System.out.println("1. View Profile");
        System.out.println("2. View Applications");
        System.out.println("3. View Available Jobs");
        System.out.println("4. Log Out");
        switch (Utility.inputOutput("\nPlease Select One Of The Options")) {
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
                applicantLogOut();
                break;
            }
            default: {
                System.out.println("You entered invalid option");
                viewApplicantDashboard();
                break;
            }
        }

    }

    /*
     * Author: Soumik Datta (sd631)
     * User Story: 16
     */
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
        System.out.println("\n1. Update Profile");
        System.out.println("2. Delete Profile");
        System.out.println("3. Reset password");
        String input = Utility
                .inputOutput("\nPlease Select One Of The Options (or type anything to go back to the dashboard)");
        switch (input) {
            case "1":
                showUpdateProfilePage();
                break;
            case "2":
                deleteApplicantProfile();
                break;
            case "3":
                viewResetPasswordPage();
                break;
            default:
                viewApplicantDashboard();
                break;
        }
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 18
     */
    public void deleteProfileHelper() {
        User user = Utility.getCurrentUser();
        ArrayList<User> users = Utility.getUsers();
        users.removeIf(u -> u.getId().equals(user.getId()));
        Utility.setUsers(users);
        Utility.setCurrentUser(null);
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 18
     */
    public void deleteApplicantProfile() {
        String input = Utility.inputOutput("Are you sure you want to delete your profile? (Y/N)");
        if (input.equalsIgnoreCase("Y")) {
            System.out.println("Deleting Applicant Profile...");
            deleteProfileHelper();
            applicantViewSignInSignUpPage();
        } else if (input.equalsIgnoreCase("N")) {
            System.out.println("Profile deletion cancelled.");
            viewApplicantProfilePage();
        } else {
            System.out.println("Invalid input. Profile deletion cancelled.");
        }
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 17
     */
    public void updateProfile(User user) {
        ArrayList<User> users = Utility.getUsers();
        users.removeIf(u -> u.getId().equals(user.getId()));
        users.add(user);
        Utility.setCurrentUser(user);
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 17
     */
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
        viewApplicantProfilePage();
    }



    public void viewResetPasswordPage() {

        System.out.println("\nWelcome to reset password page");
        System.out.println("1. Continue to reset password");
        System.out.println("2. Go back to dashboard\n");

        switch (Utility.inputOutput("Please Select One Of The Options")) {
            case "1":
                System.out.println("Welcome to reset password page\n");
                String userName = Utility.inputOutput("Enter your User name");
                resetPassword(userName);
                break;
            case "2":
                System.out.println("\nRediredting to Dashboard\n");
                if (Utility.getCurrentUser().getRole().equals(UserRole.APPLICANT)) {
                    System.out.println("\nRedirecting to Applicant dashboard\n");
                    ApplicantService.getInstance().viewApplicantDashboard();
                } else {
                    System.out.println("\nRedirecting to Recruiter dashboard\n");
                    RecruiterService.getInstance().viewRecruiterDashboard();
                }
                break;
            default:
                System.out.println("You entered invalid option");
                viewResetPasswordPage();
                break;
        }
    }

    public void resetPassword(String userName) {
        System.out.println("\nYour entered username: " + userName);
        String resetCode = "";
        if (Utility.getCurrentUser().getRole() == UserRole.RECRUITER) {
            resetCode = Utility.inputOutput("Enter the Reset Code");
        }
        if (Utility.getCurrentUser().getUserName().equals(userName)) {
            String password = Utility.inputOutput("Enter your New Password");
            Utility.getCurrentUser().setPassword(password);
            for (User user : Utility.getUsers()) {
                if (user.getUserName().equals(userName)) {
                    if (Utility.getCurrentUser().getRole() == UserRole.RECRUITER && !resetCode.equals("XVQTY")) {
                        System.out.println("\nYou have entered wrong Reset Code\n");
                        viewResetPasswordPage();
                        break;
                    }
                    user.setPassword(password);
                }
            }
            if (Utility.getCurrentUser().getRole() == UserRole.APPLICANT) {
                System.out.println("\nRedirecting to Applicant dashboard");
                ApplicantService.getInstance().viewApplicantDashboard();
            } else {
                System.out.println("\nRedirecting to Recruiter dashboard");
                RecruiterService.getInstance().viewRecruiterDashboard();
            }
        } else {
            System.out.println("\nYou have entered wrong Crediantials\n");
            viewResetPasswordPage();
        }
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 29
     */
    public void viewApplicantApplications() {
        User user = Utility.getCurrentUser();
        if (user instanceof Recruiter) {
            System.out.println("You are not an applicant.");
            viewApplicantDashboard();
            return;
        }
        ArrayList<Application> applications = ((Applicant) user).getApplications();
        if (applications == null || applications.isEmpty()) {
            System.out.println("No applications found.");
            viewApplicantDashboard();
            return;
        }
        System.out.println("\nApplications:");
        int index = 1;
        for (Application application : applications) {
            System.out.println(
                    index + ". Application ID: " + application.getId() + " | " + "Status: " + application.getStatus());
            index++;
        }
        System.out.println("\n1: View specific application details");
        System.out.println("2: Go back to the dashboard");
        String input = Utility.inputOutput("Please Select One Of The Options");
        switch (input) {
            case "1":
                String applicationId = Utility.inputOutput("Enter the Application ID");
                viewSpecificApplication(applicationId);
                break;
            case "2":
                viewApplicantDashboard();
                break;
            default:
                System.out.println("Invalid input. Redirecting to the dashboard.");
                viewApplicantDashboard();
                break;
        }
    }

    public void viewSpecificApplication(String applicationId) {
        if (applicationId == null || applicationId.isEmpty()) {
            System.out.println("Application ID is required.");
            viewApplicantApplications();
            return;
        }
        User user = Utility.getCurrentUser();
        System.out.println("\nWelcome to specific application details");

        Application application = null;
        for (Application app : Utility.getApplications()) {
            if (app.getId().equals(applicationId) &&
                    user.getId().equals(app.getApplicantId())) {
                application = app;
                break;
            }
        }
        if (application == null) {
            System.out.println("Application with ID " + applicationId + " not found.");
            viewApplicantApplications();
            return;
        }
        System.out.println("\nApplication ID: " + application.getId());
        System.out.println("Job Id: " + application.getJobId());
        System.out.println("Applicant Id: " + application.getApplicantId());
        System.out.println("Application Status: " + application.getStatus());

        System.out.println("\n1: View application process dashboard");
        System.out.println("2: View Job Description");
        System.out.println("3: Withdraw application");

        switch (Utility.inputOutput("Please Select One Of The Options")) {
            case "1":
                System.out.println("Redirecting to application process dashboard");
                viewApplicationProcessDashboard(applicationId);
                break;
            case "2":
                System.out.println("Redirecting to view job description page");
                viewJobDescFromApplication(applicationId);
                break;
            case "3":
                System.out.println("Redirecting to withdraw application page");
                withdrawApplication(applicationId);
                break;
            default:
                System.out.println("You entered invalid option, redirecting to applications page");
                viewApplicantApplications();
                break;
        }
    }

    public void withdrawApplication(String applicationId) {
        System.out.println("\nWelcome to withdraw application page");

        System.out.println("1: Confirm to withdraw application");
        System.out.println("2: Go back to applications page");

        switch (Utility.inputOutput("Please Select One Of The Options")) {
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

    // ETY1 - STORY 8
    public void applicantLogOut() {
        System.out.println("Applicant Logging Out....");
        Utility.setCurrentUser(null);
        System.out.println("Logged Out Successfully");
        System.out.println("Directing to Common Landing Page");
        CommonService.getInstance().accessLandingPage();
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 36
     */
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
        System.out.println("2. Submit assignment");
        System.out.println("3. View interview questions");
        System.out.println("4. Submit interview answers");
        System.out.println("5. View feedback");
        System.out.println("6. Go back to Applications page");

        switch (Utility.inputOutput("\nPlease Select One Of The Options")) {
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
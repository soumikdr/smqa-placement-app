package service;

import model.*;
import utility.Utility;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;
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

    // ETY1 - STORY 42
    public void viewAssessmentResult(String applicationId, String assignmentId) {

        Assignment result = new Assignment();

        for (Application a : Utility.getApplications()) {
            if (a.getId().equals(applicationId)) {
                for (Assignment assign : a.getAssignments()) {
                    if (assign.getId().equals(assignmentId)) {
                        result = assign;
                        System.out.println("Assessment Found..");
                    }
                }
            }
        }
        if (result.getId() == null || result.getId().isEmpty()) {
            System.out.println("There is no Coding Assessment Result for this application");
        } else {
            int answerCount = 0;
            System.out.println("Assessment Questions and Answers : ");
            for (String question : result.getQuestions()) {
                System.out.println(question + "\n");

                System.out.println(result.getAnswers().get(answerCount) + "\n");
                answerCount++;

            }
        }

        System.out.println("Directing to Application Page..");
        viewSpecificApplication(applicationId);

    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 43
     */
    public void sendInterview(Application application) {

        System.out.println("Send Interview Questions to the Applicant");

        ArrayList<String> questions = new ArrayList<>();

        String question = "";
        while (true) {
            question = Utility.inputOutput("Enter the question..(type 'exit' to go back)");
            if (question.equals("exit")) {
                viewSpecificApplication(application.getId());
                return;
            }
            if (question.isEmpty()) {
                System.out.println("Question cannot be empty");
            } else {
                questions.add(question);
                String moreQuestions = Utility.inputOutput("Do you want to add more questions? (y/n)");
                if (moreQuestions.equals("n")) {
                    break;
                }
            }
        }
        String interviewTitle = "Interview " + (application.getAssignments().size() + 1);
        Assignment interview = new Assignment(UUID.randomUUID().toString(), application.getApplicantId(),
                interviewTitle,
                questions, null);

        ArrayList<Assignment> interviews = application.getInterviewAssignments();
        interviews.add(interview);
        application.setInterviewAssignments(interviews);
        System.out.println("Interview questions sent successfully");
        Utility.inputOutput("Press enter to go back.");
        viewSpecificApplication(application.getId());
    }

    public void viewInterviewResult() {
    }

    /*
     * Author: Soumik Datta (sd631)
     * User Story: 15
     */
    public void viewRecruiterDashboard() {
        System.out.println("Welcome to Recruiter Dashboard\n");

        System.out.println("1. View profile");
        System.out.println("2. View available jobs");
        System.out.println("3. View available applications");
        System.out.println("4. Post a new job");
        System.out.println("5. Reset Password");
        System.out.println("6. Logout");

        switch (Utility.inputOutput("\nPlease select one option to proceed..")) {
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
                viewResetPasswordPage();
            case "6":
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
        System.out.println("First Name: " + Utility.getCurrentUser().getName());
        System.out.println("Last Name: " + Utility.getCurrentUser().getLastName());
        System.out.println("User Name: " + Utility.getCurrentUser().getUserName());
        System.out.println("Role: " + Utility.getCurrentUser().getRole());
        System.out.println();
        System.out.println("1: Update your profile");
        System.out.println("2: Delete your profile");
        System.out.println("3: Go back to dashboard");

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

    // ETY1 - STORY 5
    public void recruiterSignUp(String recruiterCode, String firstName, String lastName, String userName,
                                String password) {
        System.out.println("Sign Up processing.... \n");

        String id = UUID.randomUUID().toString();
        if (recruiterCode != null && recruiterCode.equals("XVQTY")) {
            Recruiter newRecruiter = new Recruiter(id, firstName, lastName, userName, password);
            Utility.getUsers().add(newRecruiter);
            System.out.println("Sign Up Successful for Recruiter");
            Utility.setCurrentUser(newRecruiter);
            System.out.println("directing to Recruiter Dashboard");
            viewRecruiterDashboard();
        } else {
            System.out.println("Invalid Attempt");
            System.out.println("Try Again..");
            viewRecruiterSignUpPage();
        }
    }

    // ETY1 - STORY 47
    public void approveRejectApplication(Application application) {

        System.out.println("Do you want to Approve/Reject Application?");

        System.out.println("1. Approve The Application");
        System.out.print("2. Reject The Application\n");
        System.out.println("3. Go Back to Application Page");

        String selection = Utility.inputOutput("Please Select One Of The Options");

        switch (selection) {
            case "1": {
                application.setStatus(ApplicationStatus.SUCCESSFUL);
                Utility.getApplications().stream().map(a -> {
                    if (a.getId().equals(application.getId())) {
                        a.setStatus(application.getStatus());
                    }
                    return a;
                });
                System.out.println("Application Approved");
                System.out.println("Directing to Application's Page");

                // has to redirect to application page
                viewSpecificApplication(application.getId());
                break;
            }
            case "2": {
                application.setStatus(ApplicationStatus.UNSUCCESSFUL);
                Utility.getApplications().stream().map(a -> {
                    if (a.getId().equals(application.getId())) {
                        a.setStatus(application.getStatus());
                    }
                    return a;
                });
                System.out.println("Application Rejected");
                System.out.println("Directing to Application's Page");
                // has to redirect to application page
                viewSpecificApplication(application.getId());
                break;
            }
            case "3": {
                viewSpecificApplication(application.getId());
                // has to redirect to application page
                System.out.println("Directing to Application's Page");
                break;
            }
            default:
                System.out.println("You entered invalid option");
                approveRejectApplication(application);
                break;
        }

    }

    // UserStory: 21; ar668
    public void deleteRecruiterProfile() {
        System.out.println("Deleting Recruiter profile");
        String userName = Utility.getCurrentUser().getUserName();
        Utility.getUsers().removeIf(user -> user.getUserName().equals(userName));
        Utility.setCurrentUser(null);
        System.out.println("Profile deleted successfully");
        CommonService.getInstance().accessLandingPage();
    }

    /*
     * Author: Soumik Datta (sd631)
     * User Story: 20
     */
    public void updateRecruiterProfile() {
        System.out.println("Update profile information (leave empty for no change)\n");

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

            if (Utility.getUsers().stream().filter(u -> u.getUserName().equals(userName)).findFirst()
                    .orElse(null) == null && !userName.isEmpty()) {
                uniqueUsername = true;
                Utility.getCurrentUser().setUserName(userName);
            } else {
                System.out.println("Provided username is empty or already exists. Please try again.");
            }
        }

        System.out.println("\nProfile updated successfully!\n");
        viewRecruiterProfilePage();
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 24
     */
    public void viewAvailableJobs() {
        ArrayList<Job> jobs = Utility.getJobs();
        if (jobs == null || jobs.isEmpty()) {
            System.out.println("No jobs available");
            viewRecruiterDashboard();
            return;
        }
        System.out.println("Available Jobs");
        int index = 1;
        for (Job job : jobs) {
            System.out.println(index + ". Job ID: " + job.getId() + " | Job Title: " + job.getJobName());
            index++;
        }
        System.out.println("\n1: View specific job details");
        System.out.println("2: Go back to dashboard");
        boolean inLoop = true;
        while (inLoop) {
            String answer = Utility.inputOutput("Please select one of the options..");
            switch (answer) {
                case "1":
                    inLoop = false;
                    viewSpecificJobPost();
                    break;
                case "2":
                    inLoop = false;
                    viewRecruiterDashboard();
                    break;
                default:
                    System.out.println(answer + " is not a valid option. Please try again.");
                    break;
            }
        }
    }

    public void viewSpecificJobPost() {
        System.out.println("Welocme to Specific Job Post Details");
        String jobId = Utility.inputOutput("Enter the Job Id");
        Boolean invalidJobId = true;

        for (Job job : Utility.getJobs()) {
            if (job.getId().equals(jobId)) {
                System.out.println("Job ID: " + job.getId());
                System.out.println("Job Name: " + job.getJobName());
                System.out.println("Job Description: " + job.getJobDescription());
                System.out.println("Job Status: " + job.getJobStatus());
                invalidJobId = false;
                break;
            }
        }

        System.out.println();
        if (invalidJobId) {
            System.out.println("You have entered a invalid Job id");
        } else {
            System.out.println("1: Update job description");
            System.out.println("2: Update job status");
            System.out.println("3: View total applications for this job");
            System.out.println("4: Continue to main menu");

            switch (Utility.inputOutput("Please Select One Of The Options")) {
                case "1":
                    System.out.println("Redirecting to update job description page");
                    updateDescriptionOfJobPost(jobId);
                    break;
                case "2":
                    System.out.println("Redirecting to total applications for the job");
                    updateStatusOfJobPost(jobId);
                    break;
                case "3":
                    System.out.println("Redirecting to dashboard");
                    viewTotalNumberOfApplications(jobId);
                    break;
                case "4":
                    System.out.println("Redirecting to main menu");
                    break;
                default:
                    System.out.println("You entered invalid option");
                    viewRecruiterDashboard();
                    break;
            }
        }

        System.out.println("\n1: View another job details");
        System.out.println("2: Go back to dashboard");

        switch (Utility.inputOutput("Please Select One Of The Options")) {
            case "1":
                System.out.println("Redirecting to view specific job details");
                viewSpecificJobPost();
                break;
            case "2":
                System.out.println("Redirecting to dashboard");
                viewRecruiterDashboard();
                break;
            default:
                System.out.println("You entered invalid option");
                viewRecruiterDashboard();
                break;
        }

    }

    /*
     * Author: Soumik Datta (sd631)
     * User Story: 32
     */
    public void updateDescriptionOfJobPost(String jobId) {
        System.out.println("-------- Update job description --------\n");
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

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 33
     */
    public void updateStatusOfJobPost(String jobId) {
        ArrayList<Job> jobs = Utility.getJobs();
        if (jobs == null || jobs.isEmpty()) {
            System.out.println("No jobs available");
            return;
        }
        for (Job job : jobs) {
            if (job.getId().equals(jobId)) {
                System.out.println("Current status of job: " + job.getJobStatus());
                JobStatus newStatus = Objects.equals(job.getJobStatus(), JobStatus.PRIVATE) ? JobStatus.PUBLIC
                        : JobStatus.PRIVATE;
                job.setJobStatus(newStatus);
                System.out.println("Status of job updated successfully");
                return;
            }
        }
        System.out.println("No job post available with given id");
    }

    // ETY1 - STORY 50
    public void viewTotalNumberOfApplications(String jobId) {

        AtomicInteger total = new AtomicInteger();
        Utility.getApplications().stream().forEach(application -> {
            if (application.getJobId().equals(jobId)) {
                total.getAndIncrement();
            }
        });

        System.out.println("Total Applications of : " + jobId + " is " + total);
    }

    // ETY1 - STORY 22
    public void viewJobPostingForm() {

        System.out.println("**** New Job Form ****");

        String jobTitle = Utility.inputOutput("Enter the New Job Title");

        String jobDesc = Utility.inputOutput("Enter the New Job Description");

        System.out.println("Submitting new job post...");
        if (jobTitle == null || jobTitle.isEmpty() || jobDesc == null || jobDesc.isEmpty()) {
            System.out.println("Job Title or Job Description empty, Please try again..");
            viewJobPostingForm();
            return;
        }
        submitNewJobPost(jobTitle, jobDesc);
        viewRecruiterDashboard();
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 23
     */
    public void submitNewJobPost(String jobName, String jobDescription) {
        String id = UUID.randomUUID().toString();
        Job job = new Job(
                id,
                jobName,
                jobDescription,
                JobStatus.PUBLIC);
        Utility.addJob(job);
        System.out.println("Job posted successfully");
    }

    // UserStory: 30; ar668
    public void viewAllApplications() {
        User user = Utility.getCurrentUser();
        if (user instanceof Applicant) {
            System.out.println("You are not authorized to view this page");
            viewRecruiterDashboard();
            return;
        }
        ArrayList<Application> applications = Utility.getApplications();
        if (applications == null || applications.isEmpty()) {
            System.out.println("No applications available.");
            viewRecruiterDashboard();
            return;
        }

        applications.stream().filter(application -> ApplicationStatus.INPROGRESS.equals(application.getStatus()))
                .forEach(application -> {
                    System.out.println("Application ID: " + application.getId() + "|" + "Status: "
                            + application.getStatus() + "|" + "Applicant ID: " + application.getApplicantId());
                });

        System.out.println("\n1: View specific Application details");
        System.out.println("2: Go back to dashboard");
        boolean inLoop = true;
        while (inLoop) {
            String answer = Utility.inputOutput("Please select one of the options..");
            switch (answer) {
                case "1":
                    String applicationId = Utility.inputOutput("Enter the Application ID to view details");
                    viewSpecificApplication(applicationId);
                    break;
                case "2":
                    inLoop = false;
                    viewRecruiterDashboard();
                    break;
                default:
                    System.out.println(answer + " is not a valid option. Please try again.");
                    break;
            }
        }

    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 11
     */
    public void viewResetPasswordPage() {
        String input = Utility.inputOutput("Do you want to reset your password? (y/n)");
        if (input.equalsIgnoreCase("y")) {
            String userName = Utility.inputOutput("Enter your User name");
            resetPasswordRecruiter(userName);
        } else {
            System.out.println("Redirecting to dashboard");
            viewRecruiterDashboard();
        }
    }

    // UserStory: 13; ar668
    public void resetPasswordRecruiter(String userName) {
        System.out.println("\nWelcome to Reset Password Page for Recruiter\n");
        System.out.println("\nYour entered username: " + userName + "\n");
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
        } else {
            System.out.println("\nYou have entered wrong Crediantials\n");
            viewResetPasswordPage();
        }
    }

    /*
     * Author: Soumik Datta (sd631)
     * User Story: 37
     */
    public void viewSpecificApplication(String applicationId) {
        User userApplicant = null;
        Application application = null;
        ArrayList<Application> allApplications = Utility.getApplications();
        ArrayList<User> allUsers = Utility.getUsers();

        for (Application app : allApplications) {
            if (app.getId().equals(applicationId)) {
                application = app;
                break;
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
                break;
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

        System.out.println("\n1: Update status of application");
        System.out.println("2: Send an assignment");
        System.out.println("3: Send interview questions");
        System.out.println("4: Send feedback");
        System.out.println("5: View submitted answers");
        System.out.println("6: Go back to All Applications");
        String answer = Utility.inputOutput("Please select one of the options..");

        switch (answer) {
            case "1" -> approveRejectApplication(application);
            case "2" -> sendAssignment(application);
            case "3" -> sendInterview(application);
            case "4" -> sendFeedback(application);
            case "5" -> viewSubmittedAnswers(application);
            case "6" -> viewAllApplications();
            default -> {
                System.out.println("You entered invalid option");
                viewAllApplications();
                return;
            }
        }
    }

    /*
     * Author: Soumik Datta (sd631)
     * User Story: 38
     */
    public void sendAssignment(Application application) {
        System.out.println("\n-------- Send assignment to applicant --------\n");

        String assessmentTitle = "";
        while (assessmentTitle.isEmpty()) {
            assessmentTitle = Utility.inputOutput("Enter the title of the assignment..");
            if (assessmentTitle.isEmpty()) {
                System.out.println("Assessment title cannot be empty.");
            }
        }
        ArrayList<String> questions = new ArrayList<>();
        String question = "";
        while (true) {
            question = Utility.inputOutput("Enter the question..(type 'exit' to go back)");
            if (question.equals("exit")) {
                viewSpecificApplication(application.getId());
            }
            if (question.isEmpty()) {
                System.out.println("Question cannot be empty");
            } else {
                questions.add(question);
                String moreQuestions = Utility.inputOutput("Do you want to add more questions? (y/n)");
                if (moreQuestions.equals("n")) {
                    break;
                }
            }
        }

        Assignment newAssignment = new Assignment(
                UUID.randomUUID().toString(),
                application.getApplicantId(),
                assessmentTitle,
                questions,
                new ArrayList<>());

        ArrayList<Assignment> assignments = application.getAssignments();
        assignments.add(newAssignment);

        application.setAssignments(assignments);

        System.out.println("\nAssignment sent successfully.\n");
        viewSpecificApplication(application.getId());
    }

    /*
     * Author: Soumik Datta (sd631)
     * User Story: 48
     */
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

    public void viewSubmittedAnswers(Application application) {
        System.out.println("\nWelcome to view submitted answers for the application " + application.getId());
        ArrayList<String> questions = new ArrayList<String>();
        ArrayList<String> answers = new ArrayList<String>();
        System.out.println("Submitted Answers for assessments: ");
        for (Assignment assignment : application.getAssignments()) {
            questions = assignment.getQuestions();
            answers = assignment.getAnswers();
            System.out.println("Assignment: " + assignment.getAssignmentName());
            for (int i = 0; i < questions.size(); i++) {
                System.out.println("\nQuestion: ");
                System.out.println(questions.get(i));
                System.out.println("Answer:");
                if (i < answers.size()) {
                    System.out.println(answers.get(i));
                } else {
                    System.out.println("No answer submitted yet");
                }
            }
            System.out.println("--------------------------");
        }
        System.out.println("Submitted Answers for interviews: ");

        for (Assignment interview : application.getInterviewAssignments()) {
            questions = interview.getQuestions();
            answers = interview.getAnswers();
            System.out.println("Interview: " + interview.getAssignmentName());
            for (int i = 0; i < questions.size(); i++) {
                System.out.println("\nQuestion: ");
                System.out.println(questions.get(i));
                System.out.println("Answer:");
                if (i < answers.size()) {
                    System.out.println(answers.get(i));
                } else {
                    System.out.println("No answer submitted yet");
                }
            }
            System.out.println("--------------------------");
        }

        viewSpecificApplication(application.getId());
    }

    // UserStory: 9; ar668
    public void logoutRecruiter() {
        System.out.println("Initiating logout process for Recruiter");
        Utility.setCurrentUser(null);
        System.out.println("You have been logged out successfully.");
        CommonService.getInstance().accessLandingPage();
    }

    // UserStory: 3; ar668
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
                viewRecruiterSignUpPage();
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

    public void viewRecruiterSignUpPage() {
        System.out.println("Welcome to Recruiter Sign Up Page\n");
        System.out.println("Please enter the following details");
        String firstName = Utility.inputOutput("First Name: ");
        String lastName = Utility.inputOutput("Last Name: ");
        String userName = Utility.inputOutput("User Name: ");
        String password = Utility.inputOutput("Password: ");
        String recruiterCode = Utility.inputOutput("Enter the Recruiter Code");

        if (firstName == null || firstName.isEmpty() ||
                lastName == null || lastName.isEmpty() ||
                userName == null || userName.isEmpty() ||
                password == null || password.isEmpty() ||
                recruiterCode == null || recruiterCode.isEmpty()) {
            System.out.println("All fields are required. Please try again.");
            viewRecruiterSignUpPage();
            return;
        }
        recruiterSignUp(recruiterCode, firstName, lastName, userName, password);
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

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 6
     */
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
            RecruiterService.getInstance().viewRecruiterDashboard();
        }
    }
}
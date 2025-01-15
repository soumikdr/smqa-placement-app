package test.whitebox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import model.Application;
import model.ApplicationStatus;
import model.Assignment;
import model.Job;
import model.JobStatus;
import model.Recruiter;
import model.User;
import model.UserRole;
import service.CommonService;
import service.RecruiterService;
import utility.Utility;

public class RecruiterServiceStatementTest {

    private RecruiterService recruiterService;

    private ArrayList<Application> mockApplications;
    private ArrayList<User> mockUsers;
    private ArrayList<Job> mockJobs;
    private Application mockApplication;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    private void provideInput(String data) {
        InputStream in = new ByteArrayInputStream(data.getBytes());
        System.setIn(in);
    }

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Before
    public void setup() {
        recruiterService = RecruiterService.getInstance();
        // Mock Jobs
        Job job1 = new Job("1", "Data Analyst",
                "A data analyst's job is to collect, organize, and analyze data to help businesses solve problems and gain insights. ",
                JobStatus.PRIVATE);
        Job job2 = new Job("2", "Frontend Developer",
                "As a Front End Developer you'll take ownership of technical projects, designing and developing user interfaces and client dashboards for cutting edge trading systems technology. ",
                JobStatus.PUBLIC);
        mockJobs = new ArrayList<>();
        mockJobs.add(job1);
        mockJobs.add(job2);

        // Mock Assignments
        ArrayList<String> questions = new ArrayList<String>();
        questions.add("Question 1");
        questions.add("Question 2");

        ArrayList<String> answers = new ArrayList<String>();
        answers.add("1. Answer");
        answers.add("2. Answer");

        Assignment assignment = new Assignment("1", "1", "Java Assignment", questions, answers);
        ArrayList<Assignment> assignments = new ArrayList<>();
        assignments.add(assignment);

        // Mock applications
        mockApplications = new ArrayList<>();
        Application app1 = new Application("1", "1", "1", ApplicationStatus.INPROGRESS, assignments, 2, "BSc",
                "JS, CSS", "Feedback");
        mockApplication = app1;
        mockApplications.add(app1);

        // Mock users
        mockUsers = new ArrayList<>();
        User user1 = new User("1", "John", "Doe", "johnDoe", "bestpassword", UserRole.APPLICANT);
        mockUsers.add(user1);
    }

    // ETY1 - STORY 22
    @Test
    public void testViewJobPostingForm_notEmpty() {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        RecruiterService service = new RecruiterService();
        RecruiterService spyObject = Mockito.spy(service);


        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {

            Mockito.doNothing().when(spyObject).submitNewJobPost(any(),any());
            Mockito.doNothing().when(spyObject).viewRecruiterDashboard();

            mockedUtility.when(() -> Utility.inputOutput("Enter the New Job Title")).thenReturn("Job Title");
            mockedUtility.when(() -> Utility.inputOutput("Enter the New Job Description")).thenReturn("Job Desc");
            mockedUtility.when(() -> Utility.getApplications()).thenReturn(mockApplications);

            spyObject.viewJobPostingForm();

            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("**** New Job Form ****"));

            Assert.assertFalse(consoleOutput.contains("Job Title or Job Description empty"));


            Mockito.verify(spyObject, times(1)).submitNewJobPost(any(),any());
            Mockito.verify(spyObject, times(1)).viewRecruiterDashboard();

            mockedUtility.verify(times(2), () -> Utility.inputOutput(Mockito.anyString()));

        }

    }

    // ETY1 - STORY 22
    @Test
    public void testViewJobPostingForm_empty() {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        RecruiterService service = new RecruiterService();
        RecruiterService spyObject = Mockito.spy(service);


        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {


            mockedUtility.when(() -> Utility.inputOutput("Enter the New Job Title")).thenReturn(null);
            mockedUtility.when(() -> Utility.inputOutput("Enter the New Job Description")).thenReturn(null);

            Mockito.doCallRealMethod().doNothing().when(spyObject).viewJobPostingForm();

            spyObject.viewJobPostingForm();
            String consoleOutput = outputStream.toString();

            Assert.assertTrue(consoleOutput.contains("Job Title or Job Description empty"));

            Mockito.verify(spyObject, times(2)).viewJobPostingForm();

            mockedUtility.verify(times(2), () -> Utility.inputOutput(Mockito.anyString()));

        }

    }

    // ETY1 - STORY 50
    @Test
    public void viewTotalNumberOfApplicationsTest_oneApplication() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        RecruiterService service = new RecruiterService();

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {

            mockedUtility.when(() -> Utility.getApplications()).thenReturn(mockApplications);


            service.viewTotalNumberOfApplications("1");
            String consoleOutput = outputStream.toString();

            Assert.assertTrue(consoleOutput.contains("Total Applications of : " + mockJobs.get(0).getId() + " is " + "1"));

            mockedUtility.verify(() -> Utility.getApplications());

        }
    }
    // ETY1 - STORY 47
    @Test
    public void testApproveRejectApplication_approved() {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        RecruiterService service = new RecruiterService();
        RecruiterService spyObject = Mockito.spy(service);


        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {

            Mockito.doNothing().when(spyObject).viewSpecificApplication(any());

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("1");
            mockedUtility.when(() -> Utility.getApplications()).thenReturn(mockApplications);

            spyObject.approveRejectApplication(mockApplication);

            String consoleOutput = outputStream.toString();

            Assert.assertTrue(consoleOutput.contains("Application Approved"));



            Mockito.verify(spyObject, times(1)).viewSpecificApplication(any());
            Mockito.verify(spyObject, times(1)).approveRejectApplication(Mockito.any());
            mockedUtility.verify(times(1), () -> Utility.inputOutput(Mockito.anyString()));
            mockedUtility.verify(times(1), () -> Utility.getApplications());

        }

    }

    // ETY1 - STORY 47
    @Test
    public void testApproveRejectApplication_rejected() {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        RecruiterService service = new RecruiterService();
        RecruiterService spyObject = Mockito.spy(service);


        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {

            Mockito.doNothing().when(spyObject).viewSpecificApplication(any());

            mockedUtility.when(() -> Utility.getApplications()).thenReturn(mockApplications);


            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("2");

            mockApplication.setStatus(ApplicationStatus.UNSUCCESSFUL);

            spyObject.approveRejectApplication(mockApplication);

            String consoleOutput = outputStream.toString();

            Assert.assertTrue(consoleOutput.contains("Application Rejected"));


            Mockito.verify(spyObject, times(1)).viewSpecificApplication(any());
            Mockito.verify(spyObject, times(1)).approveRejectApplication(Mockito.any());
            mockedUtility.verify(times(1), () -> Utility.inputOutput(Mockito.anyString()));
            mockedUtility.verify(times(1), () -> Utility.getApplications());

        }

    }

    // ETY1 - STORY 47
    @Test
    public void testApproveRejectApplication_goBack() {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        RecruiterService service = new RecruiterService();
        RecruiterService spyObject = Mockito.spy(service);


        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            Mockito.doNothing().when(spyObject).viewSpecificApplication(any());


            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("3");

            spyObject.approveRejectApplication(mockApplication);

            String consoleOutput = outputStream.toString();

            Assert.assertTrue(consoleOutput.contains("Directing to Application's Page"));



            Mockito.verify(spyObject, times(1)).viewSpecificApplication(any());
            Mockito.verify(spyObject, times(1)).approveRejectApplication(Mockito.any());
            mockedUtility.verify(times(1), () -> Utility.inputOutput(Mockito.anyString()));

        }

    }

    // ETY1 - STORY 47
    @Test
    public void testApproveRejectApplication_invalid() {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        RecruiterService service = new RecruiterService();
        RecruiterService spyObject = Mockito.spy(service);


        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {


            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("invalid");

            Mockito.doCallRealMethod().doNothing().when(spyObject).approveRejectApplication(any());

            spyObject.approveRejectApplication(mockApplication);

            String consoleOutput = outputStream.toString();

            Assert.assertTrue(consoleOutput.contains("You entered invalid option"));

            Mockito.verify(spyObject, times(2)).approveRejectApplication(Mockito.any());
            mockedUtility.verify(times(1), () -> Utility.inputOutput(Mockito.anyString()));

        }

    }

    // ETY1 - STORY 5
    @Test
    public void recruiterSignUpTest_Successful() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        RecruiterService service = new RecruiterService();
        RecruiterService spyObject = Mockito.spy(service);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            Mockito.doNothing().when(spyObject).viewRecruiterDashboard();

            mockedUtility.when(Utility::getUsers).thenReturn(mockUsers);
            Recruiter recruiter=new Recruiter("newId","name", "surname", "username", "password");


            spyObject.recruiterSignUp("XVQTY", recruiter.getName(),recruiter.getLastName(),recruiter.getUserName(),recruiter.getPassword());
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Sign Up Successful for Recruiter"));

            Mockito.verify(spyObject, times(1)).viewRecruiterDashboard();
            mockedUtility.verify(times(1), () -> Utility.getUsers());
        }
    }
    // ETY1 - STORY 5
    @Test
    public void recruiterSignUpTest_Invalid() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        RecruiterService service = new RecruiterService();
        RecruiterService spyObject = Mockito.spy(service);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            Mockito.doNothing().when(spyObject).viewRecruiterSignUpPage();

            Recruiter recruiter=new Recruiter("newId","name", "surname", "username", "password");

            spyObject.recruiterSignUp("12345", recruiter.getName(),recruiter.getLastName(),recruiter.getUserName(),recruiter.getPassword());

            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Invalid Attempt"));

            Mockito.verify(spyObject, times(1)).viewRecruiterSignUpPage();
        }
    }

    // ETY1 - STORY 42
    @Test
    public void viewAssessmentResultTest_assessmentFound() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        RecruiterService service = new RecruiterService();
        RecruiterService spyObject = Mockito.spy(service);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {


            mockedUtility.when(Utility::getApplications).thenReturn(mockApplications);

            Mockito.doNothing().when(spyObject).viewSpecificApplication(any());

            spyObject.viewAssessmentResult("1", "1");

            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Assessment Found"));
            Assert.assertTrue(consoleOutput.contains("Question 1"));



            Mockito.verify(spyObject, times(1)).viewSpecificApplication(any());
            mockedUtility.verify(times(1), () -> Utility.getApplications());

        }

    }

    // ETY1 - STORY 42
    @Test
    public void viewAssessmentResultTest_assessmentNotFound() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        RecruiterService service = new RecruiterService();
        RecruiterService spyObject = Mockito.spy(service);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {

            mockedUtility.when(Utility::getApplications).thenReturn(mockApplications);

            Mockito.doNothing().when(spyObject).viewSpecificApplication(any());

            spyObject.viewAssessmentResult("App1", "A3");

            String consoleOutput = outputStream.toString();

            Assert.assertTrue(consoleOutput.contains("There is no Coding Assessment Result for this application"));
            Assert.assertTrue(consoleOutput.contains("Directing to Application Page"));

            Mockito.verify(spyObject, times(1)).viewSpecificApplication(any());
            mockedUtility.verify(times(1), () -> Utility.getApplications());
        }

    }

    @Test
    public void testViewRecruiterDashboard_Option1() {
        // Simulate user input "1"
        provideInput("1");

        // Mock the viewRecruiterProfilePage method to prevent actual execution
        RecruiterService mockRecruiterService = Mockito.spy(new RecruiterService());
        Mockito.doNothing().when(mockRecruiterService).viewRecruiterProfilePage();

        mockRecruiterService.viewRecruiterDashboard();

        Mockito.verify(mockRecruiterService).viewRecruiterProfilePage();
    }

    @Test
    public void testViewRecruiterDashboard_Option2() {
        // Simulate user input "2"
        provideInput("2");

        // Mock the viewAvailableJobs method
        RecruiterService mockRecruiterService = Mockito.spy(new RecruiterService());
        Mockito.doNothing().when(mockRecruiterService).viewAvailableJobs();

        mockRecruiterService.viewRecruiterDashboard();

        Mockito.verify(mockRecruiterService).viewAvailableJobs();
    }

    @Test
    public void testViewRecruiterDashboard_Option3() {
        // Simulate user input "3"
        provideInput("3");

        // Mock the viewAllApplications method
        RecruiterService mockRecruiterService = Mockito.spy(new RecruiterService());
        Mockito.doNothing().when(mockRecruiterService).viewAllApplications();

        mockRecruiterService.viewRecruiterDashboard();

        Mockito.verify(mockRecruiterService).viewAllApplications();
    }

    @Test
    public void testViewRecruiterDashboard_InvalidOption() {
        // Simulate invalid input, then valid input to exit the recursion
        provideInput("111");

        // Mock the viewRecruiterDashboard method to prevent actual execution
        RecruiterService mockRecruiterService = Mockito.spy(new RecruiterService());
        Mockito.doNothing().when(mockRecruiterService).viewRecruiterDashboard();

        mockRecruiterService.viewRecruiterDashboard();

        Mockito.verify(mockRecruiterService).viewRecruiterDashboard();
    }

    @Test
    public void testUpdateRecruiterProfile_Valid() {
        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            // Arrange - user "John" "Doe" with username "johnDoe" provided
            mockedUtility.when(Utility::getCurrentUser).thenReturn(mockUsers.get(0));
            mockedUtility.when(() -> Utility.getUsers()).thenReturn(mockUsers);
            RecruiterService recruiterService = Mockito.spy(new RecruiterService());
            mockedUtility.when(() -> Utility.inputOutput(anyString())).thenReturn("Jack", "Dew", "jack");
            doNothing().when(recruiterService).viewRecruiterProfilePage();

            // Act
            recruiterService.updateRecruiterProfile();

            // Assert
            assertEquals("Jack", Utility.getCurrentUser().getName());
            assertEquals("Dew", Utility.getCurrentUser().getLastName());
            assertEquals("jack", Utility.getCurrentUser().getUserName());
        }
    }

    @Test
    public void testViewSpecificApplication_ValidApplicationId() {
        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            mockedUtility.when(Utility::getApplications).thenReturn(mockApplications);
            mockedUtility.when(Utility::getUsers).thenReturn(mockUsers);
            mockedUtility.when(() -> Utility.inputOutput(anyString())).thenReturn("1");

            RecruiterService recruiterService = Mockito.spy(new RecruiterService());
            doNothing().when(recruiterService).approveRejectApplication(mockApplications.get(0));

            recruiterService.viewSpecificApplication("1"); // No exception expected
        }
    }

    @Test
    public void testViewSpecificApplication_InvalidApplicationId() {
        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            mockedUtility.when(Utility::getApplications).thenReturn(mockApplications);
            mockedUtility.when(Utility::getUsers).thenReturn(mockUsers);
            RecruiterService recruiterService = Mockito.spy(new RecruiterService());
            doNothing().when(recruiterService).viewAllApplications();

            recruiterService.viewSpecificApplication("999"); // Invalid application ID

            String expectedOutput = "No application found with given id";
            assertEquals(expectedOutput, outContent.toString().trim());
        }
    }

    @Test
    public void testViewSpecificApplication_NoApplicantFound() {
        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            mockedUtility.when(Utility::getApplications).thenReturn(mockApplications);
            mockedUtility.when(Utility::getUsers).thenReturn(new ArrayList<>()); // No users
            RecruiterService recruiterService = Mockito.spy(new RecruiterService());
            doNothing().when(recruiterService).viewAllApplications();

            recruiterService.viewSpecificApplication("1");

            String expectedOutput = "No applicant found with for this application";
            assertEquals(expectedOutput, outContent.toString().trim());
        }
    }

    @Test
    public void testSendAssignment_ValidInputs() {
        try (MockedStatic<Utility> utilities = mockStatic(Utility.class)) {
            // Arrange and mock dependencies
            utilities.when(() -> Utility.inputOutput(anyString())).thenReturn("Backend Assignment").thenReturn("How database indexing work?").thenReturn("n");
            // Clear assignments if any
            mockApplication.setAssignments(new ArrayList<Assignment>());
            RecruiterService recruiterService = Mockito.spy(new RecruiterService());
            doNothing().when(recruiterService).viewSpecificApplication(mockApplication.getId());

            // Act
            recruiterService.sendAssignment(mockApplication);

            // Assert
            assertEquals(1, mockApplication.getAssignments().size());
            Assignment assignment = mockApplication.getAssignments().get(0);
            assertEquals("Backend Assignment", assignment.getAssignmentName());
        }
    }
    
    @Test
    public void testUpdateDescriptionOfJobPost_JobFoundAndUpdated() {
        try (MockedStatic<Utility> utilities = mockStatic(Utility.class)) {
            // Setup and Mocking
            // Initial Jobs have different job name and description
            utilities.when(Utility::getJobs).thenReturn(mockJobs);
            utilities.when(() -> Utility.inputOutput(anyString())).thenReturn("Senior Engineer")
                    .thenReturn("Develop and manage software projects");
            RecruiterService recruiterService = Mockito.spy(new RecruiterService());
            doNothing().when(recruiterService).viewAvailableJobs();

            // Act
            recruiterService.updateDescriptionOfJobPost("1");

            // Assert - Verify "Job title and description updated"
            assertEquals("Senior Engineer", Utility.getJobs().get(0).getJobName());
            assertEquals("Develop and manage software projects", Utility.getJobs().get(0).getJobDescription());

            // Verify interactions
            utilities.verify(times(3), Utility::getJobs);
            utilities.verify(times(2), () -> Utility.inputOutput(anyString()));
        }
    }

    @Test
    public void testSendFeedback_Valid() {
        try (MockedStatic<Utility> utilities = mockStatic(Utility.class)) {
            // Arrange
            utilities.when(() -> Utility.inputOutput(anyString())).thenReturn("Valid feedback");
            RecruiterService recruiterService = Mockito.spy(new RecruiterService());
            doNothing().when(recruiterService).viewSpecificApplication(mockApplication.getId());

            // Act
            recruiterService.sendFeedback(mockApplication);

            // Assert
            // Get the complete output
            String output = outContent.toString().trim();

            // Extract the last println message (messages are separated by newlines)
            String[] lines = output.split("\n");
            String lastMessage = lines[lines.length - 1];

            assertEquals("Feedback sent successfully", lastMessage);
        }
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 43
     *
     * Covers sendInterview(...) in RecruiterService:
     * - scenario: user enters one valid question, picks 'n' => interview is created
     */
    @Test
    public void testSendInterview_singleQuestion() {
        RecruiterService spyService = Mockito.spy(recruiterService);
        Application application = new Application();
        application.setId("app123");
        application.setApplicantId("applicant123");
        application.setAssignments(new ArrayList<>());
        application.setInterviewAssignments(new ArrayList<>());

        try (MockedStatic<Utility> utilMock = Mockito.mockStatic(Utility.class)) {
            // First prompt => "Enter the question..(type 'exit' to go back)"
            utilMock.when(() -> Utility.inputOutput("Enter the question..(type 'exit' to go back)"))
                    .thenReturn("What's your strength?");
            // Next prompt => "Do you want to add more questions? (y/n)" => 'n'
            utilMock.when(() -> Utility.inputOutput("Do you want to add more questions? (y/n)"))
                    .thenReturn("n");

            // Finally => "Press enter to go back."
            utilMock.when(() -> Utility.inputOutput("Press enter to go back."))
                    .thenReturn("");

            doNothing().when(spyService).viewSpecificApplication("app123");

            spyService.sendInterview(application);

            // Check that we created 1 new interview in the app
            assertEquals(1, application.getInterviewAssignments().size());
            Assignment interview = application.getInterviewAssignments().get(0);
            assertTrue(interview.getQuestions().contains("What's your strength?"));

            // Title is "Interview X" => there's no existing assignment, so X = 1
            assertEquals("Interview 1", interview.getAssignmentName());
        }
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 24
     *
     * Covers viewAvailableJobs() in RecruiterService:
     * - If no jobs => prints "No jobs available" => calls viewRecruiterDashboard()
     */
    @Test
    public void testViewAvailableJobs_noJobs() {
        RecruiterService spyService = Mockito.spy(recruiterService);

        try (MockedStatic<Utility> utilMock = Mockito.mockStatic(Utility.class)) {
            utilMock.when(Utility::getJobs).thenReturn(new ArrayList<>()); // empty

            doNothing().when(spyService).viewRecruiterDashboard();
            spyService.viewAvailableJobs();

            String output = outContent.toString();
            assertTrue(output.contains("No jobs available"));
            verify(spyService, times(1)).viewRecruiterDashboard();
        }
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 33
     *
     * Covers updateStatusOfJobPost(...) in RecruiterService:
     * - No matching job => prints "No job post available with given id"
     */
    @Test
    public void testUpdateStatusOfJobPost_noMatch() {
        ArrayList<Job> mockJobs = new ArrayList<>();
        // existing job with ID=XYZ
        mockJobs.add(new Job("XYZ", "Some Title", "Some Desc", JobStatus.PUBLIC));

        try (MockedStatic<Utility> utilMock = Mockito.mockStatic(Utility.class)) {
            utilMock.when(Utility::getJobs).thenReturn(mockJobs);

            recruiterService.updateStatusOfJobPost("notFound");
            String output = outContent.toString();
            assertTrue(output.contains("No job post available with given id"));
        }
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 23
     *
     * Covers submitNewJobPost(...) in RecruiterService:
     * - Checks console output "Job posted successfully" & job is added.
     */
    @Test
    public void testSubmitNewJobPost() {
        ArrayList<Job> existingJobs = new ArrayList<>();
        try (MockedStatic<Utility> utilMock = Mockito.mockStatic(Utility.class)) {
            utilMock.when(Utility::getJobs).thenReturn(existingJobs);

            recruiterService.submitNewJobPost("NewTitle", "NewDesc");
            String output = outContent.toString();
            assertTrue(output.contains("Job posted successfully"));
        }
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 11
     *
     * Covers viewResetPasswordPage(...) in RecruiterService:
     * - scenario: user chooses 'n' => calls viewRecruiterDashboard()
     */
    @Test
    public void testViewResetPasswordPage_userChoosesNo() {
        RecruiterService spyService = Mockito.spy(recruiterService);
        try (MockedStatic<Utility> utilMock = Mockito.mockStatic(Utility.class)) {
            // Current user is a recruiter
            User mockRecruiter = new Recruiter("R1", "Rec", "Tester", "recUser", "pw");
            utilMock.when(Utility::getCurrentUser).thenReturn(mockRecruiter);

            // Do you want to reset? => user enters 'n'
            utilMock.when(() -> Utility.inputOutput("Do you want to reset your password? (y/n)"))
                    .thenReturn("n");

            doNothing().when(spyService).viewRecruiterDashboard();
            spyService.viewResetPasswordPage();

            verify(spyService, times(1)).viewRecruiterDashboard();
            String out = outContent.toString();
            assertTrue(out.contains("Redirecting to dashboard"));
        }
    }


    @Test
    public void testVisitSignInSignUpPageRecruiter_option1() {

        RecruiterService spyService = spy(new RecruiterService());

        try (MockedStatic<Utility> utilityMock = mockStatic(Utility.class);
             MockedStatic<CommonService> commonServiceMock = mockStatic(CommonService.class)) {


            utilityMock.when(() -> Utility.inputOutput("Please select one of the options"))
                      .thenReturn("1");


            doNothing().when(spyService).recruiterSignInPage();


            spyService.visitSignInSignUpPageRecruiter();


            verify(spyService, times(1)).recruiterSignInPage();


            verify(spyService, times(1)).visitSignInSignUpPageRecruiter();


            verify(spyService, never()).viewRecruiterSignUpPage();
            commonServiceMock.verify( never(),() -> CommonService.getInstance());


            String consoleOutput = outContent.toString();
            assertTrue(consoleOutput.contains("Redirecting to Sign In page for Recruiter"));
        }
    }
    @Test
    public void testVisitSignInSignUpPageRecruiter() {

        RecruiterService spyService = spy(new RecruiterService());

        try (MockedStatic<Utility> utilityMock = mockStatic(Utility.class);
             MockedStatic<CommonService> commonServiceMock = mockStatic(CommonService.class)) {


            utilityMock.when(() -> Utility.inputOutput("Please select one of the options"))
                      .thenReturn("2");


            doNothing().when(spyService).viewRecruiterSignUpPage();


            spyService.visitSignInSignUpPageRecruiter();


            verify(spyService, times(1)).viewRecruiterSignUpPage();


            verify(spyService, times(1)).visitSignInSignUpPageRecruiter();


            verify(spyService, never()).recruiterSignInPage();
            commonServiceMock.verify(never(),() -> CommonService.getInstance() );


            String consoleOutput = outContent.toString();
            assertTrue(consoleOutput.contains("Redirecting to Sign Up page for Recruiter"));
        }
    }

    @Test
    public void testLogoutRecruiter() {

        try (MockedStatic<Utility> utilityMock = mockStatic(Utility.class);
             MockedStatic<CommonService> commonServiceMock = mockStatic(CommonService.class)) {


            utilityMock.when(() -> Utility.setCurrentUser(null)).thenAnswer(invocation -> null);


            CommonService mockCommonService = Mockito.mock(CommonService.class);
            commonServiceMock.when(CommonService::getInstance).thenReturn(mockCommonService);


            doNothing().when(mockCommonService).accessLandingPage();


            RecruiterService spyService = spy(new RecruiterService());
            spyService.logoutRecruiter();


            String consoleOutput = outContent.toString();
            assertTrue(
                       "Initiating logout message should be printed.",consoleOutput.contains("Initiating logout process for Recruiter"));
            assertTrue(
                       "Logout success message should be printed.",consoleOutput.contains("You have been logged out successfully."));


            utilityMock.verify(times(1),() -> Utility.setCurrentUser(null));


            commonServiceMock.verify( times(1),CommonService::getInstance);


            verify(mockCommonService, times(1)).accessLandingPage();
        }
    }

    @Test
    public void testDeleteRecruiterProfile() {

        try (MockedStatic<Utility> utilityMock = mockStatic(Utility.class);
             MockedStatic<CommonService> commonServiceMock = mockStatic(CommonService.class)) {


            User mockUser = Mockito.mock(User.class);
            when(mockUser.getUserName()).thenReturn("recruiter123");
            utilityMock.when(Utility::getCurrentUser).thenReturn(mockUser);


            List<User> mockUsers = new ArrayList<>();
            mockUsers.add(mockUser);
            utilityMock.when(Utility::getUsers).thenReturn(mockUsers);


            utilityMock.when(() -> Utility.setCurrentUser(null)).thenAnswer(invocation -> null);


            CommonService mockCommonService = Mockito.mock(CommonService.class);
            commonServiceMock.when(CommonService::getInstance).thenReturn(mockCommonService);
            doNothing().when(mockCommonService).accessLandingPage();


            RecruiterService spyService = spy(new RecruiterService());
            spyService.deleteRecruiterProfile();


            String consoleOutput = outContent.toString();
            assertTrue(
                       "Should print 'Deleting Recruiter profile'", consoleOutput.contains("Deleting Recruiter profile"));


            utilityMock.verify(times(1),Utility::getCurrentUser );

            utilityMock.verify(times(1),Utility::getUsers);
            assertTrue("User should be removed from the users list",mockUsers.isEmpty());
            utilityMock.verify(times(1),() -> Utility.setCurrentUser(null) );
            commonServiceMock.verify(times(1),CommonService::getInstance);
            verify(mockCommonService, times(1)).accessLandingPage();
        }
    }

 // UserStory: 13; ar668
    @Test
    public void testResetPasswordRecruiter() {

        try (MockedStatic<Utility> utilityMock = mockStatic(Utility.class)) {


            User mockUser = Mockito.mock(User.class);
            when(mockUser.getRole()).thenReturn(UserRole.RECRUITER);
            when(mockUser.getUserName()).thenReturn("recruiter1");
            when(mockUser.getPassword()).thenReturn("oldPassword");
            utilityMock.when(Utility::getCurrentUser).thenReturn(mockUser);


            User anotherUser = Mockito.mock(User.class);
            when(anotherUser.getUserName()).thenReturn("user2");
            List<User> users = new ArrayList<>();
            users.add(mockUser);
            users.add(anotherUser);
            utilityMock.when(Utility::getUsers).thenReturn(users);


            utilityMock.when(() -> Utility.inputOutput("Enter the Reset Code")).thenReturn("XVQTY");
            utilityMock.when(() -> Utility.inputOutput("Enter your New Password")).thenReturn("newSecurePassword");

            RecruiterService recruiterService = spy(new RecruiterService());

            doNothing().when(recruiterService).viewResetPasswordPage();


            recruiterService.resetPasswordRecruiter("recruiter1");


            String consoleOutput = outContent.toString();
            assertTrue("Should print welcome message.",consoleOutput.contains("Welcome to Reset Password Page for Recruiter")
                       );
            assertTrue( "Should display entered username.",consoleOutput.contains("Your entered username: recruiter1")
                      );



           verify(mockUser, times(2)).setPassword("newSecurePassword");


            verify(anotherUser, never()).setPassword(anyString());
        }
    }


}

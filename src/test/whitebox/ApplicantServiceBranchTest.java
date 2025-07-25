package test.whitebox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import model.Applicant;
import model.Application;
import model.ApplicationStatus;
import model.Assignment;
import model.AssignmentStatus;
import model.Job;
import model.JobStatus;
import model.Recruiter;
import model.User;
import model.UserRole;
import service.ApplicantService;
import service.CommonService;
import service.RecruiterService;
import utility.Utility;

public class ApplicantServiceBranchTest {

    private ApplicantService applicantService;

    private ArrayList<Application> mockApplications;
    private ArrayList<User> mockUsers;
    private ArrayList<Job> mockJobs;
    private Application mockApplication;
    private ArrayList<Assignment> mockInterviews;
    private Assignment mockAssignment;
    private ArrayList<String> mockedInterviewQuestions;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Before
    public void setUp() {

        applicantService = ApplicantService.getInstance();
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
        mockAssignment = assignment;
        ArrayList<Assignment> assignments = new ArrayList<>();
        assignments.add(assignment);

        Assignment mockInterview=new Assignment("1", "1", "Interview", questions, answers);

        // Mock applications
        mockApplications = new ArrayList<>();
        Application app1 = new Application("1", "1", "1", ApplicationStatus.INPROGRESS, assignments, 2, "BSc", "JS, CSS", "");
        mockInterviews=new ArrayList<>();
        mockInterviews.add(mockInterview);
        app1.setInterviewAssignments(mockInterviews);
        mockApplication = app1;

        app1.setAssignments(assignments);
        mockApplications.add(app1);

        // Mock users
        mockUsers = new ArrayList<>();
        Applicant user1 = new Applicant("1", "John", "Doe", "johnDoe", "bestpassword");
        mockUsers.add(user1);
        mockUsers.add(new Recruiter("2", "Jane", "Doe", "janeDoe", "bestpassword"));

        // Mock interview questions
        mockedInterviewQuestions = new ArrayList<>();
        mockedInterviewQuestions.add("[1] Why are you interested in this position?");
        mockedInterviewQuestions.add("[2] How do you handle conflict in a team environment?");
        mockedInterviewQuestions.add("[3] What are your salary expectations?");
    }

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
    }


    @Test
    public void testSignIn_ValidApplicant() {
        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            // Arrange
            mockedUtility.when(Utility::getUsers).thenReturn(mockUsers);
            mockedUtility.when(() -> Utility.inputOutput(anyString())).thenReturn("johnDoe").thenReturn("bestpassword");
            ApplicantService applicantService = Mockito.spy(new ApplicantService());
            doNothing().when(applicantService).viewApplicantDashboard();

            // Act
            applicantService.signIn();

            // Assert
            // Get the complete output
            String output = outContent.toString().trim();

            // Extract the last println message (messages are separated by newlines)
            String[] lines = output.split("\n");
            String lastMessage = lines[lines.length - 1];

            assertEquals("Applicant Signin successful. proceeding to applicant dashboard..", lastMessage);
        }
    }

    @Test
    public void testSignIn_InvalidUser_RetryNo() {
        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            // Arrange
            mockedUtility.when(Utility::getUsers).thenReturn(mockUsers);
            mockedUtility.when(() -> Utility.inputOutput(anyString())).thenReturn("johnDoe").thenReturn("wrongPassword").thenReturn("no");
            ApplicantService applicantService = Mockito.spy(new ApplicantService());
            doNothing().when(applicantService).applicantViewSignInSignUpPage();

            // Act
            applicantService.signIn();

            // Assert
            // Get the complete output
            String output = outContent.toString().trim();

            // Extract the last println message (messages are separated by newlines)
            String[] lines = output.split("\n");
            String lastMessage = lines[lines.length - 1];

            assertEquals("Invalid credentials or Not an Applicant.", lastMessage);
        }
    }

    @Test
    public void testSignIn_InvalidUser_RetryYes() {
        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            // Arrange
            mockedUtility.when(Utility::getUsers).thenReturn(mockUsers);
            mockedUtility.when(() -> Utility.inputOutput(anyString()))
                .thenReturn("johnDoe").thenReturn("wrongPassword").thenReturn("y").thenReturn("johnDoe").thenReturn("bestpassword");
            ApplicantService applicantService = Mockito.spy(new ApplicantService());
            doNothing().when(applicantService).viewApplicantDashboard();

            // Act
            applicantService.signIn();

            // Assert
            // Get the complete output
            String output = outContent.toString().trim();

            // Extract the last println message (messages are separated by newlines)
            String[] lines = output.split("\n");
            String lastMessage = lines[lines.length - 1];

            // Sign in successful message after retry
            assertEquals("Applicant Signin successful. proceeding to applicant dashboard..", lastMessage);
        }
    }

    @Test
    public void testSignIn_NonApplicantUser() {
        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            // Arrange
            mockedUtility.when(Utility::getUsers).thenReturn(mockUsers);
            // Provide recruiter credentials
            mockedUtility.when(() -> Utility.inputOutput(anyString())).thenReturn("janeDoe").thenReturn("wrongPassword").thenReturn("no");
            ApplicantService applicantService = Mockito.spy(new ApplicantService());
            doNothing().when(applicantService).applicantViewSignInSignUpPage();

            // Act
            applicantService.signIn();

            // Assert
            // Get the complete output
            String output = outContent.toString().trim();

            // Extract the last println message (messages are separated by newlines)
            String[] lines = output.split("\n");
            String lastMessage = lines[lines.length - 1];

            assertEquals("Invalid credentials or Not an Applicant.", lastMessage);
        }
    }

    // ETY1 - STORY 28
    @Test
    public void submitApplicationFormTest() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        ApplicantService service = ApplicantService.getInstance();
        ApplicantService spyObject = Mockito.spy(service);

        try(MockedStatic<Utility> mockedUtility=Mockito.mockStatic(Utility.class)) {
            Mockito.doNothing().when(spyObject).viewApplicantDashboard();

            mockedUtility.when(Utility::getApplications).thenReturn(mockApplications);
            mockedUtility.when(Utility::getCurrentUser).thenReturn(mockUsers.get(0));

            spyObject.submitApplicationForm(mockJobs.get(0),mockApplication.getEducation(),mockApplication.getYearOfExperience(),mockApplication.getSkills());
            String consoleOutput = outputStream.toString();

            Assert.assertTrue(consoleOutput.contains("Application submitted : "));

            mockedUtility.verify(Mockito.times(1),()->Utility.getApplications());

            Mockito.verify(spyObject,Mockito.times(1)).viewApplicantDashboard();

        }
    }

    // ETY1 - STORY 8
    @Test
    public void applicantLogOutTest() throws IOException {
        try(MockedStatic<CommonService> spyCommonService = Mockito.mockStatic(CommonService.class)
        ) {
            CommonService mockCommonService=Mockito.mock(CommonService.class);

            spyCommonService.when(CommonService::getInstance).thenReturn(mockCommonService);

            Mockito.doNothing().when(mockCommonService).accessLandingPage();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outputStream)); // Redirect System.out
            ApplicantService service = ApplicantService.getInstance();

            service.applicantLogOut();
            String consoleOutput = outputStream.toString();

            Assert.assertTrue(consoleOutput.contains("Logged Out Successfully"));

            Mockito.verify(mockCommonService).accessLandingPage();

        }
    }

    // ETY1 - STORY 14
    @Test
    public void viewApplicantDashboardTest() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        ApplicantService service = ApplicantService.getInstance();

        ApplicantService spyObject = Mockito.spy(service);

       try(MockedStatic<Utility> mockedUtility=Mockito.mockStatic(Utility.class)) {
           Mockito.doNothing().when(spyObject).viewApplicantProfilePage();
           Mockito.doNothing().when(spyObject).viewApplicantApplications();
           Mockito.doNothing().when(spyObject).viewAllAvailableJobs();

           Mockito.doNothing().when(spyObject).applicantLogOut();

           mockedUtility.when(() -> Utility.inputOutput(anyString())).thenReturn("1");


           spyObject.viewApplicantDashboard();

           String consoleOutput = outputStream.toString();

           Assert.assertTrue(consoleOutput.contains("Directing to Profile Page..."));

           outputStream.reset();
           mockedUtility.when(() -> Utility.inputOutput(anyString())).thenReturn("2");


           spyObject.viewApplicantDashboard();
           consoleOutput = outputStream.toString();
           Assert.assertTrue(consoleOutput.contains("Directing to Applications Page..."));

           outputStream.reset();
           mockedUtility.when(() -> Utility.inputOutput(anyString())).thenReturn("3");


           spyObject.viewApplicantDashboard();
           consoleOutput = outputStream.toString();
           Assert.assertTrue(consoleOutput.contains("Directing to Available Jobs Page..."));

           outputStream.reset();
           mockedUtility.when(() -> Utility.inputOutput(anyString())).thenReturn("4");


           spyObject.viewApplicantDashboard();
           consoleOutput = outputStream.toString();
           Assert.assertTrue(consoleOutput.contains("Logging Out..."));

           outputStream.reset();

           mockedUtility.when(() -> Utility.inputOutput(anyString())).thenReturn("invalidInput");

           Mockito.doCallRealMethod().doNothing().when(spyObject).viewApplicantDashboard();


           spyObject.viewApplicantDashboard();
           consoleOutput = outputStream.toString();
           Assert.assertTrue(consoleOutput.contains("You entered invalid option"));


           Mockito.verify(spyObject,Mockito.times(1)).viewApplicantProfilePage();
           Mockito.verify(spyObject,Mockito.times(1)).viewAllAvailableJobs();

           mockedUtility.verify(Mockito.times(5),()->Utility.inputOutput(Mockito.anyString()));
           Mockito.verify(spyObject,Mockito.times(6)).viewApplicantDashboard();

           Mockito.verify(spyObject,Mockito.times(1)).viewApplicantApplications();
           Mockito.verify(spyObject,Mockito.times(1)).applicantLogOut();
       }
    }


    // ETY1 - STORY 45
    @Test
    public void submitInterviewFormTest() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        ApplicantService spyObject = Mockito.spy(new ApplicantService());


        try(MockedStatic<Utility> mockedUtility=Mockito.mockStatic(Utility.class)){

            Mockito.doNothing().when(spyObject).viewApplicationProcessDashboard(any());

            Mockito.doNothing().when(spyObject).viewApplicantApplications();
            mockedUtility.when(Utility::getApplications).thenReturn(mockApplications);


            spyObject.submitInterviewForm("nonExistentId");

            String consoleOutput = outputStream.toString();

            Assert.assertTrue(consoleOutput.contains("Application with given ID not found"));

            outputStream.reset();

            mockApplications.get(0).setInterviewAssignments(new ArrayList<>());

            mockedUtility.when(Utility::getApplications).thenReturn(mockApplications);

            spyObject.submitInterviewForm(mockApplications.get(0).getId());

            consoleOutput = outputStream.toString();

            Assert.assertTrue(consoleOutput.contains("No interview questions found"));

            outputStream.reset();

            mockInterviews.get(0).setStatus(AssignmentStatus.SUBMITTED);

            mockApplications.get(0).setInterviewAssignments(mockInterviews);

            spyObject.submitInterviewForm(mockApplications.get(0).getId());

            consoleOutput = outputStream.toString();

            Assert.assertTrue(consoleOutput.contains("Interview already submitted"));

            outputStream.reset();

            mockInterviews.get(0).setStatus(AssignmentStatus.ALLOCATED);

            spyObject.submitInterviewForm(mockApplications.get(0).getId());

            consoleOutput = outputStream.toString();

            Assert.assertTrue(consoleOutput.contains("Answers Submitted"));


            Mockito.verify(spyObject,Mockito.times(2)).viewApplicationProcessDashboard(any());
            mockedUtility.verify(Mockito.times(2),()->Utility.inputOutput(Mockito.anyString()));
            mockedUtility.verify(Mockito.times(4),()->Utility.getApplications());
            Mockito.verify(spyObject,Mockito.times(2)).viewApplicantApplications();


          }
    }



    @Test
    public void testViewApplicantProfilePage_AllPropertiesNull() {
        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            // Arrange
            User mockUser = new User(null, null, null, null, null, null);
            mockedUtility.when(Utility::getCurrentUser).thenReturn(mockUser);
            mockedUtility.when(() -> Utility.inputOutput(anyString())).thenReturn("continue");
            ApplicantService applicantService = Mockito.spy(new ApplicantService());
            doNothing().when(applicantService).viewApplicantDashboard();

            // Test
            applicantService.viewApplicantProfilePage();

            // Assert
            // Get the complete output
            String output = outContent.toString().trim();

            // Extract the last println message (messages are separated by newlines)
            String[] lines = output.split("\n");

            // Filter string
            List<String> filteredLines = new ArrayList<>();

            for (String str : lines) {
                String trimmed = str.trim();
                if (!trimmed.isEmpty()) {
                    filteredLines.add(trimmed);
                }
            }

            assertTrue(filteredLines.contains("ID is missing."));
            assertTrue(filteredLines.contains("Name is missing."));
            assertTrue(filteredLines.contains("Last Name is missing."));
            assertTrue(filteredLines.contains("Username is missing."));
            assertTrue(filteredLines.contains("Role is missing."));
        }
    }

    @Test
    public void testViewApplicantProfilePage_AllPropertiesEmpty() {
        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            // Arrange
            User mockUser = new User("", "", "", "", "", null);
            mockedUtility.when(Utility::getCurrentUser).thenReturn(mockUser);
            mockedUtility.when(() -> Utility.inputOutput(anyString())).thenReturn("continue");
            ApplicantService applicantService = Mockito.spy(new ApplicantService());
            doNothing().when(applicantService).viewApplicantDashboard();

            // Test
            applicantService.viewApplicantProfilePage();

            // Assert
            // Get the complete output
            String output = outContent.toString().trim();

            // Extract the last println message (messages are separated by newlines)
            String[] lines = output.split("\n");

            // Filter string
            List<String> filteredLines = new ArrayList<>();

            for (String str : lines) {
                String trimmed = str.trim();
                if (!trimmed.isEmpty()) {
                    filteredLines.add(trimmed);
                }
            }

            assertTrue(filteredLines.contains("ID is missing."));
            assertTrue(filteredLines.contains("Name is missing."));
            assertTrue(filteredLines.contains("Last Name is missing."));
            assertTrue(filteredLines.contains("Username is missing."));
            assertTrue(filteredLines.contains("Role is missing."));
        }
    }

    @Test
    public void testViewApplicantProfilePage_AllPropertiesValid() {
        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            // Arrange
            User mockUser = new User("51", "John", "Doe", "john", "GyyGGyibkl",
                    UserRole.APPLICANT);
            mockedUtility.when(Utility::getCurrentUser).thenReturn(mockUser);
            mockedUtility.when(() -> Utility.inputOutput(anyString())).thenReturn("continue");
            ApplicantService applicantService = Mockito.spy(new ApplicantService());
            doNothing().when(applicantService).viewApplicantDashboard();

            // Test
            applicantService.viewApplicantProfilePage();

            // Assert
            // Get the complete output
            String output = outContent.toString().trim();

            // Extract the last println message (messages are separated by newlines)
            String[] lines = output.split("\n");

            // Filter string
            List<String> filteredLines = new ArrayList<>();

            for (String str : lines) {
                String trimmed = str.trim();
                if (!trimmed.isEmpty()) {
                    filteredLines.add(trimmed);
                }
            }

            assertFalse(filteredLines.contains("ID is missing."));
            assertFalse(filteredLines.contains("Name is missing."));
            assertFalse(filteredLines.contains("Last Name is missing."));
            assertFalse(filteredLines.contains("Username is missing."));
            assertFalse(filteredLines.contains("Role is missing."));
        }
    }

    @Test
    public void testSubmitAssessmentForm_ApplicationNotFound() {
        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            mockedUtility.when(Utility::getApplications).thenReturn(mockApplications);
            ApplicantService applicantService = Mockito.spy(new ApplicantService());


            applicantService.submitAssessmentForm("999g");

            // Assert
            // Get the complete output
            String output = outContent.toString().trim();

            // Extract the last println message (messages are separated by newlines)
            String[] lines = output.split("\n");
            String lastMessage = lines[lines.length - 1];

            assertEquals("Application with given ID not found", lastMessage);
        }
    }

    @Test
    public void testSubmitAssessmentForm_AssignmentAlreadySubmitted() {
        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            // Arrange (mocking the assignment as submitted)
            mockAssignment.setStatus(AssignmentStatus.SUBMITTED);
            ArrayList<Assignment> mockedAssignments = new ArrayList<>();
            mockedAssignments.add(mockAssignment);
            ArrayList<Application> mockedApplications = new ArrayList<>();

            Application app1 = new Application("1", "1", "1",
                    ApplicationStatus.INPROGRESS, mockedAssignments, 2, "BSc",
                    "JS, CSS", "");
            mockedApplications.add(app1);
            mockedUtility.when(Utility::getApplications).thenReturn(mockedApplications);
            mockedUtility.when(() -> Utility.inputOutput(anyString())).thenReturn("Test Input");

            ApplicantService applicantService = Mockito.spy(new ApplicantService());
            doNothing().when(applicantService).viewApplicationProcessDashboard("1");

            applicantService.submitAssessmentForm("1");

            // Assert
            // Get the complete output
            String output = outContent.toString().trim();

            // Extract the last println message (messages are separated by newlines)
            String[] lines = output.split("\n");

            // Filter string
            List<String> filteredLines = new ArrayList<>();

            for (String str : lines) {
                String trimmed = str.trim();
                if (!trimmed.isEmpty()) {
                    filteredLines.add(trimmed);
                }
            }

            assertTrue(filteredLines.contains("This assignment is already submitted"));
        }
    }

    @Test
    public void testSubmitAssessmentForm_SubmitAssignmentSuccessfully() {
        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            // Arrange
            mockedUtility.when(Utility::getApplications).thenReturn(mockApplications);
            mockedUtility.when(() -> Utility.inputOutput(anyString())).thenReturn("Test Input");
            ApplicantService applicantService = Mockito.spy(new ApplicantService());
            doNothing().when(applicantService).viewApplicationProcessDashboard("1");

            // Act
            applicantService.submitAssessmentForm("1");

            // Assert
            // Get the complete output
            String output = outContent.toString().trim();

            // Extract the last println message (messages are separated by newlines)
            String[] lines = output.split("\n");
            String lastMessage = lines[lines.length - 1];

            assertEquals("All assignments submitted successfully", lastMessage.trim());
        }
    }

    @Test
    public void testViewInterview_ApplicationNotFound() {
        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            mockedUtility.when(Utility::getApplications).thenReturn(mockApplications);
            ApplicantService applicantService = Mockito.spy(new ApplicantService());
            doNothing().when(applicantService).viewApplicantApplications();

            // Invalid application ID given
            applicantService.viewInterview("999g");

            // Assert
            // Get the complete output
            String output = outContent.toString().trim();

            // Extract the last println message (messages are separated by newlines)
            String[] lines = output.split("\n");
            String lastMessage = lines[lines.length - 1];

            assertEquals("Application with given ID not found", lastMessage);
        }
    }

    @Test
    public void testViewInterview_InterviewQuestionsFound() {
        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            // Arrange
            mockedUtility.when(Utility::getCommonInterviewQuestions).thenReturn(mockedInterviewQuestions);
            ArrayList<Assignment> interviewAssignments = new ArrayList<>();
            Assignment interviewAssignment = new Assignment("1", "1", "Interview", new ArrayList<>(), new ArrayList<>());
            interviewAssignment.setQuestions(Utility.getCommonInterviewQuestions());
            interviewAssignments.add(interviewAssignment);
            mockApplications.get(0).setInterviewAssignments(interviewAssignments);

            mockedUtility.when(Utility::getApplications).thenReturn(mockApplications);
            ApplicantService applicantService = Mockito.spy(new ApplicantService());
            doNothing().when(applicantService).viewApplicationProcessDashboard("1");

            // Act
            applicantService.viewInterview("1");

            // Assert
            // Get the complete output
            String output = outContent.toString().trim();

            // Extract the last println message (messages are separated by newlines)
            String[] lines = output.split("\n");

            // Filter string
            List<String> filteredLines = new ArrayList<>();

            for (String str : lines) {
                String trimmed = str.trim();
                if (!trimmed.isEmpty()) {
                    filteredLines.add(trimmed);
                }
            }

            assertTrue(filteredLines.contains("[1] Why are you interested in this position?"));
        }
    }

    /*
     * Author: Mohammad Ansar Patil (map66)
     * User Story: 34
     */ 
    @Test
    public void viewSpecificApplicationTest_ApplicationIDIsEmpty() throws IOException {
        ApplicantService service = new ApplicantService();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        ApplicantService spyObject = Mockito.spy(service);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {

            Mockito.doNothing().when(spyObject).viewApplicationProcessDashboard(Mockito.anyString());
            Mockito.doNothing().when(spyObject).viewApplicantApplications();
            Mockito.doNothing().when(spyObject).withdrawApplication(Mockito.anyString());
            Mockito.doNothing().when(spyObject).viewJobDescFromApplication(Mockito.anyString());

            spyObject.viewSpecificApplication(null);
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Application ID is required."));
            outputStream.reset();
            spyObject.viewSpecificApplication("");
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Application ID is required."));
            outputStream.reset();
        }
    }

    /*
     * Author: Mohammad Ansar Patil (map66)
     * User Story: 34
     */ 
    @Test
    public void viewSpecificApplicationTest_ApplicationIDNotFound() throws IOException {
        ApplicantService service = new ApplicantService();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        ApplicantService spyObject = Mockito.spy(service);
        Applicant mockApplicant = new Applicant("U101", "John", "Doe", "johndoe", "bestpassword");
        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            when(Utility.getCurrentUser()).thenReturn(mockApplicant);
            Mockito.doNothing().when(spyObject).viewApplicationProcessDashboard(Mockito.anyString());
            Mockito.doNothing().when(spyObject).viewApplicantApplications();
            Mockito.doNothing().when(spyObject).withdrawApplication(Mockito.anyString());
            Mockito.doNothing().when(spyObject).viewJobDescFromApplication(Mockito.anyString());

            spyObject.viewSpecificApplication("A999");
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Application with ID A999 not found."));
            outputStream.reset();

        }
    }

    /*
     * Author: Mohammad Ansar Patil (map66)
     * User Story: 34
     */ 
    @Test
    public void viewSpecificApplicationTest_Case1() throws IOException {
        ApplicantService service = new ApplicantService();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        ApplicantService spyObject = Mockito.spy(service);
        ArrayList<String> questions = new ArrayList<>();
        ArrayList<String> answers = new ArrayList<>();

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {

            Mockito.doNothing().when(spyObject).viewApplicationProcessDashboard(Mockito.anyString());
            Mockito.doNothing().when(spyObject).viewApplicantApplications();
            Mockito.doNothing().when(spyObject).withdrawApplication(Mockito.anyString());
            Mockito.doNothing().when(spyObject).viewJobDescFromApplication(Mockito.anyString());

            User mockUser = new User("U101", "John", "Doe", "johndoe", "bestpassword", UserRole.APPLICANT);
            mockedUtility.when(Utility::getCurrentUser).thenReturn(mockUser);

            questions.add("Q1");
            answers.add("A1");

            ArrayList<Application> mockApplications = new ArrayList<>();
            mockApplications.add(new Application("A101", "J101", "U101", ApplicationStatus.INPROGRESS,
                new ArrayList<>(List.of(new Assignment("Assign1", "U101", "Assignment 1", questions, answers))), null, null, null, null));
            mockedUtility.when(Utility::getApplications).thenReturn(mockApplications);

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("1");
            spyObject.viewSpecificApplication("A101");
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Application ID: A101"));
            Assert.assertTrue(consoleOutput.contains("Job Id: J101"));
            Assert.assertTrue(consoleOutput.contains("Application Status: INPROGRESS"));
            Assert.assertTrue(consoleOutput.contains("Redirecting to application process dashboard"));
            outputStream.reset();

            Mockito.verify(spyObject, Mockito.times(1)).viewApplicationProcessDashboard(Mockito.anyString());
            mockedUtility.verify(Mockito.times(1), () -> Utility.inputOutput(Mockito.anyString()));
        }
    }

    /*
     * Author: Mohammad Ansar Patil (map66)
     * User Story: 34
     */ 
    @Test
    public void viewSpecificApplicationTest_Case2() throws IOException {
        ApplicantService service = new ApplicantService();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        ApplicantService spyObject = Mockito.spy(service);
        ArrayList<String> questions = new ArrayList<>();
        ArrayList<String> answers = new ArrayList<>();

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {

            Mockito.doNothing().when(spyObject).viewApplicationProcessDashboard(Mockito.anyString());
            Mockito.doNothing().when(spyObject).viewApplicantApplications();
            Mockito.doNothing().when(spyObject).withdrawApplication(Mockito.anyString());
            Mockito.doNothing().when(spyObject).viewJobDescFromApplication(Mockito.anyString());

            User mockUser = new User("U101", "John", "Doe", "johndoe", "bestpassword", UserRole.APPLICANT);
            mockedUtility.when(Utility::getCurrentUser).thenReturn(mockUser);

            questions.add("Q1");
            answers.add("A1");

            ArrayList<Application> mockApplications = new ArrayList<>();
            mockApplications.add(new Application("A102", "J102", "U101", ApplicationStatus.SUCCESSFUL, new ArrayList<>(), null, null, null, null));
            mockedUtility.when(Utility::getApplications).thenReturn(mockApplications);

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("2");
            spyObject.viewSpecificApplication("A102");
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Application ID: A102"));
            Assert.assertTrue(consoleOutput.contains("Job Id: J102"));
            Assert.assertTrue(consoleOutput.contains("Application Status: SUCCESSFUL"));
            Assert.assertTrue(consoleOutput.contains("Redirecting to view job description page"));
            outputStream.reset();

            Mockito.verify(spyObject, Mockito.times(1)).viewJobDescFromApplication(Mockito.anyString());
            mockedUtility.verify(Mockito.times(1), () -> Utility.inputOutput(Mockito.anyString()));
        }
    }

    /*
     * Author: Mohammad Ansar Patil (map66)
     * User Story: 34
     */ 
    @Test
    public void viewSpecificApplicationTest_Case3() throws IOException {
        ApplicantService service = new ApplicantService();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        ApplicantService spyObject = Mockito.spy(service);
        ArrayList<String> questions = new ArrayList<>();
        ArrayList<String> answers = new ArrayList<>();

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {

            Mockito.doNothing().when(spyObject).viewApplicationProcessDashboard(Mockito.anyString());
            Mockito.doNothing().when(spyObject).viewApplicantApplications();
            Mockito.doNothing().when(spyObject).withdrawApplication(Mockito.anyString());
            Mockito.doNothing().when(spyObject).viewJobDescFromApplication(Mockito.anyString());

            User mockUser = new User("U101", "John", "Doe", "johndoe", "bestpassword", UserRole.APPLICANT);
            mockedUtility.when(Utility::getCurrentUser).thenReturn(mockUser);

            questions.add("Q1");
            answers.add("A1");

            ArrayList<Application> mockApplications = new ArrayList<>();
            mockApplications.add(new Application("A104", "J104", "U101", ApplicationStatus.INPROGRESS,
                new ArrayList<>(List.of(new Assignment("Assign2", "U101", "Assignment 1", new ArrayList<>(), new ArrayList<>()))), null, null, null, null));

            mockedUtility.when(Utility::getApplications).thenReturn(mockApplications);

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("3");
            spyObject.viewSpecificApplication("A104");
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Application ID: A104"));
            Assert.assertTrue(consoleOutput.contains("Job Id: J104"));
            Assert.assertTrue(consoleOutput.contains("Application Status: INPROGRESS"));
            Assert.assertTrue(consoleOutput.contains("Redirecting to withdraw application page"));
            outputStream.reset();

            Mockito.verify(spyObject, Mockito.times(1)).withdrawApplication(Mockito.anyString());
            mockedUtility.verify(Mockito.times(1), () -> Utility.inputOutput(Mockito.anyString()));
        }
    }
    
    /*
     * Author: Mohammad Ansar Patil (map66)
     * User Story: 34
     */ 
    @Test
    public void viewSpecificApplicationTest_Default() throws IOException {
        ApplicantService service = new ApplicantService();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        ApplicantService spyObject = Mockito.spy(service);
        ArrayList<String> questions = new ArrayList<>();
        ArrayList<String> answers = new ArrayList<>();

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {

            Mockito.doNothing().when(spyObject).viewApplicationProcessDashboard(Mockito.anyString());
            Mockito.doNothing().when(spyObject).viewApplicantApplications();
            Mockito.doNothing().when(spyObject).withdrawApplication(Mockito.anyString());
            Mockito.doNothing().when(spyObject).viewJobDescFromApplication(Mockito.anyString());

            User mockUser = new User("U101", "John", "Doe", "johndoe", "bestpassword", UserRole.APPLICANT);
            mockedUtility.when(Utility::getCurrentUser).thenReturn(mockUser);

            questions.add("Q1");
            answers.add("A1");

            ArrayList<Application> mockApplications = new ArrayList<>();
            mockApplications.add(new Application("A101", "J101", "U101", ApplicationStatus.INPROGRESS,
                new ArrayList<>(List.of(new Assignment("Assign1", "U101", "Assignment 1", questions, answers))), null, null, null, null));
            mockedUtility.when(Utility::getApplications).thenReturn(mockApplications);

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("invalid");
            spyObject.viewSpecificApplication("A101");
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("You entered invalid option, redirecting to applications page"));
            outputStream.reset();

            Mockito.verify(spyObject, Mockito.times(1)).viewApplicantApplications();
            mockedUtility.verify(Mockito.times(1), () -> Utility.inputOutput(Mockito.anyString()));
        }
    }

     /*
     * Author: Mohammad Ansar Patil (map66)
     * User Story: 35
     */ 
    @Test
    public void withdrawApplicationTest_Case1() throws IOException {
        ApplicantService service = new ApplicantService();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        ApplicantService spyObject = Mockito.spy(service);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {

            ArrayList<Application> mockApplications = new ArrayList<>();
            mockApplications.add(new Application("A101", "J101", "U101", ApplicationStatus.INPROGRESS, new ArrayList<>(), null, null, null, null));
            mockedUtility.when(Utility::getApplications).thenReturn(mockApplications);

            Mockito.doNothing().when(spyObject).viewApplicantApplications();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("1");
            spyObject.withdrawApplication("A101");
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Welcome to withdraw application page"));
            Assert.assertTrue(consoleOutput.contains("Withdrawing application"));
            Assert.assertFalse(mockApplications.stream().anyMatch(app -> "A101".equals(app.getId())));
            outputStream.reset();

            Mockito.verify(spyObject, Mockito.times(1)).withdrawApplication(Mockito.anyString());
            mockedUtility.verify(Mockito.times(1), () -> Utility.inputOutput(Mockito.anyString()));
            mockedUtility.verify(Mockito.times(1), Utility::getApplications);
        }
    }

     /*
     * Author: Mohammad Ansar Patil (map66)
     * User Story: 35
     */ 
    @Test
    public void withdrawApplicationTest_Case2() throws IOException {
        ApplicantService service = new ApplicantService();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        ApplicantService spyObject = Mockito.spy(service);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {

            ArrayList<Application> mockApplications = new ArrayList<>();
            mockApplications.add(new Application("A102", "J102", "U101", ApplicationStatus.SUCCESSFUL, new ArrayList<>(), null, null, null, null));

            mockedUtility.when(Utility::getApplications).thenReturn(mockApplications);

            Mockito.doNothing().when(spyObject).viewApplicantApplications();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("2");
            spyObject.withdrawApplication("A102");
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Welcome to withdraw application page"));
            Assert.assertTrue(consoleOutput.contains("Redirecting to applications page"));
            Assert.assertTrue(mockApplications.stream().anyMatch(app -> "A102".equals(app.getId())));
            outputStream.reset();

            Mockito.verify(spyObject, Mockito.times(1)).viewApplicantApplications();
            mockedUtility.verify(Mockito.times(1), () -> Utility.inputOutput(Mockito.anyString()));
        }
    }

     /*
     * Author: Mohammad Ansar Patil (map66)
     * User Story: 35
     */ 
    @Test
    public void withdrawApplicationTest_Default() throws IOException {
        ApplicantService service = new ApplicantService();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        ApplicantService spyObject = Mockito.spy(service);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {

            ArrayList<Application> mockApplications = new ArrayList<>();
            mockApplications.add(new Application("A103", "J103", "U102", ApplicationStatus.UNSUCCESSFUL, new ArrayList<>(), null, null, null, null));
            mockedUtility.when(Utility::getApplications).thenReturn(mockApplications);

            Mockito.doNothing().when(spyObject).viewApplicantApplications();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("invalid");
            spyObject.withdrawApplication("A103");
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Welcome to withdraw application page"));
            Assert.assertTrue(consoleOutput.contains("You entered invalid option"));
            Assert.assertTrue(mockApplications.stream().anyMatch(app -> "A103".equals(app.getId())));
            outputStream.reset();

            Mockito.verify(spyObject, Mockito.times(1)).withdrawApplication(Mockito.anyString());
            Mockito.verify(spyObject, Mockito.times(1)).viewApplicantApplications();
            mockedUtility.verify(Mockito.times(1), () -> Utility.inputOutput(Mockito.anyString()));
        }
    }

    /*
     * Author: Mohammad Ansar Patil (map66)
     * User Story: 39
     */ 
    @Test
    public void viewApplicationProcessDashboardTest_Case1() throws IOException {
        ApplicantService service = ApplicantService.getInstance();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        ApplicantService spyObject = Mockito.spy(service);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString()))
                .thenReturn("1");

            Mockito.doNothing().when(spyObject).viewAssessment(Mockito.anyString());
            Mockito.doNothing().when(spyObject).submitAssessmentForm(Mockito.anyString());
            Mockito.doNothing().when(spyObject).viewInterview(Mockito.anyString());
            Mockito.doNothing().when(spyObject).submitInterviewForm(Mockito.anyString());
            Mockito.doNothing().when(spyObject).viewFeedback(Mockito.anyString());
            Mockito.doNothing().when(spyObject).viewApplicantApplications();

            spyObject.viewApplicationProcessDashboard(Mockito.anyString());
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Redirecting to View assignments page"));
            Mockito.verify(spyObject, Mockito.times(1)).viewAssessment(Mockito.anyString());
            outputStream.reset();

            Mockito.verify(spyObject, Mockito.times(1)).viewApplicationProcessDashboard(Mockito.anyString());
            mockedUtility.verify(Mockito.times(1), () -> Utility.inputOutput(Mockito.anyString()));
        }
    }

    /*
     * Author: Mohammad Ansar Patil (map66)
     * User Story: 39
     */ 
    @Test
    public void viewApplicationProcessDashboardTest_Case2() throws IOException {
        ApplicantService service = ApplicantService.getInstance();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        ApplicantService spyObject = Mockito.spy(service);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString()))
                .thenReturn("2");

            Mockito.doNothing().when(spyObject).viewAssessment(Mockito.anyString());
            Mockito.doNothing().when(spyObject).submitAssessmentForm(Mockito.anyString());
            Mockito.doNothing().when(spyObject).viewInterview(Mockito.anyString());
            Mockito.doNothing().when(spyObject).submitInterviewForm(Mockito.anyString());
            Mockito.doNothing().when(spyObject).viewFeedback(Mockito.anyString());
            Mockito.doNothing().when(spyObject).viewApplicantApplications();

            spyObject.viewApplicationProcessDashboard(Mockito.anyString());
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Redirecting to submit assignments page"));
            Mockito.verify(spyObject, Mockito.times(1)).submitAssessmentForm(Mockito.anyString());
            outputStream.reset();

            Mockito.verify(spyObject, Mockito.times(1)).viewApplicationProcessDashboard(Mockito.anyString());
            mockedUtility.verify(Mockito.times(1), () -> Utility.inputOutput(Mockito.anyString()));
        }
    }

    /*
     * Author: Mohammad Ansar Patil (map66)
     * User Story: 39
     */ 
    @Test
    public void viewApplicationProcessDashboardTest_Case3() throws IOException {
        ApplicantService service = ApplicantService.getInstance();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        ApplicantService spyObject = Mockito.spy(service);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString()))
                .thenReturn("3");

            Mockito.doNothing().when(spyObject).viewAssessment(Mockito.anyString());
            Mockito.doNothing().when(spyObject).submitAssessmentForm(Mockito.anyString());
            Mockito.doNothing().when(spyObject).viewInterview(Mockito.anyString());
            Mockito.doNothing().when(spyObject).submitInterviewForm(Mockito.anyString());
            Mockito.doNothing().when(spyObject).viewFeedback(Mockito.anyString());
            Mockito.doNothing().when(spyObject).viewApplicantApplications();

            spyObject.viewApplicationProcessDashboard(Mockito.anyString());
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Redirecting to view interview questions page"));
            Mockito.verify(spyObject, Mockito.times(1)).viewInterview(Mockito.anyString());
            outputStream.reset();

            Mockito.verify(spyObject, Mockito.times(1)).viewApplicationProcessDashboard(Mockito.anyString());
            mockedUtility.verify(Mockito.times(1), () -> Utility.inputOutput(Mockito.anyString()));
        }
    }

    /*
     * Author: Mohammad Ansar Patil (map66)
     * User Story: 39
     */ 
    @Test
    public void viewApplicationProcessDashboardTest_Case4() throws IOException {
        ApplicantService service = ApplicantService.getInstance();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        ApplicantService spyObject = Mockito.spy(service);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString()))
                .thenReturn("4");

            Mockito.doNothing().when(spyObject).viewAssessment(Mockito.anyString());
            Mockito.doNothing().when(spyObject).submitAssessmentForm(Mockito.anyString());
            Mockito.doNothing().when(spyObject).viewInterview(Mockito.anyString());
            Mockito.doNothing().when(spyObject).submitInterviewForm(Mockito.anyString());
            Mockito.doNothing().when(spyObject).viewFeedback(Mockito.anyString());
            Mockito.doNothing().when(spyObject).viewApplicantApplications();

            spyObject.viewApplicationProcessDashboard(Mockito.anyString());
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Redirecting to submit interview answers page"));
            Mockito.verify(spyObject, Mockito.times(1)).submitInterviewForm(Mockito.anyString());
            outputStream.reset();

            Mockito.verify(spyObject, Mockito.times(1)).viewApplicationProcessDashboard(Mockito.anyString());
            mockedUtility.verify(Mockito.times(1), () -> Utility.inputOutput(Mockito.anyString()));
        }
    }

    /*
     * Author: Mohammad Ansar Patil (map66)
     * User Story: 39
     */ 
    @Test
    public void viewApplicationProcessDashboardTest_Case5() throws IOException {
        ApplicantService service = ApplicantService.getInstance();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        ApplicantService spyObject = Mockito.spy(service);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString()))
                .thenReturn("5");

            Mockito.doNothing().when(spyObject).viewAssessment(Mockito.anyString());
            Mockito.doNothing().when(spyObject).submitAssessmentForm(Mockito.anyString());
            Mockito.doNothing().when(spyObject).viewInterview(Mockito.anyString());
            Mockito.doNothing().when(spyObject).submitInterviewForm(Mockito.anyString());
            Mockito.doNothing().when(spyObject).viewFeedback(Mockito.anyString());
            Mockito.doNothing().when(spyObject).viewApplicantApplications();

            spyObject.viewApplicationProcessDashboard(Mockito.anyString());
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Redirecting to view feedback page"));
            Mockito.verify(spyObject, Mockito.times(1)).viewFeedback(Mockito.anyString());
            outputStream.reset();

            Mockito.verify(spyObject, Mockito.times(1)).viewApplicationProcessDashboard(Mockito.anyString());
            mockedUtility.verify(Mockito.times(1), () -> Utility.inputOutput(Mockito.anyString()));
        }
    }

    /*
     * Author: Mohammad Ansar Patil (map66)
     * User Story: 39
     */ 
    @Test
    public void viewApplicationProcessDashboardTest_Case6() throws IOException {
        ApplicantService service = ApplicantService.getInstance();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        ApplicantService spyObject = Mockito.spy(service);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString()))
                .thenReturn("6");

            Mockito.doNothing().when(spyObject).viewAssessment(Mockito.anyString());
            Mockito.doNothing().when(spyObject).submitAssessmentForm(Mockito.anyString());
            Mockito.doNothing().when(spyObject).viewInterview(Mockito.anyString());
            Mockito.doNothing().when(spyObject).submitInterviewForm(Mockito.anyString());
            Mockito.doNothing().when(spyObject).viewFeedback(Mockito.anyString());
            Mockito.doNothing().when(spyObject).viewApplicantApplications();

            spyObject.viewApplicationProcessDashboard(Mockito.anyString());
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Go back to Applications page"));
            Mockito.verify(spyObject, Mockito.times(1)).viewApplicantApplications();
            outputStream.reset();

            Mockito.verify(spyObject, Mockito.times(1)).viewApplicationProcessDashboard(Mockito.anyString());
            mockedUtility.verify(Mockito.times(1), () -> Utility.inputOutput(Mockito.anyString()));
        }
    }

    /*
     * Author: Mohammad Ansar Patil (map66)
     * User Story: 39
     */ 
    @Test
    public void viewApplicationProcessDashboardTest_Default() throws IOException {
        ApplicantService service = ApplicantService.getInstance();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        ApplicantService spyObject = Mockito.spy(service);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString()))
                .thenReturn("invalid", "6");

            Mockito.doNothing().when(spyObject).viewAssessment(Mockito.anyString());
            Mockito.doNothing().when(spyObject).submitAssessmentForm(Mockito.anyString());
            Mockito.doNothing().when(spyObject).viewInterview(Mockito.anyString());
            Mockito.doNothing().when(spyObject).submitInterviewForm(Mockito.anyString());
            Mockito.doNothing().when(spyObject).viewFeedback(Mockito.anyString());
            Mockito.doNothing().when(spyObject).viewApplicantApplications();

            spyObject.viewApplicationProcessDashboard(Mockito.anyString());
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("You entered invalid option"));
            Mockito.verify(spyObject, Mockito.times(2)).viewApplicationProcessDashboard(Mockito.anyString());
            outputStream.reset();

            Mockito.verify(spyObject, Mockito.times(2)).viewApplicationProcessDashboard(Mockito.anyString());
            mockedUtility.verify(Mockito.times(2), () -> Utility.inputOutput(Mockito.anyString()));
        }
    }

    /*
     * Author: Mohammad Ansar Patil (map66)
     * User Story: 49
     */ 
    @Test
    public void viewFeedbackTest() throws IOException {
        ApplicantService service = ApplicantService.getInstance();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        ApplicantService spyObject = Mockito.spy(service);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            ArrayList<Application> mockApplications = new ArrayList<>();
            mockApplications.add(new Application("A101", "J101", "U101", ApplicationStatus.INPROGRESS,
                new ArrayList<>(), null, null, null, "Excellent performance, keep it up!"));
            mockApplications.add(new Application("A102", "J102", "U101", ApplicationStatus.SUCCESSFUL,
                new ArrayList<>(), null, "", null, ""));
            mockApplications.add(new Application("A103", "J103", "U102", ApplicationStatus.UNSUCCESSFUL,
                new ArrayList<>(), null, "Needs improvement, better luck next time.", null, null));

            mockedUtility.when(Utility::getApplications).thenReturn(mockApplications);

            Mockito.doNothing().when(spyObject).viewApplicationProcessDashboard(Mockito.anyString());

            spyObject.viewFeedback("A101");
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Welcome to view feedback page"));
            Assert.assertTrue(consoleOutput.contains("Feedback for A101"));
            Assert.assertTrue(consoleOutput.contains("Excellent performance, keep it up!"));
            Mockito.verify(spyObject, Mockito.times(1)).viewApplicationProcessDashboard("A101");
            outputStream.reset();

            spyObject.viewFeedback("A102");
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Welcome to view feedback page"));
            Assert.assertTrue(consoleOutput.contains("Feedback not received"));
            Mockito.verify(spyObject, Mockito.times(1)).viewApplicationProcessDashboard("A102");
            outputStream.reset();

            spyObject.viewFeedback("A999");
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Welcome to view feedback page"));
            Assert.assertFalse(consoleOutput.contains("Feedback for U999"));
            Assert.assertFalse(consoleOutput.contains("Feedback not received"));
            Mockito.verify(spyObject, Mockito.times(1)).viewApplicationProcessDashboard("A999");
            outputStream.reset();

            spyObject.viewFeedback("A101");
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Welcome to view feedback page"));
            Assert.assertTrue(consoleOutput.contains("Feedback for A101"));
            Assert.assertTrue(consoleOutput.contains("Excellent performance, keep it up!"));
            Mockito.verify(spyObject, Mockito.times(2)).viewApplicationProcessDashboard("A101");
            outputStream.reset();

            Mockito.verify(spyObject, Mockito.times(4)).viewFeedback(Mockito.anyString());
            mockedUtility.verify(Mockito.times(4), Utility::getApplications);
        }
    }

    /*
     * Author: Mohammad Ansar Patil (map66)
     * User Story: 49
     */ 
    @Test
    public void viewFeedbackTest_FeedbackReceived() throws IOException {
        ApplicantService service = ApplicantService.getInstance();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        ApplicantService spyObject = Mockito.spy(service);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            ArrayList<Application> mockApplications = new ArrayList<>();
            mockApplications.add(new Application("A101", "J101", "U101", ApplicationStatus.INPROGRESS,
                new ArrayList<>(), null, null, null, "Excellent performance, keep it up!"));
            mockedUtility.when(Utility::getApplications).thenReturn(mockApplications);

            Mockito.doNothing().when(spyObject).viewApplicationProcessDashboard(Mockito.anyString());

            spyObject.viewFeedback("A101");
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Welcome to view feedback page"));
            Assert.assertTrue(consoleOutput.contains("Feedback for A101"));
            Assert.assertTrue(consoleOutput.contains("Excellent performance, keep it up!"));
            Mockito.verify(spyObject, Mockito.times(1)).viewApplicationProcessDashboard("A101");
            outputStream.reset();

            Mockito.verify(spyObject, Mockito.times(1)).viewFeedback(Mockito.anyString());
            mockedUtility.verify(Mockito.times(1), Utility::getApplications);
        }
    }

    /*
     * Author: Mohammad Ansar Patil (map66)
     * User Story: 49
     */ 
    @Test
    public void viewFeedbackTest_FeedbackNotReceived() throws IOException {
        ApplicantService service = ApplicantService.getInstance();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        ApplicantService spyObject = Mockito.spy(service);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            ArrayList<Application> mockApplications = new ArrayList<>();
            mockApplications.add(new Application("A102", "J102", "U101", ApplicationStatus.SUCCESSFUL,
                new ArrayList<>(), null, "", null, ""));

            mockedUtility.when(Utility::getApplications).thenReturn(mockApplications);

            Mockito.doNothing().when(spyObject).viewApplicationProcessDashboard(Mockito.anyString());

            spyObject.viewFeedback("A102");
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Welcome to view feedback page"));
            Assert.assertTrue(consoleOutput.contains("Feedback not received"));
            Mockito.verify(spyObject, Mockito.times(1)).viewApplicationProcessDashboard("A102");
            outputStream.reset();

            Mockito.verify(spyObject, Mockito.times(1)).viewFeedback(Mockito.anyString());
            mockedUtility.verify(Mockito.times(1), Utility::getApplications);
        }
    }

    /*
     * Author: Mohammad Ansar Patil (map66)
     * User Story: 49
     */ 
    @Test
    public void viewFeedbackTest_InvalidApplicationId() throws IOException {
        ApplicantService service = ApplicantService.getInstance();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        ApplicantService spyObject = Mockito.spy(service);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            ArrayList<Application> mockApplications = new ArrayList<>();
        
            mockedUtility.when(Utility::getApplications).thenReturn(mockApplications);

            Mockito.doNothing().when(spyObject).viewApplicationProcessDashboard(Mockito.anyString());

            spyObject.viewFeedback("A999");
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Welcome to view feedback page"));
            Assert.assertFalse(consoleOutput.contains("Feedback for U999"));
            Assert.assertFalse(consoleOutput.contains("Feedback not received"));
            Mockito.verify(spyObject, Mockito.times(1)).viewApplicationProcessDashboard("A999");
            outputStream.reset();

            Mockito.verify(spyObject, Mockito.times(1)).viewFeedback(Mockito.anyString());
            mockedUtility.verify(Mockito.times(1), Utility::getApplications);
        }
    }

    /*
     * Author: Mohammad Ansar Patil (map66)
     * User Story: 02
     */ 
    @Test
    public void applicantViewSignInSignUpPageTest_Case1() throws IOException {
        ApplicantService service = ApplicantService.getInstance();
        CommonService commonServiceService = CommonService.getInstance();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        ApplicantService spyObject = Mockito.spy(service);
        CommonService commonSpyObject = Mockito.spy(commonServiceService);
        
        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class);
            MockedStatic<CommonService> mockACommonService = Mockito.mockStatic(CommonService.class)
        ) {
            Mockito.doNothing().when(spyObject).signIn();
            Mockito.doNothing().when(spyObject).signUp();
            Mockito.doNothing().when(commonSpyObject).accessLandingPage();
    
            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("1");
            spyObject.applicantViewSignInSignUpPage();
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Redirecting to Applicant Sign In Page"));
            Mockito.verify(spyObject, Mockito.times(1)).signIn();
            outputStream.reset();

            Mockito.verify(spyObject, Mockito.times(1)).signIn();
            mockedUtility.verify(Mockito.times(1), () -> Utility.inputOutput(Mockito.anyString()));
        }
    }

    /*
     * Author: Mohammad Ansar Patil (map66)
     * User Story: 02
     */ 
    @Test
    public void applicantViewSignInSignUpPageTest_Case2() throws IOException {
        ApplicantService service = ApplicantService.getInstance();
        CommonService commonServiceService = CommonService.getInstance();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        ApplicantService spyObject = Mockito.spy(service);
        CommonService commonSpyObject = Mockito.spy(commonServiceService);
        
        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class);
            MockedStatic<CommonService> mockACommonService = Mockito.mockStatic(CommonService.class)
        ) {
            Mockito.doNothing().when(spyObject).signIn();
            Mockito.doNothing().when(spyObject).signUp();
            Mockito.doNothing().when(commonSpyObject).accessLandingPage();
    
            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("2");
            spyObject.applicantViewSignInSignUpPage();
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Redirecting to Applicant Sign Up Page"));
            Mockito.verify(spyObject, Mockito.times(1)).signUp();
            outputStream.reset();
    
            Mockito.verify(spyObject, Mockito.times(1)).signUp();
            mockedUtility.verify(Mockito.times(1), () -> Utility.inputOutput(Mockito.anyString()));
        }
    }

    /*
     * Author: Mohammad Ansar Patil (map66)
     * User Story: 02
     */ 
    @Test
    public void applicantViewSignInSignUpPageTest_Case3() throws IOException {
        ApplicantService service = ApplicantService.getInstance();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        ApplicantService spyObject = Mockito.spy(service);
    
        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class);
            MockedStatic<CommonService> mockACommonService = Mockito.mockStatic(CommonService.class)
        ) {

            CommonService mockCom=Mockito.mock(CommonService.class);
            mockACommonService.when(CommonService::getInstance).thenReturn(mockCom);

            Mockito.doNothing().when(spyObject).signIn();
            Mockito.doNothing().when(spyObject).signUp();
            Mockito.doNothing().when(mockCom).accessLandingPage();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("3");
            spyObject.applicantViewSignInSignUpPage();
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Redirecting to previous menu"));
            outputStream.reset();
    
            mockedUtility.verify(Mockito.times(1), () -> Utility.inputOutput(Mockito.anyString()));
        }
    }

    /*
     * Author: Mohammad Ansar Patil (map66)
     * User Story: 02
     */ 
    @Test
    public void applicantViewSignInSignUpPageTest_Default() throws IOException {
        ApplicantService service = ApplicantService.getInstance();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        ApplicantService spyObject = Mockito.spy(service);
    
        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class);
            MockedStatic<CommonService> mockACommonService = Mockito.mockStatic(CommonService.class)
        ) {

            CommonService mockCom=Mockito.mock(CommonService.class);
            mockACommonService.when(CommonService::getInstance).thenReturn(mockCom);

            Mockito.doNothing().when(spyObject).signIn();
            Mockito.doNothing().when(spyObject).signUp();
            Mockito.doNothing().when(mockCom).accessLandingPage();
    
            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("invalid", "3");
            spyObject.applicantViewSignInSignUpPage();
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("You entered invalid option"));
            Assert.assertTrue(consoleOutput.contains("Redirecting to previous menu"));
            outputStream.reset();
    
            mockedUtility.verify(Mockito.times(2), () -> Utility.inputOutput(Mockito.anyString()));
        }
    }

    /*
     * Author: Mohammad Ansar Patil (map66)
     * User Story: 12
     */ 
    @Test
    public void resetPasswordTest_CorrectCode() {
        ApplicantService service = Mockito.spy(new ApplicantService());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        Mockito.doNothing().when(service).viewResetPasswordPage();
        Mockito.doNothing().when(service).viewApplicantDashboard();

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            User mockCurrentUser = new User("U123", "John", "Doe", "john.doe@example.com", "password", UserRole.APPLICANT);
            mockCurrentUser.setUserName("JohnDoe");
            mockedUtility.when(Utility::getCurrentUser).thenReturn(mockCurrentUser);
            User otherUser = new User("U124", "Jane", "Smith", "jane.smith@example.com", "password",UserRole.APPLICANT);
            otherUser.setUserName("JaneSmith");
            ArrayList<User> mockUsers = new ArrayList<>();
            mockUsers.add(mockCurrentUser);
            mockUsers.add(otherUser);
            mockedUtility.when(Utility::getUsers).thenReturn(mockUsers);

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString()))
                    .thenReturn("XVQTY", "newPassword123");
            service.resetPassword("JohnDoe");
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Your entered username: JohnDoe"));
            Assert.assertTrue(consoleOutput.contains("Redirecting to Applicant dashboard"));
            Assert.assertEquals("newPassword123", mockCurrentUser.getPassword());
            outputStream.reset();

            mockedUtility.verify(Mockito.times(6), Utility::getCurrentUser);
            mockedUtility.verify(Mockito.times(1), Utility::getUsers);
            mockedUtility.verify(Mockito.times(2), () -> Utility.inputOutput(Mockito.anyString()));
        }
    }
    
    /*
     * Author: Mohammad Ansar Patil (map66)
     * User Story: 12
     */ 
    @Test
    public void resetPasswordTest_WrongCode() {
        ApplicantService service = Mockito.spy(new ApplicantService());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        Mockito.doNothing().when(service).viewResetPasswordPage();
        Mockito.doNothing().when(service).viewApplicantDashboard();

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            User mockCurrentUser = new User("U123", "John", "Doe", "john.doe@example.com", "password", UserRole.APPLICANT);
            mockCurrentUser.setUserName("JohnDoe");
            mockedUtility.when(Utility::getCurrentUser).thenReturn(mockCurrentUser);
            User otherUser = new User("U124", "Jane", "Smith", "jane.smith@example.com", "password",UserRole.APPLICANT);
            otherUser.setUserName("JaneSmith");
            ArrayList<User> mockUsers = new ArrayList<>();
            mockUsers.add(mockCurrentUser);
            mockUsers.add(otherUser);
            mockedUtility.when(Utility::getUsers).thenReturn(mockUsers);

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString()))
                    .thenReturn("WRONGCODE", "newPassword123");
            service.resetPassword("JohnDoe");
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("You have entered wrong Reset Code"));
            Assert.assertTrue(consoleOutput.contains("Redirecting to Applicant dashboard"));
            outputStream.reset();

            mockedUtility.verify(Mockito.times(6), Utility::getCurrentUser);
            mockedUtility.verify(Mockito.times(1), Utility::getUsers);
            mockedUtility.verify(Mockito.times(2), () -> Utility.inputOutput(Mockito.anyString()));
        }
    }
    
    /*
     * Author: Mohammad Ansar Patil (map66)
     * User Story: 12
     */ 
    @Test
    public void resetPasswordTest_WrongUserName() {
        ApplicantService service = Mockito.spy(new ApplicantService());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        Mockito.doNothing().when(service).viewResetPasswordPage();
        Mockito.doNothing().when(service).viewApplicantDashboard();

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            User mockCurrentUser = new User("U123", "John", "Doe", "john.doe@example.com", "password", UserRole.APPLICANT);
            mockCurrentUser.setUserName("JohnDoe");
            mockedUtility.when(Utility::getCurrentUser).thenReturn(mockCurrentUser);
            User otherUser = new User("U124", "Jane", "Smith", "jane.smith@example.com", "password",UserRole.APPLICANT);
            otherUser.setUserName("JaneSmith");
            ArrayList<User> mockUsers = new ArrayList<>();
            mockUsers.add(mockCurrentUser);
            mockUsers.add(otherUser);
            mockedUtility.when(Utility::getUsers).thenReturn(mockUsers);
        
            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString()))
                    .thenReturn("XVQTY", "newPassword123");
            service.resetPassword("WrongUser");
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("You have entered wrong Crediantials"));
            outputStream.reset();

            mockedUtility.verify(Mockito.times(3), Utility::getCurrentUser);
            mockedUtility.verify(Mockito.times(1), () -> Utility.inputOutput(Mockito.anyString()));
        }
    }

    /*
     * Author: Mohammad Ansar Patil (map66)
     * User Story: 10
     */ 
    @Test
    public void viewResetPasswordPageTest_Case1() {
        ApplicantService service = Mockito.spy(new ApplicantService());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    
        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            User mockUser = new User("U123", "John", "Doe", "john.doe@example.com", "password",UserRole.APPLICANT);
            mockedUtility.when(Utility::getCurrentUser).thenReturn(mockUser);
    
            Mockito.doNothing().when(service).resetPassword(Mockito.anyString());
    
            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString()))
                         .thenReturn("1", "JohnDoe");
            service.viewResetPasswordPage();
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Welcome to reset password page"));
            Mockito.verify(service, Mockito.times(1)).resetPassword("JohnDoe");
            outputStream.reset();
    
            Mockito.verify(service, Mockito.times(1)).resetPassword("JohnDoe");
            mockedUtility.verify(Mockito.times(2), () -> Utility.inputOutput(Mockito.anyString()));
        }
    }

    /*
     * Author: Mohammad Ansar Patil (map66)
     * User Story: 10
     */ 
    @Test
    public void viewResetPasswordPageTest_Case2() {
        ApplicantService service = Mockito.spy(new ApplicantService());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    
        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class);
            MockedStatic<ApplicantService> mockApplicantService = Mockito.mockStatic(ApplicantService.class);
            MockedStatic<RecruiterService> mockRecruiterService = Mockito.mockStatic(RecruiterService.class);
        ) {
            User mockUser = new User("U123", "John", "Doe", "john.doe@example.com", "password",UserRole.APPLICANT);
            mockedUtility.when(Utility::getCurrentUser).thenReturn(mockUser);

            ApplicantService mockApp=Mockito.mock(ApplicantService.class);
            RecruiterService mockRec=Mockito.mock(RecruiterService.class);            
            mockApplicantService.when(ApplicantService::getInstance).thenReturn(mockApp);
            mockRecruiterService.when(RecruiterService::getInstance).thenReturn(mockRec);

    
            Mockito.doNothing().when(service).resetPassword(Mockito.anyString());
    
            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("2");
            Mockito.doNothing().when(mockApp).viewApplicantDashboard();
            Mockito.doNothing().when(mockRec).viewRecruiterDashboard();

            service.viewResetPasswordPage();
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Redirecting to Applicant dashboard"));
            Mockito.verify(mockApp, Mockito.times(1)).viewApplicantDashboard();
            outputStream.reset();
    
            mockUser.setRole(UserRole.RECRUITER);
            mockedUtility.when(Utility::getCurrentUser).thenReturn(mockUser);
            Mockito.doNothing().when(mockRec).viewRecruiterDashboard();
    
            service.viewResetPasswordPage();
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Redirecting to Recruiter dashboard"));
            Mockito.verify(mockRec, Mockito.times(1)).viewRecruiterDashboard();
            outputStream.reset();

            mockedUtility.verify(Mockito.times(2), () -> Utility.inputOutput(Mockito.anyString()));
        }
    }

    /*
     * Author: Mohammad Ansar Patil (map66)
     * User Story: 10
     */ 
    @Test
    public void viewResetPasswordPageTest_Default() {
        ApplicantService service = Mockito.spy(new ApplicantService());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    
        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class);
            MockedStatic<ApplicantService> mockApplicantService = Mockito.mockStatic(ApplicantService.class)
        ) {
            User mockUser = new User("U123", "John", "Doe", "john.doe@example.com", "password",UserRole.APPLICANT);
            mockedUtility.when(Utility::getCurrentUser).thenReturn(mockUser);

            ApplicantService mockApp=Mockito.mock(ApplicantService.class);
    
            Mockito.doNothing().when(service).resetPassword(Mockito.anyString());

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("2");
            mockApplicantService.when(ApplicantService::getInstance).thenReturn(mockApp);

            Mockito.doNothing().when(mockApp).viewApplicantDashboard();
    
            service.viewResetPasswordPage();
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Redirecting to Applicant dashboard"));
            Mockito.verify(mockApp, Mockito.times(1)).viewApplicantDashboard();
            outputStream.reset();
            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("invalid", "2");
            service.viewResetPasswordPage();
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("You entered invalid option"));
            Assert.assertTrue(consoleOutput.contains("Redirecting to Dashboard"));
            outputStream.reset();
    
            mockedUtility.verify(Mockito.times(3), () -> Utility.inputOutput(Mockito.anyString()));
        }
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 17
     */
    @Test
    public void testUpdateProfile_existingUserIsReplaced() {
        User oldUser = new Applicant("123", "OldName", "OldLast", "oldApplicantname", "oldPass");
        User otherUser = new Applicant("456", "Another", "User", "anotherUser", "anotherPass");
        ArrayList<User> mockUsers = new ArrayList<>();
        mockUsers.add(oldUser);
        mockUsers.add(otherUser);

        User updatedUser = new Applicant("123", "NewName", "NewLast", "oldUsername", "newPass");

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            mockedUtility.when(Utility::getUsers).thenReturn(mockUsers);

            applicantService.updateProfile(updatedUser);

            assertEquals("Size remains 2 because we replaced one user with the updated one.", 2, mockUsers.size());

            Optional<User> foundOldUser = mockUsers.stream()
                    .filter(u -> u.getId().equals("123") && u.getName().equals("OldName"))
                    .findFirst();
            assertFalse("Old user should be removed from the list.", foundOldUser.isPresent());

            Optional<User> foundUpdatedUser = mockUsers.stream()
                    .filter(u -> u.getId().equals("123") && u.getName().equals("NewName"))
                    .findFirst();
            assertTrue("Updated user should be in the list.", foundUpdatedUser.isPresent());

            mockedUtility.verify(atLeastOnce(), () -> Utility.setCurrentUser(updatedUser));
        }
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 17
     */
    @Test
    public void testUpdateProfile_userNotInList() {
        // Arrange
        ArrayList<User> mockUsers = new ArrayList<>();
        User existingUser = new Applicant("456", "Another", "User", "anotherUser", "anotherPass");
        mockUsers.add(existingUser);

        User updatedUser = new Applicant("999", "BrandNew", "User", "brandNew", "pass");

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            mockedUtility.when(Utility::getUsers).thenReturn(mockUsers);

            applicantService.updateProfile(updatedUser);

            assertEquals("Should add the new user because ID wasn't found.", 2, mockUsers.size());

            Optional<User> foundUpdatedUser = mockUsers.stream()
                    .filter(u -> u.getId().equals("999"))
                    .findFirst();
            assertTrue("New user should be added.", foundUpdatedUser.isPresent());

            mockedUtility.verify(atLeastOnce(), () -> Utility.setCurrentUser(updatedUser));
        }
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 17
     */
    @Test
    public void testShowUpdateProfilePage_changeBothNameAndLastName() {
        User currentUser = new Applicant("111", "OldFirst", "OldLast", "user111", "pw111");

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            mockedUtility.when(Utility::getCurrentUser).thenReturn(currentUser);

            mockedUtility.when(() -> Utility.inputOutput("Enter your name: "))
                    .thenReturn("NewFirst");
            mockedUtility.when(() -> Utility.inputOutput("Enter your last name: "))
                    .thenReturn("NewLast");

            ArrayList<User> mockUsers = new ArrayList<>();
            mockUsers.add(currentUser);
            mockedUtility.when(Utility::getUsers).thenReturn(mockUsers);

            ApplicantService spyService = Mockito.spy(applicantService);
            doNothing().when(spyService).viewApplicantProfilePage();

            spyService.showUpdateProfilePage();

            assertEquals("NewFirst", currentUser.getName());
            assertEquals("NewLast", currentUser.getLastName());

            verify(spyService, times(1)).updateProfile(currentUser);

            verify(spyService, times(1)).viewApplicantProfilePage();
        }
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 17
     */
    @Test
    public void testShowUpdateProfilePage_noChanges() {
        User currentUser = new Applicant("222", "OriginalFirst", "OriginalLast", "user222", "pw222");

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            mockedUtility.when(Utility::getCurrentUser).thenReturn(currentUser);

            mockedUtility.when(() -> Utility.inputOutput("Enter your name: "))
                    .thenReturn("");
            mockedUtility.when(() -> Utility.inputOutput("Enter your last name: "))
                    .thenReturn("");

            ArrayList<User> mockUsers = new ArrayList<>();
            mockUsers.add(currentUser);
            mockedUtility.when(Utility::getUsers).thenReturn(mockUsers);

            ApplicantService spyService = Mockito.spy(applicantService);
            doNothing().when(spyService).viewApplicantProfilePage();

            spyService.showUpdateProfilePage();

            assertEquals("OriginalFirst", currentUser.getName());
            assertEquals("OriginalLast", currentUser.getLastName());

            verify(spyService, times(1)).updateProfile(currentUser);
            verify(spyService, times(1)).viewApplicantProfilePage();
        }
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 17
     */
    @Test
    public void testShowUpdateProfilePage_changeNameOnly() {
        User currentUser = new Applicant("333", "OldF", "OldL", "user333", "pw333");

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            mockedUtility.when(Utility::getCurrentUser).thenReturn(currentUser);

            mockedUtility.when(() -> Utility.inputOutput("Enter your name: "))
                    .thenReturn("NewF");
            mockedUtility.when(() -> Utility.inputOutput("Enter your last name: "))
                    .thenReturn("");

            ArrayList<User> mockUsers = new ArrayList<>();
            mockUsers.add(currentUser);
            mockedUtility.when(Utility::getUsers).thenReturn(mockUsers);

            ApplicantService spyService = Mockito.spy(applicantService);
            doNothing().when(spyService).viewApplicantProfilePage();

            spyService.showUpdateProfilePage();

            assertEquals("NewF", currentUser.getName());
            assertEquals("OldL", currentUser.getLastName());

            verify(spyService, times(1)).updateProfile(currentUser);
            verify(spyService, times(1)).viewApplicantProfilePage();
        }
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 17
     */
    @Test
    public void testShowUpdateProfilePage_changeLastNameOnly() {
        User currentUser = new Applicant("444", "FirstX", "LastX", "user444", "pw444");

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            mockedUtility.when(Utility::getCurrentUser).thenReturn(currentUser);

            mockedUtility.when(() -> Utility.inputOutput("Enter your name: "))
                    .thenReturn("");
            mockedUtility.when(() -> Utility.inputOutput("Enter your last name: "))
                    .thenReturn("NewLast");

            ArrayList<User> mockUsers = new ArrayList<>();
            mockUsers.add(currentUser);
            mockedUtility.when(Utility::getUsers).thenReturn(mockUsers);

            ApplicantService spyService = Mockito.spy(applicantService);
            doNothing().when(spyService).viewApplicantProfilePage();

            spyService.showUpdateProfilePage();

            assertEquals("FirstX", currentUser.getName());
            assertEquals("NewLast", currentUser.getLastName());

            verify(spyService, times(1)).updateProfile(currentUser);
            verify(spyService, times(1)).viewApplicantProfilePage();
        }
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 18
     */
    @Test
    public void testDeleteProfileHelper_removesCurrentUserAndSetsNull() {
        User currentUser = new Applicant("123", "John", "Doe", "johnD", "pw123");
        User anotherUser = new Applicant("999", "Other", "User", "otherU", "pw999");

        ArrayList<User> mockUsers = new ArrayList<>();
        mockUsers.add(currentUser);
        mockUsers.add(anotherUser);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            mockedUtility.when(Utility::getCurrentUser).thenReturn(currentUser);
            mockedUtility.when(Utility::getUsers).thenReturn(mockUsers);

            applicantService.deleteProfileHelper();

            assertEquals("User list should have exactly 1 left",
                    1,
                    mockUsers.size());

            assertEquals("Remaining user should be '999'",
                    "999",
                    mockUsers.get(0).getId());

            mockedUtility.verify(atLeastOnce(), () -> Utility.setUsers(mockUsers));

            mockedUtility.verify(atLeastOnce(), () -> Utility.setCurrentUser(null));
        }
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 18
     */
    @Test
    public void testDeleteProfileHelper_noMatchingCurrentUser() {
        // Scenario: The currentUser is not in the list
        User currentUser = new Applicant("888", "Lost", "User", "lostU", "pw888");
        User anotherUser = new Applicant("999", "Other", "User", "otherU", "pw999");

        ArrayList<User> mockUsers = new ArrayList<>();
        mockUsers.add(anotherUser);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            mockedUtility.when(Utility::getCurrentUser).thenReturn(currentUser);
            mockedUtility.when(Utility::getUsers).thenReturn(mockUsers);

            applicantService.deleteProfileHelper();

            assertEquals("User list should remain size 1",
                    1,
                    mockUsers.size());

            assertEquals("Remaining user should be '999'",
                    "999",
                    mockUsers.get(0).getId());

            mockedUtility.verify(atLeastOnce(), () -> Utility.setUsers(mockUsers));
            mockedUtility.verify(atLeastOnce(), () -> Utility.setCurrentUser(null));
        }
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 18
     */
    @Test
    public void testDeleteApplicantProfile_userChoosesYes() {
        // User enters "Y"
        ApplicantService spyService = Mockito.spy(applicantService);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            mockedUtility
                    .when(() -> Utility.inputOutput("Are you sure you want to delete your profile? (Y/N)"))
                    .thenReturn("Y");

            doNothing().when(spyService).deleteProfileHelper();
            doNothing().when(spyService).applicantViewSignInSignUpPage();

            spyService.deleteApplicantProfile();

            verify(spyService, times(1)).deleteProfileHelper();
            verify(spyService, times(1)).applicantViewSignInSignUpPage();
        }
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 18
     */
    @Test
    public void testDeleteApplicantProfile_userChoosesNo() {
        // User enters "N"
        ApplicantService spyService = Mockito.spy(applicantService);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            mockedUtility
                    .when(() -> Utility.inputOutput("Are you sure you want to delete your profile? (Y/N)"))
                    .thenReturn("N");

            doNothing().when(spyService).viewApplicantProfilePage();

            spyService.deleteApplicantProfile();

            verify(spyService, never()).deleteProfileHelper();
            verify(spyService, never()).applicantViewSignInSignUpPage();

            verify(spyService, times(1)).viewApplicantProfilePage();
        }
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 18
     */
    @Test
    public void testDeleteApplicantProfile_invalidInput() {
        ApplicantService spyService = Mockito.spy(applicantService);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            mockedUtility
                    .when(() -> Utility.inputOutput("Are you sure you want to delete your profile? (Y/N)"))
                    .thenReturn("Maybe");

            spyService.deleteApplicantProfile();

            verify(spyService, never()).deleteProfileHelper();
            verify(spyService, never()).applicantViewSignInSignUpPage();
            verify(spyService, never()).viewApplicantProfilePage();
        }
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 29
     */
    @Test
    public void testViewApplicantApplications_currentUserIsRecruiter() {
        ApplicantService spyService = Mockito.spy(applicantService);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            User recruiterUser = new Recruiter("R123", "Recruit", "User", "recruitUser", "pass");
            mockedUtility.when(Utility::getCurrentUser).thenReturn(recruiterUser);

            doNothing().when(spyService).viewApplicantDashboard();

            spyService.viewApplicantApplications();

            String consoleOutput = outContent.toString();
            assertEquals(
                    "Should print 'You are not an applicant.'\n",
                    true,
                    consoleOutput.contains("You are not an applicant."));
            verify(spyService, times(1)).viewApplicantDashboard();
        } finally {
            System.setOut(originalOut);
        }
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 29
     */
    @Test
    public void testViewApplicantApplications_noApplications() {

        ApplicantService spyService = Mockito.spy(applicantService);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            Applicant applicantUser = new Applicant("A111", "AppFirst", "AppLast", "appUser", "pass");

            mockedUtility.when(Utility::getCurrentUser).thenReturn(applicantUser);

            doNothing().when(spyService).viewApplicantDashboard();

            spyService.viewApplicantApplications();

            String consoleOutput = outContent.toString();
            assertEquals(
                    "Should print 'No applications found.'\n",
                    true,
                    consoleOutput.contains("No applications found."));
            verify(spyService, times(1)).viewApplicantDashboard();
        } finally {
            System.setOut(originalOut);
        }
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 29
     */
    @Test
    public void testViewApplicantApplications_hasApplications_userChooses1() {

        ApplicantService spyService = Mockito.spy(applicantService);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            Applicant applicantUser = new Applicant("A222", "First", "Last", "appUser2", "pass2");
            ArrayList<Application> apps = new ArrayList<>();
            apps.add(new Application("app1", "job1", "A222", null, null, 1, "Bachelors", "Java", ""));
            apps.add(new Application("app2", "job2", "A222", null, null, 2, "Masters", "Python", ""));

            mockedUtility.when(Utility::getCurrentUser).thenReturn(applicantUser);
            mockedUtility.when(Utility::getApplications).thenReturn(apps);
            mockedUtility
                    .when(() -> Utility.inputOutput("Please Select One Of The Options"))
                    .thenReturn("1");
            mockedUtility
                    .when(() -> Utility.inputOutput("Enter the Application ID"))
                    .thenReturn("app2");

            doNothing().when(spyService).viewSpecificApplication("app2");

            doNothing().when(spyService).viewApplicantDashboard();

            spyService.viewApplicantApplications();

            String consoleOutput = outContent.toString();
            assertEquals(
                    "Should list 'app1' and 'app2' in the console output",
                    true,
                    consoleOutput.contains("app1") && consoleOutput.contains("app2"));
            verify(spyService, times(1)).viewSpecificApplication("app2");

            verify(spyService, never()).viewApplicantDashboard();
        } finally {
            System.setOut(originalOut);
        }
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 29
     */
    @Test
    public void testViewApplicantApplications_hasApplications_userChooses2() {

        ApplicantService spyService = Mockito.spy(applicantService);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            Applicant applicantUser = new Applicant("A333", "FName", "LName", "appUser3", "pass3");
            ArrayList<Application> apps = new ArrayList<>();
            apps.add(new Application("appX", "jobX", "A333", null, null, 1, "Bachelors", "C++", ""));

            mockedUtility.when(Utility::getCurrentUser).thenReturn(applicantUser);
            mockedUtility.when(Utility::getApplications).thenReturn(apps);

            mockedUtility
                    .when(() -> Utility.inputOutput("Please Select One Of The Options"))
                    .thenReturn("2");

            doNothing().when(spyService).viewApplicantDashboard();

            spyService.viewApplicantApplications();

            verify(spyService, times(1)).viewApplicantDashboard();

            verify(spyService, never()).viewSpecificApplication(anyString());
        } finally {
            System.setOut(originalOut);
        }
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 29
     */
    @Test
    public void testViewApplicantApplications_hasApplications_invalidInput() {

        ApplicantService spyService = Mockito.spy(applicantService);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            Applicant applicantUser = new Applicant("A444", "FName2", "LName2", "appUser4", "pass4");
            ArrayList<Application> apps = new ArrayList<>();
            apps.add(new Application("appY", "jobY", "A444", null, null, 3, "Masters", "Java", ""));

            mockedUtility.when(Utility::getCurrentUser).thenReturn(applicantUser);
            mockedUtility.when(Utility::getApplications).thenReturn(apps);

            mockedUtility
                    .when(() -> Utility.inputOutput("Please Select One Of The Options"))
                    .thenReturn("abc");

            doNothing().when(spyService).viewApplicantDashboard();

            spyService.viewApplicantApplications();

            String consoleOutput = outContent.toString();
            assertEquals(
                    "Should print 'Invalid input. Redirecting to the dashboard.'",
                    true,
                    consoleOutput.contains("Invalid input. Redirecting to the dashboard."));

            verify(spyService, times(1)).viewApplicantDashboard();
            verify(spyService, never()).viewSpecificApplication(anyString());
        } finally {
            System.setOut(originalOut);
        }
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 36
     */
    @Test
    public void testViewJobDescFromApplication_applicationNotFound() {

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            ArrayList<Application> applications = new ArrayList<>();
            mockedUtility.when(Utility::getApplications).thenReturn(applications);

            mockedUtility.when(Utility::getJobs).thenReturn(new ArrayList<Job>());

            String missingAppId = "missingApp";
            applicantService.viewJobDescFromApplication(missingAppId);

            String consoleOutput = outContent.toString();
            assertTrue(
                    "Should print that the application was not found",
                    consoleOutput.contains("Application with ID missingApp not found."));
        } finally {
            System.setOut(originalOut);
        }
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 36
     */
    @Test
    public void testViewJobDescFromApplication_jobNotFound() {

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            Application app = new Application("app123", "jobXYZ", "userA", null, null, 0, "", "", "");
            ArrayList<Application> applications = new ArrayList<>();
            applications.add(app);

            mockedUtility.when(Utility::getApplications).thenReturn(applications);

            ArrayList<Job> jobs = new ArrayList<>();
            mockedUtility.when(Utility::getJobs).thenReturn(jobs);

            applicantService.viewJobDescFromApplication("app123");

            String consoleOutput = outContent.toString();
            assertTrue(
                    "Should print that the job was not found",
                    consoleOutput.contains("Job with ID jobXYZ not found."));
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    public void testSignUp_AllValidInputs() {

        try (MockedStatic<Utility> utilityMock = mockStatic(Utility.class)) {

            utilityMock.when(() -> Utility.inputOutput("First Name: ")).thenReturn("John");
            utilityMock.when(() -> Utility.inputOutput("Last Name: ")).thenReturn("Doe");
            utilityMock.when(() -> Utility.inputOutput("User Name: ")).thenReturn("johndoe");
            utilityMock.when(() -> Utility.inputOutput("Password: ")).thenReturn("securePass123");

            List<User> mockUsers = new ArrayList<>();
            utilityMock.when(() -> Utility.getUsers()).thenReturn(mockUsers);

            ApplicantService applicantService = Mockito.spy(new ApplicantService());
            doNothing().when(applicantService).viewApplicantDashboard();


            applicantService.signUp();


            assertTrue(mockUsers.size() == 1);
            User addedUser = mockUsers.get(0);
            assertTrue(addedUser instanceof Applicant);
            assertTrue(addedUser.getName().equals("John"));
            assertTrue(addedUser.getLastName().equals("Doe"));
            assertTrue(addedUser.getUserName().equals("johndoe"));
            assertTrue(addedUser.getPassword().equals("securePass123"));


            utilityMock.verify(times(1),() -> Utility.setCurrentUser(addedUser));


            verify(applicantService, times(1)).viewApplicantDashboard();


            String consoleOutput = outContent.toString();
            assertTrue(consoleOutput.contains("Welcome to Applicant Sign Up Page"));
            assertTrue(consoleOutput.contains("Please enter the following details"));
            assertTrue(consoleOutput.contains("Sign Up Successful for Applicant"));
            assertTrue(consoleOutput.contains("Directing to Applicant Dashboard"));
        }
    }
    @Test
    public void testSignUp_EmptyFirstName_thenValidInputs() {

        try (MockedStatic<Utility> utilityMock = mockStatic(Utility.class)) {

            utilityMock.when(() -> Utility.inputOutput("First Name: "))
                      .thenReturn("", "John");

            utilityMock.when(() -> Utility.inputOutput("Last Name: "))
                      .thenReturn("Doe", "Doe");
            utilityMock.when(() -> Utility.inputOutput("User Name: "))
                      .thenReturn("johndoe", "johndoe");
            utilityMock.when(() -> Utility.inputOutput("Password: "))
                      .thenReturn("securePass123", "securePass123");

            // Mock Utility.getUsers() to return a mutable list
            List<User> mockUsers = new ArrayList<>();
            utilityMock.when(() -> Utility.getUsers()).thenReturn(mockUsers);
            ApplicantService applicantService = Mockito.spy(new ApplicantService());

            doNothing().when(applicantService).viewApplicantDashboard();

            applicantService.signUp();

            verify(applicantService, times(2)).signUp();


            assertTrue(mockUsers.size() == 1);
            User addedUser = mockUsers.get(0);
            assertTrue(addedUser instanceof Applicant);
            assertTrue(addedUser.getName().equals("John"));
            assertTrue(addedUser.getLastName().equals("Doe"));
            assertTrue(addedUser.getUserName().equals("johndoe"));
            assertTrue(addedUser.getPassword().equals("securePass123"));


            utilityMock.verify( times(1),() -> Utility.setCurrentUser(addedUser));


            verify(applicantService, times(1)).viewApplicantDashboard();

            String consoleOutput = outContent.toString();
            assertTrue(consoleOutput.contains("All fields are required. Please try again."));
            assertTrue(consoleOutput.contains("Sign Up Successful for Applicant"));
            assertTrue(consoleOutput.contains("Directing to Applicant Dashboard"));
        }
    }
    @Test
    public void testSignUp_EmptyAndNullInputs_thenValidInputs() {

        try (MockedStatic<Utility> utilityMock = mockStatic(Utility.class)) {

            utilityMock.when(() -> Utility.inputOutput("First Name: "))
                      .thenReturn("", "John"); // First call: empty, Second call: valid
            utilityMock.when(() -> Utility.inputOutput("Last Name: "))
                      .thenReturn(null, "Doe"); // First call: null, Second call: valid
            utilityMock.when(() -> Utility.inputOutput("User Name: "))
                      .thenReturn("johndoe", "johndoe"); // Both calls: valid
            utilityMock.when(() -> Utility.inputOutput("Password: "))
                      .thenReturn("securePass123", "securePass123"); // Both calls: valid


            List<User> mockUsers = new ArrayList<>();
            utilityMock.when(() -> Utility.getUsers()).thenReturn(mockUsers);

            ApplicantService applicantService = spy(new ApplicantService());

            doNothing().when(applicantService).viewApplicantDashboard();

            applicantService.signUp();

            verify(applicantService, times(2)).signUp();

            User addedUser = mockUsers.get(0);

            assertTrue("Password should be 'securePass123'.",addedUser.getPassword().equals("securePass123"));

            utilityMock.verify(times(1),() -> Utility.setCurrentUser(addedUser));

            verify(applicantService, times(1)).viewApplicantDashboard();

            String consoleOutput = outContent.toString();

            assertTrue("Error message for invalid input should be printed.",
                       consoleOutput.contains("All fields are required. Please try again."));
            assertTrue("Success message should be printed upon successful sign-up.",
                       consoleOutput.contains("Sign Up Successful for Applicant"));
            assertTrue("Dashboard redirection message should be printed.",
                       consoleOutput.contains("Directing to Applicant Dashboard"));
        }
    }


    @Test
    public void testViewJobPost_UnauthorizedUser() {
        // Arrange
        try (MockedStatic<Utility> utilityMock = mockStatic(Utility.class);
             MockedStatic<CommonService> commonServiceMock = mockStatic(CommonService.class)) {


            utilityMock.when(Utility::getCurrentUser).thenReturn(null);
            ApplicantService applicantService = spy(new ApplicantService());
            doNothing().when(applicantService).viewAllAvailableJobs();


            CommonService mockCommonService = mock(CommonService.class);
            commonServiceMock.when(CommonService::getInstance).thenReturn(mockCommonService);
            doNothing().when(mockCommonService).accessLandingPage();


            applicantService.viewJobPost();


            String consoleOutput = outContent.toString();
            assertTrue("Should print unauthorized access message.",consoleOutput.contains("You are not authorized to view this page")
                       );

            verify(applicantService, times(1)).viewAllAvailableJobs();


            utilityMock.verify(times(1),Utility::getCurrentUser);

            utilityMock.verify(never(),Utility::getJobs);


        }
    }

    @Test
    public void testViewJobPost_AuthorizedUser_JobExists_SelectApply() {

        try (MockedStatic<Utility> utilityMock = mockStatic(Utility.class);
             MockedStatic<CommonService> commonServiceMock = mockStatic(CommonService.class)) {

            User mockUser = mock(User.class);
            when(mockUser.getRole()).thenReturn(UserRole.APPLICANT);
            utilityMock.when(Utility::getCurrentUser).thenReturn(mockUser);


            Job mockJob = mock(Job.class);
            when(mockJob.getId()).thenReturn("job123");
            when(mockJob.getJobName()).thenReturn("Software Engineer");
            when(mockJob.getJobDescription()).thenReturn("Develop and maintain software applications.");
            when(mockJob.getJobStatus()).thenReturn(JobStatus.PUBLIC);

            List<Job> mockJobs = new ArrayList<>();
            mockJobs.add(mockJob);
            utilityMock.when(Utility::getJobs).thenReturn(new ArrayList<>(mockJobs));


            utilityMock.when(() -> Utility.inputOutput("Enter the Job ID to view the details:"))
                       .thenReturn("job123");
            utilityMock.when(() -> Utility.inputOutput("Do you want to apply for this job? (or type anything to go back) (Y/N)"))
                       .thenReturn("Y");
            ApplicantService applicantService = spy(new ApplicantService());

            doNothing().when(applicantService).viewApplicationForm(mockJob);
            doNothing().when(applicantService).viewAllAvailableJobs();

            CommonService mockCommonService = mock(CommonService.class);
            commonServiceMock.when(CommonService::getInstance).thenReturn(mockCommonService);
            doNothing().when(mockCommonService).accessLandingPage();


            applicantService.viewJobPost();


            String consoleOutput = outContent.toString();


            assertTrue("Should print welcome message.",consoleOutput.contains("Welcome to the Job Post")
                       );
            assertTrue("Should print Job ID.",consoleOutput.contains("Job ID: job123")
                       );





            verify(applicantService, times(1)).viewApplicationForm(mockJob);


            verify(applicantService, times(1)).viewAllAvailableJobs();


            utilityMock.verify(times(1),Utility::getCurrentUser);


            utilityMock.verify(times(1),Utility::getJobs );

            utilityMock.verify(times(1),() -> Utility.inputOutput("Enter the Job ID to view the details:") );



        }
    }


    @Test
    public void testViewJobPost_AuthorizedUser_JobExists_SelectNotApply() {

        try (MockedStatic<Utility> utilityMock = mockStatic(Utility.class);
             MockedStatic<CommonService> commonServiceMock = mockStatic(CommonService.class)) {


            User mockUser = mock(User.class);
            when(mockUser.getRole()).thenReturn(UserRole.APPLICANT);
            utilityMock.when(Utility::getCurrentUser).thenReturn(mockUser);

            Job mockJob = mock(Job.class);
            when(mockJob.getId()).thenReturn("job123");
            when(mockJob.getJobName()).thenReturn("Software Engineer");
            when(mockJob.getJobDescription()).thenReturn("Develop and maintain software applications.");
            when(mockJob.getJobStatus()).thenReturn(JobStatus.PUBLIC);

            List<Job> mockJobs = new ArrayList<>();
            mockJobs.add(mockJob);
            utilityMock.when(Utility::getJobs).thenReturn(new ArrayList<>(mockJobs));

            utilityMock.when(() -> Utility.inputOutput("Enter the Job ID to view the details:"))
                       .thenReturn("job123");
            utilityMock.when(() -> Utility.inputOutput("Do you want to apply for this job? (or type anything to go back) (Y/N)"))
                       .thenReturn("N");
            ApplicantService applicantService = spy(new ApplicantService());

            doNothing().when(applicantService).viewApplicationForm(any(Job.class));
            doNothing().when(applicantService).viewAllAvailableJobs();

            CommonService mockCommonService = mock(CommonService.class);
            commonServiceMock.when(CommonService::getInstance).thenReturn(mockCommonService);
            doNothing().when(mockCommonService).accessLandingPage();

            applicantService.viewJobPost();

            String consoleOutput = outContent.toString();


            assertTrue( "Should print Job ID.",consoleOutput.contains("Job ID: job123")
                      );
            assertTrue( "Should print Job Title.",consoleOutput.contains("Job Title: Software Engineer")
                      );




            verify(applicantService, never()).viewApplicationForm(any(Job.class));


            verify(applicantService, times(1)).viewAllAvailableJobs();


            utilityMock.verify( times(1),Utility::getCurrentUser);


        }
    }


    @Test
    public void testViewJobPost_AuthorizedUser_JobNotFound() {
        // Arrange
        try (MockedStatic<Utility> utilityMock = mockStatic(Utility.class);
             MockedStatic<CommonService> commonServiceMock = mockStatic(CommonService.class)) {


            User mockUser = mock(User.class);
            when(mockUser.getRole()).thenReturn(UserRole.APPLICANT);
            utilityMock.when(Utility::getCurrentUser).thenReturn(mockUser);


            ApplicantService applicantService = spy(new ApplicantService());
            Job mockJob = mock(Job.class);
            when(mockJob.getId()).thenReturn("job123");
            when(mockJob.getJobName()).thenReturn("Software Engineer");
            when(mockJob.getJobDescription()).thenReturn("Develop and maintain software applications.");
            when(mockJob.getJobStatus()).thenReturn(JobStatus.PUBLIC);

            List<Job> mockJobs = new ArrayList<>();
            mockJobs.add(mockJob);
            utilityMock.when(Utility::getJobs).thenReturn(new ArrayList<>(mockJobs));


            utilityMock.when(() -> Utility.inputOutput("Enter the Job ID to view the details:"))
                       .thenReturn("job999");
            doNothing().when(applicantService).viewAllAvailableJobs();

            CommonService mockCommonService = mock(CommonService.class);
            commonServiceMock.when(CommonService::getInstance).thenReturn(mockCommonService);
            doNothing().when(mockCommonService).accessLandingPage();

            applicantService.viewJobPost();


            utilityMock.verify( times(1),Utility::getCurrentUser);

        }
    }

 // UserStory:27; ar668

    @Test
    public void testViewApplicationForm_AllValidInputs() {

        try (MockedStatic<Utility> utilityMock = mockStatic(Utility.class)) {
            utilityMock.when(() -> Utility.inputOutput("Enter your education: "))
                       .thenReturn("Bachelor of Science");
            utilityMock.when(() -> Utility.inputOutput("Enter your number of experience experience: "))
                       .thenReturn("5");
            utilityMock.when(() -> Utility.inputOutput("Enter your skills: "))
                       .thenReturn("Java, Spring Boot");


            Job mockJob = mock(Job.class);
            when(mockJob.getId()).thenReturn("job123");
            when(mockJob.getJobName()).thenReturn("Software Engineer");
            when(mockJob.getJobDescription()).thenReturn("Develop and maintain software applications.");
            when(mockJob.getJobStatus()).thenReturn(JobStatus.PUBLIC);
            ApplicantService applicantService = spy(new ApplicantService());

            doNothing().when(applicantService).submitApplicationForm(any(Job.class), anyString(), anyInt(), anyString());

            applicantService.viewApplicationForm(mockJob);


            String consoleOutput = outContent.toString();


            assertTrue("Should print welcome message.",consoleOutput.contains("Welcome to the Job Application Form")
                       );

            verify(applicantService, times(1)).submitApplicationForm(mockJob, "Bachelor of Science", 5, "Java, Spring Boot");
        }
    }


    @Test
    public void testViewApplicationForm_InvalidExperienceThenValid() {

        try (MockedStatic<Utility> utilityMock = mockStatic(Utility.class)) {

            utilityMock.when(() -> Utility.inputOutput("Enter your education: "))
                       .thenReturn("Master of Arts");

            utilityMock.when(() -> Utility.inputOutput("Enter your number of experience experience: "))
                       .thenReturn("five", "5");
            utilityMock.when(() -> Utility.inputOutput("Enter your skills: "))
                       .thenReturn("Python, Django");


            Job mockJob = mock(Job.class);
            when(mockJob.getId()).thenReturn("job456");
            when(mockJob.getJobName()).thenReturn("Data Analyst");
            when(mockJob.getJobDescription()).thenReturn("Analyze and interpret complex data sets.");
            when(mockJob.getJobStatus()).thenReturn(JobStatus.PUBLIC);
            ApplicantService applicantService = spy(new ApplicantService());

            doNothing().when(applicantService).submitApplicationForm(any(Job.class), anyString(), anyInt(), anyString());
            applicantService.viewApplicationForm(mockJob);

            String consoleOutput = outContent.toString();

            // Verify that welcome message is printed
            assertTrue("Should print welcome message.",consoleOutput.contains("Welcome to the Job Application Form")
                    );

            verify(applicantService, times(1)).submitApplicationForm(mockJob, "Master of Arts", 5, "Python, Django");
        }
    }

    //UserStory: 25

    @Test
    public void testViewAllAvailableJobs_UnauthorizedUser_NullUser() {

        try (MockedStatic<Utility> utilityMock = mockStatic(Utility.class)) {


            utilityMock.when(Utility::getCurrentUser).thenReturn(null);
            ApplicantService applicantService = spy(new ApplicantService());

            applicantService.viewAllAvailableJobs();

            String consoleOutput = outContent.toString();
            assertTrue("Should print unauthorized access message.",consoleOutput.contains("You are not authorized to view this page")
                       );


            verify(applicantService, never()).viewJobPost();
            verify(applicantService, never()).viewApplicantDashboard();
        }
    }


    @Test
    public void testViewAllAvailableJobs_3() {

        try (MockedStatic<Utility> utilityMock = mockStatic(Utility.class)) {

            User mockUser = mock(User.class);
            when(mockUser.getRole()).thenReturn(UserRole.RECRUITER);
            utilityMock.when(Utility::getCurrentUser).thenReturn(mockUser);
            ApplicantService applicantService = spy(new ApplicantService());

            applicantService.viewAllAvailableJobs();

            String consoleOutput = outContent.toString();
            assertTrue("Should print unauthorized access message.",consoleOutput.contains("You are not authorized to view this page")
                    );

            verify(applicantService, never()).viewJobPost();
            verify(applicantService, never()).viewApplicantDashboard();
        }
    }


    @Test
    public void testViewAllAvailableJobs_1() {

        try (MockedStatic<Utility> utilityMock = mockStatic(Utility.class)) {

            User mockUser = mock(User.class);
            when(mockUser.getRole()).thenReturn(UserRole.APPLICANT);
            utilityMock.when(Utility::getCurrentUser).thenReturn(mockUser);


            Job mockJob1 = mock(Job.class);
            when(mockJob1.getJobStatus()).thenReturn(JobStatus.PRIVATE);
            Job mockJob2 = mock(Job.class);
            when(mockJob2.getJobStatus()).thenReturn(JobStatus.PRIVATE);

            List<Job> mockJobs = new ArrayList<>();
            mockJobs.add(mockJob1);
            mockJobs.add(mockJob2);
            utilityMock.when(Utility::getJobs).thenReturn(mockJobs);
            ApplicantService applicantService = spy(new ApplicantService());

            applicantService.viewAllAvailableJobs();

            String consoleOutput = outContent.toString();

            assertTrue("Should notify that no jobs are available.",consoleOutput.contains("No jobs available at the moment")
                       );


            verify(applicantService, never()).viewJobPost();
            verify(applicantService, never()).viewApplicantDashboard();
        }
    }


    @Test
    public void testViewAllAvailableJobs() {

        try (MockedStatic<Utility> utilityMock = mockStatic(Utility.class)) {

            User mockUser = mock(User.class);
            when(mockUser.getRole()).thenReturn(UserRole.APPLICANT);
            utilityMock.when(Utility::getCurrentUser).thenReturn(mockUser);

            Job mockJob1 = mock(Job.class);
            when(mockJob1.getId()).thenReturn("job001");
            when(mockJob1.getJobName()).thenReturn("Software Engineer");
            when(mockJob1.getJobStatus()).thenReturn(JobStatus.PUBLIC);

            Job mockJob2 = mock(Job.class);
            when(mockJob2.getId()).thenReturn("job002");
            when(mockJob2.getJobName()).thenReturn("Data Analyst");
            when(mockJob2.getJobStatus()).thenReturn(JobStatus.PUBLIC);

            List<Job> mockJobs = new ArrayList<>();
            mockJobs.add(mockJob1);
            mockJobs.add(mockJob2);
            utilityMock.when(Utility::getJobs).thenReturn(mockJobs);


            utilityMock.when(() -> Utility.inputOutput("Do you want to view a job post? (Y/N)"))
                       .thenReturn("Y");
            ApplicantService applicantService = spy(new ApplicantService());

            doNothing().when(applicantService).viewJobPost();

            applicantService.viewAllAvailableJobs();

            String consoleOutput = outContent.toString();

            assertTrue("Should list available jobs.",consoleOutput.contains("Available Jobs")
                       );
            assertTrue("Should display first job details.",consoleOutput.contains("Job ID: job001|Job Name: Software Engineer")
                       );

            verify(applicantService, times(1)).viewJobPost();

            verify(applicantService, never()).viewApplicantDashboard();
        }
    }


    @Test
    public void testViewAllAvailableJobs_AuthorizedUser_OpenJobs() {

        try (MockedStatic<Utility> utilityMock = mockStatic(Utility.class)) {

            User mockUser = mock(User.class);
            when(mockUser.getRole()).thenReturn(UserRole.APPLICANT);
            utilityMock.when(Utility::getCurrentUser).thenReturn(mockUser);

            Job mockJob1 = mock(Job.class);
            when(mockJob1.getId()).thenReturn("job001");
            when(mockJob1.getJobName()).thenReturn("Software Engineer");
            when(mockJob1.getJobStatus()).thenReturn(JobStatus.PUBLIC);

            Job mockJob2 = mock(Job.class);
            when(mockJob2.getId()).thenReturn("job002");
            when(mockJob2.getJobName()).thenReturn("Data Analyst");
            when(mockJob2.getJobStatus()).thenReturn(JobStatus.PUBLIC);

            List<Job> mockJobs = new ArrayList<>();
            mockJobs.add(mockJob1);
            mockJobs.add(mockJob2);
            utilityMock.when(Utility::getJobs).thenReturn(mockJobs);

            utilityMock.when(() -> Utility.inputOutput("Do you want to view a job post? (Y/N)"))
                       .thenReturn("N");

            ApplicantService applicantService = spy(new ApplicantService());

            doNothing().when(applicantService).viewApplicantDashboard();


            applicantService.viewAllAvailableJobs();

            String consoleOutput = outContent.toString();

            assertTrue("Should display first job details.",consoleOutput.contains("Job ID: job001|Job Name: Software Engineer")
                       );
            assertTrue("Should display second job details.",consoleOutput.contains("Job ID: job002|Job Name: Data Analyst")
                       );



            verify(applicantService, times(1)).viewApplicantDashboard();

            verify(applicantService, never()).viewJobPost();
        }
    }

 // UserStory:40; ar668

    @Test
    public void testViewAssessment_ApplicationExists_WithAssignments_2() {

        try (MockedStatic<Utility> utilityMock = mockStatic(Utility.class)) {

            Application mockApplication = mock(Application.class);
            when(mockApplication.getId()).thenReturn("app123");

            Assignment mockAssignment1 = mock(Assignment.class);
            when(mockAssignment1.getId()).thenReturn("assign001");
            when(mockAssignment1.getAssignmentName()).thenReturn("Coding Challenge");
            when(mockAssignment1.getStatus()).thenReturn(AssignmentStatus.PASSIVE);

            Assignment mockAssignment2 = mock(Assignment.class);
            when(mockAssignment2.getId()).thenReturn("assign002");
            when(mockAssignment2.getAssignmentName()).thenReturn("Project Proposal");
            when(mockAssignment2.getStatus()).thenReturn(AssignmentStatus.ALLOCATED);

            List<Assignment> assignments = new ArrayList<>();
            assignments.add(mockAssignment1);
            assignments.add(mockAssignment2);
            when(mockApplication.getAssignments()).thenReturn((ArrayList<Assignment>) assignments);

            List<Application> applications = new ArrayList<>();
            applications.add(mockApplication);
            utilityMock.when(Utility::getApplications).thenReturn(applications);

            utilityMock.when(() -> Utility.inputOutput("Please Select One Of The Options"))
                       .thenReturn("1");
            ApplicantService applicantService = spy(new ApplicantService());

            doNothing().when(applicantService).submitAssessmentForm("app123");

            applicantService.viewAssessment("app123");

            String consoleOutput = outContent.toString();
            assertTrue( "Should print welcome message.",consoleOutput.contains("Welcome to the Assessment Page")
                      );
            assertTrue( "Should list assignments for the application.",consoleOutput.contains("Assignments for app123")
                      );
            assertTrue("Should display first assignment details.",consoleOutput.contains("Assignment ID: assign001 | Assignment Name: Coding Challenge | Assignment Status: PASSIVE")
                       );
            assertTrue( "Should display second assignment details.",consoleOutput.contains("Assignment ID: assign002 | Assignment Name: Project Proposal | Assignment Status: ALLOCATED")
                      );
            assertTrue("Should display option to submit assignment.",consoleOutput.contains("1. Submit Assignment")
                       );

            verify(applicantService, times(1)).submitAssessmentForm("app123");

            verify(applicantService, never()).viewApplicationProcessDashboard("app123");
        }
    }


    @Test
    public void testViewAssessment_ApplicationExists_WithAssignments() {

        try (MockedStatic<Utility> utilityMock = mockStatic(Utility.class)) {

            Application mockApplication = mock(Application.class);
            when(mockApplication.getId()).thenReturn("app456");

            Assignment mockAssignment1 = mock(Assignment.class);
            when(mockAssignment1.getId()).thenReturn("assign003");
            when(mockAssignment1.getAssignmentName()).thenReturn("Design Document");
            when(mockAssignment1.getStatus()).thenReturn(AssignmentStatus.ALLOCATED);

            List<Assignment> assignments = new ArrayList<>();
            assignments.add(mockAssignment1);
            when(mockApplication.getAssignments()).thenReturn((ArrayList<Assignment>) assignments);

            List<Application> applications = new ArrayList<>();
            applications.add(mockApplication);
            utilityMock.when(Utility::getApplications).thenReturn(applications);

            utilityMock.when(() -> Utility.inputOutput("Please Select One Of The Options"))
                       .thenReturn("2");
ApplicantService applicantService = spy(new ApplicantService());

            doNothing().when(applicantService).viewApplicationProcessDashboard("app456");

            applicantService.viewAssessment("app456");


            String consoleOutput = outContent.toString();
            assertTrue( "Should print welcome message.",consoleOutput.contains("Welcome to the Assessment Page")
                    );


            assertTrue( "Should display option to submit assignment.",consoleOutput.contains("1. Submit Assignment")
                      );

            verify(applicantService, times(1)).viewApplicationProcessDashboard("app456");

            verify(applicantService, never()).submitAssessmentForm("app456");
        }
    }

    @Test
    public void testViewAssessment_ApplicationExists_NoAssignments_1() {

        try (MockedStatic<Utility> utilityMock = mockStatic(Utility.class)) {

            Application mockApplication = mock(Application.class);
            when(mockApplication.getId()).thenReturn("app321");
            when(mockApplication.getAssignments()).thenReturn(new ArrayList<>()); // No assignments

            List<Application> applications = new ArrayList<>();
            applications.add(mockApplication);
            utilityMock.when(Utility::getApplications).thenReturn(applications);

            utilityMock.when(() -> Utility.inputOutput("Please Select One Of The Options"))
                       .thenReturn("1");
ApplicantService applicantService = spy(new ApplicantService());

            doNothing().when(applicantService).submitAssessmentForm("app321");

            applicantService.viewAssessment("app321");


            String consoleOutput = outContent.toString();

            assertTrue("Should notify that no assignments are available.",consoleOutput.contains("No assignments found")
                       );

            verify(applicantService, times(1)).submitAssessmentForm("app321");

            verify(applicantService, never()).viewApplicationProcessDashboard("app321");
        }
    }


    @Test
    public void testViewAssessment_ApplicationExists_NoAssignments() {

        try (MockedStatic<Utility> utilityMock = mockStatic(Utility.class)) {

            Application mockApplication = mock(Application.class);
            when(mockApplication.getId()).thenReturn("app654");
            when(mockApplication.getAssignments()).thenReturn(new ArrayList<>()); // No assignments

            List<Application> applications = new ArrayList<>();
            applications.add(mockApplication);
            utilityMock.when(Utility::getApplications).thenReturn(applications);

            utilityMock.when(() -> Utility.inputOutput("Please Select One Of The Options"))
                       .thenReturn("2");

            ApplicantService applicantService = spy(new ApplicantService());

            doNothing().when(applicantService).viewApplicationProcessDashboard("app654");

            applicantService.viewAssessment("app654");

            String consoleOutput = outContent.toString();

            assertTrue( "Should notify that no assignments are available.",consoleOutput.contains("No assignments found")
                      );
            assertTrue( "Should display option to submit assignment.",consoleOutput.contains("1. Submit Assignment")
                      );

            verify(applicantService, times(1)).viewApplicationProcessDashboard("app654");

            verify(applicantService, never()).submitAssessmentForm("app654");
        }
    }



    @Test
    public void testViewAssessment_ApplicationDoesNotExist_1() {

        try (MockedStatic<Utility> utilityMock = mockStatic(Utility.class)) {

            Application mockApplication = mock(Application.class);
            when(mockApplication.getId()).thenReturn("app111");

            List<Application> applications = new ArrayList<>();
            applications.add(mockApplication);
            utilityMock.when(Utility::getApplications).thenReturn(applications);

            utilityMock.when(() -> Utility.inputOutput("Please Select One Of The Options"))
                       .thenReturn("1");
            ApplicantService applicantService = spy(new ApplicantService());

            doNothing().when(applicantService).submitAssessmentForm("app999"); // Non-existent applicationId

            applicantService.viewAssessment("app999");

            String consoleOutput = outContent.toString();
            assertTrue("Should print welcome message.",consoleOutput.contains("Welcome to the Assessment Page")
                       );
            assertTrue("Should not list any assignments as application does not exist.",!consoleOutput.contains("Assignments for app999"));

            verify(applicantService, times(1)).submitAssessmentForm("app999");

            verify(applicantService, never()).viewApplicationProcessDashboard("app999");
        }
    }


    @Test
    public void testViewAssessment_ApplicationDoesNotExist() {

        try (MockedStatic<Utility> utilityMock = mockStatic(Utility.class)) {

            Application mockApplication = mock(Application.class);
            when(mockApplication.getId()).thenReturn("app222");

            List<Application> applications = new ArrayList<>();
            applications.add(mockApplication);
            utilityMock.when(Utility::getApplications).thenReturn(applications);

            utilityMock.when(() -> Utility.inputOutput("Please Select One Of The Options"))
                       .thenReturn("2");
            ApplicantService applicantService = spy(new ApplicantService());

            doNothing().when(applicantService).viewApplicationProcessDashboard("app999"); // Non-existent applicationId

            applicantService.viewAssessment("app999");

            verify(applicantService, times(1)).viewApplicationProcessDashboard("app999");

            verify(applicantService, never()).submitAssessmentForm("app999");
        }
    }



    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 36
     */
    @Test
    public void testViewJobDescFromApplication_jobAndApplicationFound() {

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            Application app = new Application("app001", "jobABC", "userX", null, null, 0, "", "", "");
            ArrayList<Application> applications = new ArrayList<>();
            applications.add(app);
            mockedUtility.when(Utility::getApplications).thenReturn(applications);

            Job job = new Job("jobABC", "Software Engineer", "Develop software", null);
            ArrayList<Job> jobs = new ArrayList<>();
            jobs.add(job);
            mockedUtility.when(Utility::getJobs).thenReturn(jobs);

            applicantService.viewJobDescFromApplication("app001");

            String output = outContent.toString();
            assertTrue(
                    "Should print the job ID", output.contains("Job ID: jobABC"));
            assertTrue(
                    "Should print the job title", output.contains("Job Title: Software Engineer"));
            assertTrue(
                    "Should print the job description", output.contains("Job Description: Develop software"));
        } finally {
            System.setOut(originalOut);
        }
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 36
     */
    @Test
    public void testViewJobDescFromApplication_applicationIdMismatch() {

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            Application existingApp = new Application("existingApp", "someJob", "userZ", null, null, 0, "", "", "");
            ArrayList<Application> apps = new ArrayList<>();
            apps.add(existingApp);

            mockedUtility.when(Utility::getApplications).thenReturn(apps);
            mockedUtility.when(Utility::getJobs).thenReturn(new ArrayList<Job>());

            applicantService.viewJobDescFromApplication("notMatching");

            String consoleOutput = outContent.toString();
            assertTrue(
                    "Should indicate the application is not found.",
                    consoleOutput.contains("Application with ID notMatching not found."));
        } finally {
            System.setOut(originalOut);
        }
    }

}

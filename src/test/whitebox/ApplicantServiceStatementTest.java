package test.whitebox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

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
import model.User;
import model.UserRole;
import service.ApplicantService;
import service.CommonService;
import service.RecruiterService;
import utility.Utility;

public class ApplicantServiceStatementTest {

    private ApplicantService applicantService;

    private ArrayList<Application> mockApplications;
    private ArrayList<User> mockUsers;
    private ArrayList<Job> mockJobs;
    private Application mockApplication;
    private ArrayList<Assignment> mockInterviews;
    private Assignment mockAssignment;
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
        User user1 = new User("1", "John", "Doe", "johnDoe", "bestpassword", UserRole.APPLICANT);
        mockUsers.add(user1);
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
    public void testSignIn_Valid() {
        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            // Arrange
            mockedUtility.when(Utility::getUsers).thenReturn(mockUsers);
            // First invalid password, then correct password
            mockedUtility.when(() -> Utility.inputOutput(anyString()))
                .thenReturn("johnDoe").thenReturn("wrongPassword").thenReturn("y").thenReturn("johnDoe").thenReturn("bestpassword");;
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

    // ETY1 - STORY 28
    @Test
    public void submitApplicationFormTest_submitApplication() {
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
    public void applicantLogOutTest_logOut() throws IOException {
        try(MockedStatic<CommonService> spyCommonService = Mockito.mockStatic(CommonService.class);
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
    public void viewApplicantDashboardTest_input1() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        ApplicantService service = ApplicantService.getInstance();

        ApplicantService spyObject = Mockito.spy(service);

        try(MockedStatic<Utility> mockedUtility=Mockito.mockStatic(Utility.class)) {
            Mockito.doNothing().when(spyObject).viewApplicantProfilePage();

            mockedUtility.when(() -> Utility.inputOutput(anyString())).thenReturn("1");

            spyObject.viewApplicantDashboard();

            String consoleOutput = outputStream.toString();

            Assert.assertTrue(consoleOutput.contains("Directing to Profile Page..."));


            Mockito.verify(spyObject,Mockito.times(1)).viewApplicantProfilePage();
            mockedUtility.verify(Mockito.times(1),()->Utility.inputOutput(Mockito.anyString()));

        }
    }

    // ETY1 - STORY 14
    @Test
    public void viewApplicantDashboardTest_input2() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        ApplicantService service = ApplicantService.getInstance();

        ApplicantService spyObject = Mockito.spy(service);

        try(MockedStatic<Utility> mockedUtility=Mockito.mockStatic(Utility.class)) {
            Mockito.doNothing().when(spyObject).viewApplicantApplications();

            mockedUtility.when(() -> Utility.inputOutput(anyString())).thenReturn("2");

            spyObject.viewApplicantDashboard();
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Directing to Applications Page..."));

            outputStream.reset();

            mockedUtility.verify(Mockito.times(1),()->Utility.inputOutput(Mockito.anyString()));
            Mockito.verify(spyObject,Mockito.times(1)).viewApplicantDashboard();

            Mockito.verify(spyObject,Mockito.times(1)).viewApplicantApplications();
        }
    }
    // ETY1 - STORY 14
    @Test
    public void viewApplicantDashboardTest_input3() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        ApplicantService service = ApplicantService.getInstance();

        ApplicantService spyObject = Mockito.spy(service);

        try(MockedStatic<Utility> mockedUtility=Mockito.mockStatic(Utility.class)) {

            Mockito.doNothing().when(spyObject).viewAllAvailableJobs();

            mockedUtility.when(() -> Utility.inputOutput(anyString())).thenReturn("3");

            spyObject.viewApplicantDashboard();
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Directing to Available Jobs Page..."));

            Mockito.verify(spyObject,Mockito.times(1)).viewAllAvailableJobs();

            mockedUtility.verify(Mockito.times(1),()->Utility.inputOutput(Mockito.anyString()));
            Mockito.verify(spyObject,Mockito.times(1)).viewApplicantDashboard();

        }
    }

    // ETY1 - STORY 14
    @Test
    public void viewApplicantDashboardTest_logOut() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        ApplicantService service = ApplicantService.getInstance();

        ApplicantService spyObject = Mockito.spy(service);

        try(MockedStatic<Utility> mockedUtility=Mockito.mockStatic(Utility.class)) {

            Mockito.doNothing().when(spyObject).applicantLogOut();


            mockedUtility.when(() -> Utility.inputOutput(anyString())).thenReturn("4");

            spyObject.viewApplicantDashboard();
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Logging Out..."));


            mockedUtility.verify(Mockito.times(1),()->Utility.inputOutput(Mockito.anyString()));
            Mockito.verify(spyObject,Mockito.times(1)).viewApplicantDashboard();

            Mockito.verify(spyObject,Mockito.times(1)).applicantLogOut();
        }
    }

    // ETY1 - STORY 14
    @Test
    public void viewApplicantDashboardTest_invalidInput() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        ApplicantService service = ApplicantService.getInstance();

        ApplicantService spyObject = Mockito.spy(service);

        try(MockedStatic<Utility> mockedUtility=Mockito.mockStatic(Utility.class)) {

            mockedUtility.when(() -> Utility.inputOutput(anyString())).thenReturn("invalidInput");

            Mockito.doCallRealMethod().doNothing().when(spyObject).viewApplicantDashboard();

            spyObject.viewApplicantDashboard();
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("You entered invalid option"));


            mockedUtility.verify(Mockito.times(1),()->Utility.inputOutput(Mockito.anyString()));
            Mockito.verify(spyObject,Mockito.times(2)).viewApplicantDashboard();

        }
    }
    // ETY1 - STORY 45
    @Test
    public void submitInterviewFormTest_noApplicationId() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        ApplicantService spyObject = Mockito.spy(new ApplicantService());


        try(MockedStatic<Utility> mockedUtility=Mockito.mockStatic(Utility.class)){

            Mockito.doNothing().when(spyObject).viewApplicantApplications();
            mockedUtility.when(Utility::getApplications).thenReturn(mockApplications);

            spyObject.submitInterviewForm("nonExistentId");

            String consoleOutput = outputStream.toString();

            Assert.assertTrue(consoleOutput.contains("Application with given ID not found"));

            mockedUtility.verify(Mockito.times(1),()->Utility.getApplications());
            Mockito.verify(spyObject,Mockito.times(1)).viewApplicantApplications();


        }
    }

    // ETY1 - STORY 45
    @Test
    public void submitInterviewFormTest_noInterview() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        ApplicantService spyObject = Mockito.spy(new ApplicantService());


        try(MockedStatic<Utility> mockedUtility=Mockito.mockStatic(Utility.class)){


            Mockito.doNothing().when(spyObject).viewApplicantApplications();

            mockApplications.get(0).setInterviewAssignments(new ArrayList<>());

            mockedUtility.when(Utility::getApplications).thenReturn(mockApplications);

            spyObject.submitInterviewForm(mockApplications.get(0).getId());

            String consoleOutput = outputStream.toString();

            Assert.assertTrue(consoleOutput.contains("No interview questions found"));



            mockedUtility.verify(Mockito.times(1),()->Utility.getApplications());
            Mockito.verify(spyObject,Mockito.times(1)).viewApplicantApplications();


        }
    }

    // ETY1 - STORY 45
    @Test
    public void submitInterviewFormTest_alreadySubmitted() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        ApplicantService spyObject = Mockito.spy(new ApplicantService());


        try(MockedStatic<Utility> mockedUtility=Mockito.mockStatic(Utility.class)){

            Mockito.doNothing().when(spyObject).viewApplicationProcessDashboard(any());

            mockedUtility.when(Utility::getApplications).thenReturn(mockApplications);

            mockInterviews.get(0).setStatus(AssignmentStatus.SUBMITTED);

            mockApplications.get(0).setInterviewAssignments(mockInterviews);

            spyObject.submitInterviewForm(mockApplications.get(0).getId());

            String consoleOutput = outputStream.toString();

            Assert.assertTrue(consoleOutput.contains("Interview already submitted"));


            Mockito.verify(spyObject,Mockito.times(1)).viewApplicationProcessDashboard(any());
            mockedUtility.verify(Mockito.times(1),()->Utility.getApplications());


        }
    }



    @Test
    public void testViewApplicantProfilePage_Valid() {
        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            // Arrange
            User mockUser = new User("51", "John", "Doe", "john", "GyyGGyibkl", UserRole.APPLICANT);
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
    public void testViewApplicantProfilePage_MissingData() {
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
    public void testSubmitAssessmentForm_Valid() {
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
    public void testViewInterview_Valid() {
        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            // Arrange
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

            assertTrue(filteredLines.contains("Redirecting back to Application Dashboard.."));
        }
    }

    /*
     * Author: Mohammad Ansar Patil (map66)
     * User Story: 10
     */ 
    @Test
    public void viewResetPasswordPageTest() {
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

    
            Mockito.doNothing().when(service).resetPassword(Mockito.anyString());
    
            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString()))
                         .thenReturn("1", "JohnDoe");
            service.viewResetPasswordPage();
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Welcome to reset password page"));
            Mockito.verify(service, Mockito.times(1)).resetPassword("JohnDoe");
            outputStream.reset();
    
            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("2");
            mockApplicantService.when(ApplicantService::getInstance).thenReturn(mockApp);
            Mockito.doNothing().when(mockApp).viewApplicantDashboard();
    
            service.viewResetPasswordPage();
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Redirecting to Applicant dashboard"));
            Mockito.verify(mockApp, Mockito.times(1)).viewApplicantDashboard();
            outputStream.reset();
    
            mockUser.setRole(UserRole.RECRUITER);
            mockedUtility.when(Utility::getCurrentUser).thenReturn(mockUser);
            mockRecruiterService.when(RecruiterService::getInstance).thenReturn(mockRec);
            Mockito.doNothing().when(mockRec).viewRecruiterDashboard();
    
            service.viewResetPasswordPage();
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Redirecting to Recruiter dashboard"));
            Mockito.verify(mockRec, Mockito.times(1)).viewRecruiterDashboard();
            outputStream.reset();
    
            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("invalid", "2");
            service.viewResetPasswordPage();
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("You entered invalid option"));
            Assert.assertTrue(consoleOutput.contains("Redirecting to Recruiter dashboard"));
            outputStream.reset();
    
            Mockito.verify(service, Mockito.times(1)).resetPassword("JohnDoe");
            mockedUtility.verify(Mockito.times(6), () -> Utility.inputOutput(Mockito.anyString()));
        }
    }
    

     /*
     * Author: Mohammad Ansar Patil (map66)
     * User Story: 12
     */ 
    @Test
    public void resetPasswordTest() {
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

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString()))
                    .thenReturn("WRONGCODE", "newPassword123");
            service.resetPassword("JohnDoe");
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("You have entered wrong Reset Code"));
            Assert.assertTrue(consoleOutput.contains("Redirecting to Applicant dashboard"));
            outputStream.reset();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString()))
                    .thenReturn("XVQTY", "newPassword123");
            service.resetPassword("WrongUser");
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("You have entered wrong Crediantials"));
            outputStream.reset();

            mockedUtility.verify(Mockito.times(12), Utility::getCurrentUser);
            mockedUtility.verify(Mockito.times(2), Utility::getUsers);
            mockedUtility.verify(Mockito.times(5), () -> Utility.inputOutput(Mockito.anyString()));
        }
    }

     /*
     * Author: Mohammad Ansar Patil (map66)
     * User Story: 02
     */ 
    @Test
    public void applicantViewSignInSignUpPageTest() throws IOException {
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
    
            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("1");
            spyObject.applicantViewSignInSignUpPage();
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Redirecting to Applicant Sign In Page"));
            Mockito.verify(spyObject, Mockito.times(1)).signIn();
            outputStream.reset();
    
            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("2");
            spyObject.applicantViewSignInSignUpPage();
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Redirecting to Applicant Sign Up Page"));
            Mockito.verify(spyObject, Mockito.times(1)).signUp();
            outputStream.reset();
    
            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("3");
            spyObject.applicantViewSignInSignUpPage();
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Redirecting to previous menu"));
            outputStream.reset();
    
            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("invalid", "3");
            spyObject.applicantViewSignInSignUpPage();
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("You entered invalid option"));
            Assert.assertTrue(consoleOutput.contains("Redirecting to previous menu"));
            outputStream.reset();
    
            Mockito.verify(spyObject, Mockito.times(1)).signIn();
            Mockito.verify(spyObject, Mockito.times(1)).signUp();
            mockedUtility.verify(Mockito.times(5), () -> Utility.inputOutput(Mockito.anyString()));
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

            Mockito.verify(spyObject, Mockito.times(3)).viewFeedback(Mockito.anyString());
            mockedUtility.verify(Mockito.times(3), Utility::getApplications);
        }
    }

    /*
     * Author: Mohammad Ansar Patil (map66)
     * User Story: 39
     */ 
    @Test
    public void viewApplicationProcessDashboardTest() throws IOException {
        ApplicantService service = ApplicantService.getInstance();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        ApplicantService spyObject = Mockito.spy(service);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString()))
                .thenReturn("1", "2", "3", "4", "5", "6", "invalid", "6", "2");

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

            spyObject.viewApplicationProcessDashboard(Mockito.anyString());
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Redirecting to submit assignments page"));
            Mockito.verify(spyObject, Mockito.times(1)).submitAssessmentForm(Mockito.anyString());
            outputStream.reset();

            spyObject.viewApplicationProcessDashboard(Mockito.anyString());
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Redirecting to view interview questions page"));
            Mockito.verify(spyObject, Mockito.times(1)).viewInterview(Mockito.anyString());
            outputStream.reset();

            spyObject.viewApplicationProcessDashboard(Mockito.anyString());
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Redirecting to submit interview answers page"));
            Mockito.verify(spyObject, Mockito.times(1)).submitInterviewForm(Mockito.anyString());
            outputStream.reset();

            spyObject.viewApplicationProcessDashboard(Mockito.anyString());
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Redirecting to view feedback page"));
            Mockito.verify(spyObject, Mockito.times(1)).viewFeedback(Mockito.anyString());
            outputStream.reset();

            spyObject.viewApplicationProcessDashboard(Mockito.anyString());
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Go back to Applications page"));
            Mockito.verify(spyObject, Mockito.times(1)).viewApplicantApplications();
            outputStream.reset();

            spyObject.viewApplicationProcessDashboard(Mockito.anyString());
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("You entered invalid option"));
            Mockito.verify(spyObject, Mockito.times(8)).viewApplicationProcessDashboard(Mockito.anyString());
            outputStream.reset();

            Mockito.verify(spyObject, Mockito.times(8)).viewApplicationProcessDashboard(Mockito.anyString());
            mockedUtility.verify(Mockito.times(8), () -> Utility.inputOutput(Mockito.anyString()));
        }
    }

    /*
     * Author: Mohammad Ansar Patil (map66)
     * User Story: 35
     */
    @Test
    public void withdrawApplicationTest() throws IOException {
        ApplicantService service = new ApplicantService();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        ApplicantService spyObject = Mockito.spy(service);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {

            ArrayList<Application> mockApplications = new ArrayList<>();
            mockApplications.add(new Application("A101", "J101", "U101", ApplicationStatus.INPROGRESS, new ArrayList<>(), null, null, null, null));
            mockApplications.add(new Application("A102", "J102", "U101", ApplicationStatus.SUCCESSFUL, new ArrayList<>(), null, null, null, null));
            mockApplications.add(new Application("A103", "J103", "U102", ApplicationStatus.UNSUCCESSFUL, new ArrayList<>(), null, null, null, null));

            mockedUtility.when(Utility::getApplications).thenReturn(mockApplications);

            Mockito.doNothing().when(spyObject).viewApplicantApplications();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("1");
            spyObject.withdrawApplication("A101");
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Welcome to withdraw application page"));
            Assert.assertTrue(consoleOutput.contains("Withdrawing application"));
            Assert.assertFalse(mockApplications.stream().anyMatch(app -> "A101".equals(app.getId())));
            outputStream.reset();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("2");
            spyObject.withdrawApplication("A102");
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Welcome to withdraw application page"));
            Assert.assertTrue(consoleOutput.contains("Redirecting to applications page"));
            Assert.assertTrue(mockApplications.stream().anyMatch(app -> "A102".equals(app.getId())));
            outputStream.reset();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("invalid");
            spyObject.withdrawApplication("A103");
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Welcome to withdraw application page"));
            Assert.assertTrue(consoleOutput.contains("You entered invalid option"));
            Assert.assertTrue(mockApplications.stream().anyMatch(app -> "A103".equals(app.getId())));
            outputStream.reset();

            Mockito.verify(spyObject, Mockito.times(3)).withdrawApplication(Mockito.anyString());
            Mockito.verify(spyObject, Mockito.times(2)).viewApplicantApplications();
            mockedUtility.verify(Mockito.times(3), () -> Utility.inputOutput(Mockito.anyString()));
            mockedUtility.verify(Mockito.times(1), Utility::getApplications);
        }
    }

    /*
     * Author: Mohammad Ansar Patil (map66)
     * User Story: 34
     */ 
    @Test
    public void viewSpecificApplicationTest() throws IOException {
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
            mockApplications.add(new Application("A102", "J102", "U101", ApplicationStatus.SUCCESSFUL, new ArrayList<>(), null, null, null, null));
            mockApplications.add(new Application("A103", "J103", "U102", ApplicationStatus.SUCCESSFUL, new ArrayList<>(), null, null, null, null));
            mockApplications.add(new Application("A104", "J104", "U101", ApplicationStatus.INPROGRESS,
                new ArrayList<>(List.of(new Assignment("Assign2", "U101", "Assignment 1", new ArrayList<>(), new ArrayList<>()))), null, null, null, null));

            mockedUtility.when(Utility::getApplications).thenReturn(mockApplications);

            spyObject.viewSpecificApplication(null);
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Application ID is required."));
            outputStream.reset();

            spyObject.viewSpecificApplication("");
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Application ID is required."));
            outputStream.reset();

            spyObject.viewSpecificApplication("A999");
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Application with ID A999 not found."));
            outputStream.reset();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("1");
            spyObject.viewSpecificApplication("A101");
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Application ID: A101"));
            Assert.assertTrue(consoleOutput.contains("Job Id: J101"));
            Assert.assertTrue(consoleOutput.contains("Application Status: INPROGRESS"));
            Assert.assertTrue(consoleOutput.contains("Redirecting to application process dashboard"));
            outputStream.reset();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("2");
            spyObject.viewSpecificApplication("A102");
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Application ID: A102"));
            Assert.assertTrue(consoleOutput.contains("Job Id: J102"));
            Assert.assertTrue(consoleOutput.contains("Application Status: SUCCESSFUL"));
            Assert.assertTrue(consoleOutput.contains("Redirecting to view job description page"));
            outputStream.reset();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("3");
            spyObject.viewSpecificApplication("A104");
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Application ID: A104"));
            Assert.assertTrue(consoleOutput.contains("Job Id: J104"));
            Assert.assertTrue(consoleOutput.contains("Application Status: INPROGRESS"));
            Assert.assertTrue(consoleOutput.contains("Redirecting to withdraw application page"));
            outputStream.reset();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("invalid");
            spyObject.viewSpecificApplication("A101");
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("You entered invalid option, redirecting to applications page"));
            outputStream.reset();

            Mockito.verify(spyObject, Mockito.times(1)).viewApplicationProcessDashboard(Mockito.anyString());
            Mockito.verify(spyObject, Mockito.times(1)).viewJobDescFromApplication(Mockito.anyString());
            Mockito.verify(spyObject, Mockito.times(1)).withdrawApplication(Mockito.anyString());
            Mockito.verify(spyObject, Mockito.times(4)).viewApplicantApplications(); // Invalid cases
            mockedUtility.verify(Mockito.times(4), () -> Utility.inputOutput(Mockito.anyString()));
        }
    }


    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 18
     *
     * Covers deleteProfileHelper() in ApplicantService:
     * Ensures the current user is removed from the global user list,
     * and current user is set to null.
     */
    @Test
    public void testDeleteProfileHelper() {
        User mockApplicant = new Applicant("A1", "John", "Doe", "johnUser", "pass");
        ArrayList<User> users = new ArrayList<>();
        users.add(mockApplicant);

        try (MockedStatic<Utility> utilMock = Mockito.mockStatic(Utility.class)) {
            utilMock.when(Utility::getCurrentUser).thenReturn(mockApplicant);
            utilMock.when(Utility::getUsers).thenReturn(users);
            applicantService.deleteProfileHelper();

            assertTrue("User list should be empty after deletion", users.isEmpty());
            utilMock.verify(times(1), () -> Utility.setCurrentUser(null));
        }
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 18
     *
     * Covers deleteApplicantProfile() in ApplicantService:
     * - Scenario where user types "Y" => actually deletes profile
     */
    @Test
    public void testDeleteApplicantProfile_yes() {
        ApplicantService spyService = Mockito.spy(applicantService);

        User mockApplicant = new Applicant("A2", "Jane", "Doe", "janeUser", "123");
        ArrayList<User> users = new ArrayList<>();
        users.add(mockApplicant);

        try (MockedStatic<Utility> utilMock = Mockito.mockStatic(Utility.class)) {
            utilMock.when(Utility::getCurrentUser).thenReturn(mockApplicant);
            utilMock.when(Utility::getUsers).thenReturn(users);

            utilMock.when(() -> Utility.inputOutput("Are you sure you want to delete your profile? (Y/N)"))
                    .thenReturn("Y");

            doNothing().when(spyService).applicantViewSignInSignUpPage();

            spyService.deleteApplicantProfile();
            assertTrue("User list should be empty after deletion", users.isEmpty());
            utilMock.verify(times(1), () -> Utility.setCurrentUser(null));
            verify(spyService, times(1)).applicantViewSignInSignUpPage();
        }
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 17
     *
     * Covers updateProfile(...) in ApplicantService:
     * - Ensures the user in memory is updated in Utility’s user list.
     */
    @Test
    public void testUpdateProfile() {
        User current = new Applicant("A3", "Jack", "Smith", "jackUser", "pw");
        // userList has that user
        ArrayList<User> userList = new ArrayList<>();
        userList.add(current);

        try (MockedStatic<Utility> utilMock = Mockito.mockStatic(Utility.class)) {
            utilMock.when(Utility::getUsers).thenReturn(userList);
            utilMock.when(Utility::getCurrentUser).thenReturn(current);

            // Simulate new user changes
            current.setName("Jacky");
            current.setLastName("Smyth");

            applicantService.updateProfile(current);

            assertEquals(1, userList.size());
            User updated = userList.get(0);
            assertEquals("Jacky", updated.getName());
            assertEquals("Smyth", updated.getLastName());
        }
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 17
     *
     * Covers showUpdateProfilePage() in ApplicantService:
     * - user enters new name, new last name, checks console output
     */
    @Test
    public void testShowUpdateProfilePage() {
        ApplicantService spyService = Mockito.spy(applicantService);

        // Current user
        User current = new Applicant("A4", "OldName", "OldLast", "oldUser", "pw");
        try (MockedStatic<Utility> utilMock = Mockito.mockStatic(Utility.class)) {
            utilMock.when(Utility::getCurrentUser).thenReturn(current);

            utilMock.when(() -> Utility.inputOutput("Enter your name: "))
                    .thenReturn("NewName");
            utilMock.when(() -> Utility.inputOutput("Enter your last name: "))
                    .thenReturn("NewLast");

            doNothing().when(spyService).updateProfile(any(User.class));
            doNothing().when(spyService).viewApplicantProfilePage();

            spyService.showUpdateProfilePage();

            // Check that the user object is updated
            assertEquals("NewName", current.getName());
            assertEquals("NewLast", current.getLastName());

            // Verify it calls updateProfile(currentUser) and returns
            verify(spyService, times(1)).updateProfile(current);
            verify(spyService, times(1)).viewApplicantProfilePage();
        }
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 29
     *
     * Covers viewApplicantApplications() in ApplicantService:
     * - If no applications, prints "No applications found." => returns
     */
    @Test
    public void testViewApplicantApplications_noApps() {
        ApplicantService spyService = Mockito.spy(applicantService);
        Applicant mockApplicant = new Applicant("A5", "App", "User", "appUser", "pw");

        try (MockedStatic<Utility> utilMock = Mockito.mockStatic(Utility.class)) {
            utilMock.when(Utility::getCurrentUser).thenReturn(mockApplicant);
            doNothing().when(spyService).viewApplicantDashboard();

            spyService.viewApplicantApplications();

            String output = outContent.toString();
            assertTrue(output.contains("No applications found."));
            verify(spyService, times(1)).viewApplicantDashboard();
        }
    }

    @Test
    public void testSignUp_InvalidInputs_thenValidInputs() {

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
            ApplicantService applicantService = Mockito.spy(new ApplicantService());

            doNothing().when(applicantService).viewApplicantDashboard();


            applicantService.signUp();


            verify(applicantService, times(2)).signUp();


            User addedUser = mockUsers.get(0);

            assertTrue("First name should be 'John'.",addedUser.getName().equals("John"));


            utilityMock.verify(times(1),() -> Utility.setCurrentUser(addedUser));


            verify(applicantService, times(1)).viewApplicantDashboard();

        }
    }

    //ar668: UserStory: 26

    @Test
    public void testViewJobPost_AuthorizedUser() {

        try (MockedStatic<Utility> utilityMock = mockStatic(Utility.class)) {
        	ApplicantService applicantService = Mockito.spy(new ApplicantService());

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


            doNothing().when(applicantService).viewApplicationForm(mockJob);
            doNothing().when(applicantService).viewAllAvailableJobs();


            applicantService.viewJobPost();

            String consoleOutput = outContent.toString();


            assertTrue("Should print Job ID.",consoleOutput.contains("Job ID: job123")
                       );
            assertTrue("Should print Job Title.",consoleOutput.contains("Job Title: Software Engineer")
                       );
            assertTrue("Should print Job Description.",consoleOutput.contains("Job Description: Develop and maintain software applications.")
                       );

            verify(applicantService, times(1)).viewApplicationForm(mockJob);

            verify(applicantService, times(1)).viewAllAvailableJobs();

            utilityMock.verify( times(1),Utility::getJobs);

            utilityMock.verify(times(1),() -> Utility.inputOutput("Enter the Job ID to view the details:"));

        }
    }

 // UserStory:27; ar668

    @Test
    public void testViewApplicationForm() {

        try (MockedStatic<Utility> utilityMock = mockStatic(Utility.class)) {

            utilityMock.when(() -> Utility.inputOutput("Enter your education: "))
                       .thenReturn("Bachelor of Science");
            utilityMock.when(() -> Utility.inputOutput("Enter your number of experience experience: "))
                       .thenReturn("five", "5");
            utilityMock.when(() -> Utility.inputOutput("Enter your skills: "))
                       .thenReturn("Java, Spring Boot");


            Job mockJob = mock(Job.class);
            when(mockJob.getId()).thenReturn("job123");
            when(mockJob.getJobName()).thenReturn("Software Engineer");
            when(mockJob.getJobDescription()).thenReturn("Develop and maintain software applications.");
            when(mockJob.getJobStatus()).thenReturn(JobStatus.PUBLIC);
            ApplicantService applicantService = Mockito.spy(new ApplicantService());

            doNothing().when(applicantService).submitApplicationForm(any(Job.class), anyString(), anyInt(), anyString());


            applicantService.viewApplicationForm(mockJob);


            String consoleOutput = outContent.toString();


            assertTrue("Should print welcome message.",consoleOutput.contains("Welcome to the Job Application Form")
                       );





            verify(applicantService, times(1)).submitApplicationForm(mockJob, "Bachelor of Science", 5, "Java, Spring Boot");

        }
    }

 // UserStory:25; ar668
    @Test
    public void testViewAllAvailableJobs_AuthorizedUser_OpenJobs_ViewJobPost() {

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
            ApplicantService applicantService = Mockito.spy(new ApplicantService());

            doNothing().when(applicantService).viewJobPost();


            applicantService.viewAllAvailableJobs();


            String consoleOutput = outContent.toString();
            assertTrue("Should print welcome message.",consoleOutput.contains("Welcome to the Available Jobs Page")
                       );
            assertTrue("Should list available jobs.",consoleOutput.contains("Available Jobs")
                       );
            assertTrue("Should display first job details.",consoleOutput.contains("Job ID: job001|Job Name: Software Engineer")
                       );


            verify(applicantService, times(1)).viewJobPost();

        }
    }

 // UserStory:40; ar668
    @Test
    public void testViewAssessment_ApplicationExists_WithAssignments_UserChoosesSubmit() {

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
            assertTrue("Should print welcome message.",consoleOutput.contains("Welcome to the Assessment Page")
                       );
            assertTrue("Should list assignments for the application.",consoleOutput.contains("Assignments for app123")
                       );
            assertTrue("Should display first assignment details.",consoleOutput.contains("Assignment ID: assign001 | Assignment Name: Coding Challenge | Assignment Status: PASSIVE")
                       );
            assertTrue( "Should display second assignment details.",consoleOutput.contains("Assignment ID: assign002 | Assignment Name: Project Proposal | Assignment Status: ALLOCATED")
                      );
            assertTrue("Should display option to submit assignment.",consoleOutput.contains("1. Submit Assignment")
                       );

            verify(applicantService, times(1)).submitAssessmentForm("app123");


        }
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 36
     *
     * Covers viewJobDescFromApplication() in ApplicantService:
     * - The job is not found => prints "Job with ID XXX not found."
     */
    @Test
    public void testViewJobDescFromApplication_noJob() {
        ArrayList<Application> mockApps = new ArrayList<>();
        // single application with jobId="unknownJob"
        Application app = new Application("APP99", "unknownJob", "A6",
                null, new ArrayList<>(), 0, "", "", "");
        mockApps.add(app);

        ArrayList<Job> mockJobs = new ArrayList<>(); // no matching job

        try (MockedStatic<Utility> utilMock = Mockito.mockStatic(Utility.class)) {
            utilMock.when(Utility::getApplications).thenReturn(mockApps);
            utilMock.when(Utility::getJobs).thenReturn(mockJobs);

            applicantService.viewJobDescFromApplication("APP99");
            String output = outContent.toString();
            assertTrue(output.contains("Job with ID unknownJob not found."));
        }
    }

}

package test.whitebox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;

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

import model.Application;
import model.ApplicationStatus;
import model.Assignment;
import model.Job;
import model.JobStatus;
import model.User;
import model.UserRole;
import service.ApplicantService;
import service.CommonService;
import service.RecruiterService;
import utility.Utility;

public class ApplicantServiceStatementTest {
    private ArrayList<Application> mockApplications;
    private ArrayList<User> mockUsers;
    private ArrayList<Job> mockJobs;
    private Application mockApplication;
    private Assignment mockAssignment;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Before
    public void setUp() {
        // Mock Jobs
        Job job1 = new Job( "1", "Data Analyst", "A data analyst's job is to collect, organize, and analyze data to help businesses solve problems and gain insights. ", JobStatus.PRIVATE );
        Job job2 = new Job( "2", "Frontend Developer", "As a Front End Developer you'll take ownership of technical projects, designing and developing user interfaces and client dashboards for cutting edge trading systems technology. ", JobStatus.PUBLIC );
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
         
        // Mock applications
        mockApplications = new ArrayList<>();
        Application app1 = new Application("1", "1", "1", ApplicationStatus.INPROGRESS, assignments, 2, "BSc", "JS, CSS", "");
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

            assertTrue(filteredLines.contains("Questions are:"));
            assertTrue(filteredLines.contains("Redirecting back to Application Dashboard.."));
        }
    }

    @Test
    public void viewResetPasswordPageTest() {
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
    
            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("2");
            ApplicantService mockApplicantService = Mockito.mock(ApplicantService.class);
            Mockito.mockStatic(ApplicantService.class).when(ApplicantService::getInstance).thenReturn(mockApplicantService);
            Mockito.doNothing().when(mockApplicantService).viewApplicantDashboard();
    
            service.viewResetPasswordPage();
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Redirecting to Applicant dashboard"));
            Mockito.verify(mockApplicantService, Mockito.times(1)).viewApplicantDashboard();
            outputStream.reset();
    
            mockUser.setRole(UserRole.RECRUITER);
            mockedUtility.when(Utility::getCurrentUser).thenReturn(mockUser);
            RecruiterService mockRecruiterService = Mockito.mock(RecruiterService.class);
            Mockito.mockStatic(RecruiterService.class).when(RecruiterService::getInstance).thenReturn(mockRecruiterService);
            Mockito.doNothing().when(mockRecruiterService).viewRecruiterDashboard();
    
            service.viewResetPasswordPage();
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Redirecting to Recruiter dashboard"));
            Mockito.verify(mockRecruiterService, Mockito.times(1)).viewRecruiterDashboard();
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


    @Test
    public void applicantViewSignInSignUpPageTest() throws IOException {
        ApplicantService service = ApplicantService.getInstance();
        CommonService commonServiceService = CommonService.getInstance();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        ApplicantService spyObject = Mockito.spy(service);
        CommonService commonSpyObject = Mockito.spy(commonServiceService);
        CommonService mockACommonService = Mockito.mock(CommonService.class);
        Mockito.mockStatic(CommonService.class).when(CommonService::getInstance).thenReturn(mockACommonService);
        

    
        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            Mockito.doNothing().when(spyObject).signIn();
            Mockito.doNothing().when(spyObject).signUp();
            Mockito.doNothing().when(commonSpyObject).accessLandingPage();
    
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


}

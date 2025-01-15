package test.whitebox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import model.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import service.ApplicantService;
import service.CommonService;
import service.RecruiterService;
import utility.Utility;

public class ApplicantServiceBranchTest {
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
    public void testSubmitAssessmentForm_ApplicationNotFound() {
        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            mockedUtility.when(Utility::getApplications).thenReturn(mockApplications);
            ApplicantService applicantService = Mockito.spy(new ApplicantService());

            // Invalid application ID given
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
    public void testSubmitAssessmentForm_NoAssignmentsForApplication() {
        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            ArrayList<Assignment> assignmentsEmpty = new ArrayList<>();
            ArrayList<Application> mockedApplications = new ArrayList<>();

            Application app1 = new Application("1", "1", "1", ApplicationStatus.INPROGRESS, assignmentsEmpty, 2, "BSc", "JS, CSS", "");
            mockedApplications.add(app1);
            mockedUtility.when(Utility::getApplications).thenReturn(mockedApplications);

            ApplicantService applicantService = Mockito.spy(new ApplicantService());
            doNothing().when(applicantService).viewApplicationProcessDashboard("1");

            applicantService.submitAssessmentForm("1");

            // Assert
            // Get the complete output
            String output = outContent.toString().trim();

            // Extract the last println message (messages are separated by newlines)
            String[] lines = output.split("\n");
            String lastMessage = lines[lines.length - 1];

            assertEquals("No assignments found for this application", lastMessage);
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

            Application app1 = new Application("1", "1", "1", ApplicationStatus.INPROGRESS, mockedAssignments, 2, "BSc", "JS, CSS", "");
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
        }
    }
}

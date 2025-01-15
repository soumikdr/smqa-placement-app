package test.whitebox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.*;
import org.junit.Assert;
import org.junit.Before;
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
import model.Job;
import model.JobStatus;
import model.Recruiter;
import model.User;
import model.UserRole;
import service.CommonService;
import service.RecruiterService;
import utility.Utility;

public class RecruiterServiceBranchTest {

    private RecruiterService recruiterService;
    private ArrayList<Application> mockApplications;
    private ArrayList<User> mockUsers;
    private ArrayList<Job> mockJobs;
    private Application mockApplication;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Before
    public void setUp() {
        // Mock Jobs
        recruiterService = RecruiterService.getInstance();
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
                "JS, CSS", "");
        mockApplication = app1;

        app1.setAssignments(assignments);
        mockApplications.add(app1);


        // Mock users
        mockUsers = new ArrayList<>();
        Applicant user1 = new Applicant("1", "John", "Doe", "johnDoe", "bestpassword");
        mockUsers.add(user1);
        mockUsers.add(new Recruiter("2", "Jane", "Doe", "janeDoe", "bestpassword"));
    }

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
    }


    // ETY1 - STORY 22
    @Test
    public void testviewJobPostingForm() {

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

            outputStream.reset();

            mockedUtility.when(() -> Utility.inputOutput("Enter the New Job Title")).thenReturn(null);
            mockedUtility.when(() -> Utility.inputOutput("Enter the New Job Description")).thenReturn(null);

            Mockito.doCallRealMethod().doNothing().when(spyObject).viewJobPostingForm();

            spyObject.viewJobPostingForm();
            consoleOutput = outputStream.toString();

            Assert.assertTrue(consoleOutput.contains("Job Title or Job Description empty"));

            Mockito.verify(spyObject, times(2)).submitNewJobPost(any(),any());
            Mockito.verify(spyObject, times(2)).viewRecruiterDashboard();
            Mockito.verify(spyObject, times(3)).viewJobPostingForm();

            mockedUtility.verify(times(4), () -> Utility.inputOutput(Mockito.anyString()));

        }

    }

    // ETY1 - STORY 50
    @Test
    public void viewTotalNumberOfApplicationsTest() throws IOException {
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
    public void testApproveRejectApplication() {

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


            outputStream.reset();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("2");

            mockApplication.setStatus(ApplicationStatus.UNSUCCESSFUL);

            spyObject.approveRejectApplication(mockApplication);

            consoleOutput = outputStream.toString();

            Assert.assertTrue(consoleOutput.contains("Application Rejected"));

            outputStream.reset();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("3");

            spyObject.approveRejectApplication(mockApplication);

            consoleOutput = outputStream.toString();

            Assert.assertTrue(consoleOutput.contains("Directing to Application's Page"));

            outputStream.reset();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("invalid");

            Mockito.doCallRealMethod().doNothing().when(spyObject).approveRejectApplication(any());

            spyObject.approveRejectApplication(mockApplication);

            consoleOutput = outputStream.toString();

            Assert.assertTrue(consoleOutput.contains("You entered invalid option"));

            Mockito.verify(spyObject, times(3)).viewSpecificApplication(any());
            Mockito.verify(spyObject, times(5)).approveRejectApplication(Mockito.any());
            mockedUtility.verify(times(4), () -> Utility.inputOutput(Mockito.anyString()));
            mockedUtility.verify(times(2), () -> Utility.getApplications());

        }

    }

    // ETY1 - STORY 5
    @Test
    public void recruiterSignUpTest() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        RecruiterService service = new RecruiterService();
        RecruiterService spyObject = Mockito.spy(service);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            Mockito.doNothing().when(spyObject).viewRecruiterDashboard();
            Mockito.doNothing().when(spyObject).viewRecruiterSignUpPage();

            mockedUtility.when(Utility::getUsers).thenReturn(mockUsers);
            Recruiter recruiter=new Recruiter("newId","name", "surname", "username", "password");


            spyObject.recruiterSignUp("XVQTY", recruiter.getName(),recruiter.getLastName(),recruiter.getUserName(),recruiter.getPassword());
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Sign Up Successful for Recruiter"));

            outputStream.reset();

            spyObject.recruiterSignUp("12345", recruiter.getName(),recruiter.getLastName(),recruiter.getUserName(),recruiter.getPassword());

            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Invalid Attempt"));

            Mockito.verify(spyObject, times(1)).viewRecruiterDashboard();
            Mockito.verify(spyObject, times(1)).viewRecruiterSignUpPage();
            mockedUtility.verify(times(1), () -> Utility.getUsers());
        }
    }

    // ETY1 - STORY 42
    @Test
    public void viewAssessmentResultTest() {
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

            outputStream.reset();

            spyObject.viewAssessmentResult("App1", "A3");

            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("There is no Coding Assessment Result for this application"));
            Assert.assertTrue(consoleOutput.contains("Directing to Application Page"));

            Mockito.verify(spyObject, times(2)).viewSpecificApplication(any());
            mockedUtility.verify(times(2), () -> Utility.getApplications());

        }

    }


    @Test
    public void testViewRecruiterDashboard_ViewProfile() {
        // Simulate user input "1"
        String input = "1";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        RecruiterService recruiterService = Mockito.spy(new RecruiterService());
        doNothing().when(recruiterService).viewRecruiterProfilePage();

        recruiterService.viewRecruiterDashboard();

        verify(recruiterService, times(1)).viewRecruiterProfilePage();
    }

    @Test
    public void testViewRecruiterDashboard_ViewAvailableJobs() {
        // Simulate user input "2"
        String input = "2";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        RecruiterService recruiterService = Mockito.spy(new RecruiterService());
        doNothing().when(recruiterService).viewAvailableJobs();

        recruiterService.viewRecruiterDashboard();

        verify(recruiterService, times(1)).viewAvailableJobs();
    }

    @Test
    public void testViewRecruiterDashboard_ViewAllApplications() {
        // Simulate user input "3"
        String input = "3";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        RecruiterService recruiterService = Mockito.spy(new RecruiterService());
        doNothing().when(recruiterService).viewAllApplications();

        recruiterService.viewRecruiterDashboard();

        verify(recruiterService, times(1)).viewAllApplications();
    }

    @Test
    public void testViewRecruiterDashboard_InvalidInput() {
        // Simulate invalid user input
        String input = "0";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        RecruiterService recruiterService = Mockito.spy(new RecruiterService());
        Mockito.doNothing().when(recruiterService).viewRecruiterDashboard();

        recruiterService.viewRecruiterDashboard();

        verify(recruiterService, times(1)).viewRecruiterDashboard();
    }

    @Test
    public void testUpdateRecruiterProfile_ValidInputs() {
        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            // Arrange - user "John" "Doe" with username "johnDoe" provided
            mockedUtility.when(Utility::getCurrentUser).thenReturn(mockUsers.get(0));
            mockedUtility.when(() -> Utility.getUsers()).thenReturn(mockUsers);
            RecruiterService recruiterService = Mockito.spy(new RecruiterService());
            mockedUtility.when(() -> Utility.inputOutput(anyString())).thenReturn("Jack",
                    "Dew", "jack");
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
    public void testUpdateRecruiterProfile_EmptyNameAndLastname() {
        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            // Arrange - user "John" "Doe" with username "johnDoe" provided
            mockedUtility.when(Utility::getCurrentUser).thenReturn(mockUsers.get(0));
            mockedUtility.when(() -> Utility.getUsers()).thenReturn(mockUsers);
            RecruiterService recruiterService = Mockito.spy(new RecruiterService());
            mockedUtility.when(() -> Utility.inputOutput(anyString())).thenReturn("", "",
                    "jack");
            doNothing().when(recruiterService).viewRecruiterProfilePage();

            // Act
            recruiterService.updateRecruiterProfile();

            // Assert
            assertEquals("John", Utility.getCurrentUser().getName());
            assertEquals("Doe", Utility.getCurrentUser().getLastName());
            assertEquals("jack", Utility.getCurrentUser().getUserName());
        }
    }

    @Test
    public void testUpdateRecruiterProfile_UsernameNotUnique() {
        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            // Arrange - user "John" "Doe" with username "johnDoe" provided
            mockedUtility.when(Utility::getCurrentUser).thenReturn(mockUsers.get(0));
            mockedUtility.when(() -> Utility.getUsers()).thenReturn(mockUsers);
            RecruiterService recruiterService = Mockito.spy(new RecruiterService());

            // jack is unique username, johnDoe is non-unique username. So, first invalid,
            // then valid input provided
            mockedUtility.when(() -> Utility.inputOutput(anyString())).thenReturn("Jack",
            "Dew", "johnDoe", "jack");
            doNothing().when(recruiterService).viewRecruiterProfilePage();

            // Act
            recruiterService.updateRecruiterProfile();

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

            assertEquals("Jack", Utility.getCurrentUser().getName());
            assertEquals("Dew", Utility.getCurrentUser().getLastName());
            assertTrue(filteredLines.contains("Provided username is empty or already exists. Please try again."));
        }
    }

    @Test
    public void testViewSpecificApplication_ApplicationNotFound() {
        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            mockedUtility.when(Utility::getApplications).thenReturn(mockApplications);
            mockedUtility.when(Utility::getUsers).thenReturn(mockUsers);
            RecruiterService recruiterService = Mockito.spy(new RecruiterService());
            doNothing().when(recruiterService).viewAllApplications();

            // Invalid application ID given
            recruiterService.viewSpecificApplication("999g");

            String expectedOutput = "No application found with given id";
            assertEquals(expectedOutput, outContent.toString().trim());
        }
    }

    @Test
    public void testViewSpecificApplication_ApplicantNotFound() {
        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            mockedUtility.when(Utility::getApplications).thenReturn(mockApplications);

            // No users given, so applicant will not be found
            mockedUtility.when(Utility::getUsers).thenReturn(new ArrayList<>());
            RecruiterService recruiterService = Mockito.spy(new RecruiterService());
            doNothing().when(recruiterService).viewAllApplications();

            recruiterService.viewSpecificApplication("1");

            String expectedOutput = "No applicant found with for this application";
            assertEquals(expectedOutput, outContent.toString().trim());
        }
    }

    @Test
    public void testViewSpecificApplication_ValidApplicationAndUser() {
        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            mockedUtility.when(Utility::getApplications).thenReturn(mockApplications);
            mockedUtility.when(Utility::getUsers).thenReturn(mockUsers);

            RecruiterService recruiterService = Mockito.spy(new RecruiterService());
            doNothing().when(recruiterService).viewAllApplications();
            doNothing().when(recruiterService).approveRejectApplication(mockApplications.get(0));
            doNothing().when(recruiterService).sendAssignment(mockApplications.get(0));
            doNothing().when(recruiterService).sendInterview(mockApplications.get(0));
            doNothing().when(recruiterService).sendFeedback(mockApplications.get(0));
            doNothing().when(recruiterService).viewSubmittedAnswers(mockApplications.get(0));

            // Checking all the switch branches one by one
            mockedUtility.when(() -> Utility.inputOutput(anyString())).thenReturn("1");
            recruiterService.viewSpecificApplication("1");

            mockedUtility.when(() -> Utility.inputOutput(anyString())).thenReturn("2");
            recruiterService.viewSpecificApplication("1");

            mockedUtility.when(() -> Utility.inputOutput(anyString())).thenReturn("3");
            recruiterService.viewSpecificApplication("1");

            mockedUtility.when(() -> Utility.inputOutput(anyString())).thenReturn("4");
            recruiterService.viewSpecificApplication("1");

            mockedUtility.when(() -> Utility.inputOutput(anyString())).thenReturn("5");
            recruiterService.viewSpecificApplication("1");
            
            mockedUtility.when(() -> Utility.inputOutput(anyString())).thenReturn("6");
            recruiterService.viewSpecificApplication("1");

            verify(recruiterService, times(1)).sendFeedback(mockApplications.get(0));
            verify(recruiterService, times(1)).approveRejectApplication(mockApplications.get(0));
            verify(recruiterService, times(1)).sendAssignment(mockApplications.get(0));
            verify(recruiterService, times(1)).sendInterview(mockApplications.get(0));
            verify(recruiterService, times(1)).viewAllApplications();
        }
    }

    @Test
    public void testViewSpecificApplication_InvalidOption() {
        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            mockedUtility.when(Utility::getApplications).thenReturn(mockApplications);
            mockedUtility.when(Utility::getUsers).thenReturn(mockUsers);

            // First input Invalid then one valid input
            mockedUtility.when(() -> Utility.inputOutput(anyString())).thenReturn("Invalid", "1");
            RecruiterService recruiterService = Mockito.spy(new RecruiterService());
            doNothing().when(recruiterService).viewAllApplications();

            recruiterService.viewSpecificApplication("1");

            verify(recruiterService, times(1)).viewAllApplications();
        }
    }

    @Test
    public void testSendAssignment_ValidRole() {
        // try (MockedStatic<Utility> utilities = mockStatic(Utility.class)) {
        //     // Mock dependencies
        //     Map<String, List<String>> questionMap = new HashMap<>();
        //     questionMap.put("frontend", Arrays.asList("Q1", "Q2"));
        //     utilities.when(Utility::getQuestionMap).thenReturn(questionMap);
        //     utilities.when(() -> Utility.inputOutput(anyString())).thenReturn("frontend");
        //     // Clear assignments if any
        //     mockApplication.setAssignments(new ArrayList<Assignment>());

        //     // Invoke the method
        //     RecruiterService recruiterService = Mockito.spy(new RecruiterService());
        //     recruiterService.sendAssignment(mockApplication);

        //     // Assertions
        //     assertEquals(1, mockApplication.getAssignments().size());
        //     Assignment assignment = mockApplication.getAssignments().get(0);
        //     assertEquals("Assignment frontend", assignment.getAssignmentName());
        //     assertEquals(Arrays.asList("Q1", "Q2"), assignment.getQuestions());
        //     assertEquals("1", assignment.getApplicantId());

        //     // Verify interaction with mocked methods
        //     utilities.verify(Utility::getQuestionMap);
        //     utilities.verify(() -> Utility.inputOutput(anyString()));
        // }
    }

    @Test
    public void testSendAssignment_InvalidRole() {
        // try (MockedStatic<Utility> utilities = mockStatic(Utility.class)) {
        //     // Arrange and mock dependencies
        //     Map<String, List<String>> questionMap = new HashMap<>();
        //     questionMap.put("frontend", Arrays.asList("Q1", "Q2"));
        //     utilities.when(Utility::getQuestionMap).thenReturn(questionMap);
        //     utilities.when(() -> Utility.inputOutput(anyString())).thenReturn("invalid_role");
        //     // Clear assignments if any
        //     mockApplication.setAssignments(new ArrayList<Assignment>());

        //     // Invoke the method
        //     RecruiterService recruiterService = Mockito.spy(new RecruiterService());
        //     recruiterService.sendAssignment(mockApplication);

        //     // Assertions
        //     assertEquals("0", Integer.toString(mockApplication.getAssignments().size()));

        //     // Verify interaction with mocked methods
        //     utilities.verify(Utility::getQuestionMap);
        //     utilities.verify(() -> Utility.inputOutput(anyString()));
        // }
    }

    @Test
    public void testSendAssignment_NullRole() {
        // try (MockedStatic<Utility> utilities = mockStatic(Utility.class)) {
        //     // Mock dependencies
        //     Map<String, List<String>> questionMap = new HashMap<>();
        //     questionMap.put("frontend", Arrays.asList("Q1", "Q2"));
        //     utilities.when(Utility::getQuestionMap).thenReturn(questionMap);
        //     utilities.when(() -> Utility.inputOutput(anyString())).thenReturn(null);
        //     // Clear assignments if any
        //     mockApplication.setAssignments(new ArrayList<Assignment>());

        //     // Invoke the method
        //     RecruiterService recruiterService = Mockito.spy(new RecruiterService());
        //     recruiterService.sendAssignment(mockApplication);

        //     // Assertions
        //     assertEquals("0", Integer.toString(mockApplication.getAssignments().size()));

        //     // Verify interaction with mocked methods
        //     utilities.verify(Utility::getQuestionMap);
        //     utilities.verify(() -> Utility.inputOutput(anyString()));
        // }
    }

    @Test
    public void testSendAssignment_NoQuestionsInMap() {
        // try (MockedStatic<Utility> utilities = mockStatic(Utility.class)) {
        //     // Mock dependencies
        //     Map<String, List<String>> questionMap = new HashMap<>();
        //     utilities.when(Utility::getQuestionMap).thenReturn(questionMap);
        //     utilities.when(() -> Utility.inputOutput(anyString())).thenReturn("frontend");
        //     // Clear assignments if any
        //     mockApplication.setAssignments(new ArrayList<Assignment>());

        //     // Invoke the method
        //     RecruiterService recruiterService = Mockito.spy(new RecruiterService());
        //     recruiterService.sendAssignment(mockApplication);

        //     // Assertions
        //     assertEquals("0", Integer.toString(mockApplication.getAssignments().size()));

        //     // Verify interaction with mocked methods
        //     utilities.verify(Utility::getQuestionMap);
        //     utilities.verify(() -> Utility.inputOutput(anyString()));
        // }
    }

    @Test
    public void testUpdateDescriptionOfJobPost_NullJobList() {
        try (MockedStatic<Utility> utilities = mockStatic(Utility.class)) {
            // Arrange
            utilities.when(Utility::getJobs).thenReturn(null);

            // Act
            RecruiterService recruiterService = Mockito.spy(new RecruiterService());
            recruiterService.updateDescriptionOfJobPost("1");

            // Assert
            // Get the complete output
            String output = outContent.toString().trim();

            // Extract the last println message (messages are separated by newlines)
            String[] lines = output.split("\n");
            String lastMessage = lines[lines.length - 1];

            assertEquals("No jobs available", lastMessage);
        }
    }

    @Test
    public void testUpdateDescriptionOfJobPost_EmptyJobList() {
        try (MockedStatic<Utility> utilities = mockStatic(Utility.class)) {
            // Arrange
            utilities.when(Utility::getJobs).thenReturn(new ArrayList<>());

            // Act
            RecruiterService recruiterService = Mockito.spy(new RecruiterService());
            recruiterService.updateDescriptionOfJobPost("1");

            // Assert
            // Get the complete output
            String output = outContent.toString().trim();

            // Extract the last println message (messages are separated by newlines)
            String[] lines = output.split("\n");
            String lastMessage = lines[lines.length - 1];

            assertEquals("No jobs available", lastMessage);
        }
    }

    @Test
    public void testUpdateDescriptionOfJobPost_JobIdNotFound() {
        try (MockedStatic<Utility> utilities = mockStatic(Utility.class)) {
            // Arrange
            utilities.when(Utility::getJobs).thenReturn(mockJobs);
            utilities.when(() -> Utility.inputOutput(anyString())).thenReturn("Updated Job Title")
            .thenReturn("Updated job description");
            RecruiterService recruiterService = Mockito.spy(new RecruiterService());
            doNothing().when(recruiterService).viewAvailableJobs();

            // Act
            recruiterService.updateDescriptionOfJobPost("864651896461651");

            // Assert
            // Get the complete output
            String output = outContent.toString().trim();

            // Extract the last println message (messages are separated by newlines)
            String[] lines = output.split("\n");
            String lastMessage = lines[lines.length - 1];

            assertEquals("No job post available with given id", lastMessage);

            // Verify
            verify(recruiterService, times(1)).viewAvailableJobs();
            verify(recruiterService,
            times(1)).updateDescriptionOfJobPost("864651896461651");
        }
    }

    @Test
    public void testUpdateDescriptionOfJobPost_NewTitleEmpty() {
        try (MockedStatic<Utility> utilities = mockStatic(Utility.class)) {
            // Arrange
            utilities.when(Utility::getJobs).thenReturn(mockJobs);
            utilities.when(() ->
            Utility.inputOutput(anyString())).thenReturn("").thenReturn("Updated job description");
            RecruiterService recruiterService = Mockito.spy(new RecruiterService());
            doNothing().when(recruiterService).viewAvailableJobs();

            // Act
            recruiterService.updateDescriptionOfJobPost("1");

            // Assert - Verify "Job title not updated as it is empty"
            assertEquals(mockJobs.get(0).getJobName(),
            Utility.getJobs().get(0).getJobName());
        }
    }

    @Test
    public void testUpdateDescriptionOfJobPost_NewDescriptionEmpty() {
        try (MockedStatic<Utility> utilities = mockStatic(Utility.class)) {
            // Arrange
            utilities.when(Utility::getJobs).thenReturn(mockJobs);
            utilities.when(() -> Utility.inputOutput(anyString())).thenReturn("Updated Job Title").thenReturn("");
            RecruiterService recruiterService = Mockito.spy(new RecruiterService());
            doNothing().when(recruiterService).viewAvailableJobs();

            // Act
            recruiterService.updateDescriptionOfJobPost("1");

            // Assert - Verify "Job description not updated as it is empty"
            assertEquals(mockJobs.get(0).getJobDescription(),
            Utility.getJobs().get(0).getJobDescription());
        }
    }

    @Test
    public void testUpdateDescriptionOfJobPost_ValidJobUpdate() {
        try (MockedStatic<Utility> utilities = mockStatic(Utility.class)) {
            // Arrange
            // Initial Jobs have different job name and description
            utilities.when(Utility::getJobs).thenReturn(mockJobs);
            utilities.when(() -> Utility.inputOutput(anyString())).thenReturn("Updated Job Title")
            .thenReturn("Updated job description");
            RecruiterService recruiterService = Mockito.spy(new RecruiterService());
            doNothing().when(recruiterService).viewAvailableJobs();

            // Act
            recruiterService.updateDescriptionOfJobPost("1");

            // Assert - Verify "Job title and description updated"
            assertEquals("Updated Job Title", Utility.getJobs().get(0).getJobName());
            assertEquals("Updated job description",
            Utility.getJobs().get(0).getJobDescription());
        }
    }

    @Test
    public void testSendFeedback_EmptyFeedback() {
        try (MockedStatic<Utility> utilities = mockStatic(Utility.class)) {
            // Arrange
            // One application with empty feedback initially
            utilities.when(Utility::getApplications).thenReturn(mockApplications);
            utilities.when(() -> Utility.inputOutput(anyString())).thenReturn("");
            RecruiterService recruiterService = Mockito.spy(new RecruiterService());
            // doNothing().when(recruiterService).sendFeedback(mockApplication);

            // Act
            recruiterService.sendFeedback(mockApplication);

            // Assert
            // Get the complete output
            String output = outContent.toString().trim();

            // Extract the last println message (messages are separated by newlines)
            String[] lines = output.split("\n");
            String lastMessage = lines[lines.length - 1];

            assertEquals("Feedback not updated as it is empty", lastMessage);
            assertEquals("", Utility.getApplications().get(0).getFeedback());
        }
    }

    @Test
    public void testSendFeedback_ValidFeedback() {
        try (MockedStatic<Utility> utilities = mockStatic(Utility.class)) {
            // Arrange
            // One application with empty feedback initially
            utilities.when(Utility::getApplications).thenReturn(mockApplications);
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
            assertEquals("Valid feedback",
            Utility.getApplications().get(0).getFeedback());
        }
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 6
     */@Test
    public void testAuthenticateUser_Success() {
        ArrayList<User> users = new ArrayList<>();
        User user = new User("1", "John", "Doe", "johndoe", "password", UserRole.RECRUITER);
        users.add(user);

        RecruiterService spyRecruiterService = Mockito.spy(recruiterService);
        User authenticatedUser = spyRecruiterService.authenticateUser(users, "johndoe", "password");

        assertNotNull(authenticatedUser);
        assertEquals("johndoe", authenticatedUser.getUserName());
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 6
     */
    @Test
    public void testAuthenticateUser_Failure_WrongPassword() {
        ArrayList<User> users = new ArrayList<>();
        User user = new User("1", "John", "Doe", "johndoe", "password", UserRole.RECRUITER);
        users.add(user);

        RecruiterService spyRecruiterService = Mockito.spy(recruiterService);
        User authenticatedUser = spyRecruiterService.authenticateUser(users, "johndoe", "wrongpassword");

        assertNull(authenticatedUser);
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 6
     */
    @Test
    public void testAuthenticateUser_Failure_WrongUsername() {
        ArrayList<User> users = new ArrayList<>();
        User user = new User("1", "John", "Doe", "johndoe", "password", UserRole.RECRUITER);
        users.add(user);

        RecruiterService spyRecruiterService = Mockito.spy(recruiterService);
        User authenticatedUser = spyRecruiterService.authenticateUser(users, "wrongusername", "password");

        assertNull(authenticatedUser);
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 6
     */
    @Test
    public void testAuthenticateUser_Failure_EmptyList() {
        ArrayList<User> users = new ArrayList<>();

        RecruiterService spyRecruiterService = Mockito.spy(recruiterService);
        User authenticatedUser = spyRecruiterService.authenticateUser(users, "johndoe", "password");

        assertNull(authenticatedUser);
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 6
     */
    @Test
    public void testRecruiterSignInPage_validRecruiterCredentials() {
        User validRecruiter = new Recruiter("123", "John", "Doe", "validUser", "validPass");

        ArrayList<User> mockUsers = new ArrayList<>();
        mockUsers.add(validRecruiter);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            mockedUtility.when(Utility::getUsers).thenReturn(mockUsers);

            mockedUtility
                    .when(() -> Utility.inputOutput("Enter your User name:"))
                    .thenReturn("validUser");
            mockedUtility
                    .when(() -> Utility.inputOutput("Enter your password:"))
                    .thenReturn("validPass");

            mockedUtility
                    .when(() -> Utility.setCurrentUser(any(User.class)))
                    .thenAnswer(inv -> {
                        User userSet = inv.getArgument(0);
                        System.out.println("setCurrentUser called with: " + userSet.getUserName());
                        return null;
                    });

            CommonService mockCommonService = mock(CommonService.class);
            try (MockedStatic<CommonService> mockedCommonService = Mockito.mockStatic(CommonService.class)) {
                mockedCommonService.when(CommonService::getInstance).thenReturn(mockCommonService);

                RecruiterService spyRecruiterService = spy(new RecruiterService());
                try (MockedStatic<RecruiterService> mockedRecruiterService = Mockito
                        .mockStatic(RecruiterService.class)) {
                    mockedRecruiterService.when(RecruiterService::getInstance).thenReturn(spyRecruiterService);
                    doNothing().when(spyRecruiterService).viewRecruiterDashboard();
                    spyRecruiterService.recruiterSignInPage();

                    mockedUtility.verify(atLeastOnce(), () -> Utility.setCurrentUser(validRecruiter));
                    verify(spyRecruiterService, times(1)).viewRecruiterDashboard();
                }
            }
        }
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 6
     */
    @Test
    public void testRecruiterSignInPage_invalidCredentials_tryAgainYes() {
        // No matching recruiter
        ArrayList<User> emptyUsers = new ArrayList<>();

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            mockedUtility.when(Utility::getUsers).thenReturn(emptyUsers);

            mockedUtility.when(() -> Utility.inputOutput("Enter your User name:"))
                    .thenReturn("wrongUser");
            mockedUtility.when(() -> Utility.inputOutput("Enter your password:"))
                    .thenReturn("wrongPass");
            mockedUtility
                    .when(() -> Utility.inputOutput("\nInvalid username or password. Do you want to try again? (y/n)"))
                    .thenReturn("y")
                    .thenReturn("n");

            CommonService mockCommonService = mock(CommonService.class);
            try (MockedStatic<CommonService> mockedCommonService = Mockito.mockStatic(CommonService.class)) {
                mockedCommonService.when(CommonService::getInstance).thenReturn(mockCommonService);

                RecruiterService spyRecruiterService = spy(new RecruiterService());
                try (MockedStatic<RecruiterService> mockedRecruiterService = Mockito
                        .mockStatic(RecruiterService.class)) {
                    mockedRecruiterService.when(RecruiterService::getInstance).thenReturn(spyRecruiterService);
                    spyRecruiterService.recruiterSignInPage();
                    verify(spyRecruiterService, times(2)).recruiterSignInPage();
                    verify(mockCommonService, times(1)).accessLandingPage();
                }
            }
        }
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 6
     */
    @Test
    public void testRecruiterSignInPage_invalidCredentials_tryAgainNo() {
        ArrayList<User> emptyUsers = new ArrayList<>();

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            mockedUtility.when(Utility::getUsers).thenReturn(emptyUsers);
            mockedUtility.when(() -> Utility.inputOutput("Enter your User name:"))
                    .thenReturn("invalidUser");
            mockedUtility.when(() -> Utility.inputOutput("Enter your password:"))
                    .thenReturn("invalidPass");
            mockedUtility
                    .when(() -> Utility.inputOutput("\nInvalid username or password. Do you want to try again? (y/n)"))
                    .thenReturn("n");

            CommonService mockCommonService = mock(CommonService.class);
            try (MockedStatic<CommonService> mockedCommonService = Mockito.mockStatic(CommonService.class)) {
                mockedCommonService.when(CommonService::getInstance).thenReturn(mockCommonService);

                RecruiterService spyRecruiterService = spy(new RecruiterService());
                try (MockedStatic<RecruiterService> mockedRecruiterService = Mockito
                        .mockStatic(RecruiterService.class)) {
                    mockedRecruiterService.when(RecruiterService::getInstance).thenReturn(spyRecruiterService);

                    spyRecruiterService.recruiterSignInPage();

                    // Verify we went to landing page (because user typed "n")
                    verify(mockCommonService, times(1)).accessLandingPage();
                    // And we do NOT call viewRecruiterDashboard
                    verify(spyRecruiterService, never()).viewRecruiterDashboard();
                }
            }
        }
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 6
     */
    @Test
    public void testRecruiterSignInPage_validCredentialsButNotRecruiter() {
        // The user is valid, but role is APPLICANT
        User applicantUser = new Recruiter("001", "Jane", "Doe", "appUser", "appPass");
        applicantUser.setRole(UserRole.APPLICANT);

        ArrayList<User> mockUsers = new ArrayList<>();
        mockUsers.add(applicantUser);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            mockedUtility.when(Utility::getUsers).thenReturn(mockUsers);
            mockedUtility.when(() -> Utility.inputOutput("Enter your User name:"))
                    .thenReturn("appUser");
            mockedUtility.when(() -> Utility.inputOutput("Enter your password:"))
                    .thenReturn("appPass");

            // After it checks role != RECRUITER, it’s invalid => asks user if they want to
            // try again
            mockedUtility
                    .when(() -> Utility.inputOutput("\nInvalid username or password. Do you want to try again? (y/n)"))
                    .thenReturn("n");

            // Mock CommonService
            CommonService mockCommonService = mock(CommonService.class);
            try (MockedStatic<CommonService> mockedCommonService = Mockito.mockStatic(CommonService.class)) {
                mockedCommonService.when(CommonService::getInstance).thenReturn(mockCommonService);

                RecruiterService spyRecruiterService = spy(new RecruiterService());
                try (MockedStatic<RecruiterService> mockedRecruiterService = Mockito
                        .mockStatic(RecruiterService.class)) {
                    mockedRecruiterService.when(RecruiterService::getInstance).thenReturn(spyRecruiterService);

                    // Execute
                    spyRecruiterService.recruiterSignInPage();

                    // Verify redirection
                    verify(mockCommonService, times(1)).accessLandingPage();
                    // No call to setCurrentUser, no call to viewRecruiterDashboard
                    mockedUtility.verify(never(), () -> Utility.setCurrentUser(any()));
                    // mockedUtility.verify(() -> Utility.setCurrentUser(any()), never());
                    verify(spyRecruiterService, never()).viewRecruiterDashboard();
                }
            }
        }
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 11
     */
    @Test
    public void testViewResetPasswordPage_userChoosesYes() {
        // 1. Spy the RecruiterService so we can verify internal method calls.
        RecruiterService spyService = Mockito.spy(recruiterService);
        User user = new Recruiter("1", "John", "Doe", "johnDoe", "bestpassword");
        // 2. Mock static Utility methods in a try-with-resources
        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {

            mockedUtility
                    .when(() -> Utility.getCurrentUser())
                    .thenReturn(user);
            // The first prompt
            mockedUtility
                    .when(() -> Utility.inputOutput("Do you want to reset your password? (y/n)"))
                    .thenReturn("y");

            // The second prompt (because user said 'y')
            mockedUtility
                    .when(() -> Utility.inputOutput("Enter your User name"))
                    .thenReturn("johnDoe");

            // 3. Call the method under test (assuming we've made it public or
            // test-accessible)
            // If it's truly private, you might need to call a public method that triggers
            // it.
            // For demonstration, assume we can do:
            spyService.viewResetPasswordPage();

            // 4. Verify that resetPasswordRecruiter(...) was called with "johnDoe"
            verify(spyService, times(1)).resetPasswordRecruiter("johnDoe");

            // 5. Verify that viewRecruiterDashboard() was NOT called
            verify(spyService, never()).viewRecruiterDashboard();
        }
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 11
     */
    @Test
    public void testViewResetPasswordPage_userChoosesNo() {
        // 1. Spy the RecruiterService
        RecruiterService spyService = Mockito.spy(recruiterService);
        User user = new Recruiter("1", "John", "Doe", "johnDoe", "bestpassword");

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            mockedUtility
                    .when(() -> Utility.getCurrentUser())
                    .thenReturn(user);
            // The first prompt
            mockedUtility
                    .when(() -> Utility.inputOutput("Do you want to reset your password? (y/n)"))
                    .thenReturn("n");

            // 2. Mock the resetPasswordRecruiter(...) method so we can verify it's not
            // called
            doNothing().when(spyService).viewRecruiterDashboard();

            // 3. Call the method
            spyService.viewResetPasswordPage();

            // 4. Verify that we do NOT call resetPasswordRecruiter(...)
            verify(spyService, never()).resetPasswordRecruiter(anyString());

            // 5. Verify we call viewRecruiterDashboard()
            verify(spyService, times(1)).viewRecruiterDashboard();
        }
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 11
     */
    @Test
    public void testViewResetPasswordPage_userChoosesSomethingElse() {
        // Same as "no" scenario, because the code just checks `equalsIgnoreCase("y")`
        RecruiterService spyService = Mockito.spy(recruiterService);
        User user = new Recruiter("1", "John", "Doe", "johnDoe", "bestpassword");

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            mockedUtility
                    .when(() -> Utility.getCurrentUser())
                    .thenReturn(user);
            mockedUtility
                    .when(() -> Utility.inputOutput("Do you want to reset your password? (y/n)"))
                    .thenReturn("maybe");
            doNothing().when(spyService).viewRecruiterDashboard();

            spyService.viewResetPasswordPage();

            verify(spyService, never()).resetPasswordRecruiter(anyString());
            verify(spyService, times(1)).viewRecruiterDashboard();
        }
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 23
     */
    @Test
    public void submitNewJobPost() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        ArrayList<Job> jobs = Utility.getJobs();
        int previousSize = jobs == null ? 0 : jobs.size();
        recruiterService.submitNewJobPost("Software Engineer", "Develop software");
        String consoleOutput = outputStream.toString();
        Assert.assertTrue(consoleOutput.contains("Job posted successfully"));
        Assert.assertEquals(previousSize + 1, Utility.getJobs().size());
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 24
     */
    @Test
    public void testViewAvailableJobs_noJobs() {
        // Scenario 1: jobs == null or empty

        // We'll capture console output so we can assert the "No jobs available" message
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        // Spy on the recruiterService so we can verify calls to
        // viewRecruiterDashboard()
        RecruiterService spyService = Mockito.spy(recruiterService);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            // Mock Utility.getJobs() to return null or empty
            mockedUtility.when(Utility::getJobs).thenReturn(new ArrayList<>()); // empty list
            doNothing().when(spyService).viewRecruiterDashboard();
            // Act
            spyService.viewAvailableJobs();

            // Assert:
            // 1) "No jobs available" should be printed
            String consoleOutput = outContent.toString();
            assertTrue(consoleOutput.contains("No jobs available"));

            // 2) It should call viewRecruiterDashboard()
            verify(spyService, times(1)).viewRecruiterDashboard();
        } finally {
            // Restore original System.out
            System.setOut(originalOut);
        }
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 24
     */
    @Test
    public void testViewAvailableJobs_jobsAvailable_userChooses1() {
        // Scenario 2a: jobs available, user picks "1", calls viewSpecificJobPost()

        // Prepare a non-empty list of jobs
        ArrayList<Job> jobs = new ArrayList<>();
        Job job1 = new Job("id1", "Title1", "Desc1", JobStatus.PUBLIC);
        Job job2 = new Job("id2", "Title2", "Desc2", JobStatus.PUBLIC);
        jobs.add(job1);
        jobs.add(job2);

        // Capture console output (optional)
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        // Spy
        RecruiterService spyService = Mockito.spy(recruiterService);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            mockedUtility.when(Utility::getJobs).thenReturn(jobs);

            // We expect the method to prompt: "Please select one of the options.."
            // Return "1" → calls viewSpecificJobPost() → exit loop
            mockedUtility
                    .when(() -> Utility.inputOutput("Please select one of the options.."))
                    .thenReturn("1");
            doNothing().when(spyService).viewSpecificJobPost();

            // Act
            spyService.viewAvailableJobs();

            // Assert the console output contains "Available Jobs" and the job list
            String consoleOutput = outContent.toString();
            assertTrue(consoleOutput.contains("Available Jobs"));
            assertTrue(consoleOutput.contains("Job ID: id1"));
            assertTrue(consoleOutput.contains("Job ID: id2"));

            // Verify it calls viewSpecificJobPost()
            verify(spyService, times(1)).viewSpecificJobPost();

            // Verify we never call viewRecruiterDashboard() in this scenario
            verify(spyService, never()).viewRecruiterDashboard();
        } finally {
            System.setOut(originalOut);
        }
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 24
     */
    @Test
    public void testViewAvailableJobs_jobsAvailable_userChooses2() {
        // Scenario 2b: user picks "2" => calls viewRecruiterDashboard()

        ArrayList<Job> jobs = new ArrayList<>();
        jobs.add(new Job("id1", "Title1", "Desc1", JobStatus.PUBLIC));

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        RecruiterService spyService = Mockito.spy(recruiterService);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            mockedUtility.when(Utility::getJobs).thenReturn(jobs);

            // Return "2" → should exit loop and call viewRecruiterDashboard()
            mockedUtility
                    .when(() -> Utility.inputOutput("Please select one of the options.."))
                    .thenReturn("2");
            doNothing().when(spyService).viewRecruiterDashboard();
            spyService.viewAvailableJobs();

            verify(spyService, times(1)).viewRecruiterDashboard();
            verify(spyService, never()).viewSpecificJobPost();
        } finally {
            System.setOut(originalOut);
        }
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 24
     */
    @Test
    public void testViewAvailableJobs_jobsAvailable_invalidInputThenValid() {
        // Scenario 2c: user inputs something invalid, then eventually picks "1" or "2"
        // We'll simulate user typing "abc" (invalid) then "2" (go to dashboard)

        ArrayList<Job> jobs = new ArrayList<>();
        jobs.add(new Job("idX", "TitleX", "DescX", JobStatus.PUBLIC));

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        RecruiterService spyService = Mockito.spy(recruiterService);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            mockedUtility.when(Utility::getJobs).thenReturn(jobs);

            // The user is prompted in a loop, so we return "abc" the first time (invalid),
            // then "2" the second time (valid).
            mockedUtility
                    .when(() -> Utility.inputOutput("Please select one of the options.."))
                    .thenReturn("abc")
                    .thenReturn("2");

            doNothing().when(spyService).viewRecruiterDashboard();

            spyService.viewAvailableJobs();

            // Check console output
            String output = outContent.toString();
            // Should contain "is not a valid option. Please try again."
            assertTrue(output.contains("abc is not a valid option. Please try again."));

            // Finally user enters "2" => calls viewRecruiterDashboard()
            verify(spyService, times(1)).viewRecruiterDashboard();

            // No calls to viewSpecificJobPost()
            verify(spyService, never()).viewSpecificJobPost();
        } finally {
            System.setOut(originalOut);
        }
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 33
     */
    @Test
    public void updateStatusOfJobPost_Empty() {
        ArrayList<Job> mockJobs = new ArrayList<>();
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));
        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            mockedUtility.when(Utility::getJobs).thenReturn(mockJobs);
            recruiterService.updateStatusOfJobPost("1");
            String consoleOutput = outContent.toString();
            Assert.assertTrue(consoleOutput.contains("No jobs available"));
        } finally {
            System.setOut(originalOut);
        }

    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 33
     */
    @Test
    public void updateStatusOfJobPost_JobNotAvailable() {

        ArrayList<Job> mockJobs = new ArrayList<>();
        mockJobs.add(new Job("1", "Software Engineer", "Develop software", JobStatus.PUBLIC));
        mockJobs.add(new Job("2", "Software Engineer", "Develop software", JobStatus.PUBLIC));
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));
        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            mockedUtility.when(Utility::getJobs).thenReturn(mockJobs);
            recruiterService.updateStatusOfJobPost("5");
            String consoleOutput = outContent.toString();
            Assert.assertTrue(consoleOutput.contains("No job post available with given id"));
        } finally {
            System.setOut(originalOut);
        }
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 33
     */
    @Test
    public void updateStatusOfJobPost_Success() {
        ArrayList<Job> mockJobs = new ArrayList<>();
        mockJobs.add(new Job("1", "Software Engineer", "Develop software", JobStatus.PUBLIC));
        mockJobs.add(new Job("2", "Software Engineer", "Develop software", JobStatus.PUBLIC));
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));
        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            mockedUtility.when(Utility::getJobs).thenReturn(mockJobs);
            recruiterService.updateStatusOfJobPost("1");
            String consoleOutput = outContent.toString();
            Assert.assertTrue(consoleOutput.contains("Status of job updated successfully"));
            Job job = Utility.getJobs().stream().filter(j -> j.getId().equals("1")).findFirst().orElse(null);
            assert job != null;
            Assert.assertEquals(JobStatus.PRIVATE, job.getJobStatus());
        } finally {
            System.setOut(originalOut);
        }

    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 43
     */
    @Test
    public void testSendInterview_userImmediatelyExits() {
        // Scenario 1: user types "exit" on the first prompt
        RecruiterService spyService = Mockito.spy(recruiterService);

        // Prepare application
        Application application = new Application();
        application.setId("app123");
        application.setApplicantId("applicant123");
        application.setAssignments(new ArrayList<Assignment>());
        application.setInterviewAssignments(new ArrayList<Assignment>());

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {

            // For the first question prompt, user types "exit"
            mockedUtility
                    .when(() -> Utility.inputOutput("Enter the question..(type 'exit' to go back)"))
                    .thenReturn("exit");
            doNothing().when(spyService).viewSpecificApplication("app123");
            // Act
            spyService.sendInterview(application);

            // Assert:
            // 1) We should call viewSpecificApplication(...) with "app123"
            verify(spyService, times(1)).viewSpecificApplication("app123");

            // 2) We do NOT add any interview assignment to application
            assertTrue(application.getInterviewAssignments().isEmpty());
        }
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 43
     */
    @Test
    public void testSendInterview_userEntersEmptyQuestionThenValidQuestionThenNo() {
        // Scenario 2 & 3 combined:
        // - user first enters empty question => "Question cannot be empty"
        // - user then enters a valid question => "some question"
        // - user chooses "n" => break from while loop

        RecruiterService spyService = Mockito.spy(recruiterService);

        // Prepare application
        Application application = new Application();
        application.setId("app123");
        application.setApplicantId("applicant123");

        // Suppose there's already 1 assignment in the system (this will influence the
        // interview title)
        ArrayList<Assignment> existingAssignments = new ArrayList<>();
        existingAssignments.add(new Assignment("a1", "applicant123", "FirstAssignment", null, null));
        application.setAssignments(existingAssignments);

        // Interview assignments are initially empty
        application.setInterviewAssignments(new ArrayList<Assignment>());

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            // Mock user inputs in sequence:
            // 1) user enters "" (empty)
            // 2) user enters "some question"
            // 3) user answers "n" to "Do you want to add more questions?"
            // Then we expect the loop to break

            // For the question prompt:
            mockedUtility
                    .when(() -> Utility.inputOutput("Enter the question..(type 'exit' to go back)"))
                    .thenReturn("")
                    .thenReturn("some question");

            // For the "Do you want to add more questions?" prompt:
            mockedUtility
                    .when(() -> Utility.inputOutput("Do you want to add more questions? (y/n)"))
                    .thenReturn("n");

            // For the final "Press enter to go back." prompt
            mockedUtility
                    .when(() -> Utility.inputOutput("Press enter to go back."))
                    .thenReturn(""); // user presumably hits enter

            // Do nothing when viewSpecificApplication is called
            doNothing().when(spyService).viewSpecificApplication("app123");
            // Act
            spyService.sendInterview(application);

            // Assert:
            // 1) We should see "Question cannot be empty" printed once
            // We can capture System.out or trust that message is printed.
            // Usually verifying the logic is enough.

            // 2) The second question is "some question", which is added
            assertEquals("Should have 1 interview assignment after valid question.", 1,
                    application.getInterviewAssignments().size());

            Assignment createdInterview = application.getInterviewAssignments().get(0);
            assertEquals("The first question in the interview should match user input.", "some question",
                    createdInterview.getQuestions().get(0));

            // The assignment name is "Interview (application.getAssignments().size() + 1)",
            // i.e. "Interview 2" in this scenario because there's already 1 assignment.
            assertEquals("Interview 2", createdInterview.getAssignmentName());

            // 3) Should call "viewSpecificApplication(...)" after finishing
            verify(spyService, times(1)).viewSpecificApplication("app123");
        }
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 43
     */
    @Test
    public void testSendInterview_multipleQuestionsAndExit() {
        // Scenario: user enters 2 valid questions, chooses "y" after the first
        // question,
        // then "n" after the second question, then we finalize.

        RecruiterService spyService = Mockito.spy(recruiterService);

        Application application = new Application();
        application.setId("app999");
        application.setApplicantId("applicant999");
        application.setAssignments(new ArrayList<Assignment>());
        application.setInterviewAssignments(new ArrayList<Assignment>());

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            // 1st question: "What's your name?"
            // user chooses "y" => another question
            // 2nd question: "What's your experience?"
            // user chooses "n" => break
            mockedUtility
                    .when(() -> Utility.inputOutput("Enter the question..(type 'exit' to go back)"))
                    .thenReturn("What's your name?")
                    .thenReturn("What's your experience?");

            mockedUtility
                    .when(() -> Utility.inputOutput("Do you want to add more questions? (y/n)"))
                    .thenReturn("y")
                    .thenReturn("n");

            // After finishing, print "Interview questions sent successfully",
            // then Utility.inputOutput("Press enter to go back.");
            mockedUtility
                    .when(() -> Utility.inputOutput("Press enter to go back."))
                    .thenReturn("");
            doNothing().when(spyService).viewSpecificApplication("app999");

            // Act
            spyService.sendInterview(application);

            // Assert:
            // We expect 1 new interview assignment
            assertEquals(1, application.getInterviewAssignments().size());
            Assignment newInterview = application.getInterviewAssignments().get(0);

            ArrayList<String> questions = newInterview.getQuestions();
            assertEquals(2, questions.size());
            assertEquals("What's your name?", questions.get(0));
            assertEquals("What's your experience?", questions.get(1));

            // Because application.getAssignments() is empty => interview title is
            // "Interview 1"
            assertEquals("Interview 1", newInterview.getAssignmentName());

            // Finally, check we eventually call "viewSpecificApplication(app999)"
            verify(spyService, times(1)).viewSpecificApplication("app999");
        }
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 43
     */
    @Test
    public void testSendInterview_userTypesExitAfterSomeQuestions() {
        // Another scenario: user enters 1 valid question, then types "exit"
        // => We expect we call viewSpecificApplication(...) and do NOT add the
        // interview
        // Actually, note the code: if the user types "exit" at the question prompt,
        // it immediately returns. No assignment is created.

        RecruiterService spyService = Mockito.spy(recruiterService);

        Application application = new Application();
        application.setId("appX");
        application.setApplicantId("applicantX");
        application.setAssignments(new ArrayList<Assignment>());
        application.setInterviewAssignments(new ArrayList<Assignment>());

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {

            // The code is a while(true). If the user typed "exit" at ANY point
            // in the question prompt, we do "viewSpecificApplication(application.getId());
            // return;"
            // Let's simulate 1 valid question, then user says "y" to add more,
            // then next question is "exit".
            mockedUtility
                    .when(() -> Utility.inputOutput("Enter the question..(type 'exit' to go back)"))
                    .thenReturn("First question")
                    .thenReturn("exit");

            // After "First question", we ask "Do you want to add more questions? (y/n)"
            mockedUtility
                    .when(() -> Utility.inputOutput("Do you want to add more questions? (y/n)"))
                    .thenReturn("y");

            doNothing().when(spyService).viewSpecificApplication("appX");

            // Act
            spyService.sendInterview(application);

            // The user typed "exit" => "return" from the method
            // => no assignment is created
            assertTrue(application.getInterviewAssignments().isEmpty());

            // Should have called viewSpecificApplication(appX)
            verify(spyService, times(1)).viewSpecificApplication("appX");
        }
    }

}

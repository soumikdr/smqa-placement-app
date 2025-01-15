package test.whitebox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import model.Application;
import model.ApplicationStatus;
import model.User;
import model.UserRole;
import model.Assignment;
import model.Job;
import model.JobStatus;
import service.RecruiterService;
import utility.Utility;


public class RecruiterServiceBranchTest {
    private ArrayList<Application> mockApplications;
    private ArrayList<User> mockUsers;
    private ArrayList<Job> mockJobs;
    private Application mockApplication;
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
    public void testUpdateRecruiterProfile_EmptyNameAndLastname() {
        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            // Arrange - user "John" "Doe" with username "johnDoe" provided
            mockedUtility.when(Utility::getCurrentUser).thenReturn(mockUsers.get(0));
            mockedUtility.when(() -> Utility.getUsers()).thenReturn(mockUsers);
            RecruiterService recruiterService = Mockito.spy(new RecruiterService());
            mockedUtility.when(() -> Utility.inputOutput(anyString())).thenReturn("", "", "jack");
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

            // jack is unique username, johnDoe is non-unique username. So, first invalid, then valid input provided
            mockedUtility.when(() -> Utility.inputOutput(anyString())).thenReturn("Jack", "Dew", "johnDoe", "jack");
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
            
            recruiterService.viewSpecificApplication("1");
            
            verify(recruiterService, times(2)).viewSpecificApplication("1");
        }
    }

    @Test
    public void testSendAssignment_ValidRole() {
        try (MockedStatic<Utility> utilities = mockStatic(Utility.class)) {
            // Mock dependencies
            Map<String, List<String>> questionMap = new HashMap<>();
            questionMap.put("frontend", Arrays.asList("Q1", "Q2"));
            utilities.when(Utility::getQuestionMap).thenReturn(questionMap);
            utilities.when(() -> Utility.inputOutput(anyString())).thenReturn("frontend");
            // Clear assignments if any
            mockApplication.setAssignments(new ArrayList<Assignment>());

            // Invoke the method
            RecruiterService recruiterService = Mockito.spy(new RecruiterService());
            recruiterService.sendAssignment(mockApplication);

            // Assertions
            assertEquals(1, mockApplication.getAssignments().size());
            Assignment assignment = mockApplication.getAssignments().get(0);
            assertEquals("Assignment frontend", assignment.getAssignmentName());
            assertEquals(Arrays.asList("Q1", "Q2"), assignment.getQuestions());
            assertEquals("1", assignment.getApplicantId());

            // Verify interaction with mocked methods
            utilities.verify(Utility::getQuestionMap);
            utilities.verify(() -> Utility.inputOutput(anyString()));
        }
    }

    @Test
    public void testSendAssignment_InvalidRole() {
        try (MockedStatic<Utility> utilities = mockStatic(Utility.class)) {
            // Arrange and mock dependencies
            Map<String, List<String>> questionMap = new HashMap<>();
            questionMap.put("frontend", Arrays.asList("Q1", "Q2"));
            utilities.when(Utility::getQuestionMap).thenReturn(questionMap);
            utilities.when(() -> Utility.inputOutput(anyString())).thenReturn("invalid_role");
            // Clear assignments if any
            mockApplication.setAssignments(new ArrayList<Assignment>());

            // Invoke the method
            RecruiterService recruiterService = Mockito.spy(new RecruiterService());
            recruiterService.sendAssignment(mockApplication);

            // Assertions
            assertEquals("0", Integer.toString(mockApplication.getAssignments().size()));

            // Verify interaction with mocked methods
            utilities.verify(Utility::getQuestionMap);
            utilities.verify(() -> Utility.inputOutput(anyString()));
        }
    }

    @Test
    public void testSendAssignment_NullRole() {
        try (MockedStatic<Utility> utilities = mockStatic(Utility.class)) {
            // Mock dependencies
            Map<String, List<String>> questionMap = new HashMap<>();
            questionMap.put("frontend", Arrays.asList("Q1", "Q2"));
            utilities.when(Utility::getQuestionMap).thenReturn(questionMap);
            utilities.when(() -> Utility.inputOutput(anyString())).thenReturn(null);
            // Clear assignments if any
            mockApplication.setAssignments(new ArrayList<Assignment>());

            // Invoke the method
            RecruiterService recruiterService = Mockito.spy(new RecruiterService());
            recruiterService.sendAssignment(mockApplication);

            // Assertions
            assertEquals("0", Integer.toString(mockApplication.getAssignments().size()));

            // Verify interaction with mocked methods
            utilities.verify(Utility::getQuestionMap);
            utilities.verify(() -> Utility.inputOutput(anyString()));
        }
    }

    @Test
    public void testSendAssignment_NoQuestionsInMap() {
        try (MockedStatic<Utility> utilities = mockStatic(Utility.class)) {
            // Mock dependencies
            Map<String, List<String>> questionMap = new HashMap<>();
            utilities.when(Utility::getQuestionMap).thenReturn(questionMap);
            utilities.when(() -> Utility.inputOutput(anyString())).thenReturn("frontend");
            // Clear assignments if any
            mockApplication.setAssignments(new ArrayList<Assignment>());

            // Invoke the method
            RecruiterService recruiterService = Mockito.spy(new RecruiterService());
            recruiterService.sendAssignment(mockApplication);

            // Assertions
            assertEquals("0", Integer.toString(mockApplication.getAssignments().size()));

            // Verify interaction with mocked methods
            utilities.verify(Utility::getQuestionMap);
            utilities.verify(() -> Utility.inputOutput(anyString()));
        }
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
            utilities.when(() -> Utility.inputOutput(anyString())).thenReturn("Updated Job Title").thenReturn("Updated job description");
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
            verify(recruiterService, times(1)).updateDescriptionOfJobPost("864651896461651");
        }
    }

    @Test
    public void testUpdateDescriptionOfJobPost_NewTitleEmpty() {
        try (MockedStatic<Utility> utilities = mockStatic(Utility.class)) {
            // Arrange
            utilities.when(Utility::getJobs).thenReturn(mockJobs);
            utilities.when(() -> Utility.inputOutput(anyString())).thenReturn("").thenReturn("Updated job description");
            RecruiterService recruiterService = Mockito.spy(new RecruiterService());
            doNothing().when(recruiterService).viewAvailableJobs();

            // Act
            recruiterService.updateDescriptionOfJobPost("1");

            // Assert - Verify "Job title not updated as it is empty"
            assertEquals(mockJobs.get(0).getJobName(), Utility.getJobs().get(0).getJobName());
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
            assertEquals(mockJobs.get(0).getJobDescription(), Utility.getJobs().get(0).getJobDescription());
        }
    }

    @Test
    public void testUpdateDescriptionOfJobPost_ValidJobUpdate() {
        try (MockedStatic<Utility> utilities = mockStatic(Utility.class)) {
            // Arrange
            // Initial Jobs have different job name and description
            utilities.when(Utility::getJobs).thenReturn(mockJobs);
            utilities.when(() -> Utility.inputOutput(anyString())).thenReturn("Updated Job Title").thenReturn("Updated job description");
            RecruiterService recruiterService = Mockito.spy(new RecruiterService());
            doNothing().when(recruiterService).viewAvailableJobs();

            // Act
            recruiterService.updateDescriptionOfJobPost("1");

            // Assert - Verify "Job title and description updated"
            assertEquals("Updated Job Title", Utility.getJobs().get(0).getJobName());
            assertEquals("Updated job description", Utility.getJobs().get(0).getJobDescription());
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
            assertEquals("Valid feedback", Utility.getApplications().get(0).getFeedback());
        }
    }

    @Test
    public void testViewSpecificJobPost_Case1() throws IOException {
        RecruiterService recruiterService = new RecruiterService();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        RecruiterService recruiterSpyObject = Mockito.spy(recruiterService);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {

            ArrayList<Job> mockJobs = new ArrayList<>();
            mockJobs.add(new Job("Job1", "HR", "Manage resources", JobStatus.PRIVATE));
            mockJobs.add(new Job("Job2", "Manager", "Manage team", JobStatus.PUBLIC));

            mockedUtility.when(Utility::getJobs).thenReturn(mockJobs);

            Mockito.doNothing().when(recruiterSpyObject).updateDescriptionOfJobPost(Mockito.anyString());
            Mockito.doNothing().when(recruiterSpyObject).updateStatusOfJobPost(Mockito.anyString());
            Mockito.doNothing().when(recruiterSpyObject).viewTotalNumberOfApplications(Mockito.anyString());
            Mockito.doNothing().when(recruiterSpyObject).viewRecruiterDashboard();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("Job1", "1", "2");
            recruiterSpyObject.viewSpecificJobPost();
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Job ID: Job1"));
            Assert.assertTrue(consoleOutput.contains("Job Name: HR"));
            Assert.assertTrue(consoleOutput.contains("Redirecting to update job description page"));
            Mockito.verify(recruiterSpyObject, Mockito.times(1)).updateDescriptionOfJobPost("Job1");
            outputStream.reset();
        }
    }

    @Test
    public void testViewSpecificJobPost_Case2() throws IOException {
        RecruiterService recruiterService = new RecruiterService();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        RecruiterService recruiterSpyObject = Mockito.spy(recruiterService);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {

            ArrayList<Job> mockJobs = new ArrayList<>();
            mockJobs.add(new Job("Job1", "HR", "Manage resources", JobStatus.PRIVATE));
            mockJobs.add(new Job("Job2", "Manager", "Manage team", JobStatus.PUBLIC));

            mockedUtility.when(Utility::getJobs).thenReturn(mockJobs);

            Mockito.doNothing().when(recruiterSpyObject).updateDescriptionOfJobPost(Mockito.anyString());
            Mockito.doNothing().when(recruiterSpyObject).updateStatusOfJobPost(Mockito.anyString());
            Mockito.doNothing().when(recruiterSpyObject).viewTotalNumberOfApplications(Mockito.anyString());
            Mockito.doNothing().when(recruiterSpyObject).viewRecruiterDashboard();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("Job2", "2");
            recruiterSpyObject.viewSpecificJobPost();
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Job ID: Job2"));
            Assert.assertTrue(consoleOutput.contains("Job Name: Manager"));
            Assert.assertTrue(consoleOutput.contains("Redirecting to update job status page"));
            Mockito.verify(recruiterSpyObject, Mockito.times(1)).updateStatusOfJobPost("Job2");
            outputStream.reset();
        }
    }

    @Test
    public void testViewSpecificJobPost_Case3() throws IOException {
        RecruiterService recruiterService = new RecruiterService();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        RecruiterService recruiterSpyObject = Mockito.spy(recruiterService);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {

            ArrayList<Job> mockJobs = new ArrayList<>();
            mockJobs.add(new Job("Job1", "HR", "Manage resources", JobStatus.PRIVATE));
            mockJobs.add(new Job("Job2", "Manager", "Manage team", JobStatus.PUBLIC));

            mockedUtility.when(Utility::getJobs).thenReturn(mockJobs);

            Mockito.doNothing().when(recruiterSpyObject).updateDescriptionOfJobPost(Mockito.anyString());
            Mockito.doNothing().when(recruiterSpyObject).updateStatusOfJobPost(Mockito.anyString());
            Mockito.doNothing().when(recruiterSpyObject).viewTotalNumberOfApplications(Mockito.anyString());
            Mockito.doNothing().when(recruiterSpyObject).viewRecruiterDashboard();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("Job2", "3");
            recruiterSpyObject.viewSpecificJobPost();
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Redirecting to total applications for the job"));
            Mockito.verify(recruiterSpyObject, Mockito.times(1)).viewTotalNumberOfApplications("Job2");
            outputStream.reset();
        }
    }

    @Test
    public void testViewSpecificJobPost_Case4() throws IOException {
        RecruiterService recruiterService = new RecruiterService();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        RecruiterService recruiterSpyObject = Mockito.spy(recruiterService);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {

            ArrayList<Job> mockJobs = new ArrayList<>();
            mockJobs.add(new Job("Job1", "HR", "Manage resources", JobStatus.PRIVATE));
            mockJobs.add(new Job("Job2", "Manager", "Manage team", JobStatus.PUBLIC));

            mockedUtility.when(Utility::getJobs).thenReturn(mockJobs);

            Mockito.doNothing().when(recruiterSpyObject).updateDescriptionOfJobPost(Mockito.anyString());
            Mockito.doNothing().when(recruiterSpyObject).updateStatusOfJobPost(Mockito.anyString());
            Mockito.doNothing().when(recruiterSpyObject).viewTotalNumberOfApplications(Mockito.anyString());
            Mockito.doNothing().when(recruiterSpyObject).viewRecruiterDashboard();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("Job2", "4");
            recruiterSpyObject.viewSpecificJobPost();
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Redirecting to main menu"));
            Mockito.verify(recruiterSpyObject, Mockito.times(2)).viewRecruiterDashboard();
            outputStream.reset();
        }
    }

    @Test
    public void testViewSpecificJobPost_Default() throws IOException {
        RecruiterService recruiterService = new RecruiterService();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        RecruiterService recruiterSpyObject = Mockito.spy(recruiterService);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {

            ArrayList<Job> mockJobs = new ArrayList<>();
            mockJobs.add(new Job("Job1", "HR", "Manage resources", JobStatus.PRIVATE));
            mockJobs.add(new Job("Job2", "Manager", "Manage team", JobStatus.PUBLIC));

            mockedUtility.when(Utility::getJobs).thenReturn(mockJobs);

            Mockito.doNothing().when(recruiterSpyObject).updateDescriptionOfJobPost(Mockito.anyString());
            Mockito.doNothing().when(recruiterSpyObject).updateStatusOfJobPost(Mockito.anyString());
            Mockito.doNothing().when(recruiterSpyObject).viewTotalNumberOfApplications(Mockito.anyString());
            Mockito.doNothing().when(recruiterSpyObject).viewRecruiterDashboard();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("Job2", "invalid");
            recruiterSpyObject.viewSpecificJobPost();
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("You entered invalid option"));
            Mockito.verify(recruiterSpyObject, Mockito.times(2)).viewRecruiterDashboard();
            outputStream.reset();
        }
    }

    @Test
    public void testViewSpecificJobPost_ViewAnotherJob() throws IOException {
        RecruiterService recruiterService = new RecruiterService();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        RecruiterService recruiterSpyObject = Mockito.spy(recruiterService);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {

            ArrayList<Job> mockJobs = new ArrayList<>();
            mockJobs.add(new Job("Job1", "HR", "Manage resources", JobStatus.PRIVATE));
            mockJobs.add(new Job("Job2", "Manager", "Manage team", JobStatus.PUBLIC));

            mockedUtility.when(Utility::getJobs).thenReturn(mockJobs);

            Mockito.doNothing().when(recruiterSpyObject).updateDescriptionOfJobPost(Mockito.anyString());
            Mockito.doNothing().when(recruiterSpyObject).updateStatusOfJobPost(Mockito.anyString());
            Mockito.doNothing().when(recruiterSpyObject).viewTotalNumberOfApplications(Mockito.anyString());
            Mockito.doNothing().when(recruiterSpyObject).viewRecruiterDashboard();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("Job2", "2", "1", "Job2");
            recruiterSpyObject.viewSpecificJobPost();
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Job ID: Job2"));
            Assert.assertTrue(consoleOutput.contains("Job Name: Manager"));
            Assert.assertTrue(consoleOutput.contains("Redirecting to update job status page"));
            Assert.assertTrue(consoleOutput.contains("Redirecting to view specific job details"));
            Mockito.verify(recruiterSpyObject, Mockito.times(1)).updateStatusOfJobPost("Job2");
            outputStream.reset();
        }
    }

    @Test
    public void viewRecruiterProfilePageTest_Case1() throws IOException {
        RecruiterService recruiterService = new RecruiterService();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        RecruiterService recruiterSpyObject = Mockito.spy(recruiterService);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {

            Mockito.doNothing().when(recruiterSpyObject).updateRecruiterProfile();
            Mockito.doNothing().when(recruiterSpyObject).deleteRecruiterProfile();
            Mockito.doNothing().when(recruiterSpyObject).viewRecruiterDashboard();

            User mockUser = new User("User1", "Ansar", "Patil", "ansarpatil", "123qwe", UserRole.RECRUITER);
            mockedUtility.when(Utility::getCurrentUser).thenReturn(mockUser);

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("1");
            recruiterSpyObject.viewRecruiterProfilePage();
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Redirecting to update profile page..."));
            outputStream.reset();
            Mockito.verify(recruiterSpyObject, Mockito.atLeastOnce()).viewRecruiterProfilePage();
            mockedUtility.verify(Mockito.atLeastOnce(), () -> Utility.inputOutput(Mockito.anyString()));
        }

    }

    @Test
    public void viewRecruiterProfilePageTest_Case2() throws IOException {
        RecruiterService recruiterService = new RecruiterService();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        RecruiterService recruiterSpyObject = Mockito.spy(recruiterService);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {

            Mockito.doNothing().when(recruiterSpyObject).updateRecruiterProfile();
            Mockito.doNothing().when(recruiterSpyObject).deleteRecruiterProfile();
            Mockito.doNothing().when(recruiterSpyObject).viewRecruiterDashboard();

            User mockUser = new User("User1", "Ansar", "Patil", "ansarpatil", "123qwe", UserRole.RECRUITER);
            mockedUtility.when(Utility::getCurrentUser).thenReturn(mockUser);

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("2");
            recruiterSpyObject.viewRecruiterProfilePage();
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Redirecting to delete profile page..."));
            outputStream.reset();
            Mockito.verify(recruiterSpyObject, Mockito.atLeastOnce()).viewRecruiterProfilePage();
            mockedUtility.verify(Mockito.atLeastOnce(), () -> Utility.inputOutput(Mockito.anyString()));
        }

    }

    @Test
    public void viewRecruiterProfilePageTest_Case3() throws IOException {
        RecruiterService recruiterService = new RecruiterService();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        RecruiterService recruiterSpyObject = Mockito.spy(recruiterService);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {

            Mockito.doNothing().when(recruiterSpyObject).updateRecruiterProfile();
            Mockito.doNothing().when(recruiterSpyObject).deleteRecruiterProfile();
            Mockito.doNothing().when(recruiterSpyObject).viewRecruiterDashboard();

            User mockUser = new User("User1", "Ansar", "Patil", "ansarpatil", "123qwe", UserRole.RECRUITER);
            mockedUtility.when(Utility::getCurrentUser).thenReturn(mockUser);

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("3");
            recruiterSpyObject.viewRecruiterProfilePage();
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Redirecting to dashboard..."));
            outputStream.reset();
            Mockito.verify(recruiterSpyObject, Mockito.atLeastOnce()).viewRecruiterProfilePage();
            mockedUtility.verify(Mockito.atLeastOnce(), () -> Utility.inputOutput(Mockito.anyString()));
        }

    }

    @Test
    public void viewRecruiterProfilePageTest_Default() throws IOException {
        RecruiterService recruiterService = new RecruiterService();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        RecruiterService recruiterSpyObject = Mockito.spy(recruiterService);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {

            Mockito.doNothing().when(recruiterSpyObject).updateRecruiterProfile();
            Mockito.doNothing().when(recruiterSpyObject).deleteRecruiterProfile();
            Mockito.doNothing().when(recruiterSpyObject).viewRecruiterDashboard();

            User mockUser = new User("User1", "Ansar", "Patil", "ansarpatil", "123qwe", UserRole.RECRUITER);
            mockedUtility.when(Utility::getCurrentUser).thenReturn(mockUser);

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("invalid", "1");
            recruiterSpyObject.viewRecruiterProfilePage();
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("You entered invalid option"));
            outputStream.reset();
            Mockito.verify(recruiterSpyObject, Mockito.atLeastOnce()).viewRecruiterProfilePage();
            mockedUtility.verify(Mockito.atLeastOnce(), () -> Utility.inputOutput(Mockito.anyString()));
        }

    }

    @Test
    public void testViewSubmittedAnswers_AssignmentWithAnswers() {
        Application mockApplication = mock(Application.class);
        RecruiterService service = new RecruiterService();
        RecruiterService spyObject = Mockito.spy(service);
        Mockito.doNothing().when(spyObject).viewSpecificApplication(Mockito.anyString());

        ArrayList<String> questions = new ArrayList<>();
        questions.add("What is Java?");
        questions.add("What is OOP?");
        
        ArrayList<String> answers = new ArrayList<>();
        answers.add("Java is a programming language.");
        answers.add("OOP stands for Object-Oriented Programming.");
        
        Assignment mockAssignment = new Assignment("A1", "U101", "Assessment 1", questions, answers);
        ArrayList<Assignment> mockAssignments = new ArrayList<>();
        mockAssignments.add(mockAssignment);

        when(mockApplication.getAssignments()).thenReturn(mockAssignments);
        when(mockApplication.getId()).thenReturn("A101");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        spyObject.viewSubmittedAnswers(mockApplication);
        String consoleOutput = outputStream.toString();
        Assert.assertTrue(consoleOutput.contains("Welcome to view submitted answers for the application A101"));
        Assert.assertTrue(consoleOutput.contains("Assignment: Assessment 1"));
        Assert.assertTrue(consoleOutput.contains("Question: "));
        Assert.assertTrue(consoleOutput.contains("What is Java?"));
        Assert.assertTrue(consoleOutput.contains("Answer:"));
        Assert.assertTrue(consoleOutput.contains("Java is a programming language."));
        Assert.assertTrue(consoleOutput.contains("Question: "));
        Assert.assertTrue(consoleOutput.contains("What is OOP?"));
        Assert.assertTrue(consoleOutput.contains("Answer:"));
        Assert.assertTrue(consoleOutput.contains("OOP stands for Object-Oriented Programming."));
        Assert.assertTrue(consoleOutput.contains("Submitted Answers for interviews:"));
        outputStream.reset();
    }

    @Test
    public void testViewSubmittedAnswers_AssignmentWithoutAnswers() {
        Application mockApplication = mock(Application.class);
        RecruiterService service = new RecruiterService();
        RecruiterService spyObject = Mockito.spy(service);
        Mockito.doNothing().when(spyObject).viewSpecificApplication(Mockito.anyString());

        ArrayList<String> questions = new ArrayList<>();
        questions.add("What is Java?");
        questions.add("What is OOP?");
        
        Assignment mockAssignment = new Assignment("A2", "U102", "Assessment 2", questions, new ArrayList<>());
        ArrayList<Assignment> mockAssignments = new ArrayList<>();
        mockAssignments.add(mockAssignment);

        when(mockApplication.getId()).thenReturn("A101");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        when(mockApplication.getAssignments()).thenReturn(mockAssignments);

        spyObject.viewSubmittedAnswers(mockApplication);
        String consoleOutput = outputStream.toString();
        Assert.assertTrue(consoleOutput.contains("Assignment: Assessment 2"));
        Assert.assertTrue(consoleOutput.contains("No answer submitted yet"));
        Assert.assertTrue(consoleOutput.contains("No answer submitted yet"));
        outputStream.reset();
    }

    @Test
    public void testViewSubmittedAnswers_EmptyAssignment() {
        Application mockApplication = mock(Application.class);
        RecruiterService service = new RecruiterService();
        RecruiterService spyObject = Mockito.spy(service);
        Mockito.doNothing().when(spyObject).viewSpecificApplication(Mockito.anyString());

        ArrayList<Assignment> mockAssignments = new ArrayList<>();

        when(mockApplication.getId()).thenReturn("A101");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        when(mockApplication.getAssignments()).thenReturn(mockAssignments);

        spyObject.viewSubmittedAnswers(mockApplication);
        String consoleOutput = outputStream.toString();
        Assert.assertTrue(consoleOutput.contains("Submitted Answers for assessments:"));
        Assert.assertTrue(consoleOutput.contains("Submitted Answers for interviews:"));
        outputStream.reset();
    }

    @Test
    public void testViewSubmittedAnswers_InterviewWithoutAnswers() {
        Application mockApplication = mock(Application.class);
        RecruiterService service = new RecruiterService();
        RecruiterService spyObject = Mockito.spy(service);
        Mockito.doNothing().when(spyObject).viewSpecificApplication(Mockito.anyString());

        ArrayList<String> interviewQuestions = new ArrayList<>();
        interviewQuestions.add("What are your strengths?");
        interviewQuestions.add("Why do you want this job?");
        ArrayList<Assignment> mockInterviewAssignments = new ArrayList<>();
        mockInterviewAssignments.add(new Assignment("I1", "U101", "Interview", interviewQuestions, new ArrayList<>()));
        
        when(mockApplication.getId()).thenReturn("A101");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        when(mockApplication.getInterviewAssignments()).thenReturn(mockInterviewAssignments);

        spyObject.viewSubmittedAnswers(mockApplication);
        String consoleOutput = outputStream.toString();
        Assert.assertTrue(consoleOutput.contains("Interview: Interview"));
        Assert.assertTrue(consoleOutput.contains("Question: "));
        Assert.assertTrue(consoleOutput.contains("What are your strengths?"));
        Assert.assertTrue(consoleOutput.contains("No answer submitted yet"));
        Assert.assertTrue(consoleOutput.contains("Question: "));
        Assert.assertTrue(consoleOutput.contains("Why do you want this job?"));
        Assert.assertTrue(consoleOutput.contains("No answer submitted yet"));
    }

    @Test
    public void testViewSubmittedAnswers_InterviewWithAnswers() {
        Application mockApplication = mock(Application.class);
        RecruiterService service = new RecruiterService();
        RecruiterService spyObject = Mockito.spy(service);
        Mockito.doNothing().when(spyObject).viewSpecificApplication(Mockito.anyString());

        ArrayList<String> interviewQuestions = new ArrayList<>();
        interviewQuestions.add("What are your strengths?");
        interviewQuestions.add("Why do you want this job?");

        ArrayList<String> interviewAnswers = new ArrayList<>();
        interviewAnswers.add("I can manage time very well");
        interviewAnswers.add("To achieve my goals");
        ArrayList<Assignment> mockInterviewAssignmentsWithAnswers = new ArrayList<>();
        mockInterviewAssignmentsWithAnswers.add(new Assignment("I1", "U101", "Interview", interviewQuestions, interviewAnswers));
        
        when(mockApplication.getId()).thenReturn("A101");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        when(mockApplication.getInterviewAssignments()).thenReturn(mockInterviewAssignmentsWithAnswers);
 
         spyObject.viewSubmittedAnswers(mockApplication); 
         String consoleOutput = outputStream.toString();
         Assert.assertTrue(consoleOutput.contains("Interview: Interview"));
         Assert.assertTrue(consoleOutput.contains("Question: "));
         Assert.assertTrue(consoleOutput.contains("What are your strengths?"));
         Assert.assertTrue(consoleOutput.contains("Answer:"));
         Assert.assertTrue(consoleOutput.contains("I can manage time very well"));
         Assert.assertTrue(consoleOutput.contains("Question: "));
         Assert.assertTrue(consoleOutput.contains("Why do you want this job?"));
         Assert.assertTrue(consoleOutput.contains("Answer:"));
         Assert.assertTrue(consoleOutput.contains("To achieve my goals"));
    }
}

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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            verify(recruiterService,
                    times(1)).approveRejectApplication(mockApplications.get(0));
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
            utilities.when(() -> Utility.inputOutput(anyString())).thenReturn("").thenReturn("Updated job description");
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

                    verify(mockCommonService, times(1)).accessLandingPage();

                    mockedUtility.verify(never(), () -> Utility.setCurrentUser(any()));

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

        RecruiterService spyService = Mockito.spy(recruiterService);
        User user = new Recruiter("1", "John", "Doe", "johnDoe", "bestpassword");

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {

            mockedUtility
                    .when(() -> Utility.getCurrentUser())
                    .thenReturn(user);

            mockedUtility
                    .when(() -> Utility.inputOutput("Do you want to reset your password? (y/n)"))
                    .thenReturn("y");

            mockedUtility
                    .when(() -> Utility.inputOutput("Enter your User name"))
                    .thenReturn("johnDoe");

            spyService.viewResetPasswordPage();

            verify(spyService, times(1)).resetPasswordRecruiter("johnDoe");

            verify(spyService, never()).viewRecruiterDashboard();
        }
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 11
     */
    @Test
    public void testViewResetPasswordPage_userChoosesNo() {

        RecruiterService spyService = Mockito.spy(recruiterService);
        User user = new Recruiter("1", "John", "Doe", "johnDoe", "bestpassword");

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            mockedUtility
                    .when(() -> Utility.getCurrentUser())
                    .thenReturn(user);

            mockedUtility
                    .when(() -> Utility.inputOutput("Do you want to reset your password? (y/n)"))
                    .thenReturn("n");

            doNothing().when(spyService).viewRecruiterDashboard();

            spyService.viewResetPasswordPage();

            verify(spyService, never()).resetPasswordRecruiter(anyString());

            verify(spyService, times(1)).viewRecruiterDashboard();
        }
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 11
     */
    @Test
    public void testViewResetPasswordPage_userChoosesSomethingElse() {

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
        System.setOut(new PrintStream(outputStream));
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

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        RecruiterService spyService = Mockito.spy(recruiterService);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {

            mockedUtility.when(Utility::getJobs).thenReturn(new ArrayList<>());
            doNothing().when(spyService).viewRecruiterDashboard();

            spyService.viewAvailableJobs();

            String consoleOutput = outContent.toString();
            assertTrue(consoleOutput.contains("No jobs available"));

            verify(spyService, times(1)).viewRecruiterDashboard();
        } finally {

            System.setOut(originalOut);
        }
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 24
     */
    @Test
    public void testViewAvailableJobs_jobsAvailable_userChooses1() {

        ArrayList<Job> jobs = new ArrayList<>();
        Job job1 = new Job("id1", "Title1", "Desc1", JobStatus.PUBLIC);
        Job job2 = new Job("id2", "Title2", "Desc2", JobStatus.PUBLIC);
        jobs.add(job1);
        jobs.add(job2);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        RecruiterService spyService = Mockito.spy(recruiterService);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            mockedUtility.when(Utility::getJobs).thenReturn(jobs);

            mockedUtility
                    .when(() -> Utility.inputOutput("Please select one of the options.."))
                    .thenReturn("1");
            doNothing().when(spyService).viewSpecificJobPost();

            spyService.viewAvailableJobs();

            String consoleOutput = outContent.toString();
            assertTrue(consoleOutput.contains("Available Jobs"));
            assertTrue(consoleOutput.contains("Job ID: id1"));
            assertTrue(consoleOutput.contains("Job ID: id2"));

            verify(spyService, times(1)).viewSpecificJobPost();

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

        ArrayList<Job> jobs = new ArrayList<>();
        jobs.add(new Job("id1", "Title1", "Desc1", JobStatus.PUBLIC));

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        RecruiterService spyService = Mockito.spy(recruiterService);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            mockedUtility.when(Utility::getJobs).thenReturn(jobs);

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

        ArrayList<Job> jobs = new ArrayList<>();
        jobs.add(new Job("idX", "TitleX", "DescX", JobStatus.PUBLIC));

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        RecruiterService spyService = Mockito.spy(recruiterService);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            mockedUtility.when(Utility::getJobs).thenReturn(jobs);

            mockedUtility
                    .when(() -> Utility.inputOutput("Please select one of the options.."))
                    .thenReturn("abc")
                    .thenReturn("2");

            doNothing().when(spyService).viewRecruiterDashboard();

            spyService.viewAvailableJobs();

            String output = outContent.toString();

            assertTrue(output.contains("abc is not a valid option. Please try again."));

            verify(spyService, times(1)).viewRecruiterDashboard();

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

        RecruiterService spyService = Mockito.spy(recruiterService);

        Application application = new Application();
        application.setId("app123");
        application.setApplicantId("applicant123");
        application.setAssignments(new ArrayList<Assignment>());
        application.setInterviewAssignments(new ArrayList<Assignment>());

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {

            mockedUtility
                    .when(() -> Utility.inputOutput("Enter the question..(type 'exit' to go back)"))
                    .thenReturn("exit");
            doNothing().when(spyService).viewSpecificApplication("app123");

            spyService.sendInterview(application);

            verify(spyService, times(1)).viewSpecificApplication("app123");

            assertTrue(application.getInterviewAssignments().isEmpty());
        }
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 43
     */
    @Test
    public void testSendInterview_userEntersEmptyQuestionThenValidQuestionThenNo() {

        RecruiterService spyService = Mockito.spy(recruiterService);

        Application application = new Application();
        application.setId("app123");
        application.setApplicantId("applicant123");

        ArrayList<Assignment> existingAssignments = new ArrayList<>();
        existingAssignments.add(new Assignment("a1", "applicant123", "FirstAssignment", null, null));
        application.setAssignments(existingAssignments);

        application.setInterviewAssignments(new ArrayList<Assignment>());

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {

            mockedUtility
                    .when(() -> Utility.inputOutput("Enter the question..(type 'exit' to go back)"))
                    .thenReturn("")
                    .thenReturn("some question");

            mockedUtility
                    .when(() -> Utility.inputOutput("Do you want to add more questions? (y/n)"))
                    .thenReturn("n");

            mockedUtility
                    .when(() -> Utility.inputOutput("Press enter to go back."))
                    .thenReturn("");

            doNothing().when(spyService).viewSpecificApplication("app123");

            spyService.sendInterview(application);

            assertEquals("Should have 1 interview assignment after valid question.", 1,
                    application.getInterviewAssignments().size());

            Assignment createdInterview = application.getInterviewAssignments().get(0);
            assertEquals("The first question in the interview should match user input.", "some question",
                    createdInterview.getQuestions().get(0));

            assertEquals("Interview 2", createdInterview.getAssignmentName());

            verify(spyService, times(1)).viewSpecificApplication("app123");
        }
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 43
     */
    @Test
    public void testSendInterview_multipleQuestionsAndExit() {

        RecruiterService spyService = Mockito.spy(recruiterService);

        Application application = new Application();
        application.setId("app999");
        application.setApplicantId("applicant999");
        application.setAssignments(new ArrayList<Assignment>());
        application.setInterviewAssignments(new ArrayList<Assignment>());

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {

            mockedUtility
                    .when(() -> Utility.inputOutput("Enter the question..(type 'exit' to go back)"))
                    .thenReturn("What's your name?")
                    .thenReturn("What's your experience?");

            mockedUtility
                    .when(() -> Utility.inputOutput("Do you want to add more questions? (y/n)"))
                    .thenReturn("y")
                    .thenReturn("n");

            mockedUtility
                    .when(() -> Utility.inputOutput("Press enter to go back."))
                    .thenReturn("");
            doNothing().when(spyService).viewSpecificApplication("app999");

            spyService.sendInterview(application);

            assertEquals(1, application.getInterviewAssignments().size());
            Assignment newInterview = application.getInterviewAssignments().get(0);

            ArrayList<String> questions = newInterview.getQuestions();
            assertEquals(2, questions.size());
            assertEquals("What's your name?", questions.get(0));
            assertEquals("What's your experience?", questions.get(1));

            assertEquals("Interview 1", newInterview.getAssignmentName());

            verify(spyService, times(1)).viewSpecificApplication("app999");
        }
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 43
     */
    @Test
    public void testSendInterview_userTypesExitAfterSomeQuestions() {

        RecruiterService spyService = Mockito.spy(recruiterService);

        Application application = new Application();
        application.setId("appX");
        application.setApplicantId("applicantX");
        application.setAssignments(new ArrayList<Assignment>());
        application.setInterviewAssignments(new ArrayList<Assignment>());

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {

            mockedUtility
                    .when(() -> Utility.inputOutput("Enter the question..(type 'exit' to go back)"))
                    .thenReturn("First question")
                    .thenReturn("exit");

            mockedUtility
                    .when(() -> Utility.inputOutput("Do you want to add more questions? (y/n)"))
                    .thenReturn("y");

            doNothing().when(spyService).viewSpecificApplication("appX");

            spyService.sendInterview(application);

            assertTrue(application.getInterviewAssignments().isEmpty());

            verify(spyService, times(1)).viewSpecificApplication("appX");
        }
    }

}

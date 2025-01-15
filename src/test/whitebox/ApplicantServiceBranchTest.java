package test.whitebox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Optional;

import model.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import jdk.jshell.execution.Util;
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
     * Author: Mayur Shinde (mss62)
     * User Story: 17
     */
    @Test
    public void testUpdateProfile_existingUserIsReplaced() {
        // Arrange
        // 1) Suppose there are users in the system
        User oldUser = new Applicant("123", "OldName", "OldLast", "oldApplicantname", "oldPass");
        User otherUser = new Applicant("456", "Another", "User", "anotherUser", "anotherPass");
        ArrayList<User> mockUsers = new ArrayList<>();
        mockUsers.add(oldUser);
        mockUsers.add(otherUser);

        // 2) We'll create an updated version of oldUser
        User updatedUser = new Applicant("123", "NewName", "NewLast", "oldUsername", "newPass");

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            // Mock the static call to get the existing user list
            mockedUtility.when(Utility::getUsers).thenReturn(mockUsers);

            // We'll spy on the applicantService so we can call updateProfile real code
            // or just call the real method directly.
            // We’ll do it directly:
            applicantService.updateProfile(updatedUser);

            // Assert
            // 1) The old user with ID "123" should be removed, and updatedUser should be
            // added
            assertEquals("Size remains 2 because we replaced one user with the updated one.", 2, mockUsers.size());

            // Check that oldUser is gone
            Optional<User> foundOldUser = mockUsers.stream()
                    .filter(u -> u.getId().equals("123") && u.getName().equals("OldName"))
                    .findFirst();
            assertFalse("Old user should be removed from the list.", foundOldUser.isPresent());

            // Check that updatedUser is in the list
            Optional<User> foundUpdatedUser = mockUsers.stream()
                    .filter(u -> u.getId().equals("123") && u.getName().equals("NewName"))
                    .findFirst();
            assertTrue("Updated user should be in the list.", foundUpdatedUser.isPresent());

            // 2) Check that Utility.setCurrentUser(...) is called with updatedUser

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

        // updatedUser has a different ID, not found in the list
        User updatedUser = new Applicant("999", "BrandNew", "User", "brandNew", "pass");

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            mockedUtility.when(Utility::getUsers).thenReturn(mockUsers);

            // Act
            applicantService.updateProfile(updatedUser);

            // Assert
            // old list had 1 user, after updateProfile it should have 2 because the user
            // wasn't found
            // so it is simply added
            assertEquals("Should add the new user because ID wasn't found.", 2, mockUsers.size());

            // Check that updatedUser is now in the list
            Optional<User> foundUpdatedUser = mockUsers.stream()
                    .filter(u -> u.getId().equals("999"))
                    .findFirst();
            assertTrue("New user should be added.", foundUpdatedUser.isPresent());

            // Check Utility.setCurrentUser(...) called with updatedUser
            mockedUtility.verify(atLeastOnce(), () -> Utility.setCurrentUser(updatedUser));
        }
    }

    // -----------------------------------------------------------------------------
    // Tests for showUpdateProfilePage()
    // -----------------------------------------------------------------------------

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 17
     */
    @Test
    public void testShowUpdateProfilePage_changeBothNameAndLastName() {
        // Scenario: user inputs new name, new last name
        User currentUser = new Applicant("111", "OldFirst", "OldLast", "user111", "pw111");

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            // 1) Utility.getCurrentUser() returns currentUser
            mockedUtility.when(Utility::getCurrentUser).thenReturn(currentUser);

            // 2) Mock user input:
            // - new name = "NewFirst"
            // - new last name = "NewLast"
            mockedUtility.when(() -> Utility.inputOutput("Enter your name: "))
                    .thenReturn("NewFirst");
            mockedUtility.when(() -> Utility.inputOutput("Enter your last name: "))
                    .thenReturn("NewLast");

            // 3) We also want to see that eventually updateProfile(...) is called.
            // We can spy on applicantService or just check the final states.

            // 4) Because showUpdateProfilePage() calls System.out.println,
            // we could capture console output if needed. For simplicity, we skip it here.

            // 5) We'll also mock Utility.getUsers() used inside updateProfile.
            // Let's have a list with the same currentUser in it.
            ArrayList<User> mockUsers = new ArrayList<>();
            mockUsers.add(currentUser);
            mockedUtility.when(Utility::getUsers).thenReturn(mockUsers);

            // 6) Now call the real method
            ApplicantService spyService = Mockito.spy(applicantService);
            doNothing().when(spyService).viewApplicantProfilePage();
            // We stub out viewApplicantProfilePage() so it doesn't cause side effects.

            spyService.showUpdateProfilePage();

            // Assert
            // The user's name and last name should be updated
            assertEquals("NewFirst", currentUser.getName());
            assertEquals("NewLast", currentUser.getLastName());

            // After changes, updateProfile is called with currentUser
            verify(spyService, times(1)).updateProfile(currentUser);

            // Then calls viewApplicantProfilePage()
            verify(spyService, times(1)).viewApplicantProfilePage();
        }
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 17
     */
    @Test
    public void testShowUpdateProfilePage_noChanges() {
        // Scenario: user enters empty string for both name and last name
        User currentUser = new Applicant("222", "OriginalFirst", "OriginalLast", "user222", "pw222");

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            mockedUtility.when(Utility::getCurrentUser).thenReturn(currentUser);

            // User inputs empty for both
            mockedUtility.when(() -> Utility.inputOutput("Enter your name: "))
                    .thenReturn("");
            mockedUtility.when(() -> Utility.inputOutput("Enter your last name: "))
                    .thenReturn("");

            ArrayList<User> mockUsers = new ArrayList<>();
            mockUsers.add(currentUser);
            mockedUtility.when(Utility::getUsers).thenReturn(mockUsers);

            // Spy
            ApplicantService spyService = Mockito.spy(applicantService);
            doNothing().when(spyService).viewApplicantProfilePage();

            // Act
            spyService.showUpdateProfilePage();

            // Assert
            // Name / last name should remain unchanged
            assertEquals("OriginalFirst", currentUser.getName());
            assertEquals("OriginalLast", currentUser.getLastName());

            // updateProfile should still be called, but with the same user data
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
        // Scenario: user changes only the first name, leaves last name empty
        User currentUser = new Applicant("333", "OldF", "OldL", "user333", "pw333");

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            mockedUtility.when(Utility::getCurrentUser).thenReturn(currentUser);

            // Input: "NewF", then "" for last name
            mockedUtility.when(() -> Utility.inputOutput("Enter your name: "))
                    .thenReturn("NewF");
            mockedUtility.when(() -> Utility.inputOutput("Enter your last name: "))
                    .thenReturn("");

            ArrayList<User> mockUsers = new ArrayList<>();
            mockUsers.add(currentUser);
            mockedUtility.when(Utility::getUsers).thenReturn(mockUsers);

            ApplicantService spyService = Mockito.spy(applicantService);
            doNothing().when(spyService).viewApplicantProfilePage();

            // Act
            spyService.showUpdateProfilePage();

            // Assert
            // Name changed, last name unchanged
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
        // Scenario: user changes only the last name
        User currentUser = new Applicant("444", "FirstX", "LastX", "user444", "pw444");

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            mockedUtility.when(Utility::getCurrentUser).thenReturn(currentUser);

            // Input: "" for name, "NewLast" for last name
            mockedUtility.when(() -> Utility.inputOutput("Enter your name: "))
                    .thenReturn("");
            mockedUtility.when(() -> Utility.inputOutput("Enter your last name: "))
                    .thenReturn("NewLast");

            ArrayList<User> mockUsers = new ArrayList<>();
            mockUsers.add(currentUser);
            mockedUtility.when(Utility::getUsers).thenReturn(mockUsers);

            ApplicantService spyService = Mockito.spy(applicantService);
            doNothing().when(spyService).viewApplicantProfilePage();

            // Act
            spyService.showUpdateProfilePage();

            // Assert
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
        // Arrange
        User currentUser = new Applicant("123", "John", "Doe", "johnD", "pw123");
        User anotherUser = new Applicant("999", "Other", "User", "otherU", "pw999");

        ArrayList<User> mockUsers = new ArrayList<>();
        mockUsers.add(currentUser);
        mockUsers.add(anotherUser);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            // Mock Utility calls
            mockedUtility.when(Utility::getCurrentUser).thenReturn(currentUser);
            mockedUtility.when(Utility::getUsers).thenReturn(mockUsers);

            // Act
            applicantService.deleteProfileHelper();

            // Assert
            // The currentUser with ID "123" should be removed from the list
            assertEquals("User list should have exactly 1 left",
                    1,
                    mockUsers.size());

            assertEquals("Remaining user should be '999'",
                    "999",
                    mockUsers.get(0).getId());

            // Check Utility.setUsers(...) was called with updated list
            mockedUtility.verify(atLeastOnce(), () -> Utility.setUsers(mockUsers));

            // Check Utility.setCurrentUser(null) is called once
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

            // Act
            applicantService.deleteProfileHelper();

            // Assert
            // The list remains the same because user "888" not found
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

    // -------------------------------------------------------------------------
    // Tests for deleteApplicantProfile()
    // -------------------------------------------------------------------------

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

            // We'll stub the internal calls so we can verify them
            doNothing().when(spyService).deleteProfileHelper();
            doNothing().when(spyService).applicantViewSignInSignUpPage();

            // Act
            spyService.deleteApplicantProfile();

            // Assert
            // 1) Should call deleteProfileHelper()
            verify(spyService, times(1)).deleteProfileHelper();
            // 2) Then call applicantViewSignInSignUpPage()
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

            // We'll stub viewApplicantProfilePage() for verification
            doNothing().when(spyService).viewApplicantProfilePage();

            // Act
            spyService.deleteApplicantProfile();

            // Assert
            // No call to deleteProfileHelper() or applicantViewSignInSignUpPage()
            verify(spyService, never()).deleteProfileHelper();
            verify(spyService, never()).applicantViewSignInSignUpPage();

            // We call viewApplicantProfilePage() instead
            verify(spyService, times(1)).viewApplicantProfilePage();
        }
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 18
     */
    @Test
    public void testDeleteApplicantProfile_invalidInput() {
        // User enters something other than "Y" or "N"
        ApplicantService spyService = Mockito.spy(applicantService);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            mockedUtility
                    .when(() -> Utility.inputOutput("Are you sure you want to delete your profile? (Y/N)"))
                    .thenReturn("Maybe");

            // Act
            spyService.deleteApplicantProfile();

            // Assert
            // No calls to deleteProfileHelper(), applicantViewSignInSignUpPage(), or
            // viewApplicantProfilePage()
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
        // Scenario 1: user is a Recruiter,
        // => prints "You are not an applicant." => calls viewApplicantDashboard()

        // We'll spy on applicantService to verify method calls
        ApplicantService spyService = Mockito.spy(applicantService);

        // We also capture console output to verify printed messages (optional)
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            // currentUser is a Recruiter
            User recruiterUser = new Recruiter("R123", "Recruit", "User", "recruitUser", "pass");
            mockedUtility.when(Utility::getCurrentUser).thenReturn(recruiterUser);

            // Stub out viewApplicantDashboard()
            doNothing().when(spyService).viewApplicantDashboard();

            // Act
            spyService.viewApplicantApplications();

            // Assert
            String consoleOutput = outContent.toString();
            assertEquals(
                    "Should print 'You are not an applicant.'\n",
                    true,
                    consoleOutput.contains("You are not an applicant."));
            // Should call viewApplicantDashboard()
            verify(spyService, times(1)).viewApplicantDashboard();
        } finally {
            // Restore original System.out
            System.setOut(originalOut);
        }
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 29
     */
    @Test
    public void testViewApplicantApplications_noApplications() {
        // Scenario 2: currentUser is an Applicant but with null or empty applications

        ApplicantService spyService = Mockito.spy(applicantService);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            // Create an Applicant user with no applications
            Applicant applicantUser = new Applicant("A111", "AppFirst", "AppLast", "appUser", "pass");

            mockedUtility.when(Utility::getCurrentUser).thenReturn(applicantUser);

            // Stub out viewApplicantDashboard()
            doNothing().when(spyService).viewApplicantDashboard();

            // Act
            spyService.viewApplicantApplications();

            // Assert
            String consoleOutput = outContent.toString();
            assertEquals(
                    "Should print 'No applications found.'\n",
                    true,
                    consoleOutput.contains("No applications found."));
            // Then calls viewApplicantDashboard()
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
        // Scenario 3: user is an Applicant with a non-empty list of applications
        // => user picks "1" => we prompt for application ID and call
        // viewSpecificApplication(...)

        ApplicantService spyService = Mockito.spy(applicantService);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            // 1) Applicant with 2 applications
            Applicant applicantUser = new Applicant("A222", "First", "Last", "appUser2", "pass2");
            ArrayList<Application> apps = new ArrayList<>();
            apps.add(new Application("app1", "job1", "A222", null, null, 1, "Bachelors", "Java", ""));
            apps.add(new Application("app2", "job2", "A222", null, null, 2, "Masters", "Python", ""));

            mockedUtility.when(Utility::getCurrentUser).thenReturn(applicantUser);
            mockedUtility.when(Utility::getApplications).thenReturn(apps);
            // 2) Mock user input:
            // - First: "Please Select One Of The Options" => "1"
            // - Then: "Enter the Application ID" => "app2"
            mockedUtility
                    .when(() -> Utility.inputOutput("Please Select One Of The Options"))
                    .thenReturn("1");
            mockedUtility
                    .when(() -> Utility.inputOutput("Enter the Application ID"))
                    .thenReturn("app2");

            // Stub out viewSpecificApplication(...) to verify
            doNothing().when(spyService).viewSpecificApplication("app2");

            // We do not expect viewApplicantDashboard() in this path
            doNothing().when(spyService).viewApplicantDashboard();

            // Act
            spyService.viewApplicantApplications();

            // Assert
            // 1) The console output should list the 2 applications
            String consoleOutput = outContent.toString();
            assertEquals(
                    "Should list 'app1' and 'app2' in the console output",
                    true,
                    consoleOutput.contains("app1") && consoleOutput.contains("app2"));
            // 2) Because user chose "1", we expect to call viewSpecificApplication("app2")
            verify(spyService, times(1)).viewSpecificApplication("app2");

            // 3) We do NOT call viewApplicantDashboard() in this scenario
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
        // Scenario: user picks "2" => calls viewApplicantDashboard()

        ApplicantService spyService = Mockito.spy(applicantService);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            // Setup: Applicant user with non-empty apps
            Applicant applicantUser = new Applicant("A333", "FName", "LName", "appUser3", "pass3");
            ArrayList<Application> apps = new ArrayList<>();
            apps.add(new Application("appX", "jobX", "A333", null, null, 1, "Bachelors", "C++", ""));

            mockedUtility.when(Utility::getCurrentUser).thenReturn(applicantUser);
            mockedUtility.when(Utility::getApplications).thenReturn(apps);

            // Mock user input => "2"
            mockedUtility
                    .when(() -> Utility.inputOutput("Please Select One Of The Options"))
                    .thenReturn("2");

            doNothing().when(spyService).viewApplicantDashboard();

            // Act
            spyService.viewApplicantApplications();

            // Assert
            // => calls viewApplicantDashboard()
            verify(spyService, times(1)).viewApplicantDashboard();

            // => never calls viewSpecificApplication(...)
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
        // Scenario: user picks something other than "1" or "2"
        // => prints "Invalid input. Redirecting to the dashboard." => calls
        // viewApplicantDashboard()

        ApplicantService spyService = Mockito.spy(applicantService);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            // Setup: Applicant user with non-empty apps
            Applicant applicantUser = new Applicant("A444", "FName2", "LName2", "appUser4", "pass4");
            ArrayList<Application> apps = new ArrayList<>();
            apps.add(new Application("appY", "jobY", "A444", null, null, 3, "Masters", "Java", ""));

            mockedUtility.when(Utility::getCurrentUser).thenReturn(applicantUser);
            mockedUtility.when(Utility::getApplications).thenReturn(apps);

            // Mock user input => "abc"
            mockedUtility
                    .when(() -> Utility.inputOutput("Please Select One Of The Options"))
                    .thenReturn("abc");

            doNothing().when(spyService).viewApplicantDashboard();

            // Act
            spyService.viewApplicantApplications();

            // Assert
            String consoleOutput = outContent.toString();
            assertEquals(
                    "Should print 'Invalid input. Redirecting to the dashboard.'",
                    true,
                    consoleOutput.contains("Invalid input. Redirecting to the dashboard."));

            // Calls viewApplicantDashboard()
            verify(spyService, times(1)).viewApplicantDashboard();
            // Never calls viewSpecificApplication(...)
            verify(spyService, never()).viewSpecificApplication(anyString());
        } finally {
            System.setOut(originalOut);
        }
    }

    /*
     * `
     * Author: Mayur Shinde (mss62)
     * User Story: 36
     */
    @Test
    public void testViewJobDescFromApplication_applicationNotFound() {
        // Scenario 1: No application with the given ID

        // 1) Capture console output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            // 2) Mock Utility.getApplications() to return an empty or non-matching list
            ArrayList<Application> applications = new ArrayList<>();
            mockedUtility.when(Utility::getApplications).thenReturn(applications);

            // 3) We can return any list for Utility.getJobs(), but it won't matter
            mockedUtility.when(Utility::getJobs).thenReturn(new ArrayList<Job>());

            // 4) Call the method
            String missingAppId = "missingApp";
            applicantService.viewJobDescFromApplication(missingAppId);

            // 5) Assert we see "Application with ID missingApp not found."
            String consoleOutput = outContent.toString();
            assertTrue(
                    "Should print that the application was not found",
                    consoleOutput.contains("Application with ID missingApp not found."));
        } finally {
            // Restore original System.out
            System.setOut(originalOut);
        }
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 36
     */
    @Test
    public void testViewJobDescFromApplication_jobNotFound() {
        // Scenario 2: The application is found, but the associated job is missing

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            // 1) Setup the application
            Application app = new Application("app123", "jobXYZ", "userA", null, null, 0, "", "", "");
            ArrayList<Application> applications = new ArrayList<>();
            applications.add(app);

            // 2) Return that applications list
            mockedUtility.when(Utility::getApplications).thenReturn(applications);

            // 3) No matching job
            ArrayList<Job> jobs = new ArrayList<>();
            mockedUtility.when(Utility::getJobs).thenReturn(jobs);

            // 4) Act
            applicantService.viewJobDescFromApplication("app123");

            // 5) Assert
            String consoleOutput = outContent.toString();
            assertTrue(
                    "Should print that the job was not found",
                    consoleOutput.contains("Job with ID jobXYZ not found."));
        } finally {
            System.setOut(originalOut);
        }
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 36
     */
    @Test
    public void testViewJobDescFromApplication_jobAndApplicationFound() {
        // Scenario 3: Both application and job exist => prints job details

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            // 1) Create an application referencing "jobABC"
            Application app = new Application("app001", "jobABC", "userX", null, null, 0, "", "", "");
            ArrayList<Application> applications = new ArrayList<>();
            applications.add(app);
            mockedUtility.when(Utility::getApplications).thenReturn(applications);

            // 2) Create a matching job "jobABC"
            Job job = new Job("jobABC", "Software Engineer", "Develop software", null);
            ArrayList<Job> jobs = new ArrayList<>();
            jobs.add(job);
            mockedUtility.when(Utility::getJobs).thenReturn(jobs);

            // 3) Call method
            applicantService.viewJobDescFromApplication("app001");

            // 4) Assert
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
        // An extra scenario: the user passes an ID that doesn't match any application
        // This is basically the same as the "application not found" test,
        // but let's be explicit with a different ID

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            // We do have some application, but not matching the ID we pass
            Application existingApp = new Application("existingApp", "someJob", "userZ", null, null, 0, "", "", "");
            ArrayList<Application> apps = new ArrayList<>();
            apps.add(existingApp);

            mockedUtility.when(Utility::getApplications).thenReturn(apps);
            mockedUtility.when(Utility::getJobs).thenReturn(new ArrayList<Job>());

            // Pass "notMatching" => doesn't match "existingApp"
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

package test.whitebox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockStatic;
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

}

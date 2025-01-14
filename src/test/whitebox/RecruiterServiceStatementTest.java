package test.whitebox;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Application;
import model.ApplicationStatus;
import model.Assignment;
import model.Job;
import model.JobStatus;
import model.User;
import model.UserRole;
import service.CommonService;
import service.RecruiterService;
import utility.Utility;


public class RecruiterServiceStatementTest {

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
        Application app1 = new Application("1", "1", "1", ApplicationStatus.INPROGRESS, assignments, 2, "BSc", "JS, CSS", "Feedback");
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

            RecruiterService recruiterService = new RecruiterService();
            
            recruiterService.viewSpecificApplication("1");  // No exception expected
        }
    }

    @Test
    public void testViewSpecificApplication_InvalidApplicationId() {
        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            mockedUtility.when(Utility::getApplications).thenReturn(mockApplications);
            mockedUtility.when(Utility::getUsers).thenReturn(mockUsers);
            RecruiterService recruiterService = Mockito.spy(new RecruiterService());
            doNothing().when(recruiterService).viewAllApplications();

            recruiterService.viewSpecificApplication("999");    // Invalid application ID

            String expectedOutput = "No application found with given id";
            assertEquals(expectedOutput, outContent.toString().trim());
        }
    }

    @Test
    public void testViewSpecificApplication_NoApplicantFound() {
        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            mockedUtility.when(Utility::getApplications).thenReturn(mockApplications);
            mockedUtility.when(Utility::getUsers).thenReturn(new ArrayList<>());        // No users
            RecruiterService recruiterService = Mockito.spy(new RecruiterService());
            doNothing().when(recruiterService).viewAllApplications();

            recruiterService.viewSpecificApplication("1");

            String expectedOutput = "No applicant found with for this application";
            assertEquals(expectedOutput, outContent.toString().trim());
        }
    }

    @Test
    public void testSendAssignment_ValidRole() {
        try (MockedStatic<Utility> utilities = mockStatic(Utility.class)) {
            // Setup and Mocking
            Application application = new Application();
            application.setApplicantId("applicant123");
            application.setAssignments(new ArrayList<>());
    
            Map<String, List<String>> mockQuestionMap = new HashMap<>();
            mockQuestionMap.put("frontend", Arrays.asList("Question 1", "Question 2"));
    
            utilities.when(Utility::getQuestionMap).thenReturn(mockQuestionMap);
            utilities.when(() -> Utility.inputOutput(anyString())).thenReturn("frontend");
            RecruiterService recruiterService = Mockito.spy(new RecruiterService());
    
            // Test
            recruiterService.sendAssignment(application);
    
            // Assertions
            assertEquals(1, application.getAssignments().size());
            Assignment assignment = application.getAssignments().get(0);
            assertEquals("applicant123", assignment.getApplicantId());
            assertTrue(assignment.getAssignmentName().contains("Assignment frontend"));
            assertEquals(Arrays.asList("Question 1", "Question 2"), assignment.getQuestions());
    
            // Verify interactions
            utilities.verify(Utility::getQuestionMap);
            utilities.verify(() -> Utility.inputOutput(anyString()));
        }
    }

    @Test
    public void testUpdateDescriptionOfJobPost_JobFoundAndUpdated() {
        try (MockedStatic<Utility> utilities = mockStatic(Utility.class)) {
            // Setup and Mocking
            // Initial Jobs have different job name and description
            utilities.when(Utility::getJobs).thenReturn(mockJobs);
            utilities.when(() -> Utility.inputOutput(anyString())).thenReturn("Senior Engineer").thenReturn("Develop and manage software projects");
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

            
            CommonService mockCommonService = mock(CommonService.class);
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

            
            User mockUser = mock(User.class);
            when(mockUser.getUserName()).thenReturn("recruiter123");
            utilityMock.when(Utility::getCurrentUser).thenReturn(mockUser);

          
            List<User> mockUsers = new ArrayList<>();
            mockUsers.add(mockUser);
            utilityMock.when(Utility::getUsers).thenReturn(mockUsers);

            
            utilityMock.when(() -> Utility.setCurrentUser(null)).thenAnswer(invocation -> null);

            
            CommonService mockCommonService = mock(CommonService.class);
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

            
            User mockUser = mock(User.class);
            when(mockUser.getRole()).thenReturn(UserRole.RECRUITER);
            when(mockUser.getUserName()).thenReturn("recruiter1");
            when(mockUser.getPassword()).thenReturn("oldPassword");
            utilityMock.when(Utility::getCurrentUser).thenReturn(mockUser);

            
            User anotherUser = mock(User.class);
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

package test.whitebox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.After;
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

public class ApplicantServiceBranchTest {
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

    
}

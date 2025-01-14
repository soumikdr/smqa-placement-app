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
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import model.Application;
import model.ApplicationStatus;
import model.Assignment;
import model.AssignmentStatus;
import model.Job;
import model.JobStatus;
import model.User;
import model.UserRole;
import service.ApplicantService;
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
}

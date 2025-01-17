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
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.After;
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
    public void testViewJobPostingForm() {

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

            Mockito.verify(spyObject, times(1)).submitNewJobPost(any(),any());
            Mockito.verify(spyObject, times(1)).viewRecruiterDashboard();
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

        
        try(MockedStatic<Utility> mockedUtility=Mockito.mockStatic(Utility.class);
        ){
            mockedUtility.when(()->Utility.inputOutput(Mockito.anyString())).thenReturn("1");
            RecruiterService recruiterService = Mockito.spy(new RecruiterService());
            doNothing().when(recruiterService).viewRecruiterProfilePage();

            recruiterService.viewRecruiterDashboard();

            verify(recruiterService, times(1)).viewRecruiterProfilePage();
        	
        }


    }

    @Test
    public void testViewRecruiterDashboard_ViewAvailableJobs() {
        // Simulate user input "2"
        try(MockedStatic<Utility> mockedUtility=Mockito.mockStatic(Utility.class);
        ){
            mockedUtility.when(()->Utility.inputOutput(Mockito.anyString())).thenReturn("2");
            RecruiterService recruiterService = Mockito.spy(new RecruiterService());
            doNothing().when(recruiterService).viewAvailableJobs();

            recruiterService.viewRecruiterDashboard();

            verify(recruiterService, times(1)).viewAvailableJobs();
        	
        }


    }

    @Test
    public void testViewRecruiterDashboard_ViewAllApplications() {

        try(MockedStatic<Utility> mockedUtility=Mockito.mockStatic(Utility.class);
        ){
            mockedUtility.when(()->Utility.inputOutput(Mockito.anyString())).thenReturn("3");
            RecruiterService recruiterService = Mockito.spy(new RecruiterService());
            doNothing().when(recruiterService).viewAllApplications();

            recruiterService.viewRecruiterDashboard();

            verify(recruiterService, times(1)).viewAllApplications();
        	
        }


    }

    @Test
    public void testViewRecruiterDashboard_InvalidInput() {

        
        try(MockedStatic<Utility> mockedUtility=Mockito.mockStatic(Utility.class);
        ){
            mockedUtility.when(()->Utility.inputOutput(Mockito.anyString())).thenReturn("0");
            RecruiterService recruiterService = Mockito.spy(new RecruiterService());
            Mockito.doNothing().when(recruiterService).viewRecruiterDashboard();

            recruiterService.viewRecruiterDashboard();

            verify(recruiterService, times(1)).viewRecruiterDashboard();
        	
        }


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
   public void testSendAssignment_ValidInputs() {
       try (MockedStatic<Utility> utilities = mockStatic(Utility.class)) {
           // Arrange and mock dependencies
           utilities.when(() -> Utility.inputOutput(anyString())).thenReturn("Math Assignment").thenReturn("What is 2 + 2?").thenReturn("n");
           // Clear assignments if any
           mockApplication.setAssignments(new ArrayList<Assignment>());
           RecruiterService recruiterService = Mockito.spy(new RecruiterService());
           doNothing().when(recruiterService).viewSpecificApplication(mockApplication.getId());

           // Act
           recruiterService.sendAssignment(mockApplication);

           // Assert
           assertEquals(1, mockApplication.getAssignments().size());
           Assignment assignment = mockApplication.getAssignments().get(0);
           assertEquals("Math Assignment", assignment.getAssignmentName());
       }
   }

   @Test
   public void testSendAssignment_EmptyTitle() {
       try (MockedStatic<Utility> utilities = mockStatic(Utility.class)) {
           // Arrange
           // First empty title, then valid title
           utilities.when(() -> Utility.inputOutput(anyString())).thenReturn("").thenReturn("Math Assignment").thenReturn("What is 2 + 2?").thenReturn("n");
           // Clear assignments if any
           mockApplication.setAssignments(new ArrayList<Assignment>());
           RecruiterService recruiterService = Mockito.spy(new RecruiterService());
           doNothing().when(recruiterService).viewSpecificApplication(mockApplication.getId());

           // Act
           recruiterService.sendAssignment(mockApplication);

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

           assertEquals(1, mockApplication.getAssignments().size());
           Assignment assignment = mockApplication.getAssignments().get(0);
           assertEquals("Math Assignment", assignment.getAssignmentName());
           assertTrue(filteredLines.contains("Assignment title cannot be empty."));
       }
   }

   @Test
   public void testSendAssignment_EmptyQuestion() {
       try (MockedStatic<Utility> utilities = mockStatic(Utility.class)) {
           // Arrange
           // First empty question, then valid question
           utilities.when(() -> Utility.inputOutput(anyString())).thenReturn("Math Assignment").thenReturn("").thenReturn("What is 2 + 2?").thenReturn("n");
           // Clear assignments if any
           mockApplication.setAssignments(new ArrayList<Assignment>());
           RecruiterService recruiterService = Mockito.spy(new RecruiterService());
           doNothing().when(recruiterService).viewSpecificApplication(mockApplication.getId());

           // Act
           recruiterService.sendAssignment(mockApplication);

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

           assertEquals(1, mockApplication.getAssignments().size());
           Assignment assignment = mockApplication.getAssignments().get(0);
           assertEquals("Math Assignment", assignment.getAssignmentName());
           assertTrue(filteredLines.contains("Question can not be empty"));
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

    @Test
    public void testVisitSignInSignUpPageRecruiter_option1() {

        RecruiterService spyService = Mockito.spy(new RecruiterService());

        try (MockedStatic<Utility> utilityMock = Mockito.mockStatic(Utility.class);
             MockedStatic<CommonService> commonServiceMock = Mockito.mockStatic(CommonService.class)) {

            utilityMock.when(() -> Utility.inputOutput("Please select one of the options")).thenReturn("1");


            doNothing().when(spyService).recruiterSignInPage();


            spyService.visitSignInSignUpPageRecruiter();


            verify(spyService, times(1)).recruiterSignInPage();

            verify(spyService, never()).viewRecruiterSignUpPage();
            commonServiceMock.verify( never(),() -> CommonService.getInstance());
        }
    }


    @Test
    public void testVisitSignInSignUpPageRecruiter_option2() {

        RecruiterService spyService = Mockito.spy(new RecruiterService());

        try (MockedStatic<Utility> utilityMock = Mockito.mockStatic(Utility.class);
             MockedStatic<CommonService> commonServiceMock = Mockito.mockStatic(CommonService.class)) {

            utilityMock.when(() -> Utility.inputOutput("Please select one of the options")).thenReturn("2");


            doNothing().when(spyService).viewRecruiterSignUpPage();


            spyService.visitSignInSignUpPageRecruiter();


            verify(spyService, times(1)).viewRecruiterSignUpPage();

            verify(spyService, never()).recruiterSignInPage();
            commonServiceMock.verify(never(),()->CommonService.getInstance());
        }
    }


    @Test
    public void testVisitSignInSignUpPageRecruiter_option3() {

        RecruiterService spyService = Mockito.spy(new RecruiterService());

        try (MockedStatic<Utility> utilityMock = Mockito.mockStatic(Utility.class);
             MockedStatic<CommonService> commonServiceMock = Mockito.mockStatic(CommonService.class)) {


            utilityMock.when(() -> Utility.inputOutput("Please select one of the options")).thenReturn("3");


            CommonService mockCommonService = mock(CommonService.class);
            commonServiceMock.when(CommonService::getInstance).thenReturn(mockCommonService);
            doNothing().when(mockCommonService).accessLandingPage();


            spyService.visitSignInSignUpPageRecruiter();


            commonServiceMock.verify(times(1),() -> CommonService.getInstance());
            verify(mockCommonService, times(1)).accessLandingPage();

            verify(spyService, never()).recruiterSignInPage();
            verify(spyService, never()).viewRecruiterSignUpPage();
        }
    }


    @Test
    public void testVisitSignInSignUpPageRecruiter() {

        RecruiterService spyService = Mockito.spy(new RecruiterService());

        try (MockedStatic<Utility> utilityMock = Mockito.mockStatic(Utility.class);
             MockedStatic<CommonService> commonServiceMock = Mockito.mockStatic(CommonService.class)) {


            utilityMock.when(() -> Utility.inputOutput("Please select one of the options"))
                      .thenReturn("4")
                      .thenReturn("1");

            doNothing().when(spyService).recruiterSignInPage();


            spyService.visitSignInSignUpPageRecruiter();


            verify(spyService, times(2)).visitSignInSignUpPageRecruiter();


            verify(spyService, times(1)).recruiterSignInPage();


            verify(spyService, never()).viewRecruiterSignUpPage();


            commonServiceMock.verify(never(),() -> CommonService.getInstance() );


            String consoleOutput = outContent.toString();
            assertTrue(consoleOutput.contains("You entered an invalid option. Please try again."));
            assertTrue(consoleOutput.contains("Welcome to the Sign In/Sign Up page for Recruiter"));
            assertTrue(consoleOutput.contains("1. Sign In for Recruiter"));
            assertTrue(consoleOutput.contains("2. Sign Up for Recruiter"));
            assertTrue(consoleOutput.contains("3. Go back to the previous menu"));
        }
    }



    @Test
    public void testVisitSignInSignUpPageRecruiter_invalidOption_tryAgainNo() {

        RecruiterService spyService = Mockito.spy(new RecruiterService());

        try (MockedStatic<Utility> utilityMock = Mockito.mockStatic(Utility.class);
             MockedStatic<CommonService> commonServiceMock = Mockito.mockStatic(CommonService.class)) {

            utilityMock.when(() -> Utility.inputOutput("Please select one of the options"))
                      .thenReturn("abc") // First input: invalid
                      .thenReturn("1");  // Second input: valid


            doNothing().when(spyService).recruiterSignInPage();


            spyService.visitSignInSignUpPageRecruiter();


            verify(spyService, times(2)).visitSignInSignUpPageRecruiter();


            verify(spyService, times(1)).recruiterSignInPage();


            verify(spyService, never()).viewRecruiterSignUpPage();


            commonServiceMock.verify( never(),() -> CommonService.getInstance());
        }
    }

    //UserStory:9
    @Test
    public void testLogoutRecruiter_SuccessfulLogout() {

    	RecruiterService spyService = Mockito.spy(new RecruiterService());
        try (MockedStatic<Utility> utilityMock = mockStatic(Utility.class);
             MockedStatic<CommonService> commonServiceMock = mockStatic(CommonService.class)) {


            utilityMock.when(() -> Utility.setCurrentUser(null)).thenAnswer(invocation -> null);


            CommonService mockCommonService = mock(CommonService.class);
            commonServiceMock.when(CommonService::getInstance).thenReturn(mockCommonService);


            doNothing().when(mockCommonService).accessLandingPage();


            spyService.logoutRecruiter();


            String consoleOutput = outContent.toString();
            assertTrue(
                       "Initiating logout message should be printed.",consoleOutput.contains("Initiating logout process for Recruiter"));


            utilityMock.verify( times(1),() -> Utility.setCurrentUser(null));


            commonServiceMock.verify(times(1),CommonService::getInstance);


            verify(mockCommonService, times(1)).accessLandingPage();
        }
    }
//UserStory: 21
    @Test
    public void testDeleteRecruiterProfile_SuccessfulDeletion() {
    	RecruiterService spyService = Mockito.spy(new RecruiterService());

        try (MockedStatic<Utility> utilityMock = mockStatic(Utility.class);
             MockedStatic<CommonService> commonServiceMock = mockStatic(CommonService.class)) {


            User mockUser = mock(User.class);
            Mockito.when(mockUser.getUserName()).thenReturn("recruiter123");
            utilityMock.when(Utility::getCurrentUser).thenReturn(mockUser);


            List<User> mockUsers = new ArrayList<>();
            mockUsers.add(mockUser);
            utilityMock.when(Utility::getUsers).thenReturn(mockUsers);


            utilityMock.when(() -> Utility.setCurrentUser(null)).thenAnswer(invocation -> null);


            CommonService mockCommonService = mock(CommonService.class);
            commonServiceMock.when(CommonService::getInstance).thenReturn(mockCommonService);
            doNothing().when(mockCommonService).accessLandingPage();


            spyService.deleteRecruiterProfile();


            String consoleOutput = outContent.toString();
            assertTrue(
                       "Should print 'Deleting Recruiter profile'",consoleOutput.contains("Deleting Recruiter profile"));
            assertTrue("Should print 'Profile deleted successfully'",consoleOutput.contains("Profile deleted successfully")
                       );

            utilityMock.verify(times(2),Utility::getCurrentUser);

            utilityMock.verify(times(1),Utility::getUsers);


            assertTrue( "User should be removed from the users list",mockUsers.isEmpty());


            utilityMock.verify( times(1),() -> Utility.setCurrentUser(null));


            commonServiceMock.verify( times(1),CommonService::getInstance);


            verify(mockCommonService, times(1)).accessLandingPage();
        }
    }

    @Test
    public void testDeleteRecruiterProfile() {

    	RecruiterService spyService = Mockito.spy(new RecruiterService());
        try (MockedStatic<Utility> utilityMock = mockStatic(Utility.class);
             MockedStatic<CommonService> commonServiceMock = mockStatic(CommonService.class)) {


            User mockUser = mock(User.class);
            Mockito.when(mockUser.getUserName()).thenReturn("recruiter123");
            utilityMock.when(Utility::getCurrentUser).thenReturn(mockUser);


            List<User> mockUsers = new ArrayList<>();
            User anotherUser = mock(User.class);
            Mockito. when(anotherUser.getUserName()).thenReturn("recruiter456");
            mockUsers.add(anotherUser);
            utilityMock.when(Utility::getUsers).thenReturn(mockUsers);


            utilityMock.when(() -> Utility.setCurrentUser(null)).thenAnswer(invocation -> null);


            CommonService mockCommonService = mock(CommonService.class);
            commonServiceMock.when(CommonService::getInstance).thenReturn(mockCommonService);
            doNothing().when(mockCommonService).accessLandingPage();


            spyService.deleteRecruiterProfile();


            String consoleOutput = outContent.toString();
            assertTrue(
                       "Should print 'Deleting Recruiter profile'",consoleOutput.contains("Deleting Recruiter profile"));


            utilityMock.verify( times(2),Utility::getCurrentUser);




            assertTrue("No user should be removed from the users list",mockUsers.size() == 1);


            utilityMock.verify( times(1),() -> Utility.setCurrentUser(null));


            commonServiceMock.verify(times(1),CommonService::getInstance);


            verify(mockCommonService, times(1)).accessLandingPage();
        }
    }

 // UserStory: 30; ar668 (Amisha Rastogi)

    @Test
    public void testViewAllApplications_UserIsApplicant() {

    	RecruiterService spyService = Mockito.spy(new RecruiterService());
        try (MockedStatic<Utility> utilityMock = mockStatic(Utility.class)) {

            User mockApplicant = mock(Applicant.class);
            utilityMock.when(Utility::getCurrentUser).thenReturn(mockApplicant);
            doNothing().when(spyService).viewRecruiterDashboard();

            spyService.viewAllApplications();


            String consoleOutput = outContent.toString();
            assertTrue("Should print unauthorized access message.",consoleOutput.contains("You are not authorized to view this page")
                       );

            verify(spyService, times(1)).viewRecruiterDashboard();

        }
    }
    @Test
    public void testViewAllApplications_ApplicationsNull() {

    	 RecruiterService spyService = Mockito.spy(new RecruiterService());
        try (MockedStatic<Utility> utilityMock = mockStatic(Utility.class);
             MockedStatic<CommonService> commonServiceMock = mockStatic(CommonService.class)) {


            User mockRecruiter = mock(User.class);
            utilityMock.when(Utility::getCurrentUser).thenReturn(mockRecruiter);


            utilityMock.when(Utility::getApplications).thenReturn(null);


            utilityMock.when(() -> Utility.inputOutput("Please select one of the options.."))
            .thenReturn("2");
            doNothing().when(spyService).viewRecruiterDashboard();

            CommonService mockCommonService = mock(CommonService.class);
            commonServiceMock.when(CommonService::getInstance).thenReturn(mockCommonService);
            doNothing().when(mockCommonService).accessLandingPage();


            spyService.viewAllApplications();


            String consoleOutput = outContent.toString();
            assertTrue(
                       "Should print 'No applications available.' message.",consoleOutput.contains("No applications available."));


            verify(spyService, times(1)).viewRecruiterDashboard();


            utilityMock.verify(times(1),Utility::getApplications );


        }
    }
    @Test
    public void testViewAllApplications_ApplicationsAvailable() {
        // Arrange
        try (MockedStatic<Utility> utilityMock = mockStatic(Utility.class);
             MockedStatic<CommonService> commonServiceMock = mockStatic(CommonService.class)) {


            User mockRecruiter = mock(User.class);
            utilityMock.when(Utility::getCurrentUser).thenReturn(mockRecruiter);


            Application mockApplication = mock(Application.class);
            Mockito.when(mockApplication.getId()).thenReturn("app123");
            Mockito.when(mockApplication.getStatus()).thenReturn(ApplicationStatus.INPROGRESS);
            Mockito.when(mockApplication.getApplicantId()).thenReturn("applicant456");

            RecruiterService spyService = Mockito.spy(new RecruiterService());
            List<Application> mockApplications = new ArrayList<>();
            mockApplications.add(mockApplication);
            utilityMock.when(Utility::getApplications).thenReturn(new ArrayList<>(mockApplications));


            utilityMock.when(() -> Utility.setCurrentUser(null)).thenAnswer(invocation -> null);


            CommonService mockCommonService = mock(CommonService.class);
            commonServiceMock.when(CommonService::getInstance).thenReturn(mockCommonService);
            doNothing().when(mockCommonService).accessLandingPage();


            utilityMock.when(() -> Utility.inputOutput("Please select one of the options.."))
                       .thenReturn("1","2");
            utilityMock.when(() -> Utility.inputOutput("Enter the Application ID to view details"))
                       .thenReturn("app123");
            doNothing().when(spyService).viewRecruiterDashboard();
            doNothing().when(spyService).viewSpecificApplication("app123");

            spyService.viewAllApplications();


            String consoleOutput = outContent.toString();

            assertTrue(
                       "Should list the application details",consoleOutput.contains("Application ID: app123|Status: INPROGRESS|Applicant ID: applicant456"));
            assertTrue(
                       "Should print option '1: View specific Application details'",consoleOutput.contains("1: View specific Application details"));


            verify(spyService, times(1)).viewSpecificApplication("app123");


            utilityMock.verify(times(1),Utility::getCurrentUser );

            utilityMock.verify(times(1),Utility::getApplications);

        }
    }


 // UserStory: 13; ar668

    @Test
    public void testResetPasswordRecruiter() {

        try (MockedStatic<Utility> utilityMock = mockStatic(Utility.class)) {


            User mockUser = mock(User.class);
            Mockito.when(mockUser.getRole()).thenReturn(UserRole.RECRUITER);
            Mockito.when(mockUser.getUserName()).thenReturn("recruiter1");
            Mockito.when(mockUser.getPassword()).thenReturn("oldPassword");
            utilityMock.when(Utility::getCurrentUser).thenReturn(mockUser);


            User anotherUser = mock(User.class);
            Mockito.when(anotherUser.getUserName()).thenReturn("user2");
            List<User> users = new ArrayList<>();
            users.add(mockUser);
            users.add(anotherUser);
            utilityMock.when(Utility::getUsers).thenReturn(users);


            utilityMock.when(() -> Utility.inputOutput("Enter the Reset Code")).thenReturn("XVQTY");
            utilityMock.when(() -> Utility.inputOutput("Enter your New Password")).thenReturn("newSecurePassword");
            RecruiterService recruiterService = Mockito.spy(new RecruiterService());

            doNothing().when(recruiterService).viewResetPasswordPage();


            recruiterService.resetPasswordRecruiter("recruiter1");


            String consoleOutput = outContent.toString();
            assertTrue( "Should print welcome message.",consoleOutput.contains("Welcome to Reset Password Page for Recruiter")
                      );
            assertTrue("Should display entered username.",consoleOutput.contains("Your entered username: recruiter1"));
                       ;




            verify(mockUser, times(2)).setPassword("newSecurePassword");

            verify(anotherUser, never()).setPassword(anyString());

        }
    }


    @Test
    public void testResetPasswordRecruiter_UserDoesNotMatch() {

        try (MockedStatic<Utility> utilityMock = mockStatic(Utility.class)) {


            User mockUser = mock(User.class);
            Mockito.when(mockUser.getRole()).thenReturn(UserRole.RECRUITER);
            Mockito.when(mockUser.getUserName()).thenReturn("recruiter3");
            Mockito.when(mockUser.getPassword()).thenReturn("oldPassword");
            utilityMock.when(Utility::getCurrentUser).thenReturn(mockUser);
            RecruiterService recruiterService = Mockito.spy(new RecruiterService());
            // 2. Mock getUsers() to return a list containing the current user
            List<User> users = new ArrayList<>();
            users.add(mockUser);
            utilityMock.when(Utility::getUsers).thenReturn(users);


            doNothing().when(recruiterService).viewResetPasswordPage();



            recruiterService.resetPasswordRecruiter("wrongUsername");


            String consoleOutput = outContent.toString();

            assertTrue( "Should display entered username.",consoleOutput.contains("Your entered username: wrongUsername")
                      );


            verify(mockUser, never()).setPassword(anyString());


            verify(recruiterService, times(1)).viewResetPasswordPage();
        }
    }






    /*
     * Author: Mohammad Ansar Patil (map66)
     * User Story: 31
     */
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

    /*
     * Author: Mohammad Ansar Patil (map66)
     * User Story: 31
     */
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

    /*
     * Author: Mohammad Ansar Patil (map66)
     * User Story: 31
     */
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

    /*
     * Author: Mohammad Ansar Patil (map66)
     * User Story: 31
     */
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

    /*
     * Author: Mohammad Ansar Patil (map66)
     * User Story: 31
     */
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

    /*
     * Author: Mohammad Ansar Patil (map66)
     * User Story: 31
     */
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

    /*
     * Author: Mohammad Ansar Patil (map66)
     * User Story: 19
     */  
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

    /*
     * Author: Mohammad Ansar Patil (map66)
     * User Story: 19
     */  
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

    /*
     * Author: Mohammad Ansar Patil (map66)
     * User Story: 19
     */  
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

    /*
     * Author: Mohammad Ansar Patil (map66)
     * User Story: 19
     */  
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

    /*
     * Author: Mohammad Ansar Patil (map66)
     * User Story: 46
     */
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

    /*
     * Author: Mohammad Ansar Patil (map66)
     * User Story: 46
     */
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

    /*
     * Author: Mohammad Ansar Patil (map66)
     * User Story: 46
     */
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

    /*
     * Author: Mohammad Ansar Patil (map66)
     * User Story: 46
     */
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

    /*
     * Author: Mohammad Ansar Patil (map66)
     * User Story: 46
     */
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

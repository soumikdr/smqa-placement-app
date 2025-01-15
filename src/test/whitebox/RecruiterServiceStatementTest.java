package test.whitebox;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

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

import model.Application;
import model.ApplicationStatus;
import model.Assignment;
import model.Job;
import model.JobStatus;
import model.User;
import model.UserRole;
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
    public void testViewSubmittedAnswers() {
        Application mockApplication = mock(Application.class);
        RecruiterService service = new RecruiterService();
        RecruiterService spyObject = Mockito.spy(service);
        Mockito.doNothing().when(spyObject).viewSpecificApplication(Mockito.anyString());

        ArrayList<String> questionsWithAnswers = new ArrayList<>();
        questionsWithAnswers.add("What is Java?");
        questionsWithAnswers.add("What is OOP?");
        
        ArrayList<String> answers = new ArrayList<>();
        answers.add("Java is a programming language.");
        answers.add("OOP stands for Object-Oriented Programming.");
        
        Assignment mockAssignmentWithAnswers = new Assignment("A1", "U101", "Assessment 1", questionsWithAnswers, answers);
        ArrayList<Assignment> mockAssignmentsWithAnswers = new ArrayList<>();
        mockAssignmentsWithAnswers.add(mockAssignmentWithAnswers);

        ArrayList<String> questionsWithoutAnswers = new ArrayList<>();
        questionsWithoutAnswers.add("What is Java?");
        questionsWithoutAnswers.add("What is OOP?");
        
        Assignment mockAssignmentWithoutAnswers = new Assignment("A2", "U102", "Assessment 2", questionsWithoutAnswers, new ArrayList<>());
        ArrayList<Assignment> mockAssignmentsWithoutAnswers = new ArrayList<>();
        mockAssignmentsWithoutAnswers.add(mockAssignmentWithoutAnswers);

        ArrayList<Assignment> mockAssignmentsEmpty = new ArrayList<>();

        ArrayList<String> interviewQuestions = new ArrayList<>();
        interviewQuestions.add("What are your strengths?");
        interviewQuestions.add("Why do you want this job?");
        ArrayList<Assignment> mockInterviewAssignments = new ArrayList<>();
        mockInterviewAssignments.add(new Assignment("I1", "U101", "Interview", interviewQuestions, new ArrayList<>()));
        
        ArrayList<String> interviewAnswers = new ArrayList<>();
        interviewAnswers.add("I can manage time very well");
        interviewAnswers.add("To achieve my goals");
        ArrayList<Assignment> mockInterviewAssignmentsWithAnswers = new ArrayList<>();
        mockInterviewAssignmentsWithAnswers.add(new Assignment("I1", "U101", "Interview", interviewQuestions, interviewAnswers));
        
        when(mockApplication.getAssignments()).thenReturn(mockAssignmentsWithAnswers);
        when(mockApplication.getInterviewAssignments()).thenReturn(mockInterviewAssignments);
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
        Assert.assertTrue(consoleOutput.contains("Interview: Interview"));

        outputStream.reset();

        when(mockApplication.getAssignments()).thenReturn(mockAssignmentsWithoutAnswers);

        spyObject.viewSubmittedAnswers(mockApplication);

        consoleOutput = outputStream.toString();
        Assert.assertTrue(consoleOutput.contains("Assignment: Assessment 2"));
        Assert.assertTrue(consoleOutput.contains("No answer submitted yet"));
        Assert.assertTrue(consoleOutput.contains("No answer submitted yet"));

        outputStream.reset();

        when(mockApplication.getAssignments()).thenReturn(mockAssignmentsEmpty);

        spyObject.viewSubmittedAnswers(mockApplication);

        consoleOutput = outputStream.toString();
        Assert.assertTrue(consoleOutput.contains("Submitted Answers for assessments:"));
        Assert.assertTrue(consoleOutput.contains("Submitted Answers for interviews:"));
        
        outputStream.reset();

        when(mockApplication.getAssignments()).thenReturn(mockAssignmentsWithAnswers);
        when(mockApplication.getInterviewAssignments()).thenReturn(mockInterviewAssignments);

        spyObject.viewSubmittedAnswers(mockApplication);

        consoleOutput = outputStream.toString();
        Assert.assertTrue(consoleOutput.contains("Interview: Interview"));
        Assert.assertTrue(consoleOutput.contains("Question: "));
        Assert.assertTrue(consoleOutput.contains("What are your strengths?"));
        Assert.assertTrue(consoleOutput.contains("No answer submitted yet"));
        Assert.assertTrue(consoleOutput.contains("Question: "));
        Assert.assertTrue(consoleOutput.contains("Why do you want this job?"));
        Assert.assertTrue(consoleOutput.contains("No answer submitted yet"));

         when(mockApplication.getAssignments()).thenReturn(mockAssignmentsWithAnswers);
         when(mockApplication.getInterviewAssignments()).thenReturn(mockInterviewAssignmentsWithAnswers);
 
         spyObject.viewSubmittedAnswers(mockApplication);
 
         consoleOutput = outputStream.toString();
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

    @Test
    public void viewRecruiterProfilePageTest() throws IOException {
        RecruiterService service = new RecruiterService();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        RecruiterService spyObject = Mockito.spy(service);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {

            Mockito.doNothing().when(spyObject).updateRecruiterProfile();
            Mockito.doNothing().when(spyObject).deleteRecruiterProfile();
            Mockito.doNothing().when(spyObject).viewRecruiterDashboard();

            User mockUser = new User("U101", "John", "Doe", "johndoe", "bestpassword", UserRole.RECRUITER);
            mockedUtility.when(Utility::getCurrentUser).thenReturn(mockUser);

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("1");
            spyObject.viewRecruiterProfilePage();
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Redirecting to update profile page..."));
            outputStream.reset();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("2");
            spyObject.viewRecruiterProfilePage();
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Redirecting to delete profile page..."));
            outputStream.reset();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("3");
            spyObject.viewRecruiterProfilePage();
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Redirecting to dashboard..."));
            outputStream.reset();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("invalid", "1");
            spyObject.viewRecruiterProfilePage();
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("You entered invalid option"));
            outputStream.reset();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("invalid", "3");
            spyObject.viewRecruiterProfilePage();
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("You entered invalid option"));
            Assert.assertTrue(consoleOutput.contains("Redirecting to dashboard"));
            outputStream.reset();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("3");
            spyObject.viewRecruiterProfilePage();
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("First Name: John"));
            Assert.assertTrue(consoleOutput.contains("Last Name: Doe"));
            Assert.assertTrue(consoleOutput.contains("User Name: johndoe"));
            Assert.assertTrue(consoleOutput.contains("Role: RECRUITER"));
            Assert.assertTrue(consoleOutput.contains("Redirecting to dashboard"));
            outputStream.reset();

            Mockito.verify(spyObject, Mockito.atLeastOnce()).viewRecruiterProfilePage();
            mockedUtility.verify(Mockito.atLeastOnce(), () -> Utility.inputOutput(Mockito.anyString()));
        }

    }

    @Test
    public void viewSpecificJobPostTest() throws IOException {
        RecruiterService service = new RecruiterService();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        RecruiterService spyObject = Mockito.spy(service);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {

            ArrayList<Job> mockJobs = new ArrayList<>();
            mockJobs.add(new Job("101", "Software Engineer", "Develop software", JobStatus.PRIVATE));
            mockJobs.add(new Job("102", "Data Scientist", "Analyze data", JobStatus.PUBLIC));

            mockedUtility.when(Utility::getJobs).thenReturn(mockJobs);

            Mockito.doNothing().when(spyObject).updateDescriptionOfJobPost(Mockito.anyString());
            Mockito.doNothing().when(spyObject).updateStatusOfJobPost(Mockito.anyString());
            Mockito.doNothing().when(spyObject).viewTotalNumberOfApplications(Mockito.anyString());
            Mockito.doNothing().when(spyObject).viewRecruiterDashboard();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("101", "1", "2");
            spyObject.viewSpecificJobPost();
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Job ID: 101"));
            Assert.assertTrue(consoleOutput.contains("Job Name: Software Engineer"));
            Assert.assertTrue(consoleOutput.contains("Redirecting to update job description page"));
            Mockito.verify(spyObject, Mockito.times(1)).updateDescriptionOfJobPost("101");
            outputStream.reset();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("101", "2", "2");
            spyObject.viewSpecificJobPost();
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Redirecting to update job status page"));
            Mockito.verify(spyObject, Mockito.times(1)).updateStatusOfJobPost("101");
            outputStream.reset();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("101", "2", "1", "101");
            spyObject.viewSpecificJobPost();
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Redirecting to update job status page"));
            Assert.assertTrue(consoleOutput.contains("Redirecting to view specific job details"));
            Mockito.verify(spyObject, Mockito.times(2)).updateStatusOfJobPost("101");
            outputStream.reset();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("101", "3", "2");
            spyObject.viewSpecificJobPost();
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Redirecting to total applications for the job"));
            Mockito.verify(spyObject, Mockito.times(1)).viewTotalNumberOfApplications("101");
            outputStream.reset();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("101", "4");
            spyObject.viewSpecificJobPost();
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Redirecting to main menu"));
            outputStream.reset();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("999", "2");
            spyObject.viewSpecificJobPost();
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("You have entered a invalid Job id"));
            Mockito.verify(spyObject, Mockito.times(8)).viewRecruiterDashboard();
            outputStream.reset();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("101", "invalid");
            spyObject.viewSpecificJobPost();
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("You entered invalid option"));
            Mockito.verify(spyObject, Mockito.times(10)).viewRecruiterDashboard();
            outputStream.reset();

            Mockito.verify(spyObject, Mockito.times(8)).viewSpecificJobPost();
            Mockito.verify(spyObject, Mockito.times(1)).updateDescriptionOfJobPost("101");
            Mockito.verify(spyObject, Mockito.times(2)).updateStatusOfJobPost("101");
            Mockito.verify(spyObject, Mockito.times(1)).viewTotalNumberOfApplications("101");
            Mockito.verify(spyObject, Mockito.times(10)).viewRecruiterDashboard();
            mockedUtility.verify(Mockito.times(23), () -> Utility.inputOutput(Mockito.anyString()));
        }
    }

    
    

}

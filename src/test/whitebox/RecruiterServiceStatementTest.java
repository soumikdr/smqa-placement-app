package test.whitebox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;

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

    // ETY1 - STORY 22
    @Test
    public void testViewJobPostingForm_notEmpty() {

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


            Mockito.verify(spyObject, times(1)).submitNewJobPost(any(),any());
            Mockito.verify(spyObject, times(1)).viewRecruiterDashboard();

            mockedUtility.verify(times(2), () -> Utility.inputOutput(Mockito.anyString()));

        }

    }

    // ETY1 - STORY 22
    @Test
    public void testViewJobPostingForm_empty() {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        RecruiterService service = new RecruiterService();
        RecruiterService spyObject = Mockito.spy(service);


        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {


            mockedUtility.when(() -> Utility.inputOutput("Enter the New Job Title")).thenReturn(null);
            mockedUtility.when(() -> Utility.inputOutput("Enter the New Job Description")).thenReturn(null);

            Mockito.doCallRealMethod().doNothing().when(spyObject).viewJobPostingForm();

            spyObject.viewJobPostingForm();
            String consoleOutput = outputStream.toString();

            Assert.assertTrue(consoleOutput.contains("Job Title or Job Description empty"));

            Mockito.verify(spyObject, times(2)).viewJobPostingForm();

            mockedUtility.verify(times(2), () -> Utility.inputOutput(Mockito.anyString()));

        }

    }

    // ETY1 - STORY 50
    @Test
    public void viewTotalNumberOfApplicationsTest_oneApplication() throws IOException {
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
    public void testApproveRejectApplication_approved() {

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



            Mockito.verify(spyObject, times(1)).viewSpecificApplication(any());
            Mockito.verify(spyObject, times(1)).approveRejectApplication(Mockito.any());
            mockedUtility.verify(times(1), () -> Utility.inputOutput(Mockito.anyString()));
            mockedUtility.verify(times(1), () -> Utility.getApplications());

        }

    }

    // ETY1 - STORY 47
    @Test
    public void testApproveRejectApplication_rejected() {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        RecruiterService service = new RecruiterService();
        RecruiterService spyObject = Mockito.spy(service);


        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {

            Mockito.doNothing().when(spyObject).viewSpecificApplication(any());

            mockedUtility.when(() -> Utility.getApplications()).thenReturn(mockApplications);


            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("2");

            mockApplication.setStatus(ApplicationStatus.UNSUCCESSFUL);

            spyObject.approveRejectApplication(mockApplication);

            String consoleOutput = outputStream.toString();

            Assert.assertTrue(consoleOutput.contains("Application Rejected"));


            Mockito.verify(spyObject, times(1)).viewSpecificApplication(any());
            Mockito.verify(spyObject, times(1)).approveRejectApplication(Mockito.any());
            mockedUtility.verify(times(1), () -> Utility.inputOutput(Mockito.anyString()));
            mockedUtility.verify(times(1), () -> Utility.getApplications());

        }

    }

    // ETY1 - STORY 47
    @Test
    public void testApproveRejectApplication_goBack() {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        RecruiterService service = new RecruiterService();
        RecruiterService spyObject = Mockito.spy(service);


        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            Mockito.doNothing().when(spyObject).viewSpecificApplication(any());


            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("3");

            spyObject.approveRejectApplication(mockApplication);

            String consoleOutput = outputStream.toString();

            Assert.assertTrue(consoleOutput.contains("Directing to Application's Page"));



            Mockito.verify(spyObject, times(1)).viewSpecificApplication(any());
            Mockito.verify(spyObject, times(1)).approveRejectApplication(Mockito.any());
            mockedUtility.verify(times(1), () -> Utility.inputOutput(Mockito.anyString()));

        }

    }

    // ETY1 - STORY 47
    @Test
    public void testApproveRejectApplication_invalid() {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        RecruiterService service = new RecruiterService();
        RecruiterService spyObject = Mockito.spy(service);


        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {


            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("invalid");

            Mockito.doCallRealMethod().doNothing().when(spyObject).approveRejectApplication(any());

            spyObject.approveRejectApplication(mockApplication);

            String consoleOutput = outputStream.toString();

            Assert.assertTrue(consoleOutput.contains("You entered invalid option"));

            Mockito.verify(spyObject, times(2)).approveRejectApplication(Mockito.any());
            mockedUtility.verify(times(1), () -> Utility.inputOutput(Mockito.anyString()));

        }

    }

    // ETY1 - STORY 5
    @Test
    public void recruiterSignUpTest_Successful() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        RecruiterService service = new RecruiterService();
        RecruiterService spyObject = Mockito.spy(service);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            Mockito.doNothing().when(spyObject).viewRecruiterDashboard();

            mockedUtility.when(Utility::getUsers).thenReturn(mockUsers);
            Recruiter recruiter=new Recruiter("newId","name", "surname", "username", "password");


            spyObject.recruiterSignUp("XVQTY", recruiter.getName(),recruiter.getLastName(),recruiter.getUserName(),recruiter.getPassword());
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Sign Up Successful for Recruiter"));

            Mockito.verify(spyObject, times(1)).viewRecruiterDashboard();
            mockedUtility.verify(times(1), () -> Utility.getUsers());
        }
    }
    // ETY1 - STORY 5
    @Test
    public void recruiterSignUpTest_Invalid() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        RecruiterService service = new RecruiterService();
        RecruiterService spyObject = Mockito.spy(service);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            Mockito.doNothing().when(spyObject).viewRecruiterSignUpPage();

            Recruiter recruiter=new Recruiter("newId","name", "surname", "username", "password");

            spyObject.recruiterSignUp("12345", recruiter.getName(),recruiter.getLastName(),recruiter.getUserName(),recruiter.getPassword());

            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Invalid Attempt"));

            Mockito.verify(spyObject, times(1)).viewRecruiterSignUpPage();
        }
    }

    // ETY1 - STORY 42
    @Test
    public void viewAssessmentResultTest_assessmentFound() {
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



            Mockito.verify(spyObject, times(1)).viewSpecificApplication(any());
            mockedUtility.verify(times(1), () -> Utility.getApplications());

        }

    }

    // ETY1 - STORY 42
    @Test
    public void viewAssessmentResultTest_assessmentNotFound() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        RecruiterService service = new RecruiterService();
        RecruiterService spyObject = Mockito.spy(service);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {

            mockedUtility.when(Utility::getApplications).thenReturn(mockApplications);

            Mockito.doNothing().when(spyObject).viewSpecificApplication(any());

            spyObject.viewAssessmentResult("App1", "A3");

            String consoleOutput = outputStream.toString();

            Assert.assertTrue(consoleOutput.contains("There is no Coding Assessment Result for this application"));
            Assert.assertTrue(consoleOutput.contains("Directing to Application Page"));

            Mockito.verify(spyObject, times(1)).viewSpecificApplication(any());
            mockedUtility.verify(times(1), () -> Utility.getApplications());
        }

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
     * Author: Mohammad Ansar Patil (map66)
     * User Story: 46
     */
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

    /*
     * Author: Mohammad Ansar Patil (map66)
     * User Story: 19
     */
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

    /*
     * Author: Mohammad Ansar Patil (map66)
     * User Story: 31
     */
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

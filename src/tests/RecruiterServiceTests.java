package tests;

import model.Application;
import model.ApplicationStatus;
import model.Assignment;
import model.Job;
import model.JobStatus;
import model.User;
import model.UserRole;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import service.ApplicantService;
import service.RecruiterService;
import utility.Utility;

import static org.mockito.Mockito.times;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class RecruiterServiceTests {

    public RecruiterService service = new RecruiterService();
    private ByteArrayOutputStream outputStream;

    public RecruiterServiceTests() {
    }

    @Before
    public void setUp() {
        ArrayList<Job> jobs = new ArrayList<>();
        jobs.add(new Job("1", "Software Engineer", "Develop software", JobStatus.PUBLIC));
        jobs.add(new Job("2", "Data Analyst", "Analyze data", JobStatus.PUBLIC));
        jobs.add(new Job("3", "Product Manager", "Manage products", JobStatus.PRIVATE));
        Utility.setJobs(jobs);

    }

     @Test
    public void viewRecruiterProfilePageTest() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        RecruiterService spyObject = Mockito.spy(service);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {

            Mockito.doNothing().when(spyObject).updateRecruiterProfile();;
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
    public void submitNewJobPost() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        service.submitNewJobPost("Software Engineer", "Develop software");
        String consoleOutput = outputStream.toString();
        Assert.assertTrue(consoleOutput.contains("Job posted successfully"));
        Assert.assertEquals(4, Utility.getJobs().size());
    }

    public void updateStatusOfJobPost_Empty() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        Clearing the jobs
        Utility.setJobs(new ArrayList<>());
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        service.updateStatusOfJobPost("1");
        String consoleOutput = outputStream.toString();
        Assert.assertTrue(consoleOutput.contains("No jobs available"));
    }

    @Test
    public void updateStatusOfJobPost_JobNotAvailable() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        service.updateStatusOfJobPost("5");
        String consoleOutput = outputStream.toString();
        Assert.assertTrue(consoleOutput.contains("No job post available with given id"));
    }

    @Test
    public void updateStatusOfJobPost_Success() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        service.updateStatusOfJobPost("1");
        String consoleOutput = outputStream.toString();
        Assert.assertTrue(consoleOutput.contains("Status of job updated successfully"));
        Job job = Utility.getJobs().stream().filter(j -> j.getId().equals("1")).findFirst().orElse(null);
        assert job != null;
        Assert.assertEquals(JobStatus.PRIVATE, job.getJobStatus());
    }

    @Test
    public void viewSpecificJobPostTest() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
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
            Assert.assertTrue(consoleOutput.contains("Redirecting to total applications for the job"));
            Mockito.verify(spyObject, Mockito.times(1)).updateStatusOfJobPost("101");
            outputStream.reset();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("101", "3", "2");
            spyObject.viewSpecificJobPost();
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Redirecting to dashboard"));
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
            Mockito.verify(spyObject, Mockito.times(5)).viewRecruiterDashboard();
            outputStream.reset();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("101", "invalid");
            spyObject.viewSpecificJobPost();
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("You entered invalid option"));
            Mockito.verify(spyObject, Mockito.times(7)).viewRecruiterDashboard();
            outputStream.reset();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("101", "1", "1", "102", "4");
            spyObject.viewSpecificJobPost();
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Redirecting to view specific job details"));
            Assert.assertTrue(consoleOutput.contains("Job ID: 102"));
            Assert.assertTrue(consoleOutput.contains("Redirecting to main menu"));
            outputStream.reset();

            Mockito.verify(spyObject, Mockito.times(8)).viewSpecificJobPost();
            Mockito.verify(spyObject, Mockito.times(2)).updateDescriptionOfJobPost("101");
            Mockito.verify(spyObject, Mockito.times(1)).updateStatusOfJobPost("101");
            Mockito.verify(spyObject, Mockito.times(1)).viewTotalNumberOfApplications("101");
            Mockito.verify(spyObject, Mockito.times(8)).viewRecruiterDashboard();
            mockedUtility.verify(Mockito.times(23), () -> Utility.inputOutput(Mockito.anyString()));
        }
    }
    @Test
    public void viewAssessmentResultTest() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        RecruiterService spyObject = Mockito.spy(service);
        
        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            ArrayList<Application> mockApplications = new ArrayList<>();
            mockApplications.add(new Application("App1", "Job1", "User1", "UnderConsideration", null));
            mockApplications.add(new Application("App2", "Job2", "User2", "UnderConsideration", null));

            ArrayList<Assignment> mockAssignments = new ArrayList<>();
            
            ArrayList<String> questions=new ArrayList<String>();
            questions.add("Q1");
            questions.add("Q2");
            ArrayList<String> answers=new ArrayList<String>();
            answers.add("ANS1");
            answers.add("ANS2");
            
            mockAssignments.add(new Assignment("A1","App1","Coding Assessment",questions,answers));
            mockAssignments.add(new Assignment("A2","App1","Interview",questions,answers));
            
            mockApplications.get(0).setAssignments(mockAssignments);


            mockedUtility.when(Utility::getApplications).thenReturn(mockApplications);

            Mockito.doNothing().when(spyObject).viewSpecificApplication();
            
            spyObject.viewAssessmentResult("App1", "A1");
            
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Assessment Found"));
            Assert.assertTrue(consoleOutput.contains("Q1"));

            outputStream.reset();

            spyObject.viewAssessmentResult("App1", "A3");
            
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("There is no Coding Assessment Result for this application"));
            Assert.assertTrue(consoleOutput.contains("Directing to Application Page"));

            Mockito.verify(spyObject,times(2)).viewSpecificApplication();
            mockedUtility.verify(times(2),() -> Utility.getApplications());

        }
        
    }

    @Test
    public void viewSubmittedAnswersTest() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        RecruiterService spyObject = Mockito.spy(service);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {

            ArrayList<Application> mockApplications = new ArrayList<>();
            ArrayList<String> mockQuestions1 = new ArrayList<>(List.of("What is Java?", "Explain OOP concepts."));
            ArrayList<String> mockAnswers1 = new ArrayList<>(List.of("Java is a programming language.", "OOP includes Encapsulation, Polymorphism."));
            ArrayList<String> mockQuestions2 = new ArrayList<>(List.of("What is your experience?"));
            ArrayList<String> mockAnswers2 = new ArrayList<>(List.of("I have 5 years of experience in software development."));
            
            mockApplications.add(new Application("A101", "J101", "U101", ApplicationStatus.INPROGRESS,
                new ArrayList<>(List.of(new Assignment("Assign1", "U101", "Assignment 1", mockQuestions1, mockAnswers1))), null));
            mockApplications.add(new Application("A102", "J102", "U102", ApplicationStatus.SUCCESSFUL,
                new ArrayList<>(List.of(new Assignment("Assign2", "U102", "Assignment 2", mockQuestions2, mockAnswers2))), null));
            mockApplications.add(new Application("A103", "J103", "U101", ApplicationStatus.UNSUCCESSFUL,
                new ArrayList<>(), null));

            mockedUtility.when(Utility::getApplications).thenReturn(mockApplications);

            spyObject.viewSubmittedAnswers("A101");
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Welcome to view submitted answers for the application A101"));
            Assert.assertTrue(consoleOutput.contains("Question:"));
            Assert.assertTrue(consoleOutput.contains("What is Java?"));
            Assert.assertTrue(consoleOutput.contains("Explain OOP concepts."));
            Assert.assertTrue(consoleOutput.contains("Answer:"));
            Assert.assertTrue(consoleOutput.contains("Java is a programming language."));
            Assert.assertTrue(consoleOutput.contains("OOP includes Encapsulation, Polymorphism."));
            outputStream.reset();

            spyObject.viewSubmittedAnswers("A103");
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Welcome to view submitted answers for the application A103"));
            Assert.assertFalse(consoleOutput.contains("Question:"));
            Assert.assertFalse(consoleOutput.contains("Answer:"));
            outputStream.reset();

            spyObject.viewSubmittedAnswers("A102");
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Welcome to view submitted answers for the application A102"));
            Assert.assertTrue(consoleOutput.contains("Question:"));
            Assert.assertTrue(consoleOutput.contains("What is your experience?"));
            Assert.assertTrue(consoleOutput.contains("Answer:"));
            Assert.assertTrue(consoleOutput.contains("I have 5 years of experience in software development."));
            outputStream.reset();

            spyObject.viewSubmittedAnswers("A999");
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Welcome to view submitted answers for the application A999"));
            Assert.assertFalse(consoleOutput.contains("Question:"));
            Assert.assertFalse(consoleOutput.contains("Answer:"));
            outputStream.reset();

            Mockito.verify(spyObject, Mockito.times(4)).viewSubmittedAnswers(Mockito.anyString());
            mockedUtility.verify(Mockito.times(4), Utility::getApplications);
        }
    }

}

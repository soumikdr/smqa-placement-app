package tests;

import model.Application;
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

import service.RecruiterService;
import utility.Utility;

import static org.mockito.Mockito.times;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

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

            Mockito.doNothing().when(spyObject).viewRecruiterDashboard();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("101", "2");
            spyObject.viewSpecificJobPost();
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Job ID: 101"));
            Assert.assertTrue(consoleOutput.contains("Job Name: Software Engineer"));
            Assert.assertTrue(consoleOutput.contains("Redirecting to dashboard"));
            outputStream.reset();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("999", "2");
            spyObject.viewSpecificJobPost();
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("You have entered a invalid Job id"));
            Assert.assertTrue(consoleOutput.contains("Redirecting to dashboard"));
            outputStream.reset();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("101", "1", "102", "2");
            spyObject.viewSpecificJobPost();
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Job ID: 101"));
            Assert.assertTrue(consoleOutput.contains("Redirecting to view specific job details"));
            Assert.assertTrue(consoleOutput.contains("Job ID: 102"));
            Assert.assertTrue(consoleOutput.contains("Redirecting to dashboard"));
            outputStream.reset();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("101", "2");
            spyObject.viewSpecificJobPost();
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Redirecting to dashboard"));

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("101", "invalid");
            spyObject.viewSpecificJobPost();
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("You entered invalid option"));

            Mockito.verify(spyObject, Mockito.times(6)).viewSpecificJobPost();
            Mockito.verify(spyObject, Mockito.times(5)).viewRecruiterDashboard();
            mockedUtility.verify(Mockito.times(12), () -> Utility.inputOutput(Mockito.anyString()));
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
}

package tests;

import model.Job;
import model.JobStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import service.RecruiterService;
import utility.Utility;

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

        String simulatedInput = "1";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        service.viewRecruiterProfilePage();
        String consoleOutput = outputStream.toString();
        Assert.assertTrue(consoleOutput.contains("Welcome to Update profile page"));

        outputStream.reset();

        simulatedInput = "2";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        service.viewRecruiterProfilePage();
        consoleOutput = outputStream.toString();
        Assert.assertTrue(consoleOutput.contains("Welcome to Delete profile page"));

        outputStream.reset();

        // simulatedInput = "invalid input";
        // System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        // simulatedInput = "1";
        // System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        // service.viewRecruiterProfilePage();
        // consoleOutput = outputStream.toString();
        // Assert.assertTrue(consoleOutput.contains("You entered invalid option"));

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
    
    @Test
    public void viewAvailableJobs_Empty() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out

        service.viewAvailableJobs();
        String consoleOutput = outputStream.toString();
        Assert.assertTrue(consoleOutput.contains("No jobs available"));
    }

    @Test
    public void viewAvailableJobs_NotEmpty() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        ArrayList<Job> jobs = new ArrayList<>();
        jobs.add(new Job("1", "Software Engineer", "Develop software", JobStatus.PUBLIC));
        jobs.add(new Job("2", "Data Analyst", "Analyze data", JobStatus.PUBLIC));
        jobs.add(new Job("3", "Product Manager", "Manage products", JobStatus.PUBLIC));
        Utility.setJobs(jobs);
        String expectedOutput = """
                Available Jobs\r
                1. Job ID: 1 | Job Title: Software Engineer\r
                2. Job ID: 2 | Job Title: Data Analyst\r
                3. Job ID: 3 | Job Title: Product Manager\r
                """;

        service.viewAvailableJobs();
        String consoleOutput = outputStream.toString();
        Assert.assertTrue(consoleOutput.contains(expectedOutput));
    }

    @Test
    public void updateStatusOfJobPost_Empty() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //        Clearing the jobs
        Utility.setJobs(new ArrayList<>());
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        service.updateStatusOfJobPost("1");
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
}

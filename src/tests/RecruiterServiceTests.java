package tests;

import model.Job;
import org.junit.Assert;
import org.junit.Test;
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

    public RecruiterServiceTests(){
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
        jobs.add(new Job("1", "Software Engineer", "Develop software", "Open"));
        jobs.add(new Job("2", "Data Analyst", "Analyze data", "Open"));
        jobs.add(new Job("3", "Product Manager", "Manage products", "Open"));
        Utility.setJobs(jobs);
        String expectedOutput = """
                Available Jobs\r
                -----------------------------------\r
                Job ID: 1\r
                Job Title: Software Engineer\r
                Job Description: Develop software\r
                Job Status: Open\r
                -----------------------------------\r
                Job ID: 2\r
                Job Title: Data Analyst\r
                Job Description: Analyze data\r
                Job Status: Open\r
                -----------------------------------\r
                Job ID: 3\r
                Job Title: Product Manager\r
                Job Description: Manage products\r
                Job Status: Open\r
                -----------------------------------\r
                """;

        service.viewAvailableJobs();
        String consoleOutput = outputStream.toString();
        Assert.assertTrue(consoleOutput.contains(expectedOutput));
    }
}

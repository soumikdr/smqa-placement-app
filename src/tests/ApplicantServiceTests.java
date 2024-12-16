package tests;

import model.Applicant;
import model.Application;
import model.Job;
import model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import service.ApplicantService;
import utility.Utility;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockedStatic;

public class ApplicantServiceTests {

    public ApplicantService service = ApplicantService.getInstance();
    @Before
    public void setUp() {
        ArrayList<User> users = new ArrayList<>();

        users.add(new User("1", "John", "Doe", "johnDoe", "bestpassword", "Applicant"));
        users.add(new User("2", "Ansar", "Patil", "darkAngel", "123qwe", "Recruiter"));
        users.add(new User("3", "Jane", "Doe", "janeDoe", "bestpassword", ""));
        Utility.setUsers(users);
    }

    @Test
    public void accessApplicantDashboardTest() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out


        String simulatedInput = "1";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        service.viewApplicantDashboard();
        String consoleOutput = outputStream.toString();
        Assert.assertTrue(consoleOutput.contains("Directing to Profile Page..."));

        outputStream.reset();

        simulatedInput = "2";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        service.viewApplicantDashboard();
        consoleOutput = outputStream.toString();
        Assert.assertTrue(consoleOutput.contains("Directing to Applications Page..."));

        outputStream.reset();

        simulatedInput = "3";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        service.viewApplicantDashboard();
        consoleOutput = outputStream.toString();
        Assert.assertTrue(consoleOutput.contains("Directing to Available Jobs Page..."));

        outputStream.reset();

        simulatedInput = "4";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        service.viewApplicantDashboard();
        consoleOutput = outputStream.toString();
        Assert.assertTrue(consoleOutput.contains("Logging Out..."));

//        outputStream.reset();
//
//        simulatedInput = "invalid input";
//        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
//
//        service.viewApplicantDashboard();
//        consoleOutput = outputStream.toString();
//        Assert.assertTrue(consoleOutput.contains("You entered invalid option"));

    }

    @Test
    public void viewAllAvailableJobsTest() throws IOException {
        ArrayList<Application> applications = new ArrayList<>();
        Utility.setCurrentUser(new Applicant("1", "John", "Doe", "johnDoe", "bestpassword",applications));
        ArrayList<Job> jobs = new ArrayList<>();
        jobs.add(new Job("1", "Software Engineer", "Knowledge of DSA and programming languages", "Public"));
        jobs.add(new Job("2", "Data Analyst", "Knowledge of data analysis and visualization", "Public"));
        Utility.setJobs(jobs);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        service.viewAllAvailableJobs();
        String consoleOutput = outputStream.toString();
        Assert.assertTrue(consoleOutput.contains("Job ID: 1"));
        Assert.assertTrue(consoleOutput.contains("Job Name: Software Engineer"));
        Assert.assertTrue(consoleOutput.contains("Job Description: Knowledge of DSA and programming languages"));
        Assert.assertTrue(consoleOutput.contains("Job Status: Public"));
        Assert.assertTrue(consoleOutput.contains("Job ID: 2"));
        Assert.assertTrue(consoleOutput.contains("Job Name: Data Analyst"));
        Assert.assertTrue(consoleOutput.contains("Job Description: Knowledge of data analysis and visualization"));
        Assert.assertTrue(consoleOutput.contains("Job Status: Public"));
    }

    @Test
    public void viewAllAvailableJobTest() {
        ArrayList<Application> applications = new ArrayList<>();
        Applicant mockApplicant = new Applicant("1", "John", "Doe", "johnDoe", "bestpassword", applications);
        try (MockedStatic<Utility> mockedUtility = mockStatic(Utility.class)) {
            mockedUtility.when(Utility::getCurrentUser).thenReturn(mockApplicant);

            List<Job> mockJobs = new ArrayList<>();
            mockJobs.add(new Job("1", "Software Engineer", "Develop and maintain software", "Public"));
            mockJobs.add(new Job("2", "Data Analyst", "Analyze and interpret complex data", "Private"));
            mockedUtility.when(Utility::getJobs).thenReturn(mockJobs);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outputStream));

            service.viewAllAvailableJobs();
           mockedUtility.verify(Utility::getJobs, times(1));

            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Job ID: 1"));
            Assert.assertTrue(consoleOutput.contains("Job Name: Software Engineer"));
            Assert.assertTrue(consoleOutput.contains("Job Description: Develop and maintain software"));
            Assert.assertTrue(consoleOutput.contains("Job Status: Public"));
            Assert.assertFalse(consoleOutput.contains("No jobs available at the moment"));
            Assert.assertFalse(consoleOutput.contains("Job ID: 2"));
            Assert.assertFalse(consoleOutput.contains("Job Name: Data Analyst"));
        }
    }
}


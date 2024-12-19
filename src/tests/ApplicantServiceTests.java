package tests;

import model.*;
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

public class ApplicantServiceTests {

    public ApplicantService service = ApplicantService.getInstance();

    @Before
    public void setUp() {
        ArrayList<User> users = new ArrayList<>();

        users.add(new Applicant("1", "John", "Doe", "johnDoe", "bestpassword", new ArrayList<>()));
        users.add(new Recruiter("2", "Ansar", "Patil", "darkAngel", "123qwe"));
        users.add(new Applicant("3", "Jane", "Doe", "janeDoe", "bestpassword", new ArrayList<>()));
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
    public void viewApplicantApplications_NotApplicant() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out

        Utility.setCurrentUser(new Recruiter("test", "test", "test", "testusername", "password"));

        service.viewApplicantApplications();
        String consoleOutput = outputStream.toString();
        Assert.assertTrue(consoleOutput.contains("You are not an applicant"));
    }

    @Test
    public void viewApplicantProfilePage_Empty() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        ArrayList<Application> applications = new ArrayList<>();
        Utility.setCurrentUser(new Applicant("test", "test", "test", "testusername", "password", applications));

        service.viewApplicantApplications();
        String consoleOutput = outputStream.toString();
        Assert.assertTrue(consoleOutput.contains("No applications found."));
    }

    @Test
    public void viewApplicantProfilePage_ViewAll() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        ArrayList<Application> applications = new ArrayList<>();
        ArrayList<Job> jobs = new ArrayList<>();
        jobs.add(new Job("1", "Software Engineer", "Develop software", "Open"));
        jobs.add(new Job("2", "Data Analyst", "Analyze data", "Open"));
        jobs.add(new Job("3", "Product Manager", "Manage products", "Open"));
        Utility.setJobs(jobs);

        applications.add(new Application(
                "1",
                "1",
                "testusername",
                "Pending",
                new ArrayList<Assignment>()
        ));
        applications.add(new Application(
                "2",
                "2",
                "testusername",
                "Pending",
                new ArrayList<Assignment>()
        ));
        applications.add(new Application(
                "3",
                "4",
                "testusername",
                "Withdrawn",
                new ArrayList<Assignment>()
        ));
        Utility.setCurrentUser(new Applicant("test", "test", "test", "testusername", "password", applications));
        String expectedOutput = """
                Applications:\r
                \r
                Application ID: 1 | Status: Pending\r
                \r
                Application ID: 2 | Status: Pending\r
                \r
                Application ID: 3 | Status: Withdrawn\r
                """;
        service.viewApplicantApplications();
        String consoleOutput = outputStream.toString();
        Assert.assertTrue(consoleOutput.contains(expectedOutput));
    }
    @Test
    public void updateProfileTest() {
        User newUserProfile = Utility.getUsers().get(0);
        newUserProfile.setName("New Name");
        newUserProfile.setLastName("New Last Name");
        ApplicantService service = ApplicantService.getInstance();
        service.updateProfile(newUserProfile);
//        Check if the user profile has been updated
        Assert.assertEquals("New Name", Utility.getCurrentUser().getName());
        Assert.assertEquals("New Last Name", Utility.getCurrentUser().getLastName());
//        Check if users list has been updated
        User filteredUser = Utility.getUsers().stream().filter(user -> user.getId().equals(newUserProfile.getId())).findFirst().orElse(null);
        Assert.assertNotNull("User not found", filteredUser);
        Assert.assertEquals("New Name", filteredUser.getName());
        Assert.assertEquals("New Last Name", filteredUser.getLastName());
        Assert.assertEquals(newUserProfile.getId(), filteredUser.getId());
    }
    @Test
    public void deleteProfileHelper() {
        ApplicantService service = ApplicantService.getInstance();
        User user = Utility.getUsers().get(0);
        Utility.setCurrentUser(user);
        int initialSize = Utility.getUsers().size();
        service.deleteProfileHelper();
        int finalSize = Utility.getUsers().size();
        Assert.assertEquals(initialSize - 1, finalSize);
//        Check if the user is deleted
        boolean isDeleted = Utility.getUsers().stream().filter(u -> u.getId().equals(user.getId())).findFirst().isEmpty();
        Assert.assertTrue(isDeleted);
    }
}

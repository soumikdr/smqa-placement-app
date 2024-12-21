package tests;

import model.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;
import service.ApplicantService;
import utility.Utility;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mockStatic;

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

    @Test
    public void viewJobPostValidTest() {
        ArrayList<Application> applications = new ArrayList<>();
        Applicant applicant = new Applicant("1", "John", "Doe", "johnDoe", "password", applications);

        try (MockedStatic<Utility> mockedUtility = mockStatic(Utility.class)) {
            mockedUtility.when(Utility::getCurrentUser).thenReturn(applicant);
            mockedUtility.when(() -> Utility.inputOutput("Enter the Job ID to view the details:")).thenReturn("1");
            List<Job> mockJobs = new ArrayList<>();
            mockJobs.add(new Job("1", "Software Engineer", "Develop and maintain software", JobStatus.PUBLIC));
            mockedUtility.when(Utility::getJobs).thenReturn(mockJobs);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outputStream));
            service.viewJobPost();
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Job ID: 1"));
            Assert.assertTrue(consoleOutput.contains("Job Title: Software Engineer"));
            Assert.assertTrue(consoleOutput.contains("Job Description: Develop and maintain software"));
            Assert.assertTrue(consoleOutput.contains("Job Status: PUBLIC"));
        }
    }
    // @Test
    // public void viewJobPostInvalidTest() {
    //     try (MockedStatic<Utility> mockedUtility = mockStatic(Utility.class)) {
    //         Applicant mockApplicant = new Applicant("1", "John", "Doe", "johnDoe", "password", new ArrayList<>());
    //         mockedUtility.when(Utility::getCurrentUser).thenReturn(mockApplicant);
    //         mockedUtility.when(() -> Utility.inputOutput("Enter the Job ID to view the details:")).thenReturn("999");
    //         List<Job> mockJobs = new ArrayList<>();
    //         mockJobs.add(new Job("1", "Software Engineer", "Develop and maintain software", JobStatus.PUBLIC));
    //         mockedUtility.when(Utility::getJobs).thenReturn(mockJobs);
    //         ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    //         System.setOut(new PrintStream(outputStream));
    //         service.viewJobPost();
    //         mockedUtility.verify(Utility::getCurrentUser, times(1));
    //         mockedUtility.verify(() -> Utility.inputOutput("Enter the Job ID to view the details:"), times(1));
    //         mockedUtility.verify(Utility::getJobs, times(1));
    //         String consoleOutput = outputStream.toString();
    //         assertTrue(consoleOutput.contains("Job with ID 999 not found."));
    //     }
    // }

    @Test
    public void viewJobDescFromApplication_InvalidJob() {
        ArrayList<Job> jobs = new ArrayList<>();
        jobs.add(new Job("1", "Software Engineer", "Develop and maintain software", JobStatus.PUBLIC));
        Utility.setJobs(jobs);
        String jobId = "2";
        Application application = new Application("1", jobId, "3", "PENDING", new ArrayList<>());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        service.viewJobDescFromApplication(application);

        String consoleOutput = outputStream.toString();
        Assert.assertTrue(consoleOutput.contains("Job with ID " + jobId + " not found."));
    }

    @Test
    public void viewJobDescFromApplication_ValidJob() {
        ArrayList<Job> jobs = new ArrayList<>();
        jobs.add(new Job("1", "Software Engineer", "Develop and maintain software", JobStatus.PUBLIC));
        Utility.setJobs(jobs);
        String jobId = "1";
        Application application = new Application("1", jobId, "3", "PENDING", new ArrayList<>());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        service.viewJobDescFromApplication(application);

        String consoleOutput = outputStream.toString();

        Assert.assertTrue(consoleOutput.contains("Job ID: " + jobId));
        Assert.assertTrue(consoleOutput.contains("Job Title: Software Engineer"));
        Assert.assertTrue(consoleOutput.contains("Job Description: Develop and maintain software"));
    }
}



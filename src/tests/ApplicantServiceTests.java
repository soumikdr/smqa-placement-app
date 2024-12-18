package tests;

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

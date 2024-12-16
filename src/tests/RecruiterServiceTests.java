package tests;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import model.Application;
import model.Assignment;
import model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.mockito.MockedStatic;
import service.RecruiterService;
import utility.Utility;

import static org.mockito.Mockito.mockStatic;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class RecruiterServiceTests {

    public RecruiterService service = new RecruiterService();
    private ByteArrayOutputStream outputStream;

    public RecruiterServiceTests() {
    }
    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
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
    public void viewAllOpenApplicationsTest() {
        try (MockedStatic<Utility> mockedUtility = mockStatic(Utility.class)) {
            List<Application> mockApplications = new ArrayList<>();
            ArrayList<Assignment> mockAssignments = new ArrayList<>();
            mockAssignments.add(new Assignment("1","1","DSA", new ArrayList<>(),new ArrayList<>()));
            mockApplications.add(new Application("1", "1", "John Doe", "Open", mockAssignments));
            mockApplications.add(new Application("2", "2", "Jane Doe", "Closed", mockAssignments));

            User mockUser = new User("1", "John", "Doe", "johnDoe", "bestpassword", "Recruiter");

            mockedUtility.when(Utility::getCurrentUser).thenReturn(mockUser);
            mockedUtility.when(Utility::getApplications).thenReturn(mockApplications);

            service.viewAllApplications();

            mockedUtility.verify(Utility::getApplications, times(1));

            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Application ID: 1"));
            Assert.assertTrue(consoleOutput.contains("Job Id: 1"));
            Assert.assertTrue(consoleOutput.contains("Status: Open"));
            Assert.assertTrue(consoleOutput.contains("Applicant ID: John Doe"));
            Assert.assertFalse(consoleOutput.contains("Application ID: 2"));
            Assert.assertFalse(consoleOutput.contains("Job Id: 2"));
            Assert.assertFalse(consoleOutput.contains("Status: Closed"));
        }
    }

}

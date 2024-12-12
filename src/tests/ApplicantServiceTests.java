package tests;

import org.junit.Assert;
import org.junit.Test;
import service.ApplicantService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class ApplicantServiceTests {

    public ApplicantService service = ApplicantService.getInstance();


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
}

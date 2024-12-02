package tests;

import org.junit.Assert;
import org.junit.Test;
import service.CommonService;

import java.io.*;

public class CommonServiceTests {

    public CommonService service = new CommonService();
    private ByteArrayOutputStream outputStream;

    public CommonServiceTests(){
    }

    @Test
    public void accessLandingPageTest() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out


        String simulatedInput = "1";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        service.accessLandingPage();
        String consoleOutput = outputStream.toString();
        Assert.assertTrue(consoleOutput.contains("directing to Sign In Page"));

        outputStream.reset();

        simulatedInput = "2";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        service.accessLandingPage();
        consoleOutput = outputStream.toString();
        Assert.assertTrue(consoleOutput.contains("directing to Sign Up Page"));

        outputStream.reset();

        simulatedInput = "invalid input";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        simulatedInput = "1";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        service.accessLandingPage();
        consoleOutput = outputStream.toString();
        Assert.assertTrue(consoleOutput.contains("You entered invalid option"));

    }
}

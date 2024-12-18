package tests;

import static org.mockito.Mockito.times;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import model.Application;
import service.CommonService;
import service.RecruiterService;
import utility.Utility;

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
    public void viewTotalNumberOfApplicationsTest() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out

        try(MockedStatic<Utility> mockedUtility=Mockito.mockStatic(Utility.class)){
        	

            ArrayList<Application> apps=new ArrayList<>();
            
            Application app=new Application();
            
            app.setJobId("JOB1");
            
            apps.add(app);
        	
            mockedUtility.when(()->Utility.getApplications()).thenReturn(apps);
            
            String consoleOutput = outputStream.toString();

            
            Assert.assertFalse(consoleOutput.contains("Total Applications of : JOB1 is 1"));

            service.viewTotalNumberOfApplications("JOB1");

            consoleOutput = outputStream.toString();

            Assert.assertTrue(consoleOutput.contains("Total Applications of : JOB1 is 1"));
        
            mockedUtility.verify(()->Utility.getApplications());

            
          }
        }

}

package tests;

import model.Application;
import model.Assignment;
import model.Job;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import service.CommonService;
import service.RecruiterService;
import utility.Utility;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

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

    @Before
    public void setUp() {
        ArrayList<Job> jobs = new ArrayList<>();
        jobs.add(new Job("1", "Software Engineer", "Develop software", "Private"));
        jobs.add(new Job("2", "Data Analyst", "Analyze data", "Private"));
        jobs.add(new Job("3", "Product Manager", "Manage products", "Private"));
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
    public void testApproveRejectApplication() {
    	
        Application application=new Application("1", "Job1", "User1", "UnderConsideration", null);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        RecruiterService spyObject = Mockito.spy(service);
        ArrayList<Application> apps=new ArrayList<Application>();
        apps.add(application);

        
        try(MockedStatic<Utility> mockedUtility=Mockito.mockStatic(Utility.class)){
        	
        	Mockito.doNothing().when(spyObject).viewSpecificApplication();
        	
            mockedUtility.when(()->Utility.inputOutput(Mockito.anyString())).thenReturn("1");
            mockedUtility.when(()->Utility.getApplications()).thenReturn(apps);


            spyObject.approveRejectApplication(application);

            String consoleOutput = outputStream.toString();

            Assert.assertTrue(consoleOutput.contains("Application Approved"));

            outputStream.reset();

            mockedUtility.when(()->Utility.inputOutput(Mockito.anyString())).thenReturn("2");

            spyObject.approveRejectApplication(application);
            
            consoleOutput = outputStream.toString();

            Assert.assertTrue(consoleOutput.contains("Application Rejected"));

            outputStream.reset();
            
            mockedUtility.when(()->Utility.inputOutput(Mockito.anyString())).thenReturn("3");

            spyObject.approveRejectApplication(application);
            
            consoleOutput = outputStream.toString();

            Assert.assertTrue(consoleOutput.contains("Directing to Application's Page"));

            outputStream.reset();

            mockedUtility.when(()->Utility.inputOutput(Mockito.anyString())).thenReturn("invalid");

        	Mockito.doCallRealMethod().doNothing().when(spyObject).approveRejectApplication(any());
            
            spyObject.approveRejectApplication(application);
            
            consoleOutput = outputStream.toString();

            Assert.assertTrue(consoleOutput.contains("You entered invalid option"));
            
            Mockito.verify(spyObject,times(3)).viewSpecificApplication();
            Mockito.verify(spyObject,times(5)).approveRejectApplication(Mockito.any());
            mockedUtility.verify(times(4),()->Utility.inputOutput(Mockito.anyString()));
            mockedUtility.verify(times(2),()->Utility.getApplications());
            
            
          }

    	
    }
    

    @Test
    public void updateStatusOfJobPost_Empty() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        Clearing the jobs
        Utility.setJobs(new ArrayList<>());
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        service.updateStatusOfJobPost("1");
        String consoleOutput = outputStream.toString();
        Assert.assertTrue(consoleOutput.contains("No jobs available"));
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
        Assert.assertEquals("Public", job.getJobStatus());
    }
}

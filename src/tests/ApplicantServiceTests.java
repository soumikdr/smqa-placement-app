package tests;

import model.Application;
import model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import service.ApplicantService;
import service.CommonService;
import utility.Utility;

import static org.mockito.Mockito.times;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.UUID;

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
    public void testSubmitApplicationForm() throws IOException {
    	
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
    	String jobId="Job1";
    	String applicantId="1";
        
    	ApplicantService spyObject = Mockito.spy(service);

    	ArrayList<Application> apps=new ArrayList<Application>();
    	
    	
        try(MockedStatic<Utility> mockedUtility=Mockito.mockStatic(Utility.class)){
        	

            mockedUtility.when(()->Utility.getApplications()).thenReturn(apps);

        	
        	Mockito.doNothing().when(spyObject).viewApplicantDashboard();
            
        	int totalApplications= apps.size();
        	
        	spyObject.submitApplicationForm(jobId,applicantId);

        	int newTotalApplications= apps.size();

            String consoleOutput = outputStream.toString();

            Assert.assertTrue(consoleOutput.contains("Application submitted"));
            Assert.assertEquals(totalApplications+1,newTotalApplications);

            Mockito.verify(spyObject).viewApplicantDashboard();

            mockedUtility.verify(()->Utility.getApplications());


          }
    	

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

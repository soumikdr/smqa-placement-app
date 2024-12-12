package tests;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

import model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import service.CommonService;
import service.RecruiterService;
import utility.Utility;

public class RecruiterServiceTests {

     public RecruiterService service = new RecruiterService();
    private ByteArrayOutputStream outputStream;

    public RecruiterServiceTests(){
    }

    @Before
    public void setUp(){
        ArrayList<User> users=new ArrayList<>();
        users.add(new User("1","John","Doe","johnDoe","bestpassword","Applicant"));
        users.add(new User("2","Ansar","Patil","darkAngel","123qwe","Recruiter"));

        Utility.setUsers(users);
    }
    @Test
    public  void logoutTest() {
        setUp();
//        Simulate user log in
        Utility.setCurrentUser(Utility.getUsers().get(1));
        CommonService commonService = new CommonService();
        commonService.logOut();
        Assert.assertNull(Utility.getCurrentUser());
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
}

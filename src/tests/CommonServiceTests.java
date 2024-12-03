package tests;

import com.sun.tools.javac.Main;
import model.Recruiter;
import model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import model.User;
import service.CommonService;
import utility.Utility;

import java.io.*;
import java.util.ArrayList;
import java.util.UUID;

public class CommonServiceTests {

    public CommonService service = new CommonService();

    public CommonServiceTests(){
    }
    @Before
    public void setUp(){
        ArrayList<User> users=new ArrayList<>();

        users.add(new User("1","John","Doe","johnDoe","bestpassword","Applicant"));
        users.add(new User("2","Ansar","Patil","darkAngel","123qwe","Recruiter"));

        Utility.setUsers(users);
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

//        simulatedInput = "invalid input";
//        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
//        simulatedInput = "1";
//        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
//
//        service.accessLandingPage();
//        consoleOutput = outputStream.toString();
//        Assert.assertTrue(consoleOutput.contains("You entered invalid option"));

    }

    @Test
    public void viewSignUpPageTest() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out


        String simulatedInput = "1";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        service.viewSignUpPage();
        String consoleOutput = outputStream.toString();
        Assert.assertTrue(consoleOutput.contains("Welcome to Applicant Signup page"));

        outputStream.reset();

        simulatedInput = "2";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        service.viewSignUpPage();
        consoleOutput = outputStream.toString();
        Assert.assertTrue(consoleOutput.contains("Welcome to Recruiter Signup page"));

        outputStream.reset();

        // simulatedInput = "invalid input";
        // System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        // simulatedInput = "1";
        // System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        // service.accessLandingPage();
        // consoleOutput = outputStream.toString();
        // Assert.assertTrue(consoleOutput.contains("You entered invalid option"));

    }

    @Test
    public void viewSignInPageTest() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out


        String simulatedInput = "1";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        service.viewSignInPage();
        String consoleOutput = outputStream.toString();
        Assert.assertTrue(consoleOutput.contains("Welcome to Signin page"));

        outputStream.reset();

        simulatedInput = "2";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        service.viewSignInPage();
        consoleOutput = outputStream.toString();
        Assert.assertTrue(consoleOutput.contains("Rediredting to Landing Page"));

        outputStream.reset();

        // simulatedInput = "invalid input";
        // System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        // simulatedInput = "1";
        // System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        // service.accessLandingPage();
        // consoleOutput = outputStream.toString();
        // Assert.assertTrue(consoleOutput.contains("You entered invalid option"));

    }

    @Test
    public void resetPasswordTest() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out


        String simulatedInput = "1";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        service.viewResetPasswordPage();
        String consoleOutput = outputStream.toString();
        Assert.assertTrue(consoleOutput.contains("Welcome to reset password page"));

        outputStream.reset();

        simulatedInput = "2";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        service.viewResetPasswordPage();
        consoleOutput = outputStream.toString();
        Assert.assertTrue(consoleOutput.contains("Rediredting to Dashboard"));

        outputStream.reset();

        // simulatedInput = "invalid input";
        // System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        // simulatedInput = "1";
        // System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        // service.accessLandingPage();
        // consoleOutput = outputStream.toString();
        // Assert.assertTrue(consoleOutput.contains("You entered invalid option"));

    }


    @Test
    public void viewResetPasswordPageTest() throws IOException {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        
        String simulatedInput = "ansarpatil";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        service.resetPassword("ansarpatil");
        String consoleOutput = outputStream.toString();
        Assert.assertTrue(consoleOutput.contains("Your entered username:"));

        outputStream.reset();
    }

    // public void recruiterSignUpTest() throws Exception {

    //     ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    //     System.setOut(new PrintStream(outputStream)); // Redirect System.out

    //     String expectedCode="XVQTY";
    //     String id= "";
    //     // We shouldn't send id, we have to delete from cons.
    //     User expectedNewUser = new User(id,"name","lastName","userName","password","Recruiter");

    //     service.signUp(expectedNewUser,expectedCode);
    //     String consoleOutput = outputStream.toString();

    //     Assert.assertTrue(consoleOutput.contains("Sign Up Successful for Recruiter"));
    //     Assert.assertEquals(expectedNewUser.getName(),Utility.getUsers().get(Utility.getUsers().size()-1).getName());

    //     outputStream.reset();

    //     String invalidCode="EEEEE";

    //     service.signUp(expectedNewUser,invalidCode);
    //     consoleOutput = outputStream.toString();
    //     Assert.assertFalse(consoleOutput.contains("Sign Up Successful for Recruiter"));

    //     outputStream.reset();

    //     String noCode=null;

    //     service.signUp(expectedNewUser,noCode);
    //     consoleOutput = outputStream.toString();
    //     Assert.assertFalse(consoleOutput.contains("Sign Up Successful for Recruiter"));



    // }

    @Test
    public void logoutTest() throws IOException {

//        User user = new User();
//        user.setId("id");
//        user.setName("name");
//        Utility.setCurrentUser(user);
//
//        service.logOut();
//
//        Assert.assertNull(Utility.getCurrentUser());

    }


}

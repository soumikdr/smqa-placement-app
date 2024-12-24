package tests;

import model.Applicant;
import model.Recruiter;
import model.User;
import model.UserRole;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import service.ApplicantService;
import service.CommonService;
import service.RecruiterService;
import utility.Utility;

import static org.mockito.Mockito.times;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class CommonServiceTests {

    public CommonService service = CommonService.getInstance();
    public RecruiterService recruiterService = RecruiterService.getInstance();
    public ApplicantService applicantService = ApplicantService.getInstance();


    public CommonServiceTests() {
    }

    @Before
    public void setUp() {
        ArrayList<User> users = new ArrayList<>();

        users.add(new Applicant("1", "John", "Doe", "johnDoe", "bestpassword", new ArrayList<>()));
        users.add(new Recruiter("2", "Ansar", "Patil", "darkAngel", "123qwe"));
        users.add(new Applicant("3", "Jane", "Doe", "janeDoe", "bestpassword", new ArrayList<>()));
        Utility.setUsers(users);
    }
    
    




    @Test
    public void accessLandingPageTest() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        CommonService spyObject = Mockito.spy(service);

        try(MockedStatic<Utility> mockedUtility=Mockito.mockStatic(Utility.class)){
        	
        	Mockito.doNothing().when(spyObject).viewSignInPage();
        	Mockito.doNothing().when(spyObject).viewSignUpPage();

        	
            mockedUtility.when(()->Utility.inputOutput(Mockito.anyString())).thenReturn("1");

            spyObject.accessLandingPage();

            String consoleOutput = outputStream.toString();

            Assert.assertTrue(consoleOutput.contains("directing to Sign In Page"));

            outputStream.reset();

            mockedUtility.when(()->Utility.inputOutput(Mockito.anyString())).thenReturn("2");

            spyObject.accessLandingPage();
            
            consoleOutput = outputStream.toString();

            Assert.assertTrue(consoleOutput.contains("directing to Sign Up Page"));

            outputStream.reset();

            mockedUtility.when(()->Utility.inputOutput(Mockito.anyString())).thenReturn("invalid");

            Mockito.doCallRealMethod().doNothing().when(spyObject).accessLandingPage();
            
            spyObject.accessLandingPage();
            
            consoleOutput = outputStream.toString();

            Assert.assertTrue(consoleOutput.contains("You entered invalid option"));
            
            Mockito.verify(spyObject).viewSignInPage();
            Mockito.verify(spyObject).viewSignUpPage();
            Mockito.verify(spyObject,times(4)).accessLandingPage();
            mockedUtility.verify(times(3),()->Utility.inputOutput(Mockito.anyString()));
            
          }
        }

        @Test
        public void viewSignUpPageTest() throws IOException {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outputStream)); // Redirect System.out
            CommonService spyObject = Mockito.spy(service);
        
            try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {

                mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString()))
                    .thenReturn("1",
                                "John", "Doe", "johndoe", "strongpassword",
                                "2",
                                "Jane", "Smith", "janesmith", "recruiterpassword", "recruitercode",
                                "3",
                                "invalid", "1"); 
            Mockito.doNothing().when(spyObject).signUp(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
            Mockito.doNothing().when(spyObject).accessLandingPage();
                spyObject.viewSignUpPage();
                String consoleOutput = outputStream.toString();
                Assert.assertTrue(consoleOutput.contains("Welcome to Applicant Signup page"));
                outputStream.reset();
        
                spyObject.viewSignUpPage();
                consoleOutput = outputStream.toString();
                Assert.assertTrue(consoleOutput.contains("Welcome to Recruiter Signup page"));
                outputStream.reset();
        
                spyObject.viewSignUpPage();
                consoleOutput = outputStream.toString();
                Assert.assertTrue(consoleOutput.contains("Redirecting to Landing Page"));
                outputStream.reset();
        
                spyObject.viewSignUpPage();
                consoleOutput = outputStream.toString();
                Assert.assertTrue(consoleOutput.contains("You entered invalid option"));
                Assert.assertTrue(consoleOutput.contains("Welcome to Applicant Signup page"));
                outputStream.reset();
        
                Mockito.verify(spyObject, Mockito.times(1)).signUp(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
                Mockito.verify(spyObject, Mockito.times(1)).accessLandingPage();
                mockedUtility.verify(Mockito.times(18), () -> Utility.inputOutput(Mockito.anyString()));
            }
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
    public void viewResetPasswordPageTest() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        CommonService spyObject = Mockito.spy(service);
        RecruiterService recruiterSpyObject = Mockito.spy(recruiterService);
        ApplicantService applicantSpyObject = Mockito.spy(applicantService);
    
        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
    
            Mockito.doNothing().when(spyObject).resetPassword(Mockito.anyString());
            Mockito.doNothing().when(applicantSpyObject).viewApplicantDashboard();
            Mockito.doNothing().when(recruiterSpyObject).viewRecruiterDashboard();
    
            User mockUser = new User("U101", "John", "Doe", "johndoe", "bestpassword", UserRole.RECRUITER);
            mockedUtility.when(Utility::getCurrentUser).thenReturn(mockUser);
            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("1", "johndoe");
            spyObject.viewResetPasswordPage();
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Welcome to reset password page"));
            outputStream.reset();
            Mockito.verify(spyObject, Mockito.times(1)).resetPassword("johndoe");
    
            User mockUser2 = new User("U102", "Alice", "Smith", "alice_smith", "password456", UserRole.APPLICANT);
            mockedUtility.when(Utility::getCurrentUser).thenReturn(mockUser2);
            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("2");
            spyObject.viewResetPasswordPage();
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Redirecting to Applicant dashboard"));
            outputStream.reset();
    
            User mockUser3 = new User("U201", "Alice", "Smith", "alice_smith", "password456", UserRole.RECRUITER);
            mockedUtility.when(Utility::getCurrentUser).thenReturn(mockUser3);
            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("2");
            spyObject.viewResetPasswordPage();
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Redirecting to Recruiter dashboard"));
            outputStream.reset();
    
            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("invalid", "1", "johndoe");
            spyObject.viewResetPasswordPage();
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("You entered invalid option"));
            outputStream.reset();
            Mockito.verify(spyObject, Mockito.times(2)).resetPassword("johndoe");
        }
    }

    @Test
public void resetPasswordTest() throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream)); // Redirect System.out
    CommonService spyObject = Mockito.spy(service);
    ApplicantService applicantSpyObject = Mockito.spy(applicantService);
    RecruiterService recruiterSpyObject = Mockito.spy(recruiterService);
    ApplicantService mockApplicantService = Mockito.mock(ApplicantService.class);
    Mockito.mockStatic(ApplicantService.class).when(ApplicantService::getInstance).thenReturn(mockApplicantService);
    RecruiterService mockRecruiterService = Mockito.mock(RecruiterService.class);
    Mockito.mockStatic(RecruiterService.class).when(RecruiterService::getInstance).thenReturn(mockRecruiterService);
    

    try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {

       // Mockito.doNothing().when(applicantSpyObject).viewApplicantDashboard();
        Mockito.doNothing().when(recruiterSpyObject).viewRecruiterDashboard();
        Mockito.doCallRealMethod().doNothing().when(applicantSpyObject).viewApplicantDashboard();
        Mockito.doNothing().when(spyObject).viewResetPasswordPage();

        User mockApplicantUser = new User("U101", "Mark", "Peter", "markpeter", "oldpassword", UserRole.APPLICANT);
        mockedUtility.when(Utility::getCurrentUser).thenReturn(mockApplicantUser);
        mockedUtility.when(Utility::getUsers).thenReturn(new ArrayList<>(Arrays.asList(mockApplicantUser)));
        mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("newpassword");

        Utility.setUsers(new ArrayList<>(Arrays.asList(mockApplicantUser)));
        spyObject.resetPassword("markpeter");

        String consoleOutput = outputStream.toString();
        Assert.assertTrue(consoleOutput.contains("Your entered username: markpeter"));
        Assert.assertTrue(consoleOutput.contains("Redirecting to Applicant dashboard"));
        Assert.assertEquals("newpassword", mockApplicantUser.getPassword());
        outputStream.reset();

        User mockRecruiterUser = new User("U102", "Alice", "Smith", "recruiterAlice", "oldpassword", UserRole.RECRUITER);
        mockedUtility.when(Utility::getCurrentUser).thenReturn(mockRecruiterUser);
        mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("newpassword");

        Utility.setUsers(new ArrayList<>(Arrays.asList(mockRecruiterUser)));
        spyObject.resetPassword("recruiterAlice");

        consoleOutput = outputStream.toString();
        Assert.assertTrue(consoleOutput.contains("Your entered username: recruiterAlice"));
        Assert.assertTrue(consoleOutput.contains("Redirecting to Recruiter dashboard"));
        Assert.assertEquals("newpassword", mockRecruiterUser.getPassword());
        outputStream.reset();

        mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("wrongusername");
        spyObject.resetPassword("wrongusername");

        consoleOutput = outputStream.toString();
        Assert.assertTrue(consoleOutput.contains("Your entered username: wrongusername"));
        Assert.assertTrue(consoleOutput.contains("You have entered wrong Credentials"));
        Mockito.verify(spyObject, Mockito.times(1)).viewResetPasswordPage();
        outputStream.reset();

        User mockApplicantUser2 = new User("U101", "Mark", "Peter", "markpeter", "oldpassword", UserRole.APPLICANT);
        mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("wrongusername");
        spyObject.resetPassword("wrongusername");

        consoleOutput = outputStream.toString();
        Assert.assertTrue(consoleOutput.contains("You have entered wrong Credentials"));
        Assert.assertEquals("oldpassword", mockApplicantUser2.getPassword());
        Mockito.verify(spyObject, Mockito.times(2)).viewResetPasswordPage();
        outputStream.reset();
    }
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


    @Test
    public void authenticateUser() {
        setUp();
        User user = service.authenticateUser(Utility.getUsers(), "johnDoe", "bestpassword");
        Assert.assertNotNull(user);
    }

    @Test
    public void authenticateUserInvalid() {
        setUp();
        User user = service.authenticateUser(Utility.getUsers(), "johnDoe", "wrongpassword");
        Assert.assertNull(user);
    }

}

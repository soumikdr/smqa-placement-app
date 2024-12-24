package tests;

import model.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import service.ApplicantService;
import service.CommonService;
import utility.Utility;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class ApplicantServiceTests {

    public ApplicantService service = ApplicantService.getInstance();

    @Before
    public void setUp() {
        ArrayList<User> users = new ArrayList<>();

        users.add(new Applicant("1", "John", "Doe", "johnDoe", "bestpassword", new ArrayList<>()));
        users.add(new Recruiter("2", "Ansar", "Patil", "darkAngel", "123qwe"));
        users.add(new Applicant("3", "Jane", "Doe", "janeDoe", "bestpassword", new ArrayList<>()));
        Utility.setUsers(users);
    }

    @Test
    public void submitInterviewFormTest() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        ApplicantService spyObject = Mockito.spy(service);

    	ArrayList<String> questions=new ArrayList<String>();
    	questions.add("Q1");
    	questions.add("Q2");

    	Assignment interview=new Assignment("AS1", "AP1", "Interview", questions, new ArrayList<String>());


        try(MockedStatic<Utility> mockedUtility=Mockito.mockStatic(Utility.class)){

        	Mockito.doNothing().when(spyObject).viewApplicationProcessDashboard();


            mockedUtility.when(()->Utility.inputOutput(Mockito.anyString())).thenReturn("Answer1").thenReturn("Answer2");

            spyObject.submitInterviewForm(interview);

            String consoleOutput = outputStream.toString();

            Assert.assertTrue(consoleOutput.contains("Answers Submitted"));

            Assert.assertEquals(interview.getAnswers().get(0), "Answer1");


            Mockito.verify(spyObject).viewApplicationProcessDashboard();

            mockedUtility.verify(Mockito.times(2),()->Utility.inputOutput(Mockito.anyString()));


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
    public void updateProfileTest() {
        User newUserProfile = Utility.getUsers().get(0);
        newUserProfile.setName("New Name");
        newUserProfile.setLastName("New Last Name");
        ApplicantService service = ApplicantService.getInstance();
        service.updateProfile(newUserProfile);
//        Check if the user profile has been updated
        Assert.assertEquals("New Name", Utility.getCurrentUser().getName());
        Assert.assertEquals("New Last Name", Utility.getCurrentUser().getLastName());
//        Check if users list has been updated
        User filteredUser = Utility.getUsers().stream().filter(user -> user.getId().equals(newUserProfile.getId())).findFirst().orElse(null);
        Assert.assertNotNull("User not found", filteredUser);
        Assert.assertEquals("New Name", filteredUser.getName());
        Assert.assertEquals("New Last Name", filteredUser.getLastName());
        Assert.assertEquals(newUserProfile.getId(), filteredUser.getId());
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

    @Test
    public void viewJobPostValidTest() {
        ArrayList<Application> applications = new ArrayList<>();
        Applicant applicant = new Applicant("1", "John", "Doe", "johnDoe", "password", applications);

        try (MockedStatic<Utility> mockedUtility = mockStatic(Utility.class)) {
            mockedUtility.when(Utility::getCurrentUser).thenReturn(applicant);
            mockedUtility.when(() -> Utility.inputOutput("Enter the Job ID to view the details:")).thenReturn("1");
            List<Job> mockJobs = new ArrayList<>();
            mockJobs.add(new Job("1", "Software Engineer", "Develop and maintain software", JobStatus.PUBLIC));
            mockedUtility.when(Utility::getJobs).thenReturn(mockJobs);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outputStream));
            service.viewJobPost();
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Job ID: 1"));
            Assert.assertTrue(consoleOutput.contains("Job Title: Software Engineer"));
            Assert.assertTrue(consoleOutput.contains("Job Description: Develop and maintain software"));
            Assert.assertTrue(consoleOutput.contains("Job Status: PUBLIC"));
        }
    }
    // @Test
    // public void viewJobPostInvalidTest() {
    //     try (MockedStatic<Utility> mockedUtility = mockStatic(Utility.class)) {
    //         Applicant mockApplicant = new Applicant("1", "John", "Doe", "johnDoe", "password", new ArrayList<>());
    //         mockedUtility.when(Utility::getCurrentUser).thenReturn(mockApplicant);
    //         mockedUtility.when(() -> Utility.inputOutput("Enter the Job ID to view the details:")).thenReturn("999");
    //         List<Job> mockJobs = new ArrayList<>();
    //         mockJobs.add(new Job("1", "Software Engineer", "Develop and maintain software", JobStatus.PUBLIC));
    //         mockedUtility.when(Utility::getJobs).thenReturn(mockJobs);
    //         ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    //         System.setOut(new PrintStream(outputStream));
    //         service.viewJobPost();
    //         mockedUtility.verify(Utility::getCurrentUser, times(1));
    //         mockedUtility.verify(() -> Utility.inputOutput("Enter the Job ID to view the details:"), times(1));
    //         mockedUtility.verify(Utility::getJobs, times(1));
    //         String consoleOutput = outputStream.toString();
    //         assertTrue(consoleOutput.contains("Job with ID 999 not found."));
    //     }
    // }

    @Test
public void viewApplicationProcessDashboardTest() throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream)); // Redirect System.out
    ApplicantService spyObject = Mockito.spy(service);

    try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
        mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString()))
            .thenReturn("1", "2", "3", "4", "5", "6", "invalid", "6", "2");

        Mockito.doNothing().when(spyObject).viewAssessment(Mockito.anyString());
        Mockito.doNothing().when(spyObject).submitAssessmentForm(Mockito.anyString());
        Mockito.doNothing().when(spyObject).viewInterview(Mockito.anyString());
        Mockito.doNothing().when(spyObject).submitInterviewForm(Mockito.anyString());
        Mockito.doNothing().when(spyObject).viewFeedback(Mockito.anyString());

        spyObject.viewApplicationProcessDashboard(Mockito.anyString());
        String consoleOutput = outputStream.toString();
        Assert.assertTrue(consoleOutput.contains("Redirecting to View assignments page"));
        Mockito.verify(spyObject, Mockito.times(1)).viewAssessment(Mockito.anyString());
        outputStream.reset();

        spyObject.viewApplicationProcessDashboard(Mockito.anyString());
        consoleOutput = outputStream.toString();
        Assert.assertTrue(consoleOutput.contains("Redirecting to submit assignments page"));
        Mockito.verify(spyObject, Mockito.times(1)).submitAssessmentForm(Mockito.anyString());
        outputStream.reset();

        spyObject.viewApplicationProcessDashboard(Mockito.anyString());
        consoleOutput = outputStream.toString();
        Assert.assertTrue(consoleOutput.contains("Redirecting to view interview questions page"));
        Mockito.verify(spyObject, Mockito.times(1)).viewInterview(Mockito.anyString());
        outputStream.reset();

        spyObject.viewApplicationProcessDashboard(Mockito.anyString());
        consoleOutput = outputStream.toString();
        Assert.assertTrue(consoleOutput.contains("Redirecting to submit interview answers page"));
        Mockito.verify(spyObject, Mockito.times(1)).submitInterviewForm(Mockito.anyString());
        outputStream.reset();

        spyObject.viewApplicationProcessDashboard(Mockito.anyString());
        consoleOutput = outputStream.toString();
        Assert.assertTrue(consoleOutput.contains("Redirecting to view feedback page"));
        Mockito.verify(spyObject, Mockito.times(1)).viewFeedback(Mockito.anyString());
        outputStream.reset();

        spyObject.viewApplicationProcessDashboard(Mockito.anyString());
        consoleOutput = outputStream.toString();
        Assert.assertTrue(consoleOutput.contains("Go back to Applications page"));
        Mockito.verify(spyObject, Mockito.times(1)).viewApplicantApplications();
        outputStream.reset();

        spyObject.viewApplicationProcessDashboard(Mockito.anyString());
        consoleOutput = outputStream.toString();
        Assert.assertTrue(consoleOutput.contains("You entered invalid option"));
        Mockito.verify(spyObject, Mockito.times(8)).viewApplicationProcessDashboard(Mockito.anyString());
        outputStream.reset();

        Mockito.verify(spyObject, Mockito.times(8)).viewApplicationProcessDashboard(Mockito.anyString());
        mockedUtility.verify(Mockito.times(8), () -> Utility.inputOutput(Mockito.anyString()));
    }
}

    @Test
    public void viewSpecificApplicationTest() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        ApplicantService spyObject = Mockito.spy(service);
        ArrayList<String> questions = new ArrayList<>();
        ArrayList<String> answers = new ArrayList<>();

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {

            Mockito.doNothing().when(spyObject).viewApplicationProcessDashboard(Mockito.anyString());
            Mockito.doNothing().when(spyObject).viewApplicantApplications();
            Mockito.doNothing().when(spyObject).withdrawApplication(Mockito.anyString());
            Mockito.doNothing().when(spyObject).viewJobDescFromApplication(Mockito.anyString());

            User mockUser = new User("U101", "John", "Doe", "johndoe", "bestpassword", UserRole.APPLICANT);
            mockedUtility.when(Utility::getCurrentUser).thenReturn(mockUser);

            questions.add("Q1");
            answers.add("A1");

            ArrayList<Application> mockApplications = new ArrayList<>();
            mockApplications.add(new Application("A101", "J101", "U101", ApplicationStatus.INPROGRESS,
                new ArrayList<>(List.of(new Assignment("Assign1", "U101", "Assignment 1", questions, answers))), null));
            mockApplications.add(new Application("A102", "J102", "U101", ApplicationStatus.SUCCESSFUL, new ArrayList<>(), null));
            mockApplications.add(new Application("A103", "J103", "U102", ApplicationStatus.SUCCESSFUL, new ArrayList<>(),  null));
            mockApplications.add(new Application("A104", "J104", "U101", ApplicationStatus.INPROGRESS,
                new ArrayList<>(List.of(new Assignment("Assign2", "U101", "Assignment 1", new ArrayList<>(), new ArrayList<>()))), null));

            mockedUtility.when(Utility::getApplications).thenReturn(mockApplications);

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("A101", "1", "invalid");
            spyObject.viewSpecificApplication();
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Application ID: A101"));
            Assert.assertTrue(consoleOutput.contains("Job Id: J101"));
            Assert.assertTrue(consoleOutput.contains("Application Status: INPROGRESS"));
            Assert.assertTrue(consoleOutput.contains("Assignment Id: Assign1"));
            Assert.assertTrue(consoleOutput.contains("Questions:"));
            Assert.assertTrue(consoleOutput.contains("Q1"));
            Assert.assertTrue(consoleOutput.contains("Answers:"));
            Assert.assertTrue(consoleOutput.contains("A1"));
            Assert.assertTrue(consoleOutput.contains("Redirecting to application process dashboard"));
            outputStream.reset();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("A101", "1", "1", "A101", "1", "invalid");
            spyObject.viewSpecificApplication();
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Application ID: A101"));
            Assert.assertTrue(consoleOutput.contains("Job Id: J101"));
            Assert.assertTrue(consoleOutput.contains("Application Status: INPROGRESS"));
            Assert.assertTrue(consoleOutput.contains("Assignment Id: Assign1"));
            Assert.assertTrue(consoleOutput.contains("Questions:"));
            Assert.assertTrue(consoleOutput.contains("Q1"));
            Assert.assertTrue(consoleOutput.contains("Answers:"));
            Assert.assertTrue(consoleOutput.contains("A1"));
            Assert.assertTrue(consoleOutput.contains("Redirecting to application process dashboard"));
            Assert.assertTrue(consoleOutput.contains("Redirecting to view specific application details "));
            outputStream.reset();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("A104", "1", "invalid");
            spyObject.viewSpecificApplication();
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Application ID: A104"));
            Assert.assertTrue(consoleOutput.contains("Job Id: J104"));
            Assert.assertTrue(consoleOutput.contains("Application Status: INPROGRESS"));
            Assert.assertTrue(consoleOutput.contains("Assignment Id: Assign2"));
            Assert.assertTrue(consoleOutput.contains("Questions:"));
            Assert.assertTrue(consoleOutput.contains("No questions for this assignemt"));
            Assert.assertTrue(consoleOutput.contains("Answers:"));
            Assert.assertTrue(consoleOutput.contains("No answers submitted for this assignemt"));
            Assert.assertTrue(consoleOutput.contains("Redirecting to application process dashboard"));
            outputStream.reset();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("A104", "2", "invalid");
            spyObject.viewSpecificApplication();
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Application ID: A104"));
            Assert.assertTrue(consoleOutput.contains("Job Id: J104"));
            Assert.assertTrue(consoleOutput.contains("Application Status: INPROGRESS"));
            Assert.assertTrue(consoleOutput.contains("Assignment Id: Assign2"));
            Assert.assertTrue(consoleOutput.contains("Questions:"));
            Assert.assertTrue(consoleOutput.contains("No questions for this assignemt"));
            Assert.assertTrue(consoleOutput.contains("Answers:"));
            Assert.assertTrue(consoleOutput.contains("No answers submitted for this assignemt"));
            Assert.assertTrue(consoleOutput.contains("Complete your application soon"));
            outputStream.reset();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("A104", "3", "invalid");
            spyObject.viewSpecificApplication();
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Application ID: A104"));
            Assert.assertTrue(consoleOutput.contains("Job Id: J104"));
            Assert.assertTrue(consoleOutput.contains("Application Status: INPROGRESS"));
            Assert.assertTrue(consoleOutput.contains("Assignment Id: Assign2"));
            Assert.assertTrue(consoleOutput.contains("Questions:"));
            Assert.assertTrue(consoleOutput.contains("No questions for this assignemt"));
            Assert.assertTrue(consoleOutput.contains("Answers:"));
            Assert.assertTrue(consoleOutput.contains("No answers submitted for this assignemt"));
            Assert.assertTrue(consoleOutput.contains("Redirecting to view job description page"));
            outputStream.reset();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("A104", "4", "invalid");
            spyObject.viewSpecificApplication();
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Application ID: A104"));
            Assert.assertTrue(consoleOutput.contains("Job Id: J104"));
            Assert.assertTrue(consoleOutput.contains("Application Status: INPROGRESS"));
            Assert.assertTrue(consoleOutput.contains("Assignment Id: Assign2"));
            Assert.assertTrue(consoleOutput.contains("Questions:"));
            Assert.assertTrue(consoleOutput.contains("No questions for this assignemt"));
            Assert.assertTrue(consoleOutput.contains("Answers:"));
            Assert.assertTrue(consoleOutput.contains("No answers submitted for this assignemt"));
            Assert.assertTrue(consoleOutput.contains("Redirecting to withdraw application page"));
            outputStream.reset();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("A103","invalid");
            spyObject.viewSpecificApplication();
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("You have entered a invalid application id"));
            Assert.assertTrue(consoleOutput.contains("You entered invalid option"));
            outputStream.reset();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("A000", "2");
            spyObject.viewSpecificApplication();
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("You have entered a invalid application id"));
            Assert.assertTrue(consoleOutput.contains("Redirecting to applications page"));
            outputStream.reset();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("A102", "2");
            spyObject.viewSpecificApplication();
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Application ID: A102"));
            Assert.assertTrue(consoleOutput.contains("Job Id: J102"));
            Assert.assertTrue(consoleOutput.contains("Application Status: SUCCESSFUL"));
            Assert.assertTrue(consoleOutput.contains("Redirecting to applications page"));
            outputStream.reset();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("A101", "nvalid");
            spyObject.viewSpecificApplication();
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("You entered invalid option"));
            outputStream.reset();

            Mockito.verify(spyObject, Mockito.times(11)).viewSpecificApplication();
            Mockito.verify(spyObject, Mockito.times(3)).viewApplicationProcessDashboard("A101");
            Mockito.verify(spyObject, Mockito.times(10)).viewApplicantApplications();
            mockedUtility.verify(Mockito.times(30), () -> Utility.inputOutput(Mockito.anyString()));
        }
    }

    @Test
public void viewSpecificApplicationTest1() throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream)); // Redirect System.out
    ApplicantService spyObject = Mockito.spy(service);
    ArrayList<String> questions = new ArrayList<>();
    ArrayList<String> answers = new ArrayList<>();

    try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {

        // Mock dependent methods
        Mockito.doNothing().when(spyObject).viewApplicationProcessDashboard(Mockito.anyString());
        Mockito.doNothing().when(spyObject).viewApplicantApplications();
        Mockito.doNothing().when(spyObject).withdrawApplication(Mockito.anyString());
        Mockito.doNothing().when(spyObject).viewJobDescFromApplication(Mockito.anyString());

        // Mock current user
        User mockUser = new User("U101", "John", "Doe", "johndoe", "bestpassword", UserRole.APPLICANT);
        mockedUtility.when(Utility::getCurrentUser).thenReturn(mockUser);

        // Create mock questions and answers
        questions.add("Q1");
        answers.add("A1");

        // Mock application list
        ArrayList<Application> mockApplications = new ArrayList<>();
        mockApplications.add(new Application("A101", "J101", "U101", ApplicationStatus.INPROGRESS,
            new ArrayList<>(List.of(new Assignment("Assign1", "U101", "Assignment 1", questions, answers))), null));
        mockApplications.add(new Application("A102", "J102", "U101", ApplicationStatus.SUCCESSFUL, new ArrayList<>(), null));
        mockApplications.add(new Application("A103", "J103", "U102", ApplicationStatus.SUCCESSFUL, new ArrayList<>(), null));
        mockApplications.add(new Application("A104", "J104", "U101", ApplicationStatus.INPROGRESS,
            new ArrayList<>(List.of(new Assignment("Assign2", "U101", "Assignment 2", new ArrayList<>(), new ArrayList<>()))), null));
        mockedUtility.when(Utility::getApplications).thenReturn(mockApplications);

        // Test Case 1: Valid application ID with questions and answers (A101)
        mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("A101", "1", "2");
        spyObject.viewSpecificApplication();
        String consoleOutput = outputStream.toString();
        Assert.assertTrue(consoleOutput.contains("Application ID: A101"));
        Assert.assertTrue(consoleOutput.contains("Job Id: J101"));
        Assert.assertTrue(consoleOutput.contains("Application Status: INPROGRESS"));
        Assert.assertTrue(consoleOutput.contains("Assignment Id: Assign1"));
        Assert.assertTrue(consoleOutput.contains("Questions:"));
        Assert.assertTrue(consoleOutput.contains("Q1"));
        Assert.assertTrue(consoleOutput.contains("Answers:"));
        Assert.assertTrue(consoleOutput.contains("A1"));
        Assert.assertTrue(consoleOutput.contains("Redirecting to application process dashboard"));
        outputStream.reset();

        // Test Case 2: Valid application ID with no questions or answers (A104)
        mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("A104", "4");
        spyObject.viewSpecificApplication();
        consoleOutput = outputStream.toString();
        Assert.assertTrue(consoleOutput.contains("Application ID: A104"));
        Assert.assertTrue(consoleOutput.contains("Job Id: J104"));
        Assert.assertTrue(consoleOutput.contains("Application Status: INPROGRESS"));
        Assert.assertTrue(consoleOutput.contains("Assignment Id: Assign2"));
        Assert.assertTrue(consoleOutput.contains("No questions for this assignemt"));
        Assert.assertTrue(consoleOutput.contains("No answers submitted for this assignemt"));
        Assert.assertTrue(consoleOutput.contains("Complete your application soon"));
        outputStream.reset();

        // Test Case 3: Invalid application ID (A999)
        mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("A999", "2");
        spyObject.viewSpecificApplication();
        consoleOutput = outputStream.toString();
        Assert.assertTrue(consoleOutput.contains("You have entered a invalid application id"));
        Assert.assertTrue(consoleOutput.contains("Redirecting to applications page"));
        outputStream.reset();

        // Test Case 4: Application for another user (A103)
        mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("A103", "2");
        spyObject.viewSpecificApplication();
        consoleOutput = outputStream.toString();
        Assert.assertTrue(consoleOutput.contains("You have entered a invalid application id"));
        Assert.assertTrue(consoleOutput.contains("Redirecting to applications page"));
        outputStream.reset();

        // Test Case 5: Successful application (A102)
        mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("A102", "2");
        spyObject.viewSpecificApplication();
        consoleOutput = outputStream.toString();
        Assert.assertTrue(consoleOutput.contains("Application ID: A102"));
        Assert.assertTrue(consoleOutput.contains("Job Id: J102"));
        Assert.assertTrue(consoleOutput.contains("Application Status: SUCCESSFUL"));
        Assert.assertTrue(consoleOutput.contains("Redirecting to applications page"));
        outputStream.reset();

        // Test Case 6: Recursive option to view another application
        mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("A101", "1", "1", "A104", "2");
        spyObject.viewSpecificApplication();
        consoleOutput = outputStream.toString();
        Assert.assertTrue(consoleOutput.contains("Redirecting to view specific application details"));
        Assert.assertTrue(consoleOutput.contains("Application ID: A104"));
        Assert.assertTrue(consoleOutput.contains("Redirecting to applications page"));
        outputStream.reset();

        // Test Case 7: Invalid option for the main menu
        mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("A101", "invalid");
        spyObject.viewSpecificApplication();
        consoleOutput = outputStream.toString();
        Assert.assertTrue(consoleOutput.contains("You entered invalid option"));
        Assert.assertTrue(consoleOutput.contains("Redirecting to applications page"));
        outputStream.reset();

        // Verify interactions
        Mockito.verify(spyObject, Mockito.atLeastOnce()).viewSpecificApplication();
        Mockito.verify(spyObject, Mockito.times(1)).viewApplicationProcessDashboard("A101");
        Mockito.verify(spyObject, Mockito.times(1)).viewApplicantApplications();
        Mockito.verify(spyObject, Mockito.times(1)).withdrawApplication("A104");
        mockedUtility.verify(Mockito.atLeastOnce(), () -> Utility.inputOutput(Mockito.anyString()));
    }
}


    @Test
    public void viewFeedbackTest() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        ApplicantService spyObject = Mockito.spy(service);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            // Mocked applications list
            ArrayList<Application> mockApplications = new ArrayList<>();
            mockApplications.add(new Application("A101", "J101", "U101", ApplicationStatus.INPROGRESS,
                new ArrayList<>(), "Excellent performance, keep it up!"));
            mockApplications.add(new Application("A102", "J102", "U101", ApplicationStatus.SUCCESSFUL,
                new ArrayList<>(), ""));
            mockApplications.add(new Application("A103", "J103", "U102", ApplicationStatus.UNSUCCESSFUL,
                new ArrayList<>(), "Needs improvement, better luck next time."));

            mockedUtility.when(Utility::getApplications).thenReturn(mockApplications);

            // Mocking dependent method call
            Mockito.doNothing().when(spyObject).viewApplicationProcessDashboard(Mockito.anyString());

            // Case 1: Application ID matches and feedback is available
            spyObject.viewFeedback("A101");
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Welcoem to view feedback page"));
            Assert.assertTrue(consoleOutput.contains("Feedback for A101"));
            Assert.assertTrue(consoleOutput.contains("Excellent performance, keep it up!"));
            Mockito.verify(spyObject, Mockito.times(1)).viewApplicationProcessDashboard("A101");
            outputStream.reset();

            // Case 2: Application ID matches but feedback is not available
            spyObject.viewFeedback("A102");
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Welcoem to view feedback page"));
            Assert.assertTrue(consoleOutput.contains("Feedback not received"));
            Mockito.verify(spyObject, Mockito.times(1)).viewApplicationProcessDashboard("A102");
            outputStream.reset();

            // Case 3: No application matches the given application ID
            spyObject.viewFeedback("A999");
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Welcoem to view feedback page"));
            Assert.assertFalse(consoleOutput.contains("Feedback for U999"));
            Assert.assertFalse(consoleOutput.contains("Feedback not received"));
            Mockito.verify(spyObject, Mockito.times(1)).viewApplicationProcessDashboard("A999");
            outputStream.reset();

            // Case 4: Multiple applications for the same applicant
            spyObject.viewFeedback("A101");
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Welcoem to view feedback page"));
            Assert.assertTrue(consoleOutput.contains("Feedback for A101"));
            Assert.assertTrue(consoleOutput.contains("Excellent performance, keep it up!"));
            Mockito.verify(spyObject, Mockito.times(2)).viewApplicationProcessDashboard("A101");
            outputStream.reset();

            // Verify interactions
            Mockito.verify(spyObject, Mockito.times(4)).viewFeedback(Mockito.anyString());
            mockedUtility.verify(Mockito.times(4), Utility::getApplications);
        }
    }
    @Test
    public void withdrawApplicationTest() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        ApplicantService spyObject = Mockito.spy(service);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {

            ArrayList<Application> mockApplications = new ArrayList<>();
            mockApplications.add(new Application("A101", "J101", "U101", ApplicationStatus.INPROGRESS, new ArrayList<>(), null));
            mockApplications.add(new Application("A102", "J102", "U101", ApplicationStatus.SUCCESSFUL, new ArrayList<>(), null));
            mockApplications.add(new Application("A103", "J103", "U102", ApplicationStatus.UNSUCCESSFUL, new ArrayList<>(), null));

            mockedUtility.when(Utility::getApplications).thenReturn(mockApplications);

            Mockito.doNothing().when(spyObject).viewApplicantApplications();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("1");
            spyObject.withdrawApplication("A101");
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Welcome to withdraw application page"));
            Assert.assertTrue(consoleOutput.contains("Withdrawing application"));
            Assert.assertFalse(mockApplications.stream().anyMatch(app -> "A101".equals(app.getId())));
            outputStream.reset();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("2");
            spyObject.withdrawApplication("A102");
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Welcome to withdraw application page"));
            Assert.assertTrue(consoleOutput.contains("Redirecting to applications page"));
            Assert.assertTrue(mockApplications.stream().anyMatch(app -> "A102".equals(app.getId())));
            outputStream.reset();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("invalid");
            spyObject.withdrawApplication("A103");
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Welcome to withdraw application page"));
            Assert.assertTrue(consoleOutput.contains("You entered invalid option"));
            Assert.assertTrue(mockApplications.stream().anyMatch(app -> "A103".equals(app.getId())));
            outputStream.reset();

            Mockito.verify(spyObject, Mockito.times(3)).withdrawApplication(Mockito.anyString());
            Mockito.verify(spyObject, Mockito.times(2)).viewApplicantApplications();
            mockedUtility.verify(Mockito.times(3), () -> Utility.inputOutput(Mockito.anyString()));
            mockedUtility.verify(Mockito.times(1), Utility::getApplications);
        }
    }

}



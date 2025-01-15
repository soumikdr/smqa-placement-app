package test.whitebox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import model.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import model.Applicant;
import model.Application;
import model.ApplicationStatus;
import model.Assignment;
import model.Job;
import model.JobStatus;
import model.User;
import model.UserRole;
import service.ApplicantService;
import service.CommonService;
import utility.Utility;

public class ApplicantServiceStatementTest {

    private ApplicantService applicantService;

    private ArrayList<Application> mockApplications;
    private ArrayList<User> mockUsers;
    private ArrayList<Job> mockJobs;
    private Application mockApplication;
    private ArrayList<Assignment> mockInterviews;
    private Assignment mockAssignment;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Before
    public void setUp() {
        applicantService = ApplicantService.getInstance();
        // Mock Jobs
        Job job1 = new Job("1", "Data Analyst",
                "A data analyst's job is to collect, organize, and analyze data to help businesses solve problems and gain insights. ",
                JobStatus.PRIVATE);
        Job job2 = new Job("2", "Frontend Developer",
                "As a Front End Developer you'll take ownership of technical projects, designing and developing user interfaces and client dashboards for cutting edge trading systems technology. ",
                JobStatus.PUBLIC);
        mockJobs = new ArrayList<>();
        mockJobs.add(job1);
        mockJobs.add(job2);

        // Mock Assignments
        ArrayList<String> questions = new ArrayList<String>();
        questions.add("Question 1");
        questions.add("Question 2");

        ArrayList<String> answers = new ArrayList<String>();
        answers.add("1. Answer");
        answers.add("2. Answer");

        Assignment assignment = new Assignment("1", "1", "Java Assignment", questions, answers);
        mockAssignment = assignment;
        ArrayList<Assignment> assignments = new ArrayList<>();
        assignments.add(assignment);

        Assignment mockInterview=new Assignment("1", "1", "Interview", questions, answers);

        // Mock applications
        mockApplications = new ArrayList<>();
        Application app1 = new Application("1", "1", "1", ApplicationStatus.INPROGRESS, assignments, 2, "BSc", "JS, CSS", "");
        mockInterviews=new ArrayList<>();
        mockInterviews.add(mockInterview);
        app1.setInterviewAssignments(mockInterviews);
        mockApplication = app1;

        app1.setAssignments(assignments);
        mockApplications.add(app1);

        // Mock users
        mockUsers = new ArrayList<>();
        User user1 = new User("1", "John", "Doe", "johnDoe", "bestpassword", UserRole.APPLICANT);
        mockUsers.add(user1);
    }

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
    }


    @Test
    public void testSignIn_Valid() {
        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            // Arrange
            mockedUtility.when(Utility::getUsers).thenReturn(mockUsers);
            // First invalid password, then correct password
            mockedUtility.when(() -> Utility.inputOutput(anyString()))
                .thenReturn("johnDoe").thenReturn("wrongPassword").thenReturn("y").thenReturn("johnDoe").thenReturn("bestpassword");;
            ApplicantService applicantService = Mockito.spy(new ApplicantService());
            doNothing().when(applicantService).viewApplicantDashboard();

            // Act
            applicantService.signIn();

            // Assert
            // Get the complete output
            String output = outContent.toString().trim();

            // Extract the last println message (messages are separated by newlines)
            String[] lines = output.split("\n");
            String lastMessage = lines[lines.length - 1];

            assertEquals("Applicant Signin successful. proceeding to applicant dashboard..", lastMessage);
        }
    }

    // ETY1 - STORY 28
    @Test
    public void submitApplicationFormTest_submitApplication() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        ApplicantService service = ApplicantService.getInstance();
        ApplicantService spyObject = Mockito.spy(service);

        try(MockedStatic<Utility> mockedUtility=Mockito.mockStatic(Utility.class)) {
            Mockito.doNothing().when(spyObject).viewApplicantDashboard();

            mockedUtility.when(Utility::getApplications).thenReturn(mockApplications);
            mockedUtility.when(Utility::getCurrentUser).thenReturn(mockUsers.get(0));

            spyObject.submitApplicationForm(mockJobs.get(0),mockApplication.getEducation(),mockApplication.getYearOfExperience(),mockApplication.getSkills());
            String consoleOutput = outputStream.toString();

            Assert.assertTrue(consoleOutput.contains("Application submitted : "));

            mockedUtility.verify(Mockito.times(1),()->Utility.getApplications());

            Mockito.verify(spyObject,Mockito.times(1)).viewApplicantDashboard();

        }
    }

    // ETY1 - STORY 8
    @Test
    public void applicantLogOutTest_logOut() throws IOException {
        try(MockedStatic<CommonService> spyCommonService = Mockito.mockStatic(CommonService.class);
        ) {
            CommonService mockCommonService=Mockito.mock(CommonService.class);

            spyCommonService.when(CommonService::getInstance).thenReturn(mockCommonService);

            Mockito.doNothing().when(mockCommonService).accessLandingPage();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outputStream)); // Redirect System.out
            ApplicantService service = ApplicantService.getInstance();

            service.applicantLogOut();
            String consoleOutput = outputStream.toString();

            Assert.assertTrue(consoleOutput.contains("Logged Out Successfully"));

            Mockito.verify(mockCommonService).accessLandingPage();

        }
    }

    // ETY1 - STORY 14
    @Test
    public void viewApplicantDashboardTest_input1() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        ApplicantService service = ApplicantService.getInstance();

        ApplicantService spyObject = Mockito.spy(service);

        try(MockedStatic<Utility> mockedUtility=Mockito.mockStatic(Utility.class)) {
            Mockito.doNothing().when(spyObject).viewApplicantProfilePage();

            mockedUtility.when(() -> Utility.inputOutput(anyString())).thenReturn("1");

            spyObject.viewApplicantDashboard();

            String consoleOutput = outputStream.toString();

            Assert.assertTrue(consoleOutput.contains("Directing to Profile Page..."));


            Mockito.verify(spyObject,Mockito.times(1)).viewApplicantProfilePage();
            mockedUtility.verify(Mockito.times(1),()->Utility.inputOutput(Mockito.anyString()));

        }
    }

    // ETY1 - STORY 14
    @Test
    public void viewApplicantDashboardTest_input2() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        ApplicantService service = ApplicantService.getInstance();

        ApplicantService spyObject = Mockito.spy(service);

        try(MockedStatic<Utility> mockedUtility=Mockito.mockStatic(Utility.class)) {
            Mockito.doNothing().when(spyObject).viewApplicantApplications();

            mockedUtility.when(() -> Utility.inputOutput(anyString())).thenReturn("2");

            spyObject.viewApplicantDashboard();
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Directing to Applications Page..."));

            outputStream.reset();

            mockedUtility.verify(Mockito.times(1),()->Utility.inputOutput(Mockito.anyString()));
            Mockito.verify(spyObject,Mockito.times(1)).viewApplicantDashboard();

            Mockito.verify(spyObject,Mockito.times(1)).viewApplicantApplications();
        }
    }
    // ETY1 - STORY 14
    @Test
    public void viewApplicantDashboardTest_input3() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        ApplicantService service = ApplicantService.getInstance();

        ApplicantService spyObject = Mockito.spy(service);

        try(MockedStatic<Utility> mockedUtility=Mockito.mockStatic(Utility.class)) {

            Mockito.doNothing().when(spyObject).viewAllAvailableJobs();

            mockedUtility.when(() -> Utility.inputOutput(anyString())).thenReturn("3");

            spyObject.viewApplicantDashboard();
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Directing to Available Jobs Page..."));

            Mockito.verify(spyObject,Mockito.times(1)).viewAllAvailableJobs();

            mockedUtility.verify(Mockito.times(1),()->Utility.inputOutput(Mockito.anyString()));
            Mockito.verify(spyObject,Mockito.times(1)).viewApplicantDashboard();

        }
    }

    // ETY1 - STORY 14
    @Test
    public void viewApplicantDashboardTest_logOut() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        ApplicantService service = ApplicantService.getInstance();

        ApplicantService spyObject = Mockito.spy(service);

        try(MockedStatic<Utility> mockedUtility=Mockito.mockStatic(Utility.class)) {

            Mockito.doNothing().when(spyObject).applicantLogOut();


            mockedUtility.when(() -> Utility.inputOutput(anyString())).thenReturn("4");

            spyObject.viewApplicantDashboard();
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Logging Out..."));


            mockedUtility.verify(Mockito.times(1),()->Utility.inputOutput(Mockito.anyString()));
            Mockito.verify(spyObject,Mockito.times(1)).viewApplicantDashboard();

            Mockito.verify(spyObject,Mockito.times(1)).applicantLogOut();
        }
    }

    // ETY1 - STORY 14
    @Test
    public void viewApplicantDashboardTest_invalidInput() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        ApplicantService service = ApplicantService.getInstance();

        ApplicantService spyObject = Mockito.spy(service);

        try(MockedStatic<Utility> mockedUtility=Mockito.mockStatic(Utility.class)) {

            mockedUtility.when(() -> Utility.inputOutput(anyString())).thenReturn("invalidInput");

            Mockito.doCallRealMethod().doNothing().when(spyObject).viewApplicantDashboard();

            spyObject.viewApplicantDashboard();
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("You entered invalid option"));


            mockedUtility.verify(Mockito.times(1),()->Utility.inputOutput(Mockito.anyString()));
            Mockito.verify(spyObject,Mockito.times(2)).viewApplicantDashboard();

        }
    }
    // ETY1 - STORY 45
    @Test
    public void submitInterviewFormTest_noApplicationId() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        ApplicantService spyObject = Mockito.spy(new ApplicantService());


        try(MockedStatic<Utility> mockedUtility=Mockito.mockStatic(Utility.class)){

            Mockito.doNothing().when(spyObject).viewApplicantApplications();
            mockedUtility.when(Utility::getApplications).thenReturn(mockApplications);

            spyObject.submitInterviewForm("nonExistentId");

            String consoleOutput = outputStream.toString();

            Assert.assertTrue(consoleOutput.contains("Application with given ID not found"));

            mockedUtility.verify(Mockito.times(1),()->Utility.getApplications());
            Mockito.verify(spyObject,Mockito.times(1)).viewApplicantApplications();


        }
    }

    // ETY1 - STORY 45
    @Test
    public void submitInterviewFormTest_noInterview() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        ApplicantService spyObject = Mockito.spy(new ApplicantService());


        try(MockedStatic<Utility> mockedUtility=Mockito.mockStatic(Utility.class)){


            Mockito.doNothing().when(spyObject).viewApplicantApplications();

            mockApplications.get(0).setInterviewAssignments(new ArrayList<>());

            mockedUtility.when(Utility::getApplications).thenReturn(mockApplications);

            spyObject.submitInterviewForm(mockApplications.get(0).getId());

            String consoleOutput = outputStream.toString();

            Assert.assertTrue(consoleOutput.contains("No interview questions found"));



            mockedUtility.verify(Mockito.times(1),()->Utility.getApplications());
            Mockito.verify(spyObject,Mockito.times(1)).viewApplicantApplications();


        }
    }

    // ETY1 - STORY 45
    @Test
    public void submitInterviewFormTest_alreadySubmitted() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        ApplicantService spyObject = Mockito.spy(new ApplicantService());


        try(MockedStatic<Utility> mockedUtility=Mockito.mockStatic(Utility.class)){

            Mockito.doNothing().when(spyObject).viewApplicationProcessDashboard(any());

            mockedUtility.when(Utility::getApplications).thenReturn(mockApplications);

            mockInterviews.get(0).setStatus(AssignmentStatus.SUBMITTED);

            mockApplications.get(0).setInterviewAssignments(mockInterviews);

            spyObject.submitInterviewForm(mockApplications.get(0).getId());

            String consoleOutput = outputStream.toString();

            Assert.assertTrue(consoleOutput.contains("Interview already submitted"));


            Mockito.verify(spyObject,Mockito.times(1)).viewApplicationProcessDashboard(any());
            mockedUtility.verify(Mockito.times(1),()->Utility.getApplications());


        }
    }



    @Test
    public void testViewApplicantProfilePage_Valid() {
        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            // Arrange
            User mockUser = new User("51", "John", "Doe", "john", "GyyGGyibkl", UserRole.APPLICANT);
            mockedUtility.when(Utility::getCurrentUser).thenReturn(mockUser);
            mockedUtility.when(() -> Utility.inputOutput(anyString())).thenReturn("continue");
            ApplicantService applicantService = Mockito.spy(new ApplicantService());
            doNothing().when(applicantService).viewApplicantDashboard();

            // Test
            applicantService.viewApplicantProfilePage();

            // Assert
            // Get the complete output
            String output = outContent.toString().trim();

            // Extract the last println message (messages are separated by newlines)
            String[] lines = output.split("\n");

            // Filter string
            List<String> filteredLines = new ArrayList<>();

            for (String str : lines) {
                String trimmed = str.trim();
                if (!trimmed.isEmpty()) {
                    filteredLines.add(trimmed);
                }
            }

            assertFalse(filteredLines.contains("ID is missing."));
            assertFalse(filteredLines.contains("Name is missing."));
            assertFalse(filteredLines.contains("Last Name is missing."));
            assertFalse(filteredLines.contains("Username is missing."));
            assertFalse(filteredLines.contains("Role is missing."));
        }
    }

    @Test
    public void testViewApplicantProfilePage_MissingData() {
        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            // Arrange
            User mockUser = new User("", "", "", "", "", null);
            mockedUtility.when(Utility::getCurrentUser).thenReturn(mockUser);
            mockedUtility.when(() -> Utility.inputOutput(anyString())).thenReturn("continue");
            ApplicantService applicantService = Mockito.spy(new ApplicantService());
            doNothing().when(applicantService).viewApplicantDashboard();

            // Test
            applicantService.viewApplicantProfilePage();

            // Assert
            // Get the complete output
            String output = outContent.toString().trim();

            // Extract the last println message (messages are separated by newlines)
            String[] lines = output.split("\n");

            // Filter string
            List<String> filteredLines = new ArrayList<>();

            for (String str : lines) {
                String trimmed = str.trim();
                if (!trimmed.isEmpty()) {
                    filteredLines.add(trimmed);
                }
            }

            assertTrue(filteredLines.contains("ID is missing."));
            assertTrue(filteredLines.contains("Name is missing."));
            assertTrue(filteredLines.contains("Last Name is missing."));
            assertTrue(filteredLines.contains("Username is missing."));
            assertTrue(filteredLines.contains("Role is missing."));
        }
    }

    @Test
    public void testSubmitAssessmentForm_Valid() {
        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            // Arrange
            mockedUtility.when(Utility::getApplications).thenReturn(mockApplications);
            mockedUtility.when(() -> Utility.inputOutput(anyString())).thenReturn("Test Input");
            ApplicantService applicantService = Mockito.spy(new ApplicantService());
            doNothing().when(applicantService).viewApplicationProcessDashboard("1");

            // Act
            applicantService.submitAssessmentForm("1");

            // Assert
            // Get the complete output
            String output = outContent.toString().trim();

            // Extract the last println message (messages are separated by newlines)
            String[] lines = output.split("\n");
            String lastMessage = lines[lines.length - 1];

            assertEquals("All assignments submitted successfully", lastMessage.trim());
        }
    }

    @Test
    public void testViewInterview_Valid() {
        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            // Arrange
            mockedUtility.when(Utility::getApplications).thenReturn(mockApplications);
            ApplicantService applicantService = Mockito.spy(new ApplicantService());
            doNothing().when(applicantService).viewApplicationProcessDashboard("1");

            // Act
            applicantService.viewInterview("1");

            // Assert
            // Get the complete output
            String output = outContent.toString().trim();

            // Extract the last println message (messages are separated by newlines)
            String[] lines = output.split("\n");

            // Filter string
            List<String> filteredLines = new ArrayList<>();

            for (String str : lines) {
                String trimmed = str.trim();
                if (!trimmed.isEmpty()) {
                    filteredLines.add(trimmed);
                }
            }

            assertTrue(filteredLines.contains("No interview questions found"));
        }
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 18
     *
     * Covers deleteProfileHelper() in ApplicantService:
     * Ensures the current user is removed from the global user list,
     * and current user is set to null.
     */
    @Test
    public void testDeleteProfileHelper() {
        User mockApplicant = new Applicant("A1", "John", "Doe", "johnUser", "pass");
        ArrayList<User> users = new ArrayList<>();
        users.add(mockApplicant);

        try (MockedStatic<Utility> utilMock = Mockito.mockStatic(Utility.class)) {
            utilMock.when(Utility::getCurrentUser).thenReturn(mockApplicant);
            utilMock.when(Utility::getUsers).thenReturn(users);
            applicantService.deleteProfileHelper();

            assertTrue("User list should be empty after deletion", users.isEmpty());
            utilMock.verify(times(1), () -> Utility.setCurrentUser(null));
        }
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 18
     *
     * Covers deleteApplicantProfile() in ApplicantService:
     * - Scenario where user types "Y" => actually deletes profile
     */
    @Test
    public void testDeleteApplicantProfile_yes() {
        ApplicantService spyService = Mockito.spy(applicantService);

        User mockApplicant = new Applicant("A2", "Jane", "Doe", "janeUser", "123");
        ArrayList<User> users = new ArrayList<>();
        users.add(mockApplicant);

        try (MockedStatic<Utility> utilMock = Mockito.mockStatic(Utility.class)) {
            utilMock.when(Utility::getCurrentUser).thenReturn(mockApplicant);
            utilMock.when(Utility::getUsers).thenReturn(users);

            utilMock.when(() -> Utility.inputOutput("Are you sure you want to delete your profile? (Y/N)"))
                    .thenReturn("Y");

            doNothing().when(spyService).applicantViewSignInSignUpPage();

            spyService.deleteApplicantProfile();
            assertTrue("User list should be empty after deletion", users.isEmpty());
            utilMock.verify(times(1), () -> Utility.setCurrentUser(null));
            verify(spyService, times(1)).applicantViewSignInSignUpPage();
        }
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 17
     *
     * Covers updateProfile(...) in ApplicantService:
     * - Ensures the user in memory is updated in Utility’s user list.
     */
    @Test
    public void testUpdateProfile() {
        User current = new Applicant("A3", "Jack", "Smith", "jackUser", "pw");
        // userList has that user
        ArrayList<User> userList = new ArrayList<>();
        userList.add(current);

        try (MockedStatic<Utility> utilMock = Mockito.mockStatic(Utility.class)) {
            utilMock.when(Utility::getUsers).thenReturn(userList);
            utilMock.when(Utility::getCurrentUser).thenReturn(current);

            // Simulate new user changes
            current.setName("Jacky");
            current.setLastName("Smyth");

            applicantService.updateProfile(current);

            assertEquals(1, userList.size());
            User updated = userList.get(0);
            assertEquals("Jacky", updated.getName());
            assertEquals("Smyth", updated.getLastName());
        }
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 17
     *
     * Covers showUpdateProfilePage() in ApplicantService:
     * - user enters new name, new last name, checks console output
     */
    @Test
    public void testShowUpdateProfilePage() {
        ApplicantService spyService = Mockito.spy(applicantService);

        // Current user
        User current = new Applicant("A4", "OldName", "OldLast", "oldUser", "pw");
        try (MockedStatic<Utility> utilMock = Mockito.mockStatic(Utility.class)) {
            utilMock.when(Utility::getCurrentUser).thenReturn(current);

            utilMock.when(() -> Utility.inputOutput("Enter your name: "))
                    .thenReturn("NewName");
            utilMock.when(() -> Utility.inputOutput("Enter your last name: "))
                    .thenReturn("NewLast");

            doNothing().when(spyService).updateProfile(any(User.class));
            doNothing().when(spyService).viewApplicantProfilePage();

            spyService.showUpdateProfilePage();

            // Check that the user object is updated
            assertEquals("NewName", current.getName());
            assertEquals("NewLast", current.getLastName());

            // Verify it calls updateProfile(currentUser) and returns
            verify(spyService, times(1)).updateProfile(current);
            verify(spyService, times(1)).viewApplicantProfilePage();
        }
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 29
     *
     * Covers viewApplicantApplications() in ApplicantService:
     * - If no applications, prints "No applications found." => returns
     */
    @Test
    public void testViewApplicantApplications_noApps() {
        ApplicantService spyService = Mockito.spy(applicantService);
        Applicant mockApplicant = new Applicant("A5", "App", "User", "appUser", "pw");

        try (MockedStatic<Utility> utilMock = Mockito.mockStatic(Utility.class)) {
            utilMock.when(Utility::getCurrentUser).thenReturn(mockApplicant);
            doNothing().when(spyService).viewApplicantDashboard();

            spyService.viewApplicantApplications();

            String output = outContent.toString();
            assertTrue(output.contains("No applications found."));
            verify(spyService, times(1)).viewApplicantDashboard();
        }
    }

    /*
     * Author: Mayur Shinde (mss62)
     * User Story: 36
     *
     * Covers viewJobDescFromApplication() in ApplicantService:
     * - The job is not found => prints "Job with ID XXX not found."
     */
    @Test
    public void testViewJobDescFromApplication_noJob() {
        ArrayList<Application> mockApps = new ArrayList<>();
        // single application with jobId="unknownJob"
        Application app = new Application("APP99", "unknownJob", "A6",
                null, new ArrayList<>(), 0, "", "", "");
        mockApps.add(app);

        ArrayList<Job> mockJobs = new ArrayList<>(); // no matching job

        try (MockedStatic<Utility> utilMock = Mockito.mockStatic(Utility.class)) {
            utilMock.when(Utility::getApplications).thenReturn(mockApps);
            utilMock.when(Utility::getJobs).thenReturn(mockJobs);

            applicantService.viewJobDescFromApplication("APP99");
            String output = outContent.toString();
            assertTrue(output.contains("Job with ID unknownJob not found."));
        }
    }

}

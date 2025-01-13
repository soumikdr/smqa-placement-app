package tests;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

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
import model.Recruiter;
import model.User;
import model.UserRole;
import service.CommonService;
import service.RecruiterService;
import utility.Utility;

public class RecruiterServiceTests {

    public RecruiterService service = new RecruiterService();
    private ByteArrayOutputStream outputStream;

    public RecruiterServiceTests() {
    }

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        ArrayList<User> users = new ArrayList<>();
        users.add(new Applicant("1", "John", "Doe", "johnDoe", "bestpassword"));
        users.add(new Recruiter("2", "Ansar", "Patil", "darkAngel", "123qwe"));

        Utility.setUsers(users);
        ArrayList<Job> jobs = new ArrayList<>();
        jobs.add(new Job("1", "Software Engineer", "Develop software", JobStatus.PUBLIC));
        jobs.add(new Job("2", "Data Analyst", "Analyze data", JobStatus.PUBLIC));
        jobs.add(new Job("3", "Product Manager", "Manage products", JobStatus.PRIVATE));
        Utility.setJobs(jobs);
    }

    @Test
    public void viewRecruiterProfilePageTest() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        RecruiterService spyObject = Mockito.spy(service);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {

            Mockito.doNothing().when(spyObject).updateRecruiterProfile();
            Mockito.doNothing().when(spyObject).deleteRecruiterProfile();
            Mockito.doNothing().when(spyObject).viewRecruiterDashboard();

            User mockUser = new User("U101", "John", "Doe", "johndoe", "bestpassword", UserRole.RECRUITER);
            mockedUtility.when(Utility::getCurrentUser).thenReturn(mockUser);

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("1");
            spyObject.viewRecruiterProfilePage();
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Redirecting to update profile page..."));
            outputStream.reset();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("2");
            spyObject.viewRecruiterProfilePage();
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Redirecting to delete profile page..."));
            outputStream.reset();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("3");
            spyObject.viewRecruiterProfilePage();
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Redirecting to dashboard..."));
            outputStream.reset();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("invalid", "1");
            spyObject.viewRecruiterProfilePage();
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("You entered invalid option"));
            outputStream.reset();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("invalid", "3");
            spyObject.viewRecruiterProfilePage();
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("You entered invalid option"));
            Assert.assertTrue(consoleOutput.contains("Redirecting to dashboard"));
            outputStream.reset();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("3");
            spyObject.viewRecruiterProfilePage();
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("First Name: John"));
            Assert.assertTrue(consoleOutput.contains("Last Name: Doe"));
            Assert.assertTrue(consoleOutput.contains("User Name: johndoe"));
            Assert.assertTrue(consoleOutput.contains("Role: RECRUITER"));
            Assert.assertTrue(consoleOutput.contains("Redirecting to dashboard"));
            outputStream.reset();

            Mockito.verify(spyObject, Mockito.atLeastOnce()).viewRecruiterProfilePage();
            mockedUtility.verify(Mockito.atLeastOnce(), () -> Utility.inputOutput(Mockito.anyString()));
        }

    }

    @Test
    public void testApproveRejectApplication() {

        Application application = new Application("1", "Job1", "User1", "UnderConsideration", null);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        RecruiterService spyObject = Mockito.spy(service);
        ArrayList<Application> apps = new ArrayList<Application>();
        apps.add(application);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {

            Mockito.doNothing().when(spyObject).viewSpecificApplication();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("1");
            mockedUtility.when(() -> Utility.getApplications()).thenReturn(apps);

            spyObject.approveRejectApplication(application);

            String consoleOutput = outputStream.toString();

            Assert.assertTrue(consoleOutput.contains("Application Approved"));

            outputStream.reset();

            // simulatedInput = "invalid input";
            // System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
            // simulatedInput = "1";
            // System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
            outputStream.reset();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("2");

            spyObject.approveRejectApplication(application);

            consoleOutput = outputStream.toString();

            Assert.assertTrue(consoleOutput.contains("Application Rejected"));

            // simulatedInput = "invalid input";
            // System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
            // simulatedInput = "1";
            // System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
            outputStream.reset();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("3");

            spyObject.approveRejectApplication(application);

            consoleOutput = outputStream.toString();

            Assert.assertTrue(consoleOutput.contains("Directing to Application's Page"));

            outputStream.reset();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("invalid");

            Mockito.doCallRealMethod().doNothing().when(spyObject).approveRejectApplication(any());

            spyObject.approveRejectApplication(application);

            consoleOutput = outputStream.toString();

            Assert.assertTrue(consoleOutput.contains("You entered invalid option"));

            Mockito.verify(spyObject, times(3)).viewSpecificApplication();
            Mockito.verify(spyObject, times(5)).approveRejectApplication(Mockito.any());
            mockedUtility.verify(times(4), () -> Utility.inputOutput(Mockito.anyString()));
            mockedUtility.verify(times(2), () -> Utility.getApplications());

        }

    }

    @Test
    public void submitNewJobPost() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        service.submitNewJobPost("Software Engineer", "Develop software");
        String consoleOutput = outputStream.toString();
        Assert.assertTrue(consoleOutput.contains("Job posted successfully"));
        Assert.assertEquals(4, Utility.getJobs().size());
    }

    @Test
    public void viewAvailableJobs_Empty() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out

        service.viewAvailableJobs();
        String consoleOutput = outputStream.toString();
        Assert.assertTrue(consoleOutput.contains("No jobs available"));
    }

    @Test
    public void viewAvailableJobs_NotEmpty() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out

        RecruiterService spyObject = Mockito.spy(service);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {

            ArrayList<Job> mockJobs = new ArrayList<>();
            mockJobs.add(new Job("1", "Software Engineer", "Develop software", JobStatus.PUBLIC));
            mockJobs.add(new Job("2", "Data Analyst", "Analyze data", JobStatus.PUBLIC));
            mockJobs.add(new Job("3", "Product Manager", "Manage products", JobStatus.PUBLIC));
            mockedUtility.when(Utility::getJobs).thenReturn(mockJobs);
            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("1");
            Mockito.doNothing().when(spyObject).viewSpecificJobPost();
            String expectedOutput = """
                    Available Jobs\r
                    1. Job ID: 1 | Job Title: Software Engineer\r
                    2. Job ID: 2 | Job Title: Data Analyst\r
                    3. Job ID: 3 | Job Title: Product Manager\r
                    """;
            spyObject.viewAvailableJobs();
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains(expectedOutput));
        }

    }

    @Test
    public void updateStatusOfJobPost_Empty() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        // Clearing the jobs
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
        Assert.assertEquals(JobStatus.PRIVATE, job.getJobStatus());
    }

    @Test
    public void viewSpecificJobPostTest() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        RecruiterService spyObject = Mockito.spy(service);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {

            ArrayList<Job> mockJobs = new ArrayList<>();
            mockJobs.add(new Job("101", "Software Engineer", "Develop software", JobStatus.PRIVATE));
            mockJobs.add(new Job("102", "Data Scientist", "Analyze data", JobStatus.PUBLIC));

            mockedUtility.when(Utility::getJobs).thenReturn(mockJobs);

            Mockito.doNothing().when(spyObject).updateDescriptionOfJobPost(Mockito.anyString());
            Mockito.doNothing().when(spyObject).updateStatusOfJobPost(Mockito.anyString());
            Mockito.doNothing().when(spyObject).viewTotalNumberOfApplications(Mockito.anyString());
            Mockito.doNothing().when(spyObject).viewRecruiterDashboard();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("101", "1", "2");
            spyObject.viewSpecificJobPost();
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Job ID: 101"));
            Assert.assertTrue(consoleOutput.contains("Job Name: Software Engineer"));
            Assert.assertTrue(consoleOutput.contains("Redirecting to update job description page"));
            Mockito.verify(spyObject, Mockito.times(1)).updateDescriptionOfJobPost("101");
            outputStream.reset();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("101", "2", "2");
            spyObject.viewSpecificJobPost();
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Redirecting to total applications for the job"));
            Mockito.verify(spyObject, Mockito.times(1)).updateStatusOfJobPost("101");
            outputStream.reset();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("101", "3", "2");
            spyObject.viewSpecificJobPost();
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Redirecting to dashboard"));
            Mockito.verify(spyObject, Mockito.times(1)).viewTotalNumberOfApplications("101");
            outputStream.reset();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("101", "4");
            spyObject.viewSpecificJobPost();
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Redirecting to main menu"));
            outputStream.reset();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("999", "2");
            spyObject.viewSpecificJobPost();
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("You have entered a invalid Job id"));
            Mockito.verify(spyObject, Mockito.times(5)).viewRecruiterDashboard();
            outputStream.reset();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("101", "invalid");
            spyObject.viewSpecificJobPost();
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("You entered invalid option"));
            Mockito.verify(spyObject, Mockito.times(7)).viewRecruiterDashboard();
            outputStream.reset();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("101", "1", "1", "102", "4");
            spyObject.viewSpecificJobPost();
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Redirecting to view specific job details"));
            Assert.assertTrue(consoleOutput.contains("Job ID: 102"));
            Assert.assertTrue(consoleOutput.contains("Redirecting to main menu"));
            outputStream.reset();

            Mockito.verify(spyObject, Mockito.times(8)).viewSpecificJobPost();
            Mockito.verify(spyObject, Mockito.times(2)).updateDescriptionOfJobPost("101");
            Mockito.verify(spyObject, Mockito.times(1)).updateStatusOfJobPost("101");
            Mockito.verify(spyObject, Mockito.times(1)).viewTotalNumberOfApplications("101");
            Mockito.verify(spyObject, Mockito.times(8)).viewRecruiterDashboard();
            mockedUtility.verify(Mockito.times(23), () -> Utility.inputOutput(Mockito.anyString()));
        }
    }

    @Test
    public void viewAssessmentResultTest() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        RecruiterService spyObject = Mockito.spy(service);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            ArrayList<Application> mockApplications = new ArrayList<>();
            mockApplications.add(new Application("App1", "Job1", "User1", "UnderConsideration", null, 1,
                    "University of Leicester", "Java"));
            mockApplications.add(new Application("App2", "Job2", "User2", "UnderConsideration", null, 1,
                    "University of Leicester", "Java"));

            ArrayList<Assignment> mockAssignments = new ArrayList<>();

            ArrayList<String> questions = new ArrayList<String>();
            questions.add("Q1");
            questions.add("Q2");
            ArrayList<String> answers = new ArrayList<String>();
            answers.add("ANS1");
            answers.add("ANS2");

            mockAssignments.add(new Assignment("A1", "App1", "Coding Assessment", questions, answers));
            mockAssignments.add(new Assignment("A2", "App1", "Interview", questions, answers));

            mockApplications.get(0).setAssignments(mockAssignments);

            mockedUtility.when(Utility::getApplications).thenReturn(mockApplications);

            Mockito.doNothing().when(spyObject).viewSpecificApplication();

            spyObject.viewAssessmentResult("App1", "A1");

            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Assessment Found"));
            Assert.assertTrue(consoleOutput.contains("Q1"));

            outputStream.reset();

            spyObject.viewAssessmentResult("App1", "A3");

            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("There is no Coding Assessment Result for this application"));
            Assert.assertTrue(consoleOutput.contains("Directing to Application Page"));

            Mockito.verify(spyObject, times(2)).viewSpecificApplication();
            mockedUtility.verify(times(2), () -> Utility.getApplications());

        }

    }

    @Test
    public void viewSubmittedAnswersTest() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        RecruiterService spyObject = Mockito.spy(service);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {

            ArrayList<Application> mockApplications = new ArrayList<>();
            ArrayList<String> mockQuestions1 = new ArrayList<>(List.of("What is Java?", "Explain OOP concepts."));
            ArrayList<String> mockAnswers1 = new ArrayList<>(
                    List.of("Java is a programming language.", "OOP includes Encapsulation, Polymorphism."));
            ArrayList<String> mockQuestions2 = new ArrayList<>(List.of("What is your experience?"));
            ArrayList<String> mockAnswers2 = new ArrayList<>(
                    List.of("I have 5 years of experience in software development."));

            mockApplications.add(new Application("A101", "J101", "U101", ApplicationStatus.INPROGRESS,
                    new ArrayList<>(
                            List.of(new Assignment("Assign1", "U101", "Assignment 1", mockQuestions1, mockAnswers1))),
                    null));
            mockApplications.add(new Application("A102", "J102", "U102", ApplicationStatus.SUCCESSFUL,
                    new ArrayList<>(
                            List.of(new Assignment("Assign2", "U102", "Assignment 2", mockQuestions2, mockAnswers2))),
                    null));
            mockApplications.add(new Application("A103", "J103", "U101", ApplicationStatus.UNSUCCESSFUL,
                    new ArrayList<>(), null));

            mockedUtility.when(Utility::getApplications).thenReturn(mockApplications);

            spyObject.viewSubmittedAnswers("A101");
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Welcome to view submitted answers for the application A101"));
            Assert.assertTrue(consoleOutput.contains("Question:"));
            Assert.assertTrue(consoleOutput.contains("What is Java?"));
            Assert.assertTrue(consoleOutput.contains("Explain OOP concepts."));
            Assert.assertTrue(consoleOutput.contains("Answer:"));
            Assert.assertTrue(consoleOutput.contains("Java is a programming language."));
            Assert.assertTrue(consoleOutput.contains("OOP includes Encapsulation, Polymorphism."));
            outputStream.reset();

            spyObject.viewSubmittedAnswers("A103");
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Welcome to view submitted answers for the application A103"));
            Assert.assertFalse(consoleOutput.contains("Question:"));
            Assert.assertFalse(consoleOutput.contains("Answer:"));
            outputStream.reset();

            spyObject.viewSubmittedAnswers("A102");
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Welcome to view submitted answers for the application A102"));
            Assert.assertTrue(consoleOutput.contains("Question:"));
            Assert.assertTrue(consoleOutput.contains("What is your experience?"));
            Assert.assertTrue(consoleOutput.contains("Answer:"));
            Assert.assertTrue(consoleOutput.contains("I have 5 years of experience in software development."));
            outputStream.reset();

            spyObject.viewSubmittedAnswers("A999");
            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Welcome to view submitted answers for the application A999"));
            Assert.assertFalse(consoleOutput.contains("Question:"));
            Assert.assertFalse(consoleOutput.contains("Answer:"));
            outputStream.reset();
            Mockito.verify(spyObject, Mockito.times(4)).viewSubmittedAnswers(Mockito.anyString());
            mockedUtility.verify(Mockito.times(4), Utility::getApplications);

        }
    }

    @Test
    public void viewAllApplicationsTest() {
        try (MockedStatic<Utility> mockedUtility = mockStatic(Utility.class)) {
            List<Application> mockApplications = new ArrayList<>();
            ArrayList<Assignment> mockAssignments = new ArrayList<>();
            mockAssignments.add(new Assignment("1", "1", "DSA", new ArrayList<>(), new ArrayList<>()));
            mockApplications.add(new Application("1", "1", "101", ApplicationStatus.INPROGRESS, mockAssignments, 2,
                    "University of Leicester", "Java", "Good"));
            mockApplications.add(new Application("2", "2", "102", ApplicationStatus.UNSUCCESSFUL, mockAssignments, 2,
                    "University of Leicester", "Java", "Good"));

            User mockUser = new Recruiter("1", "John", "Doe", "johnDoe", "bestpassword");

            mockedUtility.when(Utility::getCurrentUser).thenReturn(mockUser);
            mockedUtility.when(Utility::getApplications).thenReturn(mockApplications);

            service.viewAllApplications();

            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Application ID: 1"));
            Assert.assertTrue(consoleOutput.contains("Status: INPROGRESS"));
            Assert.assertFalse(consoleOutput.contains("Application ID: 2"));
            Assert.assertFalse(consoleOutput.contains("Status: CLOSED"));
        }
    }

    @Test
    public void viewTotalNumberOfApplicationsTest() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {

            ArrayList<Application> apps = new ArrayList<>();

            Application app = new Application();

            app.setJobId("JOB1");

            apps.add(app);

            mockedUtility.when(() -> Utility.getApplications()).thenReturn(apps);

            String consoleOutput = outputStream.toString();

            Assert.assertFalse(consoleOutput.contains("Total Applications of : JOB1 is 1"));

            service.viewTotalNumberOfApplications("JOB1");

            consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Total Applications of : JOB1 is 1"));

            mockedUtility.verify(() -> Utility.getApplications());

        }
    }

    @Test
    public void deleteRecruiterProfileTest() throws IOException {
        Assert.assertNotNull(Utility.getCurrentUser());
        Utility.setCurrentUser(Utility.getUsers().get(1));
        service.deleteRecruiterProfile();
        Assert.assertNull(Utility.getCurrentUser());
    }

    @Test
    public void logoutRecruiterTest() {
        RecruiterService service = RecruiterService.getInstance();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class);
                MockedStatic<CommonService> mockedCommonService = Mockito.mockStatic(CommonService.class)) {

            CommonService mockCommonService = Mockito.mock(CommonService.class);
            mockedCommonService.when(CommonService::getInstance).thenReturn(mockCommonService);
            service.logoutRecruiter();

            String consoleOutput = outputStream.toString();
            assertTrue(consoleOutput.contains("Initializing logout process for Recruiter"));
            assertTrue(consoleOutput.contains("Logged out successfully"));
            mockedUtility.verify(() -> Utility.setCurrentUser(null));
        }
    }

    @Test
    public void visitSignInSignUpPageRecruiterTest() {
        RecruiterService recruiterService = RecruiterService.getInstance();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        try (MockedStatic<Utility> mockedUtility = mockStatic(Utility.class)) {
            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString()))
                    .thenReturn("1", "2", "3", "invalid");

            recruiterService.visitSignInSignUpPageRecruiter();
            String consoleOutput = outputStream.toString();
            assertTrue(consoleOutput.contains("Redirecting to Sign In page for Recruiter"));
            outputStream.reset();

            recruiterService.visitSignInSignUpPageRecruiter();
            consoleOutput = outputStream.toString();
            assertTrue(consoleOutput.contains("Redirecting to Sign Up page for Recruiter"));
            outputStream.reset();

            recruiterService.visitSignInSignUpPageRecruiter();
            consoleOutput = outputStream.toString();
            assertTrue(consoleOutput.contains("Redirecting to the previous menu"));
            outputStream.reset();

            recruiterService.visitSignInSignUpPageRecruiter();
            consoleOutput = outputStream.toString();
            assertTrue(consoleOutput.contains("You entered an invalid option. Please try again."));
        }
    }

    @Test
    public void sendInterviewTest() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        RecruiterService spyObject = Mockito.spy(service);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            Application mockApplication = new Application("1", "101", "201", ApplicationStatus.INPROGRESS,
                    new ArrayList<>(), 2,
                    "University of Leicester", "Java", "Good");
            mockedUtility.when(() -> Utility.inputOutput("Enter the interview date: ")).thenReturn("2025-01-03");
            mockedUtility.when(() -> Utility.inputOutput("Enter the interview time: ")).thenReturn("12:00");
            spyObject.sendInterview(mockApplication);
            String consoleOutput = outputStream.toString();
            Assert.assertTrue(consoleOutput.contains("Interview scheduled successfully"));
            Assert.assertTrue(mockApplication.getInterviewDate().equals("2025-01-03"));
            Assert.assertTrue(mockApplication.getInterviewTime().equals("12:00"));
            Assert.assertTrue(mockApplication.getStatus().equals(ApplicationStatus.INTERVIEW));
        }
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

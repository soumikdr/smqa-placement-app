package test.whitebox;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import model.Application;
import model.ApplicationStatus;
import model.User;
import model.UserRole;
import model.Assignment;
import service.RecruiterService;
import utility.Utility;


public class RecruiterServiceBranchTest {
    private ArrayList<Application> mockApplications;
    private ArrayList<User> mockUsers;
    private Application mockApplication;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Before
    public void setUp() {
        // Mock Assignments
        ArrayList<String> questions = new ArrayList<String>();
        questions.add("Question 1");
        questions.add("Question 2");

        ArrayList<String> answers = new ArrayList<String>();
        answers.add("1. Answer");
        answers.add("2. Answer");

        Assignment assignment = new Assignment("1", "1", "Java Assignment", questions, answers);
        ArrayList<Assignment> assignments = new ArrayList<>();
        assignments.add(assignment);
         
        // Mock applications
        mockApplications = new ArrayList<>();
        Application app1 = new Application("1", "1", "1", ApplicationStatus.INPROGRESS, assignments, 2, "BSc", "JS, CSS", "Feedback");
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
    public void testViewRecruiterDashboard_ViewProfile() {
        // Simulate user input "1"
        String input = "1";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        RecruiterService recruiterService = Mockito.spy(new RecruiterService());
        doNothing().when(recruiterService).viewRecruiterProfilePage();

        recruiterService.viewRecruiterDashboard();

        verify(recruiterService, times(1)).viewRecruiterProfilePage();
    }

    @Test
    public void testViewRecruiterDashboard_ViewAvailableJobs() {
        // Simulate user input "2"
        String input = "2";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        RecruiterService recruiterService = Mockito.spy(new RecruiterService());
        doNothing().when(recruiterService).viewAvailableJobs();

        recruiterService.viewRecruiterDashboard();

        verify(recruiterService, times(1)).viewAvailableJobs();
    }

    @Test
    public void testViewRecruiterDashboard_ViewAllApplications() {
        // Simulate user input "3"
        String input = "3";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        RecruiterService recruiterService = Mockito.spy(new RecruiterService());
        doNothing().when(recruiterService).viewAllApplications();

        recruiterService.viewRecruiterDashboard();

        verify(recruiterService, times(1)).viewAllApplications();
    }

    @Test
    public void testViewRecruiterDashboard_InvalidInput() {
        // Simulate invalid user input
        String input = "0";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        RecruiterService recruiterService = Mockito.spy(new RecruiterService());
        Mockito.doNothing().when(recruiterService).viewRecruiterDashboard();

        recruiterService.viewRecruiterDashboard();

        verify(recruiterService, times(1)).viewRecruiterDashboard();
    }

    @Test
    public void testViewSpecificApplication_ApplicationNotFound() {
    	try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            mockedUtility.when(Utility::getApplications).thenReturn(mockApplications);
            mockedUtility.when(Utility::getUsers).thenReturn(mockUsers);
            RecruiterService recruiterService = Mockito.spy(new RecruiterService());
            doNothing().when(recruiterService).viewAllApplications();

            // Invalid application ID given
            recruiterService.viewSpecificApplication("999g");

            String expectedOutput = "No application found with given id";
            assertEquals(expectedOutput, outContent.toString().trim());
        }
    }

    @Test
    public void testViewSpecificApplication_ApplicantNotFound() {
        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            mockedUtility.when(Utility::getApplications).thenReturn(mockApplications);

            // No users given, so applicant will not be found
            mockedUtility.when(Utility::getUsers).thenReturn(new ArrayList<>());
            RecruiterService recruiterService = Mockito.spy(new RecruiterService());
            doNothing().when(recruiterService).viewAllApplications();

            recruiterService.viewSpecificApplication("1");

            String expectedOutput = "No applicant found with for this application";
            assertEquals(expectedOutput, outContent.toString().trim());
        }
    }

    @Test
    public void testViewSpecificApplication_ValidApplicationAndUser() {
        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            mockedUtility.when(Utility::getApplications).thenReturn(mockApplications);
            mockedUtility.when(Utility::getUsers).thenReturn(mockUsers);

            RecruiterService recruiterService = Mockito.spy(new RecruiterService());
            doNothing().when(recruiterService).viewAllApplications();
            
            // Checking all the switch branches one by one
            mockedUtility.when(() -> Utility.inputOutput(anyString())).thenReturn("1");
            recruiterService.viewSpecificApplication("1");

            mockedUtility.when(() -> Utility.inputOutput(anyString())).thenReturn("2");
            recruiterService.viewSpecificApplication("1");

            mockedUtility.when(() -> Utility.inputOutput(anyString())).thenReturn("3");
            recruiterService.viewSpecificApplication("1");

            mockedUtility.when(() -> Utility.inputOutput(anyString())).thenReturn("4");
            recruiterService.viewSpecificApplication("1");

            mockedUtility.when(() -> Utility.inputOutput(anyString())).thenReturn("5");
            recruiterService.viewSpecificApplication("1");
            
            verify(recruiterService, times(1)).sendFeedback();
            verify(recruiterService, times(1)).approveRejectApplication(mockApplications.get(0));
            verify(recruiterService, times(1)).sendAssignment(mockApplications.get(0));
            verify(recruiterService, times(1)).sendInterview(mockApplications.get(0));
            verify(recruiterService, times(1)).viewAllApplications();
        }
    }

    @Test
    public void testViewSpecificApplication_InvalidOption() {
        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            mockedUtility.when(Utility::getApplications).thenReturn(mockApplications);
            mockedUtility.when(Utility::getUsers).thenReturn(mockUsers);

            // First input Invalid then one valid input
            mockedUtility.when(() -> Utility.inputOutput(anyString())).thenReturn("Invalid", "1");
            RecruiterService recruiterService = Mockito.spy(new RecruiterService());
            
            recruiterService.viewSpecificApplication("1");
            
            verify(recruiterService, times(2)).viewSpecificApplication("1");
        }
    }

    @Test
    public void testSendAssignment_ValidRole() {
        try (MockedStatic<Utility> utilities = mockStatic(Utility.class)) {
            // Mock dependencies
            Map<String, List<String>> questionMap = new HashMap<>();
            questionMap.put("frontend", Arrays.asList("Q1", "Q2"));
            utilities.when(Utility::getQuestionMap).thenReturn(questionMap);
            utilities.when(() -> Utility.inputOutput(anyString())).thenReturn("frontend");
            // Clear assignments if any
            mockApplication.setAssignments(new ArrayList<Assignment>());

            // Invoke the method
            RecruiterService recruiterService = Mockito.spy(new RecruiterService());
            recruiterService.sendAssignment(mockApplication);

            // Assertions
            assertEquals(1, mockApplication.getAssignments().size());
            Assignment assignment = mockApplication.getAssignments().get(0);
            assertEquals("Assignment frontend", assignment.getAssignmentName());
            assertEquals(Arrays.asList("Q1", "Q2"), assignment.getQuestions());
            assertEquals("1", assignment.getApplicantId());

            // Verify interaction with mocked methods
            utilities.verify(Utility::getQuestionMap);
            utilities.verify(() -> Utility.inputOutput(anyString()));
        }
    }

    @Test
    public void testSendAssignment_InvalidRole() {
        try (MockedStatic<Utility> utilities = mockStatic(Utility.class)) {
            // Arrange and mock dependencies
            Map<String, List<String>> questionMap = new HashMap<>();
            questionMap.put("frontend", Arrays.asList("Q1", "Q2"));
            utilities.when(Utility::getQuestionMap).thenReturn(questionMap);
            utilities.when(() -> Utility.inputOutput(anyString())).thenReturn("invalid_role");
            // Clear assignments if any
            mockApplication.setAssignments(new ArrayList<Assignment>());

            // Invoke the method
            RecruiterService recruiterService = Mockito.spy(new RecruiterService());
            recruiterService.sendAssignment(mockApplication);

            // Assertions
            assertEquals("0", Integer.toString(mockApplication.getAssignments().size()));

            // Verify interaction with mocked methods
            utilities.verify(Utility::getQuestionMap);
            utilities.verify(() -> Utility.inputOutput(anyString()));
        }
    }

    @Test
    public void testSendAssignment_NullRole() {
        try (MockedStatic<Utility> utilities = mockStatic(Utility.class)) {
            // Mock dependencies
            Map<String, List<String>> questionMap = new HashMap<>();
            questionMap.put("frontend", Arrays.asList("Q1", "Q2"));
            utilities.when(Utility::getQuestionMap).thenReturn(questionMap);
            utilities.when(() -> Utility.inputOutput(anyString())).thenReturn(null);
            // Clear assignments if any
            mockApplication.setAssignments(new ArrayList<Assignment>());

            // Invoke the method
            RecruiterService recruiterService = Mockito.spy(new RecruiterService());
            recruiterService.sendAssignment(mockApplication);

            // Assertions
            assertEquals("0", Integer.toString(mockApplication.getAssignments().size()));

            // Verify interaction with mocked methods
            utilities.verify(Utility::getQuestionMap);
            utilities.verify(() -> Utility.inputOutput(anyString()));
        }
    }

    @Test
    public void testSendAssignment_NoQuestionsInMap() {
        try (MockedStatic<Utility> utilities = mockStatic(Utility.class)) {
            // Mock dependencies
            Map<String, List<String>> questionMap = new HashMap<>();
            utilities.when(Utility::getQuestionMap).thenReturn(questionMap);
            utilities.when(() -> Utility.inputOutput(anyString())).thenReturn("frontend");
            // Clear assignments if any
            mockApplication.setAssignments(new ArrayList<Assignment>());

            // Invoke the method
            RecruiterService recruiterService = Mockito.spy(new RecruiterService());
            recruiterService.sendAssignment(mockApplication);

            // Assertions
            assertEquals("0", Integer.toString(mockApplication.getAssignments().size()));

            // Verify interaction with mocked methods
            utilities.verify(Utility::getQuestionMap);
            utilities.verify(() -> Utility.inputOutput(anyString()));
        }
    }

}

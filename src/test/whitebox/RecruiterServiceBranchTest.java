package test.whitebox;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import model.Application;
import model.User;
import model.UserRole;
import service.RecruiterService;
import utility.Utility;

public class RecruiterServiceBranchTest {
    private ArrayList<Application> mockApplications;
    private ArrayList<User> mockUsers;
    RecruiterService recruiterService;

    @Before
    public void setUp() {
        // Mock applications
        mockApplications = new ArrayList<>();
        Application app1 = new Application("1", "1", "1", "Pending", new ArrayList<>());
        mockApplications.add(app1);

        // Mock users
        mockUsers = new ArrayList<>();
        User user1 = new User("1", "John", "Doe", "johnDoe", "bestpassword", UserRole.APPLICANT);
        mockUsers.add(user1);
        
        // Mock service class
        recruiterService = Mockito.spy(new RecruiterService());
    }

    @Test
    public void testViewSpecificApplication_ApplicationNotFound() {
        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            mockedUtility.when(Utility::getApplications).thenReturn(new ArrayList<>());
        }

        recruiterService.viewSpecificApplication("999");	// Non existent application ID

        verify(utilityMock).getApplications();
        verify(recruiterService).viewAllApplications();
    }

    @Test
    public void testViewSpecificApplication_ApplicantNotFound() {
        Application application = new Application("appId", "jobId", "nonExistentApplicantId", "Pending", new ArrayList<>());
        List<Application> applications = new ArrayList<>();
        applications.add(application);

        when(utilityMock::getApplications).thenReturn(applications);
        when(utilityMock.getUsers()).thenReturn(new ArrayList<>());

        recruiterService.viewSpecificApplication("appId");

        verify(utilityMock).getUsers();
        verify(utilityMock).viewAllApplications();
    }

    @Test
    public void testViewSpecificApplication_ValidApplicationAndUser() {
        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class)) {
            mockedUtility.when(Utility::getApplications).thenReturn(mockApplications);
            mockedUtility.when(Utility::getUsers).thenReturn(mockUsers);
  
            recruiterService.viewSpecificApplication("1");
            
            verify(mockedUtility).getApplications();
            verify(mockedUtility).getUsers();
            // Verify output methods for correct details if applicable
        }
    }

    @Test
    public void testViewSpecificApplication_InvalidOption() {
        Application application = new Application("appId", "jobId", "userId", "Pending", new ArrayList<>());
        User user = new User("1", "John", "Doe", "john", "password", UserRole.APPLICANT);

        List<Application> applications = new ArrayList<>();
        applications.add(application);

        List<User> users = new ArrayList<>();
        users.add(user);

        when(utilityMock.getApplications()).thenReturn(applications);
        when(utilityMock.getUsers()).thenReturn(users);
        when(utilityMock.inputOutput(anyString())).thenReturn("invalid");

        recruiterService.viewSpecificApplication("appId");

        verify(utilityMock, times(1)).inputOutput(anyString());
        verify(utilityMock).viewSpecificApplication("appId");
    }
}

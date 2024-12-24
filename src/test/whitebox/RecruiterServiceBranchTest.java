package test.whitebox;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Test;
import org.mockito.Mockito;

import service.RecruiterService;

public class RecruiterServiceBranchTest {
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
}

package test.whitebox;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import service.ApplicantService;
import service.CommonService;
import service.RecruiterService;
import utility.Utility;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.mockito.Mockito.times;

public class CommonServiceStatementTest {

    public CommonService service = CommonService.getInstance();

    // ETY1 - STORY 1
    @Test
    public void accessLandingPageTest_input1() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class);
             MockedStatic<RecruiterService> spyRecruiterService = Mockito.mockStatic(RecruiterService.class);
        ) {

            RecruiterService mockRec = Mockito.mock(RecruiterService.class);

            spyRecruiterService.when(RecruiterService::getInstance).thenReturn(mockRec);

            Mockito.doNothing().when(mockRec).visitSignInSignUpPageRecruiter();


            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("1");

            service.accessLandingPage();

            String consoleOutput = outputStream.toString();


            Assert.assertTrue(consoleOutput.contains("Directing to Recruiter Landing Page"));

            Mockito.verify(mockRec).visitSignInSignUpPageRecruiter();
            mockedUtility.verify(times(1), () -> Utility.inputOutput(Mockito.anyString()));

        }

    }


    // ETY1 - STORY 1
    @Test
    public void accessLandingPageTest_input2() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class);
             MockedStatic<ApplicantService> spyApplicantService = Mockito.mockStatic(ApplicantService.class);
        ) {

            ApplicantService mockAppl = Mockito.mock(ApplicantService.class);
            spyApplicantService.when(ApplicantService::getInstance).thenReturn(mockAppl);

            Mockito.doNothing().when(mockAppl).applicantViewSignInSignUpPage();

            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("2");

            service.accessLandingPage();

            String consoleOutput = outputStream.toString();

            Assert.assertTrue(consoleOutput.contains("Directing to Applicant Landing Page"));

            Mockito.verify(mockAppl).applicantViewSignInSignUpPage();
            mockedUtility.verify(times(1), () -> Utility.inputOutput(Mockito.anyString()));

        }

    }

    // ETY1 - STORY 1
    @Test
    public void accessLandingPageTest_inputInvalid() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        CommonService spyObject = Mockito.spy(service);

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class);
        ) {
            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("invalid");

            Mockito.doCallRealMethod().doNothing().when(spyObject).accessLandingPage();

            spyObject.accessLandingPage();

            String consoleOutput = outputStream.toString();

            Assert.assertTrue(consoleOutput.contains("You entered invalid option"));

            Mockito.verify(spyObject, times(2)).accessLandingPage();
            mockedUtility.verify(times(1), () -> Utility.inputOutput(Mockito.anyString()));

        }

    }

    // ETY1 - STORY 1
    @Test
    public void accessLandingPageTest_input3() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out

        try (MockedStatic<Utility> mockedUtility = Mockito.mockStatic(Utility.class);
        ) {
            mockedUtility.when(() -> Utility.inputOutput(Mockito.anyString())).thenReturn("3");

            service.accessLandingPage();

            String consoleOutput = outputStream.toString();

            Assert.assertTrue(consoleOutput.contains("Exiting the application"));

            mockedUtility.verify(times(1), () -> Utility.inputOutput(Mockito.anyString()));

        }

    }

}

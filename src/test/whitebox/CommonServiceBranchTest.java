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

public class CommonServiceBranchTest {

    public CommonService service = CommonService.getInstance();

    // ETY1 - STORY 1
    @Test
    public void accessLandingPageTest() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out
        CommonService spyObject = Mockito.spy(service);


        try(MockedStatic<Utility> mockedUtility=Mockito.mockStatic(Utility.class);
            MockedStatic<RecruiterService> spyRecruiterService = Mockito.mockStatic(RecruiterService.class);
            MockedStatic<ApplicantService> spyApplicantService = Mockito.mockStatic(ApplicantService.class);
        ){

            RecruiterService mockRec=Mockito.mock(RecruiterService.class);
            ApplicantService mockAppl=Mockito.mock(ApplicantService.class);

            spyRecruiterService.when(RecruiterService::getInstance).thenReturn(mockRec);
            spyApplicantService.when(ApplicantService::getInstance).thenReturn(mockAppl);

            Mockito.doNothing().when(mockRec).visitSignInSignUpPageRecruiter();
            Mockito.doNothing().when(mockAppl).applicantViewSignInSignUpPage();


            mockedUtility.when(()->Utility.inputOutput(Mockito.anyString())).thenReturn("1");

            spyObject.accessLandingPage();

            String consoleOutput = outputStream.toString();

            Assert.assertTrue(consoleOutput.contains("Directing to Recruiter Landing Page"));

            outputStream.reset();

            mockedUtility.when(()->Utility.inputOutput(Mockito.anyString())).thenReturn("2");

            spyObject.accessLandingPage();

            consoleOutput = outputStream.toString();

            Assert.assertTrue(consoleOutput.contains("Directing to Applicant Landing Page"));

            outputStream.reset();

            mockedUtility.when(()->Utility.inputOutput(Mockito.anyString())).thenReturn("3");

            spyObject.accessLandingPage();

            consoleOutput = outputStream.toString();

            Assert.assertTrue(consoleOutput.contains("Exiting the application"));

            outputStream.reset();

            mockedUtility.when(()->Utility.inputOutput(Mockito.anyString())).thenReturn("invalid");

            Mockito.doCallRealMethod().doNothing().when(spyObject).accessLandingPage();

            spyObject.accessLandingPage();

            consoleOutput = outputStream.toString();

            Assert.assertTrue(consoleOutput.contains("You entered invalid option"));

            Mockito.verify(mockAppl).applicantViewSignInSignUpPage();
            Mockito.verify(mockRec).visitSignInSignUpPageRecruiter();
            Mockito.verify(spyObject,times(5)).accessLandingPage();
            mockedUtility.verify(times(4),()->Utility.inputOutput(Mockito.anyString()));

        }
    }
}

package test.blackbox.RandomTesting;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ErrorTest0 {

    public static boolean debug = false;

    public void assertBooleanArrayEquals(boolean[] expectedArray, boolean[] actualArray) {
        if (expectedArray.length != actualArray.length) {
            throw new AssertionError("Array lengths differ: " + expectedArray.length + " != " + actualArray.length);
        }
        for (int i = 0; i < expectedArray.length; i++) {
            if (expectedArray[i] != actualArray[i]) {
                throw new AssertionError("Arrays differ at index " + i + ": " + expectedArray[i] + " != " + actualArray[i]);
            }
        }
    }

    @Test
    public void test001() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test001");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.viewApplicantApplications();
    }

    @Test
    public void test002() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test002");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.viewAssessment("hi!");
    }

    @Test
    public void test003() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test003");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.viewJobPost();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.resetPassword("");
    }

    @Test
    public void test004() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test004");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.deleteProfileHelper();
    }

    @Test
    public void test005() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test005");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.submitInterviewForm("hi!");
    }

    @Test
    public void test006() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test006");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.viewJobPost();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.deleteProfileHelper();
    }

    @Test
    public void test007() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test007");
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        model.User[] userArray1 = new model.User[] {};
        java.util.ArrayList<model.User> userList2 = new java.util.ArrayList<model.User>();
        boolean boolean3 = java.util.Collections.addAll((java.util.Collection<model.User>) userList2, userArray1);
        model.User user6 = recruiterService0.authenticateUser(userList2, "", "");
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        recruiterService0.updateRecruiterProfile();
    }

    @Test
    public void test008() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test008");
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        model.User[] userArray1 = new model.User[] {};
        java.util.ArrayList<model.User> userList2 = new java.util.ArrayList<model.User>();
        boolean boolean3 = java.util.Collections.addAll((java.util.Collection<model.User>) userList2, userArray1);
        model.User user6 = recruiterService0.authenticateUser(userList2, "", "");
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        recruiterService0.deleteRecruiterProfile();
    }

    @Test
    public void test009() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test009");
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        model.User[] userArray1 = new model.User[] {};
        java.util.ArrayList<model.User> userList2 = new java.util.ArrayList<model.User>();
        boolean boolean3 = java.util.Collections.addAll((java.util.Collection<model.User>) userList2, userArray1);
        model.User user6 = recruiterService0.authenticateUser(userList2, "", "");
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        recruiterService0.viewSpecificApplication("hi!");
    }

    @Test
    public void test010() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test010");
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        model.User[] userArray1 = new model.User[] {};
        java.util.ArrayList<model.User> userList2 = new java.util.ArrayList<model.User>();
        boolean boolean3 = java.util.Collections.addAll((java.util.Collection<model.User>) userList2, userArray1);
        model.User user6 = recruiterService0.authenticateUser(userList2, "", "");
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        recruiterService0.recruiterSignInPage();
    }

    @Test
    public void test011() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test011");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.deleteApplicantProfile();
        applicantService0.viewAllAvailableJobs();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.deleteProfileHelper();
    }

    @Test
    public void test012() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test012");
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        recruiterService0.updateStatusOfJobPost("hi!");
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        recruiterService0.viewRecruiterProfilePage();
    }

    @Test
    public void test013() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test013");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.viewJobPost();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.submitAssessmentForm("hi!");
    }

    @Test
    public void test014() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test014");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.deleteApplicantProfile();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.viewFeedback("");
    }

    @Test
    public void test015() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test015");
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        recruiterService0.updateStatusOfJobPost("hi!");
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        recruiterService0.deleteRecruiterProfile();
    }

    @Test
    public void test016() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test016");
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        recruiterService0.updateStatusOfJobPost("hi!");
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        recruiterService0.resetPasswordRecruiter("");
    }

    @Test
    public void test017() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test017");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.viewSpecificApplication("hi!");
    }

    @Test
    public void test018() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test018");
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        recruiterService0.updateStatusOfJobPost("hi!");
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        recruiterService0.updateRecruiterProfile();
    }

    @Test
    public void test019() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test019");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.viewJobPost();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.viewInterview("");
    }

    @Test
    public void test020() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test020");
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        recruiterService0.updateStatusOfJobPost("hi!");
        model.User[] userArray3 = new model.User[] {};
        java.util.ArrayList<model.User> userList4 = new java.util.ArrayList<model.User>();
        boolean boolean5 = java.util.Collections.addAll((java.util.Collection<model.User>) userList4, userArray3);
        model.User user8 = recruiterService0.authenticateUser(userList4, "", "hi!");
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        recruiterService0.updateRecruiterProfile();
    }

    @Test
    public void test021() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test021");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.viewJobPost();
        applicantService0.viewAllAvailableJobs();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.viewApplicantProfilePage();
    }

    @Test
    public void test022() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test022");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.deleteApplicantProfile();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.deleteProfileHelper();
    }

    @Test
    public void test023() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test023");
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        recruiterService0.viewAssessmentResult("hi!", "hi!");
    }

    @Test
    public void test024() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test024");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.deleteApplicantProfile();
        applicantService0.viewAllAvailableJobs();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.viewInterview("hi!");
    }

    @Test
    public void test025() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test025");
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        recruiterService0.viewTotalNumberOfApplications("");
    }

    @Test
    public void test026() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test026");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.deleteApplicantProfile();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.viewJobDescFromApplication("");
    }

    @Test
    public void test027() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test027");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.deleteApplicantProfile();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.viewApplicantProfilePage();
    }

    @Test
    public void test028() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test028");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.viewJobPost();
        applicantService0.viewAllAvailableJobs();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.viewInterview("");
    }

    @Test
    public void test029() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test029");
        service.ApplicantService applicantService0 = service.ApplicantService.getInstance();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.deleteProfileHelper();
    }

    @Test
    public void test030() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test030");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.viewJobPost();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.withdrawApplication("hi!");
    }

    @Test
    public void test031() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test031");
        service.ApplicantService applicantService0 = service.ApplicantService.getInstance();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.submitAssessmentForm("");
    }

    @Test
    public void test032() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test032");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.deleteApplicantProfile();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.viewAssessment("hi!");
    }

    @Test
    public void test033() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test033");
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        model.User[] userArray1 = new model.User[] {};
        java.util.ArrayList<model.User> userList2 = new java.util.ArrayList<model.User>();
        boolean boolean3 = java.util.Collections.addAll((java.util.Collection<model.User>) userList2, userArray1);
        model.User user6 = recruiterService0.authenticateUser(userList2, "", "");
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        recruiterService0.viewAssessmentResult("", "hi!");
    }

    @Test
    public void test034() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test034");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.viewJobPost();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.viewAssessment("hi!");
    }

    @Test
    public void test035() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test035");
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        recruiterService0.viewAssessmentResult("hi!", "");
    }

    @Test
    public void test036() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test036");
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        recruiterService0.resetPasswordRecruiter("");
    }

    @Test
    public void test037() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test037");
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        recruiterService0.viewRecruiterProfilePage();
    }

    @Test
    public void test038() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test038");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.viewInterview("");
    }

    @Test
    public void test039() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test039");
        service.ApplicantService applicantService0 = service.ApplicantService.getInstance();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.signUp();
    }

    @Test
    public void test040() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test040");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.viewJobPost();
        applicantService0.viewAllAvailableJobs();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.viewSpecificApplication("hi!");
    }

    @Test
    public void test041() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test041");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.signIn();
    }

    @Test
    public void test042() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test042");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.viewJobPost();
        applicantService0.viewAllAvailableJobs();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.submitAssessmentForm("hi!");
    }

    @Test
    public void test043() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test043");
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        model.User[] userArray1 = new model.User[] {};
        java.util.ArrayList<model.User> userList2 = new java.util.ArrayList<model.User>();
        boolean boolean3 = java.util.Collections.addAll((java.util.Collection<model.User>) userList2, userArray1);
        model.User user6 = recruiterService0.authenticateUser(userList2, "", "");
        recruiterService0.updateStatusOfJobPost("hi!");
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        recruiterService0.resetPasswordRecruiter("hi!");
    }

    @Test
    public void test044() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test044");
        service.RecruiterService recruiterService0 = new service.RecruiterService();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        recruiterService0.viewSpecificApplication("hi!");
    }

    @Test
    public void test045() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test045");
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        recruiterService0.updateStatusOfJobPost("hi!");
        model.User[] userArray3 = new model.User[] {};
        java.util.ArrayList<model.User> userList4 = new java.util.ArrayList<model.User>();
        boolean boolean5 = java.util.Collections.addAll((java.util.Collection<model.User>) userList4, userArray3);
        model.User user8 = recruiterService0.authenticateUser(userList4, "", "hi!");
        service.RecruiterService recruiterService9 = service.RecruiterService.getInstance();
        recruiterService9.updateStatusOfJobPost("hi!");
        model.User[] userArray12 = new model.User[] {};
        java.util.ArrayList<model.User> userList13 = new java.util.ArrayList<model.User>();
        boolean boolean14 = java.util.Collections.addAll((java.util.Collection<model.User>) userList13, userArray12);
        model.User user17 = recruiterService9.authenticateUser(userList13, "", "hi!");
        model.User user20 = recruiterService0.authenticateUser(userList13, "hi!", "hi!");
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        recruiterService0.viewAssessmentResult("", "hi!");
    }

    @Test
    public void test046() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test046");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.deleteApplicantProfile();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.submitAssessmentForm("hi!");
    }

    @Test
    public void test047() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test047");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.deleteApplicantProfile();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.viewInterview("");
    }

    @Test
    public void test048() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test048");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.deleteApplicantProfile();
        applicantService0.viewAllAvailableJobs();
        applicantService0.deleteApplicantProfile();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.viewAssessment("hi!");
    }

    @Test
    public void test049() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test049");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.deleteApplicantProfile();
        applicantService0.viewAllAvailableJobs();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.viewAssessment("");
    }

    @Test
    public void test050() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test050");
        service.RecruiterService recruiterService0 = new service.RecruiterService();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        recruiterService0.updateRecruiterProfile();
    }

    @Test
    public void test051() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test051");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.deleteApplicantProfile();
        applicantService0.viewAllAvailableJobs();
        applicantService0.viewJobPost();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.submitInterviewForm("");
    }

    @Test
    public void test052() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test052");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.viewAssessment("");
    }

    @Test
    public void test053() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test053");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.deleteApplicantProfile();
        applicantService0.viewAllAvailableJobs();
        applicantService0.deleteApplicantProfile();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.deleteProfileHelper();
    }

    @Test
    public void test054() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test054");
        service.ApplicantService applicantService0 = service.ApplicantService.getInstance();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.resetPassword("hi!");
    }

    @Test
    public void test055() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test055");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.viewJobPost();
        applicantService0.viewAllAvailableJobs();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.withdrawApplication("hi!");
    }

    @Test
    public void test056() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test056");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.viewFeedback("");
    }

    @Test
    public void test057() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test057");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.deleteApplicantProfile();
        applicantService0.viewAllAvailableJobs();
        applicantService0.viewJobPost();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.withdrawApplication("");
    }

    @Test
    public void test058() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test058");
        service.RecruiterService recruiterService0 = new service.RecruiterService();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        recruiterService0.viewTotalNumberOfApplications("hi!");
    }

    @Test
    public void test059() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test059");
        service.ApplicantService applicantService0 = service.ApplicantService.getInstance();
        applicantService0.viewAllAvailableJobs();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.withdrawApplication("hi!");
    }

    @Test
    public void test060() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test060");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.viewJobPost();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.withdrawApplication("");
    }

    @Test
    public void test061() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test061");
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        model.User[] userArray1 = new model.User[] {};
        java.util.ArrayList<model.User> userList2 = new java.util.ArrayList<model.User>();
        boolean boolean3 = java.util.Collections.addAll((java.util.Collection<model.User>) userList2, userArray1);
        model.User user6 = recruiterService0.authenticateUser(userList2, "", "");
        recruiterService0.submitNewJobPost("hi!", "hi!");
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        recruiterService0.recruiterSignInPage();
    }

    @Test
    public void test062() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test062");
        service.RecruiterService recruiterService0 = new service.RecruiterService();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        recruiterService0.recruiterSignInPage();
    }

    @Test
    public void test063() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test063");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.viewJobPost();
        applicantService0.viewAllAvailableJobs();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.showUpdateProfilePage();
    }

    @Test
    public void test064() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test064");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.deleteApplicantProfile();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.signUp();
    }

    @Test
    public void test065() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test065");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.deleteApplicantProfile();
        applicantService0.viewAllAvailableJobs();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.submitAssessmentForm("");
    }

    @Test
    public void test066() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test066");
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        recruiterService0.updateStatusOfJobPost("hi!");
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        recruiterService0.recruiterSignInPage();
    }

    @Test
    public void test067() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test067");
        service.ApplicantService applicantService0 = service.ApplicantService.getInstance();
        applicantService0.viewAllAvailableJobs();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.deleteProfileHelper();
    }

    @Test
    public void test068() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test068");
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        model.User[] userArray1 = new model.User[] {};
        java.util.ArrayList<model.User> userList2 = new java.util.ArrayList<model.User>();
        boolean boolean3 = java.util.Collections.addAll((java.util.Collection<model.User>) userList2, userArray1);
        model.User user6 = recruiterService0.authenticateUser(userList2, "", "");
        recruiterService0.updateStatusOfJobPost("hi!");
        recruiterService0.submitNewJobPost("hi!", "hi!");
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        recruiterService0.updateRecruiterProfile();
    }

    @Test
    public void test069() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test069");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.deleteApplicantProfile();
        applicantService0.viewAllAvailableJobs();
        applicantService0.deleteApplicantProfile();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.viewApplicantProfilePage();
    }

    @Test
    public void test070() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test070");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.deleteApplicantProfile();
        applicantService0.viewAllAvailableJobs();
        applicantService0.deleteApplicantProfile();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.viewSpecificApplication("hi!");
    }

    @Test
    public void test071() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test071");
        service.ApplicantService applicantService0 = service.ApplicantService.getInstance();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.viewFeedback("");
    }

    @Test
    public void test072() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test072");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.signUp();
    }

    @Test
    public void test073() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test073");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.deleteApplicantProfile();
        applicantService0.viewAllAvailableJobs();
        applicantService0.viewJobPost();
        applicantService0.viewAllAvailableJobs();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.withdrawApplication("");
    }

    @Test
    public void test074() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test074");
        service.ApplicantService applicantService0 = service.ApplicantService.getInstance();
        applicantService0.viewAllAvailableJobs();
        applicantService0.deleteApplicantProfile();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.signUp();
    }

    @Test
    public void test075() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test075");
        service.ApplicantService applicantService0 = service.ApplicantService.getInstance();
        applicantService0.viewAllAvailableJobs();
        applicantService0.deleteApplicantProfile();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.viewJobDescFromApplication("");
    }

    @Test
    public void test076() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test076");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.deleteApplicantProfile();
        applicantService0.viewAllAvailableJobs();
        applicantService0.viewJobPost();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.submitInterviewForm("hi!");
    }

    @Test
    public void test077() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test077");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.deleteApplicantProfile();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.viewSpecificApplication("");
    }

    @Test
    public void test078() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test078");
        service.ApplicantService applicantService0 = service.ApplicantService.getInstance();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.signIn();
    }

    @Test
    public void test079() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test079");
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        model.User[] userArray1 = new model.User[] {};
        java.util.ArrayList<model.User> userList2 = new java.util.ArrayList<model.User>();
        boolean boolean3 = java.util.Collections.addAll((java.util.Collection<model.User>) userList2, userArray1);
        model.User user6 = recruiterService0.authenticateUser(userList2, "", "");
        model.User[] userArray7 = new model.User[] {};
        java.util.ArrayList<model.User> userList8 = new java.util.ArrayList<model.User>();
        boolean boolean9 = java.util.Collections.addAll((java.util.Collection<model.User>) userList8, userArray7);
        model.User user12 = recruiterService0.authenticateUser(userList8, "", "hi!");
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        recruiterService0.updateRecruiterProfile();
    }

    @Test
    public void test080() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test080");
        service.ApplicantService applicantService0 = service.ApplicantService.getInstance();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.viewSpecificApplication("");
    }

    @Test
    public void test081() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test081");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.deleteApplicantProfile();
        applicantService0.viewAllAvailableJobs();
        applicantService0.deleteApplicantProfile();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.viewApplicantApplications();
    }

    @Test
    public void test082() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test082");
        service.ApplicantService applicantService0 = service.ApplicantService.getInstance();
        applicantService0.viewAllAvailableJobs();
        applicantService0.deleteApplicantProfile();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.submitAssessmentForm("hi!");
    }

    @Test
    public void test083() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test083");
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        model.User[] userArray1 = new model.User[] {};
        java.util.ArrayList<model.User> userList2 = new java.util.ArrayList<model.User>();
        boolean boolean3 = java.util.Collections.addAll((java.util.Collection<model.User>) userList2, userArray1);
        model.User user6 = recruiterService0.authenticateUser(userList2, "", "");
        model.User[] userArray7 = new model.User[] {};
        java.util.ArrayList<model.User> userList8 = new java.util.ArrayList<model.User>();
        boolean boolean9 = java.util.Collections.addAll((java.util.Collection<model.User>) userList8, userArray7);
        model.User user12 = recruiterService0.authenticateUser(userList8, "", "hi!");
        recruiterService0.updateStatusOfJobPost("hi!");
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        recruiterService0.viewRecruiterProfilePage();
    }

    @Test
    public void test084() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test084");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.deleteApplicantProfile();
        applicantService0.viewAllAvailableJobs();
        applicantService0.viewJobPost();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.showUpdateProfilePage();
    }

    @Test
    public void test085() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test085");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.deleteApplicantProfile();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.withdrawApplication("");
    }

    @Test
    public void test086() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test086");
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        model.User[] userArray1 = new model.User[] {};
        java.util.ArrayList<model.User> userList2 = new java.util.ArrayList<model.User>();
        boolean boolean3 = java.util.Collections.addAll((java.util.Collection<model.User>) userList2, userArray1);
        model.User user6 = recruiterService0.authenticateUser(userList2, "", "");
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        recruiterService0.viewTotalNumberOfApplications("hi!");
    }

    @Test
    public void test087() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test087");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.viewJobPost();
        applicantService0.viewAllAvailableJobs();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.viewApplicantApplications();
    }

    @Test
    public void test088() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test088");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.deleteApplicantProfile();
        applicantService0.viewAllAvailableJobs();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.resetPassword("hi!");
    }

    @Test
    public void test089() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test089");
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        model.User[] userArray1 = new model.User[] {};
        java.util.ArrayList<model.User> userList2 = new java.util.ArrayList<model.User>();
        boolean boolean3 = java.util.Collections.addAll((java.util.Collection<model.User>) userList2, userArray1);
        model.User user6 = recruiterService0.authenticateUser(userList2, "", "");
        recruiterService0.updateStatusOfJobPost("hi!");
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        recruiterService0.deleteRecruiterProfile();
    }

    @Test
    public void test090() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test090");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.deleteApplicantProfile();
        applicantService0.viewAllAvailableJobs();
        applicantService0.viewJobPost();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.viewApplicantApplications();
    }

    @Test
    public void test091() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test091");
        service.RecruiterService recruiterService0 = new service.RecruiterService();
        recruiterService0.submitNewJobPost("", "");
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        recruiterService0.viewAssessmentResult("hi!", "hi!");
    }

    @Test
    public void test092() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test092");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.deleteApplicantProfile();
        applicantService0.viewAllAvailableJobs();
        applicantService0.viewJobPost();
        applicantService0.viewAllAvailableJobs();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.submitAssessmentForm("");
    }

    @Test
    public void test093() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test093");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.deleteApplicantProfile();
        applicantService0.viewAllAvailableJobs();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.signUp();
    }

    @Test
    public void test094() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test094");
        service.ApplicantService applicantService0 = service.ApplicantService.getInstance();
        applicantService0.viewAllAvailableJobs();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.showUpdateProfilePage();
    }

    @Test
    public void test095() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test095");
        service.RecruiterService recruiterService0 = new service.RecruiterService();
        recruiterService0.submitNewJobPost("", "");
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        recruiterService0.resetPasswordRecruiter("hi!");
    }

    @Test
    public void test096() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test096");
        service.ApplicantService applicantService0 = service.ApplicantService.getInstance();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.viewInterview("");
    }

    @Test
    public void test097() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test097");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.deleteApplicantProfile();
        applicantService0.viewAllAvailableJobs();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.submitInterviewForm("hi!");
    }

    @Test
    public void test098() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test098");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.deleteApplicantProfile();
        applicantService0.viewAllAvailableJobs();
        applicantService0.viewJobPost();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.viewFeedback("hi!");
    }

    @Test
    public void test099() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test099");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.deleteApplicantProfile();
        applicantService0.viewAllAvailableJobs();
        applicantService0.viewJobPost();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.submitAssessmentForm("");
    }

    @Test
    public void test100() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test100");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.viewJobPost();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.showUpdateProfilePage();
    }

    @Test
    public void test101() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test101");
        service.ApplicantService applicantService0 = service.ApplicantService.getInstance();
        applicantService0.viewAllAvailableJobs();
        applicantService0.deleteApplicantProfile();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.submitAssessmentForm("");
    }

    @Test
    public void test102() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test102");
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        model.User[] userArray1 = new model.User[] {};
        java.util.ArrayList<model.User> userList2 = new java.util.ArrayList<model.User>();
        boolean boolean3 = java.util.Collections.addAll((java.util.Collection<model.User>) userList2, userArray1);
        model.User user6 = recruiterService0.authenticateUser(userList2, "", "");
        recruiterService0.submitNewJobPost("hi!", "hi!");
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        recruiterService0.viewAssessmentResult("hi!", "hi!");
    }

    @Test
    public void test103() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test103");
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        model.User[] userArray1 = new model.User[] {};
        java.util.ArrayList<model.User> userList2 = new java.util.ArrayList<model.User>();
        boolean boolean3 = java.util.Collections.addAll((java.util.Collection<model.User>) userList2, userArray1);
        model.User user6 = recruiterService0.authenticateUser(userList2, "", "");
        model.User[] userArray7 = new model.User[] {};
        java.util.ArrayList<model.User> userList8 = new java.util.ArrayList<model.User>();
        boolean boolean9 = java.util.Collections.addAll((java.util.Collection<model.User>) userList8, userArray7);
        model.User user12 = recruiterService0.authenticateUser(userList8, "", "hi!");
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        recruiterService0.deleteRecruiterProfile();
    }

    @Test
    public void test104() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test104");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.viewJobPost();
        applicantService0.viewAllAvailableJobs();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.deleteProfileHelper();
    }

    @Test
    public void test105() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test105");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.viewJobPost();
        applicantService0.viewAllAvailableJobs();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.viewInterview("hi!");
    }

    @Test
    public void test106() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test106");
        service.RecruiterService recruiterService0 = new service.RecruiterService();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        recruiterService0.viewAssessmentResult("", "hi!");
    }

    @Test
    public void test107() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test107");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.viewApplicantProfilePage();
    }

    @Test
    public void test108() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test108");
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        model.User[] userArray1 = new model.User[] {};
        java.util.ArrayList<model.User> userList2 = new java.util.ArrayList<model.User>();
        boolean boolean3 = java.util.Collections.addAll((java.util.Collection<model.User>) userList2, userArray1);
        model.User user6 = recruiterService0.authenticateUser(userList2, "", "");
        model.User[] userArray7 = new model.User[] {};
        java.util.ArrayList<model.User> userList8 = new java.util.ArrayList<model.User>();
        boolean boolean9 = java.util.Collections.addAll((java.util.Collection<model.User>) userList8, userArray7);
        model.User user12 = recruiterService0.authenticateUser(userList8, "", "hi!");
        recruiterService0.updateStatusOfJobPost("hi!");
        recruiterService0.updateStatusOfJobPost("");
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        recruiterService0.viewRecruiterProfilePage();
    }

    @Test
    public void test109() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test109");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.deleteApplicantProfile();
        applicantService0.viewAllAvailableJobs();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.viewApplicantProfilePage();
    }

    @Test
    public void test110() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test110");
        service.RecruiterService recruiterService0 = new service.RecruiterService();
        model.User[] userArray1 = new model.User[] {};
        java.util.ArrayList<model.User> userList2 = new java.util.ArrayList<model.User>();
        boolean boolean3 = java.util.Collections.addAll((java.util.Collection<model.User>) userList2, userArray1);
        model.User user6 = recruiterService0.authenticateUser(userList2, "", "");
        recruiterService0.submitNewJobPost("", "hi!");
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        recruiterService0.viewTotalNumberOfApplications("hi!");
    }

    @Test
    public void test111() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test111");
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        recruiterService0.updateStatusOfJobPost("hi!");
        model.User[] userArray3 = new model.User[] {};
        java.util.ArrayList<model.User> userList4 = new java.util.ArrayList<model.User>();
        boolean boolean5 = java.util.Collections.addAll((java.util.Collection<model.User>) userList4, userArray3);
        model.User user8 = recruiterService0.authenticateUser(userList4, "", "hi!");
        service.RecruiterService recruiterService9 = service.RecruiterService.getInstance();
        recruiterService9.updateStatusOfJobPost("hi!");
        model.User[] userArray12 = new model.User[] {};
        java.util.ArrayList<model.User> userList13 = new java.util.ArrayList<model.User>();
        boolean boolean14 = java.util.Collections.addAll((java.util.Collection<model.User>) userList13, userArray12);
        model.User user17 = recruiterService9.authenticateUser(userList13, "", "hi!");
        model.User user20 = recruiterService0.authenticateUser(userList13, "hi!", "hi!");
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        recruiterService0.deleteRecruiterProfile();
    }

    @Test
    public void test112() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test112");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.resetPassword("");
    }

    @Test
    public void test113() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test113");
        service.ApplicantService applicantService0 = service.ApplicantService.getInstance();
        applicantService0.viewAllAvailableJobs();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.signUp();
    }

    @Test
    public void test114() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test114");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.deleteApplicantProfile();
        applicantService0.viewAllAvailableJobs();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.viewSpecificApplication("");
    }

    @Test
    public void test115() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test115");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.deleteApplicantProfile();
        applicantService0.viewJobPost();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.deleteProfileHelper();
    }

    @Test
    public void test116() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test116");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.deleteApplicantProfile();
        applicantService0.viewAllAvailableJobs();
        applicantService0.viewJobPost();
        applicantService0.viewAllAvailableJobs();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.viewSpecificApplication("");
    }

    @Test
    public void test117() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test117");
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        model.User[] userArray1 = new model.User[] {};
        java.util.ArrayList<model.User> userList2 = new java.util.ArrayList<model.User>();
        boolean boolean3 = java.util.Collections.addAll((java.util.Collection<model.User>) userList2, userArray1);
        model.User user6 = recruiterService0.authenticateUser(userList2, "", "");
        recruiterService0.submitNewJobPost("hi!", "hi!");
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        recruiterService0.viewAssessmentResult("hi!", "");
    }

    @Test
    public void test118() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test118");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.deleteApplicantProfile();
        applicantService0.viewAllAvailableJobs();
        applicantService0.viewAllAvailableJobs();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.signUp();
    }

    @Test
    public void test119() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test119");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.viewJobPost();
        applicantService0.viewAllAvailableJobs();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.signIn();
    }

    @Test
    public void test120() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test120");
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        recruiterService0.updateStatusOfJobPost("hi!");
        model.User[] userArray3 = new model.User[] {};
        java.util.ArrayList<model.User> userList4 = new java.util.ArrayList<model.User>();
        boolean boolean5 = java.util.Collections.addAll((java.util.Collection<model.User>) userList4, userArray3);
        model.User user8 = recruiterService0.authenticateUser(userList4, "", "hi!");
        service.RecruiterService recruiterService9 = service.RecruiterService.getInstance();
        recruiterService9.updateStatusOfJobPost("hi!");
        model.User[] userArray12 = new model.User[] {};
        java.util.ArrayList<model.User> userList13 = new java.util.ArrayList<model.User>();
        boolean boolean14 = java.util.Collections.addAll((java.util.Collection<model.User>) userList13, userArray12);
        model.User user17 = recruiterService9.authenticateUser(userList13, "", "hi!");
        model.User user20 = recruiterService0.authenticateUser(userList13, "hi!", "hi!");
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        recruiterService0.recruiterSignInPage();
    }

    @Test
    public void test121() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test121");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.deleteApplicantProfile();
        applicantService0.viewAllAvailableJobs();
        applicantService0.viewAllAvailableJobs();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.viewApplicantApplications();
    }

    @Test
    public void test122() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test122");
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        model.User[] userArray1 = new model.User[] {};
        java.util.ArrayList<model.User> userList2 = new java.util.ArrayList<model.User>();
        boolean boolean3 = java.util.Collections.addAll((java.util.Collection<model.User>) userList2, userArray1);
        model.User user6 = recruiterService0.authenticateUser(userList2, "", "");
        recruiterService0.submitNewJobPost("hi!", "hi!");
        service.RecruiterService recruiterService10 = service.RecruiterService.getInstance();
        recruiterService10.updateStatusOfJobPost("hi!");
        model.User[] userArray13 = new model.User[] {};
        java.util.ArrayList<model.User> userList14 = new java.util.ArrayList<model.User>();
        boolean boolean15 = java.util.Collections.addAll((java.util.Collection<model.User>) userList14, userArray13);
        model.User user18 = recruiterService10.authenticateUser(userList14, "", "hi!");
        service.RecruiterService recruiterService19 = service.RecruiterService.getInstance();
        recruiterService19.updateStatusOfJobPost("hi!");
        model.User[] userArray22 = new model.User[] {};
        java.util.ArrayList<model.User> userList23 = new java.util.ArrayList<model.User>();
        boolean boolean24 = java.util.Collections.addAll((java.util.Collection<model.User>) userList23, userArray22);
        model.User user27 = recruiterService19.authenticateUser(userList23, "", "hi!");
        model.User user30 = recruiterService10.authenticateUser(userList23, "hi!", "hi!");
        model.User user33 = recruiterService0.authenticateUser(userList23, "hi!", "hi!");
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        recruiterService0.viewSpecificApplication("");
    }

    @Test
    public void test123() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test123");
        service.ApplicantService applicantService0 = service.ApplicantService.getInstance();
        applicantService0.viewAllAvailableJobs();
        applicantService0.deleteApplicantProfile();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.viewFeedback("hi!");
    }

    @Test
    public void test124() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test124");
        service.ApplicantService applicantService0 = service.ApplicantService.getInstance();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.viewFeedback("hi!");
    }

    @Test
    public void test125() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test125");
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        model.User[] userArray1 = new model.User[] {};
        java.util.ArrayList<model.User> userList2 = new java.util.ArrayList<model.User>();
        boolean boolean3 = java.util.Collections.addAll((java.util.Collection<model.User>) userList2, userArray1);
        model.User user6 = recruiterService0.authenticateUser(userList2, "", "");
        model.User[] userArray7 = new model.User[] {};
        java.util.ArrayList<model.User> userList8 = new java.util.ArrayList<model.User>();
        boolean boolean9 = java.util.Collections.addAll((java.util.Collection<model.User>) userList8, userArray7);
        model.User user12 = recruiterService0.authenticateUser(userList8, "", "hi!");
        recruiterService0.updateStatusOfJobPost("hi!");
        recruiterService0.submitNewJobPost("", "hi!");
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        recruiterService0.resetPasswordRecruiter("hi!");
    }

    @Test
    public void test126() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test126");
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        recruiterService0.viewAssessmentResult("", "hi!");
    }

    @Test
    public void test127() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.ErrorTest0.test127");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.deleteApplicantProfile();
        applicantService0.viewJobPost();
        // during test generation this statement threw an exception of type java.lang.NullPointerException in error
        applicantService0.viewApplicantApplications();
    }
}


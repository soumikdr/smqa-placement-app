package test.blackbox.RandomTesting;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RegressionTest0 {

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
    public void test01() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test01");
        service.CommonService commonService0 = service.CommonService.getInstance();
        org.junit.Assert.assertNotNull(commonService0);
    }

    @Test
    public void test02() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test02");
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        model.Application application1 = null;
        // The following exception was thrown during execution in test generation
        try {
            recruiterService0.sendFeedback(application1);
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"model.Application.setFeedback(String)\" because \"application\" is null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(recruiterService0);
    }

    @Test
    public void test03() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test03");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.viewJobPost();
        applicantService0.viewAllAvailableJobs();
    }

    @Test
    public void test04() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test04");
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        model.User[] userArray1 = new model.User[] {};
        java.util.ArrayList<model.User> userList2 = new java.util.ArrayList<model.User>();
        boolean boolean3 = java.util.Collections.addAll((java.util.Collection<model.User>) userList2, userArray1);
        model.User user6 = recruiterService0.authenticateUser(userList2, "", "");
        model.Application application7 = null;
        // The following exception was thrown during execution in test generation
        try {
            recruiterService0.viewSubmittedAnswers(application7);
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"model.Application.getId()\" because \"application\" is null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(recruiterService0);
        org.junit.Assert.assertNotNull(userArray1);
        org.junit.Assert.assertArrayEquals(userArray1, new model.User[] {});
        org.junit.Assert.assertTrue("'" + boolean3 + "' != '" + false + "'", boolean3 == false);
        org.junit.Assert.assertNull(user6);
    }

    @Test
    public void test05() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test05");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        model.User user1 = null;
        // The following exception was thrown during execution in test generation
        try {
            applicantService0.updateProfile(user1);
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"java.util.ArrayList.removeIf(java.util.function.Predicate)\" because \"users\" is null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
    }

    @Test
    public void test06() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test06");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.viewJobPost();
        model.User user2 = null;
        // The following exception was thrown during execution in test generation
        try {
            applicantService0.updateProfile(user2);
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"java.util.ArrayList.removeIf(java.util.function.Predicate)\" because \"users\" is null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
    }

    @Test
    public void test07() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test07");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.viewJobPost();
        model.Job job2 = null;
        // The following exception was thrown during execution in test generation
        try {
            applicantService0.submitApplicationForm(job2, "hi!", (java.lang.Integer) (-1), "hi!");
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"model.Job.getId()\" because \"job\" is null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
    }

    @Test
    public void test08() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test08");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.deleteApplicantProfile();
        applicantService0.viewAllAvailableJobs();
        applicantService0.deleteApplicantProfile();
    }

    @Test
    public void test09() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test09");
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        model.User[] userArray1 = new model.User[] {};
        java.util.ArrayList<model.User> userList2 = new java.util.ArrayList<model.User>();
        boolean boolean3 = java.util.Collections.addAll((java.util.Collection<model.User>) userList2, userArray1);
        model.User user6 = recruiterService0.authenticateUser(userList2, "", "");
        recruiterService0.updateStatusOfJobPost("hi!");
        recruiterService0.submitNewJobPost("hi!", "hi!");
        model.Application application12 = null;
        // The following exception was thrown during execution in test generation
        try {
            recruiterService0.viewSubmittedAnswers(application12);
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"model.Application.getId()\" because \"application\" is null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(recruiterService0);
        org.junit.Assert.assertNotNull(userArray1);
        org.junit.Assert.assertArrayEquals(userArray1, new model.User[] {});
        org.junit.Assert.assertTrue("'" + boolean3 + "' != '" + false + "'", boolean3 == false);
        org.junit.Assert.assertNull(user6);
    }

    @Test
    public void test10() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test10");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.deleteApplicantProfile();
        model.User user2 = null;
        // The following exception was thrown during execution in test generation
        try {
            applicantService0.updateProfile(user2);
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"java.util.ArrayList.removeIf(java.util.function.Predicate)\" because \"users\" is null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
    }

    @Test
    public void test11() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test11");
        service.CommonService commonService0 = new service.CommonService();
    }

    @Test
    public void test12() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test12");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.deleteApplicantProfile();
        applicantService0.viewAllAvailableJobs();
        model.Job job3 = null;
        // The following exception was thrown during execution in test generation
        try {
            applicantService0.submitApplicationForm(job3, "", (java.lang.Integer) 100, "");
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"model.Job.getId()\" because \"job\" is null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
    }

    @Test
    public void test13() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test13");
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
        model.Application application21 = null;
        // The following exception was thrown during execution in test generation
        try {
            recruiterService0.sendFeedback(application21);
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"model.Application.setFeedback(String)\" because \"application\" is null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(recruiterService0);
        org.junit.Assert.assertNotNull(userArray3);
        org.junit.Assert.assertArrayEquals(userArray3, new model.User[] {});
        org.junit.Assert.assertTrue("'" + boolean5 + "' != '" + false + "'", boolean5 == false);
        org.junit.Assert.assertNull(user8);
        org.junit.Assert.assertNotNull(recruiterService9);
        org.junit.Assert.assertNotNull(userArray12);
        org.junit.Assert.assertArrayEquals(userArray12, new model.User[] {});
        org.junit.Assert.assertTrue("'" + boolean14 + "' != '" + false + "'", boolean14 == false);
        org.junit.Assert.assertNull(user17);
        org.junit.Assert.assertNull(user20);
    }

    @Test
    public void test14() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test14");
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        model.User[] userArray1 = new model.User[] {};
        java.util.ArrayList<model.User> userList2 = new java.util.ArrayList<model.User>();
        boolean boolean3 = java.util.Collections.addAll((java.util.Collection<model.User>) userList2, userArray1);
        model.User user6 = recruiterService0.authenticateUser(userList2, "", "");
        model.Application application7 = null;
        // The following exception was thrown during execution in test generation
        try {
            recruiterService0.sendFeedback(application7);
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"model.Application.setFeedback(String)\" because \"application\" is null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(recruiterService0);
        org.junit.Assert.assertNotNull(userArray1);
        org.junit.Assert.assertArrayEquals(userArray1, new model.User[] {});
        org.junit.Assert.assertTrue("'" + boolean3 + "' != '" + false + "'", boolean3 == false);
        org.junit.Assert.assertNull(user6);
    }

    @Test
    public void test15() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test15");
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
        model.Application application15 = null;
        // The following exception was thrown during execution in test generation
        try {
            recruiterService0.sendFeedback(application15);
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"model.Application.setFeedback(String)\" because \"application\" is null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(recruiterService0);
        org.junit.Assert.assertNotNull(userArray1);
        org.junit.Assert.assertArrayEquals(userArray1, new model.User[] {});
        org.junit.Assert.assertTrue("'" + boolean3 + "' != '" + false + "'", boolean3 == false);
        org.junit.Assert.assertNull(user6);
        org.junit.Assert.assertNotNull(userArray7);
        org.junit.Assert.assertArrayEquals(userArray7, new model.User[] {});
        org.junit.Assert.assertTrue("'" + boolean9 + "' != '" + false + "'", boolean9 == false);
        org.junit.Assert.assertNull(user12);
    }

    @Test
    public void test16() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test16");
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
        model.Application application15 = null;
        // The following exception was thrown during execution in test generation
        try {
            recruiterService0.viewSubmittedAnswers(application15);
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"model.Application.getId()\" because \"application\" is null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(recruiterService0);
        org.junit.Assert.assertNotNull(userArray1);
        org.junit.Assert.assertArrayEquals(userArray1, new model.User[] {});
        org.junit.Assert.assertTrue("'" + boolean3 + "' != '" + false + "'", boolean3 == false);
        org.junit.Assert.assertNull(user6);
        org.junit.Assert.assertNotNull(userArray7);
        org.junit.Assert.assertArrayEquals(userArray7, new model.User[] {});
        org.junit.Assert.assertTrue("'" + boolean9 + "' != '" + false + "'", boolean9 == false);
        org.junit.Assert.assertNull(user12);
    }

    @Test
    public void test17() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test17");
        service.ApplicantService applicantService0 = service.ApplicantService.getInstance();
        applicantService0.viewAllAvailableJobs();
        applicantService0.deleteApplicantProfile();
        java.lang.Class<?> wildcardClass3 = applicantService0.getClass();
        org.junit.Assert.assertNotNull(applicantService0);
        org.junit.Assert.assertNotNull(wildcardClass3);
    }

    @Test
    public void test18() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test18");
        service.RecruiterService recruiterService0 = new service.RecruiterService();
        model.User[] userArray1 = new model.User[] {};
        java.util.ArrayList<model.User> userList2 = new java.util.ArrayList<model.User>();
        boolean boolean3 = java.util.Collections.addAll((java.util.Collection<model.User>) userList2, userArray1);
        model.User user6 = recruiterService0.authenticateUser(userList2, "", "");
        // The following exception was thrown during execution in test generation
        try {
            java.lang.Class<?> wildcardClass7 = user6.getClass();
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(userArray1);
        org.junit.Assert.assertArrayEquals(userArray1, new model.User[] {});
        org.junit.Assert.assertTrue("'" + boolean3 + "' != '" + false + "'", boolean3 == false);
        org.junit.Assert.assertNull(user6);
    }

    @Test
    public void test19() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test19");
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        model.User[] userArray1 = new model.User[] {};
        java.util.ArrayList<model.User> userList2 = new java.util.ArrayList<model.User>();
        boolean boolean3 = java.util.Collections.addAll((java.util.Collection<model.User>) userList2, userArray1);
        model.User user6 = recruiterService0.authenticateUser(userList2, "", "");
        recruiterService0.submitNewJobPost("hi!", "hi!");
        model.Application application10 = null;
        // The following exception was thrown during execution in test generation
        try {
            recruiterService0.viewSubmittedAnswers(application10);
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"model.Application.getId()\" because \"application\" is null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(recruiterService0);
        org.junit.Assert.assertNotNull(userArray1);
        org.junit.Assert.assertArrayEquals(userArray1, new model.User[] {});
        org.junit.Assert.assertTrue("'" + boolean3 + "' != '" + false + "'", boolean3 == false);
        org.junit.Assert.assertNull(user6);
    }

    @Test
    public void test20() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test20");
        service.RecruiterService recruiterService0 = new service.RecruiterService();
        recruiterService0.submitNewJobPost("", "");
        model.Application application4 = null;
        // The following exception was thrown during execution in test generation
        try {
            recruiterService0.sendFeedback(application4);
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"model.Application.setFeedback(String)\" because \"application\" is null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
    }

    @Test
    public void test21() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test21");
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
        org.junit.Assert.assertNotNull(recruiterService0);
        org.junit.Assert.assertNotNull(userArray1);
        org.junit.Assert.assertArrayEquals(userArray1, new model.User[] {});
        org.junit.Assert.assertTrue("'" + boolean3 + "' != '" + false + "'", boolean3 == false);
        org.junit.Assert.assertNull(user6);
        org.junit.Assert.assertNotNull(userArray7);
        org.junit.Assert.assertArrayEquals(userArray7, new model.User[] {});
        org.junit.Assert.assertTrue("'" + boolean9 + "' != '" + false + "'", boolean9 == false);
        org.junit.Assert.assertNull(user12);
    }

    @Test
    public void test22() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test22");
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        model.User[] userArray1 = new model.User[] {};
        java.util.ArrayList<model.User> userList2 = new java.util.ArrayList<model.User>();
        boolean boolean3 = java.util.Collections.addAll((java.util.Collection<model.User>) userList2, userArray1);
        model.User user6 = recruiterService0.authenticateUser(userList2, "", "");
        recruiterService0.submitNewJobPost("hi!", "hi!");
        model.Application application10 = null;
        // The following exception was thrown during execution in test generation
        try {
            recruiterService0.sendFeedback(application10);
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"model.Application.setFeedback(String)\" because \"application\" is null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(recruiterService0);
        org.junit.Assert.assertNotNull(userArray1);
        org.junit.Assert.assertArrayEquals(userArray1, new model.User[] {});
        org.junit.Assert.assertTrue("'" + boolean3 + "' != '" + false + "'", boolean3 == false);
        org.junit.Assert.assertNull(user6);
    }

    @Test
    public void test23() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test23");
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        recruiterService0.updateStatusOfJobPost("hi!");
        model.Application application3 = null;
        // The following exception was thrown during execution in test generation
        try {
            recruiterService0.viewSubmittedAnswers(application3);
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"model.Application.getId()\" because \"application\" is null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(recruiterService0);
    }

    @Test
    public void test24() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test24");
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        model.User[] userArray1 = new model.User[] {};
        java.util.ArrayList<model.User> userList2 = new java.util.ArrayList<model.User>();
        boolean boolean3 = java.util.Collections.addAll((java.util.Collection<model.User>) userList2, userArray1);
        model.User user6 = recruiterService0.authenticateUser(userList2, "", "");
        model.User[] userArray7 = new model.User[] {};
        java.util.ArrayList<model.User> userList8 = new java.util.ArrayList<model.User>();
        boolean boolean9 = java.util.Collections.addAll((java.util.Collection<model.User>) userList8, userArray7);
        model.User user12 = recruiterService0.authenticateUser(userList8, "", "hi!");
        java.lang.Class<?> wildcardClass13 = recruiterService0.getClass();
        org.junit.Assert.assertNotNull(recruiterService0);
        org.junit.Assert.assertNotNull(userArray1);
        org.junit.Assert.assertArrayEquals(userArray1, new model.User[] {});
        org.junit.Assert.assertTrue("'" + boolean3 + "' != '" + false + "'", boolean3 == false);
        org.junit.Assert.assertNull(user6);
        org.junit.Assert.assertNotNull(userArray7);
        org.junit.Assert.assertArrayEquals(userArray7, new model.User[] {});
        org.junit.Assert.assertTrue("'" + boolean9 + "' != '" + false + "'", boolean9 == false);
        org.junit.Assert.assertNull(user12);
        org.junit.Assert.assertNotNull(wildcardClass13);
    }

    @Test
    public void test25() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test25");
        service.RecruiterService recruiterService0 = new service.RecruiterService();
        recruiterService0.updateStatusOfJobPost("");
        recruiterService0.submitNewJobPost("hi!", "hi!");
    }

    @Test
    public void test26() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test26");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.deleteApplicantProfile();
        applicantService0.viewAllAvailableJobs();
        applicantService0.viewJobPost();
        applicantService0.viewAllAvailableJobs();
        model.User user5 = null;
        // The following exception was thrown during execution in test generation
        try {
            applicantService0.updateProfile(user5);
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"java.util.ArrayList.removeIf(java.util.function.Predicate)\" because \"users\" is null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
    }

    @Test
    public void test27() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test27");
        service.RecruiterService recruiterService0 = new service.RecruiterService();
        model.User[] userArray1 = new model.User[] {};
        java.util.ArrayList<model.User> userList2 = new java.util.ArrayList<model.User>();
        boolean boolean3 = java.util.Collections.addAll((java.util.Collection<model.User>) userList2, userArray1);
        model.User user6 = recruiterService0.authenticateUser(userList2, "", "");
        recruiterService0.submitNewJobPost("", "hi!");
        model.Application application10 = null;
        // The following exception was thrown during execution in test generation
        try {
            recruiterService0.sendFeedback(application10);
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"model.Application.setFeedback(String)\" because \"application\" is null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(userArray1);
        org.junit.Assert.assertArrayEquals(userArray1, new model.User[] {});
        org.junit.Assert.assertTrue("'" + boolean3 + "' != '" + false + "'", boolean3 == false);
        org.junit.Assert.assertNull(user6);
    }

    @Test
    public void test28() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test28");
        service.ApplicantService applicantService0 = service.ApplicantService.getInstance();
        applicantService0.viewAllAvailableJobs();
        applicantService0.viewJobPost();
        org.junit.Assert.assertNotNull(applicantService0);
    }

    @Test
    public void test29() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test29");
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        model.User[] userArray1 = new model.User[] {};
        java.util.ArrayList<model.User> userList2 = new java.util.ArrayList<model.User>();
        boolean boolean3 = java.util.Collections.addAll((java.util.Collection<model.User>) userList2, userArray1);
        model.User user6 = recruiterService0.authenticateUser(userList2, "", "");
        recruiterService0.updateStatusOfJobPost("hi!");
        recruiterService0.submitNewJobPost("hi!", "hi!");
        recruiterService0.updateStatusOfJobPost("");
        model.Application application14 = null;
        // The following exception was thrown during execution in test generation
        try {
            recruiterService0.sendFeedback(application14);
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"model.Application.setFeedback(String)\" because \"application\" is null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(recruiterService0);
        org.junit.Assert.assertNotNull(userArray1);
        org.junit.Assert.assertArrayEquals(userArray1, new model.User[] {});
        org.junit.Assert.assertTrue("'" + boolean3 + "' != '" + false + "'", boolean3 == false);
        org.junit.Assert.assertNull(user6);
    }

    @Test
    public void test30() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test30");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.deleteApplicantProfile();
        applicantService0.viewAllAvailableJobs();
        applicantService0.viewAllAvailableJobs();
    }

    @Test
    public void test31() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test31");
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
        org.junit.Assert.assertNotNull(recruiterService0);
        org.junit.Assert.assertNotNull(userArray1);
        org.junit.Assert.assertArrayEquals(userArray1, new model.User[] {});
        org.junit.Assert.assertTrue("'" + boolean3 + "' != '" + false + "'", boolean3 == false);
        org.junit.Assert.assertNull(user6);
        org.junit.Assert.assertNotNull(recruiterService10);
        org.junit.Assert.assertNotNull(userArray13);
        org.junit.Assert.assertArrayEquals(userArray13, new model.User[] {});
        org.junit.Assert.assertTrue("'" + boolean15 + "' != '" + false + "'", boolean15 == false);
        org.junit.Assert.assertNull(user18);
        org.junit.Assert.assertNotNull(recruiterService19);
        org.junit.Assert.assertNotNull(userArray22);
        org.junit.Assert.assertArrayEquals(userArray22, new model.User[] {});
        org.junit.Assert.assertTrue("'" + boolean24 + "' != '" + false + "'", boolean24 == false);
        org.junit.Assert.assertNull(user27);
        org.junit.Assert.assertNull(user30);
        org.junit.Assert.assertNull(user33);
    }

    @Test
    public void test32() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test32");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.deleteApplicantProfile();
        applicantService0.viewJobPost();
    }

    @Test
    public void test33() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test33");
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        recruiterService0.submitNewJobPost("hi!", "hi!");
        model.Application application4 = null;
        // The following exception was thrown during execution in test generation
        try {
            recruiterService0.viewSubmittedAnswers(application4);
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"model.Application.getId()\" because \"application\" is null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(recruiterService0);
    }

    @Test
    public void test34() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test34");
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
        org.junit.Assert.assertNotNull(recruiterService0);
        org.junit.Assert.assertNotNull(userArray1);
        org.junit.Assert.assertArrayEquals(userArray1, new model.User[] {});
        org.junit.Assert.assertTrue("'" + boolean3 + "' != '" + false + "'", boolean3 == false);
        org.junit.Assert.assertNull(user6);
        org.junit.Assert.assertNotNull(userArray7);
        org.junit.Assert.assertArrayEquals(userArray7, new model.User[] {});
        org.junit.Assert.assertTrue("'" + boolean9 + "' != '" + false + "'", boolean9 == false);
        org.junit.Assert.assertNull(user12);
    }

    @Test
    public void test35() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test35");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.deleteApplicantProfile();
        applicantService0.viewAllAvailableJobs();
        model.User user3 = null;
        // The following exception was thrown during execution in test generation
        try {
            applicantService0.updateProfile(user3);
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"java.util.ArrayList.removeIf(java.util.function.Predicate)\" because \"users\" is null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
    }

    @Test
    public void test36() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test36");
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        model.User[] userArray1 = new model.User[] {};
        java.util.ArrayList<model.User> userList2 = new java.util.ArrayList<model.User>();
        boolean boolean3 = java.util.Collections.addAll((java.util.Collection<model.User>) userList2, userArray1);
        model.User user6 = recruiterService0.authenticateUser(userList2, "", "");
        recruiterService0.submitNewJobPost("hi!", "hi!");
        recruiterService0.submitNewJobPost("hi!", "hi!");
        java.lang.Class<?> wildcardClass13 = recruiterService0.getClass();
        org.junit.Assert.assertNotNull(recruiterService0);
        org.junit.Assert.assertNotNull(userArray1);
        org.junit.Assert.assertArrayEquals(userArray1, new model.User[] {});
        org.junit.Assert.assertTrue("'" + boolean3 + "' != '" + false + "'", boolean3 == false);
        org.junit.Assert.assertNull(user6);
        org.junit.Assert.assertNotNull(wildcardClass13);
    }

    @Test
    public void test37() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test37");
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        recruiterService0.submitNewJobPost("hi!", "");
        recruiterService0.submitNewJobPost("hi!", "hi!");
        org.junit.Assert.assertNotNull(recruiterService0);
    }
}


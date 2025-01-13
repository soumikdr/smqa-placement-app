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
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        recruiterService0.viewInterviewResult();
        recruiterService0.sendAssessment();
        recruiterService0.updateDescriptionOfJobPost("");
        org.junit.Assert.assertNotNull(recruiterService0);
    }

    @Test
    public void test02() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test02");
        service.ApplicantService applicantService0 = service.ApplicantService.getInstance();
        model.User user1 = null;
        // The following exception was thrown during execution in test generation
        try {
            applicantService0.updateProfile(user1);
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"java.util.ArrayList.removeIf(java.util.function.Predicate)\" because \"users\" is null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(applicantService0);
    }

    @Test
    public void test03() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test03");
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
    public void test04() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test04");
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        recruiterService0.viewInterviewResult();
        recruiterService0.sendAssessment();
        recruiterService0.updateDescriptionOfJobPost("hi!");
        recruiterService0.submitNewJobPost("hi!", "hi!");
        org.junit.Assert.assertNotNull(recruiterService0);
    }

    @Test
    public void test05() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test05");
        service.ApplicantService applicantService0 = service.ApplicantService.getInstance();
        applicantService0.viewJobPost();
        model.Job job2 = null;
        // The following exception was thrown during execution in test generation
        try {
            applicantService0.submitApplicationForm(job2, "hi!", (java.lang.Integer) 10, "hi!");
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"model.Job.getId()\" because \"job\" is null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(applicantService0);
    }

    @Test
    public void test06() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test06");
        service.ApplicantService applicantService0 = service.ApplicantService.getInstance();
        applicantService0.viewJobPost();
        model.Job job2 = null;
        // The following exception was thrown during execution in test generation
        try {
            applicantService0.submitApplicationForm(job2, "", (java.lang.Integer) 100, "hi!");
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"model.Job.getId()\" because \"job\" is null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(applicantService0);
    }

    @Test
    public void test07() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test07");
        service.ApplicantService applicantService0 = service.ApplicantService.getInstance();
        applicantService0.viewJobPost();
        model.Job job2 = null;
        // The following exception was thrown during execution in test generation
        try {
            applicantService0.submitApplicationForm(job2, "", (java.lang.Integer) 10, "hi!");
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"model.Job.getId()\" because \"job\" is null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(applicantService0);
    }

    @Test
    public void test08() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test08");
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        recruiterService0.viewInterviewResult();
        recruiterService0.sendAssessment();
        recruiterService0.sendAssessment();
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
    public void test09() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test09");
        service.ApplicantService applicantService0 = service.ApplicantService.getInstance();
        applicantService0.viewAllAvailableJobs();
        model.User user2 = null;
        // The following exception was thrown during execution in test generation
        try {
            applicantService0.updateProfile(user2);
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"java.util.ArrayList.removeIf(java.util.function.Predicate)\" because \"users\" is null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(applicantService0);
    }

    @Test
    public void test10() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test10");
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        recruiterService0.viewInterviewResult();
        recruiterService0.sendAssessment();
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
    public void test11() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test11");
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        recruiterService0.updateStatusOfJobPost("hi!");
        recruiterService0.sendAssessment();
        model.User[] userArray4 = new model.User[] {};
        java.util.ArrayList<model.User> userList5 = new java.util.ArrayList<model.User>();
        boolean boolean6 = java.util.Collections.addAll((java.util.Collection<model.User>) userList5, userArray4);
        model.User user9 = recruiterService0.authenticateUser(userList5, "", "");
        recruiterService0.viewInterviewResult();
        java.lang.Class<?> wildcardClass11 = recruiterService0.getClass();
        org.junit.Assert.assertNotNull(recruiterService0);
        org.junit.Assert.assertNotNull(userArray4);
        org.junit.Assert.assertArrayEquals(userArray4, new model.User[] {});
        org.junit.Assert.assertTrue("'" + boolean6 + "' != '" + false + "'", boolean6 == false);
        org.junit.Assert.assertNull(user9);
        org.junit.Assert.assertNotNull(wildcardClass11);
    }

    @Test
    public void test12() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test12");
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        recruiterService0.sendAssessment();
        java.lang.Class<?> wildcardClass2 = recruiterService0.getClass();
        org.junit.Assert.assertNotNull(recruiterService0);
        org.junit.Assert.assertNotNull(wildcardClass2);
    }

    @Test
    public void test13() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test13");
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        recruiterService0.sendAssessment();
        model.Application application2 = null;
        // The following exception was thrown during execution in test generation
        try {
            recruiterService0.viewSubmittedAnswers(application2);
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"model.Application.getId()\" because \"application\" is null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(recruiterService0);
    }

    @Test
    public void test14() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test14");
        service.ApplicantService applicantService0 = service.ApplicantService.getInstance();
        applicantService0.viewJobPost();
        applicantService0.viewJobPost();
        org.junit.Assert.assertNotNull(applicantService0);
    }

    @Test
    public void test15() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test15");
        service.CommonService commonService0 = service.CommonService.getInstance();
        service.RecruiterService recruiterService1 = service.RecruiterService.getInstance();
        recruiterService1.viewInterviewResult();
        recruiterService1.sendAssessment();
        model.User[] userArray4 = new model.User[] {};
        java.util.ArrayList<model.User> userList5 = new java.util.ArrayList<model.User>();
        boolean boolean6 = java.util.Collections.addAll((java.util.Collection<model.User>) userList5, userArray4);
        model.User user9 = recruiterService1.authenticateUser(userList5, "hi!", "");
        model.User user12 = commonService0.authenticateUser(userList5, "", "hi!");
        // The following exception was thrown during execution in test generation
        try {
            java.lang.Class<?> wildcardClass13 = user12.getClass();
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(commonService0);
        org.junit.Assert.assertNotNull(recruiterService1);
        org.junit.Assert.assertNotNull(userArray4);
        org.junit.Assert.assertArrayEquals(userArray4, new model.User[] {});
        org.junit.Assert.assertTrue("'" + boolean6 + "' != '" + false + "'", boolean6 == false);
        org.junit.Assert.assertNull(user9);
        org.junit.Assert.assertNull(user12);
    }

    @Test
    public void test16() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test16");
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        recruiterService0.viewInterviewResult();
        recruiterService0.sendAssessment();
        model.Application application3 = null;
        // The following exception was thrown during execution in test generation
        try {
            recruiterService0.sendFeedback(application3);
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"model.Application.setFeedback(String)\" because \"application\" is null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(recruiterService0);
    }

    @Test
    public void test17() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test17");
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        recruiterService0.updateStatusOfJobPost("hi!");
        recruiterService0.sendAssessment();
        model.User[] userArray4 = new model.User[] {};
        java.util.ArrayList<model.User> userList5 = new java.util.ArrayList<model.User>();
        boolean boolean6 = java.util.Collections.addAll((java.util.Collection<model.User>) userList5, userArray4);
        model.User user9 = recruiterService0.authenticateUser(userList5, "", "");
        model.Application application10 = null;
        // The following exception was thrown during execution in test generation
        try {
            recruiterService0.sendFeedback(application10);
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"model.Application.setFeedback(String)\" because \"application\" is null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(recruiterService0);
        org.junit.Assert.assertNotNull(userArray4);
        org.junit.Assert.assertArrayEquals(userArray4, new model.User[] {});
        org.junit.Assert.assertTrue("'" + boolean6 + "' != '" + false + "'", boolean6 == false);
        org.junit.Assert.assertNull(user9);
    }

    @Test
    public void test18() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test18");
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        recruiterService0.viewInterviewResult();
        recruiterService0.sendAssessment();
        model.User[] userArray3 = new model.User[] {};
        java.util.ArrayList<model.User> userList4 = new java.util.ArrayList<model.User>();
        boolean boolean5 = java.util.Collections.addAll((java.util.Collection<model.User>) userList4, userArray3);
        model.User user8 = recruiterService0.authenticateUser(userList4, "hi!", "");
        recruiterService0.viewInterviewResult();
        java.lang.Class<?> wildcardClass10 = recruiterService0.getClass();
        org.junit.Assert.assertNotNull(recruiterService0);
        org.junit.Assert.assertNotNull(userArray3);
        org.junit.Assert.assertArrayEquals(userArray3, new model.User[] {});
        org.junit.Assert.assertTrue("'" + boolean5 + "' != '" + false + "'", boolean5 == false);
        org.junit.Assert.assertNull(user8);
        org.junit.Assert.assertNotNull(wildcardClass10);
    }

    @Test
    public void test19() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test19");
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        recruiterService0.updateStatusOfJobPost("hi!");
        recruiterService0.sendAssessment();
        model.Application application4 = null;
        // The following exception was thrown during execution in test generation
        try {
            recruiterService0.sendFeedback(application4);
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"model.Application.setFeedback(String)\" because \"application\" is null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(recruiterService0);
    }

    @Test
    public void test20() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test20");
        service.RecruiterService recruiterService0 = new service.RecruiterService();
        recruiterService0.viewInterviewResult();
        recruiterService0.viewInterviewResult();
    }

    @Test
    public void test21() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test21");
        service.ApplicantService applicantService0 = service.ApplicantService.getInstance();
        applicantService0.viewJobPost();
        model.Job job2 = null;
        // The following exception was thrown during execution in test generation
        try {
            applicantService0.submitApplicationForm(job2, "hi!", (java.lang.Integer) 10, "");
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"model.Job.getId()\" because \"job\" is null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(applicantService0);
    }

    @Test
    public void test22() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test22");
        service.ApplicantService applicantService0 = service.ApplicantService.getInstance();
        applicantService0.viewJobPost();
        model.User user2 = null;
        // The following exception was thrown during execution in test generation
        try {
            applicantService0.updateProfile(user2);
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"java.util.ArrayList.removeIf(java.util.function.Predicate)\" because \"users\" is null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(applicantService0);
    }

    @Test
    public void test23() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test23");
        service.ApplicantService applicantService0 = service.ApplicantService.getInstance();
        model.Job job1 = null;
        // The following exception was thrown during execution in test generation
        try {
            applicantService0.submitApplicationForm(job1, "", (java.lang.Integer) 100, "");
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"model.Job.getId()\" because \"job\" is null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(applicantService0);
    }

    @Test
    public void test24() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test24");
        service.RecruiterService recruiterService0 = new service.RecruiterService();
        recruiterService0.viewInterviewResult();
        service.RecruiterService recruiterService2 = service.RecruiterService.getInstance();
        recruiterService2.updateStatusOfJobPost("hi!");
        recruiterService2.sendAssessment();
        model.User[] userArray6 = new model.User[] {};
        java.util.ArrayList<model.User> userList7 = new java.util.ArrayList<model.User>();
        boolean boolean8 = java.util.Collections.addAll((java.util.Collection<model.User>) userList7, userArray6);
        model.User user11 = recruiterService2.authenticateUser(userList7, "", "");
        model.User user14 = recruiterService0.authenticateUser(userList7, "hi!", "hi!");
        model.Application application15 = null;
        // The following exception was thrown during execution in test generation
        try {
            recruiterService0.sendFeedback(application15);
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"model.Application.setFeedback(String)\" because \"application\" is null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(recruiterService2);
        org.junit.Assert.assertNotNull(userArray6);
        org.junit.Assert.assertArrayEquals(userArray6, new model.User[] {});
        org.junit.Assert.assertTrue("'" + boolean8 + "' != '" + false + "'", boolean8 == false);
        org.junit.Assert.assertNull(user11);
        org.junit.Assert.assertNull(user14);
    }

    @Test
    public void test25() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test25");
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        recruiterService0.viewInterviewResult();
        recruiterService0.sendAssessment();
        recruiterService0.sendAssessment();
        recruiterService0.viewInterviewResult();
        recruiterService0.sendAssessment();
        org.junit.Assert.assertNotNull(recruiterService0);
    }

    @Test
    public void test26() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test26");
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
    public void test27() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test27");
        service.ApplicantService applicantService0 = service.ApplicantService.getInstance();
        applicantService0.deleteApplicantProfile();
        applicantService0.viewAllAvailableJobs();
        model.Job job3 = null;
        // The following exception was thrown during execution in test generation
        try {
            applicantService0.submitApplicationForm(job3, "hi!", (java.lang.Integer) (-1), "hi!");
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"model.Job.getId()\" because \"job\" is null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(applicantService0);
    }

    @Test
    public void test28() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test28");
        service.CommonService commonService0 = service.CommonService.getInstance();
        service.CommonService commonService1 = service.CommonService.getInstance();
        service.RecruiterService recruiterService2 = service.RecruiterService.getInstance();
        recruiterService2.viewInterviewResult();
        recruiterService2.sendAssessment();
        model.User[] userArray5 = new model.User[] {};
        java.util.ArrayList<model.User> userList6 = new java.util.ArrayList<model.User>();
        boolean boolean7 = java.util.Collections.addAll((java.util.Collection<model.User>) userList6, userArray5);
        model.User user10 = recruiterService2.authenticateUser(userList6, "hi!", "");
        model.User user13 = commonService1.authenticateUser(userList6, "", "hi!");
        model.User user16 = commonService0.authenticateUser(userList6, "hi!", "hi!");
        java.lang.Class<?> wildcardClass17 = commonService0.getClass();
        org.junit.Assert.assertNotNull(commonService0);
        org.junit.Assert.assertNotNull(commonService1);
        org.junit.Assert.assertNotNull(recruiterService2);
        org.junit.Assert.assertNotNull(userArray5);
        org.junit.Assert.assertArrayEquals(userArray5, new model.User[] {});
        org.junit.Assert.assertTrue("'" + boolean7 + "' != '" + false + "'", boolean7 == false);
        org.junit.Assert.assertNull(user10);
        org.junit.Assert.assertNull(user13);
        org.junit.Assert.assertNull(user16);
        org.junit.Assert.assertNotNull(wildcardClass17);
    }

    @Test
    public void test29() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test29");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        model.Job job1 = null;
        // The following exception was thrown during execution in test generation
        try {
            applicantService0.submitApplicationForm(job1, "", (java.lang.Integer) (-1), "");
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"model.Job.getId()\" because \"job\" is null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
    }

    @Test
    public void test30() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test30");
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        recruiterService0.viewInterviewResult();
        recruiterService0.viewInterviewResult();
        model.Application application3 = null;
        // The following exception was thrown during execution in test generation
        try {
            recruiterService0.sendFeedback(application3);
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"model.Application.setFeedback(String)\" because \"application\" is null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(recruiterService0);
    }

    @Test
    public void test31() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test31");
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        recruiterService0.updateStatusOfJobPost("hi!");
        recruiterService0.sendAssessment();
        model.User[] userArray4 = new model.User[] {};
        java.util.ArrayList<model.User> userList5 = new java.util.ArrayList<model.User>();
        boolean boolean6 = java.util.Collections.addAll((java.util.Collection<model.User>) userList5, userArray4);
        model.User user9 = recruiterService0.authenticateUser(userList5, "", "");
        recruiterService0.submitNewJobPost("", "");
        model.Application application13 = null;
        // The following exception was thrown during execution in test generation
        try {
            recruiterService0.sendFeedback(application13);
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"model.Application.setFeedback(String)\" because \"application\" is null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(recruiterService0);
        org.junit.Assert.assertNotNull(userArray4);
        org.junit.Assert.assertArrayEquals(userArray4, new model.User[] {});
        org.junit.Assert.assertTrue("'" + boolean6 + "' != '" + false + "'", boolean6 == false);
        org.junit.Assert.assertNull(user9);
    }

    @Test
    public void test32() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test32");
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        recruiterService0.viewInterviewResult();
        recruiterService0.sendAssessment();
        model.User[] userArray3 = new model.User[] {};
        java.util.ArrayList<model.User> userList4 = new java.util.ArrayList<model.User>();
        boolean boolean5 = java.util.Collections.addAll((java.util.Collection<model.User>) userList4, userArray3);
        model.User user8 = recruiterService0.authenticateUser(userList4, "hi!", "");
        recruiterService0.viewInterviewResult();
        model.Application application10 = null;
        // The following exception was thrown during execution in test generation
        try {
            recruiterService0.sendFeedback(application10);
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"model.Application.setFeedback(String)\" because \"application\" is null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(recruiterService0);
        org.junit.Assert.assertNotNull(userArray3);
        org.junit.Assert.assertArrayEquals(userArray3, new model.User[] {});
        org.junit.Assert.assertTrue("'" + boolean5 + "' != '" + false + "'", boolean5 == false);
        org.junit.Assert.assertNull(user8);
    }

    @Test
    public void test33() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test33");
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        recruiterService0.viewInterviewResult();
        recruiterService0.sendAssessment();
        model.User[] userArray3 = new model.User[] {};
        java.util.ArrayList<model.User> userList4 = new java.util.ArrayList<model.User>();
        boolean boolean5 = java.util.Collections.addAll((java.util.Collection<model.User>) userList4, userArray3);
        model.User user8 = recruiterService0.authenticateUser(userList4, "hi!", "");
        recruiterService0.viewInterviewResult();
        service.CommonService commonService10 = service.CommonService.getInstance();
        service.CommonService commonService11 = service.CommonService.getInstance();
        service.RecruiterService recruiterService12 = service.RecruiterService.getInstance();
        recruiterService12.viewInterviewResult();
        recruiterService12.sendAssessment();
        model.User[] userArray15 = new model.User[] {};
        java.util.ArrayList<model.User> userList16 = new java.util.ArrayList<model.User>();
        boolean boolean17 = java.util.Collections.addAll((java.util.Collection<model.User>) userList16, userArray15);
        model.User user20 = recruiterService12.authenticateUser(userList16, "hi!", "");
        model.User user23 = commonService11.authenticateUser(userList16, "", "hi!");
        model.User user26 = commonService10.authenticateUser(userList16, "hi!", "hi!");
        model.User user29 = recruiterService0.authenticateUser(userList16, "", "");
        org.junit.Assert.assertNotNull(recruiterService0);
        org.junit.Assert.assertNotNull(userArray3);
        org.junit.Assert.assertArrayEquals(userArray3, new model.User[] {});
        org.junit.Assert.assertTrue("'" + boolean5 + "' != '" + false + "'", boolean5 == false);
        org.junit.Assert.assertNull(user8);
        org.junit.Assert.assertNotNull(commonService10);
        org.junit.Assert.assertNotNull(commonService11);
        org.junit.Assert.assertNotNull(recruiterService12);
        org.junit.Assert.assertNotNull(userArray15);
        org.junit.Assert.assertArrayEquals(userArray15, new model.User[] {});
        org.junit.Assert.assertTrue("'" + boolean17 + "' != '" + false + "'", boolean17 == false);
        org.junit.Assert.assertNull(user20);
        org.junit.Assert.assertNull(user23);
        org.junit.Assert.assertNull(user26);
        org.junit.Assert.assertNull(user29);
    }

    @Test
    public void test34() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test34");
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        recruiterService0.viewInterviewResult();
        service.RecruiterService recruiterService2 = service.RecruiterService.getInstance();
        recruiterService2.updateStatusOfJobPost("hi!");
        recruiterService2.sendAssessment();
        model.User[] userArray6 = new model.User[] {};
        java.util.ArrayList<model.User> userList7 = new java.util.ArrayList<model.User>();
        boolean boolean8 = java.util.Collections.addAll((java.util.Collection<model.User>) userList7, userArray6);
        model.User user11 = recruiterService2.authenticateUser(userList7, "", "");
        model.User user14 = recruiterService0.authenticateUser(userList7, "", "hi!");
        recruiterService0.viewInterviewResult();
        org.junit.Assert.assertNotNull(recruiterService0);
        org.junit.Assert.assertNotNull(recruiterService2);
        org.junit.Assert.assertNotNull(userArray6);
        org.junit.Assert.assertArrayEquals(userArray6, new model.User[] {});
        org.junit.Assert.assertTrue("'" + boolean8 + "' != '" + false + "'", boolean8 == false);
        org.junit.Assert.assertNull(user11);
        org.junit.Assert.assertNull(user14);
    }

    @Test
    public void test35() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test35");
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        recruiterService0.updateStatusOfJobPost("hi!");
        recruiterService0.sendAssessment();
        model.User[] userArray4 = new model.User[] {};
        java.util.ArrayList<model.User> userList5 = new java.util.ArrayList<model.User>();
        boolean boolean6 = java.util.Collections.addAll((java.util.Collection<model.User>) userList5, userArray4);
        model.User user9 = recruiterService0.authenticateUser(userList5, "", "");
        recruiterService0.submitNewJobPost("", "");
        recruiterService0.sendAssessment();
        org.junit.Assert.assertNotNull(recruiterService0);
        org.junit.Assert.assertNotNull(userArray4);
        org.junit.Assert.assertArrayEquals(userArray4, new model.User[] {});
        org.junit.Assert.assertTrue("'" + boolean6 + "' != '" + false + "'", boolean6 == false);
        org.junit.Assert.assertNull(user9);
    }

    @Test
    public void test36() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test36");
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        recruiterService0.updateStatusOfJobPost("hi!");
        recruiterService0.sendAssessment();
        model.User[] userArray4 = new model.User[] {};
        java.util.ArrayList<model.User> userList5 = new java.util.ArrayList<model.User>();
        boolean boolean6 = java.util.Collections.addAll((java.util.Collection<model.User>) userList5, userArray4);
        model.User user9 = recruiterService0.authenticateUser(userList5, "", "");
        recruiterService0.viewInterviewResult();
        model.Application application11 = null;
        // The following exception was thrown during execution in test generation
        try {
            recruiterService0.sendFeedback(application11);
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"model.Application.setFeedback(String)\" because \"application\" is null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(recruiterService0);
        org.junit.Assert.assertNotNull(userArray4);
        org.junit.Assert.assertArrayEquals(userArray4, new model.User[] {});
        org.junit.Assert.assertTrue("'" + boolean6 + "' != '" + false + "'", boolean6 == false);
        org.junit.Assert.assertNull(user9);
    }

    @Test
    public void test37() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test37");
        service.CommonService commonService0 = new service.CommonService();
        service.CommonService commonService1 = service.CommonService.getInstance();
        service.RecruiterService recruiterService2 = service.RecruiterService.getInstance();
        recruiterService2.viewInterviewResult();
        recruiterService2.sendAssessment();
        model.User[] userArray5 = new model.User[] {};
        java.util.ArrayList<model.User> userList6 = new java.util.ArrayList<model.User>();
        boolean boolean7 = java.util.Collections.addAll((java.util.Collection<model.User>) userList6, userArray5);
        model.User user10 = recruiterService2.authenticateUser(userList6, "hi!", "");
        model.User user13 = commonService1.authenticateUser(userList6, "", "hi!");
        service.RecruiterService recruiterService14 = service.RecruiterService.getInstance();
        recruiterService14.updateStatusOfJobPost("hi!");
        recruiterService14.sendAssessment();
        model.User[] userArray18 = new model.User[] {};
        java.util.ArrayList<model.User> userList19 = new java.util.ArrayList<model.User>();
        boolean boolean20 = java.util.Collections.addAll((java.util.Collection<model.User>) userList19, userArray18);
        model.User user23 = recruiterService14.authenticateUser(userList19, "", "");
        model.User user26 = commonService1.authenticateUser(userList19, "", "hi!");
        model.User user29 = commonService0.authenticateUser(userList19, "", "");
        org.junit.Assert.assertNotNull(commonService1);
        org.junit.Assert.assertNotNull(recruiterService2);
        org.junit.Assert.assertNotNull(userArray5);
        org.junit.Assert.assertArrayEquals(userArray5, new model.User[] {});
        org.junit.Assert.assertTrue("'" + boolean7 + "' != '" + false + "'", boolean7 == false);
        org.junit.Assert.assertNull(user10);
        org.junit.Assert.assertNull(user13);
        org.junit.Assert.assertNotNull(recruiterService14);
        org.junit.Assert.assertNotNull(userArray18);
        org.junit.Assert.assertArrayEquals(userArray18, new model.User[] {});
        org.junit.Assert.assertTrue("'" + boolean20 + "' != '" + false + "'", boolean20 == false);
        org.junit.Assert.assertNull(user23);
        org.junit.Assert.assertNull(user26);
        org.junit.Assert.assertNull(user29);
    }

    @Test
    public void test38() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test38");
        service.CommonService commonService0 = service.CommonService.getInstance();
        service.CommonService commonService1 = service.CommonService.getInstance();
        service.RecruiterService recruiterService2 = service.RecruiterService.getInstance();
        recruiterService2.viewInterviewResult();
        recruiterService2.sendAssessment();
        model.User[] userArray5 = new model.User[] {};
        java.util.ArrayList<model.User> userList6 = new java.util.ArrayList<model.User>();
        boolean boolean7 = java.util.Collections.addAll((java.util.Collection<model.User>) userList6, userArray5);
        model.User user10 = recruiterService2.authenticateUser(userList6, "hi!", "");
        model.User user13 = commonService1.authenticateUser(userList6, "", "hi!");
        model.User user16 = commonService0.authenticateUser(userList6, "hi!", "hi!");
        model.User[] userArray17 = new model.User[] {};
        java.util.ArrayList<model.User> userList18 = new java.util.ArrayList<model.User>();
        boolean boolean19 = java.util.Collections.addAll((java.util.Collection<model.User>) userList18, userArray17);
        model.User user22 = commonService0.authenticateUser(userList18, "", "");
        org.junit.Assert.assertNotNull(commonService0);
        org.junit.Assert.assertNotNull(commonService1);
        org.junit.Assert.assertNotNull(recruiterService2);
        org.junit.Assert.assertNotNull(userArray5);
        org.junit.Assert.assertArrayEquals(userArray5, new model.User[] {});
        org.junit.Assert.assertTrue("'" + boolean7 + "' != '" + false + "'", boolean7 == false);
        org.junit.Assert.assertNull(user10);
        org.junit.Assert.assertNull(user13);
        org.junit.Assert.assertNull(user16);
        org.junit.Assert.assertNotNull(userArray17);
        org.junit.Assert.assertArrayEquals(userArray17, new model.User[] {});
        org.junit.Assert.assertTrue("'" + boolean19 + "' != '" + false + "'", boolean19 == false);
        org.junit.Assert.assertNull(user22);
    }

    @Test
    public void test39() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test39");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.deleteApplicantProfile();
        applicantService0.viewAllAvailableJobs();
        applicantService0.viewAllAvailableJobs();
        applicantService0.viewAllAvailableJobs();
    }

    @Test
    public void test40() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test40");
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        recruiterService0.updateStatusOfJobPost("hi!");
        recruiterService0.sendAssessment();
        recruiterService0.sendAssessment();
        recruiterService0.sendAssessment();
        recruiterService0.viewInterviewResult();
        org.junit.Assert.assertNotNull(recruiterService0);
    }

    @Test
    public void test41() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test41");
        service.ApplicantService applicantService0 = service.ApplicantService.getInstance();
        model.Job job1 = null;
        // The following exception was thrown during execution in test generation
        try {
            applicantService0.submitApplicationForm(job1, "", (java.lang.Integer) 0, "hi!");
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"model.Job.getId()\" because \"job\" is null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(applicantService0);
    }

    @Test
    public void test42() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test42");
        service.RecruiterService recruiterService0 = new service.RecruiterService();
        recruiterService0.updateStatusOfJobPost("hi!");
    }

    @Test
    public void test43() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test43");
        service.RecruiterService recruiterService0 = service.RecruiterService.getInstance();
        recruiterService0.viewInterviewResult();
        service.RecruiterService recruiterService2 = service.RecruiterService.getInstance();
        recruiterService2.viewInterviewResult();
        recruiterService2.sendAssessment();
        model.User[] userArray5 = new model.User[] {};
        java.util.ArrayList<model.User> userList6 = new java.util.ArrayList<model.User>();
        boolean boolean7 = java.util.Collections.addAll((java.util.Collection<model.User>) userList6, userArray5);
        model.User user10 = recruiterService2.authenticateUser(userList6, "hi!", "");
        model.User user13 = recruiterService0.authenticateUser(userList6, "hi!", "");
        org.junit.Assert.assertNotNull(recruiterService0);
        org.junit.Assert.assertNotNull(recruiterService2);
        org.junit.Assert.assertNotNull(userArray5);
        org.junit.Assert.assertArrayEquals(userArray5, new model.User[] {});
        org.junit.Assert.assertTrue("'" + boolean7 + "' != '" + false + "'", boolean7 == false);
        org.junit.Assert.assertNull(user10);
        org.junit.Assert.assertNull(user13);
    }

    @Test
    public void test44() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test44");
        service.RecruiterService recruiterService0 = new service.RecruiterService();
        recruiterService0.sendAssessment();
        model.Application application2 = null;
        // The following exception was thrown during execution in test generation
        try {
            recruiterService0.sendFeedback(application2);
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"model.Application.setFeedback(String)\" because \"application\" is null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
    }

    @Test
    public void test45() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "test.blackbox.RandomTesting.RegressionTest0.test45");
        service.ApplicantService applicantService0 = new service.ApplicantService();
        applicantService0.deleteApplicantProfile();
        applicantService0.viewAllAvailableJobs();
        applicantService0.deleteApplicantProfile();
        model.Job job4 = null;
        // The following exception was thrown during execution in test generation
        try {
            applicantService0.submitApplicationForm(job4, "hi!", (java.lang.Integer) 1, "");
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"model.Job.getId()\" because \"job\" is null");
        } catch (java.lang.NullPointerException e) {
            // Expected exception.
        }
    }
}


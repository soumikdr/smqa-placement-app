package utility;

import model.Application;
import model.Assignment;
import model.Job;
import model.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Utility {

    private static ArrayList<User> users;

    private static User currentUser;

    private static ArrayList<Assignment> assignments;

    private static ArrayList<Job> jobs;

    private static ArrayList<Application> applications;

    private static Map<String, List<String>> questionMap;

    public static String inputOutput(String message) {
        System.out.println(message);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        // *** FOR EXECUTING RANDOM TESTS ***
        // Below for preparation random testing since randoop can't put values to
        // console input.

        // 1. Uncomment below line
        // message = getRandomString();

        // 2. Comment whole try-catch block
        try {
            message = br.readLine();
        } catch (IOException e) {
            System.out.println("Error reading in value");
        }

        return message;
    }
    // 3. Uncomment method below and import java.util.Random and other libraries if
    // they won't import automatically;
//    public static String getRandomString() {
//        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
//        StringBuilder builder = new StringBuilder();
//        Random rnd = new Random();
//        while (builder.length() < 18) { // length of the random string.
//            int index = (int) (rnd.nextFloat() * chars.length());
//            builder.append(chars.charAt(index));
//        }
//        String randomString = builder.toString();
//        return randomString;
//    }

    public static ArrayList<Assignment> getAssignments() {
        return assignments;
    }

    public static void setAssignments(ArrayList<Assignment> assignments) {
        Utility.assignments = assignments;
    }

    public static ArrayList<Job> getJobs() {
        return jobs;
    }

    public static void setJobs(ArrayList<Job> jobs) {
        Utility.jobs = jobs;
    }

    public static void addJob(Job job) {
        if (jobs == null) {
            jobs = new ArrayList<>();
        }
        jobs.add(job);
    }

    public static ArrayList<Application> getApplications() {
        return applications;
    }

    public static void setApplications(ArrayList<Application> applications) {
        Utility.applications = applications;
    }

    public static void setUsers(ArrayList<User> users) {
        Utility.users = users;
    }

    public static ArrayList<User> getUsers() {
        return users;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        Utility.currentUser = currentUser;
    }

    public static Map<String, List<String>> getQuestionMap() {
        return questionMap;
    }

    public static void setQuestionMap(Map<String, List<String>> qMap) {
        Utility.questionMap = qMap;
    }

    public static ArrayList<String> getCommonInterviewQuestions() {
        ArrayList<String> interviewQuestions = new ArrayList<>();
        interviewQuestions.add("[1] Why are you interested in this position?");
        interviewQuestions.add("[2] How do you handle conflict in a team environment?");
        interviewQuestions.add("[3] What are your salary expectations?");

        return interviewQuestions;
    }
}

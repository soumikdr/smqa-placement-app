package utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import model.Application;
import model.Assignment;
import model.Job;
import model.User;

public class Utility {

    private static ArrayList<User> users;

    private static User currentUser;

    private static ArrayList<Assignment> assignments;

    private static ArrayList<Job> jobs;

    private static ArrayList<Application> applications;


    public static String inputOutput(String message) {
        System.out.println(message);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            message = br.readLine();
        } catch (IOException e) {
            System.out.println("Error reading in value");
            //return the page
        }
        return message;
    }



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
}

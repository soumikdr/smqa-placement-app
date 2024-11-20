import model.Application;
import model.Assignment;
import model.Job;
import model.User;
import service.CommonService;
import service.RecruiterService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Main {

    private static ArrayList<User> users;
    // We dont have to keep assignment. According to score we can send "pass" status.
    private static ArrayList<Assignment> assignments;

    private static ArrayList<Job> jobs;
    // we dont have to create this since we can create a variable on users for appliedJobs with ids. Not sure.
    private static ArrayList<Application> applications;


    public static void main(String[] args) {
        System.out.println(Utility.inputOutput("Example"));
        System.out.println("Hello world!");
        CommonService.accessLandingPage();
    }



}
import model.Application;
import model.Assignment;
import model.Job;
import model.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Main {

    private static ArrayList<User> users;
    // We dont have to keep assignment. According to score we can send "pass" status.
    private static ArrayList<Assignment> assignments;

    private static ArrayList<Job> jobs;
    // we dont have to create this also since we can create a variable on users for appliedJobs with ids. Not sure.
    private static ArrayList<Application> applications;


    public static void main(String[] args) {
        inputOutput("Example");
        System.out.println("Hello world!");

    }


    private static int inputOutput(String message) {
        System.out.println(message);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int returnValue = 0;
        try {
            returnValue = Integer.parseInt(br.readLine());
        } catch (NumberFormatException e) {
            System.out.println("Error reading in integer value");
            //return the page
        } catch (IOException e) {
            System.out.println("Error reading in value");
            //return the page
        }
        return returnValue;
    }
}
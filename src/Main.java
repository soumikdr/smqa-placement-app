import model.Applicant;
import model.Application;
import model.Assignment;
import model.Job;
import model.Recruiter;
import model.User;
import service.CommonService;
import utility.Utility;

import java.util.ArrayList;

public class Main {

    private static ArrayList<User> users;
    // We dont have to keep assignment. According to score we can send "pass" status.
    private ArrayList<Assignment> assignments;

    private ArrayList<Job> jobs;
    // we dont have to create this since we can create a variable on users for appliedJobs with ids. Not sure.
    private ArrayList<Application> applications;

    private static Utility utility;

    public static void main(String[] args) {
        CommonService commonService = new CommonService();
        commonService.accessLandingPage(users);


    }



}
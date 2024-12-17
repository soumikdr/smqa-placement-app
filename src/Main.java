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
    private static ArrayList<Assignment> assignments;

    private static ArrayList<Job> jobs;
    // we dont have to create this since we can create a variable on users for appliedJobs with ids. Not sure.
    private static ArrayList<Application> applications;

    private static Utility utility;

    public static void main(String[] args) {
        users=new ArrayList<>();
        jobs=new ArrayList<>();
        users.add(new User("1","John","Doe","johnDoe","bestpassword","Applicant"));
        users.add(new User("2","Ansar","Patil","darkAngel","123qwe","Recruiter"));
        jobs.add(new Job(
            "Job1",
            "Data Analyst", 
            "A data analyst's job is to collect, organize, and analyze data to help businesses solve problems and gain insights. ",
            "Active"));
        jobs.add(new Job(
            "Job2",
            "Frontend Developer",
            "As a Front End Developer you'll take ownership of technical projects, designing and developing user interfaces and client dashboards for cutting edge trading systems technology. ", 
            "Closed"));

        
        Utility.setUsers(users);
        Utility.setAssignments(assignments);
        Utility.setJobs(jobs);
        Utility.setApplications(applications);


        CommonService commonService = new CommonService();
        commonService.accessLandingPage();


    }


}
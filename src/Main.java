import model.Applicant;
import model.Application;
import model.Assignment;
import model.Job;
import model.Recruiter;
import model.User;
import service.CommonService;
import service.RecruiterService;
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
    	
    	
    	jobs=new ArrayList<Job>();
    	jobs.add(new Job("1", "JobName1", "JobDesc1", "Active"));
    	jobs.add(new Job("2", "JobName2", "JobDesc2", "Active"));
    	jobs.add(new Job("3", "JobName3", "JobDesc3", "Active"));


        users=new ArrayList<>();
        users.add(new User("1","John","Doe","johnDoe","bestpassword","Applicant"));
        users.add(new User("2","Ansar","Patil","darkAngel","123qwe","Recruiter"));
        users.add(new User("3","Shomik","Datta","xFireTomato","12345","Recruiter"));
        
        
        
        assignments=new ArrayList<Assignment>();
        assignments.add(new Assignment("1", "1", "John", null, null));
        
        
        applications=new ArrayList<>();
        applications.add(new Application("1", jobs.get(0).getId(), users.get(2).getId(), "UnderConsideration", assignments));



        Utility.setUsers(users);
        Utility.setAssignments(assignments);
        Utility.setJobs(jobs);
        Utility.setApplications(applications);

        RecruiterService.getInstance().viewTotalNumberOfApplications("asd");

        CommonService commonService = new CommonService();
        commonService.accessLandingPage();


    }


}
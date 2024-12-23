import model.*;
import service.ApplicantService;
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
    	
    	ArrayList<String> questions=new ArrayList<String>();
    	questions.add("Q1");
    	questions.add("Q2");
    
    	
    	assignments=new ArrayList<Assignment>();
    	assignments.add(new Assignment("AS1", "AP1", "Interview", questions, new ArrayList<String>()));
        Utility.setAssignments(assignments);

    	ApplicantService.getInstance().submitInterviewForm(assignments.get(0));
    	
    	
        users=new ArrayList<>();
        jobs=new ArrayList<>();
        jobs.add(new Job(
            "Job1",
            "Data Analyst", 
            "A data analyst's job is to collect, organize, and analyze data to help businesses solve problems and gain insights. ",
            JobStatus.PRIVATE
            ));
        jobs.add(new Job(
            "Job2",
            "Frontend Developer",
            "As a Front End Developer you'll take ownership of technical projects, designing and developing user interfaces and client dashboards for cutting edge trading systems technology. ",
            JobStatus.PUBLIC));
        users.add(new Applicant("1", "John", "Doe", "johnDoe", "bestpassword", new ArrayList<>()));
        users.add(new Recruiter("2", "Ansar", "Patil", "darkAngel", "123qwe"));

        
        Utility.setUsers(users);
        Utility.setJobs(jobs);
        Utility.setApplications(applications);

    	System.out.println(Utility.getAssignments().get(0).getAnswers().get(0));

        CommonService commonService = new CommonService();
        commonService.accessLandingPage();


    }


}
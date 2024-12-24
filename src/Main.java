import model.*;
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
        users=new ArrayList<>();
        jobs=new ArrayList<>();
        applications=new ArrayList<>();
        assignments=new ArrayList<>();
        ArrayList<String> questions = new ArrayList<String>();
        ArrayList<String> answers = new ArrayList<String>();

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
        users.add(new User("1","John","Doe","johnDoe","bestpassword","Applicant"));
        users.add(new User("2","Ansar","Patil","darkAngel","123qwe","Recruiter"));
        users.add(new User("3","Shomik","Datta","xFireTomato","12345","Recruiter"));




        questions.add("What is class in Java");
        questions.add("What is object in Java");
        answers.add("A class in Java is a set of objects which shares common characteristics/ behavior and common properties/ attributes.");
        answers.add("Objects are the instances of a class that are created to use the attributes and methods of a class.");

        assignments.add(new Assignment("Assignment1", "1", "Technical", questions, answers));

        applications.add(new Application("Application1", "Job1", "1", ApplicationStatus.INPROGRESS, assignments, "Feedback"));
        applications.add(new Application("Application2", "Job2", "1", ApplicationStatus.UNSUCCESSFUL, assignments, ""));

       Utility.setUsers(users);
        Utility.setAssignments(assignments);
        Utility.setJobs(jobs);
        Utility.setApplications(applications);


        CommonService commonService = new CommonService();
        commonService.accessLandingPage();


    }


}
import model.*;
import service.CommonService;
import utility.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    private static ArrayList<User> users;
    private static ArrayList<Assignment> assignments;
    private static ArrayList<Job> jobs;
    private static ArrayList<Application> applications;

    public static void main(String[] args) {

        users = new ArrayList<>();
        jobs = new ArrayList<>();
        applications = new ArrayList<>();
        assignments = new ArrayList<>();
        ArrayList<String> questions = new ArrayList<String>();
        ArrayList<String> answers = new ArrayList<String>();

        jobs.add(new Job(
                "Job1",
                "Data Analyst",
                "A data analyst's job is to collect, organize, and analyze data to help businesses solve problems and gain insights. ",
                JobStatus.PRIVATE));
        jobs.add(new Job(
                "Job2",
                "Frontend Developer",
                "As a Front End Developer you'll take ownership of technical projects, designing and developing user interfaces and client dashboards for cutting edge trading systems technology. ",
                JobStatus.PUBLIC));

        users.add(new Applicant("1", "John", "Doe", "johnDoe", "bestpassword"));
        users.add(new Recruiter("2", "Ansar", "Patil", "ansarPatil", "123qwe"));
        users.add(new Recruiter("3", "Shomik", "Datta", "shomikDatta", "12345"));

        questions.add("What is class in Java");
        questions.add("What is object in Java");
        answers.add(
                "A class in Java is a set of objects which shares common characteristics/ behavior and common properties/ attributes.");
        answers.add(
                "Objects are the instances of a class that are created to use the attributes and methods of a class.");

        assignments.add(new Assignment("Assignment1", "1", "Technical", questions, answers));

        applications.add(new Application("Application1", "Job1", "1", ApplicationStatus.INPROGRESS, assignments, 2,
                "BSc", "JS, CSS", "Feedback"));
        applications.add(new Application("Application2", "Job2", "1", ApplicationStatus.UNSUCCESSFUL, assignments, 2,
                "MSc", "Docker, Spring MVC", "Feedback 2"));

        // Create a HashMap to store questions
        Map<String, List<String>> questionMap = new HashMap<>();

        // Create a list of questions for the first key
        List<String> questionsFrontendDev = new ArrayList<>();
        questionsFrontendDev.add("[1] How would you conditionally render an element in JSX?");
        questionsFrontendDev.add("[2] What is the difference between let and const in JavaScript?");

        // Add the list of questions to the map with a key
        questionMap.put("frontend", questionsFrontendDev);

        // Create a list of questions for the second key
        List<String> questionsBackendDev = new ArrayList<>();
        questionsBackendDev.add("[1] What is REST?");
        questionsBackendDev.add("[2] What is the purpose of a database index?");

        // Add the list of questions to the map with a different key
        questionMap.put("backend", questionsBackendDev);

        Utility.setUsers(users);
        Utility.setAssignments(assignments);
        Utility.setJobs(jobs);
        Utility.setApplications(applications);
        Utility.setQuestionMap(questionMap);

        CommonService commonService = new CommonService();
        commonService.accessLandingPage();

    }

}
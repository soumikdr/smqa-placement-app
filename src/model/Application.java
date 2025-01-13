package model;

import utility.Utility;

import java.util.ArrayList;
import java.util.UUID;

public class Application {
    private String id;

    private String jobId;

    private String applicantId;

    private ApplicationStatus applicationStatus;

    private ArrayList<Assignment> assignments;

    private ArrayList<Assignment> interviewAssignments;

    private Integer yearOfExperience;

    private String education;

    private String skills;

    private String feedback;

    public Application(String id, String jobId, String applicantId, ApplicationStatus status,
                       ArrayList<Assignment> assignments, Integer yearOfExperience, String education, String skills,
                       String feedback) {
        this.id = id;
        this.jobId = jobId;
        this.applicantId = applicantId;
        this.applicationStatus = status;
        this.assignments = assignments;
        this.feedback = feedback;
        this.yearOfExperience = yearOfExperience;
        this.education = education;
        this.skills = skills;

        ArrayList<String> commonInterviewQuestions = Utility.getCommonInterviewQuestions();
        Assignment interview = new Assignment(UUID.randomUUID().toString(), applicantId, "interview",
                commonInterviewQuestions, null);
        this.assignments.add(interview);
    }

    public Application() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(String applicantId) {
        this.applicantId = applicantId;
    }

    public ApplicationStatus getStatus() {
        return applicationStatus;
    }

    public void setStatus(ApplicationStatus applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public ArrayList<Assignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(ArrayList<Assignment> assignments) {
        this.assignments = assignments;
    }

    public Integer getYearOfExperience() {
        return yearOfExperience;
    }

    public void setYearOfExperience(Integer yearOfExperience) {
        this.yearOfExperience = yearOfExperience;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public ArrayList<Assignment> getInterviewAssignments() {
        if (interviewAssignments == null) {
            return new ArrayList<Assignment>();
        }
        return interviewAssignments;
    }

    public void setInterviewAssignments(ArrayList<Assignment> interviewAssignments) {
        this.interviewAssignments = interviewAssignments;
    }
}

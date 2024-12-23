package model;

import java.util.ArrayList;

public class Application {
    private String id;

    private String jobId;

    private String applicantId;

    private String status;

    private ArrayList<Assignment> assignments;

    private Integer yearOfExperience;

    private String education;

    private String skills;

    public Application(String id, String jobId, String applicantId, String status, ArrayList<Assignment> assignments, Integer yearOfExperience, String education, String skills) {
        this.id = id;
        this.jobId = jobId;
        this.applicantId = applicantId;
        this.status = status;
        this.assignments = assignments;
        this.yearOfExperience = yearOfExperience;
        this.education = education;
        this.skills = skills;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
}

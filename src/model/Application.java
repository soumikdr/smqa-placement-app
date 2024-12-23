package model;

import java.util.ArrayList;

public class Application {
    private String id;

    private String jobId;

    private String applicantId;

    private ApplicationStatus applicationStatus;

    private ArrayList<Assignment> assignments;

    public Application(String id, String jobId, String applicantId, ApplicationStatus applicationStatus, ArrayList<Assignment> assignments) {
        this.id = id;
        this.jobId = jobId;
        this.applicantId = applicantId;
        this.applicationStatus = applicationStatus;
        this.assignments = assignments;
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

    public ArrayList<Assignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(ArrayList<Assignment> assignments) {
        this.assignments = assignments;
    }
}

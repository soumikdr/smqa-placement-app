package model;

public class Job {

    private String id;

    private String jobName;

    private String jobDescription;

    private JobStatus jobStatus;


    public Job(String id, String jobName, String jobDescription, JobStatus jobStatus) {
        this.id = id;
        this.jobName = jobName;
        this.jobDescription = jobDescription;
        this.jobStatus = jobStatus;
    }

    public Job() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public JobStatus getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(JobStatus jobStatus) {
        this.jobStatus = jobStatus;
    }
}

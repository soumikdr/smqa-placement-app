package model;

import java.util.ArrayList;

public class Assignment {
    private String id;

    private String applicantId;

    private String assignmentName;

    private ArrayList<String> questions;

    private ArrayList<String> answers;

    private AssignmentStatus status;

    public Assignment(String id, String applicantId, String assignmentName, ArrayList<String> questions,
                      ArrayList<String> answers) {
        this.id = id;
        this.applicantId = applicantId;
        this.assignmentName = assignmentName;
        this.questions = questions;
        this.answers = answers;
        this.status = AssignmentStatus.PASSIVE;
    }

    public Assignment() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(String applicantId) {
        this.applicantId = applicantId;
    }

    public String getAssignmentName() {
        return assignmentName;
    }

    public void setAssignmentName(String assignmentName) {
        this.assignmentName = assignmentName;
    }

    public ArrayList<String> getQuestions() {
        if (questions == null) {
            return new ArrayList<>();
        }
        return questions;
    }

    public void setQuestions(ArrayList<String> questions) {
        this.questions = questions;
    }

    public ArrayList<String> getAnswers() {
        if (answers == null) {
            return new ArrayList<>();
        }
        return answers;
    }

    public void setAnswers(ArrayList<String> answers) {
        this.answers = answers;
    }

    public AssignmentStatus getStatus() {
        return status;
    }

    public void setStatus(AssignmentStatus status) {
        this.status = status;
    }

}

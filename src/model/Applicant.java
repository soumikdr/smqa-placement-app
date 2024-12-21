package model;

import java.util.ArrayList;

public class Applicant extends User{

    private ArrayList<Application> applications;

    public Applicant(String id, String name, String lastName, String userName, String passWord, ArrayList<Application> applications) {
        super(id, name, lastName, userName, passWord, UserRole.APPLICANT);
        this.applications = applications;
    }

    public Applicant() {
    }

    public ArrayList<Application> getApplications() {
        return applications;
    }

    public void setApplications(ArrayList<Application> applications) {
        this.applications = applications;
    }
}

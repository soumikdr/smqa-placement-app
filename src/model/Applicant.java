package model;

import utility.Utility;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Applicant extends User {

    public Applicant(String id, String name, String lastName, String userName, String passWord) {
        super(id, name, lastName, userName, passWord, UserRole.APPLICANT);
    }

    public Applicant() {
    }

    // Utility
    // applications (all applicants)

    // Applicant
    // applications (for specific)

    public ArrayList<Application> getApplications() {
        ArrayList<Application> _applications = new ArrayList<Application>(
                Utility.getApplications().stream().filter(
                        a -> a.getApplicantId().equals(this.getId())).collect(Collectors.toList()));

        return _applications;
    }
}

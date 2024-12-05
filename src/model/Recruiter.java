package model;

public class Recruiter extends User{

    public Recruiter(String id, String name, String lastName, String userName, String password) {
        super(id, name, lastName, userName, password, "recruiter");
    }

    public Recruiter() {
    }
}

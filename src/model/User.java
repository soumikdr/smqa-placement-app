package model;

public class User {

    private String id;

    private String name;

    private String lastName;

    private String userName;

    private String password;

    private String role;

    public User() {
    }

    public User(String id, String name, String lastName, String userName, String password, String role) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void greetUser() {
        System.out.println("Welcome " + name + " " + lastName);
    }
}

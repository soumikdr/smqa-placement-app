package service;

import model.User;
import utility.Utility;

public class ApplicantService {


    public void submitAssessmentForm(){
    }
    public void viewAssessment(){

    }
    public void submitInterviewForm(){

    }
    public void viewInterview(){

    }
    public void viewFeedback(){

    }
    public void viewJobPost(){

    }
    public void viewApplicationForm(){

    }
    public void viewAllAvailableJobs(){

    }
    public void submitApplicationForm(){

    }
    public void viewApplicantDashboard(){

    }

    public void viewApplicantProfilePage(){
        User user = Utility.getCurrentUser();
        System.out.println("\nApplicant Profile\n");

        if (user.getId() == null || user.getId().isEmpty()) {
            System.out.println("ID is missing.");
        } else {
            System.out.println("ID: " + user.getId());
        }

        if (user.getName() == null || user.getName().isEmpty()) {
            System.out.println("Name is missing.");
        } else {
            System.out.println("Name: " + user.getName());
        }

        if (user.getLastName() == null || user.getLastName().isEmpty()) {
            System.out.println("Last Name is missing.");
        } else {
            System.out.println("Last Name: " + user.getLastName());
        }

        if (user.getUserName() == null || user.getUserName().isEmpty()) {
            System.out.println("Username is missing.");
        } else {
            System.out.println("Username: " + user.getUserName());
        }

        if (user.getRole() == null || user.getRole().isEmpty()) {
            System.out.println("Role is missing.");
        } else {
            System.out.println("Role: " + user.getRole());
        }

        String answer = Utility.inputOutput("Type anything to go back to the dashboard?");
        
        if (!answer.isEmpty()) {
            viewApplicantDashboard();
        }
    }

    public void deleteApplicantProfile(){

    }
    public void updateApplicantProfile(){

    }
    public void viewApplicantApplications(){

    }
    public void viewSpecificApplication(){

    }
    public void withdrawApplication(){

    }
    public void viewJobDescFromApplication(){

    }
    public void viewApplicationProcessDashboard(){}



}

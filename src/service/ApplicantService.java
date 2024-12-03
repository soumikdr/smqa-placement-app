package service;

import utility.Utility;

public class ApplicantService {

    private final CommonService commonService;


    public ApplicantService() {
        commonService = new CommonService();
    }


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

        System.out.println("Welcome to the Applicant Dashboard\n");

        System.out.println("1. View Profile");
        System.out.println("2. View Applications");
        System.out.println("3. View Available Jobs");

        System.out.println("\n4. Log Out");
        switch (Utility.inputOutput("Please Select One Of The Options")){
            case "1":{
                System.out.println("Directing to Profile Page...");
                viewApplicantProfilePage();
                break;
            }
            case "2":{
                System.out.println("Directing to Applications Page...");
                viewApplicantApplications();
                break;
            }
            case "3":{
                System.out.println("Directing to Available Jobs Page...");
                viewAllAvailableJobs();
                break;
            }
            case "4":{
                System.out.println("Logging Out...");
                commonService.logOut();
                break;
            }
            default:{
                System.out.println("You entered invalid option");
                viewApplicantDashboard();
                break;
            }
        }

    }
    public void viewApplicantProfilePage(){

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

package service;

public class RecruiterService {

    public static RecruiterService instance;

    public static RecruiterService getInstance(){
        if(instance == null){
            instance = new RecruiterService();
        }
        return instance;
    }

    public void sendAssessment(){
    }
    public void viewAssessmentResult(){

    }
    public void sendInterview(){

    }
    public void viewInterviewResult(){

    }

    public void viewRecruiterDashboard(){
        System.out.println("Welcome to Recruiter Dashboard\n");
    }

    public void viewRecruiterProfilePage(){

    }
    public void deleteRecruiterProfile(){

    }
    public void updateRecruiterProfile(){

    }

    public void viewAvailableJobs(){

    }

    public void viewSpecificJobPost(){

    }
    public void updateDescriptionOfJobPost(){

    }

    public void updateStatusOfJobPost(){

    }
    public void viewTotalNumberOfApplications(){

    }

    public void viewJobPostingForm(){

    }
    public void submitNewJobPost(){

    }

    public void viewAllApplications(){

    }
    public void viewSpecificApplication(){

    }
    public void sendFeedback(){

    }
    public void viewFeedbackForm(){

    }


}

package service;

public class RecruiterService extends UserService {

    private static RecruiterService instance = null;

    public static RecruiterService getInstance() {
        if (instance == null) {
            instance = new RecruiterService();
        }
        return instance;
    }

    public void sendAssessment() {
    }

    public void viewAssessmentResult() {

    }

    public void sendInterview() {

    }

    public void viewInterviewResult() {

    }


    public void viewRecruiterProfilePage() {

    }

    public void deleteRecruiterProfile() {

    }

    public void updateRecruiterProfile() {

    }

    public void viewAvailableJobs() {

    }

    public void viewSpecificJobPost() {

    }

    public void updateDescriptionOfJobPost() {

    }

    public void updateStatusOfJobPost() {

    }

    public void viewTotalNumberOfApplications() {

    }

    public void viewJobPostingForm() {

    }

    public void submitNewJobPost() {

    }

    public void viewAllApplications() {

    }

    public void viewSpecificApplication() {

    }

    public void sendFeedback() {

    }

    public void viewFeedbackForm() {

    }


    @Override
    public void viewDashboard() {
        System.out.println("Welcome to Recruiter Dashboard\n");
    }

    @Override
    public void viewProfilePage() {

    }

    @Override
    public void deleteProfile() {

    }

    @Override
    public void updateProfile() {

    }
}

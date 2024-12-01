package tests;

import model.Recruiter;
import model.User;
import org.junit.Test;
import service.CommonService;

import java.util.ArrayList;
import java.util.List;

public class CommonServiceTests {

    private ArrayList<User> getUsers() {
        return new ArrayList<>(
                List.of(
                        new Recruiter("1", "John", "Doe", "johndoe", "password"),
                        new Recruiter("2", "Jane", "Doe", "janedoe", "password"),
                        new Recruiter("3", "John", "Smith", "johnsmith", "password")
                )
        );
    }

    @Test
    public void testAuthenticateRecruiter() {
        CommonService commonService = new CommonService();
        ArrayList<User> users = getUsers();
        User user = commonService.authenticateUser(users, "johndoe", "password");
        assert user != null;
    }

    @Test
    public void testAuthenticateRecruiterFailOne() {
        CommonService commonService = new CommonService();
        ArrayList<User> users = getUsers();
        User user = commonService.authenticateUser(users, "johndoe", "password1");
        assert user == null;
    }

    @Test
    public void testAuthenticateRecruiterFailTwo() {
        CommonService commonService = new CommonService();
        ArrayList<User> users = getUsers();
        User user = commonService.authenticateUser(users, "johndoe1", "password1");
        assert user == null;
    }
}

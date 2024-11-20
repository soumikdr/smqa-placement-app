import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Utility {


    public static String inputOutput(String message) {
        System.out.println(message);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            message = br.readLine();
        } catch (NumberFormatException e) {
            System.out.println("Error reading in integer value");
            //return the page
        } catch (IOException e) {
            System.out.println("Error reading in value");
            //return the page
        }
        return message;
    }
}

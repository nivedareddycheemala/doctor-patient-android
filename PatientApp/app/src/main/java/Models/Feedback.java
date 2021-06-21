package Models;

public class Feedback {

    private String message,username,usernumber;

    public Feedback(String message, String username, String usernumber) {
        this.message = message;
        this.username = username;
        this.usernumber=usernumber;
    }

    public Feedback() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsernumber() {
        return usernumber;
    }

    public void setUsernumber(String usernumber) {
        this.usernumber = usernumber;
    }
}

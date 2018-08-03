package Events;
import static client.WindowType.LOGIN;

public class AccountLoginWindow extends AnonymousLoginWindow {
    private String username;
    private String password;
    private String email;

    public AccountLoginWindow(){
        this.setWindowType(LOGIN);
    }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



}

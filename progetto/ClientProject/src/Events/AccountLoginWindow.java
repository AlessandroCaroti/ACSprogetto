package Events;
import static guiClient.WindowType.LOGIN;

public class AccountLoginWindow extends AnonymousLoginWindow {
    private String usernameOrEmail;
    private String password;

    public AccountLoginWindow(){
        this.setWindowType(LOGIN);
    }



    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



}

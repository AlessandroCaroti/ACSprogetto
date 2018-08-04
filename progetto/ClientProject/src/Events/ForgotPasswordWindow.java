package Events;

import static client.WindowType.FORGOTPASSWORD;
import static java.util.Objects.requireNonNull;

public class ForgotPasswordWindow extends Window {

    private String newPassword;
    private String repeatPassword;
    private String email;
    public ForgotPasswordWindow(){
        this.setWindowType(FORGOTPASSWORD);
    }


    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = requireNonNull(repeatPassword);
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = requireNonNull(newPassword);
    }

    public void setEmail(String email){
        this.email=requireNonNull(email);
    }

    public String getEmail(){
        return email;
    }
}

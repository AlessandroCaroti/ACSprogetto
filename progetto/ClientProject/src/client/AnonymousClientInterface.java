package client;

public interface AnonymousClientInterface {//implementare da strafo

    //login

    void anonymousLoginGUI(String serverIp,String serverPort);

    void loginAccountGUI(String serverIp,String serverPort,String emailOrUsername,String password);

    //new account

    void newAccountGUI(String username,String password,String repeatPassword,String email);

    //recover password

    void recoverPasswordGUI(String email,String newPassword,String repeatPassword);

    //insert email code

    void insertEmailCodeGUI(String emailCode);

    //



//todo arma deve cambiare il jtetxfield del login da port a email in quanto non ci interessa che l'utente sappia la porta di destinazione



}

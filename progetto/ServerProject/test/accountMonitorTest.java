

import customException.MaxNumberAccountReached;
import server.AccountCollectionInterface;
import server.AccountListMonitor;
import utility.Account;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

public class accountMonitorTest {
    private  ExecutorService executors= Executors.newCachedThreadPool();
    private AccountCollectionInterface accountMonitor=new AccountListMonitor();

    public static void main(String[] args)
    {
         accountMonitorTest test=new accountMonitorTest();
         System.out.println("maxlength: "+test.accountMonitor.getMAXACCOUNTNUMBER());


         /*addAccount*/
         test.executors.submit(() ->{
             while (true) {
                 try {
                     int randomNum = ThreadLocalRandom.current().nextInt(-100, 310);
                     Account account =
                             new Account(String.valueOf(randomNum), String.valueOf(randomNum), null, null, 0, "esempioMail@qualcosa.com");
                     System.out.println("adding" + test.accountMonitor.addAccount(account));

                 } catch(MaxNumberAccountReached | NullPointerException exc)
                 {
                     System.out.println(exc.getClass().getSimpleName());

                 } catch(Exception exc)
                 {
                     exc.printStackTrace();
                     System.exit(1);
                     return ;
                 }

             }
         });

        /*remove*/
        test.executors.submit(() ->{
            Account account;
            while (true) {
                try {

                    int randomNum = ThreadLocalRandom.current().nextInt(-100, 310);
                    account = test.accountMonitor.removeAccount(randomNum);
                    if (account != null)
                        System.out.println("removed " + randomNum);

                } catch (IllegalArgumentException exc) {
                    System.out.println(exc.getClass().getSimpleName());

                } catch (Exception exc) {
                    exc.printStackTrace();
                    System.exit(1);
                }
            }
        });

        /*getaccountcopy*/
        test.executors.submit(() ->{
            try {
                while (true) {
                    int randomNum = ThreadLocalRandom.current().nextInt(0, 298 );
                    Account account=test.accountMonitor.getAccountCopy(randomNum);
                    if(account!=null)
                        System.out.println("get a copy of "+ randomNum);
                }
            }catch(Exception exc)
            {
                exc.printStackTrace();
            }
        });

        /*addaccount con id*/
/*
        test.executors.submit(() ->{
            try {
                while (true) {
                    int randomNum = ThreadLocalRandom.current().nextInt(0, 10 );
                    Account account =
                            new Account(String.valueOf(randomNum), String.valueOf(randomNum), null, "public_key", 0);
                    if(test.accountMonitor.addAccount(account,randomNum)){
                        System.out.println("added account on position"+ randomNum);
                    }else{ System.out.println("Unable to add account on position"+ randomNum);}
                }
            }catch(Exception exc)
            {
                exc.printStackTrace();
            }
        });
*/

    /*getpublickey*/
        test.executors.submit(() ->{
            try {
                while (true) {
                    int randomNum = ThreadLocalRandom.current().nextInt(-30, 350 );//oltre la dimensione massima
                    Account account =
                            new Account(String.valueOf(randomNum), String.valueOf(randomNum), null, null, 0,"esempioMail@qualcosa.com");
                    try {
                        String pkey = test.accountMonitor.getPublicKey(randomNum);
                        if (pkey == null) {
                            System.out.println("get null public key from account " + randomNum);
                        } else {
                            System.out.println("get " + pkey + " public key from account " + randomNum);
                        }
                    }catch (IllegalArgumentException | NullPointerException exc){
                        System.out.println(exc.getClass().getSimpleName());
                    }
                }
            }catch(Exception exc)
            {
                exc.printStackTrace();
            }
        });

        //getpassword

        test.executors.submit(() ->{
            try {
                while (true) {
                    int randomNum = ThreadLocalRandom.current().nextInt(-100, 350 );
                    Account account =
                            new Account(String.valueOf(randomNum), String.valueOf(randomNum), null, null, 0, "esempioMail@qualcosa.com");
                    try {
                        byte [] pass = test.accountMonitor.getPassword(randomNum);
                        if (pass == null) {
                            System.out.println("get null password from account " + randomNum);
                        } else {
                            System.out.println("get DEFINED password from account " + randomNum);
                        }
                    }catch (IllegalArgumentException | NullPointerException exc){
                        System.out.println(exc.getClass().getSimpleName());
                    }
                }
            }catch(Exception exc)
            {
                exc.printStackTrace();
            }
        });





    }
}

package test;

import customException.MaxNumberAccountReached;
import interfaces.ClientInterface;
import server.AccountCollectionInterface;
import server.AccountListMonitor;
import utility.Account;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

public class accountMonitorTest {
    public  ExecutorService executors= Executors.newCachedThreadPool();
    public AccountCollectionInterface accountMonitor=new AccountListMonitor();


    public static void main(String[] args)
    {
         accountMonitorTest test=new accountMonitorTest();
         System.out.println("maxlength:"+test.accountMonitor.getMaxLength());


         /*addAccount*//*
         test.executors.submit(() ->{
             while (true) {
                 try {
                     int randomNum = ThreadLocalRandom.current().nextInt(0, 298);
                     Account account =
                             new Account(String.valueOf(randomNum), String.valueOf(randomNum), null, null, 0);
                     System.out.println("adding" + test.accountMonitor.addAccount(account));

                 } catch(MaxNumberAccountReached exc)
                 {
                     System.err.println(exc.getClass().getSimpleName());

                 } catch(Exception exc)
                 {
                     exc.printStackTrace();
                     return ;
                 }

             }
         });*/

        /*remove*/
        test.executors.submit(() ->{
            Account account;
            try {
                while (true) {
                    int randomNum = ThreadLocalRandom.current().nextInt(0, 298);
                    account=test.accountMonitor.removeAccount(randomNum);
                    if(account!=null)
                        System.out.println("removed "+ randomNum);
                }
            }catch(Exception exc)
            {
                exc.printStackTrace();
            }
        });

        /*getaccountcopy*/  /*TODO non va!*//*
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
*/
        /*addaccount con id*/

        test.executors.submit(() ->{
            try {
                while (true) {
                    int randomNum = ThreadLocalRandom.current().nextInt(0, 298 );
                    Account account =
                            new Account(String.valueOf(randomNum), String.valueOf(randomNum), null, null, 0);
                    if(test.accountMonitor.addAccount(account,randomNum)){
                        System.out.println("added account on position"+ randomNum);
                    }else{ System.out.println("Unable to add account on position"+ randomNum);}
                }
            }catch(Exception exc)
            {
                exc.printStackTrace();
            }
        });



        /*getpublickey
        test.executors.submit(() ->{
            try {
                while (true) {
                    int randomNum = ThreadLocalRandom.current().nextInt(0, 298 );
                    Account account =
                            new Account(String.valueOf(randomNum), String.valueOf(randomNum), null, null, 0);
                    String pkey=test.accountMonitor.getPublicKey(randomNum);
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
                    int randomNum = ThreadLocalRandom.current().nextInt(0, 298 );
                    Account account =
                            new Account(String.valueOf(randomNum), String.valueOf(randomNum), null, null, 0);
                    byte[] pass=test.accountMonitor.getPassword(randomNum);
                }
            }catch(Exception exc)
            {
                exc.printStackTrace();
            }
        });

        //getusername
        test.executors.submit(() ->{
            try {
                while (true) {
                    int randomNum = ThreadLocalRandom.current().nextInt(0, 298 );
                    Account account =
                            new Account(String.valueOf(randomNum), String.valueOf(randomNum), null, null, 0);
                    String username=test.accountMonitor.getUsername(randomNum);
                }
            }catch(Exception exc)
            {
                exc.printStackTrace();
            }
        });

        //getstub
        test.executors.submit(() ->{
            try {
                while (true) {
                    int randomNum = ThreadLocalRandom.current().nextInt(0, 298 );
                    Account account =
                            new Account(String.valueOf(randomNum), String.valueOf(randomNum), null, null, 0);
                    ClientInterface stub=test.accountMonitor.getStub(randomNum);
                }
            }catch(Exception exc)
            {
                exc.printStackTrace();
            }
        });

        //getusername
        test.executors.submit(() ->{
            try {
                while (true) {
                    int randomNum = ThreadLocalRandom.current().nextInt(0, 298 );
                    Account account =
                            new Account(String.valueOf(randomNum), String.valueOf(randomNum), null, null, 0);
                    String username=test.accountMonitor.getUsername(randomNum);
                }
            }catch(Exception exc)
            {
                exc.printStackTrace();
            }
        });



        //getlenght
/*
        test.executors.submit(() ->{
            try {
                while (true) {
                    int lenght=test.accountMonitor.getLength();
                    System.out.println(lenght);
                }
            }catch(Exception exc)
            {
                exc.printStackTrace();
            }
        });
*/
    }




}

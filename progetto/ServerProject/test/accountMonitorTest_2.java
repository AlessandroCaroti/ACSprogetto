import account.AccountCollectionInterface;
import account.AccountListMonitor;
import customException.MaxNumberAccountReached;
import server.RandomString;
import utility.Account;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class accountMonitorTest_2 {
    private ExecutorService executors=Executors.newCachedThreadPool();
    private AccountCollectionInterface accountCollection=new AccountListMonitor();
    private LinkedBlockingQueue<Runnable> codaTask=new LinkedBlockingQueue<>();
    private RandomString randomStringGenerator=new RandomString(7,new Random());

    public static void main(String[] args) {
        Random random=new Random();
        accountMonitorTest_2 classe=new accountMonitorTest_2();


        classe.executors.submit(()->{
            while(true){
             classe.executors.submit(classe.codaTask.take());
            }
        });

        while(true){
                switch(random.nextInt(8)){
                    case 0://putIfAbsentEmailUsername and removecheckemail and Ismember
                        classe.codaTask.offer(()->{
                            try{
                                Account account=classe.createRandomAccount();
                                int i=classe.accountCollection.putIfAbsentEmailUsername(account.copy());
                                Thread.sleep(100);
                                classe.accountCollection.isMember(account.getEmail(),account.getUsername());
                                Thread.sleep(100);
                                classe.accountCollection.removeAccountCheckEmail(i,account.getEmail());
                            }catch(NullPointerException| MaxNumberAccountReached  exc){

                            }catch(Exception exc){
                                exc.printStackTrace();
                                System.exit(1);
                            }

                        });
                        break;
                    case 1://getAccountCopy and removeAccount
                        classe.codaTask.offer(()->{
                            try{
                                int i=random.nextInt(classe.accountCollection.getMAXACCOUNTNUMBER());
                                classe.accountCollection.getAccountCopy(i);
                                Thread.sleep(100);
                                classe.accountCollection.removeAccount(i);
                            }catch(NullPointerException exc){

                            }catch(Exception exc){
                                exc.printStackTrace();
                                System.exit(1);
                            }

                        });
                        break;
                    case 2://getAccountCopyUsername and getAccountCopyEmail and Ismember
                        classe.codaTask.offer(()->{
                            try{
                                Account account=classe.createRandomAccount();
                                classe.accountCollection.addAccount(account.copy());
                                Thread.sleep(100);
                                classe.accountCollection.isMember(account.getEmail(),account.getUsername());
                                Thread.sleep(100);
                                classe.accountCollection.getAccountCopyUsername(account.getUsername());
                                Thread.sleep(100);
                                classe.accountCollection.getAccountCopyEmail(account.getEmail());

                            }catch(NullPointerException| MaxNumberAccountReached |IllegalArgumentException exc){

                            }catch(Exception exc){
                                exc.printStackTrace();
                                System.exit(1);
                            }

                        });
                        break;
                    case 3://removeAccount
                        classe.codaTask.offer(()->{
                            try{
                                classe.accountCollection.removeAccount(random.nextInt(classe.accountCollection.getMAXACCOUNTNUMBER()));
                            }catch(IllegalArgumentException exc){

                            }catch(Exception exc){
                                exc.printStackTrace();
                                System.exit(1);
                            }

                        });
                        break;
                    case 4://remove
                        classe.codaTask.offer(()->{
                            try{
                                classe.accountCollection.removeAccount(random.nextInt(classe.accountCollection.getMAXACCOUNTNUMBER()));
                            }catch(Exception exc){
                                exc.printStackTrace();
                                System.exit(1);
                            }

                        });
                        break;
                    case 5://i vari getter
                        try{
                            classe.accountCollection.getPublicKey(random.nextInt(classe.accountCollection.getMAXACCOUNTNUMBER()));
                            Thread.sleep(15);
                            classe.accountCollection.getPassword(random.nextInt(classe.accountCollection.getMAXACCOUNTNUMBER()));
                            Thread.sleep(15);
                            classe.accountCollection.getUsername(random.nextInt(classe.accountCollection.getMAXACCOUNTNUMBER()));
                            Thread.sleep(15);
                            classe.accountCollection.getStub(random.nextInt(classe.accountCollection.getMAXACCOUNTNUMBER()));
                            Thread.sleep(15);
                            classe.accountCollection.getEmail(random.nextInt(classe.accountCollection.getMAXACCOUNTNUMBER()));
                            Thread.sleep(15);
                        }catch(NullPointerException exc){

                        }
                        catch (Exception exc){
                            exc.printStackTrace();
                            System.exit(1);
                        }
                        break;
                    case 6://i vari setter
                        try{
                            classe.accountCollection.setPassword("password",random.nextInt(classe.accountCollection.getMAXACCOUNTNUMBER()));
                            Thread.sleep(15);
                            classe.accountCollection.setStub(null,random.nextInt(classe.accountCollection.getMAXACCOUNTNUMBER()));

                        }catch(NullPointerException exc){

                        }
                        catch (Exception exc){
                            exc.printStackTrace();
                            System.exit(1);
                        }
                        break;
                    case 7://quando ci sono troppi account fa pulizia
                        try{
                            if(classe.accountCollection.getNumberOfAccount()+5<=classe.accountCollection.getMAXACCOUNTNUMBER()) {
                                for(int i=0;i<classe.accountCollection.getMAXACCOUNTNUMBER();i++)
                                    classe.accountCollection.removeAccount(i);
                            }
                        } catch (Exception exc){
                            exc.printStackTrace();
                            System.exit(1);
                        }
                        break;

                }


            try {
                while (classe.codaTask.size() > 100) {
                    Thread.sleep(100);
                }
            }catch (InterruptedException exc){
                    exc.printStackTrace();
                    System.exit(1);
            }
        }

    }

    private Account createRandomAccount(){
        return new Account(randomStringGenerator.nextString(),
                randomStringGenerator.nextString(),
                null,null,0,randomStringGenerator.nextString()+"@gmail.com");

    }



}

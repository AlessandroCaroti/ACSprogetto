import account.AccountCollectionInterface;
import account.AccountListMonitor;
import customException.MaxNumberAccountReached;
import server.utility.RandomString;
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
                                System.out.println(Thread.currentThread().getId()+":0");
                                Account account=classe.createRandomAccount();
                                int i=classe.accountCollection.putIfAbsentEmailUsername(account.copy());
                                Thread.sleep(random.nextInt(100));
                                classe.accountCollection.isMember(account.getEmail(),account.getUsername());
                                Thread.sleep(random.nextInt(100));
                                if(i!=-2&&i!=-1)classe.accountCollection.removeAccountCheckEmail(i,account.getEmail());
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
                                System.out.println(Thread.currentThread().getId()+":1");
                                int i=random.nextInt(classe.accountCollection.getMAXACCOUNTNUMBER());
                                classe.accountCollection.getAccountCopy(i);
                                Thread.sleep(random.nextInt(100));
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
                                System.out.println(Thread.currentThread().getId()+":2");
                                Account account=classe.createRandomAccount();
                                classe.accountCollection.addAccount(account.copy());
                                Thread.sleep(random.nextInt(100));
                                classe.accountCollection.isMember(account.getEmail(),account.getUsername());
                                Thread.sleep(random.nextInt(100));
                                classe.accountCollection.getAccountCopyUsername(account.getUsername());
                                Thread.sleep(random.nextInt(100));
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
                                System.out.println(Thread.currentThread().getId()+":3");
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
                                System.out.println(Thread.currentThread().getId()+":4");
                                classe.accountCollection.removeAccount(random.nextInt(classe.accountCollection.getMAXACCOUNTNUMBER()));
                            }catch(Exception exc){
                                exc.printStackTrace();
                                System.exit(1);
                            }

                        });
                        break;
                    case 5://i vari getter
                        try{
                            System.out.println(Thread.currentThread().getId()+":5");
                            classe.accountCollection.getPublicKey(random.nextInt(classe.accountCollection.getMAXACCOUNTNUMBER()));
                            Thread.sleep(random.nextInt(100));
                            classe.accountCollection.getPassword(random.nextInt(classe.accountCollection.getMAXACCOUNTNUMBER()));
                            Thread.sleep(random.nextInt(100));
                            classe.accountCollection.getUsername(random.nextInt(classe.accountCollection.getMAXACCOUNTNUMBER()));
                            Thread.sleep(random.nextInt(100));
                            classe.accountCollection.getStub(random.nextInt(classe.accountCollection.getMAXACCOUNTNUMBER()));
                            Thread.sleep(random.nextInt(100));
                            classe.accountCollection.getEmail(random.nextInt(classe.accountCollection.getMAXACCOUNTNUMBER()));
                            Thread.sleep(random.nextInt(100));
                        }catch(NullPointerException exc){

                        }
                        catch (Exception exc){
                            exc.printStackTrace();
                            System.exit(1);
                        }
                        break;
                    case 6://i vari setter
                        try{
                            System.out.println(Thread.currentThread().getId()+":6");
                            classe.accountCollection.setPassword("password",random.nextInt(classe.accountCollection.getMAXACCOUNTNUMBER()));
                            Thread.sleep(random.nextInt(100));
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
                            System.err.println(Thread.currentThread().getId()+":7");
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
                while (classe.codaTask.size() > 9000) {
                    Thread.sleep(100);
                }
            }catch (InterruptedException exc){
                    exc.printStackTrace();
                    System.exit(1);
            }
        }

    }

    private  synchronized Account createRandomAccount(){
        return new Account(randomStringGenerator.nextString(),
                randomStringGenerator.nextString(),
                null,null,0,randomStringGenerator.nextString()+"@gmail.com");

    }



}

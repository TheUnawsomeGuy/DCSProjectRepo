
public class SimpleThreadDemo {
    private static int Resource = 0; //initial value of the resource
    private Object lock = new Object(); //lock the object (add better explanation)
    public static void main(String[] args) {
        System.out.println("Communication between threads");

        Thread depositService1 = new Thread(new SimpleThreadDemo().new DepositService(), "DepositService1"); //create a new thread for the deposit service
        Thread withdrawService1 = new Thread(new SimpleThreadDemo().new WithdrawService(), "WithdrawService1"); //create a new thread for the withdraw service
        Thread auditService1 = new Thread(new SimpleThreadDemo().new AuditService(), "AuditService1"); //create a new thread for the audit service

        depositService1.start();
        withdrawService1.start();
        auditService1.start();

        try {
            depositService1.join();
            withdrawService1.join();
            auditService1.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Final Resource: " + Resource);
        System.out.println("Main thread ends");
    }
    
    //deposit service to deposit money into the resource
    class DepositService implements Runnable {
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (lock) {
                    int oldValue = Resource;

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }

                    Resource += 10;
                    System.out.println("[" + Thread.currentThread().getName() + "] "+
                     "Deposit #" + i + " : " + oldValue + " + 10 = " + Resource); //print the deposit number, the old value, and the new value
                }

                try {
                    Thread.sleep(400); //sleep for 0.4 seconds to ensure the deposit service is not executed before the withdraw service
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }

    //withdraw service to withdraw money from the resource
    class WithdrawService implements Runnable {
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (lock) {
                    int oldValue = Resource;

                    if (Resource >= 6) { //check if the resource is greater than 6 to avoid negative values

                    try {
                        Thread.sleep(700);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }

                    Resource -= 6;
                    System.out.println("[" + Thread.currentThread().getName() + "] "+
                     "Withdraw #" + i + " : " + oldValue + " - 6 = " + Resource); //print the withdraw number, the old value, and the new value

                    } else {
                        System.out.println("[" + Thread.currentThread().getName() + "] "+
                         "Withdraw #" + i + " : " + "Not enough money to withdraw: " + Resource);
                    }

                    try {
                        Thread.sleep(400); //sleep for 0.4 seconds to ensure the withdraw service is not executed before the deposit service
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }
        }
    }

    //audit service to print the current balance of the resource
    class AuditService implements Runnable {
        public void run() {
            for (int i = 0; i < 10; i++) {
                System.out.println("[" + Thread.currentThread().getName() + "] Audit #" + i + " Current Balance: " + Resource);

                try {
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }
}



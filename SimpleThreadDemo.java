
public class SimpleThreadDemo {
    private static int Resource = 0; //initial value of the resource
    private Object lock = new Object(); //lock the object (add better explanation)
    public static void main(String[] args) {
        System.out.println("Communication between threads");

        Thread depositService1 = new Thread(new SimpleThreadDemo().new DepositService(), "DepositService1"); //create a new thread for the deposit service

        depositService1.start();

        try {
            depositService1.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Final Resource: " + Resource);
        System.out.println("Main thread ends");
    }

    class DepositService implements Runnable {
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (lock) {
                    int oldValue = Resource;

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }

                    Resource += 10;
                    System.out.println("[" + Thread.currentThread().getName() + "] "+
                     "Deposit #" + i + " : " + oldValue + " + 10 = " + Resource); //print the deposit number, the old value, and the new value
                }
            }
        }
    }
}



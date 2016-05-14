public class DeadlockExample extends Thread {

    private static Object lock1 = new Object();
    private static Object lock2 = new Object();

    public void method1() {
        synchronized (lock1) {
            try {
                Thread.sleep(500);
                System.out.println("Acquired lock1 now waiting for lock2 " + Thread.currentThread().getName());

                synchronized (lock2) {
                    System.out.println("method 1 complete " + Thread.currentThread().getName());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void method2() {
        synchronized (lock2) {
            try {
                Thread.sleep(500);
                System.out.println("Acquired lock2 now waiting for lock1 " + Thread.currentThread().getName());
                synchronized (lock1) {
                    System.out.println("method 2 complete " + Thread.currentThread().getName());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void run() {
        method1();
        method2();
    }

    public static void main(String[] args) {
        Thread t1 = new DeadlockExample();
        t1.start();
        Thread t2 = new DeadlockExample();
        t2.start();
    }
}

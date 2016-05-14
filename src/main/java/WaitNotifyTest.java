public class WaitNotifyTest extends Thread {
    final WaitNotify waitNotify;
    final boolean isNotifying;

    public WaitNotifyTest(WaitNotify waitNotify, boolean isNotifying) {
        this.waitNotify = waitNotify;
        this.isNotifying = isNotifying;
    }

    public void run() {
        if (isNotifying) {
            waitNotify.doSomethingAndNotify();
        } else {
            waitNotify.waitForMessage();
        }
    }

    public static void main(String[] args) {
        WaitNotify waitNotify = new WaitNotify();
        Thread t1 = new Thread(new WaitNotifyTest(waitNotify, false));
        Thread t2 = new Thread(new WaitNotifyTest(waitNotify, true));
        t1.start();
        t2.start();
    }

    private static class WaitNotify {

        boolean taskDone;

        public synchronized void waitForMessage() {
            while (!taskDone) {
                try {
                    System.out.println("task is not done, waiting " + Thread.currentThread().getName());
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("task is done " + Thread.currentThread().getName());
            }
        }

        public synchronized void doSomethingAndNotify() {
            taskDone = true;
            try {
                Thread.sleep(5000);
                notifyAll();
                System.out.println("Notified and task is done " + Thread.currentThread().getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
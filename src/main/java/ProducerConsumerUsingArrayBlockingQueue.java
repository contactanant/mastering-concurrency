import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class ProducerConsumerUsingArrayBlockingQueue {

    public static class Consumer extends Thread {
        private final BlockingQueue<String> queue;
        private final int MAX_WAIT_THRESHOLD_IN_SECONDS = 10;

        public Consumer(BlockingQueue<String> queue) {
            this.queue = queue;
        }

        public void run() {
            long lastMessageReceived = System.currentTimeMillis();
            while (notPassedWaitThreshold(lastMessageReceived)) {
                try {
                    TimeUnit.SECONDS.sleep(2);
                    String message = queue.poll(100, TimeUnit.MILLISECONDS);
                    if (message != null) {
                        lastMessageReceived = System.currentTimeMillis();
                        System.out.println("I received message " + message + " he ha ha");
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private boolean notPassedWaitThreshold(long lastMessageReceived) {
            return ((System.currentTimeMillis() - lastMessageReceived) / 1000) < MAX_WAIT_THRESHOLD_IN_SECONDS;
        }

    }

    public static class Producer extends Thread {
        private final BlockingQueue<String> queue;

        public Producer(BlockingQueue<String> queue) {
            this.queue = queue;
        }

        public void run() {

            IntStream.range(1, 11).forEach(
                    i -> {
                        try {
                            TimeUnit.SECONDS.sleep(2);
                            System.out.println("Offering into queue message " + i);
                            queue.offer("message " + i);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
            );
        }
    }

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(5);
        Thread producer = new Producer(queue);
        producer.start();
        Thread consumer = new Consumer(queue);
        consumer.start();
        producer.join();
    }
}

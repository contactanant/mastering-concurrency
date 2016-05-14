import java.util.concurrent.*;

public class FutureTaskExample {

    private static class InternalTask implements Callable<Integer> {

        public void doSomething() throws InterruptedException {
            TimeUnit.SECONDS.sleep(1);
        }

        public Integer call() throws Exception {
            doSomething();
            return 1;
        }
    }

    private static class ExternalTask implements Callable<Integer> {

        public void doSomething() throws InterruptedException {
            TimeUnit.SECONDS.sleep(10);
        }

        public Integer call() throws Exception {
            doSomething();
            return 10;
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
        InternalTask internalTask = new InternalTask();
        ExternalTask externalTask = new ExternalTask();
        FutureTask<Integer> task1 = new FutureTask<>(internalTask);
        FutureTask<Integer> task2 = new FutureTask<>(externalTask);
        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.submit(task1);
        executor.submit(task2);
        while (true) {

            if (task1.isDone() && task2.isDone()) {
                System.out.println("Shutting down");
                executor.shutdown();
                break;
            }
            if (!task1.isDone()) {
                Integer result = task1.get();
                System.out.println("Task 1 output is " + result);
            }
            if (!task2.isDone()) {
                try {
                    System.out.println("Waiting for task2 for 5 seconds ");
                    Integer result = task2.get(5, TimeUnit.SECONDS);
                    if (result != null) {
                        System.out.println("Task 2 output is " + result);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
//                    e.printStackTrace();
                    System.out.println("Cancelling task 2 enough is enough ");
                    task2.cancel(true);
                }
            }
        }
    }
}

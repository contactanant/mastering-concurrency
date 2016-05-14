import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ForkJoinExample extends RecursiveTask<Integer> {

    public static int COMPUTER_SIZE = 2;

    int[] numbers;
    int begin;
    int end;

    public ForkJoinExample(int[] numbers, int begin, int end) {
        this.numbers = numbers;
        this.begin = begin;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        if (end - begin <= COMPUTER_SIZE) {
            int sum = 0;
            for (int i = begin - 1; i < end; i++) {
                sum += numbers[i];
            }
            System.out.println("computed sum " + sum);
            return sum;
        } else {
            int mid = begin + (end - begin) / 2;
            ForkJoinExample forkJoinExampleLeft = new ForkJoinExample(numbers, begin, mid);
            ForkJoinExample forkJoinExampleRight = new ForkJoinExample(numbers, mid + 1, end);
            forkJoinExampleLeft.fork();
            Integer rightResult = forkJoinExampleRight.compute();
            Integer leftResult = forkJoinExampleLeft.join();
            System.out.println("left join answer is " + leftResult + " right join answer is " + rightResult);
            return leftResult + rightResult;
        }
    }

    public static void main(String[] args) {
        int numberOfCpuCores = Runtime.getRuntime().availableProcessors();
        ForkJoinPool forkJoinPool = new ForkJoinPool(numberOfCpuCores);
        System.out.println("so you have cpu cores " + numberOfCpuCores);
        forkJoinPool.invoke(new ForkJoinExample(new int[]{1, 2, 3, 4, 5}, 1, 5));
    }
}

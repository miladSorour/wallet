import java.time.LocalTime;
import java.util.concurrent.CountDownLatch;

public class SimpleSimultaneousStart {
    public static void main(String[] args) throws InterruptedException {
        int threadCount = 5;

        CountDownLatch startLatch = new CountDownLatch(1); // All threads wait on this
        CountDownLatch readyLatch = new CountDownLatch(threadCount); // Main waits for threads to be ready

        for (int i = 1; i <= threadCount; i++) {
            int id = i;
            new Thread(() -> {
                System.out.println("Thread " + id + " is ready. " + LocalTime.now().toNanoOfDay());
                readyLatch.countDown(); // Signal that this thread is ready
                try {
                    startLatch.await(); // Wait for the signal to start
                    System.out.println("Thread " + id + " started. " + LocalTime.now().toNanoOfDay());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        }

        readyLatch.await(); // Wait until all threads are ready
        System.out.println("All threads are ready. Go!");
        startLatch.countDown(); // Start all threads
    }
}

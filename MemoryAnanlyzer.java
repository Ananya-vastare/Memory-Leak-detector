
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MemoryAnanlyzer {
    static class MemorySnapshot {
        long used;
        long committed;
        long max;

        public MemorySnapshot(long used, long committed, long max) {
            this.used = used;
            this.committed = committed;
            this.max = max;
        }

        @Override
        public String toString() {
            return "Used: " + used + " | Committed: " + committed + " | Max: " + max;
        }
    }

    public static MemorySnapshot fetchAndLogMemory() {
        try {
            MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
            MemoryUsage heap = memoryBean.getHeapMemoryUsage();

            long used = heap.getUsed();
            long committed = heap.getCommitted();
            long max = heap.getMax();
            return new MemorySnapshot(used, committed, max);

        } catch (Exception e) {
            e.printStackTrace();
            return null; // or any default value indicating failure
        }
    }

    public static void main(String[] args) {
        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);

        service.scheduleAtFixedRate(() -> {
            MemorySnapshot snapshot = fetchAndLogMemory();
            if (snapshot != null) {
                System.out.println(snapshot);
            }
        }, 0, 10, TimeUnit.SECONDS);
    }
}
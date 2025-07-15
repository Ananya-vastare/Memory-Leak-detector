import java.sql.Timestamp;
import java.util.Date;
import java.util.ArrayList;

public class Memorychecker {

    static class MemoryRecord {
        String timestamp;
        long usedMemory;
        long committedMemory;
        long maxMemory;
        boolean memoryLeak;

        public MemoryRecord(String timestamp, long usedMemory, long committedMemory, long maxMemory,
                boolean memoryLeak) {
            this.timestamp = timestamp;
            this.usedMemory = usedMemory;
            this.committedMemory = committedMemory;
            this.maxMemory = maxMemory;
            this.memoryLeak = memoryLeak;
        }

        @Override
        public String toString() {
            return "Time: " + timestamp +
                    " | Used Memory: " + usedMemory + " bytes" +
                    " | Committed: " + committedMemory +
                    " | Max: " + maxMemory +
                    " | Memory Leak: " + memoryLeak;
        }
    }

    public static void main(String[] args) {
        ArrayList<MemoryRecord> details = new ArrayList<>();

        MemoryAnanlyzer.MemorySnapshot snapshot = MemoryAnanlyzer.fetchAndLogMemory();
        if (snapshot == null) {
            System.out.println("Memory fetch failed.");
            return;
        }

        long used = snapshot.used;
        long committed = snapshot.committed;
        long max = snapshot.max;
        boolean memoryLeak = used > 200 * 1024 * 1024;

        Timestamp timestamp = new Timestamp(new Date().getTime());
        String timeString = timestamp.toString();

        MemoryRecord record = new MemoryRecord(timeString, used, committed, max, memoryLeak);
        details.add(record);

        System.out.println(record.toString());

        // Log memory usage for the Python script
        System.out.println("CSV_LOG: " + timeString + "," + (used / (1024.0 * 1024.0)));
    }
}

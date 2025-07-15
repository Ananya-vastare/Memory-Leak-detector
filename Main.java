import java.io.*;
import java.util.concurrent.*;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Memory Log Output");
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new java.awt.Dimension(500, 300));
        frame.add(scrollPane);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        new Thread(() -> {
            try {
                File logFile = new File("memory.log");
                if (!logFile.exists()) {
                    logFile.createNewFile();
                }

                Process compile = new ProcessBuilder("javac", "Memorychecker.java", "MemoryAnanlyzer.java").start();
                compile.waitFor();

                ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
                Runnable task = () -> {
                    try {
                        Process process = new ProcessBuilder("java", "Memorychecker").start();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            String finalLine = line;
                            SwingUtilities.invokeLater(() -> textArea.append(finalLine + "\n"));

                            if (line.startsWith("CSV_LOG:")) {
                                try (BufferedWriter writer = new BufferedWriter(new FileWriter("memory.log", true))) {
                                    String csvLine = line.replace("CSV_LOG: ", "").trim();
                                    writer.write(csvLine);
                                    writer.newLine();
                                } catch (IOException e) {
                                    System.err.println("Failed to write to memory.log: " + e.getMessage());
                                }
                            }
                        }
                        process.waitFor();
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                };

                scheduler.scheduleAtFixedRate(task, 0, 5, TimeUnit.SECONDS);

                frame.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        scheduler.shutdownNow();
                    }
                });
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}

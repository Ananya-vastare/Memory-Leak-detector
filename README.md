# Memory-Leak-detector

#  Memory Leak Detector with Live Visualization

A cross-language project that detects memory usage patterns in Java applications and visualizes them in real-time using Python and `matplotlib`.

##  Overview

This project:
- Collects JVM memory usage using Java.
- Detects potential memory leaks.
- Logs usage data in real-time to a file (`memory.log`).
- Uses Python to read this log and plot live graphs of memory usage.

---

##  Technologies Used

- **Java**: Memory monitoring, leak detection logic.
- **Swing GUI**: For viewing Java logs in a simple GUI.
- **Python 3.x**: For live plotting.
- **Matplotlib**: Graphing tool for memory usage.
- **ScheduledExecutorService**: Periodic Java task scheduler.

---

##  Project Structure
MemoryLeakdetector/
├── Main.java # Java GUI + Scheduler
├── Memorychecker.java # Fetches memory data + detects leak
├── MemoryAnanlyzer.java # Provides memory snapshot (used in checker)
├── memory_plot.py # Python script to visualize memory usage
├── memory.log # Appended log of memory usage (created automatically)
└── README.md # This file


### Requirements

- JDK 8 or higher
- Python 3.6+
- Python packages:

##  How It Works
A graph is displayed where if the graph spikes up it indicates a leak else it indicates that the memory has no leak

## How to run it
1) Run Main.java to launch the memory logger and GUI:
``
javac Main.java Memorychecker.java MemoryAnanlyzer.java && java Main

3) In a separate terminal, run the Python plot script:
``
python memory_plot.py

View logs in the GUI and live memory usage graph from memory.log.

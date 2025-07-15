import matplotlib.pyplot as plt
import matplotlib.animation as animation
import datetime
import os

LOG_FILE = "memory.log"
leak_threshold = 20  # MB over 30s

# Ensure log file exists
if not os.path.exists(LOG_FILE):
    with open(LOG_FILE, "w") as f:
        f.write("")


def read_memory_log():
    times = []
    usage = []
    try:
        with open(LOG_FILE, "r") as f:
            for line in f:
                parts = line.strip().split(",")
                if len(parts) != 2:
                    continue
                time_str, mem_str = parts
                dt = datetime.datetime.strptime(
                    time_str.strip(), "%Y-%m-%d %H:%M:%S.%f"
                )
                times.append(dt)
                usage.append(float(mem_str.strip()))
    except Exception as e:
        print(f"Error reading log: {e}")
    return times, usage


fig, ax = plt.subplots()
(line,) = ax.plot([], [], lw=2)
warning_text = ax.text(
    0.5, 0.9, "", transform=ax.transAxes, color="red", ha="center", fontsize=12
)


def update(frame):
    times, usage = read_memory_log()
    if not times:
        return (line,)

    line.set_data(times, usage)
    ax.relim()
    ax.autoscale_view()

    if len(usage) >= 6 and (usage[-1] - usage[-6]) > leak_threshold:
        warning_text.set_text("⚠️ Memory Leak Suspected!")
    else:
        warning_text.set_text("")

    return line, warning_text


ani = animation.FuncAnimation(fig, update, interval=3000)
plt.title("Live Memory Usage (MB)")
plt.xlabel("Time")
plt.ylabel("Used Memory")
plt.tight_layout()
plt.show()

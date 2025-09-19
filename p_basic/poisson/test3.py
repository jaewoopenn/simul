import matplotlib.pyplot as plt
import numpy as np

# Example: Generate arrival times from a Poisson process

class gl:
    arrival_rate = 0.5  # average arrivals per unit time
    num_arrivals = 50
    
# Visualize as a timeline event plot
def vis(times):
    plt.figure(figsize=(10, 2))
    plt.eventplot(times, orientation='horizontal', colors='blue')
    plt.xlabel('Time')
    plt.title('Arrival Times Visualization')
    plt.yticks([])  # Removes y-axis ticks
    plt.grid(axis='x')
    plt.show()
    
if __name__ == '__main__':
    np.random.seed(0)
    inter_arrival_times = np.random.exponential(scale=1/gl.arrival_rate, size=gl.num_arrivals)
    arrival_times = np.cumsum(inter_arrival_times)
    
    vis(arrival_times)
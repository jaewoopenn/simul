import numpy as np

def simulate_poisson_arrivals(rate, n_arrivals):
    """Simulate integer arrival times for a Poisson process."""
    # Generate inter-arrival times with exponential distribution
    inter_arrival_times = np.random.exponential(1/rate, n_arrivals)
    # Cumulate to get arrival times (continuous)
    arrival_times = np.cumsum(inter_arrival_times)
    # Convert arrival times to integers (floor)
    integer_arrival_times = np.floor(arrival_times).astype(int)
    # Ensure integer arrival times are strictly increasing
    for i in range(1, len(integer_arrival_times)):
        if integer_arrival_times[i] <= integer_arrival_times[i-1]:
            integer_arrival_times[i] = integer_arrival_times[i-1] + 1
    return integer_arrival_times

# Example usage
rate = 0.1  # average arrivals per unit time
n_arrivals = 10
print(simulate_poisson_arrivals(rate, n_arrivals))

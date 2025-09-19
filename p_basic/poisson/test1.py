import numpy as np
import matplotlib.pyplot as plt
from scipy.stats import poisson, expon

class PoissonProcessSimulator:
    """
    Comprehensive Poisson Process Simulator for Integer Arrival Times
    """

    def __init__(self, rate):
        self.rate = rate

    def continuous_process(self, time_horizon):
        """Generate continuous Poisson process arrival times"""
        arrivals = []
        current_time = 0

        while current_time < time_horizon:
            inter_arrival_time = np.random.exponential(1/self.rate)
            current_time += inter_arrival_time
            if current_time < time_horizon:
                arrivals.append(current_time)

        return np.array(arrivals)

    def integer_arrivals_rounded(self, time_horizon):
        """Generate integer arrival times by rounding continuous process"""
        continuous_arrivals = self.continuous_process(time_horizon)
        integer_arrivals = np.round(continuous_arrivals).astype(int)
        return np.unique(integer_arrivals)

    def discrete_process(self, time_horizon):
        """Generate discrete Poisson process (arrivals only at integer times)"""
        arrivals = []
        for t in range(int(time_horizon) + 1):
            num_arrivals = np.random.poisson(self.rate)
            arrivals.extend([t] * num_arrivals)
        return np.array(arrivals)

    def counting_process(self, time_points):
        """Simulate Poisson counting process N(t) at given time points"""
        arrivals = self.continuous_process(max(time_points))
        counts = [np.sum(arrivals <= t) for t in time_points]
        return np.array(counts), arrivals

    def compare_methods(self, time_horizon, num_simulations=100):
        """Compare all three methods statistically"""
        continuous_counts = []
        rounded_counts = []
        discrete_counts = []

        for _ in range(num_simulations):
            continuous = self.continuous_process(time_horizon)
            continuous_counts.append(len(continuous))

            rounded = self.integer_arrivals_rounded(time_horizon)
            rounded_counts.append(len(rounded))

            discrete = self.discrete_process(time_horizon)
            discrete_counts.append(len(discrete))

        return {
            'continuous': np.array(continuous_counts),
            'rounded': np.array(rounded_counts),
            'discrete': np.array(discrete_counts),
            'theoretical_mean': self.rate * time_horizon
        }

# Example usage:
if __name__ == "__main__":
    # Initialize simulator
    simulator = PoissonProcessSimulator(rate=2.0)

    # Generate single realization
    print("Single Realization (λ=2.0, T=10):")
    continuous = simulator.continuous_process(10)
    rounded = simulator.integer_arrivals_rounded(10)
    discrete = simulator.discrete_process(10)

    print(f"Continuous arrivals: {len(continuous)} events")
    print(f"Rounded integer arrivals: {rounded}")
    print(f"Discrete process arrivals: {len(discrete)} events")

    # Statistical comparison
    results = simulator.compare_methods(20, 50)
    print(f"\nStatistical Comparison (50 runs, T=20):")
    print(f"Theoretical mean: {results['theoretical_mean']}")
    print(f"Continuous: {np.mean(results['continuous']):.1f} ± {np.std(results['continuous']):.1f}")
    print(f"Rounded: {np.mean(results['rounded']):.1f} ± {np.std(results['rounded']):.1f}")
    print(f"Discrete: {np.mean(results['discrete']):.1f} ± {np.std(results['discrete']):.1f}")

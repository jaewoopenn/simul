package queue;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class z_array1 {

    @SuppressWarnings("unused")
	public static void main(String[] args) {
        double lambda = 2.0; // Arrival rate
        double mu = 3.0;     // Service rate
        int numCustomers = 10; // Number of customers to simulate

        Queue<Double> queue = new LinkedList<>();
        Random rand = new Random();

        // Generate first arrival time
        double nextArrival = generateExponential(lambda, rand);
        double nextDeparture = Double.POSITIVE_INFINITY; // no departure yet
        double currentTime = 0.0;

        System.out.println("Customer\tArrival Time\tService Start\tDeparture Time");

        for (int i = 0; i < numCustomers; i++) {
            double arrivalTime = nextArrival;
            queue.add(arrivalTime);

            // Generate next arrival time
            nextArrival = arrivalTime + generateExponential(lambda, rand);

            // Service start time: either customer arrival or when last departure ended
            currentTime = Math.max(arrivalTime, currentTime);
            double serviceTime = generateExponential(mu, rand);
            double departureTime = currentTime + serviceTime;
            currentTime = departureTime;

            // Remove customer from queue (served)
            queue.poll();

            System.out.printf("%d\t\t%.4f\t\t%.4f\t\t%.4f%n", i + 1, arrivalTime, Math.max(arrivalTime, currentTime - serviceTime), departureTime);
        }
    }

    // Method to generate exponential random variables
    private static double generateExponential(double rate, Random rand) {
        return -Math.log(1 - rand.nextDouble()) / rate;
    }
}

package patient;

import java.util.Random;

public class Patient {

    static final double HI_RATIO = 0.3;
    static Random random = new Random();
    int id;
    int arrivalTime;
    String criticality; // "HI" or "LO"
    int goldenTime;
    int executionTime;
    int absoluteDeadline;
    int remainingExecTime;
    int originalExecTime;
    double priorityScore;
    boolean isPreempted;

    public Patient(int id, int arrivalTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.isPreempted = false;

        if (random.nextDouble() < HI_RATIO) {
            this.criticality = "HI";
            this.goldenTime = 30 + random.nextInt(31); // 30 ~ 60
            this.executionTime = 20 + random.nextInt(21); // 20 ~ 40
        } else {
            this.criticality = "LO";
            this.goldenTime = 120 + random.nextInt(121); // 120 ~ 240
            this.executionTime = 10 + random.nextInt(21); // 10 ~ 30
        }

        this.absoluteDeadline = this.arrivalTime + this.goldenTime;
        this.remainingExecTime = this.executionTime;
        this.originalExecTime = this.executionTime;
    }

    @Override
    public String toString() {
        return String.format("[%d:%s]", id, criticality);
    }
}

// ==========================================

package patient;

import java.util.Random;

import util.SLog;

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
    int preemptCount=0;
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

    public Patient(String s) {
        String[] sc=s.split(" ");
        this.id = Integer.valueOf(sc[0]).intValue();
        this.criticality = sc[1];
        this.arrivalTime = Integer.valueOf(sc[2]).intValue();
        this.goldenTime = Integer.valueOf(sc[3]).intValue();
        this.executionTime = Integer.valueOf(sc[4]).intValue();
        this.isPreempted = false;
        this.absoluteDeadline = this.arrivalTime + this.goldenTime;
        this.remainingExecTime = this.executionTime;
        this.originalExecTime = this.executionTime;
	}

	@Override
    public String toString() {
        return String.format("[%d:%s]", id, criticality);
    }

	public String getStr() {
		String s="";
		s+=this.id+" ";
		s+=this.criticality+" ";
		s+=this.arrivalTime+" ";
		s+=this.goldenTime+" ";
		s+=this.executionTime;
		return s;
	}

	public void prn() {
		SLog.prn(getStr());
		
	}

	public String getRS(int i) {
		String s=id+" ";
		s+=criticality+" ";
		s+=preemptCount+" ";
		s+=i;
		return s;
	}
}

// ==========================================

package patient;

import java.util.Random;

public class PUtil {
    // 랜덤 객체 (전역 사용)
    static Random random = new Random();
    static final double PENALTY_SCORE = 9999.0;
    // 쁘아송 분포 생성 (Knuth's algorithm)
    public static int getPoissonArrivalCount(double lambda) {
        double L = Math.exp(-lambda);
        int k = 0;
        double p = 1.0;
        while (p > L) {
            k++;
            p *= random.nextDouble();
        }
        return k - 1;
    }
    // 우선순위 계산
    public static void calculatePriority(Patient p, boolean isEmergencyMode, double alpha) {
        double di = p.absoluteDeadline;
        double ri = p.arrivalTime;

        if (isEmergencyMode) {
            if (p.criticality.equals("HI")) {
                p.priorityScore = di;
            } else {
                p.priorityScore = di + PENALTY_SCORE;
            }
        } else {
            p.priorityScore = (alpha * di) + ((1.0 - alpha) * ri);
        }
    }

}

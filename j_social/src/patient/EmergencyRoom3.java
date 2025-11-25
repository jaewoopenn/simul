package patient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import util.MList;
import util.SLog;

public class EmergencyRoom3 {

    // ==========================================
    // 1. 설정 (Configuration)
    // ==========================================
    final int SIMULATION_TIME = 1440; // 1일 (분 단위)
    final int NUM_DOCTORS = 3;

    final double HIGH_THRESHOLD = 0.8;
    final double LOW_THRESHOLD = 0.4;

    final int SWITCH_COST = 5;
    final double thresholdEnter = NUM_DOCTORS * HIGH_THRESHOLD;
    final double thresholdExit = NUM_DOCTORS * LOW_THRESHOLD;


    // 통계 변수
    private int hiLived = 0;
    private int hiDied = 0;
    private int loProcessed = 0;
    private int loDropped = 0;
    private int preemptionCount = 0;
    private int triageDropCount = 0;
    private int admissionDenyCount = 0;
    private int burstCount = 0;
    
    private List<Integer> loWaitTimes = new ArrayList<>();
    private int cur_time = 0;
    private List<Patient> waitingQueue = new ArrayList<>();
    private Patient[] doctors = new Patient[NUM_DOCTORS]; // 의사 슬롯 (null이면 빈자리)
    private boolean isEmergencyMode = false;
    private int g_id=0;
    private MList rs_ml;

    // ==========================================
    // 2. 시뮬레이션 실행 메인 로직
    // ==========================================

	public void run() {



		MList ml=MList.load("patient/test.txt");
		rs_ml=MList.new_list();
//		ml.prn();
        System.out.println("Simulation Started...");
 
        while (cur_time < SIMULATION_TIME) {
        	loadPatient(ml);
            double cur_load = cal_load();

            mode_switch(cur_load);
            
            preemption();
            
            assign_Doc();
            
            process_pat();

            handle_WQ();

            cur_time++;
        }
        rs_ml.saveTo("patient/rs.txt");
        results();
	}




	private void preemption() {
        // ---------------------------------------
        // 5. Preemption (선점)
        // ---------------------------------------
        if (isEmergencyMode && !waitingQueue.isEmpty()) {
            Patient topPatient = waitingQueue.get(0);
            boolean isHopeless = (cur_time + topPatient.remainingExecTime > topPatient.absoluteDeadline);

            if (topPatient.criticality.equals("HI") && !isHopeless) {
                // 빈 의사가 있는지 확인
                boolean hasFreeDoctor = false;
                for (Patient d : doctors) {
                    if (d == null) {
                        hasFreeDoctor = true;
                        break;
                    }
                }

                if (!hasFreeDoctor) {
                    // LO 환자를 치료 중인 의사 찾기
                    int targetDocIdx = -1;
                    for (int i = 0; i < NUM_DOCTORS; i++) {
                        if (doctors[i] != null && doctors[i].criticality.equals("LO")) {
                            targetDocIdx = i;
                            break;
                        }
                    }

                    // 교체 수행
                    if (targetDocIdx != -1) {
                        Patient evictedLo = doctors[targetDocIdx];
                        Patient incomingHi = waitingQueue.remove(0); // 큐에서 제거

                        evictedLo.isPreempted = true;
                        evictedLo.preemptCount++;
                        waitingQueue.add(evictedLo); // 쫓겨난 LO는 다시 큐로

                        doctors[targetDocIdx] = incomingHi;
                        doctors[targetDocIdx].remainingExecTime += SWITCH_COST;
                        preemptionCount++;
                    }
                }
            }
        }
		
	}




	private void mode_switch(double cur_load) {
        // ---------------------------------------
        // 3. 모드 전환
        // ---------------------------------------
        if (!isEmergencyMode) {
            if (cur_load >= thresholdEnter) {
                isEmergencyMode = true;
            }
        } else {
            if (cur_load <= thresholdExit) {
                isEmergencyMode = false;
            }
        }
//        cur_load=Math.round(cur_load*10)/10.0;

        double cur_alpha;
        cur_alpha =  Math.min(cur_load / NUM_DOCTORS, 1.0);
        cur_alpha=Math.round(cur_alpha*10)/10.0;
        if (isEmergencyMode) {
            cur_alpha = 1.0;
        }
    	if(cur_time%10==0)
    		SLog.prn(cur_time+": "+cur_load+" "+cur_alpha);
//    	cur_alpha=1;
        // ---------------------------------------
        // 4. 우선순위 갱신 및 정렬
        // ---------------------------------------
        for (Patient p : waitingQueue) {
            PUtil.calculatePriority(p, isEmergencyMode, cur_alpha);
        }
        // 점수가 낮은 순으로 정렬
        Collections.sort(waitingQueue, Comparator.comparingDouble(p -> p.priorityScore));
		
	}


	private String getPatient(MList ml) {
    	String s=ml.get(g_id);
    	if(s==null)
    		return null;
    	String[] sc=s.split(" ");
    	int ar=Integer.valueOf(sc[2]).intValue();
    	if(cur_time<ar) {
    		return null;
    	}
        g_id++;
		return s;
	}


	private void loadPatient(MList ml) {
        int numArrivals = 0;
//        SLog.prn(currentTime+"");
        while(true) {
        	String s=getPatient(ml);
        	if(s==null) 
        		break;
        	numArrivals++;
        	Patient newPatient = new Patient(s);
//        	newPatient.prn();
        	// Admission Control (Emergency 모드일 때 LO 거부)
        	if (isEmergencyMode && newPatient.criticality.equals("LO")) {
        		rs_ml.add(newPatient.getRS(2));
        		loDropped++;
        		admissionDenyCount++;
        	} else {
        		waitingQueue.add(newPatient);
        	}

        }
        if (numArrivals >= 2) {
            burstCount++;
        }
		
	}








	private void assign_Doc() {

        // ---------------------------------------
        // 6. 의사 배정 + Triage
        // ---------------------------------------
        for (int i = 0; i < NUM_DOCTORS; i++) {
            if (doctors[i] == null) {
                while (!waitingQueue.isEmpty()) {
                    Patient candidate = waitingQueue.get(0); // 확인만 하고
                    
                    // 가망 없는 환자 Triage
                    int finishTime = cur_time + candidate.remainingExecTime;
                    if (finishTime > candidate.absoluteDeadline) {
                        waitingQueue.remove(0); // 실제 제거
                        if (candidate.criticality.equals("HI")) {
                            hiDied++;
                            triageDropCount++;
                            rs_ml.add(candidate.getRS(3));
                        } else {
                            loDropped++;
                            rs_ml.add(candidate.getRS(3));
                        }
                        continue; // 다음 환자 확인
                    }

                    // 배정 가능
                    doctors[i] = waitingQueue.remove(0); // 제거 및 배정
                    break;
                }
            }
        }
		
	}




	private void process_pat() {
        // ---------------------------------------
        // 7. 치료 진행
        // ---------------------------------------
        for (int i = 0; i < NUM_DOCTORS; i++) {
            if (doctors[i] != null) {
                Patient p = doctors[i];
                p.remainingExecTime--;

                if (p.remainingExecTime <= 0) {
                    doctors[i] = null; // 퇴원
                    if (p.criticality.equals("HI")) {
                        if (cur_time <= p.absoluteDeadline) {
                            hiLived++;
                            rs_ml.add(p.getRS(0));
                        } else {
                            hiDied++;
                            rs_ml.add(p.getRS(1));
                        }
                    } else {
                        // LO 완료
                        loProcessed++;

                        int turnaroundTime = cur_time+1 - p.arrivalTime;
                        int waitTime = turnaroundTime - p.originalExecTime;
                        loWaitTimes.add(waitTime);
                        rs_ml.add(p.getRS(0)+" "+waitTime);
                        if(waitTime==-1) {
                        	SLog.prn(cur_time+" "+p.arrivalTime+" "+p.originalExecTime);
                        }
                    }
                }
            }
        }
		
	}




	private double cal_load() {
        // ---------------------------------------
        // 2. Load 계산
        // ---------------------------------------
        double currentLoad = 0.0;
        // 대기열 부하
        for (Patient p : waitingQueue) {
            currentLoad += (double) p.executionTime / p.goldenTime;
        }
        // 의사 슬롯 부하
        for (Patient p : doctors) {
            if (p != null) {
                currentLoad += (double) p.executionTime / p.goldenTime;
            }
        }
		return currentLoad;
	}




	private void handle_WQ() {
        // ---------------------------------------
        // 8. 대기열 정리 (Java 8 removeIf 사용)
        // ---------------------------------------
        final int now = cur_time;
        waitingQueue.removeIf(p -> {
            if (p.criticality.equals("HI") && now > p.absoluteDeadline) {
                // 람다 내부에서는 외부 지역 변수 수정이 까다로우므로 
                // 여기서 카운트를 직접 올리기보다 리턴값으로 처리해야 하나,
                // 간단한 시뮬레이션을 위해 카운팅 로직은 별도 루프나 atomic을 써야 함.
                // 여기서는 removeIf 대신 Iterator 방식을 사용해 정확히 카운팅함.
                return false; 
            }
            return false;
        });
        
        // Iterator를 사용한 안전한 삭제 및 카운팅
        Iterator<Patient> it = waitingQueue.iterator();
        while (it.hasNext()) {
            Patient p = it.next();
            if (p.criticality.equals("HI") && cur_time > p.absoluteDeadline) {
                hiDied++;
                rs_ml.add(p.getRS(1));
                it.remove();
            } else if (p.criticality.equals("LO")) {
                int dropTime = p.arrivalTime + (p.goldenTime * 2);
                if (cur_time > dropTime) {
                    loDropped++;
                    rs_ml.add(p.getRS(1));
                    it.remove();
                }
            }
        }
		
	}




	private void results() {

        // ==========================================
        // 결과 보고
        // ==========================================
        System.out.println("\n" + "=".repeat(45));
        System.out.printf("   [Simulation Result: %d mins]\n", SIMULATION_TIME);
        System.out.println("=".repeat(45));

        // HI 통계
        int hiTotal = hiLived + hiDied;
        double hiSurvivalRate = (hiTotal > 0) ? ((double) hiLived / hiTotal * 100) : 0.0;

        System.out.printf("🚨 [HI: Critical] (Total: %d명)\n", hiTotal);
        System.out.printf("   - 생존율      : %.1f%%\n", hiSurvivalRate);
        System.out.printf("   - 즉시폐기    : %d명 (가망없음)\n", triageDropCount);

        System.out.println("-".repeat(45));

        // LO 통계
        int loTotal = loProcessed + loDropped;
        double loRejectionRate = (loTotal > 0) ? ((double) loDropped / loTotal * 100) : 0.0;
        
        double avgLoWait = 0.0;
        if (!loWaitTimes.isEmpty()) {
            double sum = 0;
            for (int w : loWaitTimes) sum += w;
            avgLoWait = sum / loWaitTimes.size();
        }

        System.out.printf("🩹 [LO: Non-Critical] (Total: %d명)\n", loTotal);
        System.out.printf("   - 처리 완료   : %d명\n", loProcessed);
        System.out.printf("   - 거부/포기   : %d명\n", loDropped);
        System.out.printf("   👉 거부율(Drop Rate) : %.1f%%\n", loRejectionRate);
        System.out.printf("   👉 평균 대기시간     : %.1f분\n", avgLoWait);

        System.out.println("-".repeat(45));
        System.out.println("⚡ System Stats");
        System.out.printf("   - Preemption(선점)   : %d회\n", preemptionCount);
        System.out.printf("   - Burst(폭주)        : %d회\n", burstCount);
        System.out.println("=".repeat(45));
		
		
	}

	public static void main(String[] args) {
		SLog.set_lv(0);
    	EmergencyRoom3 er=new EmergencyRoom3();
    	
    	er.run();

	}
}
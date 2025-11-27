package patient;
// Removed Results ... save result in txt and process in python 


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
//	final String g_fn="patient/test.txt";
	
//	final String g_fn="patient/trace_hic.txt";
//    final int NUM_DOCTORS = 10;
    
//	final String g_fn="patient/trace_mic.txt";
//    final int NUM_DOCTORS = 6;

//	final String g_fn="patient/trace_lic.txt";
//    final int NUM_DOCTORS = 4;

	final String g_fn="israel/test.txt";
    final int NUM_DOCTORS = 70;

//    final int SIMULATION_TIME = 1440; // 1일 (분 단위)
    final int SIMULATION_TIME = 3*1440; // 30일 (분 단위)

    final double HIGH_THRESHOLD = 0.8;
    final double LOW_THRESHOLD = 0.4;

    final int SWITCH_COST = 5;
    final int MULTIPLY_WAIT = 3;
    final double thresholdEnter = NUM_DOCTORS * HIGH_THRESHOLD;
    final double thresholdExit = NUM_DOCTORS * LOW_THRESHOLD;
    final double th_doc = NUM_DOCTORS * 1/10;
    final double th_doc_exit = NUM_DOCTORS * 1.5/10;


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



		MList ml=MList.load(g_fn);
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
	}




	private void preemption() {
        // ---------------------------------------
        // 5. Preemption (선점)
        // ---------------------------------------
        if (isEmergencyMode && !waitingQueue.isEmpty()) {
//        if ( !waitingQueue.isEmpty()) {
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
                    }
                }
            }
        }
		
	}




	private void mode_switch(double cur_load) {
        // ---------------------------------------
        // 3. 모드 전환
        // ---------------------------------------
        int freedoc=0;
        for(int i=0;i<NUM_DOCTORS;i++) {
        	if(doctors[i]==null)
        		freedoc++;
        }
        if (!isEmergencyMode) {
            if (cur_load >= thresholdEnter) {
                isEmergencyMode = true;
            }
            if(freedoc<th_doc) {
            	SLog.prn("emer:"+freedoc);
                isEmergencyMode = true;
            }

        } else {
            if (cur_load <= thresholdExit) {
                isEmergencyMode = false;
            }
            if(freedoc>th_doc_exit) {
            	SLog.prn("emer exit:"+freedoc);
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
//        SLog.prn(currentTime+"");
        while(true) {
        	String s=getPatient(ml);
        	if(s==null) 
        		break;
        	Patient newPatient = new Patient(s);
//        	newPatient.prn();

       		waitingQueue.add(newPatient);
        	
        	// Admission Control (Emergency 모드일 때 LO 거부)
//        	if (isEmergencyMode && newPatient.criticality.equals("LO")) {
//        		rs_ml.add(newPatient.getRS(2,0));
//        	} else {
//        		waitingQueue.add(newPatient);
//        	}
       	 
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
                        if (candidate.criticality.equals("HI")) {
                        	SLog.prn(cur_time+","+candidate.originalExecTime+","+candidate.remainingExecTime+", "+candidate.absoluteDeadline);
                            waitingQueue.remove(0); // 실제 제거
                            rs_ml.add(candidate.getRS(3,0));
                            continue; // 다음 환자 확인
                        } 
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
                            rs_ml.add(p.getRS(0,0));
                        } else {
                            rs_ml.add(p.getRS(1,0));
                        }
                    } else {
                        // LO 완료

                        int turnaroundTime = cur_time+1 - p.arrivalTime;
                        int waitTime = turnaroundTime - p.originalExecTime;
                        rs_ml.add(p.getRS(0,waitTime));
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
                rs_ml.add(p.getRS(1,0));
                it.remove();
            } else if (p.criticality.equals("LO")) {
                int dropTime = p.arrivalTime + (p.goldenTime * MULTIPLY_WAIT);
                if (cur_time > dropTime) {
                	SLog.prn(cur_time+" <"+ dropTime);
                    rs_ml.add(p.getRS(1,0));
                    it.remove();
                }
            }
        }
		
	}




	public static void main(String[] args) {
		SLog.set_lv(0);
    	EmergencyRoom3 er=new EmergencyRoom3();
    	
    	er.run();

	}
}
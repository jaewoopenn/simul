package patient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import util.MList;
import util.SLog;

public class EmergencyRoom2 {

    // ==========================================
    // 1. 설정 (Configuration)
    // ==========================================
    final int SIMULATION_TIME = 1440; // 1일 (분 단위)
    final double ARRIVAL_RATE = 0.2; // 람다(lambda)


    private int patientIdCounter = 1;

    // ==========================================
    // 2. 시뮬레이션 실행 메인 로직
    // ==========================================

	public void run() {

		int currentTime=0;


        System.out.println("Simulation Started...");
        MList ml=MList.new_list();
        while (currentTime < SIMULATION_TIME) {
        	genPatient(ml,currentTime);
            
            currentTime++;
        }
        ml.saveTo("patient/test.txt");
        System.out.println("Simulation End");
	}








	private void genPatient(MList ml, int currentTime) {
        // ---------------------------------------
        // 1. 환자 발생
        // ---------------------------------------
        int numArrivals = PUtil.getPoissonArrivalCount(ARRIVAL_RATE);
        for (int i = 0; i < numArrivals; i++) {
            Patient newPatient = new Patient(patientIdCounter++, currentTime);
            String s=newPatient.getStr();
            ml.add(s);
//            SLog.prn(s);
        }
		
	}



}
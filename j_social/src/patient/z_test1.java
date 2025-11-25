package patient;

import util.SLog;

public class z_test1 {

	public static void main(String[] args) {
		SLog.set_lv(0);
//    	EmergencyRoom er=new EmergencyRoom();
//    	EmergencyRoom2 er=new EmergencyRoom2();  
    	EmergencyRoom3 er=new EmergencyRoom3();
    	
    	er.run();

	}

}

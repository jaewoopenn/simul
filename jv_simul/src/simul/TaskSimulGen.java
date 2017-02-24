package simul;



public class TaskSimulGen {
	public static TaskSimul get(int i){
		if(i==1){
			return new TaskSimul_EDF_VD(null);
		} else if (i==2) {
			return new TaskSimul_EDF_AD_E(null);
		} else {
			return null;
		}
	}
	public static TaskSimul_MP get_MP(int i){
		if(i==1){
			return new TaskSimul_MP_mig(null);
		} else if (i==2) {
			return new TaskSimul_MP_nomig(null);
		} else {
			return null;
		}
		
	}
}

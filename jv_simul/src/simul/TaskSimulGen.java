package simul;



public class TaskSimulGen {
	public static TaskSimul get(int i){
		if(i==1){
			return new TaskSimul_EDF_VD();
		} else if (i==2) {
			return new TaskSimul_EDF_AD_E();
		}
		return null;
	}
}

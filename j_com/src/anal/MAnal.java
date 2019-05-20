package anal;


import task.Task;

public class MAnal {
	public static double computeRBF(Task[] tm,int i, int t) {
		double r=0;
		for(int j=0;j<=i;j++) {
			if(j==i) {
				r+=tm[j].exec;
			} else {
				r+=Math.ceil(t*1.0/tm[j].period)*tm[j].exec;
				
			}
		}
		
		return r;
	}

}
	

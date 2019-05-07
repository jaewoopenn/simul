package Anal;

import basic.Task;

public class AUtil {
	public static int computeRBF(Task[] ts,int i, int t) {
		int r=0;
		for(int j=0;j<i;j++) {
			if(j==i-1) {
				r+=ts[j].exec;
			} else {
				r+=Math.floor(t*1.0/ts[j].period)*ts[j].exec;
				
			}
		}
		
		return r;
	}
}

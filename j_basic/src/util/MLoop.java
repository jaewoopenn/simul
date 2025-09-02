package util;

public class MLoop {
	public static int idx=0;
	public static int[] on(int size){
		return on(0,size);
	}
	public static int[] on(int st,int et){
		int[] loop=new int[et-st];
		for(int i=st;i<et;i++){
			loop[i-st]=i;
		}
		return loop;
	}
	public static void reset() {
		idx=0;
	}
	public static boolean until(int i) {
		idx++;
		return idx<=i;
	}

}

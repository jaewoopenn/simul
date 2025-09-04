package util;

public class MLoop {
	public int idx=0;
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
	public boolean until(int d) {
		idx++;
		return idx<=d;
	}
	public static MLoop init() {
		return new MLoop();
	}

}

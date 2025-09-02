package util;

public class MLoop {
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

}

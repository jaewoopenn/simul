package util;

public class MLoop {
	public static int[] run(int size){
		return loop(0,size);
	}
	public static int[] loop(int st,int et){
		int[] loop=new int[et-st];
		for(int i=st;i<et;i++){
			loop[i-st]=i;
		}
		return loop;
	}

}

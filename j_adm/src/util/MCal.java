package util;


public class MCal {
	public static double err=0.00000000000001;
	public static int btoi(boolean b){
		if(b) return 1;
		return 0;
	}
	public static int combi(int n,int r){
		return facto(n)/(facto(r)*facto(n-r));
	}
	public static int facto(int n){
		int rs=1;
		for (int i=2;i<=n;i++){
			rs*=i;
		}
		return rs;
	}
	public static String getStr(double d){
		return String.format("%.4f", d);
	}
	public static int[] loop(int size){
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

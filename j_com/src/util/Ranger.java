package util;

public class Ranger {
	int kind=-1;
	private int l_int;
	private int u_int;
	private double l_d;
	private double u_d;
	public Ranger(int l, int u) {
		if(l>u) {
			S_Log.err("ranger: l <= u");
		}
		l_int=l;
		u_int=u;
		kind=0;
	}
	public Ranger(double l,double u) {
		if(l>u) {
			S_Log.err("ranger: l <= u");
		}
		l_d=l;
		u_d=u;
		kind=1;
	}
	public int getIntL() {
		if(kind==0)
			return l_int;
		S_Log.err("getDblL");
		return 0;
	}
	public int getIntU() {
		if(kind==0)
			return u_int;
		S_Log.err("getDblU");
		return 0;
	}
	public double getDblL() {
		if(kind==1)
			return l_d;
		S_Log.err("getIntL");
		return 0;
	}
	public double getDblU() {
		if(kind==1)
			return u_d;
		S_Log.err("getIntU");
		return 0;
	}
	public static Ranger gen(double l, double u) {
		return new Ranger(l,u);
	}
	public static Ranger gen(int l, int u) {
		return new Ranger(l,u);
	}
}

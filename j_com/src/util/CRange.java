package util;

public class CRange {
	int kind=-1;
	private int l_int;
	private int u_int;
	private double l_d;
	private double u_d;
	public CRange(int l, int u) {
		if(l>u) {
			SLog.err("ranger: l <= u");
		}
		l_int=l;
		u_int=u;
		kind=0;
	}
	public CRange(double l,double u) {
		if(l>u) {
			SLog.err("ranger: l <= u");
		}
		l_d=l;
		u_d=u;
		kind=1;
	}
	public int getIntL() {
		if(kind==0)
			return l_int;
		SLog.err("getDblL");
		return 0;
	}
	public int getIntU() {
		if(kind==0)
			return u_int;
		SLog.err("getDblU");
		return 0;
	}
	public double getDblL() {
		if(kind==1)
			return l_d;
		SLog.err("getIntL");
		return 0;
	}
	public double getDblU() {
		if(kind==1)
			return u_d;
		SLog.err("getIntU");
		return 0;
	}
	public static CRange gen(double l, double u) {
		return new CRange(l,u);
	}
	public static CRange gen(int l, int u) {
		return new CRange(l,u);
	}
}

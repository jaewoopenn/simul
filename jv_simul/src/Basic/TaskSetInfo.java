package Basic;

public class TaskSetInfo {
	private double x=1;
	private boolean isAdd;
	private int hi_size;
	private int lo_size;
	private double lo_util;
	private double hi_util_lm;
	private double hi_util_hm;
	public boolean isAdd() {
		return isAdd;
	}
	public void setAdd(boolean isAdd) {
		this.isAdd = isAdd;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public int getSize() {
		return lo_size+hi_size;
	}
	public int getHi_size() {
		return hi_size;
	}
	public int getLo_size() {
		return lo_size;
	}
	public double getUtil() {
		return lo_util+hi_util_hm;
	}
	public double getMCUtil() {
		return 	Math.max(lo_util+hi_util_lm, hi_util_hm);	
	}
	public double getLo_util() {
		return lo_util;
	}
	public double getHi_util_lm() {
		return hi_util_lm;
	}
	public double getHi_util_hm() {
		return hi_util_hm;
	}
	public void setHi_size(int hi_size) {
		this.hi_size = hi_size;
	}
	public void setLo_size(int lo_size) {
		this.lo_size = lo_size;
	}
	public void setLo_util(double lo_util) {
		this.lo_util = lo_util;
	}
	public void setHi_util_lm(double hi_util_lm) {
		this.hi_util_lm = hi_util_lm;
	}
	public void setHi_util_hm(double hi_util_hm) {
		this.hi_util_hm = hi_util_hm;
	}
}

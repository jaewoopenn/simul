package Basic;

import Util.Log;

public class Comp {
	private int cid;
	private double g_lt_lu;
	private double g_ht_lu;
	private double g_ht_hu;

	public Comp(int cid,double lt_lu, double ht_lu,double ht_hu) {
		this.cid=cid;
		this.g_lt_lu = lt_lu;
		this.g_ht_lu = ht_lu;
		this.g_ht_hu = ht_hu;
	}

	public void prn(int lv) {
		Log.prnc(lv,"cid:"+cid);
		Log.prnc(lv," lt_lu:");
		Log.prnDbl(lv,  g_lt_lu);
		Log.prnc(lv," ht_lu:");
		Log.prnDbl(lv,  g_ht_lu);
		Log.prnc(lv," ht_hu:");
		Log.prnDbl(lv,  g_ht_hu);
		Log.prn_nl(lv);
	}
	
	public double getCompUtil() {
		double lu=g_lt_lu+g_ht_lu;
		double hu=g_ht_hu;
		return Math.max(lu,hu);
	}

	public double get_lt_lu() {
		return g_lt_lu;
	}

	public double get_ht_lu() {
		return g_ht_lu;
	}

	public double get_ht_hu() {
		return g_ht_hu;
	}

	public int get_id() {
		return cid;
	}

	public double get_lu() {
		return g_lt_lu+g_ht_lu;
	}
}


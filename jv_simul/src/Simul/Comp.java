package Simul;

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
		Log.prnc(lv," lt_lu:"+g_lt_lu);
		Log.prnc(lv," ht_lu:"+g_ht_lu);
		Log.prnc(lv," ht_hu:"+g_ht_hu);
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
}


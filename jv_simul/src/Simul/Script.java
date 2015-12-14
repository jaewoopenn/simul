package Simul;

import java.util.Vector;

import Util.FUtil;

public class Script {
	Vector<String> g_scr;
	public Script(){
		g_scr=new Vector<String>();
	}
	public void load(String fn) {
		FUtil fu=new FUtil(fn);
		fu.load();
		fu.prn();
	}
	public void test() {
		FUtil fu=new FUtil("scr/test.txt");
		fu.print("load ad.c");
		fu.print("run 100");
		fu.save();
	}


}

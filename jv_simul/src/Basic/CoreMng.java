package Basic;

import java.util.Vector;

import Simul.CompMng;

public class CoreMng {
	private Vector<CompMng> g_comp;
	public CoreMng() {
		g_comp=new Vector<CompMng>();
	}
	

	public void addCPU(CompMng tm) {
		g_comp.addElement(tm);
	}
	public CompMng getCPU(int i) {
		return g_comp.elementAt(i);
	}
	


	public int getSize() {
		return g_comp.size();
	}







}

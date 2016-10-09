package processor;

import java.util.Vector;

import comp.OldCompMng;

public class CoreMng {
	private Vector<OldCompMng> g_comp;
	public CoreMng() {
		g_comp=new Vector<OldCompMng>();
	}
	

	public void addCPU(OldCompMng tm) {
		g_comp.addElement(tm);
	}
	public OldCompMng getCPU(int i) {
		return g_comp.elementAt(i);
	}
	


	public int getSize() {
		return g_comp.size();
	}
}

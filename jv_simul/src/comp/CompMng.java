package comp;

import java.util.Vector;

import basic.TaskMng;
import utilSim.Log;

public class CompMng {
	private Vector<Comp> g_comp;
	public CompMng() {
		g_comp=new Vector<Comp>();
	}

	
	
	
	public void addComp(Comp c) {
		int cid=g_comp.size();
		c.cid=cid;
		g_comp.add(c);		
	}
	public Comp getComp(int i) {
		return g_comp.elementAt(i);
	}


	public int getSize() {
		return g_comp.size();
	}


	public void prn(){
		Log.prn(2, "tot:"+g_comp.size());
		for(Comp c:g_comp){
			c.prn();
		}
	}
	




}

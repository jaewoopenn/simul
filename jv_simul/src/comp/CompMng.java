package comp;

import java.util.Vector;

import basic.Task;
import basic.TaskMng;
import basic.TaskMngPre;
import utilSim.RUtil;
import utilSim.Log;

public class CompMng {
	private Vector<Comp> g_comp;
	private TaskMng g_tm;
	public CompMng() {
		g_comp=new Vector<Comp>();
	}

	
	
	
	public void addComp(Comp c) {
		int cid=g_comp.size();
		c.setID(cid);
		g_comp.add(c);
		g_tm=null;
	}

	public void makeTM(){
		TaskMngPre tmp=new TaskMngPre();
		for(Comp c:g_comp){
			Task[] tasks=c.getTasks();
			for(Task t:tasks){
				t.setComp(c.getID());
				tmp.add(t);
			}
		}
		g_tm=tmp.freezeTasks();
		
	}



	public void analMaxRes() {
		double u=0;
		for(Comp c:g_comp){
			double res=c.getWC_U();
			u+=res;
			c.setMaxRes(res);
//			Log.prn(1, "cur:"+u);
			
		}
		Log.prn(1, "rem:"+u);
		
	}








	public void part() {
		for(Comp c:g_comp){
			c.partition();
		}
	}


	
	// prn
	
	public void prn(){
		Log.prn(2, "tot:"+g_comp.size());
		for(Comp c:g_comp){
			c.prn();
		}
	}
	public void prnOff(){
		Log.prn(2, "tot:"+g_comp.size());
		
		for(Comp c:g_comp){
			c.prnOff();
		}
	}
	// set
	
	public void setX(double x) {
		for(Comp c:g_comp){
			c.getTM().setX(x);
		}
		
	}
	public void setAlpha(double g_a_l, double g_a_u) {
		RUtil ru=new RUtil();
		for(Comp c:g_comp){
			c.setAlpha(ru.getDbl(g_a_l,g_a_u));
		}
	}
	// get

	public double getRU() {
		double u=0;
		for(Comp c:g_comp){
			u+=c.getRU();
		}
		return u;
	}
	
	public TaskMng getTM() {
		if(g_tm==null)
			makeTM();
		return g_tm;
	}
	public Comp getComp(int i) {
		return g_comp.elementAt(i);
	}


	public int getSize() {
		return g_comp.size();
	}


	public double getMCUtil() {
		TaskMng tm=getTM();
		return tm.getMCUtil();
	}



	public Vector<Comp> getComps() {
		return g_comp;
	}
















}

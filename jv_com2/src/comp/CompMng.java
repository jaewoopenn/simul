package comp;

import java.util.Vector;

import task.Task;
import task.TaskMng;
import task.TaskSet;
import task.TaskSetMC;
import task.TaskVec;
import util.SLog;
import util.MRand;

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
		TaskVec tmp=new TaskVec();
		for(Comp c:g_comp){
			for(Task t:c.getTaskSet()){
				t.setComp(c.getID());
				tmp.add(t);
			}
		}
		TaskSetMC tme=new TaskSetMC(new TaskSet(tmp));
		g_tm=tme.getTM();
		
	}



	public void analMaxRes() {
		double u=0;
		for(Comp c:g_comp){
			double res=c.getWC_U();
			u+=res;
			c.setMaxRes(res);
//			Log.prn(1, "cur:"+u);
			
		}
		SLog.prn(1, "rem:"+u);
		
	}








	public void part() {
		for(Comp c:g_comp){
			c.partition();
		}
	}


	
	// prn
	
	public void prn(){
		SLog.prn(2, "tot:"+g_comp.size());
		for(Comp c:g_comp){
			c.prn();
		}
	}
	public void prnOff(){
		SLog.prn(2, "tot:"+g_comp.size());
		
		for(Comp c:g_comp){
			c.prnOff();
		}
	}
	// set
	
	public void setAlpha(double g_a_l, double g_a_u) {
		MRand ru=new MRand();
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
		return tm.getInfo().getCritUtil();
	}



	public Vector<Comp> getComps() {
		return g_comp;
	}
















}

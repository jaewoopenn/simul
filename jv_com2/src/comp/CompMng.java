package comp;

import java.util.Vector;

import task.Task;
import task.TaskMng;
import task.TaskVec;
import util.SLog;
import util.MRand;

public class CompMng {
	private Vector<Comp> g_comp;
	private TaskMng g_tm;
	public CompMng() {
		g_comp=new Vector<Comp>();
	}

	public void setX(double x) {
		for(Comp c:g_comp){
			c.getTM().setX(x);
		}
	}
	public void setX2(double x) {
		for(Comp c:g_comp){
			c.getTM().setX2(x);
		}
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
		g_tm=tmp.getTM();
		
	}



	public double analMaxRes() {
//		SLog.prn(1, "anal");
		double u=0;
		for(Comp c:g_comp){
			double res=c.getWC_U();
			u+=res;
			c.setMaxRes(res);
//			Log.prn(1, "cur:"+u);
			
		}
		SLog.prn(1, "fin:"+u);
		return u;
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
	
	public void setAlpha(double al, double au) {
		MRand ru=new MRand();
		for(Comp c:g_comp){
			c.setAlpha(ru.getDbl(al,au));
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
	public double getVU() {
		double u=0;
		for(Comp c:g_comp){
			u+=c.getVU();
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
	public double getMaxUtil() {
		TaskMng tm=getTM();
		return tm.getInfo().getMaxUtil();
	}



	public Vector<Comp> getComps() {
		return g_comp;
	}

	public double getLoUtil() {
		TaskMng tm=getTM();
		return tm.getLoUtil();
	}

	public double getHcUtil() {
		TaskMng tm=getTM();
		return tm.getRUtil();
	}
















}

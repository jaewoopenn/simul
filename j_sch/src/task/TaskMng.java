package task;
/*
 * hi task set & lo task set 

*/

import util.MCal;
import util.SLog;

public class TaskMng {
	private TaskSet g_tasks;
	private TaskSet g_hi_tasks;
	private TaskSet g_lo_tasks;
	private SysInfo g_info;

	public TaskMng(Task[] ts,TaskSet hi_tasks,TaskSet lo_tasks,SysInfo info) {
		this.g_tasks=new TaskSet(ts);
		this.g_hi_tasks = hi_tasks;
		this.g_lo_tasks = lo_tasks;
		this.g_info = info;
		g_lo_tasks.sortLo();
	}
	public void sortMinJobDrop() {
		g_lo_tasks.sortLo2();
	}
	
	public Task findDropTask() {
		for(Task t:g_lo_tasks.getArr()){
			if (!t.isDrop())
				return t;
		}
		return null;
	}

	public Task findResumeTask() {
		Task [] ts=g_lo_tasks.getArr();
		for(int i=ts.length-1;i>=0;i--){ // reverse order (from lowest util lo task)
			Task t=ts[i];
			if (t.isDrop()) // if task is dropped, pick this
				return t;
		}
		return null;
	}

	
	// set
	
	public void setX(double x){
		g_info.setX(x);
		g_hi_tasks.setX(x);
	}

	

	// get
	
	public SysInfo getInfo() {
		return g_info;
	}

	public Task[] getTasks() {
		return g_tasks.getArr();
	}
	public Task[] getHiTasks(){
		return g_hi_tasks.getArr();
	}
	public Task[] getLoTasks(){
		return g_lo_tasks.getArr();
	}


	public Task getTask(int i) {

		return g_tasks.get(i);
	}
	
	public double getRUtilFMC() {
		double util=0;
		for(Task t:g_tasks.getArr())	{
			if(!t.isHC())  
				continue;
			if(t.isHM()) {
				double temp=t.getLoUtil()/g_info.getUtil_HC_LO()*(1-g_info.getUtil_LC());
				util+=(temp-t.getHiUtil())/(1-g_info.getX());
			}
		}
//		return util;
		double cur=0;
		for(Task t:g_tasks.getArr())	{
			if(t.isHC())  
				continue;
			if(!t.isDrop())
				cur+=t.getLoUtil();
		}
		return cur-g_info.getUtil_LC()-util;
	}

	
	public double getRUtil() {
		double util=0;
		for(Task t:g_tasks.getArr())	{
			util+=g_info.computeRU(t);
//			SLogF.prn("ru:"+util);			
		}
		return util;
	}
	public double getVUtil() {
		double util=0;
		for(Task t:g_tasks.getArr())	{
			util+=g_info.computeVU(t);
//			SLogF.prn("ru:"+util);			
		}
		return util;
		
	}
	
	public double getRUN_Util() {
		double util=0;
		for(Task t:g_tasks.getArr())	{
			util+=g_info.computeRUN_U(t);
		}
		return util;
	}
	public double getRUN5_Util() {
		double util=0;
		for(Task t:g_tasks.getArr())	{
			util+=g_info.computeRUN5_U(t);
		}
		return util;
	}

	public double getRUNE_Util(double x) {
		double util=0;
		for(Task t:g_tasks.getArr())	{
			util+=g_info.computeRUNE_U(t,x);
		}
		return util;
	}
	public double getRUN_Util2(double x) {
		double util1=getRUN_Util();
		double util2=getRUNE_Util(x);
//		if(util2<util1) {
//			SLog.prn(2,MCal.getStr(util2)+"<"+MCal.getStr(util1)+"!!");
//		}
		return Math.min(util1, util2);
	}
	
	public double getWCUtil() {
		double util=0;
		for(Task t:g_hi_tasks.getArr())	{
			util+=t.getHiUtil();
		}
		for(Task t:g_lo_tasks.getArr())	{
			util+=getDroppedUtil(t);
		}
		return util;
	}
	public double getLoUtil() {
		double util=0;
		for(Task t:g_lo_tasks.getArr())	{
			util+=t.getLoUtil();
		}
		return util;
	}

	public double getDeLoUtil() {
		double util=0;
		for(Task t:g_lo_tasks.getArr())	{
			util+=t.getHiUtil();
		}
		return util;
	}
	
	public double getMaxUtil() {
		
		return g_info.getMaxUtil();
	}


	
	public double getDroppedUtil(Task t){
//		Log.prn(2, g_info.getX()+" "+t.getLoUtil());
		return g_info.getX()*t.getLoUtil();
	}


	public int size() {
		return g_tasks.size();
	}














	public boolean check() {
		if(g_info.get_lm_util()>1){
			SLog.prn(2, "lm "+g_info.get_lm_util()+" error");
			return false;
		}
		if(g_info.get_hm_util()>1){
			SLog.prn(2, "hm "+g_info.get_hm_util()+" error");
			return false;
		}
		return true;
	}

	// prn 

	public void prnShort() {
		g_info.prnUtil();
	}

	public void prn() {
		g_tasks.prn();
//		g_info.prn();
	}
	
	public void prnHI() {
		g_hi_tasks.prn();
		SLog.prn(2, "hi_mode_util:"+g_info.getUtil_HC_HI());
		
	}

	public void prnLoTasks() {
		g_lo_tasks.prnRuntime();
	}
	public void prnInfo() {
		g_info.prn();
//		Log.prn(2, "prob:"+g_info.getProb_ms());
	}

	public void prnRuntime() {
		SLog.prn(2, "WC:"+getWCUtil());
		SLog.prn(2, "LO:"+getLoUtil());
		g_tasks.prnRuntime();
	}

	public void prnOffline() {
		SLog.prn(2, "x:"+g_info.getX());
		g_tasks.prnOffline();
		
	}

	public int getLongPeriod() {
		int l=0;
		for(Task t:g_hi_tasks.getArr()){
			if(t.period>l)
				l=t.period;
		}
		return l;
	}

	public int getShortPeriod() {
		int l=-1;
		for(Task t:g_hi_tasks.getArr()){
			if(l==-1)
				l=t.period;
			if(t.period<l)
				l=t.period;
		}
		return l;
	}

	public void init() {
		for(Task t:g_tasks.getArr()){
			t.initMode();
		}
	}
	public void prnTxt() {
		g_tasks.prnTxt();
		
	}
	public void prnPara() {
		g_tasks.prnPara();
		
	}
	public void prnRun(double x) {
		SLog.prn(2, "x:"+x);
		double util=0;
		for(Task t:g_tasks.getArr())	{
			double u=g_info.computeRUNE_U(t,x);
			util+=u;
			SLog.prn(2, u+","+util);
		}
	}
	public boolean isZeroLife() {
		int life=0;
		for(Task t:getHiTasks()) {
			if(t.isHM()) {
				life+=t.life;
			}
		}
//		SLog.prn(2, "l:"+life);
		if(life!=0) {
			return false;
		}
		return true;
	}

	

}

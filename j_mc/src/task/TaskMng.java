package task;
/*
 * hi task set & lo task set 

*/


import util.SLog;

public class TaskMng {
	private TaskSet g_tasks;
	private TaskSet g_hc_tasks;
	private TaskSet g_lc_tasks;
	private SysInfo g_info;

	public TaskMng(Task[] ts,TaskSet hi_tasks,TaskSet lo_tasks) {
		this.g_tasks=new TaskSet(ts);
		this.g_hc_tasks = hi_tasks;
		this.g_lc_tasks = lo_tasks;
		g_info=new SysInfo();
		updateInfo();
		g_lc_tasks.sortLo();
	}
	public void updateInfo() {
		double loutil=0;
		double loutil_de=0;
		double hiutil_lm=0;
		double hiutil_hm=0;
		for(Task t:g_tasks.getArr())
		{
			if(t.removed())
				continue;
			if(t.isHC()){
				hiutil_lm+=t.getLoUtil();
				hiutil_hm+=t.getHiUtil();
			} else {
				loutil+=t.getLoUtil();
				loutil_de+=t.getHiUtil();
			}
		}
		g_info.setLo_util(loutil);
		g_info.setLo_de_util(loutil_de);
		g_info.setUtil_HC_HI(hiutil_hm);
		g_info.setUtil_HC_LO(hiutil_lm);
		
	}


	
	// set
	




	public void setX(double x){
		g_info.setX(x);
		g_hc_tasks.setVD(x);
		g_hc_tasks.set_HI_only(x);
	}

	

	// get
	
	public SysInfo getInfo() {
		return g_info;
	}

	public Task[] getTasks() {
		return g_tasks.getArr();
	}
	public Task[] get_HC_Tasks(){
		return g_hc_tasks.getArr();
	}
	public Task[] get_LC_Tasks(){
		return g_lc_tasks.getArr();
	}


	public Task getTask(int tid) {
		for(Task t:g_tasks.getArr()) {
			if(t.tid==tid)
				return t;
		}
		return null;
	}
	
	public double getRUtilFMC() {
		double util=0;
		for(Task t:g_tasks.getArr())	{
			if(t.removed())
				continue;
			if(!t.isHC())  
				continue;
			if(t.isHM()) {
				double temp=t.getLoUtil()/g_info.getUtil_HC_LO()*(1-g_info.getUtil_LC_AC());
				util+=(temp-t.getHiUtil())/(1-g_info.getX());
			}
		}
//		return util;
		double cur=0;
		for(Task t:g_tasks.getArr())	{
			if(t.removed())
				continue;
			if(t.isHC())  
				continue;
			if(!t.isDrop())
				cur+=t.getLoUtil();
		}
		return cur-g_info.getUtil_LC_AC()-util;
	}

	
//	public double getRUtil() {
//		double util=0;
//		for(Task t:g_tasks.getArr())	{
//			if(t.removed())
//				continue;
//			util+=g_info.computeRU(t);
////			SLogF.prn("ru:"+util);			
//		}
//		return util;
//	}
	public double getVUtil() {
		double util=0;
		for(Task t:g_tasks.getArr())	{
			if(t.removed())
				continue;
			util+=g_info.computeVU(t);
//			SLogF.prn("ru:"+util);			
		}
		return util;
		
	}
	

	
	public double getWCUtil() {
		double util=0;
		for(Task t:g_hc_tasks.getArr())	{
			if(t.removed())
				continue;
			util+=t.getHiUtil();
		}
		for(Task t:g_lc_tasks.getArr())	{
			if(t.removed())
				continue;
			util+=getDroppedUtil(t);
		}
		return util;
	}
	public double getLoUtil() {
		double util=0;
		for(Task t:g_lc_tasks.getArr())	{
			if(t.removed())
				continue;
			util+=t.getLoUtil();
		}
		return util;
	}

	public double getDeLoUtil() {
		double util=0;
		for(Task t:g_lc_tasks.getArr())	{
			if(t.removed())
				continue;
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
		if(g_info.get_LO_util()>1){
			SLog.prn(2, "lm "+g_info.get_LO_util()+" error");
			return false;
		}
		if(g_info.get_HI_util()>1){
			SLog.prn(2, "hm "+g_info.get_HI_util()+" error");
			return false;
		}
		return true;
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

	public int getMaxPeriod() {
		int l=0;
		for(Task t:g_hc_tasks.getArr()){
			if(t.period>l)
				l=t.period;
		}
		return l;
	}

	public int getMinPeriod() {
		int l=-1;
		for(Task t:g_hc_tasks.getArr()){
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


	public double getX() {
		return g_info.getX();
	}
	public int getTaskNum() {
		return g_tasks.size();
	}

	

}

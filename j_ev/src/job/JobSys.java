package job;

//import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeMap;

import util.SLog;

public class JobSys {
	private JobSimul g_js;
	private JobMng g_jm;
	private int g_id=0;
	private int g_t=0;
	private int g_val=0;
	private TreeMap<Integer, Integer> g_dem;
//	private PriorityQueue<JobInput> g_set;
	public JobSys(){
		g_dem = new TreeMap<>();
		g_jm=new JobMng();
		g_js=new JobSimul(g_jm);
//		reset();
	}
	public int add_in(int dl, int e) {
		return add_in(dl,e,0, e);
		
	}
	public int add_in(int dl, int e, int o, int v) {
		int et=g_t+dl;
		int rem=gemRem(et);
		if(e>rem) {
			SLog.prn("No ("+dl+","+e+","+v+")");
			return rem;
		}
		Job j=new Job(g_id,et,e,o,v);
		g_val+=v;
		g_id++;
		g_jm.add(j);
		addDem(et,e+o);
		return 0;

	}
	public boolean add_repl(int dl, int e, int o, int v) {
		double d=(double)v/e;
//		SLog.prn("d:"+d);
		int r=-1;
		int old_r=0;
		while(r!=old_r) {
			old_r=r;
			r=gemRem(dl);
//			SLog.prn("r:"+r);
			if(r>=e) {
				add_in(dl,e,o,v);
				return true;
			}
			removeOpt(d,e-r);
//			SLog.prn("old_r:"+old_r);
		}
		SLog.prn("rejected "+dl+", "+e);
		return false;
		
	}
	public int gemRem(int et) {
        Set<Integer> keys = g_dem.keySet();
        if(keys.size()==0)
        	return et;
		int dem=0;
		Integer s=g_dem.get(et);
		if(s==null) {
	        for (Integer key : keys) {
	        	if(key<et)
	        		dem=g_dem.get(key);
	        }
		} else {
			dem=s;
		}
		int rem=et-dem;
//		SLog.prn("REM:"+rem);
        for (Integer key : keys) {
        	if(key>et) {
        		dem=g_dem.get(key);
        		rem=Math.min(rem, key-dem);
        	}
        }
//		SLog.prn("REM_after:"+rem);
		return rem;
	}
	private void addDem(int et, int e) {
        Set<Integer> keys = g_dem.keySet();
		int dem=0;
		Integer s=g_dem.get(et);
		if(s==null) {
	        for (Integer key : keys) {
	        	if(key<et)
	        		dem=g_dem.get(key);
	        }
		} else {
			dem=s;
		}
		g_dem.put(et, dem+e);
        for (Integer key : keys) {
        	if(key>et) {
        		dem=g_dem.get(key);
        		g_dem.put(key, dem+e);
        	}
        }
		
	}
	public void exec(int len) {
		int et=g_t+len;
		while(g_t<et) {
			g_js.simul_one(g_t);
			g_t++;
		}
	}
	public void dbf() {
        Set<Integer> keys = g_dem.keySet();
        SLog.prn("t\t: d");
        SLog.prn("---------------");

        for (Integer key : keys) {
        	SLog.prn(key+"\t: "+g_dem.get(key));
        }
        SLog.prn("---------------");
        SLog.prn("val: "+g_val);
		
	}
	public void prn_ok() {
		g_jm.prn();
		
	}
	private void modifyDBF(Job j,int need) {
        Set<Integer> keys = g_dem.keySet();
		int dem=0;
		Integer s=g_dem.get(j.dl);
		if(s==null) {
			SLog.prn("err: "+j.exec+", "+j.dl);
			return;
		} 
		dem=s;
		g_dem.put(j.dl, dem-need);
        for (Integer key : keys) {
        	if(key>j.dl) {
        		dem=g_dem.get(key);
        		g_dem.put(key, dem-need);
        	}
        }
		
	}
	public boolean removeOpt(double d,int need) {
		Job j=g_jm.pickDenBelow(d);
		if(j!=null) {
			String s=j.info();
			SLog.prn("pick job "+s);
			if(j.opt<need) {
				need=j.opt;
			}
			modifyDBF(j,need);
			g_val-=j.den*need;
			j.opt-=need;
			return true;
		} 
		return false;
		
	}
	public void prn_den() {
		g_jm.prn_den();
		
	}
	
//	public void reset() {
//		g_set=new PriorityQueue<JobInput>();
//		
//	}
	

//	public void prn_input() {
//		JobMisc.prn_ji(g_set);
//		
//	}
	
//	public void add(int dl, int e, int v) {
//		int et=g_t+dl;
//		JobInput j=new JobInput(et,e,v);
//		g_set.add(j);
//	}
	
//	public void addAll() {
//		for(JobInput j:g_set) {
//			add(j.dl,j.exec,j.val);
//		}
//		reset();
//	}
	
//	public void remove_in(int et, int e) {
//	boolean b=g_jm.remove(et,e);
//	if(b) {
//		remDem(et,e);
//		g_val-=1;
//	} else {
//		SLog.prn("error");
//	}
//}
	
}

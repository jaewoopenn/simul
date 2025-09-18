package job;

import java.util.PriorityQueue;
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
	private PriorityQueue<JobInput> g_set;
	public JobSys(){
		g_dem = new TreeMap<>();
//		g_dem.put(0, 0);
		g_jm=new JobMng();
		g_js=new JobSimul(g_jm);
		reset();
	}
	public void reset() {
		g_set=new PriorityQueue<JobInput>();
		
	}
	public void add(int dl, int e, int v) {
		int et=g_t+dl;
		JobInput j=new JobInput(et,e,v);
		g_set.add(j);
	}
	public int add_in(int dl, int e) {
		return add_in(dl,e,e);
		
	}
	public int add_in(int dl, int e, int v) {
		int et=g_t+dl;
		int rem=testDem(et);
		if(e>rem) {
			SLog.prn("No ("+dl+","+e+","+v+")");
			return rem;
		}
		Job j=new Job(g_id,et,e,v);
		g_val+=v;
		g_id++;
		g_jm.add(j);
		addDem(et,e);
		return 0;

	}
	public boolean add_repl(int dl, int e, int v) {
		double d=(double)v/e;
//		SLog.prn("d:"+d);
		int r=0;
		int old_r=-1;
		while(r!=old_r) {
			old_r=r;
			r=getRem(dl);
//			SLog.prn("r:"+d);
			if(r>=e) {
				add_in(dl,e,v);
				return true;
			}
			removeDen(d);
		}
		SLog.prn("rejected "+dl+", "+e);
		return false;
		
	}
	private int testDem(int et) {
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
	public void prn_in() {
		g_jm.prn();
		
	}
	public void prn() {
		JobMisc.prn_ji(g_set);
		
	}
	public void addAll() {
		for(JobInput j:g_set) {
			add(j.dl,j.exec,j.val);
		}
		reset();
	}
//	public void remove_in(int et, int e) {
//		boolean b=g_jm.remove(et,e);
//		if(b) {
//			remDem(et,e);
//			g_val-=1;
//		} else {
//			SLog.prn("error");
//		}
//	}
	private void remDem(Job j) {
        Set<Integer> keys = g_dem.keySet();
		int dem=0;
		Integer s=g_dem.get(j.dl);
		if(s==null) {
			SLog.prn("err: "+j.exec+", "+j.dl);
			return;
		} 
		dem=s;
		g_dem.put(j.dl, dem-j.exec);
        for (Integer key : keys) {
        	if(key>j.dl) {
        		dem=g_dem.get(key);
        		g_dem.put(key, dem-j.exec);
        	}
        }
		
	}
	public boolean removeDen(double d) {
		Job j=g_jm.removeDen(d);
		if(j!=null) {
			String s=j.info();
			SLog.prn("remove "+s);
			remDem(j);
			g_val-=j.val;
			return true;
		} 
		return false;
		
	}
	public int getRem(int t) {
		return testDem(t);
	}
	
	
}

package job;

import java.util.Set;
import java.util.TreeMap;

import util.SLog;

public class JobSys_DEM extends JobSys {
	private TreeMap<Integer, Integer> g_dem;
	private JobMng_DEM g_jm;
	public JobSys_DEM(){
		init();
	}
	@Override
	public void init_in() {
		g_dem=new TreeMap<>();
		g_jm=new JobMng_DEM();
		g_js=new JobSimul(g_jm);
//		reset();
		
	}
	//////////////////
	/// Simul related 

	@Override
	protected void reset() {
		g_dem = new TreeMap<>();
		g_dem_base=-1;
		
	}
	//////////////////
	/// job related 
	public int add_in2(int dl, int e) {
		return add_in2(dl,e,0, e);
		
	}
	private int add_in2(int dl, int e, int o, double v) {
		int et=g_t+dl;
		int rem=getRem(et);
		if(e>rem) {
			SLog.prn("No ("+dl+","+e+","+v+")");
			return rem;
		}
		Job j=new Job(g_id,et,e);
		j.prn();
		g_val+=v;
		g_id++;
		g_jm.add(j);
		addDem(et,e+o);
		return 0;

	}
	@Override
	protected boolean add_in(int dl, int e) {
		int et=g_t+dl;
//		SLog.prn("d:"+d);
		int r=-1; // et에서 받아들일수 있는공간... 
		int old_r=0;
		while(true) {
			old_r=r;
			r=getRem(et);
//			SLog.prn("r:"+r+","+old_r);
			if(r==old_r) break;
			if(e<=r) {
				add_in2(dl,e);
				return true;
			} 
//			SLog.prn("old_r:"+old_r);
		}
		SLog.prn("rejected e:"+e+",dl:"+dl);  
		return false;
		
	}
	
	//////////////////
	/// DBF related 

	public int getRem(int et) {
        Set<Integer> keys = g_dem.keySet();
        if(keys.size()==0)
        	return et-g_dem_base;
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
//		SLog.prn("REM_after:"+rem+","+g_dem_base);
		return rem-g_dem_base;
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
	
	//////////////////
	/// print related 

	public void prn_detail() {
        Set<Integer> keys = g_dem.keySet();
        SLog.prn("dem_base: "+g_dem_base);
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


}

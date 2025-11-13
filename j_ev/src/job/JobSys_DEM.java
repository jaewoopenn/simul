package job;

import java.util.Set;
import java.util.TreeMap;

import util.SLog;

public class JobSys_DEM extends JobSys {
	private TreeMap<Integer, Integer> g_dem;
	private JobMng_DEM g_jm;
	public JobSys_DEM(){
		g_dem=new TreeMap<>();
		g_jm=new JobMng_DEM();
		g_js=new JobSimul(g_jm);
//		reset();
	}
	//////////////////
	/// Simul related 
	public void exec(int len) {
		int et=g_t+len;
		while(g_t<et) {
			int rs=g_js.simul_one(g_t);
			if(rs==0) {
				g_dem = new TreeMap<>();
				g_dem_base=-1;
			}
			g_t++;
		}
	}

	
	//////////////////
	/// job related 
	public int add_in(int dl, int e) {
		return add_in(dl,e,0, e);
		
	}
	private int add_in(int dl, int e, int o, int v) {
		int et=g_t+dl;
		int rem=gemRem(et);
		if(e>rem) {
			SLog.prn("No ("+dl+","+e+","+v+")");
			return rem;
		}
		Job j=new Job(g_id,et,e,o,v);
		j.prn();
		g_val+=v;
		g_id++;
		g_jm.add(j);
		addDem(et,e+o);
		return 0;

	}
	public boolean add(int dl, int e, int o, int v) {
        if(g_dem_base==-1) {
        	g_dem_base=g_t;
        }
		double d=(double)v/e;
		int et=g_t+dl;
//		SLog.prn("d:"+d);
		int r=-1; // et에서 받아들일수 있는공간... 
		int old_r=0;
		while(r!=old_r) {
			old_r=r;
			r=gemRem(et);
//			SLog.prn("r:"+r);
			if(e+o<=r) {
				add_in(dl,e,o,v);
				return true;
			} else if(e<=r) {
				int new_o=r-e;
				SLog.prn("opt mod:"+o+"-->"+new_o);
				add_in(dl,e,new_o,v);
				return true;
			}
			int opt=getOpt(et);
			removeOpt(d,e-r);
//			SLog.prn("old_r:"+old_r);
		}
		SLog.prn("rejected e:"+e+",dl:"+dl); // 원복해야 하는데.... 
		return false;
		
	}
	
	//////////////////
	/// DBF related 
	
	private int getOpt(int et) {
		return 0;
	}
	public int gemRem(int et) {
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
			SLog.prn("remove_opt: pick job "+s);
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
	
	//////////////////
	/// print related 

	public void prn_dbf() {
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
	public void prn_den() {
		g_jm.prn_den();
		
	}

	public void prn_ok() {
		g_jm.prn();
		
	}

}

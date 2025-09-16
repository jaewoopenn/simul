package job;

import java.util.Set;
import java.util.TreeMap;

import util.SLog;

public class JobSys {
	private JobSimul g_js;
	private int g_id=0;
	private int g_t=0;
	private TreeMap<Integer, Integer> g_dem;
	public JobSys(){
		g_dem = new TreeMap<>();
//		g_dem.put(0, 0);
		g_js=new JobSimul();
	}
	public void add(int dl, int e) {
		int et=g_t+dl;
		int rem=testDem(et);
		if(e>rem) {
			SLog.prn("No");
			return;
		}
		Job j=new Job(g_id,et,e);
		g_id++;
		g_js.add(j);
		addDem(et,e);

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
		SLog.prn("REM:"+rem);
        for (Integer key : keys) {
        	if(key>et) {
        		dem=g_dem.get(key);
        		rem=Math.min(rem, key-dem);
        	}
        }
		SLog.prn("REM_after:"+rem);
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
		int t=0;
		while(t<len) {
			g_js.simul_one();
			t++;
		}
	}
	public void dbf() {
        Set<Integer> keys = g_dem.keySet();

        for (Integer key : keys) {
        	SLog.prn(key+": "+g_dem.get(key));
        }
		
	}
	
	
}

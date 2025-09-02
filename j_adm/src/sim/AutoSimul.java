package sim;

import gen.SysLoad;
import task.DTaskVec;
import util.MProg;
import util.MList;
import util.SLog;

public class AutoSimul {
	private String g_path;
	private String g_rs_path;
	private DoSimul g_ds;
	private int g_sort;
	private boolean g_verbose=false;
	
	public AutoSimul(String path,DoSimul ds) {
		g_path=path;
		g_ds=ds;
		g_sort=ds.getSort();
	}	
	public void setRS(String rs_path) {
		g_rs_path=rs_path;
		
	}


	
	// simulate task set list with algorithm choice
	public String simulList(String ts_list) {
		SLog.prn(2, "Anal:"+g_sort);

		MList load_ts=MList.load(g_path+"/"+ts_list);
		
		
		MList rs_list=MList.new_list();
		for(int i=0;i<load_ts.size();i++) {
			String fn=load_ts.get(i);
			String out=g_rs_path+"/"+fn+".sim."+g_sort;
			simulTS(g_path+"/"+fn,out);
			rs_list.add(out);
		}		
		String rs_fn=g_rs_path+"/a_sim_list."+g_sort+".txt";
		rs_list.saveTo(rs_fn);
		return rs_fn;		
	}
	
	public void simulTS(String tsn, String out) {
		SLog.prn(2, "ts:"+tsn);
		SLog.prn(2, "out:"+out);
//		SLog.prn(2, g_dur);
//		SLog.prn(2, s.getName());
		
		SysLoad sy=new SysLoad(tsn);
		String ret=sy.open();
		int num=Integer.valueOf(ret).intValue();
		
		MList rs_list=MList.new_list();
		MProg prog=getProg(num);

		for(int i=0;i<num;i++) {
			prog.inc();
			DTaskVec dt=sy.loadOne2();
//			SLog.prnc(2, i+": ");
			g_ds.run(dt);
			rs_list.add(g_ds.getRS());
		}
		rs_list.saveTo(out);
		
	}




	
	
	
	
	
	private MProg getProg(int num) {
		MProg prog=new MProg(num);
		prog.setLog(2);
		
		if(g_verbose) {
			prog.setVerbose(1);
		} else { 
			prog.setPercent();
		}
		return prog;
	}

}

package anal;

import gen.SysLoad;
import task.TaskVec;
import util.MList;
import util.SLog;

public class AutoAnal {
	private String g_path;
	private DoAnal g_da;
	private int g_sort;
	private boolean isSimul=false;
	private String g_rs_path=null;
	public AutoAnal(String path,DoAnal da) {
		g_path=path;
		g_da=da;
		g_sort=da.getSort();
	}	
	public void setRS(String rs_path) {
		g_rs_path=rs_path;
		
	}

	// anal task set list with algorithm choice
	public String analList(String ts_list) {
		if(g_rs_path==null) {
			SLog.err("set RS path in autoanal");
		}
		SLog.prn(2, "Anal:"+g_sort);
		MList load_ts=MList.load(g_path+"/"+ts_list);
		
		MList rs_list=MList.new_list();
		for(int i=0;i<load_ts.size();i++) {
			String fn=load_ts.get(i);
			String out=g_rs_path+"/"+fn+".rs."+g_sort;
			analTS(g_path+"/"+fn,out);
			rs_list.add(out);
		}
		
		String rs_fn=g_path+"/a_rs_list."+g_sort+".txt";
		rs_list.saveTo(rs_fn);
		return rs_fn;		
	}
	
	public void analTS(String tsn, String out) {
		SLog.prn(2, "ts:"+tsn);
		SLog.prn(2, "out:"+out);
//		SLog.prn(2, g_dur);
//		SLog.prn(2, s.getName());
		
		SysLoad sy=new SysLoad(tsn);
		int num=sy.getNum();

		MList rs_list=MList.new_list();
		for(int i=0;i<num;i++) {
			TaskVec dt=sy.loadOne();
//			SLog.prnc(2, i+": ");
			g_da.run(dt);
			rs_list.add(g_da.getRS());
		}
		rs_list.saveTo(out);
		
	}

	public void setSimul() {
		isSimul=true;
		
	}


}

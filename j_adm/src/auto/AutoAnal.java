package auto;

import anal.DoAnal;
import gen.SysLoad;
import task.DTaskVec;
import util.MList;
import util.SLog;

public class AutoAnal {
	protected String g_path;
	protected String g_rs_path;
	protected DoAnal g_da;
	protected int g_sort;
	protected boolean g_verbose=false;
	
	public AutoAnal(String path,DoAnal da) {
		g_path=path;
		g_da=da;
		g_sort=da.getSort();
	}	

	// simulate task set list with algorithm choice
	public String analList(String ts_list) {
		MList fu=new MList(g_path+"/"+ts_list);
		String rs_fn=g_path+"/a_rs_list."+g_sort+".txt";
		MList fu_rs=new MList();
		SLog.prn(2, "Anal:"+g_sort);
		
		for(int i=0;i<fu.size();i++) {
			String fn=fu.get(i);
			String out=g_path+"/"+fn+".rs."+g_sort;
			analTS(g_path+"/"+fn,out);
			fu_rs.add(out);
		}		
		fu_rs.saveTo(rs_fn);
		return rs_fn;		
	}
	
	public void analTS(String tsn, String out) {
		SLog.prn(2, "ts:"+tsn);
		SLog.prn(2, "out:"+out);
//		SLog.prn(2, g_dur);
//		SLog.prn(2, s.getName());
		
		SysLoad sy=new SysLoad(tsn);
		String ret=sy.open();
		int num=Integer.valueOf(ret).intValue();
		MList fu=new MList();

		for(int i=0;i<num;i++) {
			DTaskVec dt=sy.loadOne2();
//			SLog.prnc(2, i+": ");
			g_da.run(dt);
			fu.add(g_da.getRS());
		}
		fu.saveTo(out);
		
	}


}

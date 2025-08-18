package sim;

import anal.Anal;
import anal.AnalEDF_VD_ADM;
import anal.AnalSel_IMC;
import gen.SysLoad;
import imc.SimulSel_IMC;
import imc.TaskSimul_EDF_VD_ADM;
import imc.TaskSimul_IMC;
import task.DTaskVec;
import util.MList;
import util.SLog;

public class AutoSimul {
	protected String g_path;
	protected String g_rs_path;
	protected int g_sort;
	
	public AutoSimul(String path,int sort) {
		g_path=path;
		g_sort=sort;
	}	
	public void setRS(String rs_path) {
		g_rs_path=rs_path;
		
	}



	
	// simulate task set list with algorithm choice
	public String simulList(String ts_list) {
		MList fu=new MList(g_path+"/"+ts_list);
		String rs_fn=g_rs_path+"/a_sim_list."+g_sort+".txt";
		MList fu_rs=new MList();
		SLog.prn(2, "Anal:"+g_sort);
		
		for(int i=0;i<fu.size();i++) {
			String fn=fu.get(i);
			String out=g_rs_path+"/"+fn+".sim."+g_sort;
			simulTS(g_path+"/"+fn,out);
			fu_rs.add(out);
		}		
		fu_rs.saveTo(rs_fn);
		return rs_fn;		
	}
	
	public void simulTS(String tsn, String out) {
		SysLoad sy=new SysLoad(tsn);
		String ret=sy.open();
		int num=Integer.valueOf(ret).intValue();
		MList fu=new MList();

		for(int i=0;i<num;i++) {
			DTaskVec dt=sy.loadOne2();
//			SLog.prnc(2, i+": ");
			DoSimul ds=new DoSimul(dt,g_sort);
			ds.run(fu);
		}
		fu.saveTo(out);
		
	}



	

}

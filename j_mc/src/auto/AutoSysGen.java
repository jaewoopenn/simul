package auto;

import anal.Anal;
import anal.AnalEDF_VD_IMC;
import gen.ConfigGen;
import gen.SysGen;
import util.MList;
import util.SLog;

public  class AutoSysGen {
	private boolean g_onlyMC=false;
	private int g_stage=1;
	private String g_path;
	private String g_rs_path;
	private boolean g_isSch=false;

	public AutoSysGen(String path) {
		g_path=path;
	}	
	public void setRS(String rs_path) {
		g_rs_path=rs_path;
		
	}

	
	
	public void genTS(String cfg_list,String ts) {
		SLog.prn(3, g_path+"/"+cfg_list);
		MList fu=MList.load(g_path+"/"+cfg_list);
		
		MList fu_ts=MList.new_list();
//		int n=fu.load();
//		Log.prn(1, n+" ");
		int max=fu.size();
		Anal a=new AnalEDF_VD_IMC();
		for(int i=0;i<max;i++) {
			ConfigGen cfg=ConfigGen.load(fu.get(i));
			SysGen sg=SysGen.load(cfg);
			sg.setStage(g_stage);
			String fn=cfg.get_fn();
			SLog.prn(3, fn);
			if(g_onlyMC)
				sg.setOnlyMC();
			if(g_isSch)
				sg.setSch();
			int num=sg.getNum();
			sg.gen(g_path+"/"+fn, a,num);
			fu_ts.add(fn);
		}
		fu_ts.saveTo(g_path+"/"+ts);
	}
	

	
	public void setOnlyMC() {
		g_onlyMC=true;		
	}

	public void setStage(int s) {
		g_stage=s;
	}



	
	public void setSch(){
		g_isSch=true;
	}
	

	
	public void genXA(String cfg_list, String xaxis) {
		MList fu=MList.load(g_path+"/"+cfg_list);
		
		MList fu_xa=MList.new_list();
		int max=fu.size();
		for(int i=0;i<max;i++) {
			ConfigGen cfg=ConfigGen.load(fu.get(i));
			String mod=cfg.get_mod();
			fu_xa.add(mod);
		}
		fu_xa.saveTo(g_rs_path+"/"+xaxis);
	}
	
	
}

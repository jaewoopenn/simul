package auto;

import anal.Anal;
import gen.ConfigGen;
import gen.SysLoad;
import task.TaskMng;
import util.CProg;
import util.MList;
import util.SLog;

public abstract class Platform_base {
	protected String g_path;
	protected String g_rs_path;
	protected int g_num=100;
	protected double g_p_hc=0.5;
	protected double g_ratio=-1;
	protected double g_ratio_hi=-1;
	protected boolean g_isCheck=false;
	protected int g_life=0;
	
	
	public void setNum(int n) {
		g_num=n;
	}
	public void setP_HC(double d) {
		g_p_hc=d;
	}
	public void setRatio(double d) {
		g_ratio=d;
	}
	public void setRatio_hi(double d) {
		g_ratio_hi=d;
		
	}
	public void setCheck(){
		g_isCheck=true;
	}
	

	public void genXA(String cfg_list, String xaxis) {
		MList fu=new MList(g_path+"/"+cfg_list);
		
		MList fu_xa=new MList();
		int max=fu.size();
		for(int i=0;i<max;i++) {
			ConfigGen cfg=new ConfigGen(fu.get(i));
			cfg.readFile();
			String mod=cfg.get_mod();
			fu_xa.add(mod);
		}
		fu_xa.save(g_rs_path+"/"+xaxis);
	}
	
	// anal
	public void anal_loop(String rs_list,String ts_list, int end) {
		MList fu=new MList();
		for(int i=0;i<end;i++){
			String rs=anal(ts_list,i);
			fu.add(rs);
		}
		fu.save(g_rs_path+"/"+rs_list);
	}
	// analyze task set list with algorithm choice
	public String anal(String ts_list,int sort) {
		MList fu=new MList(g_path+"/"+ts_list);
		String rs_fn=g_rs_path+"/a_rs_list."+sort+".txt";
		MList fu_rs=new MList();
		Anal a=getAnal(sort);
		
		SLog.prn(1, "a:"+a.getName());
		
		for(int i=0;i<fu.size();i++) {
			String fn=fu.get(i);
			String out=g_rs_path+"/"+fn+".rs."+sort;
//			SLog.prn(2, out);
			anal_one(fn,out,a);
			fu_rs.add(out);
		}		
		fu_rs.save(rs_fn);
		return rs_fn;
	}
	public abstract Anal getAnal(int sort) ;
	
	public void anal_one(String ts,String out,Anal a) {
		SysLoad sy=new SysLoad(g_path+"/"+ts);
		String ret=sy.open();
		int num=Integer.valueOf(ret).intValue();
		MList fu=new MList();
		for(int i=0;i<num;i++) {
			TaskMng tm=sy.loadOne();
			if(tm==null) break;
//			tm.prnPara();
//			SLog.prn(2," "+i);
//			if(i==367)
//				break;
			a.init(tm);
			a.prepare();
//			a.prn();
			if(a.is_sch()) {
				fu.add("1");
			} else {
				fu.add("0");
//				a.prn();
			}
		}
		fu.save(out);
	}
	
	
	

}

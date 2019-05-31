package auto;
/*
 * 1. gen config
 * 2. gen task set
 * 3. gen prm for task set
 * 3-2. check sch for task set and its prm 
 */


import com.PRM;

import anal.Anal;
import anal.AnalSel;
import gen.ConfigGen;
import gen.SysGen;
import task.TaskSet;
import util.MList;
import util.SLog;

public class Platform {
	private String g_path;
	private int g_num=100;
	public Platform(String path) {
		g_path=path;
	}
	
	public void setNum(int n) {
		g_num=n;
	}
	
	// gen CFG, TS
	public void genCfg_util(int base, int top,int step,String cfg_list) {
		ConfigGen cg=ConfigGen.getSample();
		MList fu=new MList();
		cg.setParam("num",g_num+"");
		for(int i=base;i<=top;i+=step){
			int ub=i;
			String lab=ub+"";
			SLog.prn(2, lab);
			cg.setParam("u_lb", (ub-5)*1.0/100);
			cg.setParam("u_ub", (ub+5)*1.0/100);
			cg.setParam("label",lab) ;
			cg.setParam("fn", g_path+"/taskset_"+lab+".txt");
			String fn=g_path+"/cfg_"+lab+".txt";
			cg.write(fn);
			fu.add(fn);
		}
		fu.save(g_path+"/"+cfg_list);
		
	}

	public void genTS(String cfg_list,String ts, String xaxis) {
		MList fu=new MList(g_path+"/"+cfg_list);
		
		MList fu_ts=new MList();
		MList fu_xa=new MList();
//		int n=fu.load();
//		Log.prn(1, n+" ");
		while(true) {
			String cfg_fn=fu.getNext();
			if(cfg_fn==null)
				break;
			ConfigGen cfg=new ConfigGen();
			cfg.load(cfg_fn);
			SysGen sg=new SysGen(cfg);
			String fn=sg.gen();
			fu_ts.add(fn);
			String mod=cfg.getLabel();
			fu_xa.add(mod);
		}
		fu_ts.save(g_path+"/"+ts);
		fu_xa.save(g_path+"/"+xaxis);
	}
	
	// anal
	public void anal_loop(String rs_list,String ts_list, int end) {
		MList fu=new MList();
		for(int i=0;i<end;i++){
			String rs=anal(ts_list,i);
			fu.add(rs);
		}
		fu.save(g_path+"/"+rs_list);
		
	}
	
	// analyze task set list with algorithm choice
	public String anal(String ts_list,int sort) {
		MList fu=new MList(g_path+"/"+ts_list);
		MList fu_rs=new MList();
		String fn;
		Anal a=AnalSel.getAnal(sort);
		while((fn=fu.getNext())!=null) {
			String out=fn+".rs."+sort;
			SLog.prn(3, out);
			anal_one2(fn,out,a);
			fu_rs.add(out);
		}		
		String rs_fn=g_path+"/_rs_list."+sort+".txt";
		fu_rs.save(rs_fn);
		return rs_fn;
	}
	
	public void anal_one(String ts,String out,Anal a) {
		SysLoad sy=new SysLoad(ts);
		String ret=sy.open();
		int num=Integer.valueOf(ret).intValue();
		MList fu=new MList(out);
		for(int i=0;i<num;i++) {
			TaskSet tm=sy.loadOne();
			if(tm==null) break;

			a.init(tm);
			if(a.is_sch()) {
				fu.add("1");
			} else {
				fu.add("0");
			}
		}
		fu.save(out);
		
	}
	public void anal_one2(String ts,String out,Anal a) {
		SysLoad sy=new SysLoad(ts);
		String ret=sy.open();
		int num=Integer.valueOf(ret).intValue();
		MList fu=new MList();
		int p=25;
		for(int i=0;i<num;i++) {
			TaskSet tm=sy.loadOne();
			if(tm==null) break;
			tm.sort();

			a.init(tm);
			double e=a.getExec(p);
			PRM prm=new PRM(p,e);
			a.setPRM(prm);
			if(!a.is_sch()) {
				SLog.err("not sch");
			}
			double ov=e/p-tm.getUtil();
			fu.add(ov+"");
		}
		fu.save(out);
		
	}
	
	
	
}

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
	private int g_num=10;
	private double g_t_lb=0.02;
	private double g_t_ub=0.1;
	public Platform(String path) {
		g_path=path;
	}
	
	public void setNum(int n) {
		g_num=n;
	}
	public void setTU(double lb,double ub) {
		g_t_lb=lb;
		g_t_ub=ub;
	}
	// gen CFG, TS
	public void genCfg_util(int base, int top,int step,String cfg_list) {
		ConfigGen cg=ConfigGen.getSample();
		MList fu=new MList();
		cg.setParam("num",g_num+"");
		cg.setParam("tu_lb",g_t_lb);
		cg.setParam("tu_ub",g_t_ub);
		for(int i=base;i<=top;i+=step){
			int ub=i;
			String lab=ub+"";
			SLog.prn(2, lab);
			cg.setParam("u_lb", (ub-5)*1.0/100);
			cg.setParam("u_ub", (ub)*1.0/100);
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
			String mod=cfg.get_lab();
			fu_xa.add(mod);
		}
		fu_ts.save(g_path+"/"+ts);
		fu_xa.save(g_path+"/"+xaxis);
	}
	
	// anal
	public void anal_loop(String rs_list,String ts_list, int[] anal) {
		MList fu=new MList();
		for(int i:anal){
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
		for(int i=0;i<num;i++) {
			TaskSet tm=sy.loadOne();
			if(tm==null) break;
			String res=anal_tasks(tm,a);
			fu.add(res);
		}
		fu.save(out);
		
	}

	public void anal_one3(String ts,Anal a,int num) {
		SysLoad sy=new SysLoad(ts);
		sy.open();
		TaskSet tm=null;
		for(int i=0;i<=num;i++) {
			tm=sy.loadOne();
		}
		String res=anal_tasks(tm,a);
		SLog.prn(3, res);
	}

	
	private String anal_tasks(TaskSet tm, Anal a) {
		int p=25;  // TODO change period
		tm.sort();

		a.init(tm);
		double e=a.getExec(p);
		PRM prm=new PRM(p,e);
		prm.prn();
		a.setPRM(prm);
		if(!a.is_sch()) {
			SLog.err("not sch ");
		}

		double ru=e/p;
		return ru+" "+tm.getUtil();

	}

}

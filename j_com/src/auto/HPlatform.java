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
import gen.HSysGen;
import task.Comp;
import task.CompSet;
import task.TaskSet;
import util.MList;
import util.SLog;

public class HPlatform {
	private String g_path;
	private int g_num=100;
	public HPlatform(String path) {
		g_path=path;
	}
	
	public void setNum(int n) {
		g_num=n;
	}
	
	// gen CFG, TS
	public void genCfg_util(int base, int top,int step,String cfg_list) {
		ConfigGen cg=ConfigGen.getHSample();
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
			HSysGen sg=new HSysGen(cfg);
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
			anal_one(fn,out,a);
			fu_rs.add(out);
		}		
		String rs_fn=g_path+"/_rs_list."+sort+".txt";
		fu_rs.save(rs_fn);
		return rs_fn;
	}
	
	public void anal_one(String f_in,String f_out,Anal anal) {
		HSysLoad sy=new HSysLoad(f_in);
		String ret=sy.open();
		int num=Integer.valueOf(ret).intValue();
		MList fu=new MList();
		for(int i=0;i<num;i++) {
			CompSet cm=sy.loadOne();
			if(cm==null) break;
//			cm.prn();
			double r=analComSet(cm,anal);
			fu.add(r+"");
		}
		fu.save(f_out);
	}
	private double analComSet(CompSet cs,Anal anal) {
		double r=0;
		for(int i=0;i<cs.size();i++) {
			Comp c=cs.get(i);
			r+=analCom(c,anal);
		}
		return r;
	}

	private double analCom(Comp c,Anal a) {
		int p=25;  // TODO change period
		TaskSet tm=c.getTS();
		tm.sort();
		a.init(tm);
		double e=a.getExec(p);
		PRM prm=new PRM(p,e);
		prm.prn();
		a.setPRM(prm);
		if(!a.is_sch()) {
			SLog.err("not sch ");
		}
		SLog.prnc(3, c.cid+" ");
		SLog.prn(3, p+" "+e);
		return e/p;
	}


	
	
}

package auto;

import anal.Anal;
import anal.AnalEDF_IMC;
import gen.ConfigGen;
import gen.SysLoad;
import imc.TaskSimul_EDF_IMC_gen;
import sim.SimulInfo;
import sim.SysMng;
import sim.TaskSimul_base;
import task.DTaskVec;
import task.TaskMng;
import task.TaskSet;
import util.CProg;
import util.MList;
import util.SLog;
import util.SLogF;

public abstract class Platform_base {
	protected String g_path;
	protected String g_rs_path;
	protected int g_num=100;
	protected int g_dur=2000;
	protected double g_p_ms=0.3;
	protected double g_p_hc=0.5;
	protected double g_ratio=-1;
	protected double g_ratio_hi=-1;
	protected boolean g_isCheck=false;
	protected int g_dur_set[]= {4000,8000,16000,32000,64000,128000};
	protected boolean g_verbose=false;
	protected boolean g_recoverIdle=true;
	protected int g_life=0; // life는 hi mode가 얼마나 지속... time unit 기
	
	
	public void setNum(int n) {
		g_num=n;
	}
	public void setDur(int n) {
		g_dur=n;
	}
	public void setP_MS(double d) {
		g_p_ms=d;
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
	
	public void setVerbose(){
		g_verbose=true;
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
		fu_xa.saveTo(g_rs_path+"/"+xaxis);
	}
	
	public void genXA_dur(String xaxis) {
		MList fu_xa=new MList();
		for(int i=0;i<g_dur_set.length;i++) {
			fu_xa.add((g_dur_set[i]/1000)+"");
		}
		fu_xa.saveTo(g_rs_path+"/"+xaxis);
	}	
	
	// anal
	public void anal_loop(String rs_list,String ts_list, int end) {
		MList fu=new MList();
		for(int i=0;i<end;i++){
			String rs=anal(ts_list,i);
			fu.add(rs);
		}
		fu.saveTo(g_rs_path+"/"+rs_list);
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
		fu_rs.saveTo(rs_fn);
		return rs_fn;
	}
	public abstract Anal getAnal(int sort) ;
	
	public void anal_one(String ts,String out,Anal a) {
		SysLoad sy=new SysLoad(g_path+"/"+ts);
		String ret=sy.open();
		int num=Integer.valueOf(ret).intValue();
		MList fu=new MList();
		for(int i=0;i<num;i++) {
			DTaskVec dt=sy.loadOne2();
			if(dt==null) break;
			double dtm=0;
//			SLog.prn(2, i+"");
			a.reset();
			for(int j=0;j<dt.getNum();j++) {
				TaskSet tmp=new TaskSet(dt.getVec(j));
				TaskMng tm=tmp.getTM();
				a.init(tm);
				dtm=Math.max(dtm, a.getDtm());
			}
			if(dtm<=1) {
				fu.add("1");
			} else {
				fu.add("0");
			}
		}
		fu.saveTo(out);
	}
	
	public abstract Anal getAnalSim(int sort) ;
	public abstract TaskSimul_base getSim(int sort) ;
	
	//simulation
	public void sim_loop(String rs_list,String ts_list, int start, int end) {
		MList fu=new MList();
		for(int i=start;i<end;i++){
			String rs=simul(ts_list,i);
			fu.add(rs);
		}
		fu.saveTo(g_rs_path+"/"+rs_list);
	}

	
	//simulation
	public void sim_loop_dur(String rs_list,String ts_list, int end) {
		MList fu=new MList();
		for(int i=0;i<end;i++){
			String rs=simul_dur(ts_list,i);
			fu.add(rs);
		}
		fu.saveTo(g_rs_path+"/"+rs_list);
	}
	


	// simulate task set list with algorithm choice
	public String simul(String ts_list,int sort) {
		MList fu=new MList(g_path+"/"+ts_list);
		String rs_fn=g_rs_path+"/a_sim_list."+sort+".txt";
		MList fu_rs=new MList();
		Anal a=getAnalSim(sort);
		SLog.prn(2, "Anal:"+a.getName());
		TaskSimul_base s=getSim(sort);
		s.setRecoverIdle(g_recoverIdle);
		
		for(int i=0;i<fu.size();i++) {
			String fn=fu.get(i);
			String out=g_rs_path+"/"+fn+".sim."+sort;
			simul_one(g_path+"/"+fn,out,a,s);
			fu_rs.add(out);
		}		
		fu_rs.saveTo(rs_fn);
		return rs_fn;		
	}
	
	public void gen_scn(String ts_list,int ts_list_n, int ts_n, int scn_n) {
		MList fu=new MList(g_path+"/"+ts_list);
		
		String fn=fu.get(ts_list_n);
		for(int i=0;i<scn_n;i++) {
			String out=g_rs_path+"/"+fn+".sim."+i+".txt";
			SLog.prn(2,out);
			gen_scn_one(g_path+"/"+fn,out,ts_n);
		}
	}

	public String run_scn(int sort,String ts_list,int ts_list_n,int ts_n,int scn_from, int scn_to) {
		MList fu=new MList(g_path+"/"+ts_list);
		String rs_fn=g_rs_path+"/a_sim_list."+sort+".txt";
		Anal a=getAnalSim(sort);
		SLog.prn(2, "Anal:"+a.getName());
		TaskSimul_base s=getSim(sort);
		s.setRecoverIdle(g_recoverIdle);
		
		String fn=fu.get(ts_list_n);
		for(int i=scn_from;i<scn_to;i++) {
			String out=g_rs_path+"/"+fn+".sim."+i+".txt";
			SLog.prn(2,out);
			run_scn_one(g_path+"/"+fn,out,a,s,ts_n,i);
		}
		return rs_fn;		
	}

	
	
	public void simul_one(String ts,String out,Anal a,TaskSimul_base s) {
		SLog.prn(2, "ts:"+ts);
		SLog.prn(2, "out:"+out);
		SLog.prn(2, g_dur);
		SLog.prn(2, s.getName());
		SysLoad sy=new SysLoad(ts);
		String ret=sy.open();
		int num=Integer.valueOf(ret).intValue();
		CProg prog=new CProg(num);
		prog.setLog(2);
		
		if(g_verbose) {
			prog.setSort(1);
			prog.setStep(1);
		} else { 
			prog.setPercent();
		}
		Anal base;
		MList fu=new MList();
		for(int i=0;i<num;i++) {
//			SLog.prn(2, "no:"+i);
			TaskMng tm=sy.loadOne();
			if(tm==null) break;
			
			a.init(tm);
			prog.inc();
			if(!a.is_sch()) {
//				SLog.prn(2, "no sch "+i);
				fu.add("1.0");
				continue;
			}
			base=new AnalEDF_IMC();
			base.init(tm);
			if(base.is_sch()) {
				fu.add("0.0");
				continue;
			}

			double x=a.computeX();
			
			SysMng sm=new SysMng();
			sm.setMS_Prob(g_p_ms);
			sm.setX(x);
			sm.setLife(g_life);
			sm.setDelay(x*tm.getLongPeriod());
//			sm.prn();
			s.init_sm_tm(sm,tm);
			s.simul(g_dur);
			s.simul_end();
			SimulInfo si=s.getSI();
			fu.add(si.getDMR()+"");
		}
		fu.saveTo(out);
	}

	public void gen_scn_one(String ts,String out,int ts_n) {
//		SLog.prn(2, ts);
		SysLoad sy=new SysLoad(ts);
		sy.open();
		TaskMng tm=null;
		SLogF.init(out);
		SLogF.setGen();
		sy.moveto(ts_n);
		tm=sy.loadOne();
		SysMng sm=new SysMng();
		TaskSimul_base s=new TaskSimul_EDF_IMC_gen();
		sm.setMS_Prob(g_p_ms);
		sm.setX(1);
		s.init_sm_tm(sm,tm);
		s.simul(g_dur);
		s.simul_end();
		SLogF.save();
		
	}

	public void run_scn_one(String ts,String out,Anal a,TaskSimul_base s,int ts_n, int scn) {
		SysLoad sy=new SysLoad(ts);
		sy.open();
		Anal base;
		MList fu=new MList();
		TaskMng tm=null;
		for(int i=0;i<=ts_n;i++) {
			tm=sy.loadOne();
			if(i<ts_n)
				continue;
		}
		a.init(tm);
		if(!a.is_sch()) {
			SLog.prn(2, "no sch "+ts_n);
			fu.add("1.0");
			return;
		}
		base=new AnalEDF_IMC();
		base.init(tm);
		if(base.is_sch()) {
			SLog.prn(2, "EDF sch "+ts_n);
			fu.add("0.0");
			return;
		}

		double x=a.computeX();
		
		SysMng sm=new SysMng();
		sm.setMS_Prob(g_p_ms);
		sm.setX(x);
		sm.setLife(g_life);
		sm.setDelay(x*tm.getLongPeriod());
		s.init_sm_tm(sm,tm);
		s.simul(g_dur);
		s.simul_end();
	}


	
	// simulate task set list with algorithm choice (duration)
	public String simul_dur(String ts_list,int sort) {
		MList fu=new MList(g_path+"/"+ts_list);
		String rs_fn=g_rs_path+"/a_sim_list."+sort+".txt";
		MList fu_rs=new MList();
		
		String fn=fu.get(0);
		int anal_sort=Math.min(sort, 3);
		
		Anal a=getAnalSim(anal_sort);
		TaskSimul_base s=getSim(sort);
		
		for(int i=0;i<g_dur_set.length;i++) {
			String out=g_rs_path+"/"+fn+"_"+i+".sim."+sort;
			g_dur=g_dur_set[i];
			simul_one(g_path+"/"+fn,out,a,s);
			fu_rs.add(out);
		}		
		fu_rs.saveTo(rs_fn);
		return rs_fn;		
	}

	

}

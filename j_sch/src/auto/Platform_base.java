package auto;

import anal.Anal;
import gen.SysLoad;
import sim.SimulInfo;
import sim.SysMng;
import sim.TaskSimul_base;
import sim.mc.TaskSimulMC;
import task.TaskMng;
import util.CProg;
import util.MList;
import util.SLog;

public abstract class Platform_base {
	protected String g_path;
	protected int g_num=100;
	protected int g_dur=2000;
	protected double g_p_ms=0.3;
	protected double g_p_hc=0.5;
	protected double g_ratio=-1;
	protected double g_ratio_hi=-1;
	protected boolean g_isCheck=false;
	protected int g_dur_set[]= {4000,8000,16000,32000,64000,128000};
	protected boolean g_be=false; // best effort 
	
	private boolean g_recoverIdle=true;
	private int g_life=0;
	
	public void setRecoverIdle(boolean b) {
		g_recoverIdle=b;
	}
	
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
	
	public void setBE() {
		SLog.prn(2, "BE");
		this.g_be=true;
		
	}
	public void setLife(int i) {
		g_life=i;
		
	}
	
	public void genXA(String xaxis) {
		MList fu_xa=new MList();
		for(int i=0;i<g_dur_set.length;i++) {
			fu_xa.add((g_dur_set[i]/1000)+"");
		}
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
		String rs_fn=g_path+"/a_rs_list."+sort+".txt";
		MList fu_rs=new MList();
		Anal a=getAnal(sort);
		
		SLog.prn(1, "a:"+a.getName());
		for(int i=0;i<fu.size();i++) {
			String fn=fu.get(i);
			String out=fn+".rs."+sort;
//			SLog.prn(2, out);
			anal_one(fn,out,a);
			fu_rs.add(out);
		}		
		fu_rs.save(rs_fn);
		return rs_fn;
	}
	public abstract Anal getAnal(int sort) ;
	
	public void anal_one(String ts,String out,Anal a) {
		SysLoad sy=new SysLoad(ts);
		String ret=sy.open();
		int num=Integer.valueOf(ret).intValue();
		MList fu=new MList();
		for(int i=0;i<num;i++) {
			TaskMng tm=sy.loadOne();
			if(tm==null) break;

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
	
	public abstract Anal getAnalSim(int sort) ;
	public abstract TaskSimul_base getSim(int sort) ;
	
	//simulation
	public void sim_loop(String rs_list,String ts_list, int start, int end) {
		MList fu=new MList();
		for(int i=start;i<end;i++){
			String rs=simul(ts_list,i);
			fu.add(rs);
		}
		fu.save(g_path+"/"+rs_list);
	}

	
	//simulation
	public void sim_loop_dur(String rs_list,String ts_list, int end) {
		MList fu=new MList();
		for(int i=0;i<end;i++){
			String rs=simul_dur(ts_list,i);
			fu.add(rs);
		}
		fu.save(g_path+"/"+rs_list);
	}
	


	// simulate task set list with algorithm choice
	public String simul(String ts_list,int sort) {
		MList fu=new MList(g_path+"/"+ts_list);
		String rs_fn=g_path+"/a_sim_list."+sort+".txt";
		MList fu_rs=new MList();
		Anal a=getAnalSim(sort);
		SLog.prn(2, "Anal:"+a.getName());
		TaskSimul_base s=getSim(sort);
		if(g_be)
			s.setBE();
		s.setRecoverIdle(g_recoverIdle);
		
		for(int i=0;i<fu.size();i++) {
			String fn=fu.get(i);
			String out=fn+".sim."+sort;
			simul_one(fn,out,a,s);
			fu_rs.add(out);
		}		
		fu_rs.save(rs_fn);
		return rs_fn;		
	}
	
	public void simul_one(String ts,String out,Anal a,TaskSimul_base s) {
		SLog.prn(2, ts);
		SLog.prn(2, g_dur);
		SLog.prn(2, s.getName());
		SysLoad sy=new SysLoad(ts);
		String ret=sy.open();
		int num=Integer.valueOf(ret).intValue();
		CProg prog=new CProg(num);
		prog.setLog(2);
		
		prog.setSort(1);
		prog.setStep(1);
		
//		prog.setPercent();

		MList fu=new MList();
		for(int i=0;i<num;i++) {
//			SLog.prn(2, "no:"+i);
			TaskMng tm=sy.loadOne();
			if(tm==null) break;
			
			a.init(tm);
			a.prepare();
			prog.inc();
			if(!a.is_sch()) {
				SLog.prn(2, "no sch "+i);
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
			s.simul(0,g_dur);
			s.simul_end();
			SimulInfo si=s.getSI();
			fu.add(si.getDMR()+"");
		}
		fu.save(out);
	}


	// one 
	public String simul_a(String fn,int sort) {
		int anal_sort=Math.min(sort, 3);
		
		Anal a=getAnalSim(anal_sort);
		TaskSimul_base s=getSim(sort);
		if(g_be)
			s.setBE();
		String out=fn+".sim."+sort;
		simul_one(fn,out,a,s);
		return "OK";		
		
	}
	// simulate task set list with algorithm choice (duration)
	public String simul_dur(String ts_list,int sort) {
		MList fu=new MList(g_path+"/"+ts_list);
		String rs_fn=g_path+"/a_sim_list."+sort+".txt";
		MList fu_rs=new MList();
		
		String fn=fu.get(0);
		int anal_sort=Math.min(sort, 3);
		
		Anal a=getAnalSim(anal_sort);
		TaskSimul_base s=getSim(sort);
		if(g_be)
			s.setBE();
		
		for(int i=0;i<g_dur_set.length;i++) {
			String out=fn+"_"+i+".sim."+sort;
			g_dur=g_dur_set[i];
			simul_one(fn,out,a,s);
			fu_rs.add(out);
		}		
		fu_rs.save(rs_fn);
		return rs_fn;		
	}

	

}

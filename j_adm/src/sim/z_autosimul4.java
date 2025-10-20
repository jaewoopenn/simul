package sim;

import java.util.Vector;

import anal.DoAnal;
import gen.SysLoad;
import task.DTaskVec;
import util.MFile;
import util.SEngineT;
import util.SLog;

// 테스트는 여기에서.

public class z_autosimul4 {
	public static void init_s() {
		s_idx=1;
//		s_idx=2;
//		s_idx=3;
//		s_idx=4;
//		s_idx=5;
//		s_idx=6;
		
		
//		s_log_level=1;
		s_log_level=2;
	}
	public String g_tsn;
	public int g_idx;
	public int g_sort;
	public int g_dur;
	public double g_prob;
		

	
	public void init() {
		g_sort=0;
//		g_sort=1;

		g_tsn="adm/sim/taskset_88.txt";
		g_idx=3;

//		g_tsn="adm/test2.txt";
//		g_idx=0;
//		g_idx=1;
//		g_idx=2;
//		g_idx=3;
//		g_idx=4;
//		g_idx=5;
//		g_idx=6;
		
		g_dur=3100;
//		g_dur=5000;
		
		g_prob=0.4;
	}
	public void run(String fn) {
		int tot=0;
		int i=0;
		int upper=1000;
//		int upper=0;
		Vector<Double> rs1=new Vector<Double>();
		Vector<Double> rs2=new Vector<Double>();
		SysLoad sy=new SysLoad(fn);
		DTaskVec dt= sy.loadOne();
		while(dt!=null) {
//			SLog.prn(2,"#### no: "+i+" #####");
			DoSimul ds=new DoSimul(g_sort,g_dur,g_prob);
			ds.run(dt);
			SimulInfo si=ds.getSI();
			rs1.add(si.getRejected());
			tot++;
			if(i==upper)
				break;
			i++;
			dt= sy.loadOne();
		}
		sy=new SysLoad(fn);
		dt= sy.loadOne();
		i=0;
		while(dt!=null) {
			DoAnal da=new DoAnal(g_sort);
			da.run_simul(dt);
			double rs=Double.valueOf(da.getRS());
			rs2.add(rs);
			if(i==upper)
				break;
			i++;
			dt= sy.loadOne();
		}
		for(i=0;i<tot;i++) {
			double r1=rs1.elementAt(i);
			double r2=rs2.elementAt(i);
			SLog.prn(2,"#### no: "+i+","+r1+","+r2);
			if(r1!=r2) {
				SLog.err("not matched");
			}
		}

	}
	public int test1()	{
		init();
		String fn=g_tsn;
		run(fn);
		return 0;

	}
	
	public int test2() {
		init();
		String prefix="adm/sim/";
		MFile mf=new MFile(prefix+"a_ts_list.txt");
		mf.load();
		for(int i=0;i<mf.bufferSize();i++) {
			String fn=mf.getBuf(i);
			SLog.prn(2,fn);
			run(prefix+fn);
		}
		return 0;
	}
	public int test3() {
		init();
		SysLoad sy=new SysLoad(g_tsn);
		DTaskVec dt= sy.loadOne();
		int tot=0;
		Vector<Double> rs1=new Vector<Double>();
		Vector<Double> rs2=new Vector<Double>();
		while(dt!=null) {
			DoAnal da=new DoAnal(0);
			da.run_simul(dt);
			double r=Double.valueOf(da.getRS());
			rs1.add(r);
			dt= sy.loadOne();
			tot++;
		}
		sy=new SysLoad(g_tsn);
		dt= sy.loadOne();
		while(dt!=null) {
			DoAnal da=new DoAnal(0);
			da.run_simul(dt);
			double r=Double.valueOf(da.getRS());
			rs2.add(r);
			dt= sy.loadOne();
		}
		for(int i=0;i<tot;i++) {
			double r1=rs1.elementAt(i);
			double r2=rs2.elementAt(i);
			SLog.prn(2,"#### no: "+i+","+r1+","+r2);
			if(r1>r2) {
				SLog.err("???");
			}
		}
		return 0;
	}
	
	public DTaskVec getDT(int n) {
		
		SysLoad sy=new SysLoad(g_tsn);
		sy.moveto(n);
		DTaskVec dt= sy.loadOne();
		if(dt==null) {
			SLog.prn("out of range ");
		}
		return dt;
	}

	public  int test4() {
		return 1;
	}
	public  int test5() {

		return 0;
	}
	public  int test6() {
		init();
		DTaskVec dt=getDT(g_idx);
		init();
//		DTUtil.prn(dt);
		String out="adm/test.txt";

		DoSimul ds=new DoSimul(0,g_dur,g_prob);
		ds.setTrace(out);
		double x=0.39;
		ds.run(dt,x);
		
		SimulInfo si=ds.getSI();
		si.prn();
		return 1;
	}
	public  int test7() {
		return 1;
	}
	public  int test8() {
		return 0;
	}
	public  int test9() {
		return 0;
	}
	public  int test10() {
		return 0;
	}
	
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws Exception {
		z_autosimul4.init_s();
		Class c = z_autosimul4.class;
		z_autosimul4 m=new z_autosimul4();
		int[] aret=z_autosimul4.gret;
		if(s_idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,s_idx,s_log_level);
	}
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
	private static int s_idx;
	private static int s_log_level;
}
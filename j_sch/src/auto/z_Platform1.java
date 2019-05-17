package auto;
// real use --> z_auto1 
// this file is developing version



import anal.AnalEDF_VD;
import basic.TaskMng;
import gen.ConfigGen;
import gen.SysGen;
import gen.SysGenMC;
import gen.SysLoad;
import util.MOut;
import util.MFile;
import util.SLog;
import util.SEngineT;

public class z_Platform1 {
//	public static int idx=5;
	public static int idx=6;
	public static int log_level=1;

	private String g_path="sch/t1";
	// gen task set (
	public int test1() 
	{
		MFile fu=new MFile(g_path+"a_cfg_list.txt");
		MOut fu_ts=new MOut(g_path+"a_ts_list.txt");
		MOut fu_rs=new MOut(g_path+"a_x_list.txt");
		fu.load();
//		int n=fu.load();
//		Log.prn(1, n+" ");
		for(int i=0;i<fu.size();i++) {
			ConfigGen cfg=new ConfigGen(fu.get(i));
			cfg.readFile();
			SysGen eg=new SysGenMC(cfg);
			String fn=cfg.get_fn();
			eg.gen(fn);
			fu_ts.write(fn);
			String mod=cfg.get_mod();
			fu_rs.write(mod);
		}
		fu_ts.save();
		fu_rs.save();
		
		return -1;
	}

	// gen task set (
	public int test2() 
	{
		String cl="a_cfg_list.txt";
		String ts="a_ts_list.txt";
		String xl="a_x_list.txt";
		Platform p=new Platform(g_path);
		p.genTS(cl,ts,xl);
		return -1;
	}
	
	public int test3() 
	{
		SysLoad sy=new SysLoad(g_path+"taskset_55");
		sy.open();
		while(true) {
			TaskMng tm=sy.loadOne();
			if(tm==null) break;
			tm.prnInfo();
		}
		return -1;		
	}
	
	public  int test4() 
	{
		MFile fu=new MFile(g_path+"a_ts_list.txt");
//		FUtil fu_rs=new FUtil(path+"a_rs_list.txt");
		fu.load();
		for(int i=0;i<fu.size();i++) {
			String fn=fu.get(i);
			int n=0;
			SysLoad sy=new SysLoad(fn);
			sy.open();
			while(true) {
				TaskMng tm=sy.loadOne();
				if(tm==null) break;
				n++;
			}
			SLog.prn(1, fn+" "+n);
		}
//		fu_rs.save();
		return -1;
		
	}
	
	public  int test5() 
	{
		String ts="a_ts_list.txt";
		Platform p=new Platform(g_path);
		p.anal(ts,0);
		p.anal(ts,1);
		return -1;
	}
	public  int test6() 
	{
//		String ts="taskset_55";
		String ts="/sch/t1/taskset_95";
		String out=ts+".rs.txt";
		Platform p=new Platform(g_path);
		p.anal_one(ts,out,new AnalEDF_VD());
		return -1;
	}
	
	public  int test7()
	{
		String path="test/t1/";
		String cf="a_cfg_list.txt";
		ConfigGen eg=ConfigGen.getPredefined();
		MOut fu=new MOut(path+cf);
		eg.setParam("subfix", path);
		eg.setParam("num","100");
		int base=50;
		for(int i=0;i<10;i++){
			int lb=i*5+base;
			SLog.prn(2, lb+"");
			eg.setParam("u_lb", (lb)*1.0/100+"");
			eg.setParam("u_ub", (lb+5)*1.0/100+"");
			eg.setParam("mod", (lb+5)+"");
			String fn=path+"/cfg_"+i+".txt";
			eg.setFile(fn);
			eg.write();
			fu.write(fn);
		}
		fu.save();
		return 1;
	}
	public  int test8()
	{
		String path="test/t1/";
		String cf="a_cfg_list.txt";
		Platform p=new Platform(path);
		p.genCfg_util(cf,100);
		return 0;
	}
	
	public  int test9()
	{
		return 0;
	}
	
	public  int test10()
	{
		return 0;
	}
	
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws Exception {
		Class c = z_Platform1.class;
		z_Platform1 m=new z_Platform1();
		int[] aret=z_Platform1.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}
	
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};

}

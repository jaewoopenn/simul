package gen;
import anal.Anal;
import anal.AnalEDF_VD;
import util.MList;
import util.SLog;
import util.SEngineT;

public class z_SysGen1 {
//	public static int idx=1;
	public static int idx=2;
	public static int log_level=1;

	// gen
	public int test1() 
	{
//		ConfigGen cfg=new ConfigGen("config/cfg1_copy.txt");
		ConfigGen cfg=new ConfigGen("sch/t1/cfg_9.txt");
		cfg.readFile();
		SysGen sg=new SysGen(cfg);
		String fn=cfg.get_fn();
		Anal a=new AnalEDF_VD();
		int num=sg.prepare_MC();
		sg.gen(fn, a,num);
		return 1;

	}

	// gen with schedulable 
	public int test2() 
	{
		ConfigGen cfg=new ConfigGen("sch/t1/cfg_8.txt");
		cfg.readFile();
		SysGen sg=new SysGen(cfg);
		sg.setCheck();
		String fn=cfg.get_fn();
		SLog.prn(1, fn);
		Anal a=new AnalEDF_VD();
		int num=sg.prepare_MC();
		sg.gen(fn, a,num);
		return 0;
	}
	
	public int test3() 
	{
		String path="test/t1/";
		MList fu=new MList(path+"a_cfg_list.txt");
		MList fu_ts=new MList();
		MList fu_rs=new MList();
//		int n=fu.load();
//		Log.prn(1, n+" ");
		Anal a=new AnalEDF_VD();
		for(int i=0;i<fu.size();i++) {
			ConfigGen cfg=new ConfigGen(fu.get(i));
			cfg.readFile();
			SysGen sg=new SysGen(cfg);
			String fn=cfg.get_fn();
			int num=sg.prepare_MC();
			sg.gen(fn, a,num);
			fu_ts.add(fn);
			String mod=cfg.get_mod();
			fu_rs.add(mod);
		}
		fu_ts.save(path+"a_ts_list.txt");
		fu_rs.save(path+"a_x_list.txt");

		return 0;
	}
	public  int test4() {
		return 1;
	}
	public  int test5() {
		return 1;
	}
	public  int test6() {
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
		Class c = z_SysGen1.class;
		z_SysGen1 m=new z_SysGen1();
		int[] aret=z_SysGen1.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}
	
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};

}

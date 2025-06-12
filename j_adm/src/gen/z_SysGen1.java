package gen;
import anal.Anal;
import anal.AnalEDF_VD;
import util.SLog;
import util.SEngineT;

public class z_SysGen1 {
	public static int idx=1;
//	public static int idx=2;
	public static int log_level=1;

	// gen
	public int test1() 
	{
		String path="adm/test1/";
		ConfigGen cfg=new ConfigGen(path+"cfg_8.txt");
		cfg.readFile();
		SysGen sg=new SysGen(cfg);
		String fn=cfg.get_fn();
		Anal a=null;
//		a=new AnalEDF_VD();
		int num=sg.prepare_IMC();
		sg.gen2(path+fn+".txt", a,num);
		SLog.prn(1, "OK "+num);
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
		int num=sg.prepare_IMC();
		sg.gen(fn, a,num);
		return 0;
	}
	
	public int test3() 
	{

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

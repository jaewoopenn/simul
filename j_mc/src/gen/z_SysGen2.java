package gen;
import anal.Anal;
import anal.AnalEDF_VD_IMC;
import util.SLog;
import util.SEngineT;

public class z_SysGen2 {
//	public static int idx=1;
	public static int idx=2;
	public static int log_level=1;

	// gen
	public int test1() 
	{
		String path="adm/sim/";
		int stage=3;
		ConfigGen cfg=ConfigGen.load(path+"cfg_0.txt");
		String fn=cfg.get_fn();
		Anal a=null;

		SysGen sg=SysGen.load(cfg,stage);
		int num=sg.getNum();
		sg.gen(path+fn, a,num);
		SLog.prn( "OK "+num);
		return 0;
	}

	// gen with schedulable 
	public int test2() 
	{
		String path="adm/sim/";
		int stage=3;
		ConfigGen cfg=ConfigGen.load(path+"cfg_0.txt");
		String fn=cfg.get_fn();
		Anal a=new AnalEDF_VD_IMC();

		SysGen sg=SysGen.load(cfg,stage);
		sg.setSch();
		sg.setOnlyMC();
		int num=sg.getNum();
		sg.gen(path+fn, a,num);
		SLog.prn( "OK "+num);
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
		Class c = z_SysGen2.class;
		z_SysGen2 m=new z_SysGen2();
		int[] aret=z_SysGen2.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}
	
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};

}

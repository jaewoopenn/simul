package gen;
import gen.ConfigGen;
import gen.SysGen;
import gen.SysGenTM;
import util.FUtil;
import util.TEngine;

public class z_SysGen1 {
	public static int idx=2;
	public static int log_level=1;
	public static int total=10;
	public static int gret[]={1,0,0,1,1, 1,0,0,0,0};
	public int test1() // gen
	{
//		ConfigGen cfg=new ConfigGen("config/cfg1_copy.txt");
		ConfigGen cfg=new ConfigGen("test/t1/cfg_0.txt");
		cfg.readFile();
		SysGen eg=new SysGenTM(cfg);
		String fn=eg.get_fn();
		eg.gen(fn);
		return 1;

	}
	public int test2() 
	{
		String path="test/t1/";
		FUtil fu=new FUtil(path+"a_cfg_list.txt");
		FUtil fu_ts=new FUtil(path+"a_ts_list.txt");
		FUtil fu_rs=new FUtil(path+"a_x_list.txt");
		fu.load();
//		int n=fu.load();
//		Log.prn(1, n+" ");
		for(int i=0;i<fu.size();i++) {
			ConfigGen cfg=new ConfigGen(fu.get(i));
			cfg.readFile();
			SysGen eg=new SysGenTM(cfg);
			String fn=eg.get_fn();
			eg.gen(fn);
			fu_ts.write(fn);
			String mod=eg.get_mod();
			fu_rs.write(mod);
		}
		fu_ts.save();
		fu_rs.save();

		return 0;
	}
	public int test3() // load one
	{
		return 0;
	}
	public  int test4() // load copy\
	{
		return 0;
	}
	public  int test5() //
	{
		return 0;
	}
	public  int test6() // 
	{
		return 0;
	}
	public  int test7()
	{
		return 0;
	}
	public  int test8()
	{
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
		Class c = z_SysGen1.class;
		z_SysGen1 m=new z_SysGen1();
		int[] aret=z_SysGen1.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}

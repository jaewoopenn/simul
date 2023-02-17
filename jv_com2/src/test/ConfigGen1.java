package test;
import gen.ConfigGen;
import util.SLog;
import util.SEngineT;

public class ConfigGen1 {
	public static int log_level=2;
//	public static int idx=-1;
	public static int idx=6;
	public static int gret[]={0,1,0,9,1, 1,0,0,0,0};
	public int test1() // error config
	{
		ConfigGen eg=new ConfigGen("config/err_cfg1.txt");
		eg.readFile();
		return 0;
	}
	public int test2() // normal config
	{
		ConfigGen eg=new ConfigGen("config/err_cfg2.txt");
		eg.readFile();
		return 0;
	}
	public int test3() // print config
	{
		ConfigGen eg=new ConfigGen("config/cfg1.txt");
		eg.readFile();
		String s=eg.readPar("util_err");
		if(s==null) 
			return 0;
		System.out.println(s);
		return 1;
	}
	public  int test4() // get config
	{
		ConfigGen eg=new ConfigGen("config/cfg1.txt");
		eg.readFile();
		String s=eg.readPar("u_lb");
		System.out.println(s);
		return (int)(Double.valueOf(s).doubleValue()*10);
	}
	public  int test5() // write config
	{
		ConfigGen eg=getCfg();
		eg.write("config/cfg1_copy.txt");
		return 1;
	}
	public  int test6() // test config
	{
		ConfigGen eg=getCfg();
		eg.setParam("subfix", "drop");
		eg.setParam("num","500");
		int base=50;
		for(int i=0;i<10;i++){
			int lb=i*5+base;
			SLog.prn(2, lb+"");
			eg.setParam("u_lb", (lb)*1.0/100+"");
			eg.setParam("u_ub", (lb+5)*1.0/100+"");
			eg.setParam("mod", (lb+5)+"");
			eg.write("cfg/cfgd_"+i+".txt");
		}
		return 1;
	}
	public  int test7()
	{
		ConfigGen eg=getCfg();
		eg.setParam("subfix", "util");
		eg.setParam("num","10");
		eg.genRange("cfg/cfg",50,5,10);
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
	public ConfigGen getCfg()	{
		ConfigGen eg=new ConfigGen("");
		eg.setParam("u_lb","0.95");
		eg.setParam("u_ub","1.0");
		eg.setParam("p_lb","50");
		eg.setParam("p_ub","300");
		eg.setParam("tu_lb","0.02");
		eg.setParam("tu_ub","0.1");
		eg.setParam("r_lb","0.05");
		eg.setParam("r_ub","1.0");
		eg.setParam("prob_hi","0.5");
		eg.setParam("num","10");
		eg.setParam("subfix","test");
		eg.setParam("mod","t");
		return eg;

	}
	
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws Exception {
		Class c = ConfigGen1.class;
		ConfigGen1 m=new ConfigGen1();
		int[] aret=ConfigGen1.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}

}

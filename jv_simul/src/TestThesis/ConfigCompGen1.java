package TestThesis;
import Util.Log;
import Util.TEngine;
import Simul.ConfigCompGen;
import Simul.ConfigGen;

public class ConfigCompGen1 {
	public static int log_level=2;
//	public static int idx=-1;
	public static int idx=1;
//	public static int cpus=2;
//	public static int cpus=4;
	public static int cpus=8;
	
	public static int total=10;
	public static int gret[]={1,1,1,0,0, 0,0,0,0,0};
	public int test1()
	{
		ConfigCompGen eg=getCfg();
		eg.setParam("subfix", "com/mp");
		eg.setParam("num","1000");
//		eg.setParam("num","400");
		eg.setParam("cpus",cpus+"");
//		eg.setParam("num","2");
		int base=50;
		for(int i=0;i<10;i++){
			int lb=i*5+base;
			Log.prn(2, lb+"");
			eg.setParam("u_lb", (lb)*1.0/100+"");
			eg.setParam("u_ub", (lb+5)*1.0/100+"");
			eg.setParam("mod", (lb+5)+"");
			eg.write("com/cfg/mp_"+cpus+"_"+i+".txt");
		}
		return 1;
	}
	public int test2() 
	{
		ConfigCompGen eg=new ConfigCompGen("com/cfg/mp_2_1.txt");
		eg.readFile();
		String s=eg.readPar("u_ub");
		System.out.println(s);
		return 1;
	}
	public int test3() // print config
	{
		return 1;
	}
	public  int test4() // get config
	{
		return 0;
	}
	public  int test5() // write config
	{
		return 0;
	}
	public  int test6() // test config
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
	public ConfigCompGen getCfg()	{
		ConfigCompGen eg=new ConfigCompGen("");
		eg.setParam("u_lb","0.95");
		eg.setParam("u_ub","1.0");
		eg.setParam("lt_lu_lb","0.02");
		eg.setParam("lt_lu_ub","0.2");
		eg.setParam("ht_lu_lb","0.02");
		eg.setParam("ht_lu_ub","0.2");
		eg.setParam("r_lb","1.0");
		eg.setParam("r_ub","4.0");
		eg.setParam("num","10");
		eg.setParam("subfix","test");
		eg.setParam("mod","t");
		return eg;

	}
	
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws Exception {
		Class c = ConfigCompGen1.class;
		ConfigCompGen1 m=new ConfigCompGen1();
		int[] aret=ConfigCompGen1.gret;
		int sz=ConfigCompGen1.total;
		if(idx==-1)
			TEngine.run(m,c,aret,sz);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}

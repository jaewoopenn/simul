package testTaskSet;
import simul.ConfigGen;
import utilSim.Log;
import utilSim.TEngine;

public class ConfigWrite1 {
	public static int log_level=1;
//	public static int idx=-1;
	public static int idx=1;
	public static int gret[]={0,1,0,9,1, 1,0,0,0,0};
	public int test1() 
	{
		ConfigGen eg=getCfg();
		eg.setParam("subfix", "exp/ts");
		eg.setParam("num","10");
		eg.setParam("u_lb", "0.45");
		eg.setParam("u_ub", "0.50");
		eg.setParam("mod", "50");
		eg.write("exp/cfg/cfg_50.txt");
		return 0;
	}
	public int test2() 
	{
		ConfigGen eg=getCfg();
		eg.setParam("subfix", "drop");
		eg.setParam("num","500");
		int base=50;
		for(int i=0;i<10;i++){
			int lb=i*5+base;
			Log.prn(2, lb+"");
			eg.setParam("u_lb", (lb)*1.0/100+"");
			eg.setParam("u_ub", (lb+5)*1.0/100+"");
			eg.setParam("mod", (lb+5)+"");
			eg.write("cfg/cfgd_"+i+".txt");
		}
		return 1;
	}
	public int test3() // print config
	{
		ConfigGen eg=getCfg();
		eg.setParam("subfix", "exp/ts");
		eg.setParam("num","10");
		eg.genRange("exp/cfg/cfg",50,5,10);
		return 0;
	}
	public  int test4() // get config
	{
		int lb=40;
		ConfigGen eg=getCfg();
		eg.setParam("subfix", "exp/ts");
		eg.setParam("num","1");
		eg.setParam("u_lb", (lb)*1.0/100+"");
		eg.setParam("u_ub", (lb+5)*1.0/100+"");
		eg.setParam("mod", (lb+5)+"");
		eg.write("exp/cfg/cfg.txt");
		return 0;
	}
	public  int test5() // write config
	{
		return 0;
	}
	public  int test6() 
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
	public ConfigGen getCfg()	{
		ConfigGen eg=new ConfigGen("");
		eg.setParam("u_lb","0.95");
		eg.setParam("u_ub","1.0");
		eg.setParam("p_lb","50");
		eg.setParam("p_ub","300");
		eg.setParam("tu_lb","0.02");
		eg.setParam("tu_ub","0.1");
		eg.setParam("r_lb","0.25");
		eg.setParam("r_ub","1.0");
		eg.setParam("prob_hi","0.5");
		eg.setParam("num","10");
		eg.setParam("subfix","exp");
		eg.setParam("mod","t");
		return eg;
	}
	
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws Exception {
		Class c = ConfigWrite1.class;
		ConfigWrite1 m=new ConfigWrite1();
		int[] aret=ConfigWrite1.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}

package TestTaskMode;
import Util.Log;
import Util.TEngine;
import Simul.ConfigGen;

public class ConfigGen1 {
	public static int log_level=2;
//	public static int idx=-1;
	public static int idx=1;
	public static int total=10;
	public static int gret[]={1,1,1,0,0, 0,0,0,0,0};
	public int test1() // error config
	{
		ConfigGen eg=getCfg();
		eg.setParam("subfix", "tm/task");
		eg.setParam("num","1000");
		int base=50;
		for(int i=0;i<10;i++){
			int lb=i*5+base;
			Log.prn(2, lb+"");
			eg.setParam("u_lb", (lb)*1.0/100+"");
			eg.setParam("u_ub", (lb+5)*1.0/100+"");
			eg.setParam("mod", (lb+5)+"");
			eg.write("tm/cfg/cfg_"+i+".txt");
		}
		return 1;
	}
	public int test2() 
	{
		ConfigGen eg=getCfg();
		eg.setParam("subfix", "tm/drop");
		eg.setParam("num","1000");
		eg.setParam("u_lb", "0.7");
		eg.setParam("u_ub", "0.75");
		eg.setParam("mod", "70");
		eg.write("tm/cfg/drop_70.txt");
		return 1;
	}
	public int test3() // print config
	{
		ConfigGen eg=getCfg();
		eg.setParam("subfix", "tm/drop");
		eg.setParam("num","1000");
		eg.setParam("u_lb", "0.9");
		eg.setParam("u_ub", "0.95");
		eg.setParam("mod", "90");
		eg.write("tm/cfg/drop_90.txt");
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
	public ConfigGen getCfg()	{
		ConfigGen eg=new ConfigGen();
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
		int sz=ConfigGen1.total;
		if(idx==-1)
			TEngine.run(m,c,aret,sz);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}

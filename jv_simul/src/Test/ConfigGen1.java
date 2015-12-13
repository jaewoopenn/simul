package Test;
import Util.Log;
import Util.TEngine;
import Simul.ConfigGen;

public class ConfigGen1 {
	public static int log_level=2;
//	public static int idx=-1;
	public static int idx=6;
	public static int total=10;
	public static int gret[]={0,1,0,9,1, 1,0,0,0,0};
	public int test1() // error config
	{
		ConfigGen eg=new ConfigGen();
		return eg.readFile("config/err_cfg1.txt");
	}
	public int test2() // normal config
	{
		ConfigGen eg=new ConfigGen();

		return eg.readFile("config/err_cfg2.txt");
	}
	public int test3() // print config
	{
		ConfigGen eg=new ConfigGen();
		eg.readFile("config/cfg1.txt");
		String s=eg.readPar("util_err");
		if(s==null) 
			return 0;
		System.out.println(s);
		return 1;
	}
	public  int test4() // get config
	{
		ConfigGen eg=new ConfigGen();
		eg.readFile("config/cfg1.txt");
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
		eg.setParam("subfix", "util");
		eg.setParam("num","300");
		for(int i=0;i<10;i++){
			eg.setParam("u_lb", (i*5+50)*1.0/100+"");
			eg.setParam("u_ub", (i*5+55)*1.0/100+"");
			eg.setParam("mod", (i*5+55)+"");
			eg.write("cfg/cfg_"+i+".txt");
		}
		return 1;
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

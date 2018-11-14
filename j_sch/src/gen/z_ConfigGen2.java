package gen;
import gen.ConfigGen;
import util.FUtil;
import util.Log;
import util.TEngine;

public class z_ConfigGen2 {
	public static int idx=1;
	public static int log_level=1;
	public static int gret[]={0,1,0,9,1, 1,0,0,0,0};
	public int test1() // error config
	{
		ConfigGen eg=getCfg();
		String path="test/t1";
		FUtil fu=new FUtil(path+"/a_cfg_list.txt");
		eg.setParam("subfix", path);
		eg.setParam("num","10");
		int base=50;
		for(int i=0;i<10;i++){
			int lb=i*5+base;
			Log.prn(2, lb+"");
			eg.setParam("u_lb", (lb)*1.0/100+"");
			eg.setParam("u_ub", (lb+5)*1.0/100+"");
			eg.setParam("mod", (lb+5)+"");
			String fn=path+"/cfg_"+i+".txt";
			eg.write(fn);
			fu.write(fn);
		}
		fu.save();
		return 1;
	}
	public int test2() 
	{
		String path="test/t1";
		FUtil fu=new FUtil(path+"/list.txt");
		int n=fu.load();
		Log.prn(1, n+" ");
		fu.view();
		return 0;
	}
	public int test3() 
	{
		return 0;
	}
	public  int test4() 
	{
		return 0;
	}
	public  int test5() 
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
		Class c = z_ConfigGen2.class;
		z_ConfigGen2 m=new z_ConfigGen2();
		int[] aret=z_ConfigGen2.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}

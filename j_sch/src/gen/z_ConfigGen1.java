package gen;
import gen.ConfigGen;
import util.SLog;
import util.SEngineT;

public class z_ConfigGen1 {
	public static int log_level=2;
	public static int idx=5;
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
		ConfigGen eg=ConfigGen.getPredefined();
		eg.setFile("config/cfg1_copy.txt");
		eg.write();
		return 1;
	}
	public  int test6() // test config
	{
		ConfigGen eg=ConfigGen.getPredefined();
		eg.setParam("subfix", "drop");
		eg.setParam("num","500");
		int base=50;
		for(int i=0;i<10;i++){
			int lb=i*5+base;
			SLog.prn(2, lb+"");
			eg.setParam("u_lb", (lb)*1.0/100+"");
			eg.setParam("u_ub", (lb+5)*1.0/100+"");
			eg.setParam("mod", (lb+5)+"");
			eg.setFile("cfg/cfgd_"+i+".txt");
			eg.write();
		}
		return 1;
	}
	public  int test7()
	{
		ConfigGen eg=ConfigGen.getPredefined();
		eg.setParam("subfix", "util");
		eg.setParam("num","10");
//		eg.genRange("cfg/cfg",50,5,10);
		return 0;
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
		Class c = z_ConfigGen1.class;
		z_ConfigGen1 m=new z_ConfigGen1();
		int[] aret=z_ConfigGen1.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}
	
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};

}

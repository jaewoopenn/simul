package auto;
// real use --> z_auto1 
// this file is developing version



import util.SEngineT;

public class z_HPlatform1 {
//	public static int idx=1;
//	public static int idx=2;
	public static int idx=3;
//	public static int idx=4;
//	public static int log_level=1;
//	public static int log_level=2;
	public static int log_level=3;

	private String g_path="com/t2";
	
	// gen cfg
	public int test1() 
	{
		String cl="a_cfg_list.txt";
		HPlatform p=new HPlatform(g_path);
		p.setNum(10);
		p.genCfg_util(30,70,5,cl);
		return -1;
	}

	// gen task set (
	public int test2() {
		String cl="a_cfg_list.txt";
		String ts="a_ts_list.txt";
		String xl="a_x_list.txt";
		HPlatform p=new HPlatform(g_path);
		p.genTS(cl,ts,xl);		
		return -1;
	}
	
	public int test3() 
	{
		String ts="a_ts_list.txt";
		HPlatform p=new HPlatform(g_path);
		p.anal(ts,0);
		return -1;		
	}
	
	public  int test4() {

		return -1;
		
	}
	
	public  int test5() 
	{
		return -1;
	}
	public  int test6() 
	{
		return -1;
	}
	
	public  int test7()
	{
		return 1;
	}
	public  int test8()
	{
		String path="test/t1/";
		String cf="a_cfg_list.txt";
		Platform p=new Platform(path);
		p.genCfg_util(25,70,5,cf);
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
		Class c = z_HPlatform1.class;
		z_HPlatform1 m=new z_HPlatform1();
		int[] aret=z_HPlatform1.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}
	
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};

}

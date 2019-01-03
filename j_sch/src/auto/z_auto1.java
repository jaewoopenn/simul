package auto;


import util.TEngine;

public class z_auto1 {
//	public static int idx=1;
//	public static int idx=2;
//	public static int idx=3;
	public static int idx=4;
	public static int log_level=1;

	private String g_path="sch/t1";
	
	public int test1() 
	{
		String cf="a_cfg_list.txt";
		Platform p=new Platform(g_path);
		p.setNum(3);
		p.genUtil(cf);
		return 0;
	}
	public int test2() 
	{
		String cl="a_cfg_list.txt";
		Platform p=new Platform(g_path);
		p.genTS(cl);
		return -1;	}
	public int test3() 
	{
		String ts="a_ts_list.txt";
		String rs="a_rs_list.txt";
		Platform p=new Platform(g_path);
		int end=3;
		p.anal_loop(rs,ts,end);
		return -1;
	}
	public  int test4() 
	{
		String xl="a_x_list.txt";
		String rs="a_rs_list.txt";
		DataAnal p=new DataAnal(g_path,0);
		p.load_x(xl);
		p.load_rs(rs);
		p.save("a_graph.txt");
		return -1;
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
	
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws Exception {
		Class c = z_auto1.class;
		z_auto1 m=new z_auto1();
		int[] aret=z_auto1.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}
	
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};

}

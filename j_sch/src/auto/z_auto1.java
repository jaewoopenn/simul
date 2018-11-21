package auto;


import util.TEngine;

public class z_auto1 {
	public static int idx=4;
	public static int log_level=1;

	public int test1() 
	{
		String path="test/t1/";
		String cf="a_cfg_list.txt";
		Platform p=new Platform(path);
		p.genConfig(cf);
		return 0;
	}
	public int test2() 
	{
		String path="test/t1/";
		String cl="a_cfg_list.txt";
		Platform p=new Platform(path);
		p.genTS(cl);
		return -1;	}
	public int test3() 
	{
		String path="test/t1/";
		String ts="a_ts_list.txt";
		Platform p=new Platform(path);
		p.anal(ts,0);
		p.anal(ts,1);
		return -1;
	}
	public  int test4() 
	{
		String path="test/t1/";
		String xl="a_x_list.txt";
		String rs1="a_rs_list.0.txt";
		String rs2="a_rs_list.1.txt";
		DataAnal p=new DataAnal(path,2);
		p.load_x(xl);
		p.load_rs(rs1,0);
		p.load_rs(rs2,1);
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

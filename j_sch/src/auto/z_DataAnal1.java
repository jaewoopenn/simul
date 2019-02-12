package auto;


import util.S_TEngine;

public class z_DataAnal1 {
	public static int idx=2;
	public static int log_level=1;

	public int test1() 
	{
		String path="test/t1/";
		String xl="a_x_list.txt";
		DataAnal p=new DataAnal(path,1);
		p.load_x(xl);
		return -1;
	}

	public int test2() 
	{
		String path="test/t1/";
		String xl="a_x_list.txt";
		String rs1="a_rs_list.0.txt";
		String rs2="a_rs_list.1.txt";
		DataAnal p=new DataAnal(path,2);
		p.load_x(xl);
		p.load(rs1,0);
		p.load(rs2,1);
		p.save("a_graph.txt");
		return -1;
	}
	
	public int test3() 
	{
		return -1;		
	}
	
	public  int test4() 
	{
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
		Class c = z_DataAnal1.class;
		z_DataAnal1 m=new z_DataAnal1();
		int[] aret=z_DataAnal1.gret;
		if(idx==-1)
			S_TEngine.run(m,c,aret,10);
		else
			S_TEngine.runOnce(m,c,aret,idx,log_level);
	}
	
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};

}

package auto;


import util.TEngine;

public class z_DataAnal1 {
	public static int idx=1;
	public static int log_level=1;

	public int test1() 
	{
		String path="test/t1/";
		String cl="a_x_list.txt";
		DataAnal p=new DataAnal(path);
		p.load_x(cl);
		return -1;
	}

	public int test2() 
	{
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
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}
	
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};

}

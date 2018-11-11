package auto;


import util.TEngine;

public class z_Platform1 {
	public static int idx=1;
	public static int log_level=1;

	public int test1() 
	{
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
		Class c = z_Platform1.class;
		z_Platform1 m=new z_Platform1();
		int[] aret=z_Platform1.gret;
		if(idx==-1)
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}
	
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};

}

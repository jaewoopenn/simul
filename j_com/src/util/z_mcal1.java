package util;



public class z_mcal1 {
//	public static int idx=1;
//	public static int idx=2;
	public static int idx=3;
	public static int log_level=1;
//	public static int log_level=2;

	
	public int test1()
	{
		int a=10;
		int b=14;
		SLog.prn(1, "lcm:"+MCal.lcm(a,b));
		return 0;
	}
	public int test2() 
	{
		int a=10;
		int b=14;
		long c=MCal.lcm(a, b);
		SLog.prn(1, "lcm:"+MCal.lcm(c, 15));
		return 0;
	}
	public  int test3()
	{
		int a[]= {10,14,15};
		SLog.prn(1, "lcm:"+MCal.lcm(a));
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
	public  int test6() {
		return 1;
	}
	public  int test7() {
		return 1;
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
		Class c = z_mcal1.class;
		z_mcal1 m=new z_mcal1();
		int[] aret=z_mcal1.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}
	
	public static int gret[]={0,0,0,0,0,0,0,0,0,0};

}

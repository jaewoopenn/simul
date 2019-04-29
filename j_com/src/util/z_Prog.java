package util;

/*
 * TODO apply progressor into z_auto
 */

public class z_Prog {
	public static int idx=1;
	public static int log_level=1;
	public static int total=10;
	public static int gret[]={1,0,0,1,1, 1,0,0,0,0};
	public int test1() // 
	{
		Progressor p=new Progressor(2000);
		p.setPercent();
//		p.setStep(2);
		for(int i=0;i<2000;i++) {
			p.inc();
//			p.prn();
		}
		return 1;

	}
	public int test2() // 
	{
		return 0;
	}
	public int test3() // 
	{
		return 0;
	}
	public  int test4() // 
	{
		return 0;
	}
	public  int test5() //
	{
		return 0;
	}
	public  int test6() // 
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
		Class c = z_Prog.class;
		z_Prog m=new z_Prog();
		int[] aret=z_Prog.gret;
		if(idx==-1)
			S_TEngine.run(m,c,aret,10);
		else
			S_TEngine.runOnce(m,c,aret,idx,log_level);
	}

}

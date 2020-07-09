package auto;

import task.CompSet;
import util.SEngineT;

public class z_HSysLoad1 {
	public static void init_s() {
//		int s=1;
		int s=2;
//		int s=3;
//		int s=4;
//		int s=5;
		
		int log=1;
//		int log=2;
//		int log=3;
		
		s_idx=s;
		s_log_level=log;
	}
	
	public int test1() 
	{
		HSysLoad sy=new HSysLoad("com/test1.txt");
		sy.open();
		CompSet cs=sy.loadOne();
		cs.prn();
		return 1;

	}
	public int test2() // load
	{
		HSysLoad sy=new HSysLoad("com/test1.txt");
		sy.open();
		CompSet cs;
		while((cs=sy.loadOne())!=null) {
			cs.prn();
		}
		return 0;
	}
	public int test3() // load one
	{
		return 1;
	}
	public  int test4() {
		return 1;
	}
	public  int test5() {
		return 1;
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
		z_HSysLoad1.init_s();
		Class c = z_HSysLoad1.class;
		z_HSysLoad1 m=new z_HSysLoad1();
		int[] aret=z_HSysLoad1.gret;
		if(s_idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,s_idx,s_log_level);
	}
	
	public static int s_idx;
	public static int s_log_level;
	public static int gret[]={1,0,0,1,1, 1,0,0,0,0};

}

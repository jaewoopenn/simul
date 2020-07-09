package gen;
import gen.ConfigGen;
import util.SEngineT;
import util.SLog;

public class z_HSysGen1 {
	public static int idx=1;
//	public static int idx=2;
	public static int log_level=1;

	// gen
	public int test1() 
	{
		ConfigGen cfg=ConfigGen.getHSample();

		HSysGen eg=new HSysGen(cfg);
		eg.prepare();
//		eg.gen();
		return 1;

	}

	// gen with schedulable 
	public int test2() {
		ConfigGen cfg=ConfigGen.getHSample();
		SLog.prn(1,cfg.readDbl("cu_lb"));

		return 0;
	}
	public int test3() {
		return 0;
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
		Class c = z_HSysGen1.class;
		z_HSysGen1 m=new z_HSysGen1();
		int[] aret=z_HSysGen1.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}
	public static int gret[]={1,0,0,1,1, 1,0,0,0,0};

}

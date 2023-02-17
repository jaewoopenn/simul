package util;

public class z_MFile {
	public static int idx=1;
//	public static int idx=2;
	public static int log_level=1;
	public int test1()
	{
		String f="fc/ts/util_65/taskset_73";
	    MList fu=new MList(f);
	    fu.prn();
		return -1;
	}
	public int test2() //vd
	{
		SLog.prn(2, "hihi");
		return -1;
	}
	public  int test3()
	{
		return -1;
	}
	public  int test4()
	{
		return -1;
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
		Class c = z_MFile.class;
		z_MFile m=new z_MFile();
		int[] aret=z_MFile.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
}
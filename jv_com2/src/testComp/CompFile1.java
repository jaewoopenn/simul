package testComp;
import comp.CompFile;
import comp.CompMng;
import util.SEngineT;
import z_ex.CompMngEx1;

public class CompFile1 {
	public static int idx=1;
//	public static int idx=2;
//	public static int idx=3;
//	public static int idx=4;
	public static int gret[]={-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
	public static int log_level=1;
	public int test1()
	{
		CompMng cm=CompMngEx1.getCompMng1();
		cm.prn();
		CompFile.writeFile("fc/test/testCom.txt",cm.getComps());
		return 0;
	}
	public int test2()
	{
		CompMng cm=CompFile.loadFile("file/testCom.txt");
//		if(cm!=null)
		cm.prn();
		return 0;
	}
	public  int test3()
	{
		CompMng cm=CompMngEx1.getCompMng2();
		cm.prn();
		CompFile.writeFile("file/testCom.txt",cm.getComps());
		return 0;
	}
	public  int test4()
	{
		String f="com/ts/taskset_util_80_2";
		CompMng cm=CompFile.loadFile(f);
		cm.part();
		cm.prn();
		return 0;
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
		Class c = CompFile1.class;
		CompFile1 m=new CompFile1();
		int[] aret=CompFile1.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}

}

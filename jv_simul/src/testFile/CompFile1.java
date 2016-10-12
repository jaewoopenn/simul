package testFile;
import comp.CompFile;
import comp.CompMng;
import taskSetEx.CompMngEx1;
import utilSim.TEngine;

public class CompFile1 {
	public static int idx=4;
//	public static int idx=-1;
	public static int gret[]={-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
	public static int log_level=1;
	public int test1()
	{
		CompMng cm=CompMngEx1.getCompMng1();
		cm.prn();
		CompFile.writeFile("file/testCom.txt",cm.getComps());
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
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}

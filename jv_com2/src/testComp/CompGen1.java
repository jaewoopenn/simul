package testComp;


import gen.TaskGenParam;
import comp.CompFile;
import comp.CompGen;
import comp.CompGenParam;
import comp.CompMng;
import util.SEngineT;

public class CompGen1 {
	public static int idx=1;
//	public static int idx=2;
//	public static int idx=-1;
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
	public static int log_level=1;
	public int test1() 
	{
		TaskGenParam tgp=TaskGenParam.getDefault();
		CompGenParam cgp=new CompGenParam();
		cgp.setUtil(0.75,0.8);
		cgp.setCUtil(0.1,0.3);
		CompGen c=new CompGen(cgp,tgp);
		CompMng cm=c.generate();
		cm.prn();
		return 0;
	}
	public int test2() 
	{
		TaskGenParam tgp=TaskGenParam.getDefault();
		CompGenParam cgp=new CompGenParam();
		cgp.setUtil(0.75,0.8);
		cgp.setCUtil(0.1,0.3);
		CompGen c=new CompGen(cgp,tgp);
		CompMng cm=c.generate();
		if(c.check(cm)!=0){
			CompFile.writeFile("file/testCom.txt",cm.getComps());
		}
		return 0;
	}
	public int test3() 
	{
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
		Class c = CompGen1.class;
		CompGen1 m=new CompGen1();
		int[] aret=CompGen1.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}

}

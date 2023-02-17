package test;

import util.SLog;
import util.MList;
import util.SEngineT;

public class Calcul2 {
	public static int log_level=1;
//	public static int idx=-1;
	public static int idx=1;
	public static int gret[]={1,1,1,-1,-1, -1,-1,-1,-1,-1};
	public int test1() 
	{
		int size=5;
		int lim=(int)Math.pow(2,size);
		for(int i=0;i<lim;i++){
			
			//Log.prn(1, "n:"+i+","+Integer.toBinaryString(i));
			for(int j=0;j<size;j++){
				int v=(i&(1<<j))>>j;
				//Log.prn(1, "s:"+j+","+v);
				if (v==0)
					SLog.prnc(1, "- ");
				else
					SLog.prnc(1, "+ ");
			}
			SLog.prn(1, "");
		}
		return 1;
	}
	public int test2() 
	{
		MList fu=new MList();
		for(int i=1;i<11;i++){
			double v=1-Math.pow(1-0.05*i,5);
			fu.add(v+"");
		}
		fu.save("graph/test2.txt");
		return 1;
	}
	public int test3() 
	{
		MList fu=new MList();
		for(int i=1;i<11;i++){
			fu.add((0.05*i)+"");
		}
		fu.save("graph/label.txt");
		return 1;
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
		Class c = Calcul2.class;
		Calcul2 m=new Calcul2();
		int[] aret=Calcul2.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}

}

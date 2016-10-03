package testExp;

import utilSim.FUtil;
import utilSim.Log;
import utilSim.TEngine;

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
					Log.prnc(1, "- ");
				else
					Log.prnc(1, "+ ");
			}
			Log.prn(1, "");
		}
		return 1;
	}
	public int test2() 
	{
		FUtil fu=new FUtil("graph/test2.txt");
		for(int i=1;i<11;i++){
			double v=1-Math.pow(1-0.05*i,5);
			fu.print(v+"");
		}
		fu.save();
		return 1;
	}
	public int test3() 
	{
		FUtil fu=new FUtil("graph/label.txt");
		for(int i=1;i<11;i++){
			fu.print((0.05*i)+"");
		}
		fu.save();
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
			TEngine.run(m,c,aret,10);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}

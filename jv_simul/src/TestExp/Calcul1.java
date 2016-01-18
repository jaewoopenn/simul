package TestExp;

import Util.FUtil;
import Util.TEngine;

public class Calcul1 {
	public static int log_level=1;
//	public static int idx=-1;
	public static int idx=3;
	public static int total=10;
	public static int gret[]={1,1,1,-1,-1, -1,-1,-1,-1,-1};
	public int test1() 
	{
		FUtil fu=new FUtil("graph/test1.txt");
		for(int i=1;i<11;i++){
			double v=1-Math.pow(1-0.04*i,5);
			fu.print(v+"");
		}
		fu.save();
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
		Class c = Calcul1.class;
		Calcul1 m=new Calcul1();
		int[] aret=Calcul1.gret;
		int sz=Calcul1.total;
		if(idx==-1)
			TEngine.run(m,c,aret,sz);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}

package test;

import util.MList;
import util.SEngineT;

public class Calcul1 {
	public static int log_level=1;
//	public static int idx=-1;
	public static int idx=3;
	public static int gret[]={1,1,1,-1,-1, -1,-1,-1,-1,-1};
	public int test1() 
	{
		MList fu=new MList();
		for(int i=1;i<11;i++){
			double v=1-Math.pow(1-0.04*i,5);
			fu.add(v+"");
		}
		fu.save("graph/test1.txt");
		return 1;
	}
	public int test2() 
	{
		MList fu=new MList();
		for(int i=1;i<11;i++){
			double v=1-Math.pow(1-0.05*i,5);
			fu.add(v+"");
		}
		fu.add("graph/test2.txt");
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
		Class c = Calcul1.class;
		Calcul1 m=new Calcul1();
		int[] aret=Calcul1.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}

}

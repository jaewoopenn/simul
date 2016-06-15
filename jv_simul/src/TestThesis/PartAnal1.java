package TestThesis;

import Util.Log;
import Util.TEngine;
import Basic.Comp;
import Simul.CompMng;
import Simul.PartAnal;

public class PartAnal1 {
	public static int log_level=2;
//	public static int idx=-1;
	public static int idx=3;
	public static int total=10;
	public static int gret[]={0,1,1,1,0,1,0,0,0,0};

	public CompMng getComp1()
	{
		CompMng cm=new CompMng();

		cm.addComp(new Comp(0, 1.0/8, 1.0/12, 4.0/12));
		cm.addComp(new Comp(1, 1.0/9, 1.0/12, 5.0/12));
		cm.addComp(new Comp(2, 1.0/6, 1.0/13, 2.0/13));
		cm.addComp(new Comp(3, 1.0/9, 1.0/14, 3.0/14));
		cm.addComp(new Comp(4, 1.0/6, 1.0/10, 3.0/10));
		
		return cm;
	}


	
	public int test1() 
	{
		CompMng cm=getComp1();
		cm.sortMC();
		PartAnal a=new PartAnal(cm,2);
		a.part_help();
		return -1;
	}

	public CompMng getComp2()
	{
		CompMng cm=new CompMng();

		cm.addComp(new Comp(0, 0.01, 0.01, 0.10));
		cm.addComp(new Comp(1, 0.04, 0.02, 0.20));
		cm.addComp(new Comp(2, 0.03, 0.04, 0.10));
		cm.addComp(new Comp(3, 0.02, 0.03, 0.30));
		cm.addComp(new Comp(4, 0.1, 0.01, 0.04));
		cm.addComp(new Comp(5, 0.2, 0.02, 0.04));
		cm.addComp(new Comp(6, 0.4, 0.01, 0.03));
		cm.addComp(new Comp(7, 0.3, 0.02, 0.05));
		return cm;
	}

	public int test2() 
	{
		CompMng cm=getComp2();
//		cm.sortMC();
		cm.sortHI();
//		cm.sortLO();
		PartAnal a=new PartAnal(cm,2);
		a.part_help();
		return -1;
	}

	public int test3() 
	{
		// TODO partition 
		CompMng cm=getComp2();
		cm.sortMC();
		PartAnal a=new PartAnal(cm,2);
		a.help1();
		a.partitionWF(0.3);
		a.help2();
		return -1;
	}
	
	public  int test4() 
	{
		return -1;
	}

	public  int test5()
	{
		return -1;
	}
	
	public  int test6() 
	{
		return -1;
	}

	public  int test7() 
	{
		return -1;
	}
	
	public  int test8()
	{
		return -1;
	}
	
	public  int test9()
	{
		return -1;
	}
	
	public  int test10()
	{
		return -1;
	}
	
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws Exception 
	{
		Class c = PartAnal1.class;
		PartAnal1 m=new PartAnal1();
		int[] aret=PartAnal1.gret;
		int sz=PartAnal1.total;
		if(idx==-1)
			TEngine.run(m,c,aret,sz);
		else
			TEngine.runOnce(m,c,aret,idx,log_level);
	}

}
package test;

import java.util.Vector;

import util.SEngineT;
import util.SLog;

public class z_array1 {
	public static int idx=1;
	public static int log_level=1;

	public int test1() 
	{
		SLog.prn(1, "hihi");
		int z[]= {1,2};
		Vector v=new Vector();
		v.add(z);
		z[0]=3;
		z[1]=4;
		v.add(z);
		return -1;
	}

	public int test2() {
		return -1;
	}
	public int test3() {
		return -1;
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
		Class c = z_array1.class;
		z_array1 m=new z_array1();
		int[] aret=z_array1.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}
	
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};

}

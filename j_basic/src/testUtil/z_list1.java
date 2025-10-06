package testUtil;

import util.MFile;
import util.MList;
import util.SEngineT;

public class z_list1 {
	public static int idx=1;
//	public static int idx=2;
//	public static int idx=3;
//	public static int idx=4;
	public static int log_level=1;
//	public static int log_level=2;


	
	public int test1()	{
		MList ml=MList.new_list();
		ml.add("hihi");
		MList ml2=MList.new_list();
		ml2.add("add");
		ml2.add("add2");
		for(String s:ml2.getVec()) {
			ml.add(s);
		}
		ml.prn();
//		ml.saveTo("test/test.txt");
		return 0;
	}
	public int test2() {
		MList ml=MList.load("test/test.txt");
		ml.prn();
		return 0;
	}
	

	public int test3() {
		MFile.makeDir("test/t2");
		return 0;
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
		Class c = z_list1.class;
		z_list1 m=new z_list1();
		int[] aret=z_list1.gret;
		if(idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,idx,log_level);
	}
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
}
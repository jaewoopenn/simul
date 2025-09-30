package test;


import job.JobSys;
import util.MFile;
import util.MStr;
import util.SEngineT;
import util.SLog;

public class file1 {
	public static void init_s() {
//		s_idx=1;
		s_idx=2;
//		s_idx=3;
		
		
		s_log_level=1;
	}

	public int test1() 
	{
		MFile mf=new MFile("ev/test.txt");
		mf.br_open();
		String rs;
		while((rs=mf.read())!=null) {
//			SLog.prn(rs);
			int[] para=MStr.getSplit(rs);
			for(int i:para) {
				SLog.prnc(1, i+",");
			}
			SLog.prn("");
		}
		return -1;
	}

	public int test2() {
		MFile mf=new MFile("ev/test.txt");
		mf.br_open();
		String rs;
		JobSys js=new JobSys();
		int t=0;
		while((rs=mf.read())!=null) {
//			SLog.prn(rs);
			int[] para=MStr.getSplit(rs);
			if(t==para[0]) {
				js.add_repl(para[1],para[2],para[3],para[4]);
			} else {
				js.prn_dbf();
				js.exec(2);
			}
		}
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
		file1.init_s();
		Class c = file1.class;
		file1 m=new file1();
		int[] aret=file1.gret;
		if(s_idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,s_idx,s_log_level);
	}
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
	private static int s_idx;
	private static int s_log_level;

}

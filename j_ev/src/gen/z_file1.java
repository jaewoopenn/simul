package gen;


import java.util.stream.IntStream;

import job.JobSys;
import util.MFile;
import util.MList;
import util.MRand;
import util.MStr;
import util.SEngineT;
import util.SLog;

public class z_file1 {
	public static void init_s() {
//		s_idx=1;
//		s_idx=2;
//		s_idx=3;
		s_idx=4;
//		s_idx=5;
		
		
		s_log_level=1;
	}

	public int test1() 
	{
		MFile mf=new MFile("ev/test2.txt");
		mf.br_open();
		String rs;
		JobSys js=new JobSys();
		int t=0;
		while((rs=mf.read())!=null) {
			SLog.prn(rs);
			int[] para=MStr.getSplit(rs);
			if(t==para[0]) {
				js.add(para[1],para[2],para[3],para[4]);
			} else {
				js.prn_dbf();
				int go=para[0]-t;
				js.exec(go);
				t=para[0];
				js.add(para[1],para[2],para[3],para[4]);
			}
		}
//		js.prn_dbf();
		return -1;
	}

	public int test2() {
		MList ml=MList.new_list();
		int[] para= {0,4,1,1,0};
		String rs=MStr.getMerge(para);
		ml.add(rs);
		
		para= IntStream.of(0, 6, 1, 1, 0).toArray();
		rs=MStr.getMerge(para);
		ml.add(rs);
		
		para= IntStream.of(5, 4, 1, 1, 0).toArray();
		rs=MStr.getMerge(para);
		ml.add(rs);

		ml.saveTo("ev/test2.txt");
		return -1;
	}
	public int test3() {
		MRand mr=MRand.init();
		MList ml=MList.new_list();
		int[] para;
		int t=0;
		String rs;
		for(int i=0;i<10;i++) {
			int dl=mr.getInt(4, 6);
			int m=mr.getInt(1, 2);
			int o=mr.getInt(0, 1);
			int v=mr.getInt(m+o);
			para= IntStream.of(t, dl, m, o, v).toArray();
			if(mr.getBool()) {
				int go=mr.getInt(1,3);
				t+=go;
			}
			rs=MStr.getMerge(para);
			ml.add(rs);
		}
		ml.saveTo("ev/test2.txt");
		return -1;
	}
	public  int test4() {
		MFile.example();
		return 1;
	}
	public  int test5() {
		return -1;
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
		z_file1.init_s();
		Class c = z_file1.class;
		z_file1 m=new z_file1();
		int[] aret=z_file1.gret;
		if(s_idx==-1)
			SEngineT.run(m,c,aret,10);
		else
			SEngineT.runOnce(m,c,aret,s_idx,s_log_level);
	}
	public static int gret[]={-1,-1,-1,-1,-1, -1,-1,-1,-1,-1};
	private static int s_idx;
	private static int s_log_level;

}

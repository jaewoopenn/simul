package gen;

import java.util.stream.IntStream;

import util.MList;
import util.MRand;
import util.MStr;
import util.SLog;

public class JGen {
	MRand mr;
	MList ml;
//	int num;
	boolean prn=false;
	public JGen() {
		mr=MRand.init();
		ml=MList.new_list();
	}
	public static JGen init() {
		JGen jg=new JGen();
		return jg;
	}

	public void run(int n) {
		int[] para;
		int t=0;
		String rs;
		for(int i=0;i<n;i++) {
			int dl=mr.getInt(4, 7);
			int m=mr.getInt(3)+1;
			int o=mr.getInt(2);
			int v=mr.getInt(m+o)+1;
			para= IntStream.of(t, dl, m, o, v).toArray();
			if(prn)
				SLog.prn(2,t+","+dl+","+m+","+o);
			if(mr.getDbl()<=0.7) {
				int go=mr.getInt(1,3);
				t+=go;
			}
			rs=MStr.getMerge(para);
			ml.add(rs);
		}
		
	}
	public void save(String fn) {
		ml.saveTo(fn);
		
	}

}

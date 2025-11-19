package gen;

import java.util.stream.IntStream;

import util.MList;
import util.MRand;
import util.MStr;
import util.SLog;

public class JGen {
	MRand mr;
	MList ml;
	boolean prn=false;
	JPara g_para;
	final String sep="--------";
	public JGen() {
		mr=MRand.init();
		ml=MList.new_list();
	}
	public static JGen init() {
		JGen jg=new JGen();
		return jg;
	}

	public void run(int n) {
		ml.add(n+"");
		for(int i=0;i<n;i++) {
			gen();
			ml.add(sep);
		}
		
	}
	private void gen() {
		int[] para;
		int t=0;
		String rs;
		JPoisson po=new JPoisson(g_para.rate);
		for(int i=0;i<g_para.num;i++) {
			int dl=mr.getInt(g_para.dl_start, g_para.dl_end);
			int m=mr.getInt(g_para.man)+1;
			int o=mr.getInt(g_para.opt);
			int v=mr.getInt(m+o)+1;
			para= IntStream.of(t, dl, m, o, v).toArray();
			if(prn)
				SLog.prn(2,t+","+dl+","+m+","+o);
			t+=Math.round(po.next());
			rs=MStr.getMerge(para);
			ml.add(rs);
		}
	}
	public void save(String fn) {
		ml.saveTo(fn);
		ml=MList.new_list();
		
	}
	public void setPara(JPara para) {
		g_para=para;
	}

}

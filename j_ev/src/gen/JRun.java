package gen;

import job.JobSys;
import util.MFile;
import util.MStr;
import util.SLog;

public class JRun {

	JobSys js;
	MFile mf;
	int g_t;
	public JRun(String fn) {
		mf=new MFile(fn);
		mf.br_open();
		js=new JobSys();
		g_t=0;
	}

	public static JRun init(String fn) {
		JRun jg=new JRun(fn);
		return jg;
	}

	public void start() {
		String rs;
		while((rs=mf.read())!=null) {
			SLog.prn(rs);
			int[] para=MStr.getSplit(rs);
			simul(para);
		}
//		js.prn_dbf();
		
	}

	private void simul(int[] para) {
		if(g_t==para[0]) {
			js.add_repl(para[1],para[2],para[3],para[4]);
			return;
		} 
		
		js.prn_dbf();
		int go=para[0]-g_t;
		js.exec(go);
		g_t=para[0];
		js.add_repl(para[1],para[2],para[3],para[4]);
		
	}

}

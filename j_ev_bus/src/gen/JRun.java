package gen;

import job.JobSys;
import job.JobSys_DEM;
import job.JobSys_FIFO;
import job.SysInfo;
import util.MFile;
import util.MStr;
import util.SLog;

public class JRun {

	JobSys js;
	SysInfo g_si;
	MFile mf;
	int g_t;
	final String sep="--------";
	public JRun(String fn, int sort) {
		mf=new MFile(fn);
		mf.br_open();
		if(sort==0) {
			js=new JobSys_DEM();
		} else if(sort==1) {
			js=new JobSys_FIFO();
		}
		g_si=new SysInfo();
		g_t=0;
	}

	public static JRun init(String fn,int i) {
		JRun jg=new JRun(fn,i);
		return jg;
	}

	public void start(int end) {
		String rs=mf.read();
		int n=Integer.valueOf(rs).intValue();
		for(int i=0;i<n;i++) {
			reset();
			run(end);
		}
	}

	private void reset() {
		g_t=0;
		js.init();
	}

	private void run(int end) {
		String rs;
		while(true) {
			rs=mf.read();
			if(rs.equals(sep))
				break;
//			SLog.prn(rs);
			int[] para=MStr.getSplit(rs);
			simul(para);
		}
		SLog.prn("---");
		if(g_t<end) {
			js.exec(end-g_t);
		}
		g_si.val=js.getVal();
//		g_si.prn();
//		js.prn_dbf();
		
	}

	private void simul(int[] para) {
		if(g_t!=para[0]) {
			int go=para[0]-g_t;
			js.exec(go);
			g_t=para[0];
		}
		g_si.total++;
		
		boolean b=js.add(para[1],para[2]);
		if(b) {
			g_si.suc++;
		}
	}

	public String getRS() {
		return g_si.getSuc()+"";
	}

}

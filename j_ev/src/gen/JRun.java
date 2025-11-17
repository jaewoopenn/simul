package gen;

import job.JobSys;
import job.JobSys_DEM;
import job.JobSys_FIFO;
import job.JobSys_Util;
import job.SysInfo;
import util.MFile;
import util.MStr;
import util.SLog;

public class JRun {

	JobSys js;
	SysInfo g_si;
	MFile mf;
	int g_t;
	public JRun(String fn, int sort) {
		mf=new MFile(fn);
		mf.br_open();
		if(sort==0) {
			js=new JobSys_DEM();
		} else if(sort==1) {
			js=new JobSys_FIFO();
		} else {
			js=new JobSys_Util();
		}
		g_si=new SysInfo();
		g_t=0;
	}

	public static JRun init(String fn,int i) {
		JRun jg=new JRun(fn,i);
		return jg;
	}

	public void start(int end) {
		String rs;
		while((rs=mf.read())!=null) {
//			SLog.prn(rs);
			int[] para=MStr.getSplit(rs);
			simul(para);
		}
		SLog.prn("---");
		if(g_t<end) {
			js.exec(end-g_t);
		}
		g_si.val=js.getVal();
		g_si.prn();
//		js.prn_dbf();
		
	}

	private void simul(int[] para) {
		if(g_t!=para[0]) {
			int go=para[0]-g_t;
			js.exec(go);
			g_t=para[0];
		}
		g_si.total++;
		
		boolean b=js.add(para[1],para[2],para[3],para[4]);
		if(b) {
			g_si.suc++;
		}
	}

}

package auto;

import java.util.Vector;

import task.CompSet;
import task.TaskUtil;
import util.MFile;
import util.MList;
//import util.SLog;

public class HSysLoad {
	private MFile g_fu;
	public HSysLoad(String fn) {
		g_fu=new MFile(fn);
	}


	public String open() {
		g_fu.br_open();
		return g_fu.read();
		
	}
	public CompSet loadOne() {
		boolean b=g_fu.readSplit("------");
		if(!b) 
			return null;
		MList ml=new MList();
		ml.copy(g_fu);
//		ml.prn();
		Vector<MList> mlv=TaskUtil.loadComML(ml);
		CompSet cs=new CompSet(mlv);
		return cs;
	}
}

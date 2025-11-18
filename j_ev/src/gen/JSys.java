package gen;

import util.MFile;
import util.MList;
import util.MLoop;
import util.SLog;

public class JSys {
	JGen jg;
	int num;
	int iter;
	public JSys() {
		jg=JGen.init();
	}
	public static JSys init() {
		return new JSys();
	}

	public void run() {
		MList ml=MList.new_list();
		for(int i:MLoop.on(1,iter)) {
			jg.run(num);
			String fn="ev/test/test"+i+".txt";
			jg.save(fn);
			SLog.prn("------------  "+fn);
			
			SLog.prn("t,dl,m,o,v");
			SLog.prn("------------");
			MFile.prn(fn);
			ml.add(fn);
		}
		ml.saveTo("ev/test/list.txt");
		
	}
	public void setNum(int i) {
		num=i;
	}
	public void setIter(int i) {
		iter=i;
	}
	public void anal() {
		MList ml=MList.load("ev/test/list.txt");
		MList rs_ml=MList.new_list();
		int i=0;
		rs_ml.add("xx Demand FIFO");
		while(true) {
			String s=ml.getNext();
			String rs=i+" ";
			if(s==null)
				break;
			SLog.prn(s);
			JRun jr=JRun.init(s,0);
			jr.start(20);
			rs+=jr.getRS()+" ";
			jr=JRun.init(s,1);
			jr.start(20);
			rs+=jr.getRS();
			rs_ml.add(rs);
			i++;
		}
		rs_ml.saveTo("ev/test/rs.txt");
	}

}

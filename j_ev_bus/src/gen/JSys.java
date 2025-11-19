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
		JPara para=new JPara();
		for(int i:MLoop.on(1,iter)) {
			para.rate=0.2+0.1*i;
			jg.setPara(para);
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
		int i=1;
		rs_ml.add("xx Demand FIFO util");
		while(true) {
			String s=ml.getNext();
			String rs=i+" ";
			if(s==null)
				break;
			SLog.prn(s);
			JRun jr=JRun.init(s,0);
			jr.start(500);
			rs+=jr.getRS()+" ";
			jr=JRun.init(s,1);
			jr.start(500);
			rs+=jr.getRS()+" ";
			jr=JRun.init(s,2);
			jr.start(500);
			rs+=jr.getRS();
			rs_ml.add(rs);
			SLog.prn(2,rs);
			i++;
		}
		rs_ml.saveTo("ev/test/rs.txt");
	}

}

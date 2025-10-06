package gen;

import anal.Anal;
import anal.AnalEDF_VD_IMC;
import task.Task;
import task.TaskMng;
import task.TaskSet;
import task.TSFile;
import util.MList;
import util.MRand;
import util.SLog;

@SuppressWarnings("unused")
public class SysGen {
	private double g_upper=0.1; //스테이지가 변하면서, 최대 추가할수 있는 util은? 
	
	private MRand g_rand=new MRand();
	private TaskGen g_tg;
	private ConfigGen g_cfg;
	private boolean g_isSch=false;
	private boolean g_isOnlyMC=false;
	private int g_stage=-1;
	private int g_num=0;
	public void load_in(ConfigGen cfg,int stage) {
		g_cfg=cfg;
		TaskGenInd_IMC tgp=getTgp();
		g_tg=new TaskGen(tgp);
		g_num=g_cfg.readInt("num");
		g_stage=stage;
	}
	public int getNum() {
		return g_num;
	}
	public void setSch() {
		g_isSch=true;
	}
	public void setOnlyMC() {
		g_isOnlyMC=true;
	}
	
	private TaskGenInd_IMC getTgp() {
		TaskGenInd_IMC tgp=new TaskGenInd_IMC();
		TGUtil.setMC(tgp,g_cfg);
		TGUtil.setMoLH(tgp,g_cfg);
		return tgp;
	}
	public void gen(String fn,Anal a,int num) {
		int i=0;
		MList ml=MList.new_list();
		ml.add(num+"");
		while(i<num){
			g_tg.genTS();
			if(!checkOnlyMC())
				continue;
			if(!isSch(a)) 
				continue;
			writeSys(ml);
			while(true) {
				int rs=writeAdd(ml);
				if(rs==1) break;
			}
//			SLog.err("ee "+i);

			i++;
		}
		ml.saveTo(fn);
	}

	
	public void writeSys(MList ml)
	{
		
		TaskSet ts=g_tg.getTS();


		TSFile.initStage(ml, g_stage);
		Task[] tss=ts.getArr();
		for(Task t:tss) {
			TSFile.writeTask(ml, t);
		}
	}
	public int writeAdd(MList ml)
	{
		TaskSet ts=g_tg.getTS();
		Task[] tss=ts.getArr();
		int num=tss.length;
		int i=1;
		double u=0;
		boolean bAdd=false;
		MList add_ml=MList.new_list();
//		SLog.prn(2, "start ");
		while(i<g_stage) {
//			SLog.prn(2, u+" ");
			boolean isAdd=g_rand.getBool();
			if(isAdd) { //add
				Task t=g_tg.genTaskOne();
				if(!t.check())
					continue;
				if(u+t.getMaxUtil()>g_upper) {
//					SLog.prn(2, (u+t.getMaxUtil())+","+g_upper);
					continue;
				}
				u+=t.getMaxUtil();
				TSFile.nextStage(add_ml,i);
				TSFile.writeTask(add_ml, t);
				num++;
//				SLog.prn(2,"add");
				bAdd=true;
			} else { // remove
//				SLog.prn(2,"del");
				int remove_n=g_rand.getInt(num);
//				SLog.prn(2,remove_n+"");
				Task t=ts.get(i);
				if(t.removed())
					continue;
				t.markRemoved();
				u-=t.getMaxUtil();
				TSFile.nextStage(add_ml,i);
				TSFile.remove(add_ml,remove_n);
//				g_tg.remove(remove_n);
				num--;
			}
			i++;
		}
		if(!bAdd) {
			for(Task t:ts.getArr()) {
				t.reset();
			}
			return 0;
		}
		for(String s:add_ml.getVec()) {
			ml.add(s);
		}
		
		ml.add("------");
		return 1;
	}
	
	private boolean isSch(Task t) {
		if(!g_isSch)
			return true;
		TaskSet ts=g_tg.getTS();
		g_tg.add(t);
		TaskMng tm=ts.getTM();
		Anal a=new AnalEDF_VD_IMC();
		a.init(tm);
		a.auto();
		return a.is_sch(); 
	}
	protected boolean checkOnlyMC() {
		if(!g_isOnlyMC)
			return true;
		TaskSet tsf=g_tg.getTS();
		TaskMng tm=tsf.getTM();
		if(tm.getMaxUtil()<=1) 
			return false;
		return true;
	}

	protected boolean isSch(Anal a) {
		if(!g_isSch)
			return true;
		TaskSet ts=g_tg.getTS();
		TaskMng tm=ts.getTM();
		a.init(tm);
		double x=a.computeX();
		a.setX(x);
		x=a.computeX();
		a.setX(x);
		return a.is_sch(); 
	}

	public static SysGen load(ConfigGen cfg,int stage) {
		SysGen sg=new SysGen();
		sg.load_in(cfg,stage);
		return sg;
	}
	public void setUpper(double d) {
		g_upper=d;
	}

	
}

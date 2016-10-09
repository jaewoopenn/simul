package simul;


import processor.CoreMng;
import comp.OldCompMng;
import comp.OldComp;
import basic.TaskMng;
import utilSim.Log;

public class PartAnal  {
	private OldCompMng g_cm;
	private CoreMng g_pm;
	private int g_num_cpu;

	public PartAnal(OldCompMng mng, int cpus) {
		g_cm=mng;
		g_num_cpu=cpus;
		g_pm=new CoreMng();
		for(int i=0;i<g_num_cpu;i++){
			g_pm.addCPU(new OldCompMng());
		}
	}



	
	public void part_help()
	{
		help1();
		
		partitionFF(0.3);
//		partitionWF(0.3);
		
		help2();

	}


	public void help2() {
		Log.prn(2, "after part");
		for(int i=0;i<g_num_cpu;i++){
			Log.prnc(2, "cpu "+i);
			OldCompMng core=g_pm.getCPU(i);
//			core.prn();
			core.computeUtils();
			Log.prnc(2, " util:");
			Log.prnDbl(2, core.get_max_util());
			core.prn2();
		}
	}


	public void help1() {
		for(int i=0;i<g_cm.getSize();i++){
			OldComp tm=g_cm.getComp(i);
			tm.prn(2);
		}
		g_cm.computeUtils();
		Log.prn(2, "max_util:"+g_cm.get_max_util());
		
	}


	public boolean partitionFF(double alpha) {
		for(int i=0;i<g_cm.getSize();i++){
			OldComp tm=g_cm.getComp(i);
			double score=0;
			for(int j=0;j<g_num_cpu;j++){
				OldCompMng core=g_pm.getCPU(j);
			
				score=getScore(core,tm,alpha);
//				Log.prn(2, "FF"+score);
//				Log.prnDbl(2, score);
				if (score<=1) {
					core.addComp(tm);
					break;
				}
			}
			if(score>1) {
				Log.prn(1, "total "+g_cm.getSize() +" no "+i);
				Log.prnDbl(1, score);
				return false;
			}
		}
		return true;
	}
	public boolean partitionBF(double alpha) {
		int pID;
		double pScore;
		for(int i=0;i<g_cm.getSize();i++){
			pID=-1;
			pScore=-1;
			OldComp tm=g_cm.getComp(i);

			for(int j=0;j<g_num_cpu;j++){
				OldCompMng core=g_pm.getCPU(j);
			
				double score=getScore(core,tm,alpha);
				if (score>pScore && score<=1){
					pID=j;
					pScore=score;
				}
			}
			if (pScore<=1 && pScore>=0) {
				OldCompMng core=g_pm.getCPU(pID);
				core.addComp(tm);
			}
			else{
				return false;
			}

		}
		return true;
	}

	public boolean partitionWF(double alpha) {
		int pID;
		double pScore;
		for(int i=0;i<g_cm.getSize();i++){
			pID=-1;
			pScore=2;
			OldComp tm=g_cm.getComp(i);

			for(int j=0;j<g_num_cpu;j++){
				OldCompMng core=g_pm.getCPU(j);
			
				double score=getScore(core,tm,alpha);
				if (score<pScore){
					pID=j;
					pScore=score;
				}
			}
			if (pScore<=1) {
				OldCompMng core=g_pm.getCPU(pID);
				core.addComp(tm);
			}
			else{
				Log.prn(1, "total "+g_cm.getSize() +" no "+i);
				Log.prnDbl(1, pScore);
				return false;
			}

		}
		return true;
	}


	private double getScore(OldCompMng core, OldComp tm,double alpha) {
		OldCompMng tempCore=new OldCompMng(core);
		tempCore.addComp(tm);
		CompAnal a=new CompAnal(tempCore);
		a.compute_X();
		a.set_alpha(alpha);
		TaskMng c_tm=a.getInterfaces();
//		tm.prn();
		
		return Analysis.getScore_EDF_VD(c_tm);
	}
	


}

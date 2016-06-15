package Simul;


import Basic.Comp;
import Basic.CoreMng;
import Basic.TaskMng;
import Util.Log;

public class PartAnal  {
	private CompMng g_cm;
	private CoreMng g_pm;
	private int g_num_cpu;

	public PartAnal(CompMng mng, int cpus) {
		g_cm=mng;
		g_num_cpu=cpus;
		g_pm=new CoreMng();
		for(int i=0;i<g_num_cpu;i++){
			g_pm.addCPU(new CompMng());
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
			CompMng core=g_pm.getCPU(i);
//			core.prn();
			core.computeUtils();
			Log.prnc(2, " util:");
			Log.prnDbl(2, core.get_max_util());
			core.prn2();
		}
	}


	public void help1() {
		for(int i=0;i<g_cm.getSize();i++){
			Comp tm=g_cm.getComp(i);
			Log.prnc(2, "comp "+tm.get_id());
			Log.prnc(2, " max_util:");
			Log.prnDbl(2,tm.getCompUtil());
		}
		
	}


	public boolean partitionFF(double alpha) {
		for(int i=0;i<g_cm.getSize();i++){
			Comp tm=g_cm.getComp(i);
			double score=0;
			for(int j=0;j<g_num_cpu;j++){
				CompMng core=g_pm.getCPU(j);
			
				score=getScore(core,tm,alpha);
				if (score<1) {
					core.addComp(tm);
					break;
				}
			}
			if(score>1) 
				return false;
		}
		return true;
	}
	public boolean partitionWF(double alpha) {
		int pID;
		double pScore;
		for(int i=0;i<g_cm.getSize();i++){
			pID=-1;
			pScore=2;
			Comp tm=g_cm.getComp(i);

			for(int j=0;j<g_num_cpu;j++){
				CompMng core=g_pm.getCPU(j);
			
				double score=getScore(core,tm,alpha);
				if (score<pScore){
					pID=j;
					pScore=score;
				}
			}
			if (pScore<=1) {
				CompMng core=g_pm.getCPU(pID);
				core.addComp(tm);
			}
			else{
				return false;
			}

		}
		return true;
	}


	private double getScore(CompMng core, Comp tm,double alpha) {
		CompMng tempCore=new CompMng(core);
		tempCore.addComp(tm);
		CompAnal a=new CompAnal(tempCore);
		a.compute_X();
		a.set_alpha(alpha);
		TaskMng c_tm=a.getInterfaces();
//		tm.prn();
		
		return Analysis.getScore_EDF_VD(c_tm);
	}
	


}

package Simul;


import Basic.Comp;
import Basic.CoreMng;
import Basic.TaskMng;
import Util.Log;

public class PartAnal  {
	private CompMng g_cm;
	private CoreMng g_pm;
	private int g_num_cpu;

	public void init(CompMng mng,int cpus) {
		g_cm=mng;
		g_num_cpu=cpus;
		g_pm=new CoreMng();
		for(int i=0;i<g_num_cpu;i++){
			g_pm.addCPU(new CompMng());
		}
	}

	
	public void part_help()
	{
		for(int i=0;i<g_cm.getSize();i++){
			Comp tm=g_cm.getComp(i);
			Log.prnc(2, "comp "+tm.get_id());
			Log.prn(2, " max_util:"+tm.getCompUtil());
		}
		
		partitionFF();

		Log.prn(2, "after part");
		for(int i=0;i<g_num_cpu;i++){
			Log.prnc(2, "cpu "+i);
			CompMng core=g_pm.getCPU(i);
//			core.prn();
			core.computeUtils();
			Log.prn(2, " util:"+core.get_max_util());
			core.prn2();
		}
	}


	private void partitionFF() {
		for(int i=0;i<g_cm.getSize();i++){
			Comp tm=g_cm.getComp(i);
			for(int j=0;j<g_num_cpu;j++){
				CompMng core=g_pm.getCPU(j);
				boolean b=checkAdd(core,tm);
				if (b) {
					core.addComp(tm);
					break;
				}
			}
		}
	}


	private boolean checkAdd(CompMng core, Comp tm) {
		CompMng tempCore=new CompMng(core);
		tempCore.addComp(tm);
		CompAnal a=new CompAnal(tempCore);
		a.compute_X();
		a.set_alpha(0.3);
//		a.set_alpha(1.0);
		TaskMng c_tm=a.getInterfaces();
//		tm.prn();
		return Analysis.anal_EDF_VD(c_tm)==1;
	}
	


}

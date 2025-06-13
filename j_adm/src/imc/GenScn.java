package imc;

import java.util.Vector;

import task.Task;
import task.TaskMng;
import util.MList;
import util.MRand;
import util.SLog;

public class GenScn {
	private double prob;
	private MList scn;
	private int nxt=-2;
	private int[] msa;
	protected MRand g_rutil=new MRand();

	public void setProb(double p) {
		prob=p;
	}
	
	public void gen(TaskMng tm,int dur,String fn) {
		tm.prn();
		boolean b=false;
		MList fu=new MList();
		fu.add(dur+"");
		for(int t=0;t<dur;t++) {
			Vector<Integer> v=new Vector<Integer>();
			b=false;
			String s="";
			s+=t+" : ";
			for(Task tsk:tm.get_HC_Tasks()){
				if (t%tsk.period==0){
					s+=tsk.tid+" ";
					b=true;
					double p=g_rutil.getDbl();
//					s+=p+" ";
					if(p<prob) { // generated prob < ms_prob
						s+="MS ";
						v.add(tsk.tid);
					}

				}
			}
			if(b)
				SLog.prn(1, s);
			if(v.size()!=0) {
				s=t+"";
				for(int vv:v) {
					s+=","+vv;
				}
				fu.add(s);
			}

		}
		fu.saveTo(fn);
	}
	public void play(String fn) {
		int dur=initGet(fn);
		int nxt=-2;
		int n=0;
		for(int t=0;t<dur;t++) {
//			SLog.prn(1, t+"");
			if(nxt<t && nxt!=-1 ) {
				fetchNxt();
				nxt=getNxt();
//				SLog.prn(1, nxt+"");
			} 
			if(nxt==t) {
				String s=t+": ";
				for(int v:getNxtA()) {
					s+=v+" ";
					n++;
				}
				SLog.prn(1, s);
			}
		}
		SLog.prn(1, "total: "+n);
		SLog.prn(1, "--end--");
		
	}
	public int initGet(String fn) {
		scn=new MList(fn);
		String next=scn.getNext();
		return Integer.valueOf(next).intValue();
		
	}
	public void fetchNxt() {
		String next;
		int t=0;
		next=scn.getNext();
		if(next==null) {
			nxt=-1;
			return;
			
		}
//			SLog.prn(1, next);
        String[] words=next.split(",");
//		SLog.prnc(1, words[0]+" ");
		nxt=Integer.valueOf(words[0]).intValue();
		
        boolean bFirst=true;
        msa=new int[words.length-1];
        int i=0;
        for(String w: words) {
        	if(bFirst) {
        		bFirst=false;
        		continue;
        	}
			msa[i]=Integer.valueOf(w);
			i++;
        }
//		SLog.prn(1, "");
	}
    public int[] getNxtA() {
		return msa;
	}

	public int getNxt() {
		return nxt;
	}
}

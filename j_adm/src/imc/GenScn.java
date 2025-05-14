package imc;

import java.util.Vector;

import task.Task;
import task.TaskMng;
import util.MList;
import util.MRand;
import util.SLog;

public class GenScn {
	TaskMng tm;
	double prob;
	MList scn;
	int next_t;
	Vector<Integer> ms_v;
	protected MRand g_rutil=new MRand();

	public GenScn(TaskMng tm) {
		this.tm=tm;
	}
	public void setProb(double p) {
		prob=p;
	}
	public void gen(int dur,String fn) {
		tm.prn();
		boolean b=false;
		MList fu=new MList();
		fu.add(dur+"");
		for(int t=0;t<dur;t++) {
			Vector<Integer> v=new Vector<Integer>();
			b=false;
			String s="";
			s+=t+" : ";
			for(Task tsk:tm.getTasks()){
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
		fu.save(fn);
	}
	public void play(String fn) {
		tm.prn();
		scn=new MList(fn);
		String next=scn.getNext();
		int dur=Integer.valueOf(next).intValue();
		int nxt=-2;
		for(int t=0;t<dur;t++) {
//			SLog.prn(1, t+"");
			if(nxt<t && nxt!=-1 ) {
				nxt=getNext();
//				SLog.prn(1, nxt+"");
			} 
			if(nxt==t) {
				String s=t+": ";
				for(int v:ms_v) s+=v+" ";
				SLog.prn(1, s);
			}
		}
		SLog.prn(1, "--end--");
		
	}
	private int getNext() {
		String next;
		ms_v=new Vector<Integer>();
		int t=0;
		next=scn.getNext();
		if(next==null)
			return -1;
//			SLog.prn(1, next);
        String[] words=next.split(",");
//		SLog.prnc(1, words[0]+" ");
		t=Integer.valueOf(words[0]).intValue();
        boolean bFirst=true;
        for(String w: words) {
        	if(bFirst) {
        		bFirst=false;
        		continue;
        	}
        	
//			SLog.prnc(1, w+" ");
			ms_v.add(Integer.valueOf(w));
        }
//		SLog.prn(1, "");
		return t;
	}
}

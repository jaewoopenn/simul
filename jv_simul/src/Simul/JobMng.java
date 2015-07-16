package Simul;

import java.util.PriorityQueue;
import java.util.Vector;

import Util.Log;

public class JobMng {
	PriorityQueue<Job> jobs;
	
	public JobMng() {
		jobs=new PriorityQueue<Job>();
	}
	public void insert(Job job) {
		jobs.add(job);
	}
	public boolean progress(int cur_t,int dur){
		while(dur>0)
		{
			Job j=getCur();
			if(j==null)
			{
				Log.prn(1," dur:"+dur+" empty");
				break;
			}
			if(dur>=j.exec) {
				Log.prn(1," dur:"+j.exec+" tid:"+j.tid+" exec_type:1");
				dur-=j.exec;
				j.exec=0;
				if(cur_t+dur>j.dl)
					return false;
			} else {  // dur <j.exec
				Log.prn(1," dur:"+dur+" tid:"+j.tid+" exec_type:2");
				j.exec-=dur;
				dur=0;
				insert(j);
			}
				
		}
		return true;
		
	}
	public Job getCur(){
		if(jobs.size()!=0)
			return jobs.poll();
		else 
			return null;
	}
	public int size(){
		return jobs.size();
	}
	public void prn(){
		int sz=jobs.size();
		if(sz==0)
		{
			Log.prn(1,"empty");
			return;
		}
		Vector<Job> v=new Vector();
		for(int i=0;i<sz;i++){
			Job j=getCur();
			Log.prn(1,j.dl+","+j.exec);
			v.add(j);
		}
		for(Job j:v){
			insert(j);
		}
	}
}

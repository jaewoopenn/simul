package task;



import java.util.Vector;

import util.MList;


public class CompSet {
	private Vector<Comp> cs=new Vector<Comp>();
	public CompSet() {
		
	}
	public CompSet(Vector<MList> mlv) {
		int id=0;
		for(MList ml:mlv) {
			TaskVec tv=TaskUtil.loadML(ml);
			Comp c=new Comp(id,tv);
			id++;
			cs.add(c);
		}
		
	}
	public void add(Comp c) {
		cs.add(c);
	}
	public int size() {
		return cs.size();
	}
	public Comp get(int i) {
		return cs.get(i);
	}
	public  double getUtil() {
		double util=0;
		for(Comp c:cs){
			util+=c.getUtil();
		}
//		SLog.prn(1, util+"");
		return util;
	}
	public TaskSet getTS(int i) {
		return cs.get(i) .getTS();
	}
	public void prn() {
		for(Comp c:cs){
			c.prn();
		}
		
	}

}


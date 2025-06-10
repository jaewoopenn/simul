package util;

import java.util.Vector;

public class MList {
	private Vector<String> g_list;
	private int cur=0;
	public MList() {
		g_list=new Vector<String>();
	}
	public MList(String fn) {
		g_list=new Vector<String>();
		MFile fu=new MFile(fn);
	    fu.load();
	    for(int i:MLoop.run(fu.bufferSize())){
	    	g_list.add(fu.getBuf(i));
		}		
	}
	public String getNext() {
		if(cur==g_list.size())
			return null;
		String s=g_list.get(cur);
		cur++;
		return s;
	}
	public String get(int i) {
		return g_list.get(i);
	}	
	
	public void add(String s) {
		g_list.add(s);
	}
	public void prn() {
		for (String s:g_list){
			SLog.prn(1, s);
		}
		
	}
	public int size() {
		return g_list.size();
	}
	

	public void saveTo(String fn) {
		if(fn==null)
			return;
		MOut fu=new MOut(fn);
		for(String s:g_list) {
			fu.write(s);
		}
		fu.save();
	}
	public void copyFrom(MFile fu) {
		for(int i=0;i<fu.bufferSize();i++) {
	    	String line=fu.getBuf(i);
	    	add(line);
		}
		
	}
}

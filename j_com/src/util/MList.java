package util;

import java.util.Vector;

public class MList {
	private Vector<String> g_list;
	private int cur=0;
	public MList() {
		g_list=new Vector<String>();
	}
	public String getNext() {
		if(cur==g_list.size())
			return null;
		String s=g_list.get(cur);
		cur++;
		return s;
	}
	public String get(int i) {
		return g_list.get(cur);
	}	
	
	public void add(String s) {
		g_list.add(s);
	}
	public void prn() {
		for (String s:g_list){
			S_Log.prn(1, s);
		}
		
	}
	public int size() {
		return g_list.size();
	}
	
	public void load(String fn) {
		MFile fu=new MFile(fn);
	    fu.load();
	    for(int i:MLoop.run(fu.size())){
	    	String line=fu.get(i);
	    	g_list.add(line);
		}
	}
	public void save(String fn) {
		MOut fu=new MOut(fn);
		for(String s:g_list) {
			fu.write(s);
		}
		fu.save();
	}
}

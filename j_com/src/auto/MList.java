package auto;

import java.util.Vector;

import util.FOut;
import util.MFile;
import util.MLoop;
import util.S_Log;

public class MList {
	private Vector<String> g_list;
	private int mode=0;
	private int cur=0;
	public MList() {
		g_list=new Vector<String>();
	}
	public void write(String s) {
		if(mode==1) {
			S_Log.err("not add mode");
		}
		g_list.add(s);
	}
	public void end() {
		mode=1;
		cur=0;
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
	
	public void load(String fn) {
		MFile fu=new MFile(fn);
	    fu.load();
	    for(int i:MLoop.run(fu.size())){
	    	String line=fu.get(i);
	    	g_list.add(line);
		}
	    end();
	}
	public void save(String fn) {
		FOut fu=new FOut(fn);
		for (String s:g_list){
			fu.write(s);
		}
		fu.save();
	}
	public void prn() {
		for (String s:g_list){
			S_Log.prn(1, s);
		}
		
	}
	public int size() {
		return g_list.size();
	}
}

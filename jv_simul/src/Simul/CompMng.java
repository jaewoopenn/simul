package Simul;

import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;

import Util.Log;

public class CompMng {
	private Vector<TaskMng> g_comp;
	public CompMng() {
		g_comp=new Vector<TaskMng>();
	}
	
	
	public void addComp(TaskMng tm) {
		g_comp.add(tm);
	}
	



}

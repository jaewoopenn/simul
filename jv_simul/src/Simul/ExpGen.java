package Simul;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;

import Util.Log;

public class ExpGen {
	private final String[] g_predefined={"util","p_ub","p_lb","tu_lb","tu_ub","num","subfix"};
	private TaskGen tg;
	private HashMap<String,String> param;
	public ExpGen() {
		tg=new TaskGen();
		param=new HashMap<String,String>();
	}
	public int readConfig(String f) {
	    File file = new File("/Users/jaewoo/data/"+f);
	    FileReader fr;
		try {
			fr = new FileReader(file);
		    BufferedReader br = new BufferedReader(fr);
		    String line;
		    while((line = br.readLine()) != null){
	            String[] words=line.split(":");
	            if(!setParam(words[0],words[1]))
	            	return 0;
		    }
		    br.close();
		    fr.close();		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		return 1;
	}
	private boolean setParam(String field, String val){
		if(Arrays.asList(g_predefined).contains(field)){
			param.put(field, val);
			return true;
		}
		return false;
	}
	public String readPar(String f) {
		if(Arrays.asList(g_predefined).contains(f))
			return param.get(f).trim();
		System.out.println("field is not defined");
		return null;
	}
	public int readInt(String f){
		return Integer.valueOf(readPar(f)).intValue();
	}
	private double readDbl(String f){
		return Double.valueOf(readPar(f)).doubleValue();
	}
	public void gen() {
		TaskGen tg=new TaskGen();
		tg.setUtil(readDbl("util"));
		tg.setPeriod(readInt("p_lb"),readInt("p_ub"));
		tg.setTUtil(readDbl("tu_lb"),readDbl("tu_ub"));
		int num=readInt("num");
		for(int i=0;i<num;i++){
			tg.generate();
			String fn=readPar("subfix").trim()+"/taskset"+i;
			tg.writeFile(fn);
		}
		
	}
	public int load() {
		int num=readInt("num");
		int sum=0;
		for(int i=0;i<num;i++){
			TaskGen tg=new TaskGen();
			String fn=readPar("subfix").trim()+"/taskset"+i;
			tg.loadFile(fn);
			tg.prn();
			TaskMng tm=new TaskMng();
			tm.setTasks(tg.getAll());
			Platform p=new Platform();
			p.init(tm);
			int ret=p.simul(20);
			sum+=ret;
			Log.prn(2, "task "+i+" ret:"+ret);
		}
		return sum;
		
	}

}

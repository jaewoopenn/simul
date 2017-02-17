package util;

import java.util.Vector;

public class Script {
	Vector<String> g_scr;
	public Script(){
		g_scr=new Vector<String>();
	}
	public void load(String fn) {
		FUtil fu=new FUtil(fn);
		fu.load();
		g_scr=fu.getVec();
		//fu.prn();
	}
	public void test() {
		FUtil fu=new FUtil("scr/test.txt");
		fu.print("load/ad.c");
		fu.print("run/100");
		fu.save();
	}
	public void exec()
	{
		for(int i=0;i<g_scr.size();i++){
			String cmd=g_scr.get(i);
			exec(cmd);
		}
	}
	private void exec(String cmd) {
        String[] words=cmd.split("/");
//        Log.prn(1, words[0]);
        switch(words[0]){
        	case "prn":
        		Log.prn(1, words[1]);
                String[] args=words[1].split("\\s+");
        		Log.prn(1, args[0]);
        		break;
        	default:
        		Log.prn(1, "default:"+cmd);
        		break;
        }
	}

}

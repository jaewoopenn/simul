package patient;

import util.MList;
import util.SLog;

// 파이썬으로 하는게 더 낫다. 

public class z_test2 {

	public static void main(String[] args) {
		SLog.set_lv(0);
		MList ml=MList.load("patient/rs.txt");
		int hi_total=0;
		int hi_live=0;
		int rs=0;
		for(String s:ml.getVec()) {
			String[] sc=s.split(" ");
			if(sc[1].equals("HI")) {
				hi_total++;
				rs=Integer.valueOf(sc[3]);
				if(rs==0)
					hi_live++;
				
			}
		}
		double per=(double)hi_live/hi_total;
		SLog.prn("total:"+hi_total);
		SLog.prn("per:"+per);

	}

}

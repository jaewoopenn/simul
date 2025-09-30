package util;


public class MStr {
	public static int[] getSplit(String s){
        String[] words=s.split(",");
        int[] ints=new int[words.length];
        int i=0;
        for(String st:words) {
        	ints[i]=Integer.valueOf(st).intValue();
        	i++;
        }
		return ints;
	}

	public static String getMerge(int[] para) {
		String rs="";
		for(int i:para) {
			if(rs=="") {
				rs+=i;
			} else {
				rs+=","+i;
				
			}
		}
		return rs;
	}

}

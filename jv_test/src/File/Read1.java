package File;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Read1 {

	public static void main(String[] args) throws Exception {
	    File file = new File("/Users/jaewoo/data/test1.txt");
	    FileReader fr = new FileReader(file);
	    BufferedReader br = new BufferedReader(fr);
	    String line;
	    while((line = br.readLine()) != null){
            System.out.println(line);
	    }
	    br.close();
	    fr.close();		

	}

}

package File;

import java.io.PrintWriter;

public class Write1 {

	public static void main(String[] args) throws Exception {
		PrintWriter writer = new PrintWriter("/Users/jaewoo/data/test1.txt");
		writer.println("The first line");
		writer.println("The second line");
		writer.close();
	}

}

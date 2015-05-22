package net.wldmr.srrcpack;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		System.out.println(args.length);
		CPack.build(args[0]);
	}

}

package net.wldmr.srrcpack;

import java.io.IOException;

public class Main {
	
	public static void main(String[] args) throws IOException {
		CPack.Tree tree = (new CPack.Builder()).build(args[0]);
		System.out.println(tree.key);
	}

}

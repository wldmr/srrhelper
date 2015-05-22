package net.wldmr.srrcpack;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.*;

public class Main {
	
	private static enum RE {
		group_start ("^\\s*(\\w+) \\{$"),
		group_end ("^\\s*\\}$"),
		property_string ("^\\s*(\\w+): \"([^\"]*)\"$"),
		property_value ("^\\s*(\\w+): (\\w+)$");
		
		private final Pattern pattern;
		RE(String regex) {
			pattern = Pattern.compile(regex);
		}
		
		Matcher match(String input) {
			return pattern.matcher(input);
		}
	}

	public static void main(String[] args) throws Exception {
		// Open the file
		FileInputStream fstream = new FileInputStream("UnderGroundClub.srt.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

		String strLine;

		//Read File Line By Line
		while ((strLine = br.readLine()) != null) {
			for (RE re : RE.values()) {
				Matcher m = re.match(strLine);
				if (m.matches()) {
					switch (re.name()) {
					case "group_start":
						System.out.println("start: " + m.group(1));
						break;
					case "group_end":
						System.out.println("end");
						break;
					case "property_string":
						System.out.println("property (string): "
								+ m.group(1) + " = " + m.group(2));
						break;
					case "property_value":
						System.out.println("property (value): "
								+ m.group(1) + " = " + m.group(2));
						break;
					default:
						throw new Exception("Fuck!");
					}
				}
			}
		}

		//Close the input stream
		br.close();
	}

}

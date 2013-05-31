package no.hild1.bank.utils;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringEscapeUtils;

public class CleanSDCTelepay {

	private static String cleanHTML(String path) throws IOException {
		String source = readFile(path, Charset.forName("ISO 8859-1"));
		
		Pattern p = Pattern.compile("(<pre>)(.+?)(</pre>)"); //"^[a-zA-Z]+([0-9]+).*");
		Matcher m = p.matcher(source);

		if (m.find()) {
			String tpData = m.group(2);
		    //
			tpData=tpData.replace("&Oslash;", "Ø");
			tpData=tpData.replace("&oslash;", "ø");
			tpData=tpData.replace("&Aring;", "Å");
			tpData=tpData.replace("&aring;", "å");
			tpData=tpData.replace("<br />", "\n");
		    return tpData;
		}
		return "No match";
	}

	private static String readFile(String path, Charset encoding) throws IOException {
		/* http://stackoverflow.com/a/326440*/
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return encoding.decode(ByteBuffer.wrap(encoded)).toString();
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		String[] files = {"testfile1.htm" };
		String path = "Telepay-filer/SDC/";
		//for (int i = 0; i <= files.length; i++) {	
			System.out.println(cleanHTML(path + files[0]));
			System.out.println(cleanHTML(path + files[1]));
		//}
	}

}

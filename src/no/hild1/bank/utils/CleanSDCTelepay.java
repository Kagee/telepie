package no.hild1.bank.utils;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringEscapeUtils;

public class CleanSDCTelepay {

	private static boolean cleanHTML(String path) throws IOException {
		String source = FileUtils.readFileToString(new File(path), "ISO_8859_1");
		
		Pattern p = Pattern.compile("(<pre>)(.+?)(</pre>)"); //"^[a-zA-Z]+([0-9]+).*");
		Matcher m = p.matcher(source);

		if (m.find()) {
			String tpData = m.group(2);
			tpData = StringEscapeUtils.unescapeHtml4(tpData).replace("<br />", "\n");
			FileUtils.writeStringToFile(new File(path+".telepay"), tpData, "ISO_8859_1");
			return true;
		}
		return false;
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		String[] files = {"testfile1.htm", "testfile2.htm" };
		String path = "Telepay-filer/SDC/";
		for (int i = 0; i < files.length; i++) {	
			if (!cleanHTML(path + files[i])) {
				System.out.println("Kunne ikke vaske " + files[i]);
			} else {
				System.out.println("Vasket " + files[i]);
			}
		}
	}

}

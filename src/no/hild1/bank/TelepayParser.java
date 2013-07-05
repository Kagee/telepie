package no.hild1.bank;

import no.hild1.bank.telepay.*;
import no.hild1.bank.utils.Consts;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mozilla.universalchardet.UniversalDetector;

public class TelepayParser {
	private static Log log = LogFactory.getLog(TelepayParser.class);
	String[] lines;
    int numRecords = 0;
    ArrayList<Betfor> records = new ArrayList<Betfor>();
	public TelepayParser(File file) throws TelepayParserException, IOException {
		checkEncoding(file);
		String source = FileUtils.readFileToString(file, "ISO_8859_1");
		lines = source.split("\n");
		if (lines.length % 4 != 0) {
			throw new TelepayParserException(
					"Lines in file is not a multiple of 4");
		}
        numRecords = lines.length/4;
		checkLines(lines);
	}

/*	public boolean parseAllRecords() throws TelepayParserException {
		for (int i = 1; i < (lines.length / 4); i++) {
			log.info(i);
			if (!parseRecord(i)) {
				return false;
			}
		}
		return true;
	}*/

	public Betfor parseRecord(int record) throws TelepayParserException {

        int startLine = (record-1)*4;
        String recordString = lines[startLine] + lines[startLine+1] + lines[startLine+2] + lines[startLine+3];
        BetforHeader header = getHeader(lines[(record-1)*4], record);
        return new Betfor();
	}



	private BetforHeader getHeader(String string, int record) throws TelepayParserException {
		log.info("Parsing header");
		log.info(string);
		Matcher m = Consts.appHeaderPattern.matcher(string);
		if (m.find()) {
			log.info(m.group("AHID"));
			return new BetforHeader(m, record);
		}
		return null;
	}

	private void checkLines(String[] lines) throws TelepayParserException {
		for (int i = 0; i < lines.length; i++) {
			String tmp = lines[i];
			if (tmp.length() != 80) {
				System.out.println(tmp);
				throw new TelepayParserException("Line " + (i + 1) + " is "
						+ tmp.length()
						+ " characters long, should be 80. (missing padding?)");
			}
		}
		log.info("All lines are 80 chars");
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		File dir = new File("Telepay/OK/");
		// dir = new File("Telepay/ERROR/");
		FileFilter fileFilter = new WildcardFileFilter("*.telepay");
		File[] files = dir.listFiles(fileFilter);
		Arrays.sort(files);
		// for (File child : files) {
		// System.out.println(child.getCanonicalPath());
		// }
		// ERROR: 0 utf8, 1 utf8, 2 tom fil,3 feil i dato,4,feil i lengde
		// OK:
		File f = files[0];
		try {
			log.info("Starting work on " + f.getPath());
			TelepayParser tp = new TelepayParser(f);
//			if (tp.parseAllRecords()) {
//				log.info("Parsed ok");
//			} else {
//				log.info("Parse failed");
//			}
		} catch (TelepayParserException e) {
			log.error("Failed to parse " + f.getPath());
			e.printStackTrace();
		}
	}

	/**
	 * Throws exception if file encoding is other than ISO-8859-1
	 * 
	 * Telepay files should be in ISO-8859-1, not e.g. UTF-8. We use
	 * http://code.google.com/p/juniversalchardet/ to try to detect the
	 * encoding. If we find no encoding, we assume it is ISO-8859-1.
	 * 
	 * @param file
	 *            The file to test
	 * @throws TelepayParserException
	 *             w. error message on errors
	 * @throws IOException
	 */
	public static void checkEncoding(File file) throws TelepayParserException,
			IOException {
		byte[] buf = new byte[4096];
		FileInputStream fis = new FileInputStream(file);
		UniversalDetector detector = new UniversalDetector(null);
		int nread;
		while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
			detector.handleData(buf, 0, nread);
		}

		fis.close();
		detector.dataEnd();
		String encoding = detector.getDetectedCharset();
		detector.reset();
		log.info("Detected encoding: " + (encoding == null ? "none" : encoding));
		if (!("WINDOWS-1252".equals(encoding) || encoding == null)) {
			throw new TelepayParserException(file.getPath() + " is encoded in "
					+ encoding + ", should be ISO-8859-1/WINDOWS-1252");
		}
	}
}

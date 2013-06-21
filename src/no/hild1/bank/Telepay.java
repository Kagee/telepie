package no.hild1.bank;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mozilla.universalchardet.UniversalDetector;

public class Telepay {
	String source;
	private static Log log = LogFactory.getLog(Telepay.class);
	public Telepay(File file) throws TelepayException {
		checkEncoding(file);
		
			try {
				source = FileUtils.readFileToString(file, "ISO_8859_1");
				System.out.println(source);
			} catch (IOException e) {
				throw new TelepayException("", e);
			}
	}
	private void getNextRecord() {
		
	}
	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		File dir = new File("../Telepay/OK/");
		//dir = new File("../Telepay/ERROR/");
		FileFilter fileFilter = new WildcardFileFilter("*.telepay");
		File[] files = dir.listFiles(fileFilter);
		Arrays.sort(files);
		//for (File child : files) {
		//	System.out.println(child.getCanonicalPath());
		//}
		try {
			log.info("Starting work on " + files[0].getPath());
			new Telepay(files[0]);
		} catch (TelepayException e) {
			log.error("Failed to parse " + files[0].getPath());
			e.printStackTrace();
		}
	}

	
	/**
	 * Throws exception if file encoding is other than ISO-8859-1
	 *
	 * Telepay files should be in ISO-8859-1, not e.g. UTF-8.
	 * We use http://code.google.com/p/juniversalchardet/
	 * to try to detect the encoding. If we find no encoding,
	 * we assume it is ISO-8859-1.
	 *
	 * @param  file The file to test
	 * @throws TelepayException w. error message on errors
	 */
	public static void checkEncoding(File file)
			throws TelepayException {
		try {
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
			log.info("Detected encoding " + (encoding == null ? "null":encoding));
			if (!("WINDOWS-1252".equals(encoding) || encoding == null)) {
				throw new TelepayException(file.getPath() + " is encoded in " + encoding + ", should be ISO-8859-1");
			}
		} catch (IOException e) {
			throw new TelepayException(
					"I/O error while determining encoding of " + file.getPath(),
					e);
		}
	}
}

package no.hild1.bank;

import no.hild1.bank.telepay.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.ArrayList;
import no.hild1.bank.telepay.Betfor.ElementInterface;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mozilla.universalchardet.UniversalDetector;

public class TelepayParser {
	private static Log log = LogFactory.getLog(TelepayParser.class);
	String[] lines;
    int numRecords = 0;
    public ArrayList<Betfor> records = new ArrayList<Betfor>();
    File sourceFile;

	public TelepayParser(File file) {
        this.sourceFile = file;
	}
    public String getFileName() {
        return sourceFile.getName();
    }
    public void basicCheck() throws TelepayParserException {
        try {
            checkEncoding(sourceFile);
            String source = FileUtils.readFileToString(sourceFile, "ISO_8859_1");

            // Remove those pesky, windows newlines.
            source=source.replaceAll("\r","\n");
            // and remove doubles.
            source=source.replaceAll("\n\n", "\n");

            lines = source.split("\n");

            if (lines.length % 4 != 0) {
                log.warn("Lines in file is not a multiple of 4");
            }

            numRecords = lines.length/4;
            checkLines(lines);
        }   catch (IOException ioe) {
            throw new TelepayParserException(ioe);
        }
    }
	public void parseAllRecords() throws TelepayParserException {
		for (int i = 1; i <= (lines.length / 4); i++) {
			log.info(i);
            records.add(parseRecord(i));
        }
	}

	public Betfor parseRecord(int recordNum) throws TelepayParserException {
        int startLine = (recordNum-1)*4;
        String recordString = lines[startLine] + lines[startLine+1] + lines[startLine+2] + lines[startLine+3];
        if (recordString.length() != 320) {
            throw new TelepayParserException("Record #" + recordNum + " is a total of "
                    + recordString.length()
                    + " characters long, should be 320.");
        }
        BetforHeader header = new BetforHeader(recordString, recordNum);

        log.debug("Probably BETFOR" + header.getBetforType());
        log.debug("Length: " + recordString.length());

        Betfor record;
        String parsed = "";
        switch (header.getBetforType()) {
            case 0:
                record = new Betfor00(header, recordString);
                break;
            case 21:
                record = new Betfor21(header, recordString);
                break;
            case 22:
                record = new Betfor22(header, recordString);
                break;
            case 23:
                record = new Betfor23(header, recordString);
                break;
            case 99:
                record = new Betfor99(header, recordString);
                break;
            default:
                throw new TelepayParserException("Unknown BETFOR type " + header.getBetforType());
        }
        for (ElementInterface elem : record.getElements()) {
            parsed += elem + ": '" + record.get(elem) + "'\n";
        }
        log.info(parsed);
        return record;
	}

	private void checkLines(String[] lines) throws TelepayParserException {
		for (int i = 0; i < lines.length; i++) {
			String tmp = lines[i];
			if (tmp.length() != 80) {
                log.info("Data in string is as follows: \n"
                        + String.format("%040x",
                        new BigInteger(tmp.getBytes(Charset.forName("ISO-8859-1"))))
                        + "\n>" + tmp + "<");
				throw new TelepayParserException("Line " + (i + 1) + " is "
						+ tmp.length()
						+ " characters long, should be 80. See log for data in line.");
			}
		}
		log.debug("All lines are 80 chars");
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
			for (int i = 0; i < buf.length; i++) {
            			if( (buf[i] & 0xFF) >= 0x7F && (buf[i] & 0xFF) <= 0x9F) { 
            				throw new TelepayParserException(file.getPath() + " is not encoded in "
					+ "ISO-8859-1/WINDOWS-1252");	
            			}
        		}
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

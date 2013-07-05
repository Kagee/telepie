package no.hild1.bank.telepay;

import java.util.regex.Matcher;
import no.hild1.bank.*;

public class BetforHeader {
	public BetforHeader(Matcher m, int record) throws TelepayParserException {
		// AH200TBII0530000026                   04
		if (!m.group("AHID").equals("AH"))
			throw new TelepayParserException(record, "Does not start with AH: " + m.group("AHID"));
		if (!m.group("AHVERSION").equals("2"))
			throw new TelepayParserException(record, "AH-VERSION not 2: " + m.group("AHVERSION"));
	}
}
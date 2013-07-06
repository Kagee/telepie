package no.hild1.bank.telepay;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Betfor00 extends Betfor {

    public Betfor00(BetforHeader header, String stringRecord) {
        super(header, stringRecord);
    }

    public String get(Element e) {
        return m.group(((Element)e).name());
    }

    /* makeBetforData.sh START */
	/* Generated by makeBetforData.sh */
	private static Log log = LogFactory.getLog(Betfor00.class);
	private static String betforRegexp = "^(APPLICATIONHEADER.{40})"
		+ "(?<"+ Element.TRANSACTIONCODE.name() + ">.{8}  "
		+ "(?<"+ Element.ENTERPRISENUMBER.name() + ">.{11}  "
		+ "(?<"+ Element.DIVISION.name() + ">.{11}  "
		+ "(?<"+ Element.SEQUENCECONTROL.name() + ">.{4}  "
		+ "(?<"+ Element.RESERVED1.name() + ">.{6}  "
		+ "(?<"+ Element.PRODUCTIONDATE.name() + ">.{4}  "
		+ "(?<"+ Element.PASSWORD.name() + ">.{10}  "
		+ "(?<"+ Element.VERSION.name() + ">.{10}  "
		+ "(?<"+ Element.NEWPASSWORD.name() + ">.{10}  "
		+ "(?<"+ Element.OPERATORNO.name() + ">.{11}  "
		+ "(?<"+ Element.SIGILLSEALUSE.name() + ">.{1}  "
		+ "(?<"+ Element.SIGILLSEALDATE.name() + ">.{6}  "
		+ "(?<"+ Element.SIGILLPARTKEY.name() + ">.{20}  "
		+ "(?<"+ Element.SIGILLSEALHOW.name() + ">.{1}  "
		+ "(?<"+ Element.RESERVED2.name() + ">.{143}  "
		+ "(?<"+ Element.OWNREFERENCEBATCH.name() + ">.{15}  "
		+ "(?<"+ Element.RESERVED3.name() + ">.{9}  "
		+ "$";
	public static Pattern betforPattern = Pattern.compile(betforRegexp);
	Matcher m;
	public enum Element {
	APPLICATIONHEADER, TRANSACTIONCODE, ENTERPRISENUMBER, 
		DIVISION, SEQUENCECONTROL, RESERVED1, 
		PRODUCTIONDATE, PASSWORD, VERSION, 
		NEWPASSWORD, OPERATORNO, SIGILLSEALUSE, 
		SIGILLSEALDATE, SIGILLPARTKEY, SIGILLSEALHOW, 
		RESERVED2, OWNREFERENCEBATCH, RESERVED3
	}
    /* makeBetforData.sh STOP */
}

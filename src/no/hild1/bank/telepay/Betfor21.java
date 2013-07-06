package no.hild1.bank.telepay;

import no.hild1.bank.TelepayParserException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Betfor21 extends Betfor {

    private static Log log = LogFactory.getLog(Betfor21.class);
    private static String betforRegexp = "^(.{40})BETFOR21"
            + "(?<"+Element.ENTERPRISENUMBER.name()+">.{11})"
            + "(?<ACCOUNTNUMBER>.{11})"
            + "(?<SEQUENCECONTROL>.{4})"
            + "(?<REFERENCENUMBER>.{6})"
            + "(?<PAYMENTDATE>.{6})"  // YYMMDD
            + "(?<OWNREFORDER>.{30})"
            + "(?<RESERVED>.{1})"
            + "(?<PAYEESACCOUNTNUMBER>.{11})"
            + "(?<PAYEESNAME>.{30})"
            + "(?<ADDRESS1>.{30})"
            + ".*";
    public static Pattern betforPattern = Pattern.compile(betforRegexp);
    Matcher m;

    public Betfor21(BetforHeader header, String stringRecord) throws TelepayParserException {
        super(header, stringRecord);
        m = betforPattern.matcher(this.stringRecord);
        if (m.matches()) {

        } else {
            throw new TelepayParserException(header.getRecordNum(), "Did not match BETFOR21");
        }
    }

    public String get(Element e) {
        return m.group(((Element)e).name());
    }
    public enum Element {
        ENTERPRISENUMBER, ACCOUNTNUMBER, SEQUENCECONTROL, REFERENCENUMBER, PAYMENTDATE, OWNREFORDER, RESERVED, PAYEESACCOUNTNUMBER, PAYEESNAME, ADDRESS1;
    }
}

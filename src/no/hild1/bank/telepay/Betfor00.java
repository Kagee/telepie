package no.hild1.bank.telepay;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Betfor00 extends Betfor {

    /* makeBetforData.sh START */
    private static Log log = LogFactory.getLog(Betfor21.class);
    private static String betforaRegexp = "^(.{40})BETFOR21"
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
    private static Pattern betforPattern = Pattern.compile(betforaRegexp);
    Matcher m;

    public enum Element {
        ENTERPRISENUMBER, ACCOUNTNUMBER, SEQUENCECONTROL, REFERENCENUMBER, PAYMENTDATE, OWNREFORDER, RESERVED, PAYEESACCOUNTNUMBER, PAYEESNAME, ADDRESS1
    }
    /* makeBetforData.sh STOP */

    public Betfor00(BetforHeader header, String stringRecord) {
        super(header, stringRecord);
    }

    public String get(Element e) {
        return m.group(((Element)e).name());
    }
}
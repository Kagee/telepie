package no.hild1.bank.telepay;

import no.hild1.bank.TelepayParserException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BetforHeader {
    private static final String appHeader =
            "^AH2"
                    + "(?<AHRETURNCODE>.{2})"
                    + "(?<AHPROCEDUREID>.{4})"
                    + "(?<AHTRANSACTIONDATE>.{4})"
                    + "(?<AHSEQNO>.{6})"
                    + "(?<AHTRANSCODE>.{8})"
                    + "(?<AHUSERID>.{11})"
                    + "(?<AHNOOF80CHAR>.{2})"
                    + "BETFOR"
                    + "(?<TRANSACTIONCODE>.{2}).*";
    public static Pattern appHeaderPattern = Pattern.compile(appHeader);
    private static Log log = LogFactory.getLog(BetforHeader.class);
    private int betforType;
    private int recordNum;

    public BetforHeader(String record, int recordNum) throws TelepayParserException {
        log.info(">" + record + "<");
        Matcher m = appHeaderPattern.matcher(record);
        if (m.matches()) {
            betforType = Integer.parseInt(m.group("TRANSACTIONCODE"));
            this.recordNum = recordNum;
        } else {
            throw new TelepayParserException(recordNum, "Failed to find header");
        }
    }

    public int getBetforType() {
        return betforType;
    }

    public int getRecordNum() {
        return recordNum;
    }
}
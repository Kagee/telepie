package no.hild1.bank.telepay;

import no.hild1.bank.TelepayParserException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.swingx.JXCollapsiblePane;
import org.jdesktop.swingx.JXTable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Betfor21 extends Betfor {

    public Betfor21(BetforHeader header, String stringRecord) throws TelepayParserException {
        super(header, stringRecord);
        log.info(this.stringRecord);
        m = this.betforPattern.matcher(this.stringRecord);

        log.info(this.betforRegexp);
        if (m.matches()) {
            log.info("Record #" + header.getRecordNum()
                    + " (staring at line " + (header.getRecordNum()*4) + ") is a BETFOR21");
        } else {
            String error = "Klarte ikke lese record #" + header.getRecordNum()
                    + " (som starter på line " + (header.getRecordNum()*4) + ") som en BETFOR21";
            log.error(error);
            throw new TelepayParserException(header.getRecordNum(),
                    error);
        }
    }

    @Override
    public Color getColor(ElementInterface e) {
        switch (((Element)e)) {
            case ACCOUNTNUMBER:
                return Color.yellow;
            case PAYEESNAME:
            case ADDRESS1:
            case ADDRESS2:
            case POSTCODE:
            case CITY:
                return Color.green;
            default:
                return null;
        }
    }

    /* makeBetforData.sh START */
	/* Generated by makeBetforData.sh */
	private static Log log = LogFactory.getLog(Betfor21.class);
	private static String betforRegexp = "^(?<"+ Element.APPLICATIONHEADER.name() + ">.{40})"
		+ "(?<"+ Element.TRANSACTIONCODE.name() + ">.{8})"
		+ "(?<"+ Element.ENTERPRISENUMBER.name() + ">.{11})"
		+ "(?<"+ Element.ACCOUNTNUMBER.name() + ">.{11})"
		+ "(?<"+ Element.SEQUENCECONTROL.name() + ">.{4})"
		+ "(?<"+ Element.REFERENCENUMBER.name() + ">.{6})"
		+ "(?<"+ Element.PAYMENTDATE.name() + ">.{6})"
		+ "(?<"+ Element.OWNREFORDER.name() + ">.{30})"
		+ "(?<"+ Element.RESERVED.name() + ">.{1})"
		+ "(?<"+ Element.PAYEESACCOUNTNUMBER.name() + ">.{11})"
		+ "(?<"+ Element.PAYEESNAME.name() + ">.{30})"
		+ "(?<"+ Element.ADDRESS1.name() + ">.{30})"
		+ "(?<"+ Element.ADDRESS2.name() + ">.{30})"
		+ "(?<"+ Element.POSTCODE.name() + ">.{4})"
		+ "(?<"+ Element.CITY.name() + ">.{26})"
		+ "(?<"+ Element.AMOUNTTOOWNACCOUNT.name() + ">.{15})"
		+ "(?<"+ Element.TEXTCODE.name() + ">.{3})"
		+ "(?<"+ Element.TRANSFERCODE.name() + ">.{1})"
		+ "(?<"+ Element.CANCELLATIONCODE.name() + ">.{1})"
		+ "(?<"+ Element.TOTALAMOUNT.name() + ">.{15})"
		+ "(?<"+ Element.CLIENTREFERENCE.name() + ">.{5})"
		+ "(?<"+ Element.VALUEDATE.name() + ">.{6})"
		+ "(?<"+ Element.VALUEDATERECEIVINGBANK.name() + ">.{6})"
		+ "(?<"+ Element.CANCELLATIONCAUSE.name() + ">.{1})"
		+ "(?<"+ Element.RESERVED2.name() + ">.{9})"
		+ "(?<"+ Element.FORMNO.name() + ">.{10})"
		+ "$";
	public static Pattern betforPattern = Pattern.compile(betforRegexp);
	public enum Element implements ElementInterface {
		APPLICATIONHEADER, TRANSACTIONCODE, ENTERPRISENUMBER, 
		ACCOUNTNUMBER, SEQUENCECONTROL, REFERENCENUMBER, 
		PAYMENTDATE, OWNREFORDER, RESERVED, 
		PAYEESACCOUNTNUMBER, PAYEESNAME, ADDRESS1, 
		ADDRESS2, POSTCODE, CITY, 
		AMOUNTTOOWNACCOUNT, TEXTCODE, TRANSFERCODE, 
		CANCELLATIONCODE, TOTALAMOUNT, CLIENTREFERENCE, 
		VALUEDATE, VALUEDATERECEIVINGBANK, CANCELLATIONCAUSE, 
		RESERVED2, FORMNO
	}
	public ElementInterface[] getElements() { return Element.values(); }
    /* makeBetforData.sh STOP */


}

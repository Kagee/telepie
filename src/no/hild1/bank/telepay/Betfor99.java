package no.hild1.bank.telepay;

import no.hild1.bank.TelepayParserException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.swingx.JXCollapsiblePane;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Betfor99 extends Betfor {

    public Betfor99(BetforHeader header, String stringRecord)  throws TelepayParserException {
        super(header, stringRecord);
        log.info(this.stringRecord);
        m = this.betforPattern.matcher(this.stringRecord);

        log.info(this.betforRegexp);
        if (m.matches()) {
            log.info("Record #" + header.getRecordNum()
                    + " (staring at line " + (header.getRecordNum()*4) + ") is a BETFOR99" +
                    "");
            if (Integer.parseInt(get(Element.NUMBEROFRECORDS)) != header.getRecordNum()) {
                throw new TelepayParserException("Record #" + header.getRecordNum()
                        + ", a BETFOR99 stated that we should have "
                        + Integer.parseInt(get(Element.NUMBEROFRECORDS))
                        + " records. We have found "
                        + header.getRecordNum());
            } else {
                log.debug("Parsed " + header.getRecordNum() + " records (objects and BETFOR99 agrees)");
            }
        } else {
            String error = "Failed to parse record #" + header.getRecordNum()
                    + " (staring at line " + (header.getRecordNum()*4) + ") as a BETFOR99";
            log.error(error);
            throw new TelepayParserException(header.getRecordNum(),
                    error);
        }
    }

    @Override
    public Color getColor(ElementInterface e) {
        return null;
    }

    /* makeBetforData.sh START */
	/* Generated by makeBetforData.sh */
	private static Log log = LogFactory.getLog(Betfor99.class);
	private static String betforRegexp = "^(?<"+ Element.APPLICATIONHEADER.name() + ">.{40})"
		+ "(?<"+ Element.TRANSACTIONCODE.name() + ">.{8})"
		+ "(?<"+ Element.ENTERPRISENUMBER.name() + ">.{11})"
		+ "(?<"+ Element.RESERVED1.name() + ">.{11})"
		+ "(?<"+ Element.SEQUENCECONTROL.name() + ">.{4})"
		+ "(?<"+ Element.RESERVED2.name() + ">.{6})"
		+ "(?<"+ Element.PRODUCTIONDATE.name() + ">.{4})"
		+ "(?<"+ Element.NUMBEROFPAYMENTS.name() + ">.{4})"
		+ "(?<"+ Element.TOTALAMOUNTBATCH.name() + ">.{15})"
		+ "(?<"+ Element.NUMBEROFRECORDS.name() + ">.{5})"
		+ "(?<"+ Element.RESERVED3.name() + ">.{163})"
		+ "(?<"+ Element.SIGILLSECURITY.name() + ">.{4})"
		+ "(?<"+ Element.SIGILLLANGUAGE.name() + ">.{1})"
		+ "(?<"+ Element.SIGILLVERSION.name() + ">.{1})"
		+ "(?<"+ Element.SIGILLINTERFACE.name() + ">.{1})"
		+ "(?<"+ Element.SIGILLCONTROLFIELD.name() + ">.{18})"
		+ "(?<"+ Element.VERSIONSOFTWARE.name() + ">.{16})"
		+ "(?<"+ Element.VERSIONBANK.name() + ">.{8})"
		+ "$";
	public static Pattern betforPattern = Pattern.compile(betforRegexp);
	public enum Element implements ElementInterface {
		APPLICATIONHEADER, TRANSACTIONCODE, ENTERPRISENUMBER, 
		RESERVED1, SEQUENCECONTROL, RESERVED2, 
		PRODUCTIONDATE, NUMBEROFPAYMENTS, TOTALAMOUNTBATCH, 
		NUMBEROFRECORDS, RESERVED3, SIGILLSECURITY, 
		SIGILLLANGUAGE, SIGILLVERSION, SIGILLINTERFACE, 
		SIGILLCONTROLFIELD, VERSIONSOFTWARE, VERSIONBANK
	}
	public ElementInterface[] getElements() { return Element.values(); }
    /* makeBetforData.sh STOP */
}

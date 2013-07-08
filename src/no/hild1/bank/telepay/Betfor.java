package no.hild1.bank.telepay;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import java.awt.*;

public abstract class Betfor {
	BetforHeader header;
    String  stringRecord;
    private static Log log = LogFactory.getLog(Betfor.class);

    public Betfor(BetforHeader header, String stringRecord) {
        this.header = header;
        this.stringRecord =  stringRecord;
	}

    @Override public String toString() {
        return "Record #" + header.getRecordNum() + ", BETFOR" + String.format("%02d", header.getBetforType());
    }
    public void displayBetfor() {
        log.info("Displaying record #"+ header.getRecordNum() +", a BETFOR" + header.getBetforTypeString() );
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        JEditorPane coloredRecordTextPane = new JEditorPane();

    }
    public abstract JPanel getPanel();
}
package no.hild1.bank.telepay;

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

public abstract class Betfor {
	public BetforHeader header;
    protected String  stringRecord;
    private static Log log = LogFactory.getLog(Betfor.class);
    protected Matcher m;
    public String[] elements;
    public Betfor(BetforHeader header, String stringRecord) {
        this.header = header;
        this.stringRecord =  stringRecord;
	}
    public Betfor() {}; // for BetforHeader
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
    public interface ElementInterface{
        String name();
    }
    public abstract Color getColor(ElementInterface e);
    public abstract ElementInterface[] getElements();

    public String get(ElementInterface e) {
        return m.group(e.name());
    }
 }
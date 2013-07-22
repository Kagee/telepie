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
    JButton showHideButton;
    JXCollapsiblePane mainCPanel;
    public JPanel getPanel() {
        JPanel panel = new JPanel();
        JTable table = new JTable();
        table.setEnabled(false);
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Key");
        model.addColumn("Value");
        String[] keyValue = new String[2];
        for(ElementInterface e: getElements()) {
            keyValue[0] = e.name();
            keyValue[1] = get(e);
            model.addRow(keyValue);
        }
        table.setModel(model);
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        showHideButton = new JButton("Record #" + header.getRecordNum() + ", BETFOR" + header.getBetforTypeString());
        showHideButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                mainCPanel.setCollapsed(!mainCPanel.isCollapsed());
            }
        });
        panel.add(showHideButton);
        mainCPanel = new JXCollapsiblePane();
        mainCPanel.setCollapsed(true);
        mainCPanel.add(table);
        mainCPanel.setCollapsed(false);
        panel.add(mainCPanel);
        return panel;
    }

    public JPanel getColorPanel() {
        JPanel panel = new JPanel();
        JEditorPane pane = new JEditorPane();
        pane.setContentType("text/html");
        pane.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        int charetPos = 0;
        String htmlRecord = "";

        String br = "<br>";

        for(ElementInterface e: getElements()) {
            String parser = "";
            Color c = getColor(e);
            if (c == null) c = Color.LIGHT_GRAY;
            String color = String.format("#%06X", (0xFFFFFF & c.getRGB()));
            String data = get(e);
            htmlRecord += "<span style='background-color: " + color +"'>";
            if (charetPos + data.length() == 80) {
                charetPos = 0;
                parser += data + br;
                data = null;
            } else { //charetPos + data.length() > 80
                while(charetPos + data.length() > 80) {
                    int rem = 80 - charetPos;
                    int len = Math.min(rem, data.length());
                    String remStr = data.substring(0, len);
                    data = data.substring(len, data.length());
                    parser += remStr + br;
                    charetPos = 0;
                }
                //if (data.length() == 0) { data = null; }
            }
            if(data != null) {
                if (charetPos + data.length() < 80) { // this is here to pick up after the while
                    charetPos += data.length();
                    parser += data;
                    data = null;
                }
            }
                htmlRecord += parser.replace(" ","&nbsp;") + "</span>";
        }

        pane.setText("<span style='font-family: \"Courier New\", Courier, monospace;' font-weight: bold;>"
                + htmlRecord + "</span>");
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(pane);
        return panel;
    }

    public interface ElementInterface{
        String name();
    }
    public abstract Color getColor(ElementInterface e);
    public abstract ElementInterface[] getElements();

    public String get(ElementInterface e) {
        return m.group(e.name());
    }
 }
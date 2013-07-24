package no.hild1.bank.telepay;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.swingx.JXCollapsiblePane;

import javax.swing.*;
import javax.swing.border.Border;
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
    JPanel colorPanel;
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
        if (colorPanel != null) {
            return colorPanel;
        }

        colorPanel = new JPanel();
        if (header.getBetforType() == 23 || header.getBetforType() == 22) {
            colorPanel.setBorder(BorderFactory.createEmptyBorder(0,30,0,0));
        }

        JEditorPane pane = new JEditorPane();
        pane.setEditable(false);
        pane.setContentType("text/html");
        pane.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        pane.setText("<span style='font-family: \"Courier New\", Courier, monospace;' font-weight: bold;>"
                + getRecord(true) + "</span>");
        colorPanel.setLayout(new BoxLayout(colorPanel, BoxLayout.PAGE_AXIS));
        colorPanel.add(pane);
        return colorPanel;
    }

    public String getRecord(boolean html) {
        return getRecord(html, null, null);
    }

    public String getRecord(boolean html, String fakeOrgNr, String fakeKonto) {
        int charetPos = 0;
        String formatedRecord = "";
        String br = "<br>";
        boolean lightGray = false;
        boolean fake = (fakeOrgNr != null && fakeKonto != null);

        for(ElementInterface e: getElements()) {
            String data = "";
            if(fake) {
                if (e.name().equals(Betfor00.Element.ENTERPRISENUMBER.name())) {
                    data = "00" + fakeOrgNr;
                } else if (e.name().equals(Betfor21.Element.ACCOUNTNUMBER.name())) {
                    data = fakeKonto;
                } else {
                    data = get(e);
                }
            } else {
                data = get(e);
            }

            String parser = "";
            if (html) {
                Color c = getColor(e);
                if (c == null) c = ((lightGray = !lightGray) ? Color.LIGHT_GRAY : Color.GRAY);
                String color = String.format("#%06X", (0xFFFFFF & c.getRGB()));
                formatedRecord += "<span style='background-color: " + color +"; font-weight: bold;'>";
            }

            if (charetPos + data.length() == 80) {
                charetPos = 0;
                parser += data + ((html) ? br :"\n");
                data = null;
            } else { //charetPos + data.length() > 80
                while(charetPos + data.length() > 80) {
                    int rem = 80 - charetPos;
                    int len = Math.min(rem, data.length());
                    String remStr = data.substring(0, len);
                    data = data.substring(len, data.length());
                    parser += remStr + ((html) ? br :"\n");
                    charetPos = 0;
                }
            }
            if(data != null) {
                if (charetPos + data.length() < 80) { // this is here to pick up after the while
                    charetPos += data.length();
                    parser += data;
                    data = null;
                }
            }
            formatedRecord += ((html) ? parser.replace(" ","&nbsp;") + "</span>" : parser);
        }
        return formatedRecord;
    }

    public void findOrHide(String searchTerm) {
        if(colorPanel != null) {
            if (stringRecord.contains(searchTerm)) {
                colorPanel.setVisible(true);
                log.info("Hiding " + header.getRecordNum());
            } else {
                colorPanel.setVisible(false);
            }
        }
    }
    public void displayColorPanel() {
        if(colorPanel != null) {
            colorPanel.setVisible(true);
        }
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
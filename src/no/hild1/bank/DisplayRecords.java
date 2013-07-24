package no.hild1.bank;

import no.hild1.bank.telepay.Betfor;
import org.jdesktop.swingx.JXCollapsiblePane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class DisplayRecords  extends JPanel /*JDialog*/ {
    ArrayList<Betfor> records;
    //JDialog dialog;
    JPanel dialog;
    LocalActionListener la;
    JTextField tf;
    JButton search, displayAll;
    public DisplayRecords(ArrayList<Betfor> records) {
        la = new LocalActionListener();
        //super("Records from " + file);
        dialog = this;
        //setTitle("Records from " + file);
        this.records = records;
        //getContentPane().add(panel, BorderLayout.CENTER);

        JPanel colorPanel = new JPanel();
        //mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        colorPanel.setLayout(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.BOTH;
        g.gridheight = 1;
        g.gridwidth = 1;
        //g.anchor = GridBagConstraints.CENTER;
        g.gridx = 0;
        g.gridy = 0;

        for (Betfor record: records) {
            colorPanel.add(record.getColorPanel(), g);
            //colorPanel.add(record.getColorPanel(), g);
                /*g.gridx=1;
                RecordButton tmp = new RecordButton("Tabell", record);
                tmp.addActionListener(new LocalActionListener());
                colorPanel.add(tmp, g);
                g.gridx=0;*/
            g.gridy++;
        }
        JScrollPane scrollpane = new JScrollPane();
        scrollpane.setViewportView(colorPanel);
        //setLayout(new GridBagLayout());
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));


            JPanel legend = new JPanel();
            legend.setLayout(new GridBagLayout());
            g = new GridBagConstraints();
            g.fill = GridBagConstraints.BOTH;
            g.gridheight = 1;
            g.gridwidth = 1;
            g.anchor = GridBagConstraints.CENTER;
            g.gridx = 0;
            g.gridy = 0;

            JEditorPane jep = new JEditorPane();
            jep.setContentType("text/html");

            String htmlLegend = "";
            htmlLegend += legend(Color.MAGENTA, "ORG. NR");
            htmlLegend += legend(Color.BLUE, "DATO");
            htmlLegend += legend(Color.GREEN, "BELØP I ØRER");
            htmlLegend += legend(Color.CYAN, "KID");
            htmlLegend += legend(Color.PINK, "TIL-KONTO");
            htmlLegend += legend(Color.YELLOW, "FRA-KONTO");

            jep.setText("<span style='font-family: \"Courier New\", Courier, monospace;' font-weight: bold;>"
                    + htmlLegend + "</span>");
            g.gridwidth = 12;
            legend.add(jep, g);
            tf = new JTextField("");
            tf.setToolTipText("Enter search text here");
            g.gridwidth = 12;
            g.fill = GridBagConstraints.HORIZONTAL;
            g.gridy = 1;
            g.gridx = 0;
            legend.add(tf, g);
            search = new JButton("Søk");
            search.addActionListener(la);
            displayAll = new JButton("Vis alle");
            displayAll.addActionListener(la);
            //g.fill = GridBagConstraints.NONE;
            g.gridy = 2;
            g.gridwidth = 6;
            g.gridx = 0;
            legend.add(search, g);
            g.gridx = 6;
            legend.add(displayAll, g);

            add(legend, BorderLayout.SOUTH);
        add(scrollpane, BorderLayout.CENTER);
    }

    String legend(Color c, String name) {
        return "<span style='background-color: " + String.format("#%06X", (0xFFFFFF & c.getRGB())) +"; font-weight: bold;'>" + name + "</span>&nbsp;&nbsp;&nbsp;";
    }
    class RecordButton extends JButton {
        public Betfor betfor;
        public RecordButton(String text, Betfor record) {
            super(text);
            betfor = record;
        }
    }
    class LocalActionListener implements java.awt.event.ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof RecordButton) {
                //RecordButton rb = ((RecordButton) e.getSource());
                //ArrayList<Betfor> ab = new ArrayList<Betfor>();
                //ab.add(rb.betfor);
                //new DisplayRecords(ab);
            } else if (e.getSource() == search) {
                String search = tf.getText();
                for(Betfor record : records) {
                    record.findOrHide(search);
                }
            } else if (e.getSource() == displayAll) {
                for(Betfor record : records) {
                    record.displayColorPanel();
                }
            }
        }
    }

}

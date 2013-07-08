package no.hild1.bank;

import no.hild1.bank.telepay.Betfor;
import org.jdesktop.swingx.JXCollapsiblePane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class DisplayRecords  extends JDialog {
    ArrayList<Betfor> records;
    JDialog dialog;
    public DisplayRecords(ArrayList<Betfor> records, String file) {
        //super("Records from " + file);
        dialog = this;
        setTitle("Records from " + file);
        this.records = records;
        //getContentPane().add(panel, BorderLayout.CENTER);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        for (Betfor record: records) {
            mainPanel.add(record.getPanel());
        }
        setModal(true);
        JScrollPane scrollpane = new JScrollPane(mainPanel);
        //scrollpane.add(mainPanel);
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
        getContentPane().add(scrollpane);
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                dialog.dispose();
            }
        });
        getContentPane().add(closeButton);
        Dimension screenSize = getToolkit().getScreenSize();
        int width = screenSize.width * 4 / 10;
        int height = screenSize.height * 5 / 10;
        setBounds(width / 8, height / 8, width, height);
        //pack();
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

}

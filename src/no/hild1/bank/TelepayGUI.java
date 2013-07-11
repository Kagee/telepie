package no.hild1.bank;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.swing.*;

import no.hild1.bank.telepay.*;
import no.hild1.bank.utils.MessageConsole;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TelepayGUI extends JFrame {
    private static Log log = LogFactory.getLog(TelepayGUI.class);
    private JEditorPane logPane;
    JButton selectFile, closeButton, attemptParseButton,
            viewAllRecordsButton, copyLog;
    JButton doubleCheck;
    final JFileChooser fc = new JFileChooser();
    AL al = new AL();
    JFrame app;
    TelepayParser telepayParser;

	public TelepayGUI() {
        super("Telepay 2v1 Viewer");
        logPane = new JEditorPane();
        fc.setCurrentDirectory(new File("./Telepay/OK"));

        app = this;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JScrollPane scrollPane = new JScrollPane(logPane);
        MessageConsole mc = new MessageConsole(logPane);
        mc.redirectOut();
        mc.redirectErr(Color.RED, null);
        //mc.setMessageLines(100);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));

        panel.add(scrollPane);
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(makeButtonPanel(), BorderLayout.SOUTH);
        setSize();
	    setVisible(true);
	}

    public JPanel makeButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));

        selectFile = new JButton("Åpne fil");
        selectFile.addActionListener(al);
        panel.add(selectFile);

        panel.add(Box.createHorizontalGlue());
        attemptParseButton = new JButton("Forsøk å lese fil");
        attemptParseButton.setEnabled(false);
        attemptParseButton.addActionListener(al);
        panel.add(attemptParseButton);

        panel.add(Box.createHorizontalGlue());
        viewAllRecordsButton = new JButton("Vis alle records");
        viewAllRecordsButton.setEnabled(false);
        viewAllRecordsButton.addActionListener(al);
        panel.add(viewAllRecordsButton);

        panel.add(Box.createHorizontalGlue());
        doubleCheck = new JButton("Sjekk for doble betalinger");
        doubleCheck.setEnabled(false);
        doubleCheck.addActionListener(al);
        panel.add(doubleCheck);

        panel.add(Box.createHorizontalGlue());
        copyLog = new JButton("Copy log");
        copyLog.addActionListener(al);
        panel.add(copyLog);

        panel.add(Box.createHorizontalGlue());
        closeButton = new JButton("Close");
        closeButton.addActionListener(al);
        panel.add(closeButton);

        return panel;
    }
    public void setSize() {
        Dimension screenSize = getToolkit().getScreenSize();
        int width = screenSize.width * 4 / 10;
        int height = screenSize.height * 5 / 10;
        //setBounds(width/8, height/8, width, height);
        logPane.setPreferredSize(new Dimension(width, height));
        pack();
    }
    public void displayError(String msg) {
        log.error(msg);
        JTextArea textArea = new JTextArea(msg);
        textArea.setColumns(50);
        textArea.setLineWrap( true );
        textArea.setWrapStyleWord( true );
        textArea.setSize(textArea.getPreferredSize().width, 1);
        JOptionPane.showMessageDialog(
                app, textArea, "Alvorlig feil funnet", JOptionPane.ERROR_MESSAGE);
    }
    public void displayMessage(String msg) {
        log.info(msg);
        JTextArea textArea = new JTextArea(msg);
        textArea.setColumns(50);
        textArea.setLineWrap( true );
        textArea.setWrapStyleWord( true );
        textArea.setSize(textArea.getPreferredSize().width, 1);
        JOptionPane.showMessageDialog(
                app, textArea, "Melding", JOptionPane.INFORMATION_MESSAGE);
    }
    class AL implements java.awt.event.ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == selectFile) {
                int returnVal = fc.showOpenDialog(TelepayGUI.this);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    log.debug("Åpner : " + file.getName() + ".");
                    telepayParser = new TelepayParser(file);
                    attemptParseButton.setEnabled(true);
                    viewAllRecordsButton.setEnabled(false);
                    doubleCheck.setEnabled(false);
                } else {
                    log.debug("Åpne-kommand avbrutt av bruker.");
                }
            } else if (e.getSource() == closeButton) {
                    app.dispose();
            }  else if (e.getSource() == attemptParseButton) {
                  if (telepayParser != null) {
                      try {
                          telepayParser.basicCheck();
                          telepayParser.parseAllRecords();
                        viewAllRecordsButton.setEnabled(true);
                        doubleCheck.setEnabled(true);
                      } catch (TelepayParserException e1) {
                          displayError(e1.toString());
                      }
                  }
            } else if (e.getSource() == viewAllRecordsButton)  {
                new DisplayRecords(telepayParser.records,fc.getSelectedFile().getName());
            } else if (e.getSource() == copyLog) {
                logPane.selectAll();
                logPane.copy();
            } else if (e.getSource() == doubleCheck) {
                Set<String> acckid = new HashSet<String>();
                boolean foundDouble = false;
                for (Betfor record: telepayParser.records) {

                    if (record instanceof Betfor23){
                        String KID = ((Betfor23)record).get(Betfor23.Element.KID);
                        String ACCOUNTNUMBER = ((Betfor23)record).get(Betfor23.Element.ACCOUNTNUMBER);
                        String INVOICEAMOUNT = ((Betfor23)record).get(Betfor23.Element.INVOICEAMOUNT);
                        String pattern = "^[ ]{27}$";
                        String key;
                        if (KID.matches(pattern)) {
                            log.info("Ikke KID: Record #" + record.header.getRecordNum());
                            key =  "ACC+AMOUNT:" + ACCOUNTNUMBER + ":"+ INVOICEAMOUNT;
                        } else {
                            log.info("KID: Record #" + record.header.getRecordNum());
                            key = "ACC+KID:" + ACCOUNTNUMBER +":"+ KID;
                        }

                        if (acckid.contains(key)) {
                            displayError("Fant nøkkel "+ key + " mer enn en gang. Sist sett i record #" + record.header.getRecordNum());
                            foundDouble = true;
                        } else {
                            log.info("Adding " + key);
                            acckid.add(key);
                        }
                    }
                }
                if (!foundDouble) {
                    displayMessage("Fant ingen KID-kontonummer eller beløp-kontonummer-par.");
                }
            }
        }
    }

	public static void main(String[] args) throws IOException {
        SwingUtilities.invokeLater(new Runnable()
        { public void run()  { new TelepayGUI(); } });
	}
}

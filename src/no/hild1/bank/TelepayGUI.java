package no.hild1.bank;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.swing.*;

import no.hild1.bank.telepay.*;
import no.hild1.bank.utils.FileDrop;
import no.hild1.bank.utils.MessageConsole;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TelepayGUI extends JFrame {
    private static Log log = LogFactory.getLog(TelepayGUI.class);
    private JEditorPane logPane;
    JButton selectFile, closeButton, viewAllRecordsButton, copyLog;
    JButton doubleCheck;
    final JFileChooser fc = new JFileChooser();
    LocalActionListener localActionListener = new LocalActionListener();
    JFrame application;
    TelepayParser telepayParser;
	public TelepayGUI() {
        super("Telepay 2v1 Viewer");
        application = this;

        logPane = new JEditorPane();
        fc.setCurrentDirectory(new File("./Telepay/OK"));
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
        javax.swing.border.TitledBorder dragBorder = new javax.swing.border.TitledBorder( "Slipp fil for å sette den som aktiv fil" );
        new FileDrop( panel, dragBorder, true, new LocalFileDropListener());

        setSize();
	    setVisible(true);
	}

    public JPanel makeButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));

        selectFile = new JButton("Åpne fil");
        selectFile.addActionListener(localActionListener);
        panel.add(selectFile);

        panel.add(Box.createHorizontalGlue());
        viewAllRecordsButton = new JButton("Vis alle records");
        viewAllRecordsButton.setEnabled(false);
        viewAllRecordsButton.addActionListener(localActionListener);
        panel.add(viewAllRecordsButton);

        panel.add(Box.createHorizontalGlue());
        doubleCheck = new JButton("Sjekk for doble betalinger");
        doubleCheck.setEnabled(false);
        doubleCheck.addActionListener(localActionListener);
        panel.add(doubleCheck);

        panel.add(Box.createHorizontalGlue());
        copyLog = new JButton("Copy log");
        copyLog.addActionListener(localActionListener);
        panel.add(copyLog);

        panel.add(Box.createHorizontalGlue());
        closeButton = new JButton("Close");
        closeButton.addActionListener(localActionListener);
        panel.add(closeButton);

        return panel;
    }
    public void setSize() {
        Dimension screenSize = getToolkit().getScreenSize();
        int width = screenSize.width * 4 / 10;
        int height = screenSize.height * 5 / 10;
        logPane.setPreferredSize(new Dimension(width, height));
        pack();
    }
    public void displayError(String msg) { showMessageDialog(msg, JOptionPane.ERROR_MESSAGE);}
    public void displayMessage(String msg) { showMessageDialog(msg, JOptionPane.INFORMATION_MESSAGE); }
    public void showMessageDialog(String msg, int messagetype) {
        log.error(msg);
        JTextArea textArea = new JTextArea(msg);
        textArea.setColumns(50);
        textArea.setLineWrap( true );
        textArea.setWrapStyleWord( true );
        textArea.setSize(textArea.getPreferredSize().width, 1);
        JOptionPane.showMessageDialog(
                application, textArea, "Alvorlig feil funnet", messagetype);
    }

    private void resetApp() {
        telepayParser = null;
        viewAllRecordsButton.setEnabled(false);
        viewAllRecordsButton.setEnabled(false);
    }

    class LocalFileDropListener implements no.hild1.bank.utils.FileDrop.Listener {
        public void filesDropped(File[] files) {
            {
                    log.info("Parsing dropped file: " + files[0].getName());
                    resetApp();
                    parseFile(files[0]);
            }
        }
    }

    private void parseFile(File file) {
        resetApp();
        telepayParser = new TelepayParser(file);
            try {
                telepayParser.basicCheck();
                telepayParser.parseAllRecords();
                viewAllRecordsButton.setEnabled(true);
                doubleCheck.setEnabled(true);
            } catch (TelepayParserException e1) {
                resetApp();
                displayError(e1.toString());
            }
    }

    class LocalActionListener implements java.awt.event.ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == selectFile) {
                int returnVal = fc.showOpenDialog(TelepayGUI.this);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    log.debug("Åpner : " + file.getName() + ".");
                    parseFile(file);
                } else {
                    resetApp();
                    log.debug("Åpne-kommand avbrutt av bruker.");
                }
            } else if (e.getSource() == closeButton) {
                    application.dispose();
            } else if (e.getSource() == viewAllRecordsButton)  {
                new DisplayRecords(telepayParser.records, telepayParser.getFileName());
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

package no.hild1.bank;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.*;

import no.hild1.bank.telepay.*;
import no.hild1.bank.utils.MessageConsole;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TelepayGUI extends JFrame {
    private static Log log = LogFactory.getLog(TelepayGUI.class);
	private JEditorPane htmlPane;
    private JEditorPane logPane;
    JButton selectFile, closeButton, attemptParseButton, displayGUIButton, viewRecordButton, viewAllRecordsButton, copyLog;
    JComboBox dropdown;
    final JFileChooser fc = new JFileChooser();
    AL al = new AL();
    JFrame app;
    TelepayParser telepayParser;

    /**
     * @throws IOException
     */
	public TelepayGUI() {
        super("Telepay 2v1 Viewer");
        logPane = new JEditorPane();
        //htmlPane = new JEditorPane();
        fc.setCurrentDirectory(new File("./Telepay/OK"));

        app = this;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//htmlPane.setText("Foo");
        JScrollPane scrollPane = new JScrollPane(logPane);
		//JScrollPane scrollPane2 = new JScrollPane(htmlPane);

        MessageConsole mc = new MessageConsole(logPane);
        mc.redirectOut();
        mc.redirectErr(Color.RED, null);
        //mc.setMessageLines(100);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
        //panel.add(scrollPane2);
        panel.add(scrollPane);
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(makeButtonPanel(), BorderLayout.SOUTH);
        setSize();
	    setVisible(true);
	}


    public JPanel makeButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));

        selectFile = new JButton("Open file");
        selectFile.addActionListener(al);
        panel.add(selectFile);

        panel.add(Box.createHorizontalGlue());
        attemptParseButton = new JButton("Attempt to parse file");
        attemptParseButton.setEnabled(false);
        attemptParseButton.addActionListener(al);
        panel.add(attemptParseButton);
      /*
        panel.add(Box.createHorizontalGlue());
        displayGUIButton = new JButton("Show GUI version of file");
        displayGUIButton.setEnabled(false);
        displayGUIButton.addActionListener(al);
        panel.add(displayGUIButton);


        panel.add(Box.createHorizontalGlue());

        dropdown = new JComboBox();
        dropdown.addItem("Please parse file first");
        panel.add(dropdown);

        viewRecordButton = new JButton("View single record");
        viewRecordButton.setEnabled(true);
        viewRecordButton.addActionListener(al);
        panel.add(viewRecordButton);
     */
        panel.add(Box.createHorizontalGlue());
        viewAllRecordsButton = new JButton("View all records");
        viewAllRecordsButton.setEnabled(false);
        viewAllRecordsButton.addActionListener(al);
        panel.add(viewAllRecordsButton);

        panel.add(Box.createHorizontalGlue());
        copyLog = new JButton("Kopier log");
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
        setBounds(width/8, height/8, width, height);
    }
    public void displayError(String msg) {
        log.error(msg);
        JTextArea textArea = new JTextArea(msg);
        textArea.setColumns(50);
        textArea.setLineWrap( true );
        textArea.setWrapStyleWord( true );
        textArea.setSize(textArea.getPreferredSize().width, 1);
        JOptionPane.showMessageDialog(
                app, textArea, "Error found", JOptionPane.ERROR_MESSAGE);
    }
    class AL implements java.awt.event.ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == selectFile) {
                int returnVal = fc.showOpenDialog(TelepayGUI.this);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    log.debug("Opening: " + file.getName() + ".");
                    telepayParser = new TelepayParser(file);
                    attemptParseButton.setEnabled(true);
                } else {
                    attemptParseButton.setEnabled(false);
                    viewAllRecordsButton.setEnabled(false);
                    log.debug("Open command cancelled by user.");
                }
            } else if (e.getSource() == closeButton) {
                    app.dispose();
            }  else if (e.getSource() == attemptParseButton) {
                  if (telepayParser != null) {
                      try {
                          telepayParser.basicCheck();
                          telepayParser.parseAllRecords();
                          //displayGUIButton.setEnabled(true);
                          //dropdown = new JComboBox();
                          //dropdown.removeAllItems();
                          //log.info("# of records: " + telepayParser.records.size());
                          //for (int i = 0; i < telepayParser.records.size(); i++) {
                          //  dropdown.addItem(telepayParser.records.get(i));
                          //  //dropdown.add(telepayParser.records.get(i));
                          //}
                        viewAllRecordsButton.setEnabled(true);

                      } catch (TelepayParserException e1) {
                          //displayGUIButton.setEnabled(false);
                          displayError(e1.toString());
                      }
                  }
            } else if (e.getSource() == displayGUIButton) {
                if (telepayParser != null) {
                    for (Betfor record : telepayParser.records) {
                        if (record instanceof Betfor00) {
                            Betfor00 tmp = ((Betfor00)record);
                            log.info("PRODUCTIONDATE: " + tmp.get(Betfor00.Element.PRODUCTIONDATE));
                            tmp = null;
                        } else if (record instanceof Betfor21) {
                            Betfor21 tmp = ((Betfor21)record);
                            log.info("PAYEESNAME: " + tmp.get(Betfor21.Element.PAYEESNAME));
                            tmp = null;
                        } else if (record instanceof Betfor22) {
                            Betfor22 tmp = ((Betfor22)record);
                            log.info("PAYEESNAME: " + tmp.get(Betfor22.Element.PAYEESNAME));
                            tmp = null;
                        }else if (record instanceof Betfor23) {
                            Betfor23 tmp = ((Betfor23)record);
                            log.info("KID: " + tmp.get(Betfor23.Element.KID));
                            tmp = null;
                        } else if (record instanceof Betfor99) {
                            Betfor99 tmp = ((Betfor99)record);
                            log.info("NUMBEROFRECORDS: " + tmp.get(Betfor99.Element.NUMBEROFRECORDS));
                            tmp = null;
                        } else {
                            displayError("Code to disply record of type " + record.getClass() +" missing");
                        }
                    }
                }
            } else if (e.getSource() == viewRecordButton) {
                ((Betfor) dropdown.getSelectedItem()).displayBetfor();
            } else if (e.getSource() == viewAllRecordsButton)  {
                new DisplayRecords(telepayParser.records,fc.getSelectedFile().getName());
            } else if (e.getSource() == copyLog) {
                logPane.selectAll();
                logPane.copy();
            }
        }
    }

	public static void main(String[] args) throws IOException {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                new TelepayGUI();
            }
        });

	}

}

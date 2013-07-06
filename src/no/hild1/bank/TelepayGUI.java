package no.hild1.bank;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.*;

import no.hild1.bank.utils.MessageConsole;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TelepayGUI extends JFrame {
    private static Log log = LogFactory.getLog(TelepayGUI.class);
	private static final long serialVersionUID = -5121434288061327051L;
	private JEditorPane htmlPane;
    private JEditorPane logPane;
    JButton selectFile, closeButton, attemptParseButton;
    final JFileChooser fc = new JFileChooser();
    AL al = new AL();
    JFrame app;
    TelepayParser telepayParser;

    /**
     * @throws IOException
     */
	public TelepayGUI() throws IOException {
        logPane = new JEditorPane();
        htmlPane = new JEditorPane();
        fc.setCurrentDirectory(new File("./Telepay/OK"));

        app = this;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		htmlPane.setText("Foo");
        JScrollPane scrollPane = new JScrollPane(logPane);
		JScrollPane scrollPane2 = new JScrollPane(htmlPane);
 /*
        MessageConsole mc = new MessageConsole(logPane);
        mc.redirectOut();
        mc.redirectErr(Color.RED, null);
        mc.setMessageLines(100);
   */
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
        panel.add(scrollPane2);
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
        attemptParseButton.addActionListener(al);
        panel.add(attemptParseButton);

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
    class AL implements java.awt.event.ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            //Handle open button action.
            if (e.getSource() == selectFile) {
                int returnVal = fc.showOpenDialog(TelepayGUI.this);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    //This is where a real application would open the file.
                    log.info("Opening: " + file.getName() + ".");
                    telepayParser = null;
                    try {
                        telepayParser = new TelepayParser(file);
                    } catch (Exception e1) {
                        log.error(e1);
                    }
                } else {
                    log.warn("Open command cancelled by user.");
                }
            } else if (e.getSource() == closeButton) {
                    app.dispose();
            }  else if (e.getSource() == attemptParseButton) {
                  if (telepayParser != null) {
                      try {
                          telepayParser.basicCheck();

                      } catch (Exception e1) {
                          log.error(e1);
                      }
                      try {
                          telepayParser.parseAllRecords();
                      } catch (TelepayParserException e1) {
                          log.error(e1);
                      }
                  }
            }
        }
    }

	public static void main(String[] args) throws IOException {
		new TelepayGUI();
	}

}

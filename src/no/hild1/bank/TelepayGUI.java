package no.hild1.bank;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.*;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TelepayGUI extends JFrame {
    private static Log log = LogFactory.getLog(TelepayGUI.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = -5121434288061327051L;
	private JEditorPane htmlPane;
    JButton selectFile, closeButton;
    final JFileChooser fc = new JFileChooser();
    AL al = new AL();
    JFrame app;
	private static String bgc(String color) {
		return "<span style=\"background-color: " + color + ";\">$1</span>";
	}
    /**
     * @param content
     * @throws IOException
     */
	public TelepayGUI(String content) throws IOException {
		htmlPane = new JEditorPane();
        app = this;
		/*content = content.replaceAll("\n", "\u23CE<br>");
		content = content.replaceAll(" ", "&nbsp;");
		content = content.replaceAll("(BETFOR23)", bgc("gray"));
		content = "<span style='font-family: \"Courier New\", Courier, monospace;'>" + content + "</span>";
		
		
		content = content.replaceAll("(2013[0-1][0-9][0-3][0-9])", bgc("green"));
		
		htmlPane.setEditable(false);
		htmlPane.setContentType("text/html");*/
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		htmlPane.setText(content);
		JScrollPane scrollPane = new JScrollPane(htmlPane);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
        closeButton = new JButton("Close");
        closeButton.addActionListener(al);
        JPanel panel = new JPanel();
        panel.add(closeButton, BorderLayout.WEST);
        selectFile = new JButton("Open file");
        selectFile.addActionListener(al);
        panel.add(selectFile, BorderLayout.EAST);
        getContentPane().add(panel, BorderLayout.SOUTH);
	    Dimension screenSize = getToolkit().getScreenSize();
	    int width = 700; //screenSize.width * 4 / 10;
	    int height = screenSize.height * 8 / 10;
	    setBounds(width/8, height/8, width, height);
	    setVisible(true);
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
                } else {
                    log.info("Open command cancelled by user.");
                }
            } else if (e.getSource() == closeButton) {
                    app.dispose();
            }
        }
    }

	public static void main(String[] args) throws IOException {
		String path = "Telepay/OK/";
		String file = "0-205087999878132  Familia  Telepay Direkte Remittering 1312  30.05.13  13-15  Innsendt.htm.telepay";
		String source = FileUtils
				.readFileToString(new File(path + file), "ISO_8859_1");
		new TelepayGUI(source);

	}

}

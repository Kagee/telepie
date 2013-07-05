package no.hild1.bank;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.io.IOException;

import javax.swing.*;

import org.apache.commons.io.FileUtils;

public class TelepayGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5121434288061327051L;
	private JEditorPane htmlPane;


	private static String bgc(String color) {
		return "<span style=\"background-color: " + color + ";\">$1</span>";
	}
    /**
     * @param content
     * @throws IOException
     */
	public TelepayGUI(String content) throws IOException {
		htmlPane = new JEditorPane();
		content = content.replaceAll("\n", "\u23CE<br>");
		content = content.replaceAll(" ", "&nbsp;");
		content = content.replaceAll("(BETFOR23)", bgc("gray"));
		content = "<span style='font-family: \"Courier New\", Courier, monospace;'>" + content + "</span>";
		
		
		content = content.replaceAll("(2013[0-1][0-9][0-3][0-9])", bgc("green"));
		
		htmlPane.setEditable(false);
		htmlPane.setContentType("text/html");
		htmlPane.setText(content);
		JScrollPane scrollPane = new JScrollPane(htmlPane);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
	    Dimension screenSize = getToolkit().getScreenSize();
	    int width = 700; //screenSize.width * 4 / 10;
	    int height = screenSize.height * 8 / 10;
	    setBounds(width/8, height/8, width, height);
	    setVisible(true);
	}

	public static void main(String[] args) throws IOException {
		String path = "Telepay-filer/";
		String file = "OK/testfil1.telepay";
		String source = FileUtils
				.readFileToString(new File(path + file), "ISO_8859_1");
		new TelepayGUI(source);

	}

}

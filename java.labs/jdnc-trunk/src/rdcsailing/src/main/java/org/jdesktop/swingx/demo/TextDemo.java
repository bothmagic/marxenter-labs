package org.jdesktop.swingx.demo;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.AbstractDocument;

public class TextDemo extends JFrame {

    public TextDemo() {
        super("Test");
        JTextArea area = new JTextArea(loadFile(),25,50);
        area.setLineWrap( true );
        area.setWrapStyleWord(true);
        JScrollPane pane = new JScrollPane(area);
        getContentPane().add(pane);
        pack();
        setVisible(true);
        ((AbstractDocument)area.getDocument()).dump(System.out);
    }

    private String loadFile() {
        String path = "/org/jdesktop/swingx/demo/text.txt";
        StringBuilder builder = new StringBuilder();
        InputStream is = null;
        try {
            is = TextDemo.class.getResourceAsStream(path);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader input =  new BufferedReader(isr);
            String line;
            while (( line = input.readLine()) != null){
                builder.append(line);
                builder.append(System.getProperty("line.separator"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try { if(is!=null) is.close(); } catch (Throwable t) {}
        }
        return builder.toString();
    }


    public static void main(String args[]) {
        new TextDemo();
    }
}

package org.jdesktop.swingx.util;

import java.io.File;
import org.jdesktop.swingx.spelling.Dictionary;

public class DictionaryBuilder {

    public static void main(String args[]) throws Exception {
        Dictionary dictionary = new Dictionary();
        dictionary.build(DictionaryBuilder.class.getResourceAsStream("/org/jdesktop/swingx/spelling/dictionary.txt"));
        dictionary.save(new File("/Users/rcuprak/dictionary.properties"));
    }
}

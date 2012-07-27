/**
 * Copyright 2010 Cuprak Enterprise LLC.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.jdesktop.swingx.spelling;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Dictionary of words to be used by spell checking along with their expected frequency.
 * @author Ryan Cuprak
 */
public class Dictionary {

    /**
     * Words contained within the dictionary
     */
    private Map<String,Integer> words;

    /**
     * Current locale
     */
    private Locale dictionaryLocale;

    /**
     * Creates a new dictionary. Uses the current locale
     * @throws SpellCheckerException - thrown if the dictionary isn't available
     */
    public Dictionary() throws SpellCheckerException {
        this(Locale.getDefault());
    }

    /**
     * Creates a new Dictionary with the specified dictionary.
     * @param locale - locale
     * @throws SpellCheckerException - thrown if the dictionary is unavailable.
     */
    public Dictionary(Locale locale) throws SpellCheckerException {
        dictionaryLocale = locale;
        String path = "/org/jdesktop/swingx/spelling/dictionary." + dictionaryLocale.toString() + ".properties";
        if(words == null) {
            words = new HashMap<String, Integer>();
            InputStream is = null;
            try {
                is = Dictionary.class.getResourceAsStream(path);
                if(is == null) {
                    throw new SpellCheckerException("Unable to load the dictionary: "+path);
                }
                Properties p = new Properties();
                p.load(is);
                Enumeration e = p.propertyNames();
                String key;
                while(e.hasMoreElements()) {
                    key = (String)e.nextElement();
                    words.put(key,Integer.parseInt(p.getProperty(key)));
                }
            } catch (IOException e) {
                throw new SpellCheckerException("Unable to resolve the dictionary: " + path);
            } finally {
                try { if(is!=null) is.close(); } catch (Throwable t) {}
            }
        }
    }

    /**
     * Returns the occurrence count for a given word.
     * Null is returns if there is no occurrence
     * @param word - word
     * @return occurrence count
     */
    public Integer getOccurrenceCount(String word) {
        return words.get(word);
    }

    /**
     * Returns the locale of the dictionary
     * @return dictionary locale
     */
    public Locale getDictionaryLocale() {
        return dictionaryLocale;
    }

    /**
     * Adds a word to a dictionary.
     * @param word - word
     */
    public void addWord(String word) {
        if(words.containsKey(word)) {
            int count = words.get(word);
            count++;
            words.put(word,count);
        } else {
            words.put(word,1);
        }
    }

    /**
     * Returns true if the word exists
     * @param word - word we are checking
     * @return true if the word exists
     */
    public boolean exists(String word) {
        return words.containsKey(word.toLowerCase());
    }

    /**
     * Builds a dictionary from the specified file
     * @param is - input stream
     * @throws java.io.IOException - thrown if there is an error reading the file
     */
    public void build(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String str;
        while((str = br.readLine()) != null) {
            sb.append(str.toLowerCase());
            sb.append("\n");
        }
        str = sb.toString();
        Pattern pattern = Pattern.compile("[a-z]+");
        Matcher m = pattern.matcher(str);
        while(m.find()) {
            addWord(m.group());
        }
    }

    /**
     * Saves the dictionary out to the specified path.
      * @param path - path where the dictionary is to be saved
     * @throws java.io.IOException - thrown if there is an error reading the file
     */
    public void save(File path) throws IOException {
        if(!path.createNewFile()) {
            throw new IOException("Unable to create the file: " + path.getAbsolutePath());
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path);
            Properties p = new Properties();
            for(Map.Entry<String,Integer> entry :words.entrySet()) {
                p.put(entry.getKey(),Integer.toString(entry.getValue()));
            }
            p.store(fos,"dictionary");
        } finally {
            if(fos != null) try { fos.close(); } catch (Exception e) {}
        }
    }
}

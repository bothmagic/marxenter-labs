/*
 * Created on 11.04.2007
 *
 */
package org.jdesktop.appframework.beansbinding.album;

import java.util.Arrays;
import java.util.List;

import org.jdesktop.beans.AbstractBean;


public class Album extends AbstractBean {
        
        // Examples ***************************************************************
        
        /** An example Album. */
        public static final Album ALBUM1 = createExample1();

        /** An example Album. */
        public static final Album ALBUM2 = createExample2();

        /** An example Album. */
        public static final Album ALBUM3 = createExample3();

        /** An example Album. */
        public static final Album ALBUM4 = createExample4();
        
        /** A List of Albums made of the examples 1 to 4. */
        public static final List<Album> ALBUMS = Arrays.asList(new Album[]{
                ALBUM1, ALBUM2, ALBUM3, ALBUM4});
        
        
        // Names of the Bound Bean Properties *************************************

        public static final String PROPERTYNAME_ARTIST    = "artist";
        public static final String PROPERTYNAME_CLASSICAL = "classical";
        public static final String PROPERTYNAME_COMPOSER  = "composer";
        public static final String PROPERTYNAME_TITLE     = "title";
        
        
        // Instance Fields ********************************************************
        
        /**
         * This Album's title as associated with its ISBN,
         * for example "Symphony No. 5".
         */
        private String title;
        
        /**
         * Holds this Album's artist, for example: "Albert Ayler",
         * or "Berliner Philharmoniker".
         */
        private String artist;

        /**
         * Describes if this Album is classical music; in this case
         * it has a composer.
         */
        private boolean classical;
        
        /**
         * Holds the composer of this Album's music, for example "Beethoven". 
         * Available if and only if this is a classical album.
         */
        private String composer;
        

        // Instance Creation ******************************************************
        
        /**
         * Constructs an empty Album: empty title and artist, not classical
         * and no composer set.
         */
        public Album() {
            this("", "");
        }
        
        
        private Album(String title, String artist) {
            setTitle(title);
            setArtist(artist);
            setClassical(false);
            setComposer(null);
        }
        
        
        private Album(String title, String artist, String composer) {
            setTitle(title);
            setArtist(artist);
            setClassical(true);
            setComposer(composer);
        }

        
        // Creating Example Instances *********************************************
        
        private static Album createExample1() {
            return new Album(
                    "A Love Supreme", 
                    "John Coltrane");
        }
        
        private static Album createExample2() {
            return new Album(
                    "In a Silent Way", 
                    "Miles Davis");
        }
        
        private static Album createExample3() {
            return new Album(
                    "Sheik Yerbouti", 
                    "Frank Zappa");
        }
        
        private static Album createExample4() {
            return new Album(
                    "Tristan und Isolde", 
                    "Berliner Philharmoniker",
                    "Richard Wagner");
        }
        
        

        // Accessors **************************************************************
        
        
        /**
         * Returns this album's title, for example "A Love Supreme",
         * or "Symphony No. 5".
         * 
         * @return this album's title.
         */
        public String getTitle() {
            return title;
        }
        

        /**
         * Returns this album's artist, for example "Albert Ayler"
         * or "Berliner Philharmoniker". 
         * 
         * @return this album's artist.
         */
        public String getArtist() {
            return artist;
        }
        
        
        /**
         * Answers whether this is a classical album or not.
         * 
         * @return true if this album is classical, false if not
         */
        public boolean isClassical() {
            return classical;
        }
        
        
        /**
         * Returns this album's composer - if any, for example "Richard Wagner".
         * A composer is available if and only if this is a classical album.
         * 
         * @return the composer of this album's music.
         * 
         * @see #isClassical
         */
        public String getComposer() {
            return composer;
        }
        
        

        /**
         * Sets this album's title and notifies observers 
         * if the title changed.
         * 
         * @param title   The title to set.
         */
        public void setTitle(String title) {
            Object oldValue = getTitle();
            this.title = title;
            firePropertyChange(PROPERTYNAME_TITLE, oldValue, title);
        }
        
        
        /**
         * Sets a new artist and notifies observers if the artist changed.
         * 
         * @param artist  The artist to set.
         */
        public void setArtist(String artist) {
            String oldValue = getArtist();
            this.artist = artist;
            firePropertyChange(PROPERTYNAME_ARTIST, oldValue, artist);
        }
        

        /**
         * Sets this album's classical property and notifies observers 
         * about changes. If not classical the composer is set to <code>null</code>.
         * 
         * @param classical   true to indicate that this album is classical
         */
        public void setClassical(boolean classical) {
            boolean oldValue = isClassical();
            this.classical = classical;
            firePropertyChange(PROPERTYNAME_CLASSICAL, oldValue, classical);
            if (!classical) {
                setComposer(null);
            }
        }
        

        /**
         * Sets this album's composer and notifies observers if it has changed.
         * A composer shall be set only if this is a classical album.
         * 
         * @param composer   The composer to set.
         * 
         * @see #isClassical
         */
        public void setComposer(String composer) {
            Object oldValue = getComposer();
            this.composer = composer;
            firePropertyChange(PROPERTYNAME_COMPOSER, oldValue, composer);
        }
        
        
        // Misc *******************************************************************
        
        /**
         * Returns a string representation of this album
         * that contains the property values in a single text line.
         * 
         * @return a string representation of this album
         */
        public String toString() {
            StringBuffer buffer = new StringBuffer("Album");
            buffer.append(" [title=");
            buffer.append(getTitle());
            buffer.append("; artist=");
            buffer.append(getArtist());
            buffer.append("; classical=");
            buffer.append(isClassical());
            buffer.append("; composer=");
            buffer.append(getComposer());
            buffer.append("]");
            return buffer.toString();
        }

        /**
         * Returns a string representation of this album
         * that contains the property values.
         * 
         * @return a string representation of this album
         */
        public String toWrappedString() {
            StringBuffer buffer = new StringBuffer("Album");
            buffer.append("[\ntitle=");
            buffer.append(getTitle());
            buffer.append(";\nartist=");
            buffer.append(getArtist());
            buffer.append(";\nclassical=");
            buffer.append(isClassical());
            buffer.append(";\ncomposer=");
            buffer.append(getComposer());
            buffer.append("\n]");
            return buffer.toString();
        }

    }

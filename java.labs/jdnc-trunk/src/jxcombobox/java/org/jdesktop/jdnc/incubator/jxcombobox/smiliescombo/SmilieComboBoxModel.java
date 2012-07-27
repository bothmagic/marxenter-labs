/*
 * EmoticonsComboBoxModel.java
 *
 * Created on 25 de Fevereiro de 2005, 17:46
 */

package org.jdesktop.jdnc.incubator.jxcombobox.smiliescombo;

import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author Lopes
 */
public class SmilieComboBoxModel extends DefaultComboBoxModel {
    
    public SmilieComboBoxModel() {
        super(DEFAULT_EMOTICONS);
    }
    
    /**
     * Tries to get the smilie for the supplied text
     * @param text the text of the smilie to find
     * @return the Smilie object or null if the smilie wasn't found
     */
    public Smilie getSmilie(String text) {
        int n = DEFAULT_EMOTICONS.length;
        for (int i=0; i<n; i++) {
            Smilie smilie = DEFAULT_EMOTICONS[i];
            if (smilie.text.equals(text)) {
                return smilie;
            }
        }
        return null;
    }
    
    /**
     * Default emoticons to use
     */
    protected static final Smilie[] DEFAULT_EMOTICONS =  {
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/regular.png", ":)"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/wink.png", ";)"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/tongue.png", ":P"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/megasmile.png", ":D"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/sad.png", ":("),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/cry.png", ":'("),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/angry.png", ":@"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/blank_look.png", ":|"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/embarrassed.png", ":$"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/omg.png", ":o"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/confused.png", ":s"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/shade.png", "(h)"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/bearingteeth.png", "8o|"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/party.png", "<o)"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/sick.png", "+o("),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/sleepy.png", "|)"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/thinking.png", "*-)"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/dunno.png", ":^)"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/sshh.png", ":#"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/secret.png", ":*"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/nerd.png", "8-|"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/eyeroll.png", "8-)"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/sarcastic.png", "^o)"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/angel.png", "(a)"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/devil.png", "(6)"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/sun.png", "(#)"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/moon.png", "(s)"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/star.png", "(*)"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/li.png", "(li)"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/rainbow.png", "(r)"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/ip.png", "(ip)"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/cat.png", "(@)"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/dog.png", "(&)"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/bat.png", ":["),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/tu.png", "(tu)"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/snail.png", "(sn)"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/sheep.png", "(bah)"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/beer.png", "(b)"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/cocktail.png", "(d)"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/cake.png", "(^)"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/cup.png", "(c)"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/pizza.png", "(pi)"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/pl.png", "(pl)"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/ll.png", "(||)"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/love.png", "(l)"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/unlove.png", "(u)"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/rose.png", "(f)"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/wilted_rose.png", "(w)"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/kiss.png", "(k)"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/present.png", "(g)"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/dude_hug.png", "({)"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/girl_hug.png", "(})"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/boy.png", "(z)"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/girl.png", "(x)"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/h5.png", "(h5)"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/yn.png", "(yn)"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/camera.png", "(p)"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/film.png", "(~)"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/note.png", "(8)"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/clock.png", "(0)"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/envelope.png", "(e)"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/handcuffs.png", "(%)"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/lightbulb.png", "(i)"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/phone.png", "(t)"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/mp.png", "(mp)"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/asl.png", "(?)"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/messenger.png", "(m)"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/co.png", "(co)"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/so.png", "(so)"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/um.png", "(um)"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/xx.png", "(xx)"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/coins.png", "(mo)"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/ci.png", "(ci)"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/ap.png", "(ap)"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/au.png", "(au)"),
        new Smilie("/org/jdesktop/jdnc/incubator/jxcombobox/smiliescombo/resources/brb.png", "(brb)")
    };
}


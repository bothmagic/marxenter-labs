/*
 * EmoticonsComboBoxModel.java
 *
 * Created on 25 de Fevereiro de 2005, 17:46
 */

package org.jdesktop.jdnc.incubator.rlopes.smiliescombo;

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
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/regular.png", ":)"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/wink.png", ";)"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/tongue.png", ":P"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/megasmile.png", ":D"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/sad.png", ":("),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/cry.png", ":'("),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/angry.png", ":@"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/blank_look.png", ":|"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/embarrassed.png", ":$"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/omg.png", ":o"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/confused.png", ":s"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/shade.png", "(h)"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/bearingteeth.png", "8o|"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/party.png", "<o)"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/sick.png", "+o("),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/sleepy.png", "|)"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/thinking.png", "*-)"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/dunno.png", ":^)"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/sshh.png", ":#"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/secret.png", ":*"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/nerd.png", "8-|"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/eyeroll.png", "8-)"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/sarcastic.png", "^o)"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/angel.png", "(a)"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/devil.png", "(6)"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/sun.png", "(#)"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/moon.png", "(s)"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/star.png", "(*)"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/li.png", "(li)"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/rainbow.png", "(r)"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/ip.png", "(ip)"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/cat.png", "(@)"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/dog.png", "(&)"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/bat.png", ":["),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/tu.png", "(tu)"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/snail.png", "(sn)"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/sheep.png", "(bah)"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/beer.png", "(b)"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/cocktail.png", "(d)"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/cake.png", "(^)"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/cup.png", "(c)"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/pizza.png", "(pi)"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/pl.png", "(pl)"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/ll.png", "(||)"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/love.png", "(l)"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/unlove.png", "(u)"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/rose.png", "(f)"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/wilted_rose.png", "(w)"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/kiss.png", "(k)"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/present.png", "(g)"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/dude_hug.png", "({)"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/girl_hug.png", "(})"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/boy.png", "(z)"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/girl.png", "(x)"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/h5.png", "(h5)"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/yn.png", "(yn)"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/camera.png", "(p)"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/film.png", "(~)"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/note.png", "(8)"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/clock.png", "(0)"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/envelope.png", "(e)"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/handcuffs.png", "(%)"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/lightbulb.png", "(i)"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/phone.png", "(t)"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/mp.png", "(mp)"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/asl.png", "(?)"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/messenger.png", "(m)"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/co.png", "(co)"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/so.png", "(so)"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/um.png", "(um)"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/xx.png", "(xx)"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/coins.png", "(mo)"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/ci.png", "(ci)"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/ap.png", "(ap)"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/au.png", "(au)"),
        new Smilie("/org/jdesktop/jdnc/incubator/rlopes/smiliescombo/resources/brb.png", "(brb)")
    };
}


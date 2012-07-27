package org.jdesktop.jdnc.incubator.vprise.net.http;

import org.jdesktop.jdnc.incubator.vprise.i18n.*;
import org.jdesktop.jdnc.incubator.vprise.net.*;
import java.net.*;
import java.util.*;
import java.io.*;
import java.util.logging.*;
import java.lang.reflect.*;

/**
 * A CommunicationManager on top of http based on serialization and basic security.
 * This can be layered on HTTPs, basic security is the simplest form of authentication
 * for Swing clients.
 *
 * @author Shai Almog
 */
public class HttpCommunicationManager extends CommunicationManager {
    /**
     * This is the url of the servlet to which we submit requests
     */
    private URL serverURL;
    
    /**
     * This member indicates if a read operation was ever successful
     * and thus helps us pin down whether an occuring problem could
     * be an authentication problem
     */ 
    private boolean everSuccessful = false;

    /**
     * This is the cached username/password
     */
    private static PasswordAuthentication auth;

    /**
     * This member indicates whether the authentication class was
     * ever invoked and so if everSuccessful is false and this is 
     * true then the problem is probably within the authentication code.
     */
    private boolean wasAuthenticated = false;
    
    /**
     * Doesn't let recursion get too deep
     */
    private int recurseLevel = 0;
    
    /**
     * Registers the appropriate default authenticator
     */
    public HttpCommunicationManager() {
        Authenticator.setDefault(new SimpleAuthenticator());
    }


    /**
     * Loggs off the current user
     */ 
    public void logoff() {
        // todo, logoff call to the server, this doesn't really exist in basic
        // security
        auth = null;
        wasAuthenticated = false;
        recurseLevel = 0;
        everSuccessful = false;
        super.logoff();
    }

    /**
     * This method performs the synchronious request.
     */
    protected Object performRequest(Object request) throws CommunicationException {
        Object value;
        try {
            HttpURLConnection connection = (HttpURLConnection)serverURL.openConnection();
            if(auth != null) {
                String authorizationString = "Basic " + Base64.encode( auth.getUserName() + ':' + new String(auth.getPassword()) );
                connection.setRequestProperty( "Authorization", authorizationString );
            }
            connection.setDoInput(true);
            connection.setDoOutput(true);
            ObjectOutputStream output = new ObjectOutputStream(connection.getOutputStream());
            output.writeObject((java.io.Serializable)request);
            output.close();

            ObjectInputStream data = new ObjectInputStream(connection.getInputStream());
            value = data.readObject();
            data.close();
            connection.disconnect();
            everSuccessful = true;
            return(value);
        } catch(IOException ioErr) {
            if(recurseLevel < 30) {
                recurseLevel++;
                Object message = handleAuthenticationProblem(request);
                if(message != null) {
                    return(message);
                }
            }
            recurseLevel = 0;
            ioErr.printStackTrace();
            throw(new CommunicationException(ioErr.getMessage()));
        } catch(ClassNotFoundException classErr) {
            classErr.printStackTrace();
            throw(new CommunicationException(classErr.getMessage()));
        }
    }
    
    /**
     * This method checks whether the communication problem could be
     * caused by authentication and so it retries.
     */
    private Object handleAuthenticationProblem(Object request) throws CommunicationException {
        if(!everSuccessful) {
            // probably a wrong password. retry
            auth = promptUserPassword();
            return(performRequest(request));
        }
        return(null);
    }

    /**
     * This method is invoked when it is clear that the user password isn't
     * supplied. It tries to login and reinvokes performRequest()
     */
    protected PasswordAuthentication promptUserPassword() throws CommunicationException {
        if(recurseLevel > 0) {
            return(super.promptUserPassword("exception.failed_login", null));
        } else {
            return(super.promptUserPassword(null, null));
        }
    }
    
    /**
     * Implementations may define attributes for the communcation. The HTTP
     * implementation defines one attribute serverURL.
     * Any other key is simply ignored. The reason for using this method rather
     * than System.getProprty() is to avoid the need for security manager 
     * permissions and to allow the application not to depend on API's such
     * as webstart JNLP.
     */
    public void setCommunicationAttribute(String key, String value) {
        if(key.equals("serverURL")) {
            try {
                serverURL = new URL(value);
                return;
            } catch(MalformedURLException err) {
                err.printStackTrace();
                throw( new IllegalArgumentException(Resources.getString("serverURL_is_not_a_valid_URL", new Object[] { value })) );
            }
        }
    }

    /**
     * Tries to login to the server. This implementation does nothing since http
     * authentication is handled by the authenticator.
     */
    protected void login(String username, char[] password) throws CommunicationException {
    }        

    /**
     * Implements a custom authenticator that pops up a custom login dialog
     */
    class SimpleAuthenticator extends Authenticator {        
        /**
         * Called when password authorization is needed.
         */
        protected PasswordAuthentication getPasswordAuthentication() {
            return(new PasswordAuthentication("", new char[0]));
        }     
    }
}

/**
 * Static methods for translating Base64 encoded strings to byte arrays
 * and vice-versa.
 *
 * @author  Josh Bloch
 * @version 1.5, 12/19/03
 * @see     Preferences
 * @since   1.4
 */
class Base64 {
    private static String byteArrayToBase64(byte[] a, boolean alternate) {
        int aLen = a.length;
        int numFullGroups = aLen/3;
        int numBytesInPartialGroup = aLen - 3*numFullGroups;
        int resultLen = 4*((aLen + 2)/3);
        StringBuffer result = new StringBuffer(resultLen);
        char[] intToAlpha = (alternate ? intToAltBase64 : intToBase64);

        // Translate all full groups from byte array elements to Base64
        int inCursor = 0;
        for (int i=0; i<numFullGroups; i++) {
            int byte0 = a[inCursor++] & 0xff;
            int byte1 = a[inCursor++] & 0xff;
            int byte2 = a[inCursor++] & 0xff;
            result.append(intToAlpha[byte0 >> 2]);
            result.append(intToAlpha[(byte0 << 4)&0x3f | (byte1 >> 4)]);
            result.append(intToAlpha[(byte1 << 2)&0x3f | (byte2 >> 6)]);
            result.append(intToAlpha[byte2 & 0x3f]);
        }

        // Translate partial group if present
        if (numBytesInPartialGroup != 0) {
            int byte0 = a[inCursor++] & 0xff;
            result.append(intToAlpha[byte0 >> 2]);
            if (numBytesInPartialGroup == 1) {
                result.append(intToAlpha[(byte0 << 4) & 0x3f]);
                result.append("==");
            } else {
                // assert numBytesInPartialGroup == 2;
                int byte1 = a[inCursor++] & 0xff;
                result.append(intToAlpha[(byte0 << 4)&0x3f | (byte1 >> 4)]);
                result.append(intToAlpha[(byte1 << 2)&0x3f]);
                result.append('=');
            }
        }
        // assert inCursor == a.length;
        // assert result.length() == resultLen;
        return result.toString();
    }

    /**
     * This array is a lookup table that translates 6-bit positive integer
     * index values into their "Base64 Alphabet" equivalents as specified 
     * in Table 1 of RFC 2045.
     */
    private static final char intToBase64[] = {
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
        'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
        'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'
    };

    /**
     * This array is a lookup table that translates 6-bit positive integer
     * index values into their "Alternate Base64 Alphabet" equivalents.
     * This is NOT the real Base64 Alphabet as per in Table 1 of RFC 2045.
     * This alternate alphabet does not use the capital letters.  It is
     * designed for use in environments where "case folding" occurs.
     */
    private static final char intToAltBase64[] = {
        '!', '"', '#', '$', '%', '&', '\'', '(', ')', ',', '-', '.', ':',
        ';', '<', '>', '@', '[', ']', '^',  '`', '_', '{', '|', '}', '~',
        'a', 'b', 'c', 'd', 'e', 'f', 'g',  'h', 'i', 'j', 'k', 'l', 'm',
        'n', 'o', 'p', 'q', 'r', 's', 't',  'u', 'v', 'w', 'x', 'y', 'z',
        '0', '1', '2', '3', '4', '5', '6',  '7', '8', '9', '+', '?'
    };

    /**
     * Translates the specified character, which is assumed to be in the
     * "Base 64 Alphabet" into its equivalent 6-bit positive integer.
     *
     * @throw IllegalArgumentException or ArrayOutOfBoundsException if
     *        c is not in the Base64 Alphabet.
     */
    private static int base64toInt(char c, byte[] alphaToInt) {
        int result = alphaToInt[c];
        if (result < 0)
            throw new IllegalArgumentException("Illegal character " + c);
        return result;
    }

    /**
     * This array is a lookup table that translates unicode characters
     * drawn from the "Base64 Alphabet" (as specified in Table 1 of RFC 2045)
     * into their 6-bit positive integer equivalents.  Characters that
     * are not in the Base64 alphabet but fall within the bounds of the
     * array are translated to -1.
     */
    private static final byte base64ToInt[] = {
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54,
        55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4,
        5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23,
        24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34,
        35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51
    };

    /**
     * This array is the analogue of base64ToInt, but for the nonstandard
     * variant that avoids the use of uppercase alphabetic characters.
     */
    private static final byte altBase64ToInt[] = {
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 1,
        2, 3, 4, 5, 6, 7, 8, -1, 62, 9, 10, 11, -1 , 52, 53, 54, 55, 56, 57,
        58, 59, 60, 61, 12, 13, 14, -1, 15, 63, 16, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, 17, -1, 18, 19, 21, 20, 26, 27, 28, 29, 30, 31, 32, 33,
        34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50,
        51, 22, 23, 24, 25
    };

    /**
     * This method was removed due to copyright issues
     */
    public static String encode( String source ) {
        try {
            return byteArrayToBase64(source.getBytes("UTF-8"), false);
        } catch(UnsupportedEncodingException err) {
            err.printStackTrace();
            return byteArrayToBase64(source.getBytes(), false);
        }
    }
}
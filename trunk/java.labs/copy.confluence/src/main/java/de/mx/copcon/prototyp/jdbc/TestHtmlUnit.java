/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mx.copcon.prototyp.jdbc;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marxma
 */
public class TestHtmlUnit {
    
    
    public static void main(String[] args) {
        try {
            final WebClient webClient = new WebClient();
            webClient.setJavaScriptEnabled(false);
            final HtmlPage page = (HtmlPage)webClient.getPage("http://10.96.18.170:8090/login.action");
            
            final HtmlTextInput user = (HtmlTextInput)page.getHtmlElementById("os_username");
            final HtmlPasswordInput password = (HtmlPasswordInput)page.getHtmlElementById("os_password");
            user.setValueAttribute("chefe");
            password.setValueAttribute("erdgasi");
            final HtmlSubmitInput button = (HtmlSubmitInput) page.getHtmlElementById("loginButton");
            button.click();
            
            final HtmlPage page1 = (HtmlPage)webClient.getPage("http://10.96.18.170:8090/pages/templates2/createpagetemplate.action?key=team3");
            final HtmlTextInput title = (HtmlTextInput)page1.getHtmlElementById("content-title");
            title.setValueAttribute("test4565");
            final HtmlButton add = (HtmlButton)page1.getHtmlElementById("rte-button-publish");
            add.click();
        } catch (IOException ex) {
            Logger.getLogger(TestHtmlUnit.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FailingHttpStatusCodeException ex) {
            Logger.getLogger(TestHtmlUnit.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
    }
}

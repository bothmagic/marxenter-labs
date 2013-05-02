package de.mmx.etester.RetrieveData;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import javax.ws.rs.core.MediaType;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        MainDialog app = new MainDialog();
        app.setVisible(true);
        
    }
    
}



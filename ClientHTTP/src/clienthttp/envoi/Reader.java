/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clienthttp.envoi;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tete2
 */
public class Reader {    
    
    private static final String COOKIES_HEADER = "Set-Cookie";    
    private static java.net.CookieManager msCookieManager;
    
    public String readPage(HttpURLConnection con, BufferedReader in) {

        StringBuilder content = new StringBuilder();
        String s;
        String inputLine;
        try {
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                Map<String, List<String>> headers = con.getHeaderFields();
                for (Map.Entry<String, List<String>> header : headers.entrySet()) {
                    System.out.println(header.getKey() + ": " + header.getValue());
                }
                Cookie.saveCookies(con);
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Send.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return content.toString();
        }
    }
}

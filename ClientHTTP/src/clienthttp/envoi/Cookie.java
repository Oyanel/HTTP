/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clienthttp.envoi;

import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tete2
 */
public class Cookie {
    
    //cookies    
    private static final String COOKIES_REQUEST_HEADER = "Cookie"; 
    private static final String COOKIES_RESPONSE_HEADER = "Set-Cookie";
    private static java.net.CookieManager msCookieManager;

    public static void saveCookies(HttpURLConnection con) {
        msCookieManager = new java.net.CookieManager();
        Map<String, List<String>> headerFields = con.getHeaderFields();
        List<String> cookiesHeader = headerFields.get(COOKIES_RESPONSE_HEADER);
        if (cookiesHeader != null) {
            for (String cookie : cookiesHeader) {
                try {
                    msCookieManager.getCookieStore().add(new URI(con.getURL().getPath()), HttpCookie.parse(cookie).get(0));
                } catch (URISyntaxException ex) {
                    Logger.getLogger(Send.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    public static void sendCookie(HttpURLConnection con) {
        msCookieManager = new java.net.CookieManager();
        Map<String, List<String>> headerFields = con.getHeaderFields();
        List<String> cookiesHeader = headerFields.get(COOKIES_REQUEST_HEADER);
        int nbCookies = msCookieManager.getCookieStore().getCookies().size();
        if (nbCookies > 0) {
            String temp = new String();
            // While joining the Cookies, use ',' or ';' as needed. Most of the servers are using ';'
            for (int i = 0; i < nbCookies; i++) {
                temp.concat(msCookieManager.getCookieStore().getCookies().get(i).toString());
            }
            con.setRequestProperty("Cookie", temp);
        }
    }
}

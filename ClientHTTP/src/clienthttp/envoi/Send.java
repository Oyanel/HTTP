/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clienthttp.envoi;

import UI.BrowseUI;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Epulapp
 */
public class Send {

    private static String _URL = "localhost";
    private static int _PORT = 8080;
    private static String _PAGE = "";
    private static String _RESPONSE;
    private static HttpURLConnection con;
    private static DataOutputStream out;
    private static BufferedReader in;
    //cookies
    private static java.net.CookieManager msCookieManager;
    private static final String COOKIES_HEADER = "Set-Cookie";

    public static String getURL() {
        return _URL;
    }

    public static void setURL(String _URL) {
        Send._URL = _URL;
    }

    public static int getPORT() {
        return _PORT;
    }

    public static void setPORT(int _PORT) {
        Send._PORT = _PORT;
    }

    public static String getPAGE() {
        return _PAGE;
    }

    public static void setPAGE(String _PAGE) {
        Send._PAGE = _PAGE;
    }

    public static String getRESPONSE() {
        return _RESPONSE;
    }

    public static void setRESPONSE(String _RESPONSE) {
        Send._RESPONSE = _RESPONSE;
        if (BrowseUI.getWindows().length > 0) {
            BrowseUI window = (BrowseUI) BrowseUI.getWindows()[0];
            window.setJPane1(_RESPONSE);
        }
    }

    public static HttpURLConnection getCon() {
        return con;
    }

    public static DataOutputStream getOut() {
        return out;
    }

    public static void setOut(DataOutputStream out) {
        Send.out = out;
    }

    public static BufferedReader getIn() {
        return in;
    }

    public static void setIn(BufferedReader in) {
        Send.in = in;
    }

    public Send() throws Exception {
    }

    public void run() {

        try {
            URL url = new URL("http://" + _URL + ":" + _PORT + "/" + _PAGE);
            con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            System.out.println("Tentative de connexion vers " + _URL + "...\n\r");

            //creation des flux entrants et sortants
            con.setDoOutput(true);
            out = new DataOutputStream(con.getOutputStream());
            in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));

            //écriture et envoi de la requete
            System.out.println("Envoi de la requete");
            sendGetRequest();

            //attente de a réponse
            this.setRESPONSE(readPage());
            System.out.println(_RESPONSE);

            in.close();
            con.disconnect();
        } catch (IOException ex) {
            Logger.getLogger(Send.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
    }

    private String readPage() {

        StringBuilder content = new StringBuilder();
        try {
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                Map<String, List<String>> headers = con.getHeaderFields();
                for (Entry<String, List<String>> header : headers.entrySet()) {
                    System.out.println(header.getKey() + ": " + header.getValue());
                }
                String s;
                String inputLine;
                saveCookies();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Send.class.getName()).log(Level.SEVERE, null, ex);
        }
        return content.toString();
    }

    private static void sendGetRequest() {
        try {
            sendCookie();
            out.flush();
            out.close();
        } catch (ProtocolException ex) {
            Logger.getLogger(Send.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Send.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void closeConnexion() {
        try {
            out.write("CLOSE".getBytes());
            out.flush();
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(Send.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void saveCookies() {
        Map<String, List<String>> headerFields = con.getHeaderFields();
        List<String> cookiesHeader = headerFields.get(COOKIES_HEADER);
        if (cookiesHeader != null) {
            for (String cookie : cookiesHeader) {
                try {
                    msCookieManager.getCookieStore().add(new URI(getFullURL()), HttpCookie.parse(cookie).get(0));
                } catch (URISyntaxException ex) {
                    Logger.getLogger(Send.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private String getFullURL() {
        return "http://" + _URL + ":" + _PORT + "/" + _PAGE;
    }

    private static void sendCookie() {
        msCookieManager = new java.net.CookieManager();
        Map<String, List<String>> headerFields = con.getHeaderFields();
        List<String> cookiesHeader = headerFields.get(COOKIES_HEADER);
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

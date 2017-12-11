/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clienthttp.envoi;

import UI.BrowseUI;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
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
    private static final String _CONTENT_TYPE = "Content-type";
    private static final String _HTML = "text/html";
    private static final String _IMAGE = "image/png";
    private static final String _STREAM = "application/octet-stream";
    private static HttpURLConnection con;
    private static DataOutputStream out;
    private static BufferedReader in;

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
            window.setJPane1Text(_RESPONSE);
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
            con.setConnectTimeout(3000);
            con.setReadTimeout(3000);
            System.out.println("Tentative de connexion vers " + _URL + "...\n\r");

            //creation des flux entrants et sortants
            con.setDoOutput(true);
            out = new DataOutputStream(con.getOutputStream());
            in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));

            //écriture et envoi de la requete
            System.out.println("Envoi de la requete");
            sendGetRequest();
            if("HTTP".equals(con.getHeaderField("Protocol"))){
                
            }
            //attente de a réponse
            Reader reader = new Reader();
            if (_HTML.equals(con.getHeaderField(_CONTENT_TYPE))) {
                this.setRESPONSE(reader.readPage(con, in));
            } else if (_IMAGE.equals(con.getHeaderField(_CONTENT_TYPE)) || _STREAM.equals(con.getHeaderField(_CONTENT_TYPE))) {
                Download.downloadFile(url, con.getInputStream());
                this.setRESPONSE("Fichier téléchargé :" + Download.DOWNLOAD_DIRECTORY + "\\" + url.getFile().substring(url.getFile().lastIndexOf('/') + 1));
            } else {                
                this.setRESPONSE("Type de contenu non géré par le client");
            }
            System.out.println(_RESPONSE);

            in.close();
            con.disconnect();
            closeConnexion();
        } catch (IOException ex) {
            Logger.getLogger(Send.class.getName()).log(Level.SEVERE, null, ex);
            setRESPONSE(con.getErrorStream().toString());
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
    }

    private static void sendGetRequest() {
        try {
            Cookie.sendCookie(con);
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
}

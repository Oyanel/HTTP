/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clienthttp.envoi;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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

    private final String _URL = "http://localhost";
    private final int _PORT = 8080;
    private HttpURLConnection con;
    private DataOutputStream out;
    private BufferedReader in;

    public Send() throws Exception {
        URL url = new URL(_URL + ":" + _PORT);
        System.err.println("Lancement du traitement de la connexion cliente");
        con = (HttpURLConnection) url.openConnection();
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);
        System.out.println("connected");

        //creation des flux entrants et sortants
        con.setDoOutput(true);
        con.setDoInput(true);
        out = new DataOutputStream(con.getOutputStream());
        in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));

        //écriture et envoi de la requete
        System.out.println("Envoi de la requete");
        sendGetRequest();

        //attente de a réponse
        readPage();

        in.close();
        con.disconnect();
    }

    private void readPage() {
        try {
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String s;
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Send.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void sendGetRequest() {
        try {
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            System.out.println(out.toString());
            out.flush();
            out.close();
        } catch (ProtocolException ex) {
            Logger.getLogger(Send.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Send.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void closeConnexion(PrintWriter writer) {
        writer.write("CLOSE");
        writer.flush();
        writer.close();
    }

    private boolean isError(String error) {
        return true;
    }
}

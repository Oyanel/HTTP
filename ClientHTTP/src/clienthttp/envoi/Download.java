/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clienthttp.envoi;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 *
 * @author epulapp
 */
public class Download {

    public static final String DOWNLOAD_DIRECTORY = "C:\\\\Users\\tete2\\";
    
    public static void downloadFile(URL url, BufferedReader reader) {        
        //InputStream input = new InputStream();
        FileOutputStream writeFile = null;
        try {
            String fileName = url.getFile().substring(url.getFile().lastIndexOf('/') + 1);
            writeFile = new FileOutputStream(fileName);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = input.read(buffer)) > 0) {
                writeFile.write(buffer, 0, read);
            }
            writeFile.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writeFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

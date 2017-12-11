/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clienthttp.envoi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 *
 * @author epulapp
 */
public class Download {

    public static final String DOWNLOAD_DIRECTORY = "C:" + File.separator + "Users" + File.separator + "Epulapp" + File.separator + "Documents" + File.separator + "Cours" + File.separator + "2017" + File.separator + "ARSIR";

    public static void downloadFile(URL url, InputStream in) {
        //InputStream input = new InputStream();
        FileOutputStream writeFile = null;
        try {
            String fileName = url.getFile().substring(url.getFile().lastIndexOf('/') + 1);
            writeFile = new FileOutputStream(DOWNLOAD_DIRECTORY + File.separator + fileName);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) > 0) {
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

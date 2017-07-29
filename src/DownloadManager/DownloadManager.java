package DownloadManager;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

/**
 * Downloader
 * Downloads file from internet and save at give url
 * @author Hunger-Sona
 *
 */
public class DownloadManager {
    /**
     * Defualt Constructor:
     * 
     */
    public DownloadManager(){
        
    }
    
    public boolean download(String webUrl, String destUrl){
        // download file
        byte[] response = null;
        try{
            // COPY FROM STACK OVERFLOW
            URL url = new URL(webUrl);
            InputStream in = new BufferedInputStream(url.openStream());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int n = 0;
            while ( -1 != (n=in.read(buf)))
            {
               out.write(buf, 0, n);
            }
            out.close();
            in.close();
            response = out.toByteArray();
        }
        catch(Exception e){
            System.out.print("Download error: " + e.getMessage());
            return false;
        }
        //save image
        try{
            FileOutputStream fos = new FileOutputStream(destUrl);
            fos.write(response);
            
            fos.close();
        }
        catch(Exception e){
            System.out.print("Save error: " + e.getMessage());
            return false;
        }
        
        return true;
    }
}

package Scraper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * PlainScraper class:
 * scraps class with old method:
 *   downloading plainHtml and parsing it
 * 
 * @author Lasha Kharshiladze
 *
 */

public class PlainScraper implements Scraper {
    private static final String MALFORMED_URL = "MAL_URL";
    private static final String IO_EXCEPTION  = "IOException";
    
    BufferedReader in;
    URL curUrl;
    
    public PlainScraper(){
        System.out.println(getHtml("https://facebook.com"));
            
        
    }
    public static void  main(String[] args){
        Scraper scraper = new PlainScraper();
    }
    /**
     * return html source of url
     * @param url
     * @return
     */
    private String getHtml(String url){
        String html = "";
        try {
            curUrl = new URL(url);
            in = new BufferedReader(new InputStreamReader(curUrl.openStream()));
            String line = "";
            while( (line = in.readLine()) != null){
                html += line;
            }
        } catch (MalformedURLException e) {
           return MALFORMED_URL;
        } catch (IOException e) {
            return IO_EXCEPTION;
        }
        
        return html;
    }
    
    @Override
    public List<String> getImageSrcs(String url) {
        
        return null;
    }

    @Override
    public List<String> getPageLinks(String url) {
        
        return null;
    }

    @Override
    public String checkUrl(String url) {
        
        return null;
    }
    
}

import java.io.IOException;
import java.util.*;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


/**
 * SoupScrapper Class:
 *  this class is responsible for pull down all image and page urls;
 *  this realization of Scraper uses JSoup for parsing html document
 * 
 * @author Lasha Kharshiladze
 */
public class SoupScraper {
    // Links that are not meaninfull for example: '#', "javascript:void(0)"
    private final Set<String> bannedLinks;
    
    /**
     * Default Constructor
     */
    public SoupScraper(){
        bannedLinks = new HashSet<String>();
        bannedLinks.add("#");
        bannedLinks.add("javascript:void(0)");
    }
    
    /**
     * gets url of web page and pull downs all page links
     * 
     * @param url
     * @return Set of links that occur on that page,
     * nulls if IOException is thrown while connecting
     */
    public Set<String> getPageLinks(String url){
        
        Set<String> pageLinks  = new HashSet<String>();

        try {
            Connection  connection = Jsoup.connect(url);
            Document document = connection.get();
            pageLinks = parseDocument(document, "a[href]", "href");
        } catch (IOException e) {
            System.out.println("There was error:" + e.getMessage());
            return null;
        }
        catch(Exception e){
            System.out.println("There was error:" + e.getMessage());
            return null;
        }
        
        return pageLinks;
    }
    
    /**
     * parses HTML document and returns list of attributes for selected cssQuery
     * 
     * @param document
     * @param cssQuery
     * @param attr
     * @return  list of attributes for selected cssQuery
     */
    private Set<String> parseDocument(Document document, String cssQuery, String attr){
        Set<String> result = new HashSet<String>();
        Elements elements = document.select(cssQuery);
        
        for(Element element: elements){
            if(bannedLinks.contains(element.attr(attr)))
                continue;
            result.add(element.attr(attr));
        }
        
        return result;
        
    }
    
    

}

package Scraper;

import java.util.List;

/**
 * Scraper Interface:
 * scraper can pull down all image scrs from url;
 * scraper can pull down all page link
 * scraper checks if connecting for url is possible
 * 
 * @author Lasha Kharshiladze
 *
 */
public interface Scraper {
    public static final String OK = "GOOD_URL[#12314124sdmaslsad]";
    
    /**
     * gets url of web page and pull downs all page links
     * 
     * @param url
     * @return Set of links that occur on that page,
     * nulls if IOException is thrown while connecting
     */
    public List<String> getImageSrcs(String url);
    
    /**
     * gets url of web page and pull downs all page links
     * 
     * @param url
     * @return Set of links that occur on that page,
     * nulls if IOException is thrown while connecting
     */
    public List<String> getPageLinks(String url);
    
    /**
     * Checks if give url responds request
     * return Scraper.OK if connection succssed
     * error text Otherwise
     * @param url
     */
    public String checkUrl(String url);
};

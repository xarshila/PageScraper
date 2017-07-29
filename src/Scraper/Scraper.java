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
    
    public List<String> getImageSrcs(String url);
    public List<String> getPageLinks(String url);
    public String checkUrl(String url);
};

import java.util.*;
public class WebScraperApp {
    private static final String SENTINEL = "quit";
    
    
    public static void main(String[] args){
        System.out.println("Welcome WebScraper Console Applcation!");
        System.out.println("-------------------------------------");
        System.out.println("Write arbitrary url and Scraper lists down \n"
                 + "all Page Links on that URL (wirte " + SENTINEL +" for exit)");
        
        SoupScraper scraper = new SoupScraper();
        Scanner sc = new Scanner(System.in);
        
        while(true){
            System.out.print("URL:");
            String url = sc.nextLine();
            if(url.equals(SENTINEL))
                break;
            List<String> pageLinks = scraper.getImageSrcs(url);
            if(pageLinks == null){
                continue;
            }
            printUrlList(pageLinks);
        }
        sc.close();
    }
    
    /**
     * prints given List with indexes
     * @param urls
     */
    private static void printUrlList(List<String> urls){
        int ind = 1;
        for(String url: urls){
            System.out.println(ind + ") "+ url);
            ind++;
        }
    }
}

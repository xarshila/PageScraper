package Console;
import java.util.*;

import Scraper.Scraper;
import Scraper.SoupScraper;
public class WebScraperConsoleApp {
    /* Constants */
    private static final String SENTINEL    = "QUIT";
    private static final String IMG_OPTION  = "IMG";
    private static final String LINK_OPTION = "LINK";
    
    Scraper scraper;
    Scanner sc ;
    
    /**
     * Default Construct:
     * runs maim menu 
     */
    public WebScraperConsoleApp(){
        printHeader();
        scraper = new SoupScraper();
        sc = new Scanner(System.in);
        runMainMenu();
        sc.close();
    }
    
    /**
     * Console Application for Web Scrapper
     * @param args
     */
    public static void main(String[] args){
        WebScraperConsoleApp  app = new WebScraperConsoleApp();
    }
    
    /**
     * Print Welocme Text and Instruction
     */
    private void printHeader(){
        System.out.println("Welcome WebScraper Console Applcation!");
    }
    
    /**
     * Take Options you what scrap images or URLS
     */
    private  void runMainMenu(){
        while(true){
            System.out.println("-------------  MAIN MENU  --------------");
            System.out.println("Write '" + LINK_OPTION + "' for link Scrapin");
            System.out.println("Write '" + IMG_OPTION + "' for Image Scrapin");
            System.out.println("Write '" + SENTINEL + "' for esc");
            System.out.println("-----------------------------------------");
            
            String line = sc.nextLine();
            if(line.equals(SENTINEL))
                break;
            if(line.equals(LINK_OPTION))
                runLinkScraping();
            if(line.equals(IMG_OPTION))
                runImageScraping();
        }
    }
    
    /**
     * Reads URL from console and lists Image scrcs
     * for that url
     */
    private void runImageScraping(){
        System.out.println("---------- Image Scraping -----------");
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
    }
    
    /**
     * Reads URL from console and lists Image scrcs
     * for that url
     */
    private void runLinkScraping(){
        System.out.println("---------- Link Scraping -----------");
        while(true){
            System.out.print("URL:");
            String url = sc.nextLine();
            if(url.equals(SENTINEL))
                break;
            List<String> pageLinks = scraper.getPageLinks(url);
            if(pageLinks == null){
                continue;
            }
            printUrlList(pageLinks);
        }
    }
    
    
    /**
     * prints given List with indexes
     * @param urls
     */
    private static void printUrlList(List<String> urls){
        System.out.println("----------" + urls.size() + " Result Founded --------");
        int ind = 1;
        for(String url: urls){
            System.out.println(ind + ") "+ url);
            ind++;
        }
        System.out.println("------------------------------------------------------");
        System.out.println();
    }
    
    
}

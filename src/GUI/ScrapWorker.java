package GUI;
import java.util.List;

import javax.swing.SwingUtilities;

import GUI.ResultTableModel;
import Scraper.SoupScraper;

/**
 * ScrapWorker class is responsible for:
 *  -> scraping given url in new Thread
 *  -> adding results in resultTableModel from Swing Thread
 * 
 * @author Lasha Kharshiladze
 */
class ScrapWorker implements Runnable{
        private static final int SCRAP_DELAY = 80;
        
        // references from outside
        String           url;
        SoupScraper      scraper;
        ResultTableModel resultTableModel;
        boolean images;
        boolean links;
        
        
        /**
         * Defualt Constructor
         * 
         * @param resultTableModel
         * @param scraper
         * @param url
         * @param images
         * @param links
         */
        public ScrapWorker(ResultTableModel resultTableModel, SoupScraper scraper,String url, boolean images, boolean links){
            this.url     = url;
            this.scraper = scraper;
            this.images  = images;
            this.links   = links;
            this.resultTableModel = resultTableModel;
        }
        
        @Override
        public void run(){
            if(images)
                scrapImages(url);
            if(links)
                scrapLinks(url);
        }
        
        /**
         * Scraps image for url in new Thread
         * @param url to scrap
         */
        private void scrapImages(String url){
            List<String> imageSrcs = scraper.getImageSrcs(url);
            if(imageSrcs == null)
                return; 
            for(String src : imageSrcs){
                try {
                    Thread.sleep(SCRAP_DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                SwingUtilities.invokeLater(new Runnable(){
                    @Override
                    public void run() {
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                               resultTableModel.add(src, "img");
                            }
                        });
                    }
                    
                });
            }
        }
        
        /**
         * Scraps link for url in new Thread
         * @param url to scrap for links
         */
        public void scrapLinks(String url){
            List<String> pageLinks = scraper.getPageLinks(url);
            if(pageLinks == null)
                return;
            for(String link : pageLinks){
                try {
                    Thread.sleep(SCRAP_DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                SwingUtilities.invokeLater(new Runnable(){
                    @Override
                    public void run() {
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                               resultTableModel.add(link, "link");
                            }
                        });
                    }
                    
                });
            }
        }
    }
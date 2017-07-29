package GUI;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JLabel;
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
        
        Semaphore sem;
         
        // references from outside
        String           url;
        SoupScraper      scraper;
        ResultTableModel resultTableModel;
        boolean images;
        boolean links;
        JLabel statusLabel;
        int linkNum;
        int imgNum;
        /**
         * Defualt Constructor
         * 
         * @param resultTableModel
         * @param scraper
         * @param url
         * @param images
         * @param links
         */
        public ScrapWorker(ResultTableModel resultTableModel, SoupScraper scraper, String url, JLabel statusLabel, boolean images, boolean links){
            this.url     = url;
            this.scraper = scraper;
            this.images  = images;
            this.links   = links;
            this.resultTableModel = resultTableModel;
            this.statusLabel      = statusLabel;
            this.linkNum = 0;
            this.imgNum = 0;
        }
        public ScrapWorker(){
            sem = new Semaphore(0);
        }
        public void update(ResultTableModel resultTableModel, SoupScraper scraper, String url, JLabel statusLabel, boolean images, boolean links){
            this.url     = url;
            this.scraper = scraper;
            this.images  = images;
            this.links   = links;
            this.resultTableModel = resultTableModel;
            this.statusLabel      = statusLabel;
            this.linkNum = 0;
            this.imgNum = 0;
        }
        
        @Override
        public void run(){
            while(true){
                try {
                    sem.acquire();
                } catch (InterruptedException e) {
                    break;
                }
                if(images)
                    scrapImages(url);
                if(links)
                    scrapLinks(url);
                SwingUtilities.invokeLater(new Runnable(){
                    @Override
                    public void run() {
                        statusLabel.setText("fetched images: " + imgNum + "; \tfetched links: " + linkNum + ";");
                    }
               });
            }
            
        }
        public void go(){
           sem.release();
        }
        
        /**
         * Scraps image for url in new Thread
         * @param url to scrap
         */
        private void scrapImages(String url){
            List<String> imageSrcs = scraper.getImageSrcs(url);
            if(imageSrcs == null)
                return; 
            imgNum = imageSrcs.size();
            for(String src : imageSrcs){
                SwingUtilities.invokeLater(new Runnable(){
                    @Override
                    public void run() {
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                               resultTableModel.add(src, "img");
                               statusLabel.setText("added img: " + src);
                            }
                        });
                    }
                    
                });
                try {
                    Thread.sleep(SCRAP_DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
            linkNum = pageLinks.size();
            for(String link : pageLinks){
                SwingUtilities.invokeLater(new Runnable(){
                    @Override
                    public void run() {
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                               resultTableModel.add(link, "link");
                               statusLabel.setText("added link: " + link);
                            }
                        });
                    }
                    
                });
                try {
                    Thread.sleep(SCRAP_DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        
    }
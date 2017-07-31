package GUI;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.*;

import DownloadManager.DownloadManager;
import GUI.ResultTableModel;
import Scraper.Scraper;
import Scraper.SoupScraper;


/**
 * WebScraperFrame:
 * GUI Interface for scraper in swing librery
 * 
 * @author Lasha Kharshiladze
 *
 */
public class WebScraperFrame extends JFrame implements ActionListener{
    private static final int FRAME_WIDTH    = 700;
    private static final int FRAME_HEIGHT   = 700;
    private static final int URL_FIELD_LEN  = 30;
    private static final int COL_NUM        = 4;
    private static final int INDEX_COLUMN_LEN = 25;
    private static final int TYPE_COLUMN_LEN  = 80;
    private static final int CHECK_COLUMN_LEN = 80;
    
    private static final int SAVE_ALL         = 0;
    private static final int SAVE_ALL_IMG     = 1;
    private static final int SAVE_ALL_LINK    = 2;
    private static final int SAVE_ALL_CHECKED = 3;
    
    // North region
    JPanel      topBar;
    JTextField  urlField;
    JButton     scrapButton;
    JCheckBox   linksCheck;
    JCheckBox   imagesCheck;
    
    
    // Table (Central Region)
    JTable resultTable;
    ResultTableModel resultTableModel;
    String[] colNames;
    int colNum;
    
    // South Region
    JPanel south;
    JButton clearButton;
    JLabel statusLabel;
    
    // East Region
    JButton saveAllButton;
    JButton saveImgsButton;
    JButton saveLinksButton;
    JButton saveCheckedButton;
    
    
    // Scraper
    Scraper scraper;
    ScrapWorker scraping;
    Thread      scrapingThread;
    
    // Download Manager
    DownloadManager downloadManager;
    /**
     * Initializes all objects for GUI
     */
    public WebScraperFrame(){
        scraper  =  new SoupScraper();
        scraping =  new ScrapWorker();
        scrapingThread = new Thread(scraping);
        scrapingThread.start();
        
        downloadManager = new DownloadManager();
        
       // adding header
       topBar = getTopBar();
       this.getContentPane().add(topBar, BorderLayout.NORTH);
       
       // add  JTable on pane
       addTable();
       
       // easth region
       JPanel leftBar = new JPanel();
       saveCheckedButton = new JButton("Download Checked Files");
       saveAllButton = new JButton("download all Files");
       saveImgsButton = new JButton("download all images");
       saveLinksButton = new JButton("download all links");
       leftBar.add(saveCheckedButton);
       
       this.getContentPane().add(leftBar, BorderLayout.EAST);
       
       // south Region 
       south = new JPanel();
       statusLabel = new JLabel("");
       south.add(statusLabel,0,0);
       
       
       this.getContentPane().add(south, BorderLayout.SOUTH);
       
       //Action Listeners
       scrapButton.addActionListener(this);
       clearButton.addActionListener(this);
       
       saveAllButton.addActionListener(this);
       saveImgsButton.addActionListener(this);
       saveLinksButton.addActionListener(this);
       saveCheckedButton.addActionListener(this);
       this.pack();
       
    }
  
    /**
     * get header of frame (norther par of window)
     */
    private JPanel getTopBar(){
        JPanel panel = new JPanel();
        JLabel urlLabel = new JLabel("URL:");
        urlField = new JTextField(URL_FIELD_LEN);
        scrapButton = new JButton("Scrap!");
        clearButton = new JButton("Clear");
        imagesCheck = new JCheckBox("Images");
        linksCheck = new JCheckBox("Links");
        linksCheck.setSelected(true);
        imagesCheck.setSelected(true);
        
        panel.add(urlLabel);
        panel.add(urlField);
        panel.add(scrapButton);
        panel.add(clearButton);
        panel.add(linksCheck);
        panel.add(imagesCheck);
        return panel;
    }
    
    /**
     * adding table;
     */
    public void addTable(){
        // Model
        colNum = COL_NUM;
        colNames = new String[colNum];
        colNames[0] = "#";
        colNames[1] = "url";
        colNames[2] = "type";
        colNames[3] = "check";
        resultTableModel  = new ResultTableModel(colNames);
        
        // View
        resultTable = new JTable(resultTableModel);
        resultTable.getColumnModel().getColumn(0).setPreferredWidth(INDEX_COLUMN_LEN);
        resultTable.getColumnModel().getColumn(1).setPreferredWidth(1000);
        resultTable.getColumnModel().getColumn(2).setPreferredWidth(TYPE_COLUMN_LEN);
        resultTable.getColumnModel().getColumn(3).setPreferredWidth(CHECK_COLUMN_LEN);
       
        // add scroll area
        JScrollPane scrollPane = new JScrollPane(resultTable);
        this.getContentPane().add(scrollPane);
    }
    
    /**
     * checks if is it possible to connect given url
     * 
     * @param url
     * @return if given url respons scraper request
     */
    private boolean checkUrl(String url){
        String res = scraper.checkUrl(url);
        if(res.equals(Scraper.OK))
            return true;
        statusLabel.setText(res);
        return false;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        // Scrap button Slicked
        if(e.getSource().equals(scrapButton)){
            if(!checkUrl(urlField.getText()))
                return;
            resultTableModel.clear();
            
            // give references to scraper
            scraping.update(resultTableModel,scraper,urlField.getText(),statusLabel, 
                            imagesCheck.isSelected(),linksCheck.isSelected());
            // unlock scraping semaphore 
            scraping.go();
            
        }
        if(e.getSource().equals(saveCheckedButton)){
            downloadOnLocal(SAVE_ALL_CHECKED);
            
        }
        // Clear  Button was clicked
        if(e.getSource().equals(clearButton)){
            resultTableModel.clear();
            statusLabel.setText("Everything Cleaned Up!");
        }
    }
    
    public void downloadOnLocal(int policy){
     // Set up File choser Frame
        JFileChooser chooser;
        chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle("Choose Folder for Saving Resources");
        chooser.setFileSelectionMode(JFileChooser.SAVE_DIALOG);
        chooser.setApproveButtonText("Save");
        
        // If Save button(on chooser) was clicked 
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) { 
            String saveUrl = chooser.getSelectedFile().toString();
            for(int i = 0; i < resultTable.getRowCount(); i++){
                
                String webUrl = (String)resultTable.getValueAt(i, 1);
                String type = (String)resultTable.getValueAt(i, 2);
                Boolean checked = (Boolean)resultTable.getValueAt(i, 3);
                if(!checked && policy == SAVE_ALL_CHECKED)
                    continue;
                if(type.equals("img") && policy == SAVE_ALL_LINK)
                    continue;
                if(type.equals("link") && policy == SAVE_ALL_IMG)
                    continue;
                saveUrl += "/" + getFileName(webUrl, type, i);
                downloadManager.download(webUrl, saveUrl);
                statusLabel.setText(saveUrl + " Saved");
            }
        }
    }
    /**
     * for a given url getFileName return file name of that object
     * if it is only page url retuns "";
     * @param url
     * @return
     */
    private String getFileName(String webUrl,String type, int row){
        if(type.equals("link"))
            return "link[row "+row+" .html";
        String name = "";
        int pInd = webUrl.length() - 1;
        for(; pInd >=0 ; pInd-- ){
            if(webUrl.charAt(pInd) == '/')
                break;
        }
        
        if(pInd != -1)
            name = webUrl.substring(pInd + 1);
        
        return name;
    }
    
    /**
     * Application Launcher:
     * runs WebScraperFrame 
     */
    public static void main(String[] args){
        WebScraperFrame scraperFrame = new WebScraperFrame();
        scraperFrame.setSize( new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        scraperFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        scraperFrame.setVisible(true);
    }

    

}

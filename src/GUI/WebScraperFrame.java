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
    private static final int COL_NUM        = 3;
    
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
    JButton saveButton;
    String saveUrl;
    
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
       saveButton = new JButton("Save Images on Local");
       leftBar.add(saveButton);
       add(leftBar, BorderLayout.EAST);
       
       // south Region 
       south = new JPanel();
       statusLabel = new JLabel("");
       south.add(statusLabel,0,0);
       
       
       this.getContentPane().add(south, BorderLayout.SOUTH);
       
       //Action Listeners
       scrapButton.addActionListener(this);
       clearButton.addActionListener(this);
       saveButton.addActionListener(this);
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
        colNames[2] = "status";
        resultTableModel  = new ResultTableModel(colNames);
        
        // View
        resultTable = new JTable(resultTableModel);
        resultTable.getColumnModel().getColumn(0).setPreferredWidth(20);
        resultTable.getColumnModel().getColumn(1).setPreferredWidth(500);
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
        if(e.getSource().equals(scrapButton)){
            if(!checkUrl(urlField.getText()))
                return;
            resultTableModel.clear();
            
            scraping.update(resultTableModel,scraper,urlField.getText(),statusLabel, 
                            imagesCheck.isSelected(),linksCheck.isSelected());
            scraping.go();
            
        }
        if(e.getSource().equals(saveButton)){
            JFileChooser chooser;
            chooser = new JFileChooser();
            chooser.setCurrentDirectory(new java.io.File("."));
            chooser.setDialogTitle("Choose Folder for Saving Resources");
            chooser.setFileSelectionMode(JFileChooser.SAVE_DIALOG);
            chooser.setApproveButtonText("Save");
            
            chooser.setAcceptAllFileFilterUsed(false);
            
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) { 
                 saveUrl = chooser.getSelectedFile().toString();
                for(int i = 0; i < resultTable.getRowCount(); i++){
                    String webUrl = (String)resultTable.getValueAt(i, 1);
                    String type = (String)resultTable.getValueAt(i, 2);
                    if(!type.equals("img"))
                        continue;
                    int pInd = webUrl.length() - 1;
                    for(; pInd >=0 ; pInd-- ){
                        if(webUrl.charAt(pInd) == '/')
                            break;
                    }
                    String  destUrl = saveUrl;
                    
                        destUrl += webUrl.substring(pInd);
                    downloadManager.download(webUrl, destUrl);
                }
            }
            
        }
        if(e.getSource().equals(clearButton)){
            resultTableModel.clear();
            statusLabel.setText("Everything Cleaned Up!");
        }
    }
    
    /**
     * Application:
     * runs WebScraperFrame 
     */
    public static void main(String[] args){
        WebScraperFrame scraperFrame = new WebScraperFrame();
        scraperFrame.setSize( new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        scraperFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        scraperFrame.setVisible(true);
    }

    

}

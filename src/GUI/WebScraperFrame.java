package GUI;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.*;

import GUI.ResultTableModel;
import Scraper.SoupScraper;

public class WebScraperFrame extends JFrame implements ActionListener{
    private static final int FRAME_WIDTH    = 700;
    private static final int FRAME_HEIGHT   = 700;
    private static final int URL_FIELD_LEN  = 30;
    
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
    //Scraper
    SoupScraper scraper;
    ScrapWorker scraping;
    Thread      scrapingThread;
    public WebScraperFrame(){
        scraper  =  new SoupScraper();
        scraping =  new ScrapWorker();
        scrapingThread = new Thread(scraping);
        scrapingThread.start();
        
       // set up table
       colNum = 3;
       colNames = new String[colNum];
       colNames[0] = "#";
       colNames[1] = "url";
       colNames[2] = "status";
       resultTableModel  = new ResultTableModel(colNames);
       
       // adding header
       topBar = getTopBar();
       this.getContentPane().add(topBar,BorderLayout.NORTH);
       
       // add  JTable on pane
       resultTable = new JTable(resultTableModel);
       resultTable.getColumnModel().getColumn(0).setPreferredWidth(20);
       resultTable.getColumnModel().getColumn(1).setPreferredWidth(500);
       
       JScrollPane scrollPane = new JScrollPane(resultTable);
       this.getContentPane().add(scrollPane);
       
       // south Region 
       south = new JPanel();
       statusLabel = new JLabel("");
       south.add(statusLabel,0,0);
       
       
       this.getContentPane().add(south, BorderLayout.SOUTH);
       
       //Action Listeners
       scrapButton.addActionListener(this);
       clearButton.addActionListener(this);
       this.pack();
       
    }
    
    private JPanel getTopBar(){
        JPanel panel = new JPanel();
        JLabel urlLabel = new JLabel("URL:");
        urlField = new JTextField(URL_FIELD_LEN);
        scrapButton = new JButton("Scrap!");
        clearButton = new JButton("Clear");
        imagesCheck = new JCheckBox("Images");
        linksCheck = new JCheckBox("Links");
        linksCheck.setSelected(true);
        
        panel.add(urlLabel);
        panel.add(urlField);
        panel.add(scrapButton);
        panel.add(clearButton);
        panel.add(linksCheck);
        panel.add(imagesCheck);
        return panel;
    }
    /**
     * @param url
     * @return if given url respons scraper request
     */
    private boolean checkUrl(String url){
        String res = scraper.checkUrl(url);
        if(res.equals(SoupScraper.OK))
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
        if(e.getSource().equals(clearButton)){
            resultTableModel.clear();
            statusLabel.setText("Everything Cleaned Up!");
        }
    }
    
    public static void main(String[] args){
        WebScraperFrame scraperFrame = new WebScraperFrame();
        scraperFrame.setSize( new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        scraperFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        scraperFrame.setVisible(true);
    }

    

}

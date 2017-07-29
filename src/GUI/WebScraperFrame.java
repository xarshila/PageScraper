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
    private static final int FRAME_WIDTH    = 650;
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
    
    //Scraper
    SoupScraper scraper;
    
    public WebScraperFrame(){
        scraper = new SoupScraper();
        
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
       clearButton = new JButton("Clear");
       south.add(clearButton);
       this.getContentPane().add(south, BorderLayout.SOUTH);
       
       //Action Listeners
       scrapButton.addActionListener(this);
       clearButton.addActionListener(this);
       this.pack();
       
    }
    
    private JPanel getTopBar(){
        JPanel panel = new JPanel();
        JLabel urlLabel = new JLabel("url for scrap:");
        urlField = new JTextField(URL_FIELD_LEN);
        scrapButton = new JButton("Scrap!");
        imagesCheck = new JCheckBox("Images");
        linksCheck = new JCheckBox("Links");
        linksCheck.setSelected(true);
        
        panel.add(urlLabel);
        panel.add(urlField);
        panel.add(scrapButton);
        panel.add(linksCheck);
        panel.add(imagesCheck);
        return panel;
    }
    
   
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(scrapButton)){
            resultTableModel.clear();
            Runnable scraping = new ScrapWorker(resultTableModel,scraper,urlField.getText(), 
                                          imagesCheck.isSelected(),linksCheck.isSelected());
            Thread t = new Thread(scraping);
            t.start();
        }
        if(e.getSource().equals(clearButton)){
            resultTableModel.clear();
        }
    }
    
    public static void main(String[] args){
        WebScraperFrame scraperFrame = new WebScraperFrame();
        scraperFrame.setSize( new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        scraperFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        scraperFrame.setVisible(true);
    }

    

}

import java.awt.BorderLayout;

import javax.swing.*;

public class WebScraperFrame extends JFrame{
    private static final int FIELD_LEN = 30;
    
    // North region
    JPanel      topBar;
    JTextField  urlField;
    JButton     scrapButton;
    JCheckBox   linksCheck;
    JCheckBox   imagesCheck;
    
    
    // Central Region
    JTable resultTable;
    ResultTableModel tableModel;
    String[] colNames;
    int colNum;
    
    public WebScraperFrame(){
       colNum = 2;
       colNames = new String[colNum];
       colNames[0] = "url";
       colNames[1] = "status";
       tableModel  = new ResultTableModel(colNames);
       resultTable = new JTable(tableModel);
       
       
       // resize columns
       resultTable.getColumnModel().getColumn(0).setPreferredWidth(500);
       
       topBar = getTopBar();
       this.getContentPane().add(topBar,BorderLayout.NORTH);
       JScrollPane scrollPane = new JScrollPane(resultTable);
       this.getContentPane().add(scrollPane);
       this.pack();
       
    }
    
    private JPanel getTopBar(){
        JPanel panel = new JPanel();
        urlField = new JTextField(FIELD_LEN);
        scrapButton = new JButton("Scrap!");
        imagesCheck = new JCheckBox("Images");
        linksCheck = new JCheckBox("Links");
        linksCheck.setSelected(true);
        
        panel.add(urlField);
        panel.add(scrapButton);
        panel.add(linksCheck);
        panel.add(imagesCheck);
        return panel;
    }
    
    public static void main(String[] args){
        WebScraperFrame scraperFrame = new WebScraperFrame();
        scraperFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        scraperFrame.setVisible(true);
    }

}

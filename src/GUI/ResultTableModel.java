package GUI;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.table.*;
import java.util.*;
/**
 * Class ResultModelTable:
 * Model for JTable to make Data abstraction
 * 
 * @author Lasha Kharshiladze
 *
 */
public class ResultTableModel  extends AbstractTableModel {
    private static final int DEF_COL_NUM = 4;
    
    List<List<Object>> grid;
    String[] colNames;
    int colNum;
    
    /**
     * Constructor creates TableModel with given colNames
     * @param colNames
     */
    public ResultTableModel(String[] colNames){
       grid = new ArrayList<List<Object>>();
       colNum = colNames.length;
       this.colNames = new String[colNum];
       System.arraycopy(colNames, 0,this.colNames, 0, colNum);
    }
    
    /**
     * Adding Row into ResultTableModel
     * auto indexing
     * @param url
     * @param type
     * 
     */
    public void add(String url, String type){
        List<Object> list = new ArrayList<Object>();
        list.add(new Integer(grid.size() + 1));
        list.add(url);
        list.add(type);
        list.add(new Boolean( false)           );
        
        grid.add(list);
        
        fireTableDataChanged();
    }
    
    /**
     * Clears table 
     */
    public void clear(){
        grid.clear();
        fireTableDataChanged();
    }
    
    @Override
    public int getRowCount() {
        return grid.size();
    }

    @Override
    public int getColumnCount() {
        return colNum;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return grid.get(rowIndex).get(columnIndex);
    }
    
    @Override
    public String getColumnName(int index){
        return colNames[index];
    }
    
    @Override
    public Class getColumnClass(int c) {
        
        return getValueAt(0, c).getClass();
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex){
       
        return (columnIndex == 3);
    }
    @Override
    public void setValueAt(Object value, int row, int col) {
        grid.get(row).set(col, value);        
        fireTableCellUpdated(row, col);
    }
}

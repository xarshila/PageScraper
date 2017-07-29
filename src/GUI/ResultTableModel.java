package GUI;
import javax.swing.table.*;
import java.util.*;
public class ResultTableModel  extends AbstractTableModel {
    private static final int DEF_COL_NUM = 3;
    List<List<Object>> grid;
    String[] colNames;
    int colNum;
    public ResultTableModel(String[] colNames){
       grid = new ArrayList<List<Object>>();
       colNum = colNames.length;
       this.colNames = new String[colNum];
       System.arraycopy(colNames, 0,this.colNames,0, colNum);
;    }
    
    public void add(String url, String type){
        List<Object> list = new ArrayList<Object>();
        list.add(new Integer(grid.size() + 1));
        list.add(url);
        list.add(type);
        
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
}

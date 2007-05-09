package net.sf.jxls.transformation;

import net.sf.jxls.tag.Block;
import net.sf.jxls.tag.Point;
import net.sf.jxls.formula.CellRef;

import java.util.List;
import java.util.ArrayList;

import org.apache.poi.hssf.util.CellReference;

/**
 * @author Leonid Vysochyn
 */
public class DuplicateTransformationByColumns extends BlockTransformation{

    int rowNum, colNum;
    int duplicateNumber;
    List cells = new ArrayList();

    public DuplicateTransformationByColumns(Block block, int duplicateNumber) {
        super(block);
        this.duplicateNumber = duplicateNumber;
    }

    public Block getBlockAfterTransformation() {
        return null;
    }

    public List transformCell(Point p) {
        List cells;
        if( block.contains( p ) ){
            cells = new ArrayList();
            Point rp = p;
            cells.add( p );
            for( int i = 0; i < duplicateNumber; i++){
                cells.add( rp = rp.shift( 0, block.getNumberOfColumns()));
            }
        }else{
            cells = new ArrayList();
            cells.add( p );
        }
        return cells;
    }

    public String getDuplicatedCellRef(String sheetName, String cell, int duplicateBlock){
        CellReference cellRef = new CellReference(cell);
        int rowNum = cellRef.getRow();
        short colNum = cellRef.getCol();
        String refSheetName = cellRef.getSheetName();
        String resultCellRef = cell;
        if( block.getSheet().getSheetName().equalsIgnoreCase( refSheetName ) || (refSheetName == null && block.getSheet().getSheetName().equalsIgnoreCase( sheetName ))){
            // sheet check passed
            if( block.contains( rowNum, colNum ) && duplicateNumber >= 1 && duplicateNumber >= duplicateBlock){
                colNum += block.getNumberOfColumns() * duplicateBlock;
                resultCellRef = cellToString( rowNum, colNum, refSheetName );
            }
        }
        return resultCellRef;
    }

    public List transformCell(String sheetName, CellRef cellRef) {
        String refSheetName = cellRef.getSheetName();
        cells.clear();
        if( block.getSheet().getSheetName().equalsIgnoreCase( refSheetName ) || (refSheetName == null && block.getSheet().getSheetName().equalsIgnoreCase( sheetName ))){
            // sheet check passed
            if( block.contains( cellRef.getRowNum(), cellRef.getColNum() ) /*&& duplicateNumber >= 1*/){
                colNum = cellRef.getColNum();
                cells.add( cellToString( cellRef.getRowNum(), colNum, refSheetName) );
                for( int i = 0; i < duplicateNumber; i++){
                    colNum += block.getNumberOfColumns();
                    cells.add( cellToString( cellRef.getRowNum(), colNum, refSheetName ));
                }
            }
        }
        return cells;
    }

    public String cellToString(int rowNum, int colNum, String sheetName){
        String cellname;
        CellReference cellReference = new CellReference( rowNum, colNum );
        if( sheetName != null ){
            cellname = sheetName + "!" + cellReference.toString();
        }else{
            cellname = cellReference.toString();
        }
        return cellname;
    }

    public boolean equals(Object obj) {
        if( obj != null && obj instanceof DuplicateTransformation ){
            DuplicateTransformation dt = (DuplicateTransformation) obj;
            return ( super.equals( obj ) && dt.duplicateNumber == duplicateNumber);
        }else{
            return false;
        }
    }

    public int hashCode() {
        int result = super.hashCode();
        result = 29 * result + duplicateNumber;
        return result;
    }

    public String toString() {
        return "DuplicateTransformation: {" + super.toString() + ", duplicateNumber=" + duplicateNumber + "}";
    }
}
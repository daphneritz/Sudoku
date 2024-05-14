package sudoku;

import java.util.ArrayList;

public class Move {
    private int row; 
    private int col;
    private int prevVal;
    private int newVal;
    private static ArrayList<Move> vals= new ArrayList<Move>();

    public Move(int row, int col, int prevVal, int newVal) {
        this.row = row;
        this.col = col;
        this.prevVal = prevVal;
        this.newVal = newVal;
        vals.add(this); 
    }

    public Move(){
        this.row = 0;
        this.col = 0;
        this.prevVal = 0;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getPrevVal() {
        return prevVal;
    }

    public int getNewVal() {
        return newVal;
    }

    public String getNewVals() {
        String newVals = "";
        for (int i = 0; i < vals.size(); i++) {
            Move curr= vals.get(i);
            newVals += "Row: " + curr.getRow() + " Col: " + curr.getCol() + " New value: " +curr.getNewVal()+"\n";
        }
        return newVals;
    }

}

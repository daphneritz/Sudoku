package sudoku;

import java.io.InputStream;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;

public class Board
{
    private int[][] board;

    public Board()
    {
        board = new int[9][9];
    }

    public static Board loadBoard(InputStream in) throws Exception
    {
        Board board = new Board();
        Scanner scanner = new Scanner(in);
        for (int row = 0; row < 9; row++)
        {
            for (int col = 0; col < 9; col++)
            {
                try{ 
                    int value = scanner.nextInt();
                if ((value < 0 || value > 9)|| !board.isLegal(row, col, value)){
                    throw new Exception();
                }
                board.setCell(row, col, scanner.nextInt());
                } catch (Exception e){
                    throw new Exception("Invalid board");
                }
            }
        }
        scanner.close();
        return board;
    }

    public boolean isLegal(int row, int col, int value)
    {
        return value >= 1 && value <= 9 && getPossibleValues(row, col).contains(value);
    }

    public void setCell(int row, int col, int value)
    {
        if (value < 0 || value > 9)
        {
            throw new IllegalArgumentException("Value must be between 1 and 9 (or 0 to reset a value)");
        }
        if (value != 0 && !getPossibleValues(row, col).contains(value))
        {
            throw new IllegalArgumentException("Value " + value + " is not possible for this cell");
        }
        // based on other values in the sudoku grid
        board[row][col] = value;
    }

    public int getCell(int row, int col)
    {
        return board[row][col];
    }

    public boolean hasValue(int row, int col)
    {
        return getCell(row, col) > 0;
    }

    public Set<Integer> getPossibleValues(int row, int col)
    {
        Set<Integer> possibleValues = new HashSet<>();
        for (int i = 1; i <= 9; i++)
        {
            possibleValues.add(i);
        }
        // check the row
        for (int c = 0; c < 9; c++)
        {
            possibleValues.remove(getCell(row, c));
        }
        // check the column
        for (int r = 0; r < 9; r++)
        {
            possibleValues.remove(getCell(r, col));
        }
        // check the 3x3 square
        int startRow = row / 3 * 3;
        int startCol = col / 3 * 3;
        for (int r = startRow; r < startRow + 3; r++)
        {
            for (int c = startCol; c < startCol + 3; c++)
            {
                possibleValues.remove(getCell(r, c));
            }
        }
        return possibleValues;
    }

    public boolean isFull()
    {
        for (int row = 0; row < 9; row++)
        {
            for (int col = 0; col < 9; col++)
            {
                if (!hasValue(row, col))
                {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean won(){
        if (!isFull()){
            return false; 
        }

        for (int i = 0; i < 9; i++){
            Set<Integer> rowSet = new HashSet<>();
            Set<Integer> colSet = new HashSet<>();
            for (int j = 0; j < 9; j++){
                if (rowSet.contains(getCell(i, j)) || colSet.contains(getCell(j, i))){
                    return false; 
                }
                rowSet.add(getCell(i, j));
                colSet.add(getCell(j, i));
            }
        }
        return true; 
    }

    public void clearBoard(){
        for (int i = 0; i < 9; i++){
            for (int j = 0; j < 9; j++){
                setCell(i, j, 0);
            }
        }
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < 9; row++)
        {
            for (int col = 0; col < 9; col++)
            {
                sb.append(getCell(row, col));
                if (col < 8)
                {
                    sb.append(" ");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

}

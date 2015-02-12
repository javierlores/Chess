package views;

import java.awt.LayoutManager;

import javax.swing.JPanel;

import models.Board;

public class BoardCell extends JPanel 
{
	private int row;
	private int col;
	
	public BoardCell (LayoutManager layout, int row, int col) 
	{
		super(layout);
		this.row = row;
		this.col = col;
	}
	
	public int getRow()  { return row; }
	
	public int getCol()  { return col; }
	
	public void setRow(int row)  { if (row >= 0 && row < Board.ROW) this.row = row; }
	
	public void setCol(int col)  { if (col >= 0 && col < Board.COL) this.col = col; }
}

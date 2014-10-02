
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
public class GridJPanel extends JPanel{
	private int row=10,col=10,generation=10,mat[][]=new int[row+2][col+2];
	LifeGUI gui;
	public GridJPanel(LifeGUI gui) {
		setLayout(new GridLayout(row,col));
		this.gui = gui;
		//addActionListener(this);
		setGrid();
	}
	public GridJPanel(int row, int col, int generation,LifeGUI gui) {
		this.gui = gui;
		this.row = row;
		this.col = col;
		this.generation = generation;
		setLayout(new GridLayout(row,col));
		setGrid();
	}
	/*public GridJPanel(int row, int col, int generation,int mat[][]) {
		this.row = row;
		this.col = col;
		this.generation = generation;
		this.mat = mat;
		setLayout(new GridLayout(row,col));
		setGrid();
	}*/
	public void setGrid(){
		for(int i=0; i<row; i++)
			for(int j=0;j<col;j++){
		//		cell = new CellButton();
				add(new CellButton(this));
			}
	}
	public int[][] getMat(){
		return mat;
	}
	public void setMat(int i, int j,int isLive){
		this.mat[i][j] = isLive; 
	}
	public void setMat(int mat[][]){
		this.mat = mat; 
	}
	public int getRow(){
		return row;
	}
	public int getCol(){
		return col;
	}
	public int getGeneration(){
		return generation;
	}
	public void refreshGrid(){
		for(int i=1; i<mat.length-1;i++)
			for(int j=1; j<mat[0].length-1; j++){
				JButton cell = (JButton) getComponent((i-1)*(mat[0].length-2)+j-1);
				if(mat[i][j]==1){
					cell.setText("o");
				}
				else{
					cell.setText("");
				}
			
			};
	}

	public void print(){

		for(int i=1; i<row+1; i++){
			for(int j=1; j<col+1; j++)
				switch (mat[i][j]){
				case 1: System.out.print('*');break;
				default: System.out.print('-');
				}
			System.out.println("");
		}
		System.out.println("=====================");
	}

}

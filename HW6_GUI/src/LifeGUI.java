//import java.awt.Button;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;



public class LifeGUI extends JFrame{
	private Life life;
	private int row,col,generation;
	private CellButton cell;
	//private StartButton startBut;
	private GridJPanel grid = new GridJPanel(this);
	private ControlPanel control = new ControlPanel(this);
	private int mat[][];
	public LifeGUI() {
		row = 10;
		col = 10;
		generation = 10;
		mat = new int[row+2][col+2];	
		setSize(800, 800); // width, height
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setTitle("LIFE by Chenguang Yu, 0454485");
		setLocation(60, 60); // x, y
		setLayout(new BorderLayout(20, 20));
		add(grid,BorderLayout.CENTER);	
		add(control, BorderLayout.NORTH);
	}
	
	public void print(){
		for(int i=1; i<this.row+1; i++){
			for(int j=1; j<this.col+1; j++)
				switch (this.mat[i][j]){
				case 1: System.out.print('*');break;
				default: System.out.print('-');
				}
			System.out.println("");
		}
		System.out.println("=====================");
	}
	public int[][] getMat(){
		return mat;
	}
	public int getGeneration() {
		return generation;
	}
	public int getRow() {
		return row;
	}
	public int getCol() {
		return col;
	}
	public GridJPanel getGridPanel() {
		return grid;
	}
	public ControlPanel getControlPanel(){
		return control;
	}
	
	public void setGridPanel(GridJPanel grid){
	this.grid = grid;	
	}
	public static void main(String[] args) {
		
		int generation;

		EventQueue.invokeLater(new Runnable() {
				public void run() {
					//new LifeGUI();
					LifeGUI lifeGUI =  new LifeGUI();
				}
		});
		
		
	}
	

}
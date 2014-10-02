import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.*;
public class ControlPanel extends JPanel{
	LifeGUI gui;
	int row,col,generation;
	MyTextFeild colText, rowText, generationText;
	ClearButton clearButton;

	public ControlPanel(LifeGUI gui){
		this.gui = gui;
		rowText = new MyTextFeild(gui);
		colText = new MyTextFeild(gui);
		generationText = new MyTextFeild(gui);
		clearButton = new ClearButton(gui); 

		setLayout(new GridLayout(3,3,30,30));
		add(new StartButton(gui));
		add( new MyLabel("Input Max Row ->") );
		add(rowText);

		add(new NextGenButton(gui));
		add( new MyLabel("Input Max Collum ->") );
		add(colText);

		add(clearButton);
		add( new MyLabel("Input Generation ->") );
		add(generationText);
	
	}
	public void clearGrid(){
		row = getRow();
		col = getCol();
		//System.out.println(""+row+col);
		generation = getGeneration();
		gui.getGridPanel().setVisible(false);
		gui.remove(gui.getGridPanel());
		gui.setGridPanel(new GridJPanel(row,col,generation,gui));
		gui.getGridPanel().setMat(new int[row+2][col+2]);
		gui.add(gui.getGridPanel(),BorderLayout.CENTER);
	}
	public int[][] reShape(){
		row = getRow();
		col = getCol();
		int newMat[][] = new int[row+2][col+2];
		int mat[][] = gui.getGridPanel().getMat();


		for (int i=0; i<mat.length; i++)
			for (int j=0; j<mat[0].length; j++){
				if (i<newMat.length && j<newMat[0].length)
					if(i==newMat.length-1 || j==newMat[0].length-1)
						newMat[i][j]=0;
					else
						newMat[i][j] = mat[i][j];
			}

		return newMat;
	}
	
	public int getRow(){
		return rowText.toInt();
	}
	public int getCol(){
		return colText.toInt();
	}
	public int getGeneration(){
		return generationText.toInt();
	}

}
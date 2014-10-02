
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.*;
public class MyTextFeild extends JTextField  implements ActionListener{

	private LifeGUI gui;

	public MyTextFeild() {
		super();
		Font f = new Font(null, Font.BOLD,20);
		setFont(f);
		setText("10");
		setEditable(true);
		addActionListener(this);
	}
	
	public MyTextFeild(LifeGUI gui) {
		this();
		this.gui = gui;
	}
	
	public int toInt(){
		return Integer.valueOf(getText());
				
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		//System.out.println(toInt());
		int mat[][] = gui.getControlPanel().reShape();
		gui.getControlPanel().clearGrid();
		gui.getGridPanel().setMat(mat);
		//gui.getGridPanel().print();
		gui.getGridPanel().refreshGrid();
	}


}

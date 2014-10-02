

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
public class ClearButton extends JButton implements ActionListener{
	LifeGUI gui;
	int row,col,generation;
	public ClearButton() {
		super("Reset Grid");
		Font f = new Font("Times", Font.BOLD,20);
		setMargin(new Insets(0, 0, 0, 0));
		setFont(f);
		addActionListener(this);
	}
	public ClearButton(LifeGUI gui){
		this();
		this.gui = gui;
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		gui.getControlPanel().clearGrid();
	}

}

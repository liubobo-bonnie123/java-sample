

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;


public class NextGenButton extends JButton implements ActionListener {
	Life life;
	LifeGUI gui;
	private int mat[][];
	public NextGenButton() {
		super("Next Gen");
		Font f = new Font("Times", Font.BOLD,20);
		setMargin(new Insets(0, 0, 0, 0));
		setFont(f);
		addActionListener(this);
	}
	public NextGenButton(LifeGUI gui){
		this();
		this.gui = gui;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Life life = new Life(gui.getGridPanel().getMat(),gui.getGridPanel().getRow(), gui.getGridPanel().getCol(),2);
		life.evolution();
		gui.getGridPanel().refreshGrid();
	
		
		
	}

}
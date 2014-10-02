
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;


public class StartButton extends JButton implements ActionListener {
	Life life;
	LifeGUI gui;
	public StartButton() {
		super("Start");
		Font f = new Font("Times", Font.BOLD,20);
		setMargin(new Insets(1, 1, 1, 1));
		setFont(f);
		setSize(100,100);
		addActionListener(this);
	}
	public StartButton(LifeGUI gui){
		this();
		this.gui = gui;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Life life = new Life(gui.getGridPanel().getMat(),gui.getGridPanel().getRow(), gui.getGridPanel().getCol(),gui.getGridPanel().getGeneration());
		life.evolution();
		gui.getGridPanel().refreshGrid();

	}

}
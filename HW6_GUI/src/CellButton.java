import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;


public class CellButton extends JButton implements ActionListener {
	GridJPanel grid;
	public CellButton() {
		super();
		Font f = new Font("Times", Font.BOLD,40);
		setMargin(new Insets(0, 0, 0, 0));
		setFont(f);
		addActionListener(this);
	}
	public CellButton(GridJPanel grid){
		this();
		this.grid = grid;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton cell = ((JButton) e.getSource());
		int i = cell.getLocation().y/cell.getSize().height;
		int j = cell.getLocation().x/cell.getSize().width;
		if(cell.getText().isEmpty()){
			cell.setText("o");
			grid.setMat(i+1,j+1,1);
		}
		else{
			cell.setText("");
			grid.setMat(i+1,j+1,0);
		}
		grid.print();
	}

}

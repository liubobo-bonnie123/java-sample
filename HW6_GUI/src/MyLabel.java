import java.awt.Font;

import javax.swing.*;
public class MyLabel extends JLabel{

	public MyLabel(String str) {
		super(str);
		Font f = new Font(null, Font.BOLD,20);
		setHorizontalAlignment(RIGHT);
		setFont(f);
	
	}

}

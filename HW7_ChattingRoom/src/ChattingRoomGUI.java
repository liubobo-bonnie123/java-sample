import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class ChattingRoomGUI extends JFrame implements ActionListener{
	private JTextArea messageDisplayArea;
	private JTextField messageEditField;
	private PrintWriter messageWriter;
	private String userName;
	public ChattingRoomGUI(String userName, PrintWriter messageWriter) {
		super();
		this.setLocation(0,0);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setTitle(userName);
		this.messageDisplayArea = new JTextArea(15,30); //message display area
		messageDisplayArea.setLineWrap(true); //wrap
		JScrollPane messageDisplayAreaScroll=new JScrollPane(this.messageDisplayArea);//scroll
		JPanel messageDisplayAreaPanel=new JPanel();
		messageDisplayAreaPanel.add(messageDisplayAreaScroll);
		this.add(messageDisplayAreaPanel);
		this.messageDisplayArea.setEditable(false);
		this.pack();
		setSize(400, 300);

		JPanel messageEditPanel = new JPanel();    //message edit area & "send" button  
		this.add(messageEditPanel, BorderLayout.SOUTH);
		this.messageEditField = new JTextField(20);
		messageEditPanel.add(this.messageEditField);
		this.messageEditField.addActionListener(this);
		JButton sendButton = new JButton("send");
		messageEditPanel.add(sendButton);
		sendButton.addActionListener(this);
		this.messageWriter = messageWriter;
		this.userName = userName;
		this.setTitle(userName);
	}
	
	public void setWriter(PrintWriter messageWriter){
		this.messageWriter = messageWriter;
	}
	public void dispalyMessage(String message){
		this.messageDisplayArea.append(message + "\r\n");
	}
	public String getSendMessage(){
		String str = this.messageEditField.getText();
		this.messageEditField.setText(null);
		return str;
	}
	public void autoBottom(){
		this.messageDisplayArea.setCaretPosition(this.messageDisplayArea.getText().length());
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		this.messageWriter.println(this.userName + " says: " + this.messageEditField.getText());
		this.messageDisplayArea.append("I say: " + this.messageEditField.getText() + "\n");
		this.messageEditField.setText("");
		this.autoBottom();
	}

}

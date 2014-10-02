import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.Scanner;

public class ChattingRoomServer {
	private ChattingRoomGUI chatGUI;
	private ServerSocket chatServer;
	private Socket chatClient;
	public ChattingRoomServer(int port, String userName) throws IOException{
		while(true) {
			//EventQueue.invokeLater(run(userName, null));
			chatGUI = new ChattingRoomGUI(userName,null);
			chatServer = new ServerSocket(port);
			chatClient = chatServer.accept();
			BufferedReader msgIn = new BufferedReader(new InputStreamReader(chatClient.getInputStream()));
			//Scanner scan = new Scanner(chatClient.getInputStream());
			PrintWriter msgOut = new PrintWriter(chatClient.getOutputStream(),true);
			chatGUI.setWriter(msgOut);
			chatGUI.dispalyMessage("hello, this is server\n");
			msgOut.println("Chatting with: " + userName + "...");
			String aline = "";
			do{
				aline = msgIn.readLine();
				if(aline!=null)
					chatGUI.dispalyMessage(aline);
				chatGUI.autoBottom();
				}while(aline!=null);
			chatGUI.setWriter(null);
			msgIn.close();
			msgOut.close();
			chatClient.close();
			chatServer.close();
		}
	}
	/*&public Runnable run(String userName, PrintWriter toClient) {
		chatGUI = new ChattingRoomGUI(userName,toClient);
		return null;
	}*/

	public static void main(String[] args) throws IOException {
		new ChattingRoomServer(5678, "Alice");
		/*EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new ChattingRoomServer(5678, "Alice");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			});		*/	
				
	}
}
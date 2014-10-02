import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.Scanner;
public class ChattingRoomClient {
	private ChattingRoomGUI chatGUI;
	private Socket chatClient;
	public ChattingRoomClient(String serverName, int port, String clientName) throws UnknownHostException, IOException {
		chatClient = new Socket(serverName, port);
		BufferedReader msgIn = new BufferedReader(new InputStreamReader(chatClient.getInputStream()));
		PrintWriter msgOut = new PrintWriter(chatClient.getOutputStream(),true);
		//EventQueue.invokeLater(run(clientName, msgOut));
		chatGUI = new ChattingRoomGUI(clientName,msgOut);
		chatGUI.setLocation(400,0);
		chatGUI.dispalyMessage("hello, this is client\n");
		msgOut.println("Chatting with: " + clientName + "...");
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
	}
	/*public Runnable run(String clientName, PrintWriter toServer) {
		chatGUI = new ChattingRoomGUI(clientName,toServer);
		return null;
	}*/

	public static void main(String[] args) throws UnknownHostException, IOException {
		new ChattingRoomClient("localhost",5678,"Bob");
		/*EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new ChattingRoomClient("localhost",5678,"Bob");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			});		*/			
	}

}

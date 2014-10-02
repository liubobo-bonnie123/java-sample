import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ChattingRoomServer {
	BlockingQueue msgQueue = new ArrayBlockingQueue<String>(100); 
	private ServerSocket chatServer;
	private Socket chatClient;
	private ArrayList<Socket> chatClientList = new ArrayList<Socket>();
	public ChattingRoomServer(int port, String userName) throws IOException{
		while(true) {
			chatServer = new ServerSocket(port);
			while(true){
				chatClient = chatServer.accept();
				chatClientList.add(chatClient);
				new Thread(new ClientThread(chatClient, msgQueue)).start();
				new Thread(new MsgSender(msgQueue,chatClientList)).start();
			}

		}
	}
class ClientThread implements Runnable{
	private Socket chatClient;
	private final BlockingQueue<String> msgQueue;
	public ClientThread(Socket s, BlockingQueue<String> q){
		chatClient = s;
		msgQueue = q;
	}
		
	public void run(){
		
		try {
			BufferedReader msgIn = new BufferedReader(new InputStreamReader(chatClient.getInputStream()));
			PrintWriter msgOut = new PrintWriter(chatClient.getOutputStream(),true);
			msgOut.println("Welcome to Chatting Room! ");
			String aline = getMsg(msgIn);
			while(aline != null){
				msgQueue.put(aline);
				aline = getMsg(msgIn);
				//Thread.sleep(100);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public String getMsg(BufferedReader msgIn) throws IOException{
		String s = msgIn.readLine();
		return s;
	}
}

class MsgSender implements Runnable{
	private final BlockingQueue<String> msgQueue;
	private ArrayList<Socket> chatClientList;
	String str;
	public MsgSender(BlockingQueue<String> q, ArrayList<Socket> l){
		msgQueue = q;
		chatClientList = l;
	}
	
	public void run() {
		while (true){
			while (!msgQueue.isEmpty()){
				
				try {
					str = msgQueue.take();
					System.out.println(str);
					for(Socket s:chatClientList){
						PrintWriter msgOut = new PrintWriter(s.getOutputStream(),true);
						msgOut.println(str);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
					
			}
		}
	}

}

	public static void main(String[] args) throws IOException {
		if (args.length == 0)
			new ChattingRoomServer(5678, "Server");
		else if (args.length == 1)
			new ChattingRoomServer(Integer.parseInt(args[0]), "Server");
		else
		{
			System.out.println("Usage:");
			System.out.println("java ChattingRoomServer [port]");
			System.out.println("java ChattingRoomServer");
		}
				
	}
}
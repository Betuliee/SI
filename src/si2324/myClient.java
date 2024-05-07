package si2324;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.io.FileInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class myClient {
	public static void main (String[] args) throws UnknownHostException, IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		//criar socket
		Socket echoSocket = new Socket ("127.0.0.1", 23456);
		
		//criar stream object
		ObjectInputStream in = new ObjectInputStream(echoSocket.getInputStream());
		ObjectOutputStream out = new ObjectOutputStream(echoSocket.getOutputStream());
		
		// enviar info para servidor
		String user ="MAria";
		String pass= "aaaa"; 
		out.writeObject(user);
		out.writeObject(pass);
		
		// receber info do servidor
		Boolean res = (Boolean)in.readObject();
		System.out.println(res);

		
		File ff = new File ("src/si3.pdf");
		long size= ff.length();
		out.writeLong(size);
		FileInputStream file = new FileInputStream("src/si3.pdf");
		int n;
		byte buffer[] = new byte[1024];
		while ((n = file.read(buffer, 0, 1024)) >0) {
			out.write(buffer,0,n);
		}
		out.close();
		in.close();
		echoSocket.close();
	}
}

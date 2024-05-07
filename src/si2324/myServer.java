package si2324;

import java.io.FileOutputStream;

/***************************************************************************
*   Seguranca Informatica
*
*
***************************************************************************/

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class myServer{

	public static void main(String[] args) {
		System.out.println("servidor: main");
		myServer server = new myServer();
		server.startServer();
	}

	public void startServer (){
		ServerSocket sSoc = null;
        
		try {
			sSoc = new ServerSocket(23456);
		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(-1);
		}
         
		while(true) {
			try {
				Socket inSoc = sSoc.accept();
				ServerThread newServerThread = new ServerThread(inSoc);
				newServerThread.start();
		    }
		    catch (IOException e) {
		        e.printStackTrace();
		    }
		    
		}
		//sSoc.close();
	}


	//Threads utilizadas para comunicacao com os clientes
	class ServerThread extends Thread {

		private Socket socket = null;

		ServerThread(Socket inSoc) {
			socket = inSoc;
			System.out.println("thread do server para cada cliente");
		}
 
		public void run(){
			try {
				ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());

				String user = null;
				String passwd = null;
			
				try {
					user = (String)inStream.readObject();
					passwd = (String)inStream.readObject();
					System.out.println("thread: depois de receber a password e o user");
				}catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				}
 			
				//TODO: refazer
				//este codigo apenas exemplifica a comunicacao entre o cliente e o servidor
				//nao faz qualquer tipo de autenticacao
				if (user.length() != 0){
					outStream.writeObject( (Boolean) true);
				}
				else {
					outStream.writeObject( (Boolean) false);
				}
				
				// receber a dimensão do ficheiro
				// ciclo em que vão receber bytes ate recebem todos os bytes
				long dim = inStream.readLong();
				System.out.print("dim"+ dim);
				long recebidos = 0;
				byte buffer[] = new byte[1024];
				FileOutputStream file = new FileOutputStream("newFile.txt");
				int n;
				while (recebidos < dim) {
					
					n = inStream.read(buffer, 0, (dim-recebidos) > 1024?1024:(int)(dim-recebidos));
					file.write(buffer, 0, n);
					recebidos +=n;
					
				}
//				String strfinal = (String) inStream.readObject();
//				System.out.println(strfinal);
				file.close();
				outStream.close();
				inStream.close();
 			
				socket.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

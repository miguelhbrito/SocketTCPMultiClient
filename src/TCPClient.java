import java.io.*;
import java.net.*;

public class TCPClient {
	public static void main(String[] args) throws Exception {
		Socket s = new Socket("10.13.37.176", 7878);
		if (s.isConnected()) {
			System.out.println("Connected to Server....");
		}
		Ouvindo clientThread = new Ouvindo(s);
		clientThread.start();
		
		while (true) {

			System.out.println("Digite sua mensagem:");
			DataInputStream in = new DataInputStream(System.in);
			String str = in.readLine();
			
			if(str != null && str.equals("Sair")) {
			
				clientThread.terminaConexao = true;
				break;
			}
			DataOutputStream dout = new DataOutputStream(s.getOutputStream());
			dout.writeUTF(str);
			
		}
		
		System.out.println("Fechando conexao");
		s.close();
	}
}

class Ouvindo extends Thread {
	Socket socketListen = null;

	static public boolean terminaConexao = false;
	
	public Ouvindo(Socket socket) {
		socketListen = socket;
	}

	public void run() {
		try {
			while (true) {
				BufferedReader din = new BufferedReader
						(new InputStreamReader(socketListen.getInputStream()));
				
				if(din.ready()) {
					String texto = din.readLine();
					System.out.println("Texto recebido:\t" + texto);
				}
				Thread.sleep(500l);
				
				if(terminaConexao == true) {
					break;
				}
			}
		} catch (IOException e) {
			System.err.println("Falha na conexao de leitura do Buffer:"+  e);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
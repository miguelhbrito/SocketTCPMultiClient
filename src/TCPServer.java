import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class TCPServer {
	public static void main(String[] args) throws Exception {
		System.out.println("Server is running...................");
		ServerSocket serverSocket = new ServerSocket(7878);
		while (true) {
			Socket socketCliente = serverSocket.accept();
			RevThread clientThread = new RevThread(socketCliente);
			clientThread.start();
			System.out.println("client connected" + socketCliente.getInetAddress());
		}
	}
}

class RevThread extends Thread {
	Socket socketClient = null;
	public static List<Socket> lista = new ArrayList<Socket>();

	public RevThread(Socket socket) {
		socketClient = socket;
		lista.add(socket);
	}

	public void run() {
		try {
			while (true) {
				System.out.println("receiving data from client " + socketClient.getInetAddress());
				DataInputStream entradaTexto = new DataInputStream(socketClient.getInputStream());
				String textoRecebido = entradaTexto.readUTF();
				System.out.println("processing data of Client " + socketClient.getInetAddress());
				System.out.println("sending to client " + socketClient.getInetAddress());
				enviaParaTodos(textoRecebido);
				if (textoRecebido != null && textoRecebido.equals("Sair")) {
					break;
				}
			}
			socketClient.close();
		} catch (IOException e) {
			System.out.println(e);
		} finally {
			lista.remove(socketClient);
		}
	}

	public void enviaParaTodos(String texto) throws IOException {

		for (Socket client : lista) {
			if (!client.equals(socketClient)) {
				DataOutputStream textoSaida = new DataOutputStream(client.getOutputStream());
				textoSaida.writeUTF(texto);
			}
		}
	}
}
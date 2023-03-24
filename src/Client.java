import java.net.*;
import java.io.*;
import java.nio.Buffer;
import java.util.Arrays;
import java.util.Scanner;

public class Client {
	Socket requestSocket;
	ObjectOutputStream msgOut;
	ObjectInputStream msgIn;
	DataOutputStream out;         //stream write to the socket
 	DataInputStream in;          //stream read from the socket
	String message;                //message send to the server
	String MESSAGE;                //capitalized message read from the server

	public void Client() {}

	void run(String serverPort)
	{
		try{
			//create a socket to connect to the server
			requestSocket = new Socket("localhost", Integer.parseInt(serverPort));
			System.out.println("Connected to localhost in port 8000");
			//initialize inputStream and outputStream
			out = new DataOutputStream(requestSocket.getOutputStream());
			out.flush();
			in = new DataInputStream(requestSocket.getInputStream());
			msgOut = new ObjectOutputStream(requestSocket.getOutputStream());
			out.flush();
			msgIn = new ObjectInputStream(requestSocket.getInputStream());

			//get Input from standard input
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
			Scanner src = new Scanner(System.in);
			while(true)
			{
				System.out.print("Hello, please input a request: ");
				String input = src.nextLine();
				String[] request = input.split(" ");
				byte[] buffer = new byte[1024];
				File file = new File(request[1]);
				msgOut.writeObject(input);
				msgOut.flush();
				System.out.println("Send message: " + input);
				int byt = 0;
				switch (request[0]) {
					case "upload":
					byt=0;
					FileInputStream fileInputStream = new FileInputStream(file);
					System. out.println("File length: "+file.length());
					out.writeLong(file.length());

					while ((byt = fileInputStream.read(buffer)) != -1) {

						out.write(buffer, 0, byt);
						out.flush();
					}
						fileInputStream.close();
					case "get":
						byt =0;
						FileOutputStream fileOutputStream = new FileOutputStream("new" + request[1]);

						long size = in.readLong(); // read file size
						System.out.println("File length: "+size);
						while (size > 0 && (byt = in.read(buffer, 0,
								(int) Math.min(buffer.length, size)))
								!= -1) {
							// Here we write the file using write method
							fileOutputStream.write(buffer, 0, byt);
							size -= byt; // read upto file size
						}
						fileOutputStream.close();
					// close the file here

				}
			}
		}
		catch (ConnectException e) {
    			System.err.println("Connection refused. You need to initiate a server first.");
		} 

		catch(UnknownHostException unknownHost){
			System.err.println("You are trying to connect to an unknown host!");
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
		finally{
			//Close connections
			try{
				in.close();
				out.close();
				requestSocket.close();
			}
			catch(IOException ioException){
				ioException.printStackTrace();
			}
		}
	}

	//main method
	public static void main(String args[])
	{
		String serverPort, ftpClient;
		while (true) {

			Scanner scanner = new Scanner(System.in);
			String input = scanner.nextLine();
			String[] cmd = input.split(" ");

			if (cmd.length != 2) {
				System.out.println("Enter valid command to start the client socket");
				continue;
			}

			ftpClient = cmd[0];
			serverPort = cmd[1];

			// Break loop in case of valid input to connect to server
			if (ftpClient.equals("ftpclient") && serverPort.equals("8000"))
				break;

			else {
				System.out.println("Enter valid port number - 8000");
				continue;
			}
		}
		Client client = new Client();
		client.run(serverPort);
	}

}

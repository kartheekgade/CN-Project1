import java.net.*;
import java.io.*;

public class Server {
    private static final int sPort = 8000;    //The server will be listening on this port number


    public static void main(String args[]) throws IOException {
        System.out.println("The server is running.");
        ServerSocket listener = new ServerSocket(sPort);
        int clientNum = 1;
        try {
            while (true) {
                new Handler(listener.accept(), clientNum).start();
                System.out.println("Client " + clientNum + " is connected!");
                clientNum++;
            }
        } finally {
            listener.close();
        }
    }

    private static class Handler extends Thread {
        ServerSocket sSocket;   //serversocket used to lisen on port number 8000
        Socket connection = null; //socket for the connection with the client
        String message;    //message received from the client
        String MESSAGE;    //uppercase message send to the client
        DataOutputStream out;  //stream write to the socket
        DataInputStream in;    //stream read from the socket
        ObjectOutputStream msgOut;
        ObjectInputStream msgIn;
        private int no;

        public Handler(Socket connection, int no) {
            this.connection = connection;
            this.no = no;
        }

        public void run() {
            try {
//            //create a serversocket
//            sSocket = new ServerSocket(sPort, 10);
//            //Wait for connection`
//            System.out.println("Waiting for connection");
//            //accept a connection from the client
//            connection = sSocket.accept();
//            System.out.println("Connection received from " + connection.getInetAddress().getHostName());
                //initialize Input and Output streams
                out = new DataOutputStream(connection.getOutputStream());
                out.flush();
                in = new DataInputStream(connection.getInputStream());
                msgOut = new ObjectOutputStream(connection.getOutputStream());
                msgOut.flush();
                msgIn = new ObjectInputStream(connection.getInputStream());

                try {
                    while (true) {
                        message = (String) msgIn.readObject();
                        String[] request = message.split(" ");
                        int bytes = 0;
                        byte[] buffer = new byte[1024];
                        switch (request[0]) {
                            case "upload":
                                bytes = 0;
                                FileOutputStream fileOutputStream = new FileOutputStream("new" + request[0]);
                                long size = in.readLong(); // read file size
                                System.out.println("File length: " + size);

                                while (size > 0 && (bytes = in.read(buffer, 0,
                                        (int) Math.min(buffer.length, size)))
                                        != -1) {
                                    // Here we write the file using write method
                                    fileOutputStream.write(buffer, 0, bytes);
                                    size -= bytes; // read upto file size
                                }
                                // Here we received file
                                System.out.println("File is Received");
                                File file1 = new File("newUploadTestFile.pptx");
                                System.out.println("File recived length: " + file1.length());
                                fileOutputStream.close();
                            case "get":
                                bytes = 0;
                                File file = new File(request[1]);
                                if (file.exists()) {
                                    FileInputStream fileInputStream = new FileInputStream(file);
                                    out.writeLong(file.length());
                                    while ((bytes = fileInputStream.read(buffer)) != -1) {

                                        out.write(buffer, 0, bytes);
                                        out.flush();
                                    }
                                    fileInputStream.close();
                                } else {
                                    out.writeLong(0L);
                                    out.flush();
                                    System.out.println("File not found");
                                    return;
                                }
                        }


                    }
                } catch (Exception e) {
                    System.err.println("Data received in unknown format");
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } finally {
                //Close connections
                try {
                    in.close();
                    out.close();
                    sSocket.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }
    }
}




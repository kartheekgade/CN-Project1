Computer Networks
CNT 5106
Project-1
README
UFID: 58842404
Kartheek Reddy Gade
                                            
FTP client server is implemented using java language with the help of socket connection. The implementation of ftp client-server is described below:


File names: Server.java, Client.java


Connection:
Socket is used to connect client - server


Steps to run:
1. Start the server socket program and wait for the client.
2. Now, start client socket and connect to server using the port                                       Cmd:  ftpclient <port number>
3. After a successful connection, the client requests the service to the server.
4. To upload file to server, use “upload  <filepath>”
5. To download file from server, use “get <filepath>”


Description:
The command entered in the client is sent as a message to the server to know what operations need to be performed at the server side. If we perform an operation that means the client is requesting the server for a specific file. The server will check for the file and break the file into 1KB chunks. By making them into chunks, the server can send them effectively. If any connection is lost in between the client will receive certain chunks. We cannot lose the complete file. The client will receive the same in chunks as we are using TCP, it will only perform the task of sequencing packets. After receiving the file we are able to view the complete file in the folder and save as appended with “new” to the original file name. For upload, the same viceversa. 1KB buffers are used to make the file into chunks.
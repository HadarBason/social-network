# social-network
simple social network server and  client. The communication between the server and the client(s) performed using a binary communication protocol.

How to run the code:
server (JAVA): 2 kindes of servers:
	1) Reactor Server:	
      mvn clean
			mvn package
			cd target
			java -cp spl-net-1.0-SNAPSHOT.jar bgu.spl.net.impl.BGSServer.ReactorMain 3 8888

	2) Thread-Per-Client(TPC) Server:	
      mvn clean
			mvn package
			cd target
			java -cp spl-net-1.0-SNAPSHOT.jar bgu.spl.net.impl.BGSServer.TPCMain 7777

client (C++) :
	BGSclient:	
      make clean
			make
			cd bin
			./BGSclient 127.0.0.1 <port>

The BGS protocol supports various commands needed in order to share posts and messages. 
There are two types of commands, Server-to-Client and Client-to-Server. 
The commands begin with 2 bytes (short) to describe the opcode. 

Client-to-Server messages: 
1- Register request (REGISTER)
2- Login request (LOGIN)
3- Logout request (LOGOUT)
4- Follow / Unfollow request (FOLLOW)
5- Post request (POST)
6- PM privet message request (PM)
7- Logged in States request (LOGSTAT)
8- Stats request (STAT)

Server-to-Client messages: 
9- Notification (NOTIFICATION)
10- Ack (ACK)
11- Error (ERROR)
12- Block (BLOCK)


examples for orders in client: 

REGISTER hadar 1234 07-05-1998

LOGIN hadar 1234 1

LOGOUT

FOLLOW 0 din

FOLLOW 1 din

POST this assignment desserves a 100 !

PM hadar Hi how are you?

LOGSTAT

STAT hadar|din

BLOCK din


Filterd words: src/main/java/bgu/spl/net/srv/ConnectionsImp.java 
               line 15




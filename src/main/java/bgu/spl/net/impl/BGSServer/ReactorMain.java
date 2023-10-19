package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.srv.*;
import bgu.spl.net.srv.Message.Message;

public class ReactorMain {
    public static void main(String[] args) {
        int port = 8888;
        int numOfThreads = 3;
        if (args.length == 2) {
            port = Integer.parseInt(args[1]);
            numOfThreads = Integer.parseInt(args[0]);
        }
        Server.reactor(Integer.parseInt(args[0]),Integer.parseInt(args[1]), BidiMessagingProtocol::new,()->new EncoDeco()).serve();

    }
}

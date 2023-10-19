package bgu.spl.net.impl.BGSServer;
import bgu.spl.net.srv.*;

public class TPCMain {
    public static void main(String[] args) {
        new TPCServer(Integer.valueOf(args[0]),()->new BidiMessagingProtocol<>(),()->new EncoDeco()).serve();
    }
}

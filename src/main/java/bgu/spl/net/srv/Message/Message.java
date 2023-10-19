package bgu.spl.net.srv.Message;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.ConnectionsImp;

public interface Message {

    Message act(int id, ConnectionsImp connections);
}

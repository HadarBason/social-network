package bgu.spl.net.srv.Message;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.ConnectionsImp;
import bgu.spl.net.srv.User;

public class LogOutMessage implements Message{
    short op;
    public LogOutMessage(short op)  {
        this.op = op;
    }

    @Override
    public Message act(int id, ConnectionsImp connections) {
        if(!connections.getIdToHandler().containsKey(id)) //no connection
            return new ErrorMessage(this.op);
        User.Status s = connections.getIdToUser().get(id).getS();
        if(s== User.Status.Online){
            //connections.disconnect(id);
            return new AckMessage(this.op);
        }
        else //user is already offline
            return new ErrorMessage(this.op);
    }
}

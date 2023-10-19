package bgu.spl.net.srv.Message;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.newsfeed.PublishNewsCommand;
import bgu.spl.net.srv.ConnectionsImp;

public class ErrorMessage implements Message {
    private short Rop;
    private String myname;
    private short op;

    public ErrorMessage(short Rop){
        this.myname = "ERROR " + Rop;
        this.Rop = Rop;
        this.op =11;

    }
    public short getOp() {
        return op;
    }
    public short getRop(){
        return Rop;
    }
    @Override
    public Message act(int id, ConnectionsImp connections) {
        return null;
    }

}

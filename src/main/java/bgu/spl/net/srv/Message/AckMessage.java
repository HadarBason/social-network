package bgu.spl.net.srv.Message;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.ConnectionsImp;

public class AckMessage implements Message {
    private short Rop;
    private String optional;
    private short op;
    public AckMessage(short Rop){//
        this.Rop = Rop;
        this.optional= null;
        this.op = 10;

    }
    public AckMessage(short Rop,String optional){
        this.Rop = Rop;
        this.optional = optional;
        this.op = 10;
    }


    public short getOp() {
        return op;
    }
    public short getRop(){
        return Rop;
    }
    public String getOptional(){
        return optional;
    }
    @Override
    public Message act(int id, ConnectionsImp connections) {
        return null;
    }
}

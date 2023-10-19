package bgu.spl.net.srv.Message;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.ConnectionsImp;
import bgu.spl.net.srv.User;

public class LogInMessage implements Message {
    private short op;
    private String Username;
    private String password;
    private  byte captcha;
    public LogInMessage(short op,String Username,String password,byte captcha){
        this.op=op;
        this.Username=Username;
        this.password=password;
        this.captcha=captcha;

    }
    @Override
    public Message act(int id, ConnectionsImp connections) {
        if(!connections.getUserNameToUser().containsKey(Username))//not registered
            return new ErrorMessage(this.op);
        User me =  connections.getUserNameToUser().get(Username);
        connections.updateUserConID(id,me);
        String ps = me.getPassword();
        User.Status s = me.getS();
        if(!this.password.equals(ps) || this.captcha==0 || s == User.Status.Online)
            return new ErrorMessage(this.op);
        me.setS(User.Status.Online);
        return new AckMessage(this.op);
    }
}

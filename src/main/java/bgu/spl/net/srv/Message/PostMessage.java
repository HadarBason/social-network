package bgu.spl.net.srv.Message;

import bgu.spl.net.srv.ConnectionsImp;
import bgu.spl.net.srv.User;

import java.util.HashSet;
import java.util.Set;

public class PostMessage implements Message{
    private short op;
    private String content;
    private Set<User> extra ;
    private User user;

    public PostMessage(short op, String content) {
        this.op = op;
        this.content = content;
        this.user = null;
        this.extra = new HashSet<User>();
    }
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public Message act(int id, ConnectionsImp connections) {
        if (!connections.getIdToUser().containsKey(id))
            return new ErrorMessage(op);
        User postu = connections.getIdToUser().get(id);
        setUser(postu);
        if(postu.getS()== User.Status.Offline)
            return new ErrorMessage(op);
        String tmpMsg = content;
        String tmpUserName;
        if(tmpMsg.contains("@"))
            tmpMsg = tmpMsg.substring(tmpMsg.indexOf("@")+1);
        else tmpMsg = null;
        while(tmpMsg != null){
            tmpUserName = tmpMsg.substring(0,tmpMsg.indexOf(' '));
            if(connections.getUserNameToUser().containsKey(tmpUserName))
                extra.add(connections.getUserNameToUser().get(tmpUserName));
            if(tmpMsg.contains("@"))
                tmpMsg = tmpMsg.substring(tmpMsg.indexOf("@")+1);
            else tmpMsg = null;
        }
        try {
            connections.getMemory().get(user.getUserName()).put(this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        postu.getPosts().add(this);
        NotificationMessage notification = new NotificationMessage(op,(byte) 1,postu," " +content,extra);

        return notification;


    }
}

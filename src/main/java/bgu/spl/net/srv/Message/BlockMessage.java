package bgu.spl.net.srv.Message;

import bgu.spl.net.srv.ConnectionsImp;
import bgu.spl.net.srv.User;

public class BlockMessage implements Message{
    short op;
    String userName;

    public BlockMessage(short op, String userName) {
        this.op = op;
        this.userName = userName;
    }

    @Override
    public Message act(int id, ConnectionsImp connections) {
        User me = connections.getIdToUser().get(id);
        if(!connections.getUserNameToUser().containsKey(userName))
            return new ErrorMessage(op);
        User other =connections.getUserNameToUser().get(userName);
        if (connections.getIdToUser().get(id).getS() == User.Status.Online) {
            if(connections.getIdToUser().get(id).getFollowing().contains(other)){
                connections.getIdToUser().get(id).getFollowing().remove(other);
                other.getFollowers().remove(connections.getIdToUser().get(id));
            }
            if(connections.getIdToUser().get(id).getFollowers().contains(other)){
                connections.getIdToUser().get(id).getFollowers().remove(other);
                other.getFollowing().remove(connections.getIdToUser().get(id));
            }
            other.getBlocking().add(me);
            me.getBlocking().add(other);
            return new AckMessage(op,userName);
        }
        else {
            return new ErrorMessage(op);
        }
    }
}

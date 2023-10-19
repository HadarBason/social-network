package bgu.spl.net.srv.Message;

import bgu.spl.net.srv.ConnectionsImp;
import bgu.spl.net.srv.User;

import java.util.HashMap;

public class LogStatMessage implements Message{
    private short op;

    public LogStatMessage(short op) {
        this.op = op;
    }

    @Override
    public Message act(int id, ConnectionsImp connections) {
        User user = connections.getIdToUser().get(id);
        if(user == null)
            return new ErrorMessage(op);
        if(user.getS()== User.Status.Offline)
            return new ErrorMessage(op);
        String Log ="";
        HashMap<String,User> allUsers = connections.getUserNameToUser();
        for(User tmpUser :allUsers.values()){
            if((!user.getBlocking().contains(tmpUser))&& tmpUser.getS()== User.Status.Online){
                Log = Log + tmpUser.getAge() + " " + tmpUser.getPosts().size() + " " + tmpUser.getFollowers().size() + " " + tmpUser.getFollowing().size() + "\n";
            }
        }
        return new AckMessage(this.op,Log);
    }
}

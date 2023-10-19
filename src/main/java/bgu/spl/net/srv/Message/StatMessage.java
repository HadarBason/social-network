package bgu.spl.net.srv.Message;

import bgu.spl.net.srv.ConnectionsImp;
import bgu.spl.net.srv.User;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class StatMessage implements Message{
    private short op;
    private String ListOfUserNames;

    public StatMessage(short op, String listOfUserNames) {
        this.op = op;
        ListOfUserNames = listOfUserNames;
    }

    @Override
    public Message act(int id, ConnectionsImp connections) {
        User user = connections.getIdToUser().get(id);
        if(user == null)
            return new ErrorMessage(op);
        if(user.getS()== User.Status.Offline)
            return new ErrorMessage(op);
        Set<User> users = new HashSet<User>();
        String tmpList = ListOfUserNames;
        String tmpUser ;
        while(!tmpList.equals("")){ // create the users set
            if(tmpList.contains("|")) {
                tmpUser = tmpList.substring(0, tmpList.indexOf("|"));
                tmpList = tmpList.substring(tmpList.indexOf("|")+1);
            }else {
                tmpUser = tmpList;
                tmpList = "";
            }
            if (connections.getUserNameToUser().containsKey(tmpUser) && !user.getBlocking().contains(tmpUser))
                users.add(connections.getUserNameToUser().get(tmpUser));
        }
        String Log= "";
        for(User u :users){
            Log = Log + u.getAge() + " " + u.getPosts().size() + " " + u.getFollowers().size() + " " + u.getFollowing().size() + "\n";
        }
        return new AckMessage(this.op,Log);
    }
}

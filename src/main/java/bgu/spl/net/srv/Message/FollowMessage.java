package bgu.spl.net.srv.Message;

import bgu.spl.net.srv.ConnectionsImp;
import bgu.spl.net.srv.User;

public class FollowMessage implements Message{

    enum ToDo{ Follow, UnFollow}
    private ToDo follow;
    private String userNameIWantToFollow;
    private short op;
    private byte followOp;

    public FollowMessage(short op,byte follow, String userNameIWantToFollow) {
        this.followOp=follow;
        if(follow == 0)
            this.follow = ToDo.Follow;
        else this.follow = ToDo.UnFollow;
        this.userNameIWantToFollow = userNameIWantToFollow;
        this.op=op;
    }

    @Override
    public Message act(int id, ConnectionsImp connections) {
        if(!connections.getUserNameToUser().containsKey(userNameIWantToFollow))
            return new ErrorMessage(op);
        User iwanttofollow =connections.getUserNameToUser().get(userNameIWantToFollow);
        boolean isfollow = connections.getIdToUser().get(id).getFollowing().contains(iwanttofollow);
        boolean Heblocked = iwanttofollow.getBlocking().contains(connections.getIdToUser().get(id));
        boolean Iblocked = connections.getIdToUser().get(id).getBlocking().contains(iwanttofollow);
        if (connections.getIdToUser().get(id).getS() == User.Status.Online) {
            if (follow == ToDo.Follow && isfollow == false && Heblocked == false && Iblocked == false) {
                connections.getIdToUser().get(id).getFollowing().add(iwanttofollow);
                iwanttofollow.getFollowers().add(connections.getIdToUser().get(id));
                return new AckMessage(this.op, followOp + " " + userNameIWantToFollow);
            }
            else {
                if (isfollow == true && follow == ToDo.UnFollow) {
                    connections.getIdToUser().get(id).getFollowing().remove(iwanttofollow);
                    iwanttofollow.getFollowers().remove(connections.getIdToUser().get(id));
                    return new AckMessage(this.op, followOp + " " + userNameIWantToFollow);
                }
            }
        }
        else {
            return new ErrorMessage(op);
        }
        return new ErrorMessage(op);
    }

}

package bgu.spl.net.srv.Message;

import bgu.spl.net.srv.ConnectionsImp;
import bgu.spl.net.srv.User;

import java.util.Set;

public class NotificationMessage implements Message {
    public enum NotificationType { PM , Public};
    private short op;
    private byte NotifiType;
    private User PostingUser;
    private User ToUser;
    private String Content;
    private NotificationType type;
    private String myName;
    private String date;
    private String time;
    private short Rop;
    private Set<User> extra;

    public NotificationMessage(short Rop, byte notifiType, User postingUser, String content,Set<User> extra) {
        this.Rop = Rop;
        this.NotifiType = notifiType;
        this.PostingUser = postingUser;
        this.Content = content;
        if(notifiType == 0)
            this.type = NotificationType.PM;
        else
            this.type = NotificationType.Public;
        this.myName = "NOTIFICATION " + this.type + " " + postingUser.getUserName();
        this.op =9;
        this.extra = extra;
    }
    public NotificationMessage(short Rop, byte notifiType, User postingUser, String content , User ToUser, String date,String time) {
        this.Rop = Rop;
        this.NotifiType = notifiType;
        this.PostingUser = postingUser;
        this.Content = content;
        if(notifiType == 0)
            this.type = NotificationType.PM;
        else
            this.type = NotificationType.Public;
        this.myName = "NOTIFICATION " + this.type + " " + postingUser.getUserName();
        this.time = time;
        this.date = date;
        this.ToUser = ToUser;
        this.op =9;
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

    public byte getNotifiType() {
        return NotifiType;
    }

    public User getPostingUser() {
        return PostingUser;
    }

    public User getToUser() {
        return ToUser;
    }

    public String getContent() {
        return Content;
    }

    public NotificationType getType() {
        return type;
    }


    public String getMyName() {
        return myName;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public Set<User> getExtra() {
        return extra;
    }
}

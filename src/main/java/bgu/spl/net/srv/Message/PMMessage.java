package bgu.spl.net.srv.Message;

import bgu.spl.net.srv.ConnectionsImp;
import bgu.spl.net.srv.User;

public class PMMessage implements Message{
    private short op;
    private String userName;
    private String content;
    private String date;
    private String time;

    public PMMessage(short op, String userName, String content, String date, String time) {
        this.op = op;
        this.userName = userName;
        this.content = content;
        this.date = date;
        this.time = time;
    }

    @Override
    public Message act(int id, ConnectionsImp connections) {
        if(!connections.getUserNameToUser().containsKey(userName))
            return new ErrorMessage(op);
        User userSendTo =connections.getUserNameToUser().get(userName);
        User sender = connections.getIdToUser().get(id);
        if(sender.getS()== User.Status.Offline)
            return new ErrorMessage(op);
        if(!sender.getFollowing().contains(userSendTo))
            return new ErrorMessage(op);
        String[] notlegal = connections.getNotlegal();
        for(int i = 0 ; i<notlegal.length;i++){
            this.content = this.content.replaceAll(notlegal[i],"<filtered>");
        }
        try {
            connections.getMemory().get(sender.getUserName()).put(this);//todo add pm to user memory
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        NotificationMessage notification = new NotificationMessage(op,(byte) 0,sender," " + content,userSendTo,date,time);
        //connections.send(userSendTo.getConID(),notification);
        return notification;
    }
}

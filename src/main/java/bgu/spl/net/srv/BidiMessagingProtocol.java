package bgu.spl.net.srv;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.Message.AckMessage;
import bgu.spl.net.srv.Message.Message;
import bgu.spl.net.srv.Message.NotificationMessage;

public class BidiMessagingProtocol<T> implements bgu.spl.net.api.bidi.BidiMessagingProtocol<T> {
    private int connectionId;
    private Connections<T> connections;
    private boolean shouldTerminate;


    @Override
    public void start(int connectionId, Connections<T> connections) {
        this.connectionId = connectionId;
        this.connections = connections;
        shouldTerminate = false;
    }
    @Override
    public void process(T message) {
        Message ready = ((Message)message).act(connectionId,(ConnectionsImp) connections);
        User user = ((ConnectionsImp) connections).getIdToUser().get(connectionId);
        if(ready instanceof NotificationMessage ){
            AckMessage ack = new AckMessage(((NotificationMessage)ready).getRop());
            connections.send(connectionId,(T) ack);
            if(((NotificationMessage) ready).getNotifiType()== 1) {//postMessage
                 for(User u : user.getFollowers()) {
                     if (u.getS() == User.Status.Online)
                         connections.send(u.getConID(), (T) ready);
                     else u.addMessage(ready);
                 }
                 if((((NotificationMessage)ready).getExtra())!=null){
                     for(User u : ((NotificationMessage) ready).getExtra()) {
                         if (u.getS() == User.Status.Online)
                             connections.send(u.getConID(), (T) ready);
                         else u.addMessage(ready);
                     }
                 }
            }
            else if (((NotificationMessage) ready).getToUser().getS() == User.Status.Online){ // PM online
                connections.send(((NotificationMessage) ready).getToUser().getConID(),(T)ready);
            }
            else {
                ((NotificationMessage) ready).getToUser().addMessage(ready);
            }
        }
        else {
            connections.send(connectionId, (T) ready);//Ack or Error
            if (ready instanceof AckMessage && ((AckMessage) ready).getRop() == 3) {//logout
                connections.disconnect(connectionId);
            }
            else if (ready instanceof AckMessage && ((AckMessage) ready).getRop() == 2) {//login
                while(!user.getMessageToSend().isEmpty())
                    connections.send(connectionId,(T)user.getMessageToSend().remove());
            }

        }

    }

    @Override
    public boolean shouldTerminate() {
        return false;
    }
    public void terminate(){
        shouldTerminate = true;
    }
}

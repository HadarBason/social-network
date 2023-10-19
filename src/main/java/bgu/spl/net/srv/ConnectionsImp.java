package bgu.spl.net.srv;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.echo.EchoClient;
import bgu.spl.net.srv.Message.Message;
import bgu.spl.net.srv.Message.NotificationMessage;


import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class ConnectionsImp implements Connections {
    private final String [] notlegal = { "kill" , "die" };
    private ConcurrentHashMap<Integer,ConnectionHandler> IdToHandler;
    private HashMap<Integer,User> IdToUser;
    private HashMap<String,User> userNameToUser;
    private HashMap<String,Integer> UsernameToId;
    private AtomicInteger idCount;
    private HashMap<String, LinkedBlockingQueue<Message>> userNameToMemory;
    private static class ConnectionImpHolder{ // thread safe singleton
        private static ConnectionsImp instance = new ConnectionsImp();
    }
    private ConnectionsImp(){ // thread safe singleton
        IdToHandler = new ConcurrentHashMap<Integer,ConnectionHandler>();
        IdToUser = new HashMap<Integer, User>();
        userNameToUser = new HashMap<String,User>();
        UsernameToId = new HashMap<String,Integer>();
        idCount = new AtomicInteger(0);
        userNameToMemory = new HashMap<String,LinkedBlockingQueue<Message>>();

    }
    public static ConnectionsImp getInstance(){ // thread safe singleton
        return ConnectionImpHolder.instance;
    }


    @Override
    public boolean send(int connectionId, Object msg) {
        if(this.getIdToHandler().containsKey(connectionId)) {
            IdToHandler.get(connectionId).send(msg);
            return true;
        }
        return false;
    }

    @Override
    public void broadcast(Object msg) {
        for(ConnectionHandler CH : IdToHandler.values()){
            CH.send(msg);
        }
    }

    @Override
    public void disconnect(int connectionId) {
        IdToUser.get(connectionId).setS(User.Status.Offline);
        IdToUser.remove(connectionId);
        IdToHandler.remove(connectionId);
    }
    public int addConnection (ConnectionHandler CH){
        int id = idCount.getAndIncrement();
        IdToHandler.put(id,CH);
        return id;
    }
    public void addUser(User user) { //צריך לעשות פה טרי וקאטץ נתקן אח"כ
        IdToUser.put(user.getConID(), user);
      //  UsernameToId.put(user.getUserName(), user.getConID());
        userNameToUser.put(user.getUserName(),user);
        userNameToMemory.put(user.getUserName(),new LinkedBlockingQueue<>());
    }
    public void updateUserConID(int conId, User user){
        user.setConID(conId);
        IdToUser.put(conId,user);
    }
    public HashMap<Integer, User> getIdToUser() {
        return IdToUser;
    }

    public HashMap<String, Integer> getUsernameToId() {
        return UsernameToId;
    }

    public String[] getNotlegal() {
        return notlegal;
    }

    public HashMap<String, LinkedBlockingQueue<Message>> getMemory() {
        return userNameToMemory;
    }

    public ConcurrentHashMap<Integer, ConnectionHandler> getIdToHandler() {
        return IdToHandler;
    }

    public HashMap<String, User> getUserNameToUser() {
        return userNameToUser;
    }
}

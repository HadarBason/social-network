package bgu.spl.net.srv.Message;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.ConnectionHandler;
import bgu.spl.net.srv.ConnectionsImp;
import bgu.spl.net.srv.User;

public class RegisterMessage implements Message {
    private short op;
    private String userName;
    private String password;
    private String birthday;

    public RegisterMessage(short op, String userName, String password, String birthday) {
        this.op = op;
        this.userName = userName;
        this.password = password;
        this.birthday = birthday;
    }

    public short getOp() {
        return op;
    }

    public void setOp(short op) {
        this.op = op;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    @Override
    public Message act(int id, ConnectionsImp connections) {
        if(!connections.getIdToHandler().containsKey(id)) // no connection
            return new ErrorMessage(op);
        if (connections.getUserNameToUser().containsKey(userName)) // already registred
            return new ErrorMessage(op);
        String year = birthday.substring(6);
        int age;
        try {
            age = 2022 - Integer.parseInt(year);
        }
        catch (NumberFormatException e) {
            age = 0;
        }
        connections.addUser(new User(id,userName,password,age));
        return new AckMessage(this.op);
    }
}

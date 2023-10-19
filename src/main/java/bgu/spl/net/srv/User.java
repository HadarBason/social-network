package bgu.spl.net.srv;

import bgu.spl.net.srv.Message.Message;
import bgu.spl.net.srv.Message.PostMessage;

import javax.print.attribute.SetOfIntegerSyntax;
import java.util.*;

public class User {
    public enum Status { Online , Offline};
    private int conID;
    private Status s ;
    private String userName;
    private String password;
    private int age;
    private Set<User> followers;
    private Set<User> following;
    private Set<User> blocking;
    private Set<PostMessage> posts;
    private Queue<Message> messageToSend;


    public User(int conID, String userName, String password, int age) {
        this.conID = conID;
        this.userName = userName;
        this.password = password;
        this.age = age;
        this.followers = new HashSet<>();
        this.following = new HashSet<>();
        this.blocking = new HashSet<>();
        this.s = Status.Offline;
        this.posts = new LinkedHashSet<>();
        messageToSend = new LinkedList<Message>();
    }


    public void addMessage(Message message){
        messageToSend.add(message);
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

    public void setAge(int age){
        this.age = age;
    }

    public Status getS() {
        return s;
    }

    public void setS(Status s) {
        this.s = s;
    }

    public int getAge() {
        return age;
    }

    public Set<User> getFollowers() {
        return followers;
    }

    public Set<User> getFollowing() {
        return following;
    }

    public Set<User> getBlocking() {
        return blocking;
    }

    public Set<PostMessage> getPosts() {
        return posts;
    }

    public void setPosts(Set<PostMessage> posts) {
        this.posts = posts;
    }

    public int getConID() {
        return conID;
    }

    public void setConID(int conID) {
        this.conID = conID;
    }

    public Queue<Message> getMessageToSend() {
        return messageToSend;
    }
}

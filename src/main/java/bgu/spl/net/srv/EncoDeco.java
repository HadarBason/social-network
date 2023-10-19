package bgu.spl.net.srv;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.srv.Message.*;
import java.time.LocalTime;
import java.time.LocalDate;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class EncoDeco implements MessageEncoderDecoder<Object> {
    public EncoDeco (){}
    private byte[] bytes = new byte[1<<10];
    private int len = 0;
    private short OPcode;
    String msg;

    @Override
    public Message decodeNextByte(byte nextByte) {
        if (nextByte == ';') {
            OPcode = bytesToShort(bytes);
            msg = popString();
            msg = msg.substring(2);
            if(OPcode == 1){ //RegisterMessage
                String userName = msg.substring(0,msg.indexOf('\0'));
                msg = msg.substring(userName.length()+1);
                String password = msg.substring(0,msg.indexOf('\0'));
                msg = msg.substring(password.length()+1);
                RegisterMessage RM = new RegisterMessage(OPcode,userName,password,msg);
                return RM;
            }
            else if(OPcode == 2){ //LoginMessage
                String userName = msg.substring(0,msg.indexOf('\0'));
                msg = msg.substring(userName.length()+1);
                String password = msg.substring(0,msg.indexOf('\0'));
                msg = msg.substring(password.length()+1);
                byte captcha = Byte.parseByte(msg);
                LogInMessage LM = new LogInMessage(OPcode,userName,password,captcha);
                return LM;
            }
            else if(OPcode == 3){ //LogOutMessage
                LogOutMessage LOM = new LogOutMessage(OPcode);
                return LOM;
            }
            else if(OPcode == 4){ //FollowMessage
                String foll = msg.substring(0,msg.indexOf('\0'));
                byte follow = Byte.parseByte(foll);
                String userName = msg.substring(msg.indexOf('\0')+1);
                FollowMessage FM = new FollowMessage(OPcode,follow,userName);
                return FM;
            }
            else if(OPcode == 5){ //PostMessage
                String content = msg;
                PostMessage PM = new PostMessage(OPcode,content);
                return PM;
            }
            else if(OPcode == 6){ // PM message
                String userName = msg.substring(0,msg.indexOf('\0'));
                msg = msg.substring(userName.length()+1);
                String content = msg;
                String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                String time =LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                PMMessage PMM = new PMMessage(OPcode,userName,content,date, time);
                return PMM;
            }
            else if(OPcode == 7){ //Log stat Message
                LogStatMessage LSM = new LogStatMessage(OPcode);
                return LSM;
            }
            else if(OPcode == 8){ // Stat Message
                String listOfUserNames = msg;
                StatMessage SM = new StatMessage(OPcode,listOfUserNames);
                return SM;
            }
            else if(OPcode == 12){ //
                String userName = msg;
                BlockMessage BM = new BlockMessage(OPcode,userName);
                return BM;
            }
        }

            pushByte(nextByte, this.bytes);
            return null; //not a complete line yet

    }
    @Override
    public byte[] encode(Object message) {
        short op;
        short Rop;
        String optional ;
        String Postuser ;
        String content ;
        byte[] end  = ";".getBytes(StandardCharsets.UTF_8);
        if (message instanceof NotificationMessage) {
            op = ((NotificationMessage) message).getOp();
            byte[] opBytes = shortToBytes(op);
            byte [] notifyBytes = new byte[1];
            notifyBytes[0] =  ((NotificationMessage) message).getNotifiType();
            Postuser = ((NotificationMessage) message).getPostingUser().getUserName();
            byte[] postUserBytes = Postuser.getBytes(StandardCharsets.UTF_8);
            content = ((NotificationMessage) message).getContent();
            byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
            ByteBuffer buffer= ByteBuffer.wrap(new byte[opBytes.length + notifyBytes.length + postUserBytes.length + contentBytes.length +end.length]);
            buffer.put(opBytes);
            buffer.put(notifyBytes);
            buffer.put(postUserBytes);
            buffer.put(contentBytes);
            buffer.put(end);
            return buffer.array();

        } else if (message instanceof AckMessage) {
                op = ((AckMessage) message).getOp();
                byte[] opByts = shortToBytes(op);
                Rop = ((AckMessage) message).getRop();
                byte[] RopBytes = shortToBytes(Rop);
                optional = ((AckMessage) message).getOptional();
                byte[] optionalBytes;
                int optionalSize =0;
                ByteBuffer buffer;
                if(optional != null){
                    optionalBytes = optional.getBytes(StandardCharsets.UTF_8);
                    optionalSize = optionalBytes.length;
                    buffer = ByteBuffer.wrap(new byte[4 + optionalSize + end.length]);
                    buffer.put(opByts);
                    buffer.put(RopBytes);
                    buffer.put(optionalBytes);
                    buffer.put(end);
                }
                else {
                    buffer = ByteBuffer.wrap(new byte[4+end.length]);
                    buffer.put(opByts);
                    buffer.put(RopBytes);
                    buffer.put(end);
                }
                return buffer.array();

        } else if (message instanceof ErrorMessage) {
            op = ((ErrorMessage) message).getOp();
            byte[] opByts = shortToBytes(op);
            Rop = ((ErrorMessage) message).getRop();
            byte[] RopBytes = shortToBytes(Rop);
            ByteBuffer buffer= ByteBuffer.wrap(new byte[4+end.length]);
            buffer.put(opByts);
            buffer.put(RopBytes);
            buffer.put(end);
            return buffer.array();
        }
        return null;
    }

    private void pushByte(byte nextByte, byte[] bytes) {
        if(len >= bytes.length)
            bytes = Arrays.copyOf(bytes,len*2);
        bytes[len]=nextByte;
        len++;
    }

    private String popString() {
        String result = new String(bytes,0,len,StandardCharsets.UTF_8);
        len =0;
        return result;
    }

    //Decode 2 bytes to short
    public short bytesToShort(byte[] byteArr) {
        short result = (short)((byteArr[0] & 0xff) << 8);
        result += (short)(byteArr[1] & 0xff);
        return result;
    }

    //Encode short to 2 bytes
    public byte[] shortToBytes(short num) {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }
}

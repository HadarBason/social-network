#include "../include/connectionHandler.h"
#include "../include/connectionReader.h"
#include "../include/messageType.h"

using namespace std;

ConnectionReader::ConnectionReader(ConnectionHandler *connectionHandler, bool *isLogout, bool *isTerminate)
        : connectionHandler(connectionHandler), isLogout(isLogout), isTerminate(isTerminate)
{}

void ConnectionReader::run()
{
    *isTerminate = false;
    *isLogout = false;
    short opcode;
    short msg_opcode;
    string response;
    string msg_data;

    while ( ! *isTerminate ) // not terminated
    {
        char opcode_buff[2];
        memset(opcode_buff, 0, 2);
        connectionHandler->getBytes(opcode_buff, 2);
        opcode = bytesToShort(opcode_buff);
        cout << "opcode " << opcode <<endl;
        response = "";

        if ( opcode == ACK )
        {
            response = "ACK";
            connectionHandler->getBytes(opcode_buff, 2);
            msg_opcode = bytesToShort(opcode_buff);
            response = response + " " + to_string(msg_opcode);
            msg_data = "";

            if ( msg_opcode == FOLLOW  )
            {
                connectionHandler->getLine(msg_data);
                response = response + " " +  msg_data.substr(0,msg_data.length()-1);
                msg_data = "";
            }

            else if (  msg_opcode == LOGSTAT || msg_opcode == STAT )
            {
                connectionHandler->getLine(msg_data);
                response = response + "\n" +  msg_data.substr(0,msg_data.length()-1);
                msg_data = "";
            }
            else if ( msg_opcode == LOGOUT )
            {
                string s="";
                connectionHandler->getFrameAscii(s,';');
                *isTerminate = true;
            } else{
                string s="";
                connectionHandler->getFrameAscii(s,';');
            }

        }
        else if ( opcode == ERROR )
        {
            response = "ERROR";
            connectionHandler->getBytes(opcode_buff, 2);
            msg_opcode = bytesToShort(opcode_buff);
            response = response + " " + to_string(msg_opcode);
            string s="";
            connectionHandler->getFrameAscii(s,';');
            if ( msg_opcode == LOGOUT )
            {
                *isLogout = false;
            }
        }
        else if ( opcode == NOTIFICATION  )
        {
            msg_data = "";
            response="NOTIFICATION";
            char typeBytes[1];
            connectionHandler->getBytes(typeBytes, 1);
            short type =  (short)((typeBytes[0] & 0xff));
            if(type == 0)
                response = response + " PM";
            else response = response + " PUBLIC";
            connectionHandler->getLine(msg_data);
            response = response + " " +  msg_data.substr(0,msg_data.size()-1);
            msg_data = "";
        }
        else
        {
            cout << "illegal opcode" << endl;
        }


        if ( response != "" )
            cout << response << endl;
    }

    // free allocated memory
    //if ( opcode_buff )
      //  delete[] opcode_buff;
}

// convert data from Bytes to Short
short ConnectionReader::bytesToShort(char *bytesArray)
{
    short result = (short)((bytesArray[0] & 0xff) << 8);
    result += (short)(bytesArray[1] & 0xff);
    return result;
}
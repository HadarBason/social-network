//
// Created by doron on 06/01/2022.
//

#include "../include/connectionHandler.h"
#include "../include/keyboardReader.h"
#include "boost/lexical_cast.hpp"

#include <boost/algorithm/string.hpp>
#include "../include/messageType.h"

using namespace std;

KeyboardReader ::KeyboardReader(ConnectionHandler *connectionHandler, bool *isLogout, bool *isTerminate)
        : connectionHandler(connectionHandler), isLogout(isLogout) , isTerminate(isTerminate)
{}

void KeyboardReader::run(){
    *isTerminate = false;
    *isLogout = false;

    while( ! *isTerminate){
        bool finitolabomba = false;
        while( *isLogout) {
            if (*isTerminate) {
                finitolabomba = true;
                break;
            }
        }
        if (finitolabomba)
            break;
        const short bufsize = 1024;
        char buf[bufsize];
        cout << "wait for your command...:" << endl;
        cin.getline(buf, bufsize);
        string line(buf);
        vector<string> splitWords;

        boost::split(splitWords, line, boost::is_any_of(" "));

        char dataToBytes[2];
        string end = "" ;
        string content = "";

        switch (translate(splitWords[0]))
        {
            case REGISTER:
                shortToBytes(REGISTER, dataToBytes);
                connectionHandler->sendBytes(dataToBytes, 2);
                connectionHandler->sendLine(splitWords[1]);
                connectionHandler->sendLine(splitWords[2]);
                connectionHandler->sendLastLine(splitWords[3]);
                break;

            case LOGIN:
                shortToBytes(LOGIN, dataToBytes);
                connectionHandler->sendBytes(dataToBytes, 2);
                connectionHandler->sendLine(splitWords[1]);
                connectionHandler->sendLine(splitWords[2]);
                connectionHandler->sendLastLine(splitWords[3]);
                break;

            case LOGOUT:
                shortToBytes(LOGOUT, dataToBytes);
                connectionHandler->sendBytes(dataToBytes, 2);
                connectionHandler->sendLastLine(end);
                *isLogout = true;
                break;

            case FOLLOW:
                shortToBytes(FOLLOW, dataToBytes);
                connectionHandler->sendBytes(dataToBytes, 2);
                connectionHandler->sendLine(splitWords[1]);
                connectionHandler->sendLastLine(splitWords[2]);
                break;

            case POST:
                shortToBytes(POST, dataToBytes);
                connectionHandler->sendBytes(dataToBytes, 2);
                for(size_t i =1; i<splitWords.size() -1 ; i++){
                    content = content + splitWords[i] + " ";
                }
                content = content + splitWords[splitWords.size()-1];
                connectionHandler->sendLastLine(content);
                content = "";
                break;

            case PM:
                shortToBytes(PM, dataToBytes);
                connectionHandler->sendBytes(dataToBytes, 2);
                connectionHandler->sendLine(splitWords[1]);
                for(size_t i =2; i<splitWords.size() -1 ; i++){
                    content = content + splitWords[i] + " ";
                }
                content = content + splitWords[splitWords.size()-1];
                connectionHandler->sendLastLine(content);
                content = "";
                break;

            case LOGSTAT:
                shortToBytes(LOGSTAT, dataToBytes);
                connectionHandler->sendBytes(dataToBytes, 2);
                connectionHandler->sendLastLine(end);
                break;

            case STAT:
                shortToBytes(STAT, dataToBytes);
                connectionHandler->sendBytes(dataToBytes, 2);
                connectionHandler->sendLastLine(splitWords[1]);
                break;

            case BLOCK:
                shortToBytes(BLOCK, dataToBytes);
                connectionHandler->sendBytes(dataToBytes, 2);
                connectionHandler->sendLastLine(splitWords[1]);
                break;
            case ACK:
                break;
            case NOTIFICATION:
                break;
            case ERROR:
                break;
            case DUMMY:
                break;
        }
    }
    }

void KeyboardReader::shortToBytes(short num, char* bytesArray)
{
    bytesArray[0] = ((num >> 8) & 0xFF);
    bytesArray[1] = (num & 0xFF);
}
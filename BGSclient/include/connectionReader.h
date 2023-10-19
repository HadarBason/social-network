//
// Created by doron on 06/01/2022.
//

#ifndef C_CLIENTS_CONNECTIONREADER_H
#define C_CLIENTS_CONNECTIONREADER_H

#include <string>
#include <iostream>

using namespace std;

class ConnectionReader
{
private:
    ConnectionHandler *connectionHandler;
    bool *isLogout;
    bool *isTerminate;

public:
    ConnectionReader(ConnectionHandler *connectionHandler, bool *pLogout, bool *pTerminate);

    void run();
    short bytesToShort(char *bytesArray);
};

#endif //C_CLIENTS_CONNECTIONREADER_H

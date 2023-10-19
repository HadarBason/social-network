//
// Created by doron on 06/01/2022.
//

#ifndef C_CLIENTS_KEYBOARDREADER_H
#define C_CLIENTS_KEYBOARDREADER_H

#include <string>
#include <iostream>

using namespace std;

class KeyboardReader
{
private:
    ConnectionHandler *connectionHandler;
    bool *isLogout;
    bool *isTerminate;

public:
    KeyboardReader(ConnectionHandler *connectionHandler, bool *isLogout, bool *isTerminate);
    void run();
    void shortToBytes(short num, char *bytesArray);

};
#endif //C_CLIENTS_KEYBOARDREADER_H

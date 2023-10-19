#include <stdlib.h>
#include "../include/connectionHandler.h"
#include "../include/connectionReader.h"
#include "../include/keyboardReader.h"
#include "thread"


int main (int argc, char *argv[]) {
    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
        return -1;
    }
    std::string host = argv[1];
    short port = atoi(argv[2]);

    ConnectionHandler connectionHandler(host, port);
    if (!connectionHandler.connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }

    bool *isTerminate = new bool;
    bool *isLogout = new bool;

    KeyboardReader keyboardReader(&connectionHandler, isLogout, isTerminate);
    thread keyboardThread(&KeyboardReader::run, &keyboardReader);

    ConnectionReader connectionReader(&connectionHandler, isLogout, isTerminate);
    connectionReader.run();

    keyboardThread.join();

    delete isTerminate;
    return 0;
}
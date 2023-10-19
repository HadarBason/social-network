//
// Created by doron on 06/01/2022.
//

#ifndef C_CLIENTS_MASSEGETYPE_H
#define C_CLIENTS_MASSEGETYPE_H

#include <string>
using namespace std;

enum OpDecode
{
    DUMMY,
    REGISTER,
    LOGIN,
    LOGOUT,
    FOLLOW,
    POST,
    PM,
    LOGSTAT,
    STAT,
    NOTIFICATION,
    ACK,
    ERROR,
    BLOCK
};

inline OpDecode translate(string const& translated)
{
    if (translated == "REGISTER") return REGISTER;
    if (translated == "LOGOUT") return LOGOUT;
    if (translated == "LOGIN") return LOGIN;
    if (translated == "FOLLOW") return FOLLOW;
    if (translated == "POST") return POST;
    if (translated == "STAT") return STAT;
    if (translated == "LOGSTAT") return LOGSTAT;
    if (translated == "PM") return PM;
    if (translated == "NOTIFICATION") return NOTIFICATION;
    if (translated == "ACK") return ACK;
    if (translated == "ERROR") return ERROR;
    if (translated == "BLOCK") return BLOCK;

    return DUMMY;
}

#endif //C_CLIENTS_MASSEGETYPE_H

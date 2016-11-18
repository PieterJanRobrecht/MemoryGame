package Dispatcher;

import Model.User;
import Server.Server;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Pieter-Jan on 18/11/2016.
 */
public interface IDispatcherMethod extends Remote {
    int getDatabaseId(Server server) throws RemoteException;
    String getToken() throws RemoteException;
    int getServerId(User thisUser) throws RemoteException;
}

package Dispatcher;

import Model.User;
import Server.Server;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Pieter-Jan on 18/11/2016.
 */
public interface IDispatcherMethod extends Remote {
    int getDatabasePoort(Server server) throws RemoteException;
    int getServerId(User thisUser) throws RemoteException;
}

package Interfaces;

//import Model.User;
import Threads.Server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

/**
 * Created by Pieter-Jan on 05/11/2016.
 */
public class RegistreerMethod extends UnicastRemoteObject implements IRegistreerMethod {
    private List<Server> serverList;

    public RegistreerMethod(List<Server> serverList) throws RemoteException {
        this.serverList= serverList;
    }

//    public void addClient(User user) throws RemoteException {
//    }
//
//    public Server getServer(int userId)throws RemoteException {
//        //TODO Schrijven van de logica voor het verdelen van de load
//        //Opgelet logica best zoals chat voorbeeld
//        return serverList.get(0);
//    }

    public boolean checkCredentials(String name, String pas) throws RemoteException{

        return true;
    }
}

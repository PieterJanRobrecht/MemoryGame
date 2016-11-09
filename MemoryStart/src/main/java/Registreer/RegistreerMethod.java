package Registreer;

//import Model.User;
import DatabasePackage.Database;
import Threads.Server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

/**
 * Created by Pieter-Jan on 05/11/2016.
 */
public class RegistreerMethod extends UnicastRemoteObject implements IRegistreerMethod {
    private List<Server> serverList;
    private Database database;

    public RegistreerMethod(List<Server> serverList,Database database) throws RemoteException {
        this.serverList= serverList;
        this.database = database;
    }

//    public void addClient(User user) throws RemoteException {
//    }
//
    public int getServer(int userId)throws RemoteException {
        //TODO Schrijven van de logica voor het verdelen van de load
        //Opgelet logica best zoals chat voorbeeld

        int poort = Server.getSERVERPOORT() + serverList.get(0).getServerID();
        return poort;
    }

    public boolean checkCredentials(String name, String pas) throws RemoteException{
        boolean check = database.checkCredentials(name,pas);
        return check;
    }
}

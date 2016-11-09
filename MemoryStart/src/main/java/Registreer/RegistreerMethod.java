package Registreer;

//import Model.User;
import DatabasePackage.Database;
import Model.User;
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

    public User getUser(String userName) throws RemoteException {
        User gebruiker = new User();

        gebruiker.setNaam(userName);
        gebruiker.setId(database.getID(userName));

        Main.MainStart.getUserList().add(gebruiker);

        return gebruiker;
    }

    //    public void addClient(User user) throws RemoteException {
//    }
//
    public int getServer(User user)throws RemoteException {
        //TODO Schrijven van de logica voor het verdelen van de load
        //Opgelet logica best zoals chat voorbeeld
        //Mapping doen van user 1 in server 2, ...

        int poort = Server.getSERVERPOORT() + serverList.get(0).getServerID();
        return poort;
    }

    public boolean checkCredentials(String name, String pas) throws RemoteException{
        boolean check = database.checkCredentials(name,pas);
        return check;
    }
}

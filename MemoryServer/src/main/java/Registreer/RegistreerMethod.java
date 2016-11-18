package Registreer;

import Database.IDatabaseMethod;
import Model.User;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by Pieter-Jan on 05/11/2016.
 */
public class RegistreerMethod extends UnicastRemoteObject implements IRegistreerMethod {
    private IDatabaseMethod database;

    public RegistreerMethod(IDatabaseMethod database) throws RemoteException {
        this.database = database;
    }

    public User getUser(String userName, User user) throws RemoteException {
        User gebruiker = user;

        gebruiker.setNaam(userName);
        gebruiker.setId(database.getID(userName));

        return gebruiker;
    }

    public boolean checkCredentials(String name, String pas) throws RemoteException{
        boolean check = database.checkCredentials(name,pas);
        return check;
    }
}

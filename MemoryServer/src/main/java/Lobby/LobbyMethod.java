package Lobby;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by Pieter-Jan on 05/11/2016.
 */
public class LobbyMethod extends UnicastRemoteObject implements ILobbyMethod {

    public LobbyMethod() throws RemoteException {
    }
}
